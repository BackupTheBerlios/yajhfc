package yajhfc;
/*
 * YAJHFC - Yet another Java Hylafax client
 * Copyright (C) 2005-2006 Jonas Wolz
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

import static yajhfc.utils._;
import gnu.hylafax.HylaFAXClient;
import gnu.hylafax.Job;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.print.PrinterException;
import java.io.File;
import java.net.SocketException;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;
import javax.swing.UIManager;
import javax.swing.JTable.PrintMode;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;

import yajhfc.PluginManager.PluginMenuCreator;
import yajhfc.filters.CustomFilterDialog;
import yajhfc.filters.FilterCreator;
import yajhfc.filters.StringFilter;
import yajhfc.filters.StringFilterOperator;
import yajhfc.filters.Filter;
import yajhfc.phonebook.NewPhoneBookWin;
import yajhfc.readstate.PersistentReadState;
import yajhfc.send.SendController;
import yajhfc.send.SendWinControl;
import yajhfc.util.JTableTABAction;

@SuppressWarnings("serial")
public final class mainwin extends JFrame {
    
    private static final Logger log = Logger.getLogger(mainwin.class.getName());
    // Uncomment for archive support (change 3 -> 4)
    private static final int TABLE_COUNT = 3;
    
    protected JPanel jContentPane = null;
    
    protected JToolBar toolbar;
    
    protected JTabbedPane tabMain = null;
    
    protected JScrollPane scrollRecv = null;
    protected JScrollPane scrollSent = null;
    protected JScrollPane scrollSending = null;
    
    protected TooltipJTable tableRecv = null;
    protected TooltipJTable tableSent = null;
    protected TooltipJTable tableSending = null;
    
    protected UnReadMyTableModel recvTableModel = null;  
    protected MyTableModel sentTableModel = null;  
    protected MyTableModel sendingTableModel = null; 
    
    protected NumberRowViewport recvRowNumbers, sentRowNumbers, sendingRowNumbers;
    
    protected JTextPane textStatus = null;
    
    protected JMenuBar jJMenuBar = null;
    
    protected JMenu menuFax = null;
    protected JMenu menuView = null;
    protected JMenu menuExtras = null;
    protected JMenu helpMenu = null;
    
    protected JCheckBoxMenuItem menuMarkError;
    
    protected JRadioButtonMenuItem menuViewAll, menuViewOwn, menuViewCustom;
    protected ButtonGroup viewGroup;
    
    protected FaxOptions myopts = null;
      
    protected java.util.Timer utmrTable;
    protected TableRefresher tableRefresher = null;
    protected StatusRefresher statRefresher = null;
   
    protected MouseListener tblMouseListener;
    protected KeyListener tblKeyListener;
    protected DefaultTableCellRenderer hylaDateRenderer;
    
    protected JPopupMenu tblPopup;
    
    protected ProgressPanel tablePanel;
    
    protected MenuViewListener menuViewListener;
    
    // Uncomment for archive support.
//    protected TooltipJTable tableArchive;
//    protected JScrollPane scrollArchive;
//    protected ArchiveTableModel archiveTableModel;
//    protected NumberRowViewport archiveRowNumbers;
    
    // Actions:
    protected Action actSend, actShow, actDelete, actOptions, actExit, actAbout, actPhonebook, actReadme, actPoll, actFaxRead, actFaxSave, actForward, actAdminMode;
    protected Action actRefresh, actResend, actPrintTable, actSuspend, actResume, actClipCopy, actShowRowNumbers, actAdjustColumns, actReconnect;
    protected ActionEnabler actChecker;
    
    protected HylaClientManager clientManager;
    
    public enum SendReadyState {
        Ready, NeedToWait, NotReady;
    }
    protected SendReadyState sendReady = SendReadyState.NeedToWait;
    
    // Worker classes:
    private class DeleteWorker extends ProgressWorker {
        private TooltipJTable selTable;
        
        @Override
        protected int calculateMaxProgress() {
            return 20 + 10*selTable.getSelectedRowCount();
        }
        
        @Override
        public void doWork() {
            HylaFAXClient hyfc = clientManager.beginServerTransaction(mainwin.this);
            if (hyfc == null) {
                return;
            }
            int[] selRows =  selTable.getSelectedRows();

            for (int i : selRows) {
                YajJob yj = null;
                try {
                    yj = selTable.getJobForRow(i);
                    updateNote(MessageFormat.format(_("Deleting fax {0}"), yj.getIDValue()));
                    
                    yj.delete(hyfc);
                    
                    stepProgressBar(10);
                } catch (Exception e1) {
                    String msgText;
                    if (yj == null)
                        msgText = _("Error deleting a fax:\n");
                    else
                        msgText = MessageFormat.format(_("Error deleting the fax \"{0}\":\n"), yj.getIDValue());
                    //JOptionPane.showMessageDialog(mainwin.this, msgText + e1.getMessage(), _("Error"), JOptionPane.ERROR_MESSAGE);
                    showExceptionDialog(msgText, e1);
                }
            }
            
            clientManager.endServerTransaction();
        }
        @Override
        protected void done() {
            refreshTables();
        }
        
        public DeleteWorker(TooltipJTable selTable) {
            this.selTable = selTable;
            this.progressMonitor = tablePanel;
            this.setCloseOnExit(false);
        }
    }
    private class MultiSaveWorker extends ProgressWorker {
        private TooltipJTable selTable;
        private File targetDir;
        private int fileCounter;
        
        @Override
        protected int calculateMaxProgress() {
            return 1000*selTable.getSelectedRowCount();
        }
        
        @Override
        public void doWork() {
            fileCounter = 0;
            HylaFAXClient hyfc = clientManager.beginServerTransaction(mainwin.this);
            if (hyfc == null) {
                return;
            }
            int[] selRows =  selTable.getSelectedRows();
            
            for (int i : selRows) {
                YajJob yj = null;
                try {
                    yj = selTable.getJobForRow(i);
                    updateNote(MessageFormat.format(_("Saving fax {0}"), yj.getIDValue()));
                    for(HylaServerFile hsf : yj.getServerFilenames(hyfc)) {
                        try {
                            String filename = hsf.getPath();
                            int seppos = filename.lastIndexOf('/');
                            if (seppos < 0)
                                seppos = filename.lastIndexOf(File.separatorChar);
                            if (seppos >= 0)
                                filename = filename.substring(seppos + 1);
                            
                            File target = new File(targetDir, filename);
                            hsf.download(hyfc, target);
                            fileCounter++;
                        } catch (Exception e1) {
                            //JOptionPane.showMessageDialog(mainwin.this, MessageFormat.format(_("An error occured saving the file {0} (job {1}):\n"), hsf.getPath(), yj.getIDValue()) + e1.getMessage() , _("Error"), JOptionPane.ERROR_MESSAGE);
                            showExceptionDialog(MessageFormat.format(_("An error occured saving the file {0} (job {1}):"), hsf.getPath(), yj.getIDValue()), e1);
                        }
                    }
                } catch (Exception e1) {
                    //JOptionPane.showMessageDialog(mainwin.this, _("An error occured saving the fax:\n") + e1.getMessage(), _("Error"), JOptionPane.ERROR_MESSAGE);
                    showExceptionDialog(_("An error occured saving the fax:"), e1);
                }
                stepProgressBar(1000);
            }
            
            clientManager.endServerTransaction();
        }
        
        @Override
        protected void pMonClosed() {
            JOptionPane.showMessageDialog(mainwin.this, MessageFormat.format(_("{0} files saved to directory {1}."), fileCounter, targetDir.getPath()), _("Faxes saved"), JOptionPane.INFORMATION_MESSAGE);
        }
        
        public MultiSaveWorker(TooltipJTable selTable, File targetDir) {
            this.selTable = selTable;
            this.targetDir = targetDir;
            this.progressMonitor = tablePanel;
            this.setCloseOnExit(true);
        }
    }
    private class ShowWorker extends ProgressWorker {
        private TooltipJTable selTable;
        private int sMin, sMax;
        
        @Override
        protected int calculateMaxProgress() {
            return 100 + 1100*selTable.getSelectedRowCount();
        }
        
        @Override
        public void doWork() {
            HylaFAXClient hyfc = clientManager.beginServerTransaction(mainwin.this);
            if (hyfc == null) {
                return;
            }
            int[] selRows =  selTable.getSelectedRows();
            sMin = Integer.MAX_VALUE; sMax = Integer.MIN_VALUE;
            for (int i : selRows) {
                YajJob yj = null;
                try {
                    yj = selTable.getJobForRow(i);
                    updateNote(MessageFormat.format(_("Displaying fax {0}"), yj.getIDValue()));
                    
                    //System.out.println("" + i + ": " + yj.getIDValue().toString());
                    List<HylaServerFile> serverFiles = yj.getServerFilenames(hyfc);

                    stepProgressBar(100);
                    if (serverFiles.size() == 0) {
                        showMessageDialog(MessageFormat.format(_("No document files available for the fax \"{0}\"."), yj.getIDValue()), _("Display fax"), JOptionPane.INFORMATION_MESSAGE);

                       stepProgressBar(1000);
                    } else {
                        int step = 1000 / serverFiles.size();
                        for(HylaServerFile hsf : serverFiles) {
                            try {
                                hsf.view(hyfc);
                                //System.out.println(hsf.getPath());
                            } catch (Exception e1) {
                                //JOptionPane.showMessageDialog(mainwin.this, MessageFormat.format(_("An error occured displaying the file {0} (job {1}):\n"), hsf.getPath(), yj.getIDValue()) + e1.getMessage() , _("Error"), JOptionPane.ERROR_MESSAGE);
                                ExceptionDialog.showExceptionDialog(mainwin.this, MessageFormat.format(_("An error occured displaying the file {0} (job {1}):\n"), hsf.getPath(), yj.getIDValue()), e1);
                            }
                            stepProgressBar(step);
                        }
                    }
                    if (yj instanceof RecvYajJob) {
                        ((RecvYajJob)yj).setRead(true);
                        if (i < sMin)
                            sMin = i;
                        if (i > sMax)
                            sMax = i;
                    }
                } catch (Exception e1) {
                    //JOptionPane.showMessageDialog(mainwin.this, MessageFormat.format(_("An error occured displaying the fax \"{0}\":\n"), yj.getIDValue()) + e1.getMessage(), _("Error"), JOptionPane.ERROR_MESSAGE);
                    showExceptionDialog(MessageFormat.format(_("An error occured displaying the fax \"{0}\":"), yj.getIDValue()), e1);
                }
            }
            clientManager.endServerTransaction();
        }
        
        @Override
        protected void done() {
            if (sMax >= 0 && selTable == tableRecv) {
                tableRecv.getSorter().fireTableRowsUpdated(sMin, sMax);
                actFaxRead.putValue(ActionJCheckBoxMenuItem.SELECTED_PROPERTY, true);
            }
        }
        
        public ShowWorker(TooltipJTable selTable) {
            this.selTable = selTable;
            this.progressMonitor = tablePanel;
            this.setCloseOnExit(true);
        }
    }
    // Worker classes:
    private class SuspendWorker extends ProgressWorker {
        private TooltipJTable selTable;
        
        @Override
        protected int calculateMaxProgress() {
            return 20 + 10*selTable.getSelectedRowCount();
        }
        
        @Override
        public void doWork() {
            HylaFAXClient hyfc = clientManager.beginServerTransaction(mainwin.this);
            if (hyfc == null) {
                return;
            }
            int[] selRows =  selTable.getSelectedRows();

            for (int i : selRows) {
                SendingYajJob yj = null;
                try {
                    yj = (SendingYajJob)selTable.getJobForRow(i);
                    updateNote(MessageFormat.format(_("Suspending job {0}"), yj.getIDValue()));
                    
                    char jobstate = yj.getJobState();
                    if (jobstate == SentYajJob.JOBSTATE_RUNNING) {
                        if (showConfirmDialog(MessageFormat.format(_("Suspending the currently running job {0} may block until it is done (or switch to another \"non running state\"). Try to suspend it anyway?") , yj.getIDValue()),
                                _("Suspend fax job"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                            yj.suspend(hyfc);
                        }
                    } else {
                        yj.suspend(hyfc);
                    }
                    
                    stepProgressBar(10);
                } catch (Exception e1) {
                    String msgText;
                    if (yj == null)
                        msgText = _("Error suspending a fax job:\n");
                    else
                        msgText = MessageFormat.format(_("Error suspending the fax job \"{0}\":\n"), yj.getIDValue());
                    showExceptionDialog(msgText, e1);
                }
            }
            clientManager.endServerTransaction();
        }
        @Override
        protected void done() {
            refreshTables();
        }
        
        public SuspendWorker(TooltipJTable selTable) {
            this.selTable = selTable;
            this.progressMonitor = tablePanel;
            this.setCloseOnExit(false);
        }
    }
    // Worker classes:
    private class ResumeWorker extends ProgressWorker {
        private TooltipJTable selTable;
        
        @Override
        protected int calculateMaxProgress() {
            return 20 + 10*selTable.getSelectedRowCount();
        }
        
        @Override
        public void doWork() {
            HylaFAXClient hyfc = clientManager.beginServerTransaction(mainwin.this);
            if (hyfc == null) {
                return;
            }
            int[] selRows =  selTable.getSelectedRows();

            for (int i : selRows) {
                SendingYajJob yj = null;
                try {
                    yj = (SendingYajJob)selTable.getJobForRow(i);
                    updateNote(MessageFormat.format(_("Resuming job {0}"), yj.getIDValue()));
                    char jobstate = yj.getJobState();
                    if (jobstate != SentYajJob.JOBSTATE_SUSPENDED) {
                        if (showConfirmDialog(MessageFormat.format(_("Job {0} is not in state \"Suspended\" so resuming it probably will not work. Try to resume it anyway?") , yj.getIDValue()),
                                _("Resume fax job"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                            yj.resume(hyfc);
                        }
                    } else {
                        yj.resume(hyfc);
                    }
                    
                    stepProgressBar(10);
                } catch (Exception e1) {
                    String msgText;
                    if (yj == null)
                        msgText = _("Error resuming a fax job:\n");
                    else
                        msgText = MessageFormat.format(_("Error resuming the fax job \"{0}\":\n"), yj.getIDValue());
                    showExceptionDialog(msgText, e1);
                }
            }
            clientManager.endServerTransaction();
        }
        @Override
        protected void done() {
            refreshTables();
        }
        
        public ResumeWorker(TooltipJTable selTable) {
            this.selTable = selTable;
            this.progressMonitor = tablePanel;
            this.setCloseOnExit(false);
        }
    }
    
    // Creates all actions:
    private void createActions(boolean adminState) {
        actOptions = new ExcDialogAbstractAction() {
            public void actualActionPerformed(java.awt.event.ActionEvent e) {
                utils.setWaitCursor(null);
                
                SendReadyState oldState = sendReady;
                sendReady = SendReadyState.NeedToWait;
                
                List<HylaModem> modems;
                if (clientManager != null) {
                    try {
                        modems = clientManager.getModems();
                        if (modems == null) {
                            modems = HylaModem.defaultModems;
                        }
                    } catch (Exception ex) {
                        modems = HylaModem.defaultModems;
                    }
                } else {
                    modems = HylaModem.defaultModems;
                }
                
                OptionsWin ow = new OptionsWin(myopts, mainwin.this, modems);
                ow.setModal(true);
                utils.unsetWaitCursorOnOpen(null, ow);
                ow.setVisible(true);
                if (ow.getModalResult()) 
                    reconnectToServer(null);
                else
                    sendReady = oldState;
                    
            }
        };
        actOptions.putValue(Action.NAME, _("Options") + "...");
        actOptions.putValue(Action.SHORT_DESCRIPTION, _("Shows the Options dialog"));
        actOptions.putValue(Action.SMALL_ICON, utils.loadIcon("general/Preferences"));
        
        actSend = new ExcDialogAbstractAction() {
            public void actualActionPerformed(java.awt.event.ActionEvent e) {
                utils.setWaitCursor(null);
                SendWinControl sw = SendController.createSendWindow(mainwin.this, clientManager, false, false);

                utils.unsetWaitCursorOnOpen(null, sw.getWindow());
                sw.setVisible(true);
                if (sw.getModalResult()) {
                    refreshTables();
                }
            }
        };
        actSend.putValue(Action.NAME, _("Send") + "...");
        actSend.putValue(Action.SHORT_DESCRIPTION, _("Shows the send fax dialog"));
        actSend.putValue(Action.SMALL_ICON, utils.loadIcon("general/SendMail"));
        
        actPoll = new ExcDialogAbstractAction() {
            public void actualActionPerformed(java.awt.event.ActionEvent e) {
                utils.setWaitCursor(null);
                SendWinControl sw = SendController.createSendWindow(mainwin.this, clientManager, true, true);
                utils.unsetWaitCursorOnOpen(null, sw.getWindow());
                sw.setVisible(true);
            }
        };
        actPoll.putValue(Action.NAME, _("Poll") + "...");
        actPoll.putValue(Action.SHORT_DESCRIPTION, _("Shows the poll fax dialog"));
        actPoll.putValue(Action.SMALL_ICON, utils.loadIcon("general/Import"));
        
        actDelete = new ExcDialogAbstractAction() {
            public void actualActionPerformed(ActionEvent e) {
                TooltipJTable selTable = (TooltipJTable)((JScrollPane)tabMain.getSelectedComponent()).getViewport().getView();
                
                String msgText;
                
                if (selTable == tableSending)
                    msgText = _("Do you really want to cancel the selected fax jobs?");
                else
                    msgText = _("Do you really want to delete the selected faxes?");
                
                if (JOptionPane.showConfirmDialog(mainwin.this, msgText, _("Delete faxes"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    DeleteWorker wrk = new DeleteWorker(selTable);
                    wrk.startWork(mainwin.this, _("Deleting faxes"));
                }
            };
            
        };
        actDelete.putValue(Action.NAME, _("Delete"));
        actDelete.putValue(Action.SHORT_DESCRIPTION, _("Deletes the selected fax"));
        actDelete.putValue(Action.SMALL_ICON, utils.loadIcon("general/Delete"));
        
        actShow = new ExcDialogAbstractAction() {
            public void actualActionPerformed(java.awt.event.ActionEvent e) {
                TooltipJTable selTable = (TooltipJTable)((JScrollPane)tabMain.getSelectedComponent()).getViewport().getView();
                
                ShowWorker wrk = new ShowWorker(selTable);
                wrk.startWork(mainwin.this, _("Viewing faxes"));
            }
        };
        actShow.putValue(Action.NAME, _("Show") + "...");
        actShow.putValue(Action.SHORT_DESCRIPTION, _("Displays the selected fax"));
        actShow.putValue(Action.SMALL_ICON, utils.loadIcon("general/Zoom"));
        
        actExit = new ExcDialogAbstractAction() {
            public void actualActionPerformed(ActionEvent e) {
                dispose();
                //System.exit(0);
            }
        };
        actExit.putValue(Action.NAME, _("Exit"));
        actExit.putValue(Action.SHORT_DESCRIPTION, _("Exits the application"));
        actExit.putValue(Action.SMALL_ICON, utils.loadIcon("general/Stop"));
        
        actAbout = new ExcDialogAbstractAction() {
            public void actualActionPerformed(ActionEvent e) {
                utils.setWaitCursor(null);
                //JOptionPane.showMessageDialog(aboutMenuItem.getComponent(), utils.AppName + "\n\n" + _("by Jonas Wolz"), _("About"), JOptionPane.INFORMATION_MESSAGE);
                AboutDialog aDlg = new AboutDialog(mainwin.this);
                aDlg.setMode(AboutDialog.Mode.ABOUT);
                utils.unsetWaitCursorOnOpen(null, aDlg);
                aDlg.setVisible(true);
            }
        };
        actAbout.putValue(Action.NAME, _("About") +  "...");
        actAbout.putValue(Action.SHORT_DESCRIPTION, _("Shows the about dialog"));
        actAbout.putValue(Action.SMALL_ICON, utils.loadIcon("general/About"));
        
        actPhonebook = new ExcDialogAbstractAction() {
            public void actualActionPerformed(ActionEvent e) {
                utils.setWaitCursor(null);
                NewPhoneBookWin pbw = new NewPhoneBookWin(mainwin.this);
                pbw.setModal(true);
                utils.unsetWaitCursorOnOpen(null, pbw);
                pbw.setVisible(true);
            }
        };
        actPhonebook.putValue(Action.NAME, _("Phone book") +  "...");
        actPhonebook.putValue(Action.SHORT_DESCRIPTION, _("Display/edit the phone book"));
        actPhonebook.putValue(Action.SMALL_ICON, utils.loadIcon("general/Bookmarks"));
        
        actReadme = new ExcDialogAbstractAction() {
            public void actualActionPerformed(ActionEvent e) {
                utils.setWaitCursor(null);
                AboutDialog aDlg = new AboutDialog(mainwin.this);
                aDlg.setMode(AboutDialog.Mode.READMES);
                utils.unsetWaitCursorOnOpen(null, aDlg);
                aDlg.setVisible(true);
            }
        };
        actReadme.putValue(Action.NAME, _("Documentation") +  "...");
        actReadme.putValue(Action.SHORT_DESCRIPTION, _("Shows the README files"));
        actReadme.putValue(Action.SMALL_ICON, utils.loadIcon("general/Help"));
        
        actFaxRead = new ExcDialogAbstractAction() {
            public void actualActionPerformed(ActionEvent e) {
                Boolean state = (Boolean)getValue(ActionJCheckBoxMenuItem.SELECTED_PROPERTY);
                boolean newState;
                if (state == null)
                    newState = true;
                else
                    newState = !state;
                
                if (tabMain.getSelectedComponent() == scrollRecv) { // TableRecv
                    int sMin = Integer.MAX_VALUE, sMax = Integer.MIN_VALUE;
                    for (int i:tableRecv.getSelectedRows()) {
                        ((RecvYajJob)tableRecv.getJobForRow(i)).setRead(newState);
                        if (i < sMin)
                            sMin = i;
                        if (i > sMax)
                            sMax = i;
                    }
                    if (sMax >= 0)
                        tableRecv.getSorter().fireTableRowsUpdated(sMin, sMax);
                    putValue(ActionJCheckBoxMenuItem.SELECTED_PROPERTY, newState);
                }
            };
        };
        actFaxRead.putValue(Action.NAME, _("Marked as read"));
        actFaxRead.putValue(Action.SHORT_DESCRIPTION, _("Marks the selected fax as read/unread"));
        actFaxRead.putValue(ActionJCheckBoxMenuItem.SELECTED_PROPERTY, true);
        
        actFaxSave = new ExcDialogAbstractAction() {
            public void actualActionPerformed(ActionEvent e) {
                TooltipJTable selTable = (TooltipJTable)((JScrollPane)tabMain.getSelectedComponent()).getViewport().getView();
                
                HylaFAXClient hyfc = clientManager.beginServerTransaction(mainwin.this);
                if (hyfc == null) {
                    return;
                }
                if (selTable.getSelectedRowCount() == 1) {
                    JFileChooser jfc = new yajhfc.util.SafeJFileChooser();
                        
                    try {
                        YajJob yj = selTable.getJobForRow(selTable.getSelectedRow());
                        List<HylaServerFile> serverFiles = yj.getServerFilenames(hyfc);
                        if (serverFiles.size() == 0) {
                            JOptionPane.showMessageDialog(mainwin.this, MessageFormat.format(_("No document files available for the fax \"{0}\"."), yj.getIDValue()), _("Display fax"), JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            for(HylaServerFile hsf : serverFiles) {
                                try {
                                    String filename = hsf.getPath();
                                    int seppos = filename.lastIndexOf('/');
                                    if (seppos < 0)
                                        seppos = filename.lastIndexOf(File.separatorChar);
                                    if (seppos >= 0)
                                        filename = filename.substring(seppos + 1);
                                    jfc.setDialogTitle(MessageFormat.format(_("Save {0} to"), hsf.getPath()));
                                    jfc.setSelectedFile(new File(utils.getFaxOptions().lastSavePath.length() > 0 ? utils.getFaxOptions().lastSavePath : null, filename));
                                    
                                    jfc.resetChoosableFileFilters();
                                    ExampleFileFilter curFilter = new ExampleFileFilter(hsf.getType().getDefaultExtension(), hsf.getType().getDescription());
                                    jfc.addChoosableFileFilter(curFilter);
                                    jfc.setFileFilter(curFilter);
                                    
                                    if (jfc.showSaveDialog(mainwin.this) == JFileChooser.APPROVE_OPTION) {
                                        hsf.download(hyfc, jfc.getSelectedFile());
                                        utils.getFaxOptions().lastSavePath = jfc.getSelectedFile().getParentFile().getPath();
                                    }
                                } catch (Exception e1) {
                                    //JOptionPane.showMessageDialog(mainwin.this, MessageFormat.format(_("An error occured saving the file {0} (job {1}):\n"), hsf.getPath(), yj.getIDValue()) + e1.getMessage() , _("Error"), JOptionPane.ERROR_MESSAGE);
                                    ExceptionDialog.showExceptionDialog(mainwin.this, MessageFormat.format(_("An error occured saving the file {0} (job {1}):"), hsf.getPath(), yj.getIDValue()), e1);
                                }                               
                            }
                        }
                    } catch (Exception e1) {
                        //JOptionPane.showMessageDialog(mainwin.this, _("An error occured saving the fax:\n")  + e1.getMessage(), _("Error"), JOptionPane.ERROR_MESSAGE);
                        ExceptionDialog.showExceptionDialog(mainwin.this, _("An error occured saving the fax:"), e1);
                    }
                } else {
                    JFileChooser jfc = new yajhfc.util.SafeJFileChooser();
                    jfc.setDialogTitle(_("Select a directory to save the faxes in"));
                    jfc.setApproveButtonText(_("Select"));
                    jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    if (utils.getFaxOptions().lastSavePath.length() > 0) {
                        jfc.setSelectedFile(new File(utils.getFaxOptions().lastSavePath));
                    }
                    if (jfc.showOpenDialog(mainwin.this) == JFileChooser.APPROVE_OPTION) {
                        MultiSaveWorker wrk = new MultiSaveWorker(selTable, jfc.getSelectedFile());
                        wrk.startWork(mainwin.this, _("Saving faxes"));
                        utils.getFaxOptions().lastSavePath = jfc.getSelectedFile().getPath();
                    }
                }
                clientManager.endServerTransaction();
            };
        };
        actFaxSave.putValue(Action.NAME, _("Save fax..."));
        actFaxSave.putValue(Action.SHORT_DESCRIPTION, _("Saves the selected fax on disk"));
        actFaxSave.putValue(Action.SMALL_ICON, utils.loadIcon("general/SaveAs"));
        
        actForward = new ExcDialogAbstractAction() {
            public void actualActionPerformed(ActionEvent e) {
                if (tabMain.getSelectedComponent() != scrollRecv || tableRecv.getSelectedRow() < 0)
                    return;
                
                HylaFAXClient hyfc = clientManager.beginServerTransaction(mainwin.this);
                HylaServerFile file;
                try {
                    file = tableRecv.getJobForRow(tableRecv.getSelectedRow()).getServerFilenames(hyfc).get(0);
                } catch (Exception e1) {
                    //JOptionPane.showMessageDialog(mainwin.this, _("Couldn't get a filename for the fax:\n") + e1.getMessage(), _("Error"), JOptionPane.ERROR_MESSAGE);
                    ExceptionDialog.showExceptionDialog(mainwin.this, _("Couldn't get a filename for the fax:"), e1);
                    clientManager.endServerTransaction();
                    return;
                }
                
                SendWinControl sw = SendController.createSendWindow(mainwin.this, clientManager, false, true);
                sw.addServerFile(file);
                sw.setVisible(true);
                refreshTables();
                
                clientManager.endServerTransaction();
            }
        };
        actForward.putValue(Action.NAME, _("Forward fax..."));
        actForward.putValue(Action.SHORT_DESCRIPTION, _("Forwards the fax"));
        actForward.putValue(Action.SMALL_ICON, utils.loadIcon("general/Redo"));
        
        actAdminMode = new ExcDialogAbstractAction() {
            public void actualActionPerformed(ActionEvent e) {
                utils.setWaitCursor(null);
                Boolean state = (Boolean)getValue(ActionJCheckBoxMenuItem.SELECTED_PROPERTY);
                boolean newState;
                if (state == null)
                    newState = false;
                else
                    newState = !state;

                putValue(ActionJCheckBoxMenuItem.SELECTED_PROPERTY, newState);
                
                reconnectToServer(null);
                utils.unsetWaitCursor(null);
            };
        };
        actAdminMode.putValue(Action.NAME, _("Admin mode"));
        actAdminMode.putValue(Action.SHORT_DESCRIPTION, _("Connect to the server in admin mode (e.g. to delete faxes)"));
        actAdminMode.putValue(ActionJCheckBoxMenuItem.SELECTED_PROPERTY, adminState);
        
        actRefresh = new ExcDialogAbstractAction() {
            public void actualActionPerformed(ActionEvent e) {
                utils.setWaitCursor(null);
                refreshStatus();
                refreshTables();
                utils.unsetWaitCursor(null);
            };
        };
        actRefresh.putValue(Action.NAME, _("Refresh"));
        actRefresh.putValue(Action.SHORT_DESCRIPTION, _("Refresh tables and server status"));
        actRefresh.putValue(Action.SMALL_ICON, utils.loadIcon("general/Refresh"));
        actRefresh.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));
        
        actResend = new ExcDialogAbstractAction() {
            public void actualActionPerformed(ActionEvent e) {                
                TooltipJTable selTable = (TooltipJTable)((JScrollPane)tabMain.getSelectedComponent()).getViewport().getView();
                if (!(selTable == tableSent && selTable != tableSending) || selTable.getSelectedRow() < 0)
                    return;
                
                utils.setWaitCursor(null);
                SentYajJob job = (SentYajJob)selTable.getJobForRow(selTable.getSelectedRow());
                
                List<HylaServerFile> files;
                String number, voiceNumber, company, name, location, subject;
                HylaFAXClient hyfc = clientManager.beginServerTransaction(mainwin.this);
                
                try {
                    synchronized (hyfc) {
                        files = job.getServerFilenames(hyfc);
                        
                        Job hyJob = job.getJob(hyfc);
                        number = hyJob.getDialstring();
                        name = hyJob.getProperty("TOUSER");
                        company = hyJob.getProperty("TOCOMPANY");
                        location = hyJob.getProperty("TOLOCATION");
                        voiceNumber = hyJob.getProperty("TOVOICE");
                        subject = hyJob.getProperty("REGARDING");
                    }
                } catch (Exception e1) {
                    //JOptionPane.showMessageDialog(mainwin.this, _("Couldn't get a filename for the fax:\n") + e1.getMessage(), _("Error"), JOptionPane.ERROR_MESSAGE);
                    utils.unsetWaitCursor(null);
                    ExceptionDialog.showExceptionDialog(mainwin.this, _("Could not get all of the job information necessary to resend the fax:"), e1);
                    return;
                } finally {
                    clientManager.endServerTransaction();
                }
                
                SendWinControl sw = SendController.createSendWindow(mainwin.this, clientManager, false, true);
                
                for (HylaServerFile hysf : files) {
                    sw.addServerFile(hysf);
                }
                sw.addRecipient(number, name, company, location, voiceNumber);
                sw.setSubject(subject);
                
                utils.unsetWaitCursorOnOpen(null, sw.getWindow());
                sw.setVisible(true);
                refreshTables();
            };
        };
        actResend.putValue(Action.NAME, _("Resend fax..."));
        actResend.putValue(Action.SHORT_DESCRIPTION, _("Resend the fax"));
        actResend.putValue(Action.SMALL_ICON, utils.loadIcon("general/Export"));
        
        actPrintTable = new ExcDialogAbstractAction() {
            public void actualActionPerformed(ActionEvent e) {
                TooltipJTable selTable = (TooltipJTable)((JScrollPane)tabMain.getSelectedComponent()).getViewport().getView();
                try {
                    MessageFormat header = new MessageFormat(tabMain.getToolTipTextAt(tabMain.getSelectedIndex()));
                    Date now = new Date();
                    MessageFormat footer = new MessageFormat("'" + DateFormat.getDateInstance(DateFormat.SHORT, utils.getLocale()).format(now) + " " + DateFormat.getTimeInstance(DateFormat.SHORT, utils.getLocale()).format(now) + "' - " + utils._("page {0}"));
                    
                    selTable.print(PrintMode.FIT_WIDTH, header, footer);
                } catch (PrinterException pe) {
                    ExceptionDialog.showExceptionDialog(mainwin.this, utils._("Error printing the table:"), pe);
                }
            };
        };
        actPrintTable.putValue(Action.NAME, _("Print table..."));
        actPrintTable.putValue(Action.SHORT_DESCRIPTION, _("Prints the currently displayed table"));
        actPrintTable.putValue(Action.SMALL_ICON, utils.loadIcon("general/Print"));
        
        actSuspend = new ExcDialogAbstractAction() {
            public void actualActionPerformed(java.awt.event.ActionEvent e) {
                TooltipJTable selTable = (TooltipJTable)((JScrollPane)tabMain.getSelectedComponent()).getViewport().getView();
                SuspendWorker wrk = new SuspendWorker(selTable);
                wrk.startWork(mainwin.this, _("Suspending jobs"));
            }
        };
        actSuspend.putValue(Action.NAME, _("Suspend"));
        actSuspend.putValue(Action.SHORT_DESCRIPTION, _("Suspends the transfer of the selected fax"));
        actSuspend.putValue(Action.SMALL_ICON, utils.loadIcon("media/Pause"));
        
        actResume = new ExcDialogAbstractAction() {
            public void actualActionPerformed(java.awt.event.ActionEvent e) {
                TooltipJTable selTable = (TooltipJTable)((JScrollPane)tabMain.getSelectedComponent()).getViewport().getView();
                ResumeWorker wrk = new ResumeWorker(selTable);
                wrk.startWork(mainwin.this, _("Resuming jobs"));
            }
        };
        actResume.putValue(Action.NAME, _("Resume"));
        actResume.putValue(Action.SHORT_DESCRIPTION, _("Resumes the transfer of the selected fax"));
        actResume.putValue(Action.SMALL_ICON, utils.loadIcon("media/Play"));
        
        actClipCopy = new ExcDialogAbstractAction() {
            public void actualActionPerformed(java.awt.event.ActionEvent e) {
                TooltipJTable selTable = (TooltipJTable)((JScrollPane)tabMain.getSelectedComponent()).getViewport().getView();
                selTable.getTransferHandler().exportToClipboard(selTable, Toolkit.getDefaultToolkit().getSystemClipboard(), TransferHandler.COPY);
            }
        };
        actClipCopy.putValue(Action.NAME, _("Copy"));
        actClipCopy.putValue(Action.SHORT_DESCRIPTION, _("Copies the selected table items to the clipboard"));
        actClipCopy.putValue(Action.SMALL_ICON, utils.loadIcon("general/Copy"));
        
        actShowRowNumbers = new ExcDialogAbstractAction() {
            public void actualActionPerformed(ActionEvent e) {
                Boolean state = (Boolean)getValue(ActionJCheckBoxMenuItem.SELECTED_PROPERTY);
                boolean newState;
                if (state == null)
                    newState = false;
                else
                    newState = !state; 

                recvRowNumbers.setVisible(newState);
                sentRowNumbers.setVisible(newState);
                sendingRowNumbers.setVisible(newState);
                
                putValue(ActionJCheckBoxMenuItem.SELECTED_PROPERTY, newState);
            };
        };
        actShowRowNumbers.putValue(Action.NAME, _("Show row numbers"));
        actShowRowNumbers.putValue(Action.SHORT_DESCRIPTION, _("Show row numbers"));
        actShowRowNumbers.putValue(ActionJCheckBoxMenuItem.SELECTED_PROPERTY, myopts.showRowNumbers);
        
        actAdjustColumns = new ExcDialogAbstractAction() {
            public void actualActionPerformed(ActionEvent e) {
                Boolean state = (Boolean)getValue(ActionJCheckBoxMenuItem.SELECTED_PROPERTY);
                boolean newState;
                if (state == null)
                    newState = false;
                else
                    newState = !state; 
                
                int newMode = newState ? JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS : JTable.AUTO_RESIZE_OFF;
                tableRecv.setAutoResizeMode(newMode);
                tableSent.setAutoResizeMode(newMode);
                tableSending.setAutoResizeMode(newMode);
                // Uncomment for archive support.
//                tableArchive.setAutoResizeMode(newMode);
                
                putValue(ActionJCheckBoxMenuItem.SELECTED_PROPERTY, newState);
            };
        };
        actAdjustColumns.putValue(Action.NAME, _("Adjust column widths"));
        actAdjustColumns.putValue(Action.SHORT_DESCRIPTION, _("Adjust column widths to fit the window size"));
        actAdjustColumns.putValue(ActionJCheckBoxMenuItem.SELECTED_PROPERTY, myopts.adjustColumnWidths);
        
        actReconnect = new ExcDialogAbstractAction() {
            @Override
            protected void actualActionPerformed(ActionEvent e) {
                utils.setWaitCursor(null);
                if (clientManager != null) {
                    doLogout();
                } else {
                    reconnectToServer(null);
                }
                utils.unsetWaitCursor(null);
            }
        };
        actReconnect.putValue(Action.NAME, _("Connect"));
        actReconnect.putValue(Action.SHORT_DESCRIPTION, _("Connect or disconnect to the HylaFAX server"));
        
        actChecker = new ActionEnabler();
    }
    
    private JPopupMenu getTblPopup() {
        if (tblPopup == null) {
            tblPopup = new JPopupMenu(_("Fax"));
            tblPopup.add(new JMenuItem(actShow));
            tblPopup.add(new JMenuItem(actFaxSave));
            tblPopup.addSeparator();
            tblPopup.add(new JMenuItem(actClipCopy));
            tblPopup.addSeparator();
            tblPopup.add(new JMenuItem(actForward));
            tblPopup.add(new JMenuItem(actResend));
            tblPopup.addSeparator();
            tblPopup.add(new JMenuItem(actDelete));
            tblPopup.addSeparator();
            tblPopup.add(new ActionJCheckBoxMenuItem(actFaxRead));
        }
        return tblPopup;
    }
    
    public void refreshTables() {
        if (tableRefresher == null)
            return;
        
        tablePanel.showIndeterminateProgress(_("Fetching fax list..."));
        
        utmrTable.schedule(new TimerTaskWrapper(tableRefresher), 0);
    }
    
    public void refreshStatus() {
        if (statRefresher == null)
            return;
        
        utmrTable.schedule(new TimerTaskWrapper(statRefresher), 0);
    }
    
    public SendReadyState getSendReadyState() {
        return sendReady;
    }
    
    public void setSelectedTab(int index)
    {
        if (index >= 0 && index < TABLE_COUNT) {
            getTabMain().setSelectedIndex(index);
        }
    }
    
    private MouseListener getTblMouseListener() {
        if (tblMouseListener == null) {
            tblMouseListener = new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    if ((e.getButton() == MouseEvent.BUTTON1) && (e.getClickCount() == 2)) {
                        JTable src = (JTable)e.getComponent();
                        int row = src.rowAtPoint(e.getPoint());
                        if ((row >= 0) && (src.getSelectedRow() == row))
                            actShow.actionPerformed(null);
                    }
                }
                
                public void mousePressed(MouseEvent e) {
                    maybeShowPopup(e);
                }
                
                public void mouseReleased(MouseEvent e) {
                    maybeShowPopup(e);
                }
                
                private void maybeShowPopup(MouseEvent e) {
                    if (e.isPopupTrigger()) {
                        JTable src = (JTable)e.getComponent();
                        int row = src.rowAtPoint(e.getPoint());
                        if (row >= 0) {
                            if (!src.isRowSelected(row)) {
                                src.setRowSelectionInterval(row, row);
                            }
                            getTblPopup().show(src, e.getX(), e.getY());
                        }
                    }
                }
                
                
            };
        }
        return tblMouseListener;
    }
    
    private KeyListener getTblKeyListener() {
        if (tblKeyListener == null) {
            tblKeyListener = new KeyAdapter() {
                public void keyPressed(java.awt.event.KeyEvent e) {
                    switch (e.getKeyCode()) {
                    case KeyEvent.VK_ENTER:
                        actShow.actionPerformed(null);
                        break;
                    case KeyEvent.VK_DELETE:
                        actDelete.actionPerformed(null);
                        break;
                    }
                };
            };            
        }
        return tblKeyListener;
    }
    
    private DefaultTableCellRenderer getHylaDateRenderer() {
        if (hylaDateRenderer == null) {
            hylaDateRenderer = new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    if (value != null) {
                        int realCol = table.getColumnModel().getColumn(column).getModelIndex();
                        MyTableModel model = ((TooltipJTable)table).getRealModel();
                        value = model.columns.get(realCol).dateFormat.fmtOut.format(value);
                    }
                    return super.getTableCellRendererComponent(table, value, isSelected, hasFocus,
                            row, column);
                }
            };
        }
        return hylaDateRenderer;
    }
    
    private JToolBar getToolbar() {
        if (toolbar == null) {
            toolbar = new JToolBar();

            toolbar.add(actSend);
            toolbar.addSeparator();
            toolbar.add(actShow);
            toolbar.add(actDelete);
            toolbar.addSeparator();
            toolbar.add(actRefresh);
            toolbar.addSeparator();
            toolbar.add(actPhonebook);
            toolbar.addSeparator();
            toolbar.add(actResume);
            toolbar.add(actSuspend);
        }
        return toolbar;
    }
    
    private JMenu getMenuView() {
        if (menuView == null) {
            menuView = new JMenu(_("View"));
            
            menuViewListener = new MenuViewListener();
            
            menuViewAll = new JRadioButtonMenuItem(_("All faxes"));
            menuViewAll.setActionCommand("view_all");
            menuViewAll.setSelected(true);
            menuViewAll.addActionListener(menuViewListener);
            
            menuViewOwn = new JRadioButtonMenuItem(_("Only own faxes"));
            menuViewOwn.setActionCommand("view_own");
            menuViewOwn.addActionListener(menuViewListener);
            
            menuViewCustom = new JRadioButtonMenuItem(_("Custom filter..."));
            menuViewCustom.setActionCommand("view_custom");
            menuViewCustom.addActionListener(menuViewListener);
            
            menuMarkError = new JCheckBoxMenuItem(_("Mark failed jobs"));
            menuMarkError.setActionCommand("mark_failed");
            menuMarkError.addActionListener(menuViewListener);
            menuMarkError.setSelected(utils.getFaxOptions().markFailedJobs);
            
            viewGroup = new ButtonGroup();
            viewGroup.add(menuViewAll);
            viewGroup.add(menuViewOwn);
            viewGroup.add(menuViewCustom);
         
            menuView.add(menuViewAll);
            menuView.add(menuViewOwn);
            menuView.add(menuViewCustom);
            menuView.add(new JSeparator());
            menuView.add(menuMarkError);
            menuView.add(new ActionJCheckBoxMenuItem(actShowRowNumbers));
            menuView.add(new ActionJCheckBoxMenuItem(actAdjustColumns));
            menuView.add(new JSeparator());
            menuView.add(new JMenuItem(actRefresh));
            
            getTabMain().addChangeListener(menuViewListener);
        }
        return menuView;
    }
    
    /**
     * This is the default constructor
     */
    public mainwin(boolean adminState) {
        super();
        initialize(adminState);
    }

    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize(boolean adminState) {
        myopts = utils.getFaxOptions();
        
        createActions(adminState);
        
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setJMenuBar(getJJMenuBar());
        this.setSize(644, 466);
        this.setContentPane(getJContentPane());
        this.setTitle(utils.AppName);
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            private boolean saved = false;
            
            public void windowClosing(java.awt.event.WindowEvent e) {
                sendReady = SendReadyState.NotReady;
                
                doLogout();
                PersistentReadState.getCurrent().persistReadState();
                
                menuViewListener.saveToOptions();
                myopts.mainWinBounds = getBounds();
                myopts.mainwinLastTab = getTabMain().getSelectedIndex();
                
                Boolean selVal = (Boolean)actShowRowNumbers.getValue(ActionJCheckBoxMenuItem.SELECTED_PROPERTY);
                myopts.showRowNumbers = (selVal != null && selVal.booleanValue());
                selVal = (Boolean)actAdjustColumns.getValue(ActionJCheckBoxMenuItem.SELECTED_PROPERTY);
                myopts.adjustColumnWidths = (selVal != null && selVal.booleanValue());
                
                myopts.storeToFile(FaxOptions.getDefaultConfigFile());
                saved = true;
                Launcher.releaseLock();
                System.exit(0);
            }
            
            @Override
            public void windowClosed(WindowEvent e) {
                if (!saved)
                    windowClosing(null);
            }
        });
        setIconImage(Toolkit.getDefaultToolkit().getImage(mainwin.class.getResource("icon.png")));
        
        MyInit();
        
        if (myopts.mainWinBounds != null)
            this.setBounds(myopts.mainWinBounds);
        else
            //this.setLocationByPlatform(true);
            utils.setDefWinPos(this);
        
        tabMain.setSelectedIndex(myopts.mainwinLastTab);
        actChecker.doEnableCheck();
        
    }

    
    private void MyInit() {         
        utmrTable = new java.util.Timer("RefreshTimer", true);
        reloadTableColumnSettings();
        menuViewListener.loadFromOptions();
    }
    
    void doLogout() {
        try {
            tablePanel.showIndeterminateProgress(_("Logging out..."));
            if (tableRefresher != null)
                tableRefresher.doCancel();
            if (statRefresher != null)
                statRefresher.doCancel();
            
            if (clientManager != null) {                
                myopts.recvColState = getTableRecv().getColumnCfgString();
                myopts.sentColState = getTableSent().getColumnCfgString();
                myopts.sendingColState = getTableSending().getColumnCfgString();
                
                //myopts.recvReadState = recvTableModel.getStateString();
//                if (tableRefresher != null && tableRefresher.didFirstRun ) {
//                    recvTableModel.storeReadState(PersistentReadState.CURRENT);
//                }
                
                recvTableModel.cleanupReadState();
                
                //hyfc.quit();
                clientManager.forceLogout();
                clientManager = null;
            }
            //tmrStat.stop();
            //tmrTable.stop();

            
            getRecvTableModel().setData(null);
            getSentTableModel().setData(null);
            getSendingTableModel().setData(null);
            
            getTextStatus().setBackground(getDefStatusBackground());
            getTextStatus().setText(_("Disconnected."));
            
            actSend.setEnabled(false);
            actPoll.setEnabled(false);
            menuView.setEnabled(false);
            
            actReconnect.putValue(Action.NAME, _("Connect"));
            this.setTitle("Disconnected - " + utils.AppName);
        } catch (Exception e) {
            log.log(Level.WARNING, "Error logging out:", e);
            // do nothing
        }
        tablePanel.hideProgress();
    }
    
    private void reloadTableColumnSettings() {
        MyTableModel tm = getRecvTableModel();
        tm.columns = myopts.recvfmt;
        tm.fireTableStructureChanged();
        
        tm = getSentTableModel();
        tm.columns = myopts.sentfmt;
        tm.fireTableStructureChanged();

        tm = getSendingTableModel();
        tm.columns = myopts.sendingfmt;
        tm.fireTableStructureChanged();
        
        // Uncomment for archive support.
//        tm = getArchiveTableModel();
//        tm.columns = myopts.archiveFmt;
//        tm.fireTableStructureChanged();
        
        tableRecv.setColumnCfgString(myopts.recvColState);
        tableSent.setColumnCfgString(myopts.sentColState);
        tableSending.setColumnCfgString(myopts.sendingColState);
        // Uncomment for archive support.
//        tableArchive.setColumnCfgString(myopts.archiveColState);
    }
    
    public void reconnectToServer(Runnable loginAction) {
        sendReady = SendReadyState.NeedToWait;
        
        doLogout();
        
        if (myopts.host.length() == 0) { // Prompt for server if not set
            actOptions.actionPerformed(null);
            return;
        }
        
        this.setEnabled(false);
        tablePanel.showIndeterminateProgress(_("Logging in..."));
        
        new LoginThread((Boolean)actAdminMode.getValue(ActionJCheckBoxMenuItem.SELECTED_PROPERTY), loginAction).start();
        
    }
    
    
    
    /**
     * This method initializes jContentPane
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getJContentPane() {
        if (jContentPane == null) {
            jContentPane = new JPanel();
            jContentPane.setLayout(new BorderLayout());
            
            tablePanel = new ProgressPanel();
            
            Box box = Box.createVerticalBox();
            box.add(getTabMain());
            box.add(getTextStatus());
            
            tablePanel.setContentComponent(box);
            
            jContentPane.add(tablePanel, BorderLayout.CENTER);
            jContentPane.add(getToolbar(), BorderLayout.NORTH);
        }
        return jContentPane;
    }

    /**
     * This method initializes jJMenuBar	
     * 	
     * @return javax.swing.JMenuBar	
     */
    private JMenuBar getJJMenuBar() {
        if (jJMenuBar == null) {
            jJMenuBar = new JMenuBar();
            jJMenuBar.add(getMenuFax());
            jJMenuBar.add(getMenuView());
            jJMenuBar.add(getMenuExtras());
            jJMenuBar.add(getHelpMenu());
        }
        return jJMenuBar;
    }

    private JMenu getHelpMenu() {
        if (helpMenu == null) {
            helpMenu = new JMenu();
            helpMenu.setText(_("Help"));
            helpMenu.add(new JMenuItem(actReadme));
            helpMenu.addSeparator();
            helpMenu.add(new JMenuItem(actAbout));
        }
        return helpMenu;
    }

    private JTabbedPane getTabMain() {
        if (tabMain == null) {
            tabMain = new JTabbedPane();
            tabMain.addTab(_("Received"), utils.loadCustomIcon("received.gif"), getScrollRecv(), _("Received faxes"));
            tabMain.addTab(_("Sent"), utils.loadCustomIcon("sent.gif"), getScrollSent(), _("Sent faxes"));
            tabMain.addTab(_("Transmitting"), utils.loadCustomIcon("sending.gif"), getScrollSending(), _("Faxes in the output queue"));
            // Uncomment for archive support.
//            if (myopts.showArchive) {
//                tabMain.addTab(_("Archive"), getScrollArchive());
//            }
            
            tabMain.addChangeListener(actChecker);
        }
        return tabMain;
    }

    private JScrollPane getScrollRecv() {
        if (scrollRecv == null) {
            scrollRecv = new JScrollPane();
            scrollRecv.setViewportView(getTableRecv());
            
            recvRowNumbers = new NumberRowViewport(tableRecv, scrollRecv);
            recvRowNumbers.setVisible(myopts.showRowNumbers);
        }
        return scrollRecv;
    }

    private TooltipJTable getTableRecv() {
        if (tableRecv == null) {
            tableRecv = new TooltipJTable(getRecvTableModel());
            doCommonTableSetup(tableRecv);
            
            recvTableModel.unreadFont = tableRecv.getFont().deriveFont(Font.BOLD);
        }
        return tableRecv;
    }

    private Color getDefStatusBackground() {
        Color rv;
        rv = UIManager.getColor("control");
        if (rv == null)
            rv = new Color(230, 230, 230);
        return rv;
    }
    
    private JTextPane getTextStatus() {
        if (textStatus == null) {
            textStatus = new JTextPane() {
                @Override
                public Dimension getMinimumSize() {
                    return super.getPreferredSize();
                }
                
                @Override
                public Dimension getMaximumSize() {
                    Dimension d = super.getPreferredSize();
                    d.width = Integer.MAX_VALUE;
                    return d;
                }
            };
            textStatus.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

            textStatus.setBackground(getDefStatusBackground());
            textStatus.setFont(new java.awt.Font("DialogInput", java.awt.Font.PLAIN, 12));
            textStatus.setEditable(false);
        }
        return textStatus;
    }


    private UnReadMyTableModel getRecvTableModel() {
        if (recvTableModel == null) {
            recvTableModel = new UnReadMyTableModel(PersistentReadState.getCurrent());
            recvTableModel.addUnreadItemListener(new UnreadItemListener() {
                public void newItemsAvailable(UnreadItemEvent evt) {
                    if (evt.isOldDataNull())
                        return;
                    
                    if ((myopts.newFaxAction & utils.NEWFAX_TOFRONT) != 0) {
                        int state = getExtendedState();
                        if ((state & mainwin.ICONIFIED) != 0) 
                            setExtendedState(state & (~mainwin.ICONIFIED));
                        toFront();
                    }
                    if ((myopts.newFaxAction & utils.NEWFAX_BEEP) != 0) {
                        Toolkit.getDefaultToolkit().beep();
                    }
                    if ((myopts.newFaxAction & utils.NEWFAX_VIEWER) != 0) {
                        HylaFAXClient hyfc = clientManager.beginServerTransaction(mainwin.this);
                        if (hyfc == null) {
                            return;
                        }
                        for (RecvYajJob j : evt.getItems()) {
                            for (HylaServerFile hsf : j.getServerFilenames(hyfc)) {
                                try {
                                    hsf.view(hyfc);
                                } catch (Exception e) {
                                    if (utils.debugMode) {
                                        log.log(Level.WARNING, "Exception while trying to view new faxes:", e);
                                        //e.printStackTrace(utils.debugOut);
                                    }
                                }
                            }
                            if ((myopts.newFaxAction & utils.NEWFAX_MARKASREAD) != 0) {
                                j.setRead(true);
                            }
                        }
                        clientManager.endServerTransaction();
                    }
                };
            });
        }
        return recvTableModel;
    }


    private JScrollPane getScrollSent() {
        if (scrollSent == null) {
            scrollSent = new JScrollPane();
            scrollSent.setViewportView(getTableSent());
            
            sentRowNumbers = new NumberRowViewport(tableSent, scrollSent);
            sentRowNumbers.setVisible(myopts.showRowNumbers);
        }
        return scrollSent;
    }

    private TooltipJTable getTableSent() {
        if (tableSent == null) {
            tableSent = new TooltipJTable(getSentTableModel());
            doCommonTableSetup(tableSent);
        }
        return tableSent;
    }

    private MyTableModel getSentTableModel() {
        if (sentTableModel == null) {
            sentTableModel = new MyTableModel();
        }
        return sentTableModel;
    }

    private JScrollPane getScrollSending() {
        if (scrollSending == null) {
            scrollSending = new JScrollPane();
            scrollSending.setViewportView(getTableSending());
            
            sendingRowNumbers = new NumberRowViewport(tableSending, scrollSending);
            sendingRowNumbers.setVisible(myopts.showRowNumbers);
        }
        return scrollSending;
    }

    private TooltipJTable getTableSending() {
        if (tableSending == null) {
            tableSending = new TooltipJTable(getSendingTableModel());
            doCommonTableSetup(tableSending);
        }
        return tableSending;
    }

    private void doCommonTableSetup(TooltipJTable table) {
        table.setShowGrid(true);
        table.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        table.getSelectionModel().addListSelectionListener(actChecker);
        table.addMouseListener(getTblMouseListener());
        table.addKeyListener(getTblKeyListener());
        table.setDefaultRenderer(Date.class, getHylaDateRenderer());
        table.setDefaultRenderer(IconMap.class, new IconMap.TableCellRenderer());
        
        JTableTABAction.replaceTABWithNextRow(table);
    }
    
    private MyTableModel getSendingTableModel() {
        if (sendingTableModel == null) {
            sendingTableModel = new MyTableModel() {
                @Override
                protected YajJob createYajJob(String[] data) {
                    return new SendingYajJob(this.columns, data);
                }
            };
        }
        return sendingTableModel;
    }

    // Uncomment for archive support.
//    private ArchiveTableModel getArchiveTableModel() {
//        if (archiveTableModel == null) {
//            archiveTableModel = new ArchiveTableModel();
//        }
//        return archiveTableModel;
//    }
//    
//    private JScrollPane getScrollArchive() {
//        if (scrollArchive == null) {
//            tableArchive = new TooltipJTable(getArchiveTableModel());
//            doCommonTableSetup(tableArchive);
//            scrollArchive = new JScrollPane(tableArchive);
//            archiveRowNumbers = new NumberRowViewport(tableArchive, scrollArchive);
//            archiveRowNumbers.setVisible(myopts.showRowNumbers);
//        }
//        return scrollArchive;
//    }
    
    private JMenu getMenuFax() {
        if (menuFax == null) {
            menuFax = new JMenu();
            menuFax.setText(_("Fax"));
            menuFax.add(new JMenuItem(actSend));
            menuFax.add(new JMenuItem(actPoll));
            menuFax.add(new JMenuItem(actForward));
            menuFax.add(new JMenuItem(actResend));
            menuFax.addSeparator();
            menuFax.add(new JMenuItem(actShow));
            menuFax.add(new JMenuItem(actFaxSave));
            menuFax.add(new JMenuItem(actDelete));
            menuFax.addSeparator();
            menuFax.add(new JMenuItem(actResume));
            menuFax.add(new JMenuItem(actSuspend));
            menuFax.addSeparator();
            menuFax.add(new ActionJCheckBoxMenuItem(actFaxRead));
            menuFax.addSeparator();
            menuFax.add(new JMenuItem(actExit));
        }
        return menuFax;
    }

    private JMenu getMenuExtras() {
        if (menuExtras == null) {
            menuExtras = new JMenu(_("Extras"));
            menuExtras.add(actPhonebook);
            menuExtras.addSeparator();
            menuExtras.add(new JMenuItem(actClipCopy));
            menuExtras.add(new JMenuItem(actPrintTable));
            menuExtras.addSeparator();
            menuExtras.add(new JMenuItem(actOptions));
            menuExtras.addSeparator();
            menuExtras.add(new JMenuItem(actReconnect));
            menuExtras.add(new ActionJCheckBoxMenuItem(actAdminMode));
            if (PluginManager.pluginMenuEntries.size() > 0) {
                menuExtras.addSeparator();
                for (PluginMenuCreator pmc : PluginManager.pluginMenuEntries) {
                    for (JMenuItem item : pmc.createMenuItems()) {
                        menuExtras.add(item);
                    }
                }
            }
        }
        return menuExtras;
    }
    
    class MenuViewListener implements ActionListener, ChangeListener {
        private JRadioButtonMenuItem[] lastSel = new JRadioButtonMenuItem[TABLE_COUNT];
        
        public void actionPerformed(ActionEvent e) {
            String cmd = e.getActionCommand();
            MyTableModel model = ((TooltipJTable)((JScrollPane)tabMain.getSelectedComponent()).getViewport().getView()).getRealModel();
            int selTab = tabMain.getSelectedIndex();
            
            if (cmd.equals("view_all")) {
                model.setJobFilter(null);
                lastSel[selTab] = menuViewAll;
            } else if (cmd.equals("view_own")) {
                model.setJobFilter(getOwnFilterFor(model));
                lastSel[selTab] = menuViewOwn;
            } else if (cmd.equals("view_custom")) {
                CustomFilterDialog cfd = new CustomFilterDialog(mainwin.this, tabMain.getTitleAt(selTab), 
                        model.columns, (lastSel[selTab] == menuViewCustom) ? model.getJobFilter() : null);
                cfd.setVisible(true);
                if (cfd.returnValue != null) {
                    model.setJobFilter(cfd.returnValue);
                    lastSel[selTab] = menuViewCustom;
                } else {
                    if (lastSel[selTab] != menuViewCustom)
                            resetLastSel(selTab);
                }
            } else if (cmd.equals("mark_failed")) {
                myopts.markFailedJobs = menuMarkError.isSelected();
                
                TooltipJTable selTable = (TooltipJTable)((JScrollPane)tabMain.getSelectedComponent()).getViewport().getView();
                selTable.repaint();
            }
        }
        
        private void resetLastSel(int selTab) {
            if (lastSel[selTab] != null)
                lastSel[selTab].setSelected(true);
            else 
                menuViewAll.setSelected(true);
        }
        
        public void stateChanged(ChangeEvent e) {
            MyTableModel model = ((TooltipJTable)((JScrollPane)tabMain.getSelectedComponent()).getViewport().getView()).getRealModel();
            boolean viewOwnState  = ownFilterOK(model);
            boolean markErrorState = canMarkError(model);
            
            resetLastSel(tabMain.getSelectedIndex());
            menuViewOwn.setEnabled(viewOwnState);
            menuMarkError.setEnabled(markErrorState);
            if ((!viewOwnState && menuViewOwn.isSelected())) {
                menuViewAll.setSelected(true);
                model.setJobFilter(null);
            }
        }
        
        private Filter<YajJob,FmtItem> getOwnFilterFor(MyTableModel model) {
            return new StringFilter<YajJob,FmtItem>((model == recvTableModel ? utils.recvfmt_Owner : utils.jobfmt_Owner), StringFilterOperator.EQUAL, myopts.user, true);
        }
        
        private boolean canMarkError(MyTableModel model) {
            if (model == sentTableModel || model == sendingTableModel) { 
                return model.columns.getCompleteView().contains(utils.jobfmt_Jobstate) || model.columns.getCompleteView().contains(utils.jobfmt_Status);
            } else if (model == recvTableModel) { 
                return myopts.recvfmt.getCompleteView().contains(utils.recvfmt_ErrorDesc);
            } else //TODO for archive support?
                return false;
        }
        
        private boolean ownFilterOK(MyTableModel model) {
            if (model == recvTableModel) { 
                return model.columns.getCompleteView().contains(utils.recvfmt_Owner);
            } else if (model == sentTableModel || model == sendingTableModel) { 
                return model.columns.getCompleteView().contains(utils.jobfmt_Owner);
                // Uncomment for archive support.
//            } else if (model == archiveTableModel) {
//                return model.columns.getCompleteView().contains(ArchiveYajJob.ownerField);
            } else
                return false;
        }
        /**
         * Re-validates the filters on reconnection
         */
        public void reConnected() {
            for (int i = 0; i < lastSel.length; i++) {
                MyTableModel model = ((TooltipJTable)((JScrollPane)tabMain.getComponent(i)).getViewport().getView()).getRealModel();
                if (lastSel[i] == menuViewOwn) {
                    if (ownFilterOK(model)) 
                        model.setJobFilter(getOwnFilterFor(model));
                    else {
                        lastSel[i] = menuViewAll;
                        model.setJobFilter(null);
                    }
                } else if (lastSel[i] == menuViewCustom) {
                    if (!model.getJobFilter().validate(model.columns)) {
                        lastSel[i] = menuViewAll;
                        model.setJobFilter(null);
                    }
                    
                } else if (lastSel[i] == menuViewAll) 
                    model.setJobFilter(null);
            }
            stateChanged(null);
        }
        

        private void loadSaveString(int idx, String data) {
            if ((data == null) || data.equals("A")) {
                lastSel[idx] = menuViewAll;
            } else if (data.equals("O")) {
                lastSel[idx] = menuViewOwn;
            } else if (data.startsWith("C")) {
                MyTableModel model = ((TooltipJTable)((JScrollPane)tabMain.getComponent(idx)).getViewport().getView()).getRealModel();
                Filter<YajJob,FmtItem> yjf = FilterCreator.stringToFilter(data.substring(1), model.columns);
                if (yjf == null) {
                    lastSel[idx] = menuViewAll;
                } else {
                    lastSel[idx] = menuViewCustom;
                    model.setJobFilter(yjf);
                }
            }
        }
        
        public void loadFromOptions() {
            loadSaveString(0, myopts.recvFilter);
            loadSaveString(1, myopts.sentFilter);
            loadSaveString(2, myopts.sendingFilter);
            reConnected();
        }
        
        private String getSaveString(int idx) {
            if (lastSel[idx] == null || lastSel[idx] == menuViewAll) {
                return "A";
            } else if (lastSel[idx] == menuViewOwn) {
                return "O";
            } else if (lastSel[idx] == menuViewCustom) {
                MyTableModel model = ((TooltipJTable)((JScrollPane)tabMain.getComponent(idx)).getViewport().getView()).getRealModel();
                return "C" + FilterCreator.filterToString(model.getJobFilter());
            } else
                return null;
        }
        
        public void saveToOptions() {
            myopts.recvFilter = getSaveString(0);
            myopts.sentFilter = getSaveString(1);
            myopts.sendingFilter = getSaveString(2);
        }
    }

    class ActionEnabler implements ListSelectionListener, ChangeListener {
        public void stateChanged(ChangeEvent e) {
            doEnableCheck();
        }
        
        public void valueChanged(ListSelectionEvent e) {
            doEnableCheck();
        }
        
        public void doEnableCheck() {
            boolean showState = false;
            boolean deleteState = false;
            boolean faxReadState = false, faxReadSelected = false;
            boolean resendState = false;
            boolean suspResumeState = false;
            
            if (tabMain.getSelectedComponent() == scrollRecv) { // Received Table active
                if (tableRecv.getSelectedRow() >= 0) {
                    showState = true;
                    deleteState = true;
                    faxReadState = true;
                    faxReadSelected = ((RecvYajJob)tableRecv.getJobForRow(tableRecv.getSelectedRow())).isRead();
                }
            } else if (tabMain.getSelectedComponent() == scrollSent) { // Sent Table
                if (tableSent.getSelectedRow() >= 0) {
                    deleteState = true;
                    showState = true;
                    resendState = true;
                }
            } else if (tabMain.getSelectedComponent() == scrollSending) { // Sending Table
                if (tableSending.getSelectedRow() >= 0) {
                    deleteState = true;
                    showState = true;
                    resendState = true;
                    suspResumeState = true;
                }
                // Uncomment for archive support.
//            } if (tabMain.getSelectedComponent() == scrollArchive) { // Sending Table
//                if (tableArchive.getSelectedRow() >= 0) {
//                    deleteState = true;
//                    showState = true;
//                    resendState = true;
//                    suspResumeState = true;
//                }
            } 
            
            actShow.setEnabled(showState);
            actFaxSave.setEnabled(showState);
            actDelete.setEnabled(deleteState);
            actFaxRead.setEnabled(faxReadState);
            actForward.setEnabled(faxReadState);
            actResend.setEnabled(resendState);
            actSuspend.setEnabled(suspResumeState);
            actResume.setEnabled(suspResumeState);
            actClipCopy.setEnabled(showState);
            
            actFaxRead.putValue(ActionJCheckBoxMenuItem.SELECTED_PROPERTY, faxReadSelected);
        }
    }
    
    class StatusRefresher extends TimerTask {
        String oldText = "";
        private boolean cancelled = false;
        
        public synchronized boolean doCancel() {
            cancelled = true;
            return cancel();
        }

        public synchronized void run() {
            if (cancelled)
                return;

            String newText;
            HylaFAXClient hyfc = clientManager.beginServerTransaction(mainwin.this);
            if (hyfc == null) {
                newText = utils._("Could not log in");
            } else {
                try {
                    newText = utils.listToString(hyfc.getList("status"), "\n");
                } catch (SocketException se) {
                    log.log(Level.WARNING, "Error refreshing the status, logging out.", se);
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            doLogout();
                        } 
                    });
                    return;
                } catch (Exception e) {
                    newText = _("Error refreshing the status:") + " " + e;
                    log.log(Level.WARNING, "Error refreshing the status:", e);
                }
            }
            if (!newText.equals(oldText)) {
                oldText = newText;
                
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        textStatus.setText(oldText);
                    } 
                });
            }
            clientManager.endServerTransaction();
        }
    };
    
    class TableRefresher extends TimerTask {
        private String sentfmt, sendingfmt;
        private Vector<?> lastRecvList = null, lastSentList = null, lastSendingList = null;
        // Uncomment for archive support.
//        private Vector<?> lastArchiveList = null;
        private boolean cancelled = false;
        //public boolean didFirstRun = false;
        
        public synchronized boolean doCancel() {
            cancelled = true;
            return cancel();
        }
        
        public synchronized void run() {
            if (cancelled)
                return;
            
            HylaFAXClient hyfc = clientManager.beginServerTransaction(mainwin.this);
            if (hyfc == null) {
                return;
            }
            Vector<?> lst;
            try {
                //System.out.println(System.currentTimeMillis() + ": Getting list...");
                lst = hyfc.getList("recvq");
                //System.out.println(System.currentTimeMillis() + ": Got list...");
                if ((lastRecvList == null) || !lst.equals(lastRecvList)) {
                    String[][] data = new String[lst.size()][];
                    for (int i = 0; i < lst.size(); i++) {
                        //data[i] = ((String)lst.get(i)).split("\\|");
                        data[i] = utils.fastSplit((String)lst.get(i), '|');
                    }
                    SwingUtilities.invokeLater(new TableDataRunner(recvTableModel, data));
                    lastRecvList = lst;
                    
//                    if (!didFirstRun) {
//                        // Read the read/unread status *after* the table contents has been set 
//                        SwingUtilities.invokeLater(new Runnable() {
//                            public void run() {
//                                recvTableModel.loadReadState(PersistentReadState.CURRENT);
//                                tableRecv.repaint();
//                            }
//                        });
//                        didFirstRun = true;
//                    }
                    //System.out.println(System.currentTimeMillis() + ": Did invokeLater()");
                }
            } catch (Exception e) {
                log.log(Level.WARNING, "An error occured refreshing the tables: ", e);
//                if (utils.debugMode) {
//                    utils.debugOut.println("An error occured refreshing the tables: ");
//                    e.printStackTrace(utils.debugOut);
//                }
            }        
            
            try {
                synchronized (hyfc) {
                    hyfc.jobfmt(sentfmt);
                    lst = hyfc.getList("doneq");
                }
                if ((lastSentList == null) || !lst.equals(lastSentList)) {
                    String[][] data = new String[lst.size()][];
                    for (int i = 0; i < lst.size(); i++) {
                        //data[i] = ((String)lst.get(i)).split("\\|");
                        data[i] = utils.fastSplit((String)lst.get(i), '|');
                    }
                    SwingUtilities.invokeLater(new TableDataRunner(sentTableModel, data));
                    lastSentList = lst;
                }
            } catch (Exception e) {
                log.log(Level.WARNING, "An error occured refreshing the tables: ", e);
//                if (utils.debugMode) {
//                    utils.debugOut.println("An error occured refreshing the tables: ");
//                    e.printStackTrace(utils.debugOut);
//                }
            }
            
            try {
                synchronized (hyfc) {
                    hyfc.jobfmt(sendingfmt);
                    lst = hyfc.getList("sendq");
                }
                if ((lastSendingList == null) || !lst.equals(lastSendingList)) {
                    String[][] data = new String[lst.size()][];
                    for (int i = 0; i < lst.size(); i++) {
                        //data[i] = ((String)lst.get(i)).split("\\|");
                        data[i] = utils.fastSplit((String)lst.get(i), '|');
                    }
                    SwingUtilities.invokeLater(new TableDataRunner(sendingTableModel, data));
                    lastSendingList = lst;
                }
            } catch (Exception e) {
                log.log(Level.WARNING, "An error occured refreshing the tables: ", e);
//                if (utils.debugMode) {
//                    utils.debugOut.println("An error occured refreshing the tables: ");
//                    e.printStackTrace(utils.debugOut);
//                }
            }

            // Uncomment for archive support.
//            if (myopts.showArchive) {
//                try {
//                    synchronized (hyfc) {
//                        lst = hyfc.getNameList("archive");
//                    }
//                    if ((lastArchiveList == null) || !lst.equals(lastArchiveList)) {
//                        final List<ArchiveYajJob> archiveJobs = ArchiveYajJob.getArchiveFiles(hyfc, lst, archiveTableModel.columns);
//                        SwingUtilities.invokeLater(new Runnable() {
//                           public void run() {
//                               archiveTableModel.setData(archiveJobs);
//                            } 
//                        });
//                        lastArchiveList = lst;
//                    }
//                } catch (Exception e) {
//                    log.log(Level.WARNING, "An error occured refreshing the tables: ", e);
//                }
//            }
            
            if (tablePanel.isShowingProgress()) {
                SwingUtilities.invokeLater(new Runnable() 
                {
                    public void run() {
                        tablePanel.hideProgress();
                    }
                });
            }
            clientManager.endServerTransaction();
        }
        
        public TableRefresher(String sentfmt, String sendingfmt) {
            this.sentfmt = sentfmt;
            this.sendingfmt = sendingfmt; 
        }
        
        class TableDataRunner implements Runnable {
            private String[][] data = null;
            private MyTableModel tm;
                    
            public void run() {
                //System.out.println(System.currentTimeMillis() + ": About to set data...");
                tm.setData(data);         
                //System.out.println(System.currentTimeMillis() + ": Set data.");
            }
            
            public TableDataRunner(MyTableModel tm, String[][] data) {
                this.tm = tm;
                this.data = data;
            }
        }
    }
    
    static class TimerTaskWrapper extends TimerTask {
        protected Runnable wrapped;
        
        @Override
        public void run() {
            wrapped.run();
        }
        
        public TimerTaskWrapper(Runnable wrapped) {
            this.wrapped = wrapped;
        }
    }
    
    class LoginThread extends Thread {
        protected boolean wantAdmin;
        protected Runnable loginAction;
        
        @Override
        public void run() {  
            
            /*
            hyfc = new HylaFAXClient();
            hyfc.setDebug(utils.debugMode);*/
            try {
                /*hyfc.open(myopts.host, myopts.port);
                
                while (hyfc.user(myopts.user)) {                
                        if (myopts.askPassword) {
                            
                            String pwd = PasswordDialog.showPasswordDialogThreaded(mainwin.this, _("User password"), MessageFormat.format(_("Please enter the password for user \"{0}\"."), myopts.user));
                            if (pwd == null) { // User cancelled
                                hyfc.quit();
                                doErrorCleanup();
                                return;
                            } else
                                try {
                                    hyfc.pass(pwd);
                                    //repeatAsk = false;
                                    break;
                                } catch (ServerResponseException e) {
                                    ExceptionDialog.showExceptionDialogThreaded(mainwin.this, _("An error occured in response to the password:"), e);
                                    //repeatAsk = true;
                                }
                        } else {
                            hyfc.pass(myopts.pass);
                            break;
                        }
                } 
                
                if (haveAdmin) {
                    boolean authOK = false;
                    if (myopts.askAdminPassword) {
                        do {
                            String pwd = PasswordDialog.showPasswordDialogThreaded(mainwin.this, _("Admin password"), MessageFormat.format(_("Please enter the administrative password for user \"{0}\"."), myopts.user));
                            if (pwd == null) { // User cancelled
                                break; //Continue in "normal" mode
                            } else
                                try {
                                    hyfc.admin(pwd);
                                    authOK = true;
                                } catch (ServerResponseException e) {
                                    ExceptionDialog.showExceptionDialogThreaded(mainwin.this, _("An error occured in response to the password:"), e);
                                    authOK = false;
                                }
                        } while (!authOK);
                    } else {
                        hyfc.admin(myopts.AdminPassword);
                        authOK = true; // No error => authOK
                    }
;
                    haveAdmin = authOK;
                }
                
                hyfc.setPassive(myopts.pasv);
                hyfc.tzone(myopts.tzone.type);
                
                hyfc.rcvfmt(myopts.recvfmt.getFormatString());*/
                if (utils.debugMode) {
                    log.info("Begin login (wantAdmin=" + wantAdmin + ")");
                }
                clientManager = new HylaClientManager(myopts);
                clientManager.setAdminMode(wantAdmin);
                if (clientManager.beginServerTransaction(mainwin.this) == null) {
                    doErrorCleanup();
                    return;
                }
                if (utils.debugMode) {
                    log.info("Login succeeded. -- begin init work.");
                }
                
                PersistentReadState persistentReadState = PersistentReadState.getCurrent();
                recvTableModel.setPersistentReadState(persistentReadState);
                
                // Multi-threaded implementation of the periodic refreshes.
                // I hope I didn't introduce too many race conditions/deadlocks this way
                statRefresher = new StatusRefresher();

                tableRefresher = new TableRefresher(myopts.sentfmt.getFormatString(), myopts.sendingfmt.getFormatString());
                
                //// Read the read/unread status *after* the table contents has been set 
                //tableRefresher.run();

                persistentReadState.prepareReadStates();
                
                // Final UI updates:
                SwingUtilities.invokeLater(new Runnable() {
                   public void run() {
                       mainwin.this.setTitle(myopts.user + "@" + myopts.host + (clientManager.isAdminMode() ? " (admin)" : "") + " - " +utils.AppName);
                       
                       actAdminMode.putValue(ActionJCheckBoxMenuItem.SELECTED_PROPERTY, clientManager.isAdminMode());
                       if (clientManager.isAdminMode()) {
                           // A reddish gray
                           Color defStatusBackground = getDefStatusBackground();
                           textStatus.setBackground(new Color(Math.min(defStatusBackground.getRed() + 40, 255), defStatusBackground.getGreen(), defStatusBackground.getBlue()));
                       } 
                       
                       reloadTableColumnSettings();
                       
                       menuView.setEnabled(true);
                       // Re-check menu View state:
                       menuViewListener.reConnected();
                       
                       tablePanel.showIndeterminateProgress(_("Fetching fax list..."));
                       
                       utmrTable.schedule(statRefresher, 0, myopts.statusUpdateInterval);
                       utmrTable.schedule(tableRefresher, 0, myopts.tableUpdateInterval);
                       
                       actSend.setEnabled(true);
                       actPoll.setEnabled(true);
                       
                       actReconnect.putValue(Action.NAME, _("Disconnect"));
                       
                       sendReady = SendReadyState.Ready;
                       mainwin.this.setEnabled(true);
                       if (utils.debugMode) {
                           log.info("Finished init work!");
                       }
                       if (loginAction != null) {
                           if (utils.debugMode) {
                               log.info("Doing login action: " + loginAction.getClass().getName());
                           }
                           loginAction.run();
                           if (utils.debugMode) {
                               log.info("Finished login action.");
                           }
                       }
                       clientManager.endServerTransaction();
                    } 
                });
            } catch (Exception e) {
                ExceptionDialog.showExceptionDialogThreaded(mainwin.this, _("An error occured connecting to the server:"), e);
                doErrorCleanup();
            }
        }
        
        private void doErrorCleanup() {
            if (utils.debugMode) {
                log.info("Login failed! -- doing cleanup.");
            }
            clientManager = null;
            sendReady = SendReadyState.NotReady;
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    tablePanel.hideProgress();
                    mainwin.this.setEnabled(true);
                 } 
             });
        }
        
        public LoginThread(boolean wantAdmin, Runnable loginAction) {
            super(LoginThread.class.getName());
            this.wantAdmin = wantAdmin;
            this.loginAction = loginAction;
        }
    }

    public HylaClientManager getClientManager() {
        return clientManager;
    }
    
    public java.util.Timer getRefreshTimer() {
        return utmrTable;
    }
}  


