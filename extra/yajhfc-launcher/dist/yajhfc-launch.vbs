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

Dim wshhell, userprofile, java, strSrc, strDst, homedrv, homedir, env

Set WshShell = WScript.CreateObject("WScript.Shell")

' Get the correct user profile path from the registry
homedrv =  wshshell.regread("HKEY_CURRENT_USER\Volatile Environment\HOMEDRIVE")
homedir = wshshell.regread("HKEY_CURRENT_USER\Volatile Environment\HOMEPATH")
userprofile = homedrv & homedir

' Fix some enviroment variables:
set env = WshShell.Environment("PROCESS")
env("HOMEDRIVE") = homedrv
env("HOMEDIR") = homedir
env("USERPROFILE") = userprofile

' Launch java:
Set java = WshShell.Exec(javaexe & " -Duser.home=""" & userprofile & """ " & yajhfcargs)

' Copy stdin of this script over to the java executable:
Set strSrc = WScript.StdIn
Set strDst = java.StdIn

Do While (java.Status = 0 and not strSrc.AtEndOfStream)
	strDst.Write strSrc.Read(4000)
Loop
strDst.Close

'Wait for termination of YajHFC:
Do While (java.Status = 0)
	WScript.Sleep(200)
Loop
