' This script is a wrapper to work around a bug that may show up
'  when Java gets started by RedMon.
'  See http://yajhfc.berlios.de/doc/yajhfc-launcher.html for details how to use it.

'Configurable Parameters:

'Set this to the command line arguments to java.exe needed to
' start YajHFC 
' *or* to ":registry:" to use the value from HKEY_LOCAL_MACHINE\Software\YajHFC\printlaunchparams
Const yajhfcargs = ":registry:"


'''''''''''''''''''''''''''''''''''''''''''''''''''''''''
' You usually should not need to edit below this line '''
'''''''''''''''''''''''''''''''''''''''''''''''''''''''''

const regkey = "HKCU\Software\YajHFC\"

Dim wshhell, userprofile, java, strSrc, strDst, homedrv, homedir, env, yjargs, javaexe, fso

Set WshShell = WScript.CreateObject("WScript.Shell")
Set fso = CreateObject("Scripting.FileSystemObject")

function getJavaHome
	dim javaver, result
	getJavaHome = ""
	on error resume next
	err.clear
	javaver = wshshell.RegRead("HKLM\SOFTWARE\JavaSoft\Java Runtime Environment\CurrentVersion")
	if err = 0 and javaver <> "" then
		result = wshshell.regread("HKLM\SOFTWARE\JavaSoft\Java Runtime Environment\" + javaver + "\JavaHome")
		if (err = 0 and len(result) > 1) then
		  dim lastchar
		  lastchar = right(result, 1)
		  if (lastchar <> "\" and lastchar <> "/") then
		    result = result & "\"
		  end if
		  getJavaHome = result
		end if
	end if
end function

' Find out the location of java:
javaexe = getJavaHome()
if javaexe = "" then
	MsgBox "Could not find the location where the Java Runtime Environment has been installed. Please check if Java is installed correctly.", vbCritical, "Start YajHFC"
	WScript.Exit(1)
end if
javaexe = javaexe & "bin\java.exe"

On Error Resume Next

set env = WshShell.Environment("PROCESS")

' Check if execyajhfc has written the correct value for the user profile
userprofile = wshshell.regread(regkey + "USERPROFILE")
if (IsEmpty(userprofile)) then
	
	' Try to get the correct user profile path from the registry
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

if yajhfcargs = ":registry:" then
	yjargs = wshshell.regread("HKLM\Software\YajHFC\printlaunchparams")
	on error resume next
	yjargs = yjargs & " " & wshshell.regread("HKLM\Software\YajHFC\printlaunchyajhfcparams")
	on error goto 0
else
	yjargs = yajhfcargs
end if

' Launch java:
Set java = WshShell.Exec("""" & javaexe & """ -Duser.home=""" & userprofile & """ " & yjargs)

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

