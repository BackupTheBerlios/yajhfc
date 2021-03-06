package yajhfc.file;
/*
 * YAJHFC - Yet another Java Hylafax client
 * Copyright (C) 2006 Jonas Wolz
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;
import java.util.regex.Pattern;

import javax.print.DocFlavor;
import javax.swing.filechooser.FileFilter;

import yajhfc.Utils;
import yajhfc.util.ExampleFileFilter;

public class FormattedFile {
    public final File file;
    public FileFormat format = FileFormat.Unknown;

    public FormattedFile(String fileName, FileFormat format) {
        this(new File(fileName), format);
    }
    
    public FormattedFile(File file, FileFormat format) {
        this.file = file;
        this.format = format;
    }
    
    public FormattedFile(File file) {
        this.file = file;
        try {
            detectFormat();
        } catch (Exception e) {
            format = FileFormat.Unknown;
        }
    }
    
    public void view() throws IOException, UnknownFormatException {
        String execCmd;

        switch (format) {
        case TIFF:
            execCmd = Utils.getFaxOptions().faxViewer;
            break;
        case PostScript:
            execCmd = Utils.getFaxOptions().psViewer;
            break;
        case PDF:
            if (Utils.getFaxOptions().viewPDFAsPS) {
                execCmd = Utils.getFaxOptions().psViewer;
            } else {
                execCmd = Utils.getFaxOptions().pdfViewer;
            }
            break;
        default:
            throw new UnknownFormatException(MessageFormat.format(Utils._("File format {0} not supported."), format.toString()));
        }

        Utils.startViewer(execCmd, file);
    }
    
    public void detectFormat() throws FileNotFoundException, IOException {
        format = detectFileFormat(file.getPath());
    }
    


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Static fields & methods:
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    public static final Map<FileFormat,FileConverter> fileConverters = new EnumMap<FileFormat, FileConverter>(FileFormat.class);
    static {
        fileConverters.put(FileFormat.PostScript, FileConverter.IDENTITY_CONVERTER);
        fileConverters.put(FileFormat.PDF, FileConverter.IDENTITY_CONVERTER);
        fileConverters.put(FileFormat.TIFF, new TIFFLibConverter());
        
        fileConverters.put(FileFormat.PNG, new PrintServiceFileConverter(DocFlavor.URL.PNG));
        fileConverters.put(FileFormat.GIF, new PrintServiceFileConverter(DocFlavor.URL.GIF));
        fileConverters.put(FileFormat.JPEG, new PrintServiceFileConverter(DocFlavor.URL.JPEG));
        
        fileConverters.put(FileFormat.HTML, EditorPaneFileConverter.HTML_CONVERTER);
        // Doesn't work very well
        //fileConverters.put(FileFormat.RTF, new EditorPaneFileConverter("text/rtf"));
        
        loadCustomConverters(Utils.getFaxOptions().customFileConverters);
    }
    
    public static void loadCustomConverters(Map<String,String> converters) {
        for (FileFormat format : FileFormat.values()) {
            FileConverter oldConv = fileConverters.get(format);
            FileConverter defaultConverter;
            if (oldConv instanceof ExternalProcessConverter) {
                ExternalProcessConverter epc = (ExternalProcessConverter)oldConv;
                if (epc.isUserDefined()) {
                    defaultConverter = epc.getInternalConverter();
                } else {
                    defaultConverter = oldConv;
                }
            } else {
                defaultConverter = oldConv;
            }
            // Ignore non-overridable converters
            if (oldConv != null && !oldConv.isOverridable())
                continue;
            
            String customConv = converters.get(format.name());
            if (customConv != null && customConv.length() > 0) {
                fileConverters.put(format, new ExternalProcessConverter(customConv, defaultConverter));
            } else {
                if (oldConv != defaultConverter) {
                    if (defaultConverter == null)
                        fileConverters.remove(format);
                    else
                        fileConverters.put(format, defaultConverter);
                }
            }
        }
        fileConvertersChanged();
    }
    
    //private static final short[] JPEGSignature = { 0xff, 0xd8, 0xff, 0xe0, -1, -1, 'J', 'F', 'I', 'F', 0 };
    private static final short[] JPEGSignature = { 0xff, 0xd8, 0xff }; //, -1, -1, -1, 'J', 'F', 'I', 'F', 0 };

    private static final short[] PNGSignature = { 137,  80,  78,  71,  13,  10,  26,  10 };

    private static final short[] GIFSignature1 = { 'G', 'I', 'F', '8', '9', 'a' };
    private static final short[] GIFSignature2 = { 'G', 'I', 'F', '8', '7', 'a' };

    private static final short[] TIFFSignature1 = { 'M', 'M', 0, 42 };
    private static final short[] TIFFSignature2 = { 'I', 'I', 42, 0 };

    private static final short[] PostScriptSignature = { '%', '!' };

    private static final short[] PDFSignature = { '%', 'P', 'D', 'F', '-' };

    private static final short[] PCLSignature = { 033, 'E', 033 };
    
    private static final short[] XMLSignature = { '<', '?', 'x', 'm', 'l', ' ' };

    private static final String ODTMimeString = "mimetypeapplication/vnd.oasis.opendocument.text";
    private static final short[] ODTSignature;
    static {
        // See http://lists.oasis-open.org/archives/office/200505/msg00006.html
        ODTSignature = new short[30 + ODTMimeString.length()];
        ODTSignature[0] = 'P';
        ODTSignature[1] = 'K';
        ODTSignature[2] = 3;
        ODTSignature[3] = 4;
        for (int i = 4; i < 30; i++) {
            ODTSignature[i] = -1;
        }
        for (int i = 0; i < ODTMimeString.length(); i++) {
            ODTSignature[30+i] = (short)ODTMimeString.charAt(i);
        }
    }
    
    private static final int maxSignatureLen = 4096;

    private static final Pattern FOPattern = Pattern.compile("<[^>]+?xmlns(?::\\w+)?=\"http://www\\.w3\\.org/1999/XSL/Format\"");
    
    public static FileFormat detectFileFormat(String fileName) throws FileNotFoundException, IOException {
        return detectFileFormat(new FileInputStream(fileName));
    }
    
    public static FileFormat detectFileFormat(File file) throws FileNotFoundException, IOException {
        return detectFileFormat(new FileInputStream(file));
    }
    
    /**
     * Detects the file format of the given InputStream and closes it afterwards
     * @param fIn
     * @return
     * @throws IOException
     */
    public static FileFormat detectFileFormat(InputStream fIn) throws IOException {
        try {
            byte[] data = new byte[maxSignatureLen];
            int bytesRead = fIn.read(data);

            if (matchesSignature(data, JPEGSignature))
                return FileFormat.JPEG;

            if (matchesSignature(data, PNGSignature))
                return FileFormat.PNG;

            if (matchesSignature(data, GIFSignature1) || matchesSignature(data, GIFSignature2))
                return FileFormat.GIF;

            if (matchesSignature(data, TIFFSignature1) || matchesSignature(data, TIFFSignature2))
                return FileFormat.TIFF;

            if (matchesSignature(data, PDFSignature))
                return FileFormat.PDF;

            if (matchesSignature(data, PostScriptSignature))
                return FileFormat.PostScript;

            if (matchesSignature(data, PCLSignature))
                return FileFormat.PCL;
            
            if (matchesSignature(data, ODTSignature)) {
                return FileFormat.ODT;
            }
            
            if (matchesSignature(data, XMLSignature)) {
                // Check if there is a namespace definition for FOP
                String startOfFile = new String(data, "UTF-8");
                if (FOPattern.matcher(startOfFile).find()) {
                    return FileFormat.FOP;
                } else {
                    return FileFormat.XML;
                }
            }
            
            String startOfFileLower;
            if (data[0] == (byte)0xef && data[1] == (byte)0xbb && data[2] == (byte)0xbf) { // Byte order mark (utf-8)
                startOfFileLower = new String(data, 3, 35, "UTF-8");
            } else {
                startOfFileLower = new String(data, 0, 32, "ISO-8859-1");
            }
            startOfFileLower = startOfFileLower.trim().toLowerCase();
            if (startOfFileLower.startsWith("<html") || startOfFileLower.startsWith("<!doctype html") ||
                startOfFileLower.startsWith("<head") || startOfFileLower.startsWith("<title")) {
                return FileFormat.HTML;
            }
            
            if (startOfFileLower.startsWith("{\\rtf")) {
                return FileFormat.RTF;
            }
            
            for (int i = 0; i < bytesRead; i++) {
                int b = data[i] & 0xff;

                if (b == 127)
                    return FileFormat.Any;
                if (b < 32) {
                    if (b != 10 && b != 13 && b != 9)
                        return FileFormat.Any;
                }
            }
            return FileFormat.PlainText;
        } finally {
            fIn.close();
        }
    }   
    
    /**
     * Checks if the first bytes of data equal those given in signature.
     * @param raf The file to check
     * @param signature The signature to check against. A value of -1 for a byte means "don't care".
     * @return true if the signature matches.
     * @throws IOException
     */
    private static boolean matchesSignature(byte[] data, short[] signature){       
        for (int i = 0; i < signature.length; i++) {
            short s = signature[i];
            if (s >= 0 && s <= 255) {
                int b = data[i] & 0xff; // If data[i] is negative this ensures we get the corresponding positive byte value 
                
                if (b != s)
                    return false;
            }
        }
        return true;
    }

    /**
     * Returns true if the given format can be viewed without conversion.
     * @param format
     * @return
     */
    public static boolean canViewFormat(FileFormat format) {
        switch (format) {
        case TIFF:
        case PostScript:
        case PDF:
            return true;
        default:
            return false;
        }
    }
    
    
//  protected static final FileFormat[] acceptedFormats = {
//  FileFormat.PostScript, FileFormat.PDF, FileFormat.JPEG, FileFormat.GIF, FileFormat.PNG, FileFormat.TIFF
//  };
    protected static FileFilter[] acceptedFilters;

    public static FileFilter[] createFileFiltersFromFormats(Collection<FileFormat> formats) {
        ExampleFileFilter allSupported = new ExampleFileFilter((String)null, Utils._("All supported file formats"));
        allSupported.setExtensionListInDescription(false);
        
        FileFilter[] filters = new FileFilter[formats.size() + 1];
        filters[0] = allSupported;
        
        int i = 0;
        for (FileFormat ff : formats) {
            for (String ext : ff.getPossibleExtensions()) {
                allSupported.addExtension(ext);
            }
            filters[++i] = new ExampleFileFilter(ff.getPossibleExtensions(), ff.getDescription());
        }
        
        return filters;
    }
    
    public static FileFilter[] getConvertableFileFilters() {
        if (acceptedFilters == null) {
            acceptedFilters = createFileFiltersFromFormats(fileConverters.keySet());
        }
        return acceptedFilters;
    }

    /**
     * Called to notify this class that the file converter map changed and
     * any information computed from that map should be refreshed
     */
    public static void fileConvertersChanged() {
        acceptedFilters = null;
    }
    
    public static void main(String[] args) throws Exception {
        for (String file : args) {
            System.out.println(file + ": " + detectFileFormat(file));
        }
    }
}
