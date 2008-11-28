package yajhfc;
/*
 * YAJHFC - Yet another Java Hylafax client
 * Copyright (C) 2005-2006 Jonas Wolz
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

import java.awt.Point;
import java.awt.Rectangle;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import yajhfc.file.FormattedFile.FileFormat;
import yajhfc.phonebook.PBEntryField;
import yajhfc.phonebook.convrules.CompanyRule;
import yajhfc.phonebook.convrules.LocationRule;
import yajhfc.phonebook.convrules.NameRule;
import yajhfc.phonebook.convrules.PBEntryFieldContainer;
import yajhfc.phonebook.convrules.ZIPCodeRule;
import yajhfc.send.SendWinStyle;

public class FaxOptions {
    private static final Logger log = Logger.getLogger(FaxOptions.class.getName());
    
    public FaxTimezone tzone;
    public String host;
    public int port;
    public boolean pasv;
    public String user;
    public String pass;
    
    public final FmtItemList recvfmt;
    public final FmtItemList sentfmt;
    public final FmtItemList sendingfmt;
    
    public String faxViewer;
    public String psViewer;
    public String pdfViewer;
    public boolean viewPDFAsPS = true;
    
    public String notifyAddress;
    public FaxNotification notifyWhen = null;
    public FaxResolution resolution = null;
    public PaperSize paperSize = null;
    
    public int maxTry; 
    public int maxDial;
    public int killTime = 180;
    
    public Rectangle mainWinBounds, phoneWinBounds, customFilterBounds = null;
    public Point sendWinPos, optWinPos;
    public String recvColState, sentColState, sendingColState/*, recvReadState*/;
    public int mainwinLastTab;
    
    public int statusUpdateInterval, tableUpdateInterval;
    public int socketTimeout = 90000;
    
    //public String lastPhonebook;
    
    public String FromFaxNumber;
    public String FromVoiceNumber;
    public String FromName;
    public String FromLocation;
    public String FromCompany;
    public String CustomCover;
    public String FromEMail = "";
    public String FromCountry= "";
    public String FromDepartment= "";
    public String FromGivenName= "";
    public String FromPosition= "";
    public String FromState= "";
    public String FromStreet= "";
    public String FromTitle= "";
    public String FromZIPCode= "";
    public boolean useCover, useCustomCover;
    
    //public FaxIntProperty newFaxAction = Utils.newFaxActions[3];
    public int newFaxAction = Utils.NEWFAX_BEEP | Utils.NEWFAX_TOFRONT;
    public boolean pclBug = false;
    public boolean askPassword = false, askAdminPassword = true;
    public String AdminPassword = "";
    
    public String recvFilter = null, sentFilter = null, sendingFilter = null;
    
    public String defaultCover = null;
    public boolean useCustomDefaultCover = false;
    
    public YajLanguage locale = YajLanguage.SYSTEM_DEFAULT;
    
    // Offset for displayed date values in seconds:
    public int dateOffsetSecs = 0;
    
    public boolean preferRenderedTIFF = false;
    public boolean markFailedJobs = true;
    public boolean showRowNumbers = false;
    public boolean adjustColumnWidths = true;
    
    public String lookAndFeel = LOOKANDFEEL_SYSTEM;
    public static final String LOOKANDFEEL_SYSTEM = "!system!";
    public static final String LOOKANDFEEL_CROSSPLATFORM = "!crossplatform!"; 
    
    public ArrayList<String> phoneBooks = new ArrayList<String>();
    //public int lastSelectedPhonebook = 0;
    
    public boolean useDisconnectedMode = false;
    public String defaultModem = "any";
    public boolean regardingAsUsrKey = true;
    public String lastSavePath = "";
    
    public SendWinStyle sendWinStyle = SendWinStyle.SIMPLIFIED;
    public boolean sendWinIsAdvanced = false;
    public String lastSendWinPath = "";
    public String persistenceMethod = "local";
    public String persistenceConfig = "";
    
    public static final String DEF_TOOLBAR_CONFIG = "Send|---|Show|Delete|---|Refresh|---|Phonebook|---|Resume|Suspend";
    public String toolbarConfig = DEF_TOOLBAR_CONFIG;
    
    public boolean minimizeToTray = true;
    public NameRule phonebookDisplayStyle = NameRule.GIVENNAME_NAME;
    public NameRule coverNameRule = NameRule.TITLE_GIVENNAME_NAME_JOBTITLE;
    public ZIPCodeRule coverZIPCodeRule = ZIPCodeRule.ZIPCODE_LOCATION;
    public LocationRule coverLocationRule = LocationRule.STREET_LOCATION;
    public CompanyRule coverCompanyRule = CompanyRule.DEPARTMENT_COMPANY;
    
    public boolean useJDK16PSBugfix = true;
    public boolean createSingleFileForViewing = false;
    public FileFormat singleFileFormat = FileFormat.PDF;
    public String ghostScriptLocation;
    
    
    // Uncomment for archive support.
//    public boolean showArchive = true;
//    public FmtItemList archiveFmt;
//    public String archiveColState = "";
    
    public FaxOptions() {
        this.host = "";
        this.port = 4559;
        this.user = System.getProperty("user.name");
        this.pass = "";
        this.pasv = true;
        this.tzone = FaxTimezone.LOCAL;
        
        this.recvfmt = new FmtItemList(Utils.recvfmts, Utils.requiredRecvFmts);
        this.recvfmt.add(Utils.recvfmts[0]);  // Y
        this.recvfmt.add(Utils.recvfmts[16]); // s
        this.recvfmt.add(Utils.recvfmts[4]);  // e
        this.recvfmt.add(Utils.recvfmts[6]);  // h
        this.recvfmt.add(Utils.recvfmts[13]); // p
        this.recvfmt.add(Utils.recvfmt_FileName);
        //this.recvFileCol = 2;
        this.recvColState = String.valueOf(this.recvfmt.indexOf(Utils.recvfmt_FileName) + 1); // 0 means: no sorting
        
        this.sentfmt = new FmtItemList(Utils.jobfmts, Utils.requiredSentFmts);
        this.sentfmt.add(Utils.jobfmts[40]); //o
        this.sentfmt.add(Utils.jobfmts[30]); //e 
        this.sentfmt.add(Utils.jobfmts[44]);       
        this.sentfmt.add(Utils.jobfmts[45]);
        this.sentfmt.add(Utils.jobfmt_JobID);
        this.sentfmt.add(Utils.jobfmt_Jobstate);
        this.sentfmt.add(Utils.jobfmts[51]);
        
        this.sendingfmt = new FmtItemList(Utils.jobfmts, Utils.requiredSendingFmts);
        this.sendingfmt.addAll(this.sentfmt);
        
        // Uncomment for archive support.
//        this.archiveFmt = new FmtItemList(ArchiveYajJob.availableFields, new FmtItem[0]);
//        this.archiveFmt.addAll(Arrays.asList(ArchiveYajJob.availableFields)); //TODO: More sane default
        
        this.sentColState = this.sendingColState = "";
        
        String sysname = System.getProperty("os.name");
        if (sysname.startsWith("Windows")) {
            String startCmd = System.getenv("COMSPEC");
            if (startCmd == null) startCmd = "COMMAND";
            startCmd += " /C start \"Viewer\" \"%s\"";
            
            this.psViewer = startCmd;  //"rundll32.exe URL.DLL,FileProtocolHandler \"%s\"";//"gsview32.exe";
            this.pdfViewer = startCmd;
            if (sysname.indexOf("XP") >= 0 || sysname.indexOf("Vista") >= 0) 
                this.faxViewer = "rundll32.exe shimgvw.dll,ImageView_Fullscreen %s";
            else
                this.faxViewer = startCmd; //"rundll32.exe URL.DLL,FileProtocolHandler \"%s\"";//"kodakimg.exe";
            
            this.ghostScriptLocation = "gswin32.exe";
        } else {
            Map<String,String> env = System.getenv();
            if ("true".equals(env.get("KDE_FULL_SESSION"))) {
                this.faxViewer = "kfmclient exec %s";
                this.psViewer = "kfmclient exec %s";
                this.pdfViewer = "kfmclient exec %s";
            } else {
                String gnome = env.get("GNOME_DESKTOP_SESSION_ID");
                if (gnome != null && gnome.length() > 0) {
                    this.faxViewer = "gnome-open %s";
                    this.psViewer = "gnome-open %s";
                    this.pdfViewer = "gnome-open %s";
                } else {
                    this.faxViewer = "kfax %s";
                    this.psViewer = "gv %s";
                    this.pdfViewer = "xpdf %s";
                }
            }
            this.ghostScriptLocation = "gs";
        }
        
        this.resolution = FaxResolution.HIGH;
        this.paperSize = PaperSize.A4;
        this.notifyWhen = FaxNotification.DONE_AND_REQUEUE;
        this.notifyAddress = this.user +  "@localhost"; //+ this.host;
        this.maxTry = 6;
        this.maxDial = 12;  
        
        mainWinBounds = null;
        optWinPos = null;
        sendWinPos = null;
        phoneWinBounds = null;
        mainwinLastTab = 0;
        
        statusUpdateInterval = 2000;
        tableUpdateInterval = 6000;
        //recvReadState = "";
        
        //lastPhonebook = "";
        
        FromFaxNumber = "";
        FromVoiceNumber = "";
        FromName = user;
        FromLocation = "";
        FromCompany = "";
        
        useCover = false;
        useCustomCover = false;
        CustomCover = "";
    }
    
    
    private static final char sep = '|';
    //private static final String sepregex = "\\|";
    
    @SuppressWarnings("unchecked")
    public void storeToFile(File file) {
        Properties p = new Properties();
        java.lang.reflect.Field[] f = FaxOptions.class.getFields();
        
        for (int i = 0; i < f.length; i++) {
            try {
                if (Modifier.isStatic(f[i].getModifiers())) // || Modifier.isFinal(f[i].getModifiers()))
                    continue;
                
                Object val = f[i].get(this);
                if (val == null)
                    continue;
                
                String name = f[i].getName();
                if ((val instanceof String) || (val instanceof Integer) || (val instanceof Boolean))
                    p.setProperty(name, val.toString());
                else if (val instanceof YajLanguage) {
                    p.setProperty(name, ((YajLanguage)val).getLangCode());
                } else/*if (val instanceof MyManualMapObject) 
                    p.setProperty(name, ((MyManualMapObject)val).getKey().toString());
                else*/ if (val instanceof FmtItemList) {
                    p.setProperty(name, ((FmtItemList)val).saveToString());
                } else /*if (val instanceof Vector) {
                    Vector vec = (Vector)val;
                    StringBuilder saveval = new StringBuilder();
                    for (int j = 0; j < vec.size(); j++) 
                        saveval.append(((FmtItem)vec.get(j)).fmt).append(sep);
                    p.setProperty(name, saveval.toString());
                } else*/ if (val instanceof Rectangle) {
                    Rectangle rval = (Rectangle)val;
                    p.setProperty(name, "" + rval.x + sep + rval.y + sep + rval.width + sep + rval.height);
                } else if (val instanceof Point) {
                    Point pval = (Point)val;
                    p.setProperty(name, "" + pval.x + sep + pval.y);
                } else if (val instanceof List) {
                    List lst = (List)val;
                    int idx = 0;
                    for (Object o : lst) {
                        p.setProperty(name + "." + (++idx), (String)o);
                    }
                } else if (val instanceof Enum) {
                    p.setProperty(name, ((Enum)val).name());
                } else {
                    log.log(Level.WARNING, "Unknown field type " + val.getClass().getName());
                }
            } catch (Exception e) {
                log.log(Level.WARNING, "Exception reading field: ", e);
            }
        }
        
        try {
            FileOutputStream filout = new FileOutputStream(file);
            p.store(filout, Utils.AppShortName + " " + Utils.AppVersion + " configuration file");
            filout.close();
        } catch (Exception e) {
            log.log(Level.WARNING, "Couldn't save file '" + file + "': ", e);
        }
    }
    
    private final PBEntryFieldContainer coverFrom = new PBEntryFieldContainer() {
        public String getField(PBEntryField field) {
            switch (field) {
            case Company:
                return FromCompany;
            case Country:
                return FromCountry;
            case Department:
                return FromDepartment;
            case EMailAddress:
                return FromEMail;
            case FaxNumber:
                return FromFaxNumber;
            case GivenName:
                return FromGivenName;
            case Location:
                return FromLocation;
            case Name:
                return FromName;
            case Position:
                return FromPosition;
            case State:
                return FromState;
            case Street:
                return FromStreet;
            case Title:
                return FromTitle;
            case VoiceNumber:
                return FromVoiceNumber;
            case ZIPCode:
                return FromZIPCode;
            default:
                log.warning("Unknown PBEntryField: " + field.name());
                // Fall through intended
            case Comment:
                return "";
            }
        }
        
        public void setField(PBEntryField field, String value) {
            switch (field) {
            case Company:
                FromCompany = value;
                break;
            case Country:
                FromCountry = value;
                break;
            case Department:
                FromDepartment = value;
                break;
            case EMailAddress:
                FromEMail = value;
                break;
            case FaxNumber:
                FromFaxNumber = value;
                break;
            case GivenName:
                FromGivenName = value;
                break;
            case Location:
                FromLocation = value;
                break;
            case Name:
                FromName = value;
                break;
            case Position:
                FromPosition = value;
                break;
            case State:
                FromState = value;
                break;
            case Street:
                FromStreet = value;
                break;
            case Title:
                FromTitle = value;
                break;
            case VoiceNumber:
                FromVoiceNumber = value;
                break;
            case ZIPCode:
                FromZIPCode = value;
                break;
            default:
                log.warning("Unknown PBEntryField: " + field.name());
                // Fall through intended
            case Comment:
                break;
            }
        }
    };
    public PBEntryFieldContainer getCoverFrom() {
        return coverFrom;
    }
    
    @SuppressWarnings("unchecked")
    public void loadFromFile(File file) {
        if (Utils.debugMode) {
            log.info("Loading prefs from " + file);
        }
        Properties p = new Properties();
        //System.err.println(fileName);
        try {
            FileInputStream filin = new FileInputStream(file);
            p.load(filin);
            filin.close();
        } catch (FileNotFoundException e) {
            return; // No file yet
        } catch (IOException e) {
            log.log(Level.WARNING, "Error reading file '" + file + "': " , e);
            return;
        }
        if (Utils.debugMode) {
            log.config("---- BEGIN preferences dump");
            Utils.dumpProperties(p, log, "pass", "AdminPassword");
            log.config("---- END preferences dump");
        }
        // Clear all lists:
        phoneBooks.clear();
        
        Enumeration<?> e = p.propertyNames();
        while (e.hasMoreElements()) {
            String propName = (String)e.nextElement();
            
            // Special case for old "lastPhonebook" property
            if (propName.equals("lastPhonebook")) {
                phoneBooks.add(p.getProperty(propName));
                continue;
            }
            
            try {
                String fName;
                int pntIdx = propName.indexOf('.');
                if (pntIdx >= 0) { //Lists
                    fName = propName.substring(0, pntIdx); // Cut off parts right of point
                } else {
                    fName = propName;
                }
                
                Field f = FaxOptions.class.getField(fName);
                Class<?> fcls = f.getType();
                if (String.class.isAssignableFrom(fcls))
                    f.set(this, p.getProperty(fName));
                else if (Integer.TYPE.isAssignableFrom(fcls))
                    f.setInt(this, Integer.parseInt(p.getProperty(fName)));
                else if (Boolean.TYPE.isAssignableFrom(fcls))
                    f.setBoolean(this, Boolean.parseBoolean(p.getProperty(fName)));
                else if (YajLanguage.class.isAssignableFrom(fcls)) {
                    f.set(this, YajLanguage.languageFromLangCode(p.getProperty(fName)));
                } else /* if (MyManualMapObject.class.isAssignableFrom(fcls)) {
                    MyManualMapObject[] dataarray;
                    
                    if (fName.equals("locale"))
                        dataarray = Utils.AvailableLocales;
                    else {
                        log.log(Level.WARNING, "Unknown MyManualMapObject field: " + fName);
                        continue;
                    }
                    Object res = Utils.findInArray(dataarray, p.getProperty(fName));
                    if (res != null)
                        f.set(this, res);
                    else
                        log.log(Level.WARNING, "Unknown value for MyManualMapObject field " + fName);
                } else */ if (FmtItemList.class.isAssignableFrom(fcls)) {
                    FmtItemList fim = (FmtItemList)f.get(this);
                    fim.loadFromString(p.getProperty(fName));
                } else /*if (Vector.class.isAssignableFrom(fcls)) {
                    String[] fields = Utils.fastSplit(p.getProperty(fName), sep);
                    FmtItem[] dataarray, required;
                    Vector<FmtItem> vecres = new Vector<FmtItem>();
                    
                    if (fName.equals("recvfmt")) {
                        dataarray = Utils.recvfmts;
                        required = Utils.requiredRecvFmts;
                    } else if (fName.equals("sentfmt")) {
                        dataarray = Utils.jobfmts;
                        required = Utils.requiredSentFmts;
                    } else if (fName.equals("sendingfmt")) {
                        dataarray = Utils.jobfmts;
                        required = Utils.requiredSendingFmts;
                    } else {
                        System.err.println("Unknown vector field name: " + fName);
                        continue;
                    }
                    for (int i=0; i < fields.length; i++) {
                        FmtItem res = (FmtItem)Utils.findInArray(dataarray, fields[i]);
                        if (res == null) 
                            System.err.println("FmtItem for " + fields[i] + "not found.");
                        else 
                            if (!vecres.contains(res))
                                vecres.add(res);
                    }
                    
                    Utils.addUniqueToVec(vecres, required);
                    f.set(this, vecres);
                } else */ if (Rectangle.class.isAssignableFrom(fcls)) {
                    String [] v =  Utils.fastSplit(p.getProperty(fName), sep);
                    f.set(this, new Rectangle(Integer.parseInt(v[0]), Integer.parseInt(v[1]), Integer.parseInt(v[2]), Integer.parseInt(v[3])));
                } else if (Point.class.isAssignableFrom(fcls)) {
                    String [] v =  Utils.fastSplit(p.getProperty(fName), sep);
                    f.set(this, new Point(Integer.parseInt(v[0]), Integer.parseInt(v[1])));
                } else if (List.class.isAssignableFrom(fcls)) {
                    List lst = (List)f.get(this);
                    lst.add(p.getProperty(propName));
                } else if (Enum.class.isAssignableFrom(fcls)) {
                    f.set(this, Enum.valueOf((Class<? extends Enum>)fcls, p.getProperty(propName)));
                } else {
                    log.log(Level.WARNING, "Unknown field type " + fcls.getName());
                }
            } catch (Exception e1) {
                log.log(Level.WARNING, "Couldn't load setting for " + propName + ": ", e1);
            }
        }
    }
    
    public static File getDefaultConfigFile() {
        return new File(Utils.getConfigDir(), "settings");
    }
}
