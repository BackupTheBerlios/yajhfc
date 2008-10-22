' This script is a wrapper to work around a bug that may show up
'  when Java gets started by RedMon.
'  See http://yajhfc.berlios.de/doc/yajhfc-launcher.html for details how to use it.

'Configurable Parameters:

'Set this to the command line arguments to java.exe needed to
' start YajHFC:
Const yajhfcargs = "-jar ""c:\program files\yajhfc.jar"" --stdin --background"

' The Java executable. Using just "java.exe" should be fine in most cases.
Const javaexe = "java.exe"

'''''''''''''''''''''''''''''''''''''''''''''''''''''''''
' You usually should not need to edit below this line '''
'''''''''''''''''''''''''''''''''''''''''''''''''''''''''

Dim wshhell, userprofile, java, strSrc, strDst, homedrv, homedir, env, yjargs

Set WshShell = WScript.CreateObject("WScript.Shell")

On Error Resume Next

set env = WshShell.Environment("PROCESS")

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

if IsEmpty(UserProfile) or UserProfile = "" then
	MsgBox "Could not determine User Profile Path. Cancelling YajHFC launch.", vbCritical, "Error"
	WScript.Quit 1
end if

' Fix %USERPROFILE% in Environment:
env("USERPROFILE") = userprofile

' Fix temp dir
tmpdir  = WshShell.ExpandEnvironmentStrings(wshshell.regread("HKEY_CURRENT_USER\Environment\TMP"))
tempdir = WshShell.ExpandEnvironmentStrings(wshshell.regread("HKEY_CURRENT_USER\Environment\TEMP"))

if fso.FolderExists(tmpdir) then
	env("TMP") = tmpdir
end if
if fso.FolderExists(tempdir) then
	env("TEMP") = tempdir
end if

on error goto 0

if yajhfcargs = ":registry:" then
	yjargs = wshshell.regread("HKLM\Software\YajHFC\printlaunchparams")
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


