dim wshshell, cmdline

Set wshshell = WScript.CreateObject("WScript.Shell")

function getJavaHome
	dim javaver
	getJavaHome = ""
	on error resume next
	err.clear
	javaver = wshshell.RegRead("HKLM\SOFTWARE\JavaSoft\Java Runtime Environment\CurrentVersion")
	if err = 0 and javaver <> "" then
		getJavaHome = wshshell.regread("HKLM\SOFTWARE\JavaSoft\Java Runtime Environment\" + javaver + "\JavaHome")
	end if
end function

cmdline = getJavaHome()
if cmdline = "" then
	MsgBox "Could not find the location where the Java Runtime Environment has been installed. Please check if Java is installed correctly.", vbCritical, "Start YajHFC"
	WScript.Exit(1)
end if

set args = WScript.Arguments

cmdline = """" + cmdline + "bin\" + args(0) + """ "

For I = 1 to args.Count - 1
   arg = args(I)
   if InStr(arg, " ") > 0 then
      	cmdline = cmdline + """" + args(I) + """ "
   else 
	cmdline = cmdline + args(I) + " "
   end if
Next

msgbox cmdline
wshshell.run cmdline

