' Start YajHFC producing a log file

Dim fileName

On Error Resume Next
Set objDialog = CreateObject("UserAccounts.CommonDialog")

If Err.Number = 0 then
  on error goto 0
  objDialog.Filter = "Log Files (*.log)|*.log|Text Files (*.txt)|*.txt|All Files|*.*"
  'objDialog.DialogTitle = "Select log file name"
  'objDialog.FileTitle = "yajhfc.log"
  objDialog.Flags = &H806

  intResult = objDialog.ShowOpen
 
  If intResult = 0 Then
      Wscript.Quit
  Else
      fileName = objDialog.FileName
  End If
Else
  on error goto 0
  fileName = InputBox("Please enter the desired log file name:", "Log file name", "C:\yajhfc.log")
  if (fileName = "") then
    WScript.Quit
  end if
End if
MsgBox fileName

