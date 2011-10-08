#ifndef VERSION
 #define public VERSION "0.4.2a"
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
  #define FOPDIR "..\..\..\..\build\fop-1.0"
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
Source: execyajhfc2.vbs; DestDir: {app}; Components: base

; YajHFC console support
Source: ..\..\..\workspace\yajhfc-console\build\yajhfc-console.jar; DestDir: {app}; Components: console
Source: ..\..\..\workspace\yajhfc-console\build\cyajhfc.exe; DestDir: {app}; Components: console
Source: ..\..\..\workspace\yajhfc-console\dist\README.txt; DestName: README-cyajhfc.txt; DestDir: {app}; Components: console

; Redmon: Common files (docs):
Source: redmon\LICENCE; DestDir: {app}\redmon; Components: FaxPrinter/Redmon
Source: redmon\README.TXT; DestDir: {app}\redmon; Components: FaxPrinter/Redmon
Source: redmon\redmon.hlp; DestDir: {app}\redmon; Components: FaxPrinter/Redmon
; Redmon: 32bit files
Source: redmon\enum.exe; DestDir: {app}\redmon; Components: FaxPrinter/Redmon; Check: not IsWin64
Source: redmon\FILE_ID.DIZ; DestDir: {app}\redmon; Components: FaxPrinter/Redmon; Check: not IsWin64
Source: redmon\redfile.exe; DestDir: {app}\redmon; Components: FaxPrinter/Redmon; Check: not IsWin64
Source: redmon\redmon35.dll; DestDir: {app}\redmon; Components: FaxPrinter/Redmon; Check: not IsWin64
Source: redmon\redmon95.dll; DestDir: {app}\redmon; Components: FaxPrinter/Redmon; Check: not IsWin64
Source: redmon\redmonnt.dll; DestDir: {app}\redmon; Components: FaxPrinter/Redmon; Check: not IsWin64
Source: redmon\redpr.exe; DestDir: {app}\redmon; Components: FaxPrinter/Redmon; Check: not IsWin64
Source: redmon\redrun.exe; DestDir: {app}\redmon; Components: FaxPrinter/Redmon; Check: not IsWin64
Source: redmon\setup.exe; DestDir: {app}\redmon; Components: FaxPrinter/Redmon; Check: not IsWin64
Source: redmon\src.zip; DestDir: {app}\redmon; Components: FaxPrinter/Redmon; Check: not IsWin64
Source: redmon\unredmon.exe; DestDir: {app}\redmon; Components: FaxPrinter/Redmon; Check: not IsWin64
; Redmon: 64bit files
Source: redmon64\enum.exe; DestDir: {app}\redmon; Components: FaxPrinter/Redmon; Check: IsWin64
Source: redmon64\FILE_ID.DIZ; DestDir: {app}\redmon; Components: FaxPrinter/Redmon; Check: IsWin64
Source: redmon64\redfile.exe; DestDir: {app}\redmon; Components: FaxPrinter/Redmon; Check: IsWin64
Source: redmon64\redmonnt.dll; DestDir: {app}\redmon; Components: FaxPrinter/Redmon; Check: IsWin64
Source: redmon64\redpr.exe; DestDir: {app}\redmon; Components: FaxPrinter/Redmon; Check: IsWin64
Source: redmon64\redrun.exe; DestDir: {app}\redmon; Components: FaxPrinter/Redmon; Check: IsWin64
Source: redmon64\setup.exe; DestDir: {app}\redmon; Components: FaxPrinter/Redmon; Check: IsWin64
Source: redmon64\src64.zip; DestDir: {app}\redmon; Components: FaxPrinter/Redmon; Check: IsWin64
Source: redmon64\unredmon.exe; DestDir: {app}\redmon; Components: FaxPrinter/Redmon; Check: IsWin64
; RedmonEE: 32bit files
Source: RedMonEE\Doc2Html.exe; DestDir: {app}\redmonee; Components: FaxPrinter/Redmonee
Source: RedMonEE\Enum.exe; DestDir: {app}\redmonee; Components: FaxPrinter/Redmonee
Source: RedMonEE\LICENCE; DestDir: {app}\redmonee; Components: FaxPrinter/Redmonee
Source: RedMonEE\readme.1st; DestDir: {app}\redmonee; Components: FaxPrinter/Redmonee
Source: RedMonEE\RedFile.exe; DestDir: {app}\redmonee; Components: FaxPrinter/Redmonee
Source: RedMonEE\RedMon.chm; DestDir: {app}\redmonee; Components: FaxPrinter/Redmonee
Source: RedMonEE\redmoncl.exe; DestDir: {app}\redmonee; Components: FaxPrinter/Redmonee
Source: RedMonEE\redmoncs.exe; DestDir: {app}\redmonee; Components: FaxPrinter/Redmonee
Source: RedMonEE\RedMonEE.pdf; DestDir: {app}\redmonee; Components: FaxPrinter/Redmonee
Source: RedMonEE\redmonfn.exe; DestDir: {app}\redmonee; Components: FaxPrinter/Redmonee
Source: RedMonEE\redmonnt.dll; DestDir: {app}\redmonee; Components: FaxPrinter/Redmonee
Source: RedMonEE\redmonS.exe; DestDir: {app}\redmonee; Components: FaxPrinter/Redmonee
Source: RedMonEE\redpr.exe; DestDir: {app}\redmonee; Components: FaxPrinter/Redmonee
Source: RedMonEE\redrun.exe; DestDir: {app}\redmonee; Components: FaxPrinter/Redmonee
Source: RedMonEE\RedRunEE.exe; DestDir: {app}\redmonee; Components: FaxPrinter/Redmonee
Source: RedMonEE\RedRunEE2.exe; DestDir: {app}\redmonee; Components: FaxPrinter/Redmonee
Source: RedMonEE\RedShell.exe; DestDir: {app}\redmonee; Components: FaxPrinter/Redmonee
Source: RedMonEE\RedShellW.exe; DestDir: {app}\redmonee; Components: FaxPrinter/Redmonee
Source: RedMonEE\RedSleep.exe; DestDir: {app}\redmonee; Components: FaxPrinter/Redmonee
Source: RedMonEE\Setup.exe; DestDir: {app}\redmonee; Components: FaxPrinter/Redmonee
Source: RedMonEE\UnRedmon.exe; DestDir: {app}\redmonee; Components: FaxPrinter/Redmonee
Source: submitanddelete.cmd; DestDir: {app}; Components: FaxPrinter/Redmonee

Source: ..\..\..\workspace\yajhfc\COPYING; DestDir: {app}; Components: base
Source: temp\README.txt; DestDir: {app}; Components: base
Source: temp\README_de.txt; DestDir: {app}; Components: docs
Source: temp\README_es.txt; DestDir: {app}; Components: docs
Source: temp\README_fr.txt; DestDir: {app}; Components: docs
Source: temp\README_ru.txt; DestDir: {app}; Components: docs
Source: temp\README_tr.txt; DestDir: {app}; Components: docs
Source: w98info.txt; DestDir: {app}; OnlyBelowVersion: 0,5.0; Components: faxprinter/redmon faxprinter/redmonee; Tasks:
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
;  #define ClassPath ""

  #sub ProcessFoundFile
    #define FileName FindGetFileName(FindHandle)
Source: {#FOPDIR}\lib\{#FileName}; DestDir: {app}\lib; Components: base
;	#define public ClassPath = ClassPath + "{app}\lib\" + FileName + ";"
  #endsub

  #for {FindHandle = FindResult = FindFirst(FOPDIR + "\lib\*.jar", 0); FindResult; FindResult = FindNext(FindHandle)} ProcessFoundFile
  #if FindHandle
    #expr FindClose(FindHandle)
  #endif

;  #define ClassPath = ClassPath + "{app}\lib\fop.jar"
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
 #define public AdditionalLaunchArgs=""
#else
 #define public AdditionalLaunchArgs="--loadplugin """"{app}\FOPPlugin.jar"""""
#endif

#define public LaunchEXEArgs="""""{app}\execyajhfc2.vbs"""" " + AdditionalLaunchArgs
#define public LaunchEXE="wscript.exe"

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
Root: HKLM; Subkey: Software\YajHFC; ValueType: string; ValueName: printlaunchyajhfcparams; ValueData: "{#AdditionalLaunchArgs} --stdin"; Flags: createvalueifdoesntexist uninsdeletekey; Components: faxprinter/redmon
Root: HKLM; Subkey: SYSTEM\CurrentControlSet\Control\Print\Monitors\{cm:redmonname}\Ports\YAJHFC:; Flags: uninsdeletekey dontcreatekey; Components: faxprinter/redmon faxprinter/redmonee

[Components]
Name: Base; Description: {cm:CoreApplicationFiles}; Flags: fixed; Types: custom compact full
Name: Docs; Description: {cm:Documentation}; Types: full
Name: FaxPrinter; Description: {cm:InstallFaxPrinter}; Types: full
Name: FaxPrinter/Redmon; Description: {cm:RedmonComponentDesc}; Types: full; Flags: exclusive
Name: FaxPrinter/RedmonEE; Description: {cm:RedmonEEComponentDesc}; Flags: exclusive
Name: Console; Description: {cm:ConsoleSupport}; Types: full

[Run]
Filename: rundll32; StatusMsg: {cm:RemovingX,{cm:faxprinter}}; Components: faxprinter/redmon faxprinter/redmonee; Parameters: "printui.dll,PrintUIEntry /dl /n ""{cm:printername}"""; Check: RemoveOldPrinter; MinVersion: 0,5.0
Filename: unredmon.exe; WorkingDir: {sys}; StatusMsg: {cm:RemovingX,RedMon}; Components: FaxPrinter/Redmon FaxPrinter/RedmonEE; Check: RemoveOldPrinter; BeforeInstall: RemoveYajHFCPort; Parameters: /Q

Filename: {app}\redmon\setup.exe; WorkingDir: {app}\redmon; StatusMsg: {cm:InstallingX,RedMon}; Components: FaxPrinter/Redmon; Check: RedmonNotInstalled; Parameters: /Q
Filename: {app}\redmonee\setup.exe; WorkingDir: {app}\redmonee; StatusMsg: {cm:InstallingX,RedMonEE}; Components: FaxPrinter/RedmonEE; Check: RedmonNotInstalled; Parameters: /Q
Filename: rundll32; Parameters: "printui.dll,PrintUIEntry /if /b ""{cm:printername}"" /f {win}\inf\ntprint.inf /r ""yajhfc:"" /m ""Apple LaserWriter 16/600 PS"""; Components: faxprinter/redmon faxprinter/redmonee; StatusMsg: {cm:InstallingX,{cm:faxprinter}}; Check: NoYajHFCPrinter; BeforeInstall: InstallYajHFCPort(); MinVersion: 0,5.0; OnlyBelowVersion: 0,6.0
Filename: rundll32; Parameters: "printui.dll,PrintUIEntry /if /b ""{cm:printername}"" /f {win}\inf\ntprint.inf /r ""yajhfc:"" /m ""FX DP 305-AP PS"""; Components: faxprinter/redmon faxprinter/redmonee; StatusMsg: {cm:InstallingX,{cm:faxprinter}}; Check: NoYajHFCPrinter; BeforeInstall: InstallYajHFCPort(); MinVersion: 0,6.0; OnlyBelowVersion: 0,6.01
Filename: rundll32; Parameters: "printui.dll,PrintUIEntry /if /b ""{cm:printername}"" /f {win}\inf\ntprint.inf /r ""yajhfc:"" /m ""Xerox Phaser 6120 PS"""; Components: faxprinter/redmon faxprinter/redmonee; StatusMsg: {cm:InstallingX,{cm:faxprinter}}; Check: NoYajHFCPrinter; BeforeInstall: InstallYajHFCPort(); MinVersion: 0,6.01

Filename: {app}\w98info.txt; Flags: shellexec; OnlyBelowVersion: 0,5.0; Components: faxprinter/redmon faxprinter/redmonee
;Filename: {code:GSSetupPath}; Check: InstallGS; StatusMsg: {cm:InstallingX,GhostScript}; Parameters: -auto
Filename: {code:GSSetupPath}; Check: InstallGS; StatusMsg: {cm:InstallingX,GhostScript}; Parameters: /S
Filename: {code:TIFFSetupPath}; Check: InstallTIFF; StatusMsg: {cm:InstallingX,tiff2pdf}; Parameters: /SILENT
[UninstallRun]
Filename: rundll32; Components: faxprinter/redmon faxprinter/redmonee; Parameters: "printui.dll,PrintUIEntry /dl /n ""{cm:printername}"""; RunOnceId: DeletePrinter; MinVersion: 0,5.0
Filename: reg; Components: faxprinter/redmon faxprinter/redmonee; Parameters: "delete ""HKLM\SYSTEM\CurrentControlSet\Control\Print\Monitors\{cm:redmonname}\Ports\YAJHFC:"" /f"; RunOnceId: DeletePort; MinVersion: 0,5.0
Filename: unredmon.exe; WorkingDir: {sys}; Components: FaxPrinter/Redmon FaxPrinter/RedmonEE; RunOnceId: RemoveRedmon
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
RedmonComponentDesc=Redmon
RedmonEEComponentDesc=RedmonEE

; English/Default text:
InstallFaxPrinter=Install a fax printer
InstallingX=Installing %1...
RemovingX=Removing %1...
FaxPrinter=fax printer
CoreApplicationFiles=Core application files
Documentation=Documentation
InstallGSTask=Download and install GhostScript (if not already installed)
InstallTIFFTask=Download and install tiff2pdf (if not already installed)
PreserveDLMsg=Save downloaded files on the desktop?
RedmonEE32bitOnly=RedmonEE is only supported on 32bit versions of Windows.
RedmonEERecommended=On this version of Windows, using RedmonEE instead of Redmon is recommended.
InstalledRedmonDiffers=The Redmon version already installed on this computer differs from the version you have selected for installation. Do you want setup to uninstall the already installed version? (If you select "No" YajHFC will continue to use the installed version.)
ConsoleSupport=Support for command line only mode

; German translation:
de.InstallFaxPrinter=Einen Faxdrucker installieren
de.InstallingX=Installiere %1...
de.RemovingX=Entferne %1...
de.FaxPrinter=Faxdrucker
de.CoreApplicationFiles=Zentrale Programmdateien
de.Documentation=Dokumentation
de.InstallGSTask=GhostScript herunterladen und installieren (falls noch nicht installiert)
de.InstallTIFFTask=tiff2pdf herunterladen und installieren (falls noch nicht installiert)
de.PreserveDLMsg=Heruntergeladene Dateien auf dem Desktop speichern?
de.RedmonEE32bitOnly=RedmonEE wird nur auf 32bit-Versionen von Windows unterstützt.
de.RedmonEERecommended=Auf dieser Version von Windows wird empfohlen, RedmonEE anstatt von Redmon zu verwenden.
de.InstalledRedmonDiffers=Die auf diesem Computer bereits installierte Redmon-Version weicht von der zur Installation ausgewählten ab. Möchten Sie, dass Setup die bereits installierte Version deinstalliert? (Wenn Sie "Nein" auswählen, wird YajHFC die bereits installierte Version weiterbenutzen.)
de.ConsoleSupport=Unterstützung für Nur-Kommandozeilenmodus

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
    bRemoveOldPrinter: boolean;
const
    ghostscript32path = 'http://downloads.sourceforge.net/sourceforge/ghostscript/gs902w32.exe';
    ghostscript64path = 'http://downloads.sourceforge.net/sourceforge/ghostscript/gs902w64.exe';
    ghostscriptdllkey = 'SOFTWARE\GPL Ghostscript';
    tiffkey = 'SOFTWARE\GnuWin32\Tiff';
    tiffpath = 'http://downloads.sourceforge.net/sourceforge/gnuwin32/tiff-3.8.2-1.exe';
    setupsig = '# Auto-generated by YajHFC setup. Remove this line if you manually edit this file, else it will get overwritten on a update';

function checkJavaVersion(SOFTWAREprefix: string; var javaver: string):boolean;
var javamajor, javaminor, p: integer;
begin
	result := false;
  javaver := 'none';
	// Check for an JRE with the right version
	if RegQueryStringValue(HKEY_LOCAL_MACHINE, SOFTWAREprefix + '\JavaSoft\Java Runtime Environment', 'CurrentVersion', javaver) then
	begin
		Log('Java version ' + javaver + ' found at HKLM/' + SOFTWAREprefix);
		p := Pos('.', javaver);
		if (p > 0) then
		begin
			javamajor := strtointdef(copy(javaver, 1, p-1), 0);
			javaminor := strtointdef(copy(javaver, p+1, length(javaver)-p), 0);
			result := (javamajor > 1) or ((javamajor = 1) and (javaminor >= 5))
		end;
	end else
		Log('No Java found at HKLM/' + SOFTWAREprefix);
end;

function InitializeSetup(): Boolean;
var
  rc: integer;
  javaver: string;
begin
  bInstallGS := false;
  bInstallTIFF := false;
  bRemoveOldPrinter := false;
  preserveDownload := -1;

  result := checkJavaVersion('SOFTWARE', javaver);
  if (not result) and Is64BitInstallMode then // Check for 32bit Java also
    result := checkJavaVersion('SOFTWARE\Wow6432Node', javaver);

	if (not result) then
	begin
		case MsgBox(
		  'YajHFC needs an installed Java Runtime Environment version 1.5 or higher (installed is ' + javaver + ').'#10#13
		  'Please download and install a Java Runtime Environment from http://www.java.com/ or http://java.sun.com/ and rerun Setup.'#10#13
		  'Do you want to download Java now?'#10#13#10#13
		  '"Yes" opens http://www.java.com/ in a web browser and cancels the setup.'#10#13
		  '"No" continues with the setup anyway (you will NOT be able to run YajHFC until a suitable Java version is installed, however!).'#10#13
		  '"Cancel" aborts the installation.',
		  mbError, MB_YESNOCANCEL) of
		IDYES:
			ShellExec('open', 'http://www.java.com/', '', '', SW_SHOW, ewNoWait, rc);
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

// Install port for "normal" Redmon
procedure InstallYajHFCPortNormal();
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
	RegWriteStringValue(HKEY_LOCAL_MACHINE, portkey, 'Arguments', '"' + ExpandConstant('{app}\execyajhfc2.vbs') + '" /printlaunch');
	RegWriteStringValue(HKEY_LOCAL_MACHINE, portkey, 'Printer', '');
	RegWriteStringValue(HKEY_LOCAL_MACHINE, portkey, 'LogFileName', ExpandConstant('{app}\yajhfc-redmon.log'));
	RegWriteDWordValue(HKEY_LOCAL_MACHINE, portkey, 'Output', 0);
	RegWriteDWordValue(HKEY_LOCAL_MACHINE, portkey, 'ShowWindow', 0);
	RegWriteDWordValue(HKEY_LOCAL_MACHINE, portkey, 'RunUser', 1);
	RegWriteDWordValue(HKEY_LOCAL_MACHINE, portkey, 'Delay', 1200);
	RegWriteDWordValue(HKEY_LOCAL_MACHINE, portkey, 'LogFileUse', 0);
	RegWriteDWordValue(HKEY_LOCAL_MACHINE, portkey, 'LogFileDebug', 0);
	RegWriteDWordValue(HKEY_LOCAL_MACHINE, portkey, 'PrintError', 0);
end;

// Install port for RedmonEE
procedure InstallYajHFCPortEE();
var redmonkey, portkey: string;
begin
	redmonkey := 'SYSTEM\CurrentControlSet\Control\Print\Monitors\' + CustomMessage('redmonname');
	if not RegKeyExists(HKEY_LOCAL_MACHINE, redmonkey) then
	begin
		MsgBox('Could not create a redirected port for YajHFC (RedmonEE not correctly installed).', mbError, MB_OK);
		exit;
	end;

	portkey := redmonkey + '\Ports\YAJHFC:';
	if RegKeyExists(HKEY_LOCAL_MACHINE, portkey) then
		Log('YAJHFC: port seems to already exist. Continuing anyway...'); // Port already created ??

	RegWriteStringValue(HKEY_LOCAL_MACHINE, portkey, 'Description', 'YajHFC Redirected Port');
	RegWriteStringValue(HKEY_LOCAL_MACHINE, portkey, 'Command', '');
	RegWriteStringValue(HKEY_LOCAL_MACHINE, portkey, 'Arguments', '');
  RegWriteStringValue(HKEY_LOCAL_MACHINE, portkey, 'AfterworksCommand', '"' + ExpandConstant('{app}\submitanddelete.cmd') + '"');
  RegWriteStringValue(HKEY_LOCAL_MACHINE, portkey, 'AfterworksArguments', '%s');
  RegWriteDWordValue(HKEY_LOCAL_MACHINE, portkey, 'Output', 0);
  RegWriteDWordValue(HKEY_LOCAL_MACHINE, portkey, 'ShowWindow', 0);
  RegWriteDWordValue(HKEY_LOCAL_MACHINE, portkey, 'RunUser', 1);
  RegWriteDWordValue(HKEY_LOCAL_MACHINE, portkey, 'StartShell', 0);
  RegWriteDWordValue(HKEY_LOCAL_MACHINE, portkey, 'ServerRunning', 0);
  RegWriteDWordValue(HKEY_LOCAL_MACHINE, portkey, 'Delay', 1200);
  RegWriteDWordValue(HKEY_LOCAL_MACHINE, portkey, 'DebugLevel', 0);
  RegWriteDWordValue(HKEY_LOCAL_MACHINE, portkey, 'LogFileUse', 0);
	RegWriteStringValue(HKEY_LOCAL_MACHINE, portkey, 'LogFileName', ExpandConstant('{app}\yajhfc-redmonee.log'));
  RegWriteDWordValue(HKEY_LOCAL_MACHINE, portkey, 'LogFileDebug', 0);
  RegWriteDWordValue(HKEY_LOCAL_MACHINE, portkey, 'LogOmitPrintCode', 0);
  RegWriteDWordValue(HKEY_LOCAL_MACHINE, portkey, 'PrintError', 0);
  RegWriteDWordValue(HKEY_LOCAL_MACHINE, portkey, 'CleanUpDocNames', 0);
  RegWriteDWordValue(HKEY_LOCAL_MACHINE, portkey, 'LogOmitPrintCode', 0);
  RegWriteDWordValue(HKEY_LOCAL_MACHINE, portkey, 'DefaultFileName', 0);
  RegWriteDWordValue(HKEY_LOCAL_MACHINE, portkey, 'UseRunDll32', 0);
	RegWriteStringValue(HKEY_LOCAL_MACHINE, portkey, 'ExternalSaveAs', '');
  RegWriteStringValue(HKEY_LOCAL_MACHINE, portkey, 'FilenameTemplate', '');
  RegWriteStringValue(HKEY_LOCAL_MACHINE, portkey, 'DefaultExtension', '');
  RegWriteStringValue(HKEY_LOCAL_MACHINE, portkey, 'ServerProgram', '');
  RegWriteStringValue(HKEY_LOCAL_MACHINE, portkey, 'ServerPgmParam', '');
  RegWriteDWordValue(HKEY_LOCAL_MACHINE, portkey, 'UseRPC', 0);
  RegWriteDWordValue(HKEY_LOCAL_MACHINE, portkey, 'RPCDefaultProtocol', 0);
  RegWriteDWordValue(HKEY_LOCAL_MACHINE, portkey, 'RPCUseDefaultPort', 0);
  RegWriteDWordValue(HKEY_LOCAL_MACHINE, portkey, 'RPCSendError', 0);
  RegWriteDWordValue(HKEY_LOCAL_MACHINE, portkey, 'RPCFileTransfer', 0);
  RegWriteStringValue(HKEY_LOCAL_MACHINE, portkey, 'ServerAccountLog', '');
  RegWriteStringValue(HKEY_LOCAL_MACHINE, portkey, 'SpoolDirectory', '%TEMP%');
  RegWriteStringValue(HKEY_LOCAL_MACHINE, portkey, 'SpoolTemplate', '%REDMON_USER%-%c-%t-%n.ps');
  RegWriteDWordValue(HKEY_LOCAL_MACHINE, portkey, 'SpoolFiles', 1);
  RegWriteDWordValue(HKEY_LOCAL_MACHINE, portkey, 'SpoolNoRedirect', 0);
  RegWriteDWordValue(HKEY_LOCAL_MACHINE, portkey, 'SpoolAsUser', 0);
  RegWriteDWordValue(HKEY_LOCAL_MACHINE, portkey, 'SpoolDelete', 0);
  RegWriteDWordValue(HKEY_LOCAL_MACHINE, portkey, 'PDFLevel', 0);
  RegWriteDWordValue(HKEY_LOCAL_MACHINE, portkey, 'Encrypt', 0);
  RegWriteStringValue(HKEY_LOCAL_MACHINE, portkey, 'OwnerPassword', '');
  RegWriteStringValue(HKEY_LOCAL_MACHINE, portkey, 'UserPassword', '');
  RegWriteDWordValue(HKEY_LOCAL_MACHINE, portkey, 'DisableAll', 0);
  RegWriteDWordValue(HKEY_LOCAL_MACHINE, portkey, 'PromptSecOptions', 0);
  RegWriteDWordValue(HKEY_LOCAL_MACHINE, portkey, 'DisablePrint', 0);
  RegWriteDWordValue(HKEY_LOCAL_MACHINE, portkey, 'DisableModify', 0);
  RegWriteDWordValue(HKEY_LOCAL_MACHINE, portkey, 'DisableCopy', 0);
  RegWriteDWordValue(HKEY_LOCAL_MACHINE, portkey, 'DisableAnnotations', 0);
  RegWriteDWordValue(HKEY_LOCAL_MACHINE, portkey, 'DisableExtract', 0);
  RegWriteDWordValue(HKEY_LOCAL_MACHINE, portkey, 'DisableAssemble', 0);
  RegWriteDWordValue(HKEY_LOCAL_MACHINE, portkey, 'DisableHQPrint', 0);
  RegWriteDWordValue(HKEY_LOCAL_MACHINE, portkey, 'PriorityPrim', 0);
  RegWriteDWordValue(HKEY_LOCAL_MACHINE, portkey, 'PrioritySec', 0);
  RegWriteDWordValue(HKEY_LOCAL_MACHINE, portkey, 'FileNumber', 0);
end;

// Detects which version of Redmon is installed
// 0: No Redmon installed
// 1: "Normal" Redmon installed
// 2: Redmon EE installed
// 3: Unknown Redmon installed
function DetectInstalledRedmonVersion(): integer;
begin
  if RedmonNotInstalled then
    result := 0
  else
  begin
    //TODO: Find a better algorithm here...
    if FileExists(ExpandConstant('{sys}\redmon.hlp')) then
      result := 1
    else if FileExists(ExpandConstant('{sys}\redmon.chm')) then
      result := 2
    else
      result := 3;
  end;
end;

procedure InstallYajHFCPort();
begin
  if (DetectInstalledRedmonVersion = 2) then
    InstallYajHFCPortEE()
  else
    InstallYajHFCPortNormal();
end;

procedure RemoveYajHFCPort();
begin
  RegDeleteKeyIncludingSubkeys(HKEY_LOCAL_MACHINE, 'SYSTEM\CurrentControlSet\Control\Print\Monitors\' + CustomMessage('redmonname') + '\Ports\YAJHFC:')
end;

function IsRedmonEERecommended(): boolean;
var winver: TWindowsVersion;
begin
   if IsWin64 then
   begin
     result := false;
     exit;
   end;

   GetWindowsVersionEx(winver);
   result := (winver.ProductType = VER_NT_DOMAIN_CONTROLLER) or
             (winver.ProductType = VER_NT_SERVER) or
             ((winver.SuiteMask and (VER_SUITE_TERMINAL or VER_SUITE_SMALLBUSINESS or VER_SUITE_SMALLBUSINESS_RESTRICTED)) <> 0);
end;

function RemoveOldPrinter(): boolean;
begin
  result := bRemoveOldPrinter;
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
var installedRedmonVer: integer;
begin
  result := true;

  //*********************************************************************************
  // Only run this at the "Ready To Install" wizard page.
  //*********************************************************************************
  case CurPage of
  wpReady:
      result := downloadFiles();
  wpSelectComponents:
    begin
      if IsComponentSelected('faxprinter/redmonee') then
      begin
        if IsWin64 then
        begin
          MsgBox(CustomMessage('RedmonEE32bitOnly'), mbError, MB_OK);
          result := false;
        end;
      end
      else if IsComponentSelected('faxprinter/redmon') then
      begin
        if IsRedmonEERecommended then
        begin
          MsgBox(CustomMessage('RedmonEERecommended'), mbInformation, MB_OK);
        end;
      end;
      if result then
      begin
        bRemoveOldPrinter := false;
        installedredmonver := DetectInstalledRedmonVersion;
        log('Found installed Redmon version of ' + IntToStr(installedredmonver));
        if installedredmonver > 0 then //Some redmon version already installed
        begin
          if ((installedRedmonVer <> 1) and IsComponentSelected('faxprinter/redmon')) or
             ((installedRedmonVer <> 2) and IsComponentSelected('faxprinter/redmonee')) then
          begin
             if MsgBox(CustomMessage('InstalledRedmonDiffers'), mbConfirmation, MB_YESNO) = IDYES then
              bRemoveOldPrinter := true;
          end;
        end;
      end;
    end;
  end;
end;

procedure CheckComponent(const componentDescription: string);
var i: integer;
begin
  for i:=0 to wizardform.ComponentsList.Items.Count-1 do
  begin
    if wizardform.componentslist.itemcaption[i] = componentDescription then
    begin
      wizardform.componentslist.checked[i] := true;
      exit;
    end;
  end;
end;

procedure InitializeWizard();
begin
  if IsRedmonEERecommended then
    CheckComponent(CustomMessage('RedmonEEComponentDesc'));
end;









