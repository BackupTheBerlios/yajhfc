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
Source: temp\README.txt; DestDir: {app}; Components: base
Source: temp\README_de.txt; DestDir: {app}; Components: docs
Source: temp\README_es.txt; DestDir: {app}; Components: docs
Source: temp\README_fr.txt; DestDir: {app}; Components: docs
Source: temp\README_ru.txt; DestDir: {app}; Components: docs
Source: temp\README_tr.txt; DestDir: {app}; Components: docs
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
AppCopyright=© 2005-2009 by Jonas Wolz
AppName={#APPNAME}
AppVerName={#APPVERNAME}
InfoBeforeFile=temp\README.txt
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
 #define public LaunchArgs="-cp """"" + ClassPath + ";{app}\yajhfc.jar"""" yajhfc.Launcher --loadplugin """"{app}\FOPPlugin.jar"""""
; #define public LaunchEXE="{app}\yajhfc-fo.cmd"
; #define public LaunchEXEArgs=""
 #define public LaunchEXEArgs="--loadplugin """"{app}\FOPPlugin.jar"""""
#endif

#define public LaunchEXE="{app}\execyajhfc.vbs"

[Icons]
Name: {group}\{cm:YajHFCName}; Filename: {#LaunchEXE}; Parameters: "{#LaunchEXEArgs}"; WorkingDir: {app}; IconFilename: {app}\yajhfc.ico; IconIndex: 0; Components: base
Name: {group}\{cm:YajHFCName} ({cm:debugmode}); Filename: {#LaunchEXE}; Parameters: "{#LaunchEXEArgs} --debug --logfile=:prompt:"; WorkingDir: {app}; IconFilename: {app}\yajhfc.ico; IconIndex: 0; Components: base
Name: {commondesktop}\{cm:YajHFCName}; Filename: {#LaunchEXE}; Parameters: "{#LaunchEXEArgs}"; IconFilename: {app}\yajhfc.ico; IconIndex: 0; WorkingDir: {app}; Tasks: DesktopIcon
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
Root: HKLM; Subkey: Software\YajHFC; ValueType: string; ValueName: printlaunchparams; ValueData: "{#LaunchArgs}"; Flags: uninsdeletekey; Components: redmon
Root: HKLM; Subkey: Software\YajHFC; ValueType: string; ValueName: printlaunchyajhfcparams; ValueData: "--stdin --background"; Flags: createvalueifdoesntexist uninsdeletekey; Components: redmon
Root: HKLM; Subkey: SYSTEM\CurrentControlSet\Control\Print\Monitors\{cm:redmonname}\Ports\YAJHFC:; Flags: uninsdeletekey dontcreatekey; Components: redmon

[Components]
Name: Base; Description: {cm:CoreApplicationFiles}; Flags: fixed; Types: custom compact full
Name: Docs; Description: {cm:Documentation}; Types: full
Name: Redmon; Description: {cm:InstallFaxPrinter}; Types: full
[Run]
Filename: {app}\redmon\setup.exe; WorkingDir: {app}\redmon; StatusMsg: {cm:InstallingX,RedMon}; Components: redmon; Check: RedmonNotInstalled; Parameters: /Q
Filename: rundll32; Parameters: "printui.dll,PrintUIEntry /if /b ""{cm:printername}"" /f {win}\inf\ntprint.inf /r ""yajhfc:"" /m ""Apple LaserWriter 16/600 PS"""; Components: redmon; StatusMsg: {cm:InstallingX,{cm:faxprinter}}; Check: NoYajHFCPrinter; BeforeInstall: InstallYajHFCPort(); MinVersion: 0,5.0; OnlyBelowVersion: 0,6.0
Filename: rundll32; Parameters: "printui.dll,PrintUIEntry /if /b ""{cm:printername}"" /f {win}\inf\ntprint.inf /r ""yajhfc:"" /m ""FX DP 305-AP PS"""; Components: redmon; StatusMsg: {cm:InstallingX,{cm:faxprinter}}; Check: NoYajHFCPrinter; BeforeInstall: InstallYajHFCPort(); MinVersion: 0,6.0; OnlyBelowVersion: 0,6.01
Filename: rundll32; Parameters: "printui.dll,PrintUIEntry /if /b ""{cm:printername}"" /f {win}\inf\ntprint.inf /r ""yajhfc:"" /m ""Xerox Phaser 6120 PS"""; Components: redmon; StatusMsg: {cm:InstallingX,{cm:faxprinter}}; Check: NoYajHFCPrinter; BeforeInstall: InstallYajHFCPort(); MinVersion: 0,6.01;

Filename: {app}\w98info.txt; Flags: shellexec; OnlyBelowVersion: 0,5.0; Components: redmon
Filename: {code:GSSetupPath}; Check: InstallGS; StatusMsg: {cm:InstallingX,GhostScript}; Parameters: -auto
Filename: {code:TIFFSetupPath}; Check: InstallTIFF; StatusMsg: {cm:InstallingX,tiff2pdf}; Parameters: /SILENT
[UninstallRun]
Filename: rundll32; Components: redmon; Parameters: "printui.dll,PrintUIEntry /dl /n ""{cm:printername}"""; RunOnceId: DeletePrinter; MinVersion: 0,5.0
[INI]
Filename: {group}\Homepage.url; Section: InternetShortcut; Key: URL; String: http://yajhfc.berlios.de/
[UninstallDelete]
Type: files; Name: {group}\Homepage.url
Type: files; Name: {app}\settings.default
Type: files; Name: {app}\tiff2pdf.cmd

[CustomMessages]
; Do not translate redmonname
redmonname=Redirected Port

; May be translated
printername=YajHFC Fax
DebugMode=debug mode
YajHFCName=YajHFC fax client

; English/Default text:
InstallFaxPrinter=Install a fax printer
InstallingX=Installing %1...
FaxPrinter=fax printer
CoreApplicationFiles=Core application files
Documentation=Documentation
InstallGSTask=Download and install GhostScript (if not already installed)
InstallTIFFTask=Download and install tiff2pdf (if not already installed)
PreserveDLMsg=Save downloaded files on the desktop?

; German translation:
de.InstallFaxPrinter=Einen Faxdrucker installieren
de.InstallingX=Installiere %1...
de.FaxPrinter=Faxdrucker
de.CoreApplicationFiles=Zentrale Programmdateien
de.Documentation=Dokumentation
de.InstallGSTask=GhostScript herunterladen und installieren (falls noch nicht installiert)
de.InstallTIFFTask=tiff2pdf herunterladen und installieren (falls noch nicht installiert)
de.PreserveDLMsg=Heruntergeladene Dateien auf dem Desktop speichern?

; Spanish translation:
es.InstallFaxPrinter=Instalar una impresora de faxes
es.InstallingX=Instalando %1...
es.FaxPrinter=impresora de faxes
es.CoreApplicationFiles=Archivos principales de la aplicación
es.Documentation=Documentación
es.InstallGSTask=Descargar e instalar GhostScript (si no se encuentra instalado)
es.InstallTIFFTask=Descargar e instalar tiff2pdf (si no se encuentra instalado)
es.PreserveDLMsg=¿Guardar los archivos descargados en el escritorio?

; Polish translation
pl.InstallFaxPrinter=Zainstaluj drukarkę faksów
pl.InstallingX=Zainstalowano %1...
pl.FaxPrinter=Drukarka faksów
pl.CoreApplicationFiles=Główne pliki aplikacji
pl.Documentation=Dokumentacja
pl.InstallGSTask=Pobierz i zainstaluj GhostScript (jeżeli nie zainstalowany)
pl.InstallTIFFTask=Pobierz i zainstaluj tiff2pdf (jeżeli nie zainstalowany)
pl.PreserveDLMsg=Zapisać pobrane pliki na pulpicie?

; Italian translation:
it.InstallFaxPrinter=Installa una stampante fax
it.InstallingX=Installazione %1...
it.FaxPrinter=stampante fax
it.CoreApplicationFiles=File applicativi di base
it.Documentation=Documentazione
it.InstallGSTask=Scarica e installa GhostScript (se non già installato)
it.InstallTIFFTask=Scarica e installa tiff2pdf (se non già installato)
it.PreserveDLMsg=Salvo i file scaricati sul Desktop?

; French translation:
fr.InstallFaxPrinter=Installer une imprimante Fax
fr.InstallingX=Installation %1...
fr.FaxPrinter=imprimante fax
fr.CoreApplicationFiles=Fichier application (Core)
fr.Documentation=Documentation
fr.InstallGSTask=Téléchargement et installation de GhostScript (Si pas déjà installé)
fr.InstallTIFFTask=Téléchargement et installation de tiff2pdf (Si pas déjà installé)
fr.PreserveDLMsg=Enregistrer les fichiers téléchargés sur le bureau?

; Russian translation:
ru.InstallFaxPrinter=Установить факс-принтер
ru.InstallingX=Устанавливается %1...
ru.FaxPrinter=факс-принтер
ru.CoreApplicationFiles=Базовые компоненты программы
ru.Documentation=Документация
ru.InstallGSTask=Загрузить и установить GhostScript (если еще не установлен)
ru.InstallTIFFTask=Загрузить и установить tiff2pdf (если еще не установлен)
ru.PreserveDLMsg=Сохранить загруженные файлы на рабочем столе?


; Native names for the languages (so do not translate them either):
German=Deutsch
English=English
Spanish=Español
French=Français
Turkish=Türkçe
Russian=Русский

[Tasks]
Name: DesktopIcon; Description: {cm:CreateDesktopIcon}; Components: base
Name: GhostScript; Description: {cm:InstallGSTask}; Components: base
Name: tiff2pdf; Description: {cm:InstallTIFFTask}; Components: base
[Languages]
Name: en; MessagesFile: compiler:Default.isl
Name: de; MessagesFile: compiler:Languages\German.isl; InfoBeforeFile: temp\README_de.txt
Name: fr; MessagesFile: compiler:Languages\French.isl; InfoBeforeFile: temp\README_fr.txt
Name: es; MessagesFile: compiler:Languages\Spanish.isl; InfoBeforeFile: temp\README_es.txt
Name: it; MessagesFile: compiler:Languages\Italian.isl
Name: pl; MessagesFile: compiler:Languages\Polish.isl
Name: ru; MessagesFile: compiler:Languages\Russian.isl; InfoBeforeFile: temp\README_ru.txt
Name: tr; MessagesFile: compiler:Languages\Turkish.isl; InfoBeforeFile: temp\README_tr.txt
[Code]
var bInstallGS: boolean;
    bInstallTIFF: boolean;
    sGSSetupPath: string;
    sTIFFSetupPath:string;
    preserveDownload: integer;
const
    ghostscript32path = 'http://downloads.sourceforge.net/sourceforge/ghostscript/gs864w32.exe';
    ghostscript64path = 'http://downloads.sourceforge.net/sourceforge/ghostscript/gs864w64.exe';
    ghostscriptdllkey = 'SOFTWARE\GPL Ghostscript';
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


procedure isxdl_AddFile(URL, Filename: PAnsiChar);
external 'isxdl_AddFile@files:isxdl.dll stdcall';
function isxdl_DownloadFiles(hWnd: Integer): Integer;
external 'isxdl_DownloadFiles@files:isxdl.dll stdcall';
function isxdl_SetOption(Option, Value: PAnsiChar): Integer;
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
		       case MsgBox(CustomMessage('PreserveDLMsg'), mbConfirmation, MB_YESNOCANCEL) of
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

function getGSEXE2(const RootKey: integer): string;
var
  path: string;
  subkeys: TArrayOfString;
  i: integer;
begin
  result := '';
  if RegGetSubkeyNames(RootKey, ghostscriptdllkey, subkeys) then
  begin
    // Test if any of the keys exist starting from the back to hopefully get newer versions first
    for I := GetArrayLength(subkeys)-1 downto 0 do
    begin
      if RegQueryStringValue(RootKey, ghostscriptdllkey + '\' + subkeys[i], 'GS_DLL', path) then
      begin
        result := extractFileDir(path) + '\gswin32c.exe';
        if FileExists(result) then
        begin
           log('Found GhostScript at: ' + result);
           exit;
        end
        else
          if is64BitInstallMode() then
          begin
              result := extractFileDir(path) + '\gswin64c.exe'
              if FileExists(result) then
              begin
                log('Found GhostScript at: ' + result);
                exit;
              end;
          end;
      end;
    end;
  end;
end;

function getGSEXE(): string;
begin
  result := getGSEXE2(HKEY_LOCAL_MACHINE);
  if Is64BitInstallMode() then
     if (result = '') or not FileExists(result) then
       result := getGSEXE2(HKEY_LOCAL_MACHINE_32);
end;

function getTIFFEXE2(const RootKey: integer): string;
var
  path: string;
begin
  result := '';
  if RegQueryStringValue(RootKey, tiffkey, 'InstallPath', path) then
    result := addbackslash(path) + 'bin\tiff2pdf.exe'
end;

function getTIFFEXE(): string;
begin
  result := getTIFFEXE2(HKEY_LOCAL_MACHINE);
  if Is64BitInstallMode() then
     if (result = '') or not FileExists(result) then
       result := getTIFFEXE2(HKEY_LOCAL_MACHINE_32);
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

procedure CheckTIFF2PDFWorkaround(var tiffExe:string);
var tiffExeOrig: string;
    lines: TArrayOfString;
begin
    if (GetWindowsVersion shr 24) >= 6 then // On Vista or Win7
    begin
      log('Need tiff2pdf work around...');
      tiffExeOrig := tiffExe;
      tiffExe     := ExpandConstant('{app}\tiff2pdf.cmd');
      setArrayLength(lines, 5);
      
      lines[0] := '@echo off';
      lines[1] := 'rem This batch file is used to work around a bug in tiff2pdf 3.8.2 on Vista + Win 7';
      lines[2] := '"' + tiffExeOrig + '" -o "%tmp%\tiff2pdf.tmp" %*';
      lines[3] := 'type "%tmp%\tiff2pdf.tmp"';
      lines[4] := 'del "%tmp%\tiff2pdf.tmp"';
      
      SaveStringsToFile(tiffExe, lines, false);
    end;
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

  log('Writing settings.default...');
  setArrayLength(lines, 6);
  lines[0] := setupsig;
  if (tiffExe <> '') and fileexists(tiffExe) then
  begin
    CheckTIFF2PDFWorkaround(tiffExe);
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
    log('settings.default written.');
  end
  else
    log('settings.default NOT written.');
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




