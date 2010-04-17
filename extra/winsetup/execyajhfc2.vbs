' This script starts YajHFC by reading the location of the Java installation from the Registry.
'
' This is necessary in Vista amd64 because javaw.exe can't be found in the "normal" PATH there.
' With all other versions of Windows the effect of this script should be roughly equivalent
' to invoking "javaw -jar yajhfc.jar"

''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''
' Command line Parameters: '''''''''''''''''''''''''''''''''''''''''''''''''''''
''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''
' 
' Command line parameters to execyajhfc.vbs start with "/" and must be given
' *before* any parameters to YajHFC (starting with "-")
'
' Supported parameters:
' /wait          Wait for java.exe to exit
' /printlaunch   Start YajHFC in "printlaunch" mode
' /nosaveenv     Do not save environment variables for printlaunch mode
'
''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''
' You should not need to edit below this line ''''''''''''''''''''''''''''''''''
''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''
option explicit

dim wshshell, fso, env
dim printlaunchmode, dowait, dosaveenv, copystdin

const jarname              = "yajhfc.jar"
const launchclass          = "yajhfc.Launcher"
const regkey               = "HKCU\Software\YajHFC\"

Set wshshell = WScript.CreateObject("WScript.Shell")
Set fso = WScript.CreateObject("Scripting.FileSystemObject")
Set env = wshshell.Environment("PROCESS")

function getJavaHome
	dim javaver, result, prefix
	getJavaHome = ""
	on error resume next
	err.clear
	prefix = "HKLM\SOFTWARE\JavaSoft\Java Runtime Environment\"
	javaver = wshshell.RegRead(prefix & "CurrentVersion")
	if err <> 0 or javaver = "" then
		err.clear
		' Try if we are on a 64bit system and have only 32bit Java installed
		prefix = "HKLM\SOFTWARE\Wow6432Node\JavaSoft\Java Runtime Environment\"
		javaver = wshshell.RegRead(prefix & "CurrentVersion")
	end if
	
	if err = 0 and javaver <> "" then
		result = wshshell.regread(prefix & javaver & "\JavaHome")
		if (err = 0 and len(result) > 1) then
		  ' Append backslash if necessary
		  dim lastchar
		  lastchar = right(result, 1)
		  if (lastchar <> "\" and lastchar <> "/") then
		    result = result & "\"
		  end if
		  getJavaHome = result
		end if
	end if
end function

' Return java arguments
function getJavaArgs
	' Set current directory to app directory:
	wshshell.CurrentDirectory = fso.getParentFolderName(WScript.ScriptFullName)

	' Add all files in the "lib" sub-directory to the class path
	if (fso.FolderExists("lib")) then
	  dim libs, classpath, file
	  classpath = env("CLASSPATH")
	
	  set libs = fso.getFolder("lib")
	  for each file in libs.files
	    if len(file) > 4 then
	      if lcase(right(file, 4)) = ".jar" then
	        classpath = classpath & ";" & file
	      end if
	    end if
	  next
	  classpath = classpath & ";" & jarname
	  env("CLASSPATH") = classpath
	
	  getJavaArgs = launchclass
	else
	  getJavaArgs = "-jar " & jarname
	end if
end function

' Return java arguments as saved in the registry for printlaunch mode
function getJavaArgsFromRegistry(userprofile)
	getJavaArgsFromRegistry = "-Duser.home=""" & userprofile & """ " & getJavaArgs()
	on error resume next
	getJavaArgsFromRegistry = getJavaArgsFromRegistry & " " & wshshell.regread("HKLM\Software\YajHFC\printlaunchyajhfcparams")
	on error goto 0
end function

' Save correct values for enviroment variables when running in normal mode
sub saveCorrectEnv
	on error resume next
	' Save the correct values for user profile and TEMP for the print launcher
	WshShell.RegWrite regkey & "USERPROFILE", env("USERPROFILE"), "REG_SZ"
	WshShell.RegWrite regkey & "TMP", env("TMP"), "REG_SZ"
	WshShell.RegWrite regkey & "TEMP", env("TEMP"), "REG_SZ"
	on error goto 0
end sub

' Fix the environment in printlaunch mode and return the user profile path
function fixPrintEnv
	dim userprofile, homedrv, homedir, tmpdir, tempdir, haveRegistry
	
	on error resume next
	' Check if we have already written the correct value for the user profile
	userprofile = wshshell.regread(regkey + "USERPROFILE")
	if (IsEmpty(userprofile)) then
		
		' If not, try to get the correct user profile path from the registry
		homedrv =  wshshell.regread("HKEY_CURRENT_USER\Volatile Environment\HOMEDRIVE")
		homedir = wshshell.regread("HKEY_CURRENT_USER\Volatile Environment\HOMEPATH")
	
		haveRegistry = fso.FileExists(homedrv & homedir & "\ntuser.dat")
	
		if (IsEmpty(homedrv) Or IsEmpty(homedir) or (not haveRegistry)) then
			' Keys not found in Volatile Env., try the "hard way"
			dim WshNet, WMIService, Account
			Set WshNet = WScript.CreateObject("WScript.Network")
			Set WMIService = GetObject("winmgmts:\\.\root\cimv2")
			Set Account = WMIService.Get("Win32_UserAccount.Name='" & WshNet.UserName & "',Domain='" & WshNet.UserDomain & "'")
	
			userprofile = WshShell.ExpandEnvironmentStrings(WshShell.RegRead("HKEY_LOCAL_MACHINE\SOFTWARE\Microsoft\Windows NT\CurrentVersion\ProfileList\" + Account.SID + "\ProfileImagePath"))
			if (IsEmpty(userprofile)) then
				' Probably domain name was empty, try to find it in registry
				Set Account = WMIService.Get("Win32_UserAccount.Name='" & WshNet.UserName & "',Domain='" & _
				wshshell.regread("HKEY_LOCAL_MACHINE\Software\Microsoft\Windows NT\CurrentVersion\Winlogon\CachePrimaryDomain") & "'")
		
				userprofile = WshShell.ExpandEnvironmentStrings(WshShell.RegRead("HKEY_LOCAL_MACHINE\SOFTWARE\Microsoft\Windows NT\CurrentVersion\ProfileList\" + Account.SID + "\ProfileImagePath"))
			end if
		else
			' Fix some enviroment variables:
			env("HOMEDRIVE") = homedrv
			env("HOMEDIR") = homedir
	
			userprofile = homedrv & homedir
		end if
	end if
	
	if IsEmpty(UserProfile) or UserProfile = "" then
		MsgBox "Could not determine User Profile Path. Cancelling YajHFC launch.", vbCritical, "Error"
		WScript.Quit 1
	end if
	
	' Fix %USERPROFILE% in Environment:
	env("USERPROFILE") = userprofile
	
	' Fix temp dir
	tmpdir = wshshell.regread(regkey + "TMP") 'Check for value by execyajhfc
	if isEmpty(tmpdir) then
		tmpdir  = WshShell.ExpandEnvironmentStrings(wshshell.regread("HKEY_CURRENT_USER\Environment\TMP"))
	end if
	tempdir = wshshell.regread(regkey + "TEMP") 'Check for value by execyajhfc
	if isEmpty(tempdir) then
		tempdir = WshShell.ExpandEnvironmentStrings(wshshell.regread("HKEY_CURRENT_USER\Environment\TEMP"))
	end if
	
	if fso.FolderExists(tmpdir) then
		env("TMP") = tmpdir
	end if
	if fso.FolderExists(tempdir) then
		env("TEMP") = tempdir
	end if
	
	on error goto 0
	fixPrintEnv = userprofile
end function

' Parses the command line and returns a string with the arguments to pass
' to YajHFC
function parseCommandLine
	dim args, arg, cmdline, foundyajarg, i
	' Default values for command line args:
	dowait = False
	printlaunchmode = False
	dosaveenv = True
	
	foundyajarg = False
	
	' Append any command line arguments
	set args = WScript.Arguments
	For I = 0 to args.Count - 1
		arg = args(I)
		if foundyajarg or left(arg, 1) <> "/" then
			if InStr(arg, " ") > 0 then
				cmdline = cmdline & """" & args(I) & """ "
			else 
				cmdline = cmdline & args(I) & " "
			end if
			foundyajarg = True
		else
			select case arg
			case "/wait"
				dowait = True
			case "/printlaunch"
				printlaunchmode = True
				dowait = True
				dosaveenv = False
			case "/nosaveenv"
				dosaveenv = False
			case else
				WScript.Echo "Unknown command line argument: " & arg
			end select
		end if
	Next
	parseCommandLine = cmdline
end function

dim javahome, launchargs, javaexe, commandline

javahome = getJavaHome()
if javahome = "" then
	MsgBox "Could not find the location where the Java Runtime Environment has been installed. Please check if Java is installed correctly.", vbCritical, "Start YajHFC"
	WScript.Quit(1)
end if

launchargs = parseCommandLine

if printlaunchmode then
	dim userprofile
	userprofile = fixPrintEnv
	launchargs = getJavaArgsFromRegistry(userprofile) & " " & launchargs
	javaexe = "java.exe"
	copystdin = True
else
	launchargs = getJavaArgs & " " & launchargs
	javaexe = "javaw.exe"
	copystdin = False
	if dosaveenv then
		saveCorrectEnv
	end if
end if

commandline = """" & javahome & "bin\" & javaexe & """ " & launchargs 

'msgbox """" & commandline & """, dowait = " & dowait & ", printlaunchmode = " & printlaunchmode & ", dosaveenv = " & dosaveenv

if copystdin then
	dim java, strSrc, strDst

	' Launch java:
	Set java = WshShell.Exec(commandline)
	
	' Copy stdin of this script over to the java executable:
	Set strSrc = WScript.StdIn
	Set strDst = java.StdIn
		
	Do While (java.Status = 0 and not strSrc.AtEndOfStream)
		On Error Resume Next
		strDst.Write strSrc.Read(4000)
		if (err <> 0) then
			Wscript.echo "Error #" & CStr(Err.Number) & ": " & Err.Description
			Err.Clear
		end if
	Loop
	strDst.Close

	'Wait for termination of YajHFC:
	Do While (java.Status = 0)
		WScript.Sleep(200)
	Loop
	
	' Copy eventual error messages
	'wscript.stdout.write java.stdout.readAll()
	'wscript.stderr.write java.stderr.readAll()
else
	wshshell.run commandline, , dowait
end if

