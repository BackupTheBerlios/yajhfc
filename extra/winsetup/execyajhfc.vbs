' This script starts YajHFC by reading the location of the Java installation from the Registry.
'
' This is necessary in Vista amd64 because javaw.exe can't be found in the "normal" PATH there.
' With all other versions of Windows the effect of this script should be roughly equivalent
' to invoking "javaw -jar yajhfc.jar"

dim wshshell, javahome, classpath, fso, launchargs, cmdline

const jarname = "yajhfc.jar"
const launchclass = "yajhfc.Launcher"

Set wshshell = WScript.CreateObject("WScript.Shell")
Set fso = WScript.CreateObject("Scripting.FileSystemObject")

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

javahome = getJavaHome()
if javahome = "" then
	MsgBox "Could not find the location where the Java Runtime Environment has been installed. Please check if Java is installed correctly.", vbCritical, "Start YajHFC"
	WScript.Exit(1)
end if

on error goto 0
' Set current directory to app directory:
wshshell.CurrentDirectory = fso.getParentFolderName(WScript.ScriptFullName)

' Add all files in the "lib" sub-directory to the class path
if (fso.FolderExists("lib")) then
  dim libs,env
  set env = WshShell.Environment("PROCESS")
  classpath = env("CLASSPATH")

  set libs = fso.getFolder("lib")
  for each file in libs.files
    if len(file) > 4 then
      if lcase(right(file, 4)) = ".jar" then
        classpath = classpath & ";lib\" & file
      end if
    end if
  next
  classpath = classpath & ";" & jarname
  env("CLASSPATH") = classpath

  launchargs = launchclass
else
  launchargs = "-jar " & jarname
end if


cmdline = """" + javahome & "bin\javaw.exe"" " & launchargs & " "

' Append any command line arguments
set args = WScript.Arguments
For I = 0 to args.Count - 1
   arg = args(I)
   if InStr(arg, " ") > 0 then
      	cmdline = cmdline & """" & args(I) & """ "
   else 
	cmdline = cmdline & args(I) & " "
   end if
Next

wshshell.run cmdline

