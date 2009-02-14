#ifndef VERSION
 #define public VERSION "0.3.7"
#endif
#ifndef FOPVersion
  #define public FOPVersion "0.1"
#endif
#ifndef WITHFOP
  #define APPNAME "YajHFC"
  #define APPVERNAME APPNAME + " " + VERSION
#else
  #define APPNAME "YajHFC and FOPPlugin"
  #define APPVERNAME "YajHFC " + VERSION + " and FOPPlugin " + FOPVersion
  #define FOPDIR "..\..\..\..\build\fop-0.95"
#endif

[Files]
Source: ..\..\..\workspace\yajhfc\build\yajhfc.jar; DestDir: {app}; Components: base
Source: ..\..\..\workspace\yajhfc\doc\faq_es.pdf; DestDir: {app}; Components: docs
Source: ..\..\..\workspace\yajhfc\doc\faq_de.pdf; DestDir: {app}; Components: docs
Source: ..\..\..\workspace\yajhfc\doc\faq_fr.pdf; DestDir: {app}; Components: docs
Source: ..\..\..\workspace\yajhfc\doc\faq.pdf; DestDir: {app}; Components: docs
Source: ..\..\..\workspace\yajhfc\doc\faq_ru.pdf; DestDir: {app}; Components: docs
Source: ..\..\..\workspace\yajhfc\doc\faq_tr.pdf; DestDir: {app}; Components: docs

Source: ..\icons\yajhfc.ico; DestDir: {app}; Components: base
Source: execyajhfc.vbs; DestDir: {app}; Components: base
; Redmon: Common files (docs):
Source: printlaunch.vbs; DestDir: {app}; Components: redmon
Source: redmon\LICENCE; DestDir: {app}\redmon; Components: redmon
Source: redmon\README.TXT; DestDir: {app}\redmon; Components: redmon
Source: redmon\redmon.hlp; DestDir: {app}\redmon; Components: redmon
; Redmon: 32bit files
Source: redmon\enum.exe; DestDir: {app}\redmon; Components: redmon; Check: not IsWin64
Source: redmon\FILE_ID.DIZ; DestDir: {app}\redmon; Components: redmon; Check: not IsWin64
Source: redmon\redfile.exe; DestDir: {app}\redmon; Components: redmon; Check: not IsWin64
Source: redmon\redmon35.dll; DestDir: {app}\redmon; Components: redmon; Check: not IsWin64
Source: redmon\redmon95.dll; DestDir: {app}\redmon; Components: redmon; Check: not IsWin64
Source: redmon\redmonnt.dll; DestDir: {app}\redmon; Components: redmon; Check: not IsWin64
Source: redmon\redpr.exe; DestDir: {app}\redmon; Components: redmon; Check: not IsWin64
Source: redmon\redrun.exe; DestDir: {app}\redmon; Components: redmon; Check: not IsWin64
Source: redmon\setup.exe; DestDir: {app}\redmon; Components: redmon; Check: not IsWin64
Source: redmon\src.zip; DestDir: {app}\redmon; Components: redmon; Check: not IsWin64
Source: redmon\unredmon.exe; DestDir: {app}\redmon; Components: redmon; Check: not IsWin64
; Redmon: 64bit files
Source: redmon64\enum.exe; DestDir: {app}\redmon; Components: redmon; Check: IsWin64
Source: redmon64\FILE_ID.DIZ; DestDir: {app}\redmon; Components: redmon; Check: IsWin64
Source: redmon64\redfile.exe; DestDir: {app}\redmon; Components: redmon; Check: IsWin64
Source: redmon64\redmonnt.dll; DestDir: {app}\redmon; Components: redmon; Check: IsWin64
Source: redmon64\redpr.exe; DestDir: {app}\redmon; Components: redmon; Check: IsWin64
Source: redmon64\redrun.exe; DestDir: {app}\redmon; Components: redmon; Check: IsWin64
Source: redmon64\setup.exe; DestDir: {app}\redmon; Components: redmon; Check: IsWin64
Source: redmon64\src64.zip; DestDir: {app}\redmon; Components: redmon; Check: IsWin64
Source: redmon64\unredmon.exe; DestDir: {app}\redmon; Components: redmon; Check: IsWin64

Source: ..\..\..\workspace\yajhfc\COPYING; DestDir: {app}; Components: base
Source: ..\..\..\workspace\yajhfc\README.txt; DestDir: {app}; Components: base
Source: ..\..\..\workspace\yajhfc\README_de.txt; DestDir: {app}; Components: docs
Source: ..\..\..\workspace\yajhfc\README_es.txt; DestDir: {app}; Components: docs
Source: ..\..\..\workspace\yajhfc\README_fr.txt; DestDir: {app}; Components: docs
Source: ..\..\..\workspace\yajhfc\README_ru.txt; DestDir: {app}; Components: docs
Source: ..\..\..\workspace\yajhfc\README_tr.txt; DestDir: {app}; Components: docs
Source: w98info.txt; DestDir: {app}; OnlyBelowVersion: 0,5.0; Components: redmon; Tasks: 
Source: ..\cover\Coverpage example.html; DestDir: {app}\examples; Components: docs
#ifndef WITHFOP
Source: ..\cover\SomeLogo.png; DestDir: {app}\examples; Components: docs
#else
Source: ..\..\..\workspace\FOPPlugin\build\FOPPlugin.jar; DestDir: {app}; Components: base
Source: ..\..\..\workspace\FOPPlugin\dist\readme.odt; DestDir: {app}; Components: docs
Source: ..\..\..\workspace\FOPPlugin\dist\readme.pdf; DestDir: {app}; Components: base
Source: ..\..\..\workspace\FOPPlugin\dist\yajhfc-fo.cmd; DestDir: {app}; Components: base
Source: ..\..\..\workspace\FOPPlugin\dist\examples\cover.odt; DestDir: {app}\examples; Components: docs
Source: ..\..\..\workspace\FOPPlugin\dist\examples\SomeLogo.png; DestDir: {app}\examples; Components: docs
Source: ..\..\..\workspace\FOPPlugin\dist\examples\SomeLogoScaled.png; DestDir: {app}\examples; Components: docs
Source: {#FOPDIR}\build\fop.jar; DestDir: {app}\lib; Components: base

  #define FindHandle
  #define FindResult
  #define ClassPath ""

  #sub ProcessFoundFile
    #define FileName FindGetFileName(FindHandle)
Source: {#FOPDIR}\lib\{#FileName}; DestDir: {app}\lib; Components: base
	#define public ClassPath = ClassPath + "{app}\lib\" + FileName + ";"
  #endsub

  #for {FindHandle = FindResult = FindFirst(FOPDIR + "\lib\*.jar", 0); FindResult; FindResult = FindNext(FindHandle)} ProcessFoundFile
  #if FindHandle
    #expr FindClose(FindHandle)
  #endif

  #define ClassPath = ClassPath + "{app}\lib\fop.jar"
#endif


Source: c:\programme\istool\isxdl.dll; DestDir: {tmp}; Flags: dontcopy
[Setup]
AppCopyright=© 2005-2008 by Jonas Wolz
AppName={#APPNAME}
AppVerName={#APPVERNAME}
InfoBeforeFile=..\..\..\workspace\yajhfc\README.txt
LicenseFile=..\..\..\workspace\yajhfc\COPYING
DefaultDirName={pf}\YajHFC
DefaultGroupName=YajHFC
AppPublisher=Jonas Wolz
AppPublisherURL=http://yajhfc.berlios.de/
AppVersion={#APPVERNAME}
AppID={{2B5B4C28-0B7E-45C8-AF23-9A1816E70911}
UninstallDisplayIcon={app}\yajhfc.ico
UninstallDisplayName={#APPVERNAME}
#ifdef WITHFOP
OutputBaseFilename=Setup-FOPPlugin
#endif
ArchitecturesInstallIn64BitMode=x64

#ifndef WITHFOP
  #define public LaunchArgs="-jar """"{app}\yajhfc.jar"""""
; #define public LaunchEXE="javaw.exe"
; #define public LaunchEXEArgs=LaunchArgs
 #define public LaunchEXEArgs=""
#else
 #define public LaunchArgs="-cp """"" + ClassPath + ";{app}\yajhfc.jar"""" yajhfc.Launcher --loadplugin=""""{app}\FOPPlugin.jar"""""
; #define public LaunchEXE="{app}\yajhfc-fo.cmd"
; #define public LaunchEXEArgs=""
 #define public LaunchEXEArgs="--loadplugin=FOPPlugin.jar"
#endif

#define public LaunchEXE="{app}\execyajhfc.vbs"

[Icons]
Name: {group}\{cm:YajHFCName}; Filename: {#LaunchEXE}; Parameters: {#LaunchEXEArgs}; WorkingDir: {app}; IconFilename: {app}\yajhfc.ico; IconIndex: 0; Components: base
Name: {group}\{cm:YajHFCName} ({cm:debugmode}); Filename: {#LaunchEXE}; Parameters: {#LaunchEXEArgs} --debug --logfile=:prompt:; WorkingDir: {app}; IconFilename: {app}\yajhfc.ico; IconIndex: 0; Components: base
Name: {commondesktop}\{cm:YajHFCName}; Filename: {#LaunchEXE}; Parameters: {#LaunchEXEArgs}; IconFilename: {app}\yajhfc.ico; IconIndex: 0; WorkingDir: {app}; Tasks: DesktopIcon
#ifdef WITHFOP
Name: {group}\FOPPlugin README; Filename: {app}\readme.pdf
#endif
Name: {group}\Homepage; Filename: http://yajhfc.berlios.de/; Components: base
Name: {group}\FAQ; Filename: {app}\faq.pdf; Components: docs
Name: {group}\FAQ ({cm:French}); Filename: {app}\faq_fr.pdf; Components: docs
Name: {group}\FAQ ({cm:German}); Filename: {app}\faq_de.pdf; Components: docs
Name: {group}\FAQ ({cm:Russian}); Filename: {app}\faq_ru.pdf; Components: docs
Name: {group}\FAQ ({cm:Spanish}); Filename: {app}\faq_es.pdf; Components: docs
Name: {group}\FAQ ({cm:Turkish}); Filename: {app}\faq_tr.pdf; Components: docs

[Registry]
Root: HKLM; Subkey: Software\YajHFC; ValueType: string; ValueName: instpath; ValueData: {app}; Flags: uninsdeletekey
Root: HKLM; Subkey: Software\YajHFC; ValueType: string; ValueName: jarfile; ValueData: yajhfc.jar; Flags: uninsdeletekey
Root: HKLM; Subkey: Software\YajHFC; ValueType: string; ValueName: printlaunchparams; ValueData: "{#LaunchArgs} --stdin --background"; Flags: uninsdeletekey; Components: redmon
Root: HKLM; Subkey: SYSTEM\CurrentControlSet\Control\Print\Monitors\{cm:redmonname}\Ports\YAJHFC:; Flags: uninsdeletekey dontcreatekey; Components: redmon

[Components]
Name: Base; Description: {cm:CoreApplicationFiles}; Flags: fixed; Types: custom compact full
Name: Docs; Description: {cm:Documentation}; Types: full
Name: Redmon; Description: {cm:InstallFaxPrinter}; Types: full
[Run]
Filename: {app}\redmon\setup.exe; WorkingDir: {app}\redmon; StatusMsg: {cm:InstallingX,RedMon}; Components: redmon; Check: RedmonNotInstalled; Parameters: /Q
Filename: rundll32; Parameters: "printui.dll,PrintUIEntry /if /b ""{cm:printername}"" /f {win}\inf\ntprint.inf /r ""yajhfc:"" /m ""Apple LaserWriter 16/600 PS"""; Components: redmon; StatusMsg: {cm:InstallingX,{cm:faxprinter}}; Check: NoYajHFCPrinter; BeforeInstall: InstallYajHFCPort(); MinVersion: 0,5.0; OnlyBelowVersion: 0,6.0
Filename: rundll32; Parameters: "printui.dll,PrintUIEntry /if /b ""{cm:printername}"" /f {win}\inf\ntprint.inf /r ""yajhfc:"" /m ""FX DP 305-AP PS"""; Components: redmon; StatusMsg: {cm:InstallingX,{cm:faxprinter}}; Check: NoYajHFCPrinter; BeforeInstall: InstallYajHFCPort(); MinVersion: 0,6.0; Tasks: 
Filename: {app}\w98info.txt; Flags: shellexec; OnlyBelowVersion: 0,5.0; Components: redmon
Filename: {code:GSSetupPath}; Check: InstallGS; Parameters: -auto
Filename: {code:TIFFSetupPath}; Check: InstallTIFF; Parameters: /SILENT
[UninstallRun]
Filename: rundll32; Components: redmon; Parameters: "printui.dll,PrintUIEntry /dl /n ""{cm:printername}"""; RunOnceId: DeletePrinter; MinVersion: 0,5.0
[INI]
Filename: {group}\Homepage.url; Section: InternetShortcut; Key: URL; String: http://yajhfc.berlios.de/
[UninstallDelete]
Type: files; Name: {group}\Homepage.url
Type: files; Name: {app}\settings.default
[CustomMessages]
; Do not translate redmonname
redmonname=Redirected Port

; May be translated
printername=YajHFC Fax
InstallFaxPrinter=Install a fax printer
InstallingX=Installing %1...
FaxPrinter=fax printer
CoreApplicationFiles=Core application files
Documentation=Documentation
YajHFCName=YajHFC fax client
DebugMode=debug mode

; Native names for the languages (so do not translate them either):
; "Deutsch" is fully ASCII, so no need for two versions
German=Deutsch
; "English" too
English=English
; Language names for cp 1252
Spanish=Español
French=Français
Turkish=Türkçe
Russian=Russkij
; Language names for cp 1251
ru.Spanish=Espanol
ru.French=Francais
ru.Turkish=Turkce
ru.Russian=Ðóññêèé
[Tasks]
Name: DesktopIcon; Description: {cm:CreateDesktopIcon}; Components: base
Name: GhostScript; Description: Download and install GhostScript; Components: base; Flags: checkedonce
Name: tiff2pdf; Description: Download and install tiff2pdf; Components: base; Flags: checkedonce
[Languages]
Name: en; MessagesFile: compiler:Default.isl
Name: de; MessagesFile: compiler:Languages\German.isl
Name: fr; MessagesFile: compiler:Languages\French.isl
Name: es; MessagesFile: compiler:Languages\Spanish.isl
Name: it; MessagesFile: compiler:Languages\Italian.isl
Name: ru; MessagesFile: compiler:Languages\Russian.isl
Name: tr; MessagesFile: compiler:Languages\Turkish.isl
[Code]
var bInstallGS: boolean;
    bInstallTIFF: boolean;
    sGSSetupPath: string;
    sTIFFSetupPath:string;
    preserveDownload: integer;
const
    ghostscript32path = 'http://downloads.sourceforge.net/sourceforge/ghostscript/gs863w32.exe';
    ghostscript64path = 'http://downloads.sourceforge.net/sourceforge/ghostscript/gs863w64.exe';
    ghostscriptdllkey = 'SOFTWARE\GPL Ghostscript\8.63';
    tiffkey = 'SOFTWARE\GnuWin32\Tiff';
    tiffpath = 'http://downloads.sourceforge.net/sourceforge/gnuwin32/tiff-3.8.2-1.exe';
    setupsig = '# Auto-generated by YajHFC setup. Remove this line if you manually edit this file, else it will get overwritten on a update';

function InitializeSetup(): Boolean;
var javaver: string;
    javamajor, javaminor, p: integer;
begin
  bInstallGS := false;
  bInstallTIFF := false;
  preserveDownload := -1;

	result := false;
	// Check for an JRE with the right version
	if RegQueryStringValue(HKEY_LOCAL_MACHINE, 'SOFTWARE\JavaSoft\Java Runtime Environment', 'CurrentVersion', javaver) then
	begin
		Log('Java version ' + javaver + ' found.');
		p := Pos('.', javaver);
		if (p > 0) then
		begin
			javamajor := strtointdef(copy(javaver, 1, p-1), 0);
			javaminor := strtointdef(copy(javaver, p+1, length(javaver)-p), 0);
			result := (javamajor > 1) or ((javamajor = 1) and (javaminor >= 5))
		end;
	end else
		Log('No Java found...');
	if (not result) then
	begin
		case MsgBox(
		  'YajHFC needs an installed Java Runtime Environment version 1.5 or higher.'#10#13
		  'Please download and install a Java Runtime Environment from http://www.java.com/ or http://java.sun.com/ and rerun Setup.'#10#13
		  'Do you want to download Java now?'#10#13#10#13
		  '"Yes" opens http://www.java.com/ in a web browser and cancels the setup.'#10#13
		  '"No" continues with the setup anyway (you will NOT be able to run YajHFC until a suitable Java version is installed, however!).'#10#13
		  '"Cancel" aborts the installation.',
		  mbError, MB_YESNOCANCEL) of
		IDYES:
			ShellExec('open', 'http://www.java.com/', '', '', SW_SHOW, ewNoWait, p);
		IDNO:
			result := true;
		// else (IDCANCEL) do nothing
		end;
	end;
end;

function RedmonNotInstalled(): Boolean;
begin
	result := not RegKeyExists(HKEY_LOCAL_MACHINE, 'SYSTEM\CurrentControlSet\Control\Print\Monitors\' + CustomMessage('redmonname'));
end;

function NoYajHFCPrinter(): Boolean;
begin
	result := not RegKeyExists(HKEY_LOCAL_MACHINE, 'SYSTEM\CurrentControlSet\Control\Print\Printers\' + CustomMessage('printername'));
end;

procedure InstallYajHFCPort();
var redmonkey, portkey: string;
begin
	redmonkey := 'SYSTEM\CurrentControlSet\Control\Print\Monitors\' + CustomMessage('redmonname');
	if not RegKeyExists(HKEY_LOCAL_MACHINE, redmonkey) then
	begin
		MsgBox('Could not create a redirected port for YajHFC (Redmon not correctly installed).', mbError, MB_OK);
		exit;
	end;

	portkey := redmonkey + '\Ports\YAJHFC:';
	if RegKeyExists(HKEY_LOCAL_MACHINE, portkey) then
		Log('YAJHFC: port seems to already exist. Continuing anyway...'); // Port already created ??

	RegWriteStringValue(HKEY_LOCAL_MACHINE, portkey, 'Description', 'YajHFC Redirected Port');
	RegWriteStringValue(HKEY_LOCAL_MACHINE, portkey, 'Command', 'cscript.exe');
	RegWriteStringValue(HKEY_LOCAL_MACHINE, portkey, 'Arguments', '"' + ExpandConstant('{app}\printlaunch.vbs') + '"');
	RegWriteStringValue(HKEY_LOCAL_MACHINE, portkey, 'Printer', '');
	RegWriteStringValue(HKEY_LOCAL_MACHINE, portkey, 'LogFileName', ExpandConstant('{app}\yajhfc-redmon.log'));
	RegWriteDWordValue(HKEY_LOCAL_MACHINE, portkey, 'Output', 0);
	RegWriteDWordValue(HKEY_LOCAL_MACHINE, portkey, 'ShowWindow', 1);
	RegWriteDWordValue(HKEY_LOCAL_MACHINE, portkey, 'RunUser', 1);
	RegWriteDWordValue(HKEY_LOCAL_MACHINE, portkey, 'Delay', 600);
	RegWriteDWordValue(HKEY_LOCAL_MACHINE, portkey, 'LogFileUse', 0);
	RegWriteDWordValue(HKEY_LOCAL_MACHINE, portkey, 'LogFileDebug', 0);
	RegWriteDWordValue(HKEY_LOCAL_MACHINE, portkey, 'PrintError', 0);
end;


procedure isxdl_AddFile(URL, Filename: PChar);
external 'isxdl_AddFile@files:isxdl.dll stdcall';
function isxdl_DownloadFiles(hWnd: Integer): Integer;
external 'isxdl_DownloadFiles@files:isxdl.dll stdcall';
function isxdl_SetOption(Option, Value: PChar): Integer;
external 'isxdl_SetOption@files:isxdl.dll stdcall';

function InstallGS(): boolean;
begin
  result := bInstallGS;
end;
function InstallTIFF(): boolean;
begin
  result := bInstallTIFF;
end;
function TIFFSetupPath(Param: String): string;
begin
  result := sTIFFSetupPath;
end;
function GSSetupPath(Param: String): string;
begin
  result := sGSSetupPath;
end;

// Return value: 1 needed, 0 not needed, -1 do not download
function isDownloadNeeded(var setupPath: string; downloadURL: string): integer;
var
  exeName: string;
begin
  result := 0;
  exeName := ExtractFileName(downloadURL);
  setupPath := ExpandConstant('{src}\' + exeName);
	if not FileExists(setupPath) then
	begin
		setupPath := ExpandConstant('{userdesktop}\' + exeName);
		if not FileExists(setupPath) then
		begin
		  setupPath := ExpandConstant('{tmp}\' + exeName);
		  if not FileExists(setupPath) then
		  begin
		   result := 1;
		   
		   if preserveDownload = -1 then
		   begin
		       case MsgBox('Preserve downloaded files (save on the desktop)?', mbConfirmation, MB_YESNOCANCEL) of
           IDYES:
		          preserveDownload := 1;
		       IDNO:
  		        preserveDownload := 0;
		       else
	   	        result := -1;
		       end;		
		   end;
		   
		   if result = 1 then
       begin
  		   if preserveDownload = 1 then
	   	     setupPath := ExpandConstant('{userdesktop}\' + exeName);
	 	
		  	 isxdl_AddFile(downloadURL, setupPath);
		   end;
		  end
		end
	end
end;

function getGSEXE(): string;
var
  path: string;
begin
  result := '';
  if RegQueryStringValue(HKEY_LOCAL_MACHINE, ghostscriptdllkey, 'GS_DLL', path) then
  begin
    result := extractFileDir(path) + '\gswin32c.exe';
    if is64BitInstallMode() then
    begin
      if not FileExists(result) then
        result := extractFileDir(path) + '\gswin64c.exe'
    end;
  end;
end;

function getTIFFEXE(): string;
var
  path: string;
begin
  result := '';
  if RegQueryStringValue(HKEY_LOCAL_MACHINE, tiffkey, 'InstallPath', path) then
    result := addbackslash(path) + 'bin\tiff2pdf.exe'
end;

function DownloadFiles(): boolean;
var
  hWnd: Integer;
  path: string;
  gsURL: string;
  downloadNeeded: integer;
begin
    downloadNeeded := 0;

    if (IsTaskSelected('GhostScript')) then
    begin
      bInstallGS := true;
      path := getGSEXE();
      if path <> '' then
        if FileExists(path) then
        begin
          bInstallGS := false;
          log('Found Ghostscript: ' + path);
        end;
    end
    else
      bInstallGS := false;

    if (IsTaskSelected('tiff2pdf')) then
    begin
      bInstallTIFF := true;
      path := getTIFFEXE();
      if path <> '' then
        if FileExists(path) then
        begin
          bInstallTIFF := false;
          log('Found tiff2pdf: ' + path);
       end;
    end
    else
      bInstallTIFF := false;

    if bInstallGS then
    begin
      if Is64bitInstallMode() then
        gsURL := ghostscript64path
      else
        gsURL := ghostscript32path;
        
      downloadNeeded := isDownloadNeeded(sGSSetupPath, gsURL);
    end;

    if bInstallTIFF and (downloadNeeded >= 0) then
    begin
      case isDownloadNeeded(sTIFFSetupPath, tiffpath) of
       0: ; // Do nothing
       1: downloadNeeded := 1;
      -1: downloadNeeded := -1;
      end;
    end;

		// don't try to init isxdl if it's not needed because it will error on < ie 3

		if downloadNeeded = 1 then
			begin
				hWnd := StrToInt(ExpandConstant('{wizardhwnd}'));
				//isxdl_SetOption('label', 'Downloading Ghost');
				//isxdl_SetOption('description', 'This program needs to install the Microsoft .NET Framework 2.0. Please wait while Setup is downloading extra files to your computer.');
				if isxdl_DownloadFiles(hWnd) = 0 then Result := false;
			end;
			
		result := (downloadNeeded >= 0);
end;

procedure WriteSettingsDefault();
var
  tiffExe, gsExe: string;
  settingsFile: string;
  haveTIFFAndGS: boolean;
  lines: TArrayOfString;
begin
  tiffExe := getTIFFExe();
  gsExe := getGSExe();
  settingsFile := ExpandConstant('{app}\settings.default');
  haveTIFFAndGS := true;

  if fileExists(settingsFile) then
  begin
    loadStringsFromFile(settingsFile, lines);
    if lines[0] <> setupsig then
      exit;
  end;

  setArrayLength(lines, 6);
  lines[0] := setupsig;
  if (tiffExe <> '') and fileexists(tiffExe) then
  begin
    StringChangeEx(tiffExe, '\', '\\', true);
    StringChangeEx(tiffExe, ':', '\:', true);
    lines[1] := 'tiff2PDFLocation=' + tiffExe;
  end
  else
    haveTIFFAndGS := false;
  if (gsExe <> '') and fileexists(gsExe) then
  begin
    StringChangeEx(gsExe, '\', '\\', true);
    StringChangeEx(gsExe, ':', '\:', true);
    lines[2] := 'ghostScriptLocation=' + gsExe;
  end
  else
    haveTIFFAndGS := false;
    
  if haveTIFFAndGS then
  begin
    lines[3] := 'createSingleFilesForViewing=true';
    lines[4] := 'alwaysCreateTargetFormat=true';
    lines[5] := 'multiFileSendMode=EXCEPT_COVER';
    saveStringsToFile(settingsFile, lines, true);
  end;
end;

procedure CurStepChanged(CurStep: TSetupStep);
begin
  if CurStep = ssPostInstall then
  begin
    WriteSettingsDefault();
  end;
end;

function NextButtonClick(CurPage: Integer): Boolean;
begin
  result := true;

  //*********************************************************************************
  // Only run this at the "Ready To Install" wizard page.
  //*********************************************************************************
  if CurPage = wpReady then
	begin
    result := downloadFiles();
	end;

end;
