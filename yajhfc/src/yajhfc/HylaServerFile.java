package yajhfc;
/*
 * YAJHFC - Yet another Java Hylafax client
 * Copyright (C) 2005 Jonas Wolz
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

import gnu.hylafax.HylaFAXClient;
import gnu.inet.ftp.ServerResponseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import yajhfc.file.FileFormat;
import yajhfc.file.FormattedFile;

public class HylaServerFile {
    protected String path;
    protected FileFormat type;
    protected FormattedFile previewFile = null;
    
    public String getPath() {
        return path;
    }
    
    public FileFormat getType() {
        return type;
    }
    
    /**
     * Copy this file's content into the specified stream.
     * @param hyfc
     * @param target
     * @throws IOException
     * @throws ServerResponseException
     */
    public void downloadToStream(HylaFAXClient hyfc, OutputStream target) throws IOException, ServerResponseException {
        synchronized (hyfc) {
            hyfc.type(gnu.inet.ftp.FtpClientProtocol.TYPE_IMAGE);
            hyfc.get(path, target);
        }
    }
    
    /**
     * Download this file to the specified local file.
     * The default implementation just opens a FileOutputStream and then calls downloadToStream()
     * @param hyfc
     * @param target
     * @throws IOException
     * @throws FileNotFoundException
     * @throws ServerResponseException
     */
    public void download(HylaFAXClient hyfc, File target) throws IOException, FileNotFoundException, ServerResponseException {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(target);
            downloadToStream(hyfc, out);
        } finally {
            if (out != null)
                out.close();
        }
    }
    
    public String getDefaultExtension() {
        if (type == FileFormat.Unknown) {
            int pos = path.lastIndexOf('.');
            if (pos < 0)
                return "tmp";
            else
                return path.substring(pos+1);
        } else
            return type.getDefaultExtension();
    }
    
    /**
     * Returns a (temporary) file with the contents of this server file.
     * The default implementation calls download() to copy the contents into a new temporary file
     * @param hyfc
     * @return
     * @throws IOException
     * @throws ServerResponseException
     */
    public FormattedFile getPreviewFile(HylaFAXClient hyfc) throws IOException, ServerResponseException {
        if (previewFile == null) {
            File tmpFile = File.createTempFile("fax", "." + getDefaultExtension());
            tmpFile.deleteOnExit();
            
            download(hyfc, tmpFile);     
            if (type == FileFormat.Unknown) { // Try to autodetect
                type = FormattedFile.detectFileFormat(tmpFile);
            }
            previewFile = new FormattedFile(tmpFile, type);
        }
        return previewFile;
    }
    
    @Override
    public int hashCode() {
        return path.hashCode();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof HylaServerFile))
            return false;
        
        return path.equals(((HylaServerFile)obj).path);
    }
    
    @Override
    public String toString() {
        return path;
    }
    
    public HylaServerFile(String path, FileFormat type) {
        this.path = path;
        this.type = type;
    }
}

