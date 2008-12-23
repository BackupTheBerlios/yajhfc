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
Name: {group}\YajHFC fax client; Filename: {#LaunchEXE}; Parameters: {#LaunchEXEArgs}; WorkingDir: {app}; IconFilename: {app}\yajhfc.ico; IconIndex: 0; Components: base
Name: {group}\YajHFC fax client (debug mode); Filename: {#LaunchEXE}; Parameters: {#LaunchEXEArgs} --debug --logfile=:prompt:; WorkingDir: {app}; IconFilename: {app}\yajhfc.ico; IconIndex: 0; Components: base
Name: {commondesktop}\YajHFC fax client; Filename: {#LaunchEXE}; Parameters: {#LaunchEXEArgs}; IconFilename: {app}\yajhfc.ico; IconIndex: 0; WorkingDir: {app}; Tasks: DesktopIcon
#ifdef WITHFOP
Name: {group}\FOPPlugin README; Filename: {app}\readme.pdf
#endif
Name: {group}\Homepage; Filename: http://yajhfc.berlios.de/; Components: base
Name: {group}\FAQ; Filename: {app}\faq.pdf; Components: docs
Name: {group}\FAQ (Deutsch); Filename: {app}\faq_de.pdf; Components: docs
Name: {group}\FAQ (Español); Filename: {app}\faq_es.pdf; Components: docs
Name: {group}\FAQ (Français); Filename: {app}\faq_fr.pdf; Components: docs
Name: {group}\FAQ (Türkçe); Filename: {app}\faq_tr.pdf; Components: docs
Name: {group}\FAQ (Russian); Filename: {app}\faq_ru.pdf; Components: docs

[Registry]
Root: HKLM; Subkey: Software\YajHFC; ValueType: string; ValueName: instpath; ValueData: {app}; Flags: uninsdeletekey
Root: HKLM; Subkey: Software\YajHFC; ValueType: string; ValueName: jarfile; ValueData: yajhfc.jar; Flags: uninsdeletekey
Root: HKLM; Subkey: Software\YajHFC; ValueType: string; ValueName: printlaunchparams; ValueData: "{#LaunchArgs} --stdin --background"; Flags: uninsdeletekey; Components: redmon
Root: HKLM; Subkey: SYSTEM\CurrentControlSet\Control\Print\Monitors\{cm:redmonname}\Ports\YAJHFC:; Flags: uninsdeletekey dontcreatekey; Components: redmon

[Components]
Name: Base; Description: Core Application files; Flags: fixed; Types: custom compact full
Name: Docs; Description: PDF-Documentation; Types: full
Name: Redmon; Description: Install a fax printer; Types: full
[Run]
Filename: {app}\redmon\setup.exe; WorkingDir: {app}\redmon; StatusMsg: Installing redmon...; Components: redmon; Check: RedmonNotInstalled; Parameters: /Q
Filename: rundll32; Parameters: "printui.dll,PrintUIEntry /if /b ""{cm:printername}"" /f {win}\inf\ntprint.inf /r ""yajhfc:"" /m ""Apple LaserWriter 16/600 PS"""; Components: redmon; StatusMsg: Installing fax printer...; Check: NoYajHFCPrinter; BeforeInstall: InstallYajHFCPort(); MinVersion: 0,5.0; OnlyBelowVersion: 0,6.0
Filename: rundll32; Parameters: "printui.dll,PrintUIEntry /if /b ""{cm:printername}"" /f {win}\inf\ntprint.inf /r ""yajhfc:"" /m ""FX DP 305-AP PS"""; Components: redmon; StatusMsg: Installing fax printer...; Check: NoYajHFCPrinter; BeforeInstall: InstallYajHFCPort(); MinVersion: 0,6.0; Tasks: 
Filename: {app}\w98info.txt; Flags: shellexec; OnlyBelowVersion: 0,5.0; Components: redmon
[UninstallRun]
Filename: rundll32; Components: redmon; Parameters: "printui.dll,PrintUIEntry /dl /n ""{cm:printername}"""; RunOnceId: DeletePrinter; MinVersion: 0,5.0
[INI]
Filename: {group}\Homepage.url; Section: InternetShortcut; Key: URL; String: http://yajhfc.berlios.de/
[UninstallDelete]
Type: files; Name: {group}\Homepage.url
[CustomMessages]
redmonname=Redirected Port
printername=YajHFC Fax
[Tasks]
Name: DesktopIcon; Description: Create desktop icon; Components: base
[Code]
function InitializeSetup(): Boolean;
var javaver: string;
    javamajor, javaminor, p: integer;
begin
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
