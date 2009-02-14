/**
 * 
 */
package yajhfc.settingstool;

import static yajhfc.settingstool.EntryPoint._;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Properties;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.TableColumn;

import yajhfc.Launcher2;
import yajhfc.Utils;
import yajhfc.util.CancelAction;
import yajhfc.util.ExcDialogAbstractAction;
import yajhfc.util.ExceptionDialog;
import yajhfc.util.SafeJFileChooser;

/**
 * @author jonas
 *
 */
public class SettingsTool extends JFrame {

    protected JTable settingsTable;
    protected SettingsTableModel tableModel;
    protected Action actSave, actLoad, actQuit, actSelectAll, actDeselectAll, actAbout;
    protected Action actSaveDefault, actSaveOverride, actLoadDefault, actLoadOverride;
    
    protected JFileChooser fileChooser;
    
    public SettingsTool() {
        super(_("Settings editor"));
        initialize();
    }
    
    JFileChooser getFileChooser() {
        if (fileChooser == null) {
            fileChooser = new SafeJFileChooser();
            fileChooser.addChoosableFileFilter(new FileFilter() {
                @Override
                public boolean accept(File f) {
                    if (f.isDirectory())
                        return true;
                    
                    String fileName = f.getName();
                    return fileName.equals("settings") || fileName.equals("settings.default") || fileName.equals("settings.override");
                }

                @Override
                public String getDescription() {
                    return _("YajHFC settings files");
                } 
            
            });
            fileChooser.setFileHidingEnabled(false);
        }
        return fileChooser;
    }
    
    private void createActions() {
        actSave = new ExcDialogAbstractAction() {
            @Override
            protected void actualActionPerformed(ActionEvent e) {
                if (getFileChooser().showSaveDialog(SettingsTool.this) == JFileChooser.APPROVE_OPTION) {
                    saveSettings(fileChooser.getSelectedFile());
                }
            }
        };
        actSave.putValue(Action.NAME, _("Save selected properties as..."));
        
        
        actLoad = new ExcDialogAbstractAction() {
            @Override
            protected void actualActionPerformed(ActionEvent e) {
                if (getFileChooser().showOpenDialog(SettingsTool.this) == JFileChooser.APPROVE_OPTION) {
                    loadSettings(fileChooser.getSelectedFile());
                }
            }
        };
        actLoad.putValue(Action.NAME, _("Load properties from..."));
        
        actQuit = new CancelAction(this, _("Quit"));
        
        actSelectAll = new ExcDialogAbstractAction() {
            @Override
            protected void actualActionPerformed(ActionEvent e) {
                tableModel.selectAll(true);
            }
        };
        actSelectAll.putValue(Action.NAME, _("Select All"));
        
        actDeselectAll = new ExcDialogAbstractAction() {
            @Override
            protected void actualActionPerformed(ActionEvent e) {
                tableModel.selectAll(false);
            }
        };
        actDeselectAll.putValue(Action.NAME, _("Deselect All"));
        
        actAbout = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                final String aboutString = String.format(
                        "%s\nVersion %s\n\n%s\n\nHomepage: %s\nE-Mail: %s",
                        EntryPoint.AppShortName, EntryPoint.AppVersion, EntryPoint.AppCopyright, EntryPoint.HomepageURL, EntryPoint.AuthorEMail);
                JOptionPane.showMessageDialog(Launcher2.application, aboutString, MessageFormat.format(_("About {0}"), EntryPoint.AppShortName), JOptionPane.INFORMATION_MESSAGE);
            }
        };
        actAbout.putValue(Action.NAME, _("About..."));
        
        actLoadDefault = new LoadAction("settings.default");
        actSaveDefault = new SaveAction("settings.default");
        actLoadOverride = new LoadAction("settings.override");
        actSaveOverride = new SaveAction("settings.override");
    }
    
    void saveSettings(File destination) {
        Properties p = new Properties();
        tableModel.storeToProperties(p);
        if (p.size() == 0) {
            JOptionPane.showMessageDialog(SettingsTool.this, _("You have selected no settings to save. Nothing saved."));
        } else {
            try {
                FileOutputStream fo = new FileOutputStream(destination);
                p.store(fo, "YajHFC settings file generated by settings tool");
                fo.close();
            } catch (IOException e1) {
                ExceptionDialog.showExceptionDialog(SettingsTool.this, _("Error saving the settings:"), e1);
            }
        }
    }
    
    void loadSettings(File source) {
        Properties p = new Properties();
        try {
            FileInputStream fi = new FileInputStream(source);
            p.load(fi);
            fi.close();
        } catch (IOException e1) {
            ExceptionDialog.showExceptionDialog(SettingsTool.this, _("Error loading the settings:"), e1);
            return;
        }
        tableModel.loadFromProperties(p);
    }
    
    private void initialize() {
        createActions();
        setJMenuBar(createMenuBar());
        setContentPane(createContentPane());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        
        tableModel.loadAvailableSettings();
    }
 
    private JMenuBar createMenuBar() {
        JMenuBar menu = new JMenuBar();
        
        JMenu fileMenu = new JMenu(_("File"));
        fileMenu.add(new JMenuItem(actLoadDefault));
        fileMenu.add(new JMenuItem(actSaveDefault));
        fileMenu.addSeparator();
        fileMenu.add(new JMenuItem(actLoadOverride));
        fileMenu.add(new JMenuItem(actSaveOverride));
        fileMenu.addSeparator();
        fileMenu.add(new JMenuItem(actLoad));
        fileMenu.add(new JMenuItem(actSave));
        fileMenu.addSeparator();
        fileMenu.add(new JMenuItem(actQuit));
        
        JMenu editMenu = new JMenu(_("Edit"));
        editMenu.add(new JMenuItem(actSelectAll));
        editMenu.add(new JMenuItem(actDeselectAll));

        JMenu aboutMenu = new JMenu(_("Help"));
        aboutMenu.add(new JMenuItem(actAbout));
        
        menu.add(fileMenu);
        menu.add(editMenu);
        menu.add(aboutMenu);
        
        return menu;
    }
    
    private JPanel createContentPane() {
        JPanel contentPane = new JPanel(new BorderLayout());
        tableModel = new SettingsTableModel();
        settingsTable = new JTable(tableModel);
        TableColumn col = settingsTable.getColumnModel().getColumn(0);
        col.setResizable(false);
        col.sizeWidthToFit();
        int width = col.getWidth();
        col.setMaxWidth(width);
        col.setPreferredWidth(width);
        
        contentPane.add(new JScrollPane(settingsTable), BorderLayout.CENTER);
        contentPane.add(createButtonsPanel(), BorderLayout.SOUTH);
        return contentPane;
    }
    
    private JPanel createButtonsPanel() {
        JPanel buttonsPanel = new JPanel(new GridLayout(1, 5, 10, 10));
        
        buttonsPanel.add(new JButton(actLoadDefault));
        buttonsPanel.add(new JButton(actLoadOverride));
        buttonsPanel.add(new JButton(actSaveDefault));
        buttonsPanel.add(new JButton(actSaveOverride));
        buttonsPanel.add(new JButton(actQuit));
        return buttonsPanel;
    }
    
    class LoadAction extends ExcDialogAbstractAction {
        protected File destination;
        protected String fileName;
        
        @Override
        protected void actualActionPerformed(ActionEvent e) {
            if (destination.exists()) {
                loadSettings(destination);
            } else {
                JOptionPane.showMessageDialog(SettingsTool.this, MessageFormat.format(_("No existing {0} file found. Starting with an empty one."), fileName), getValue(Action.NAME).toString(), JOptionPane.INFORMATION_MESSAGE);
                tableModel.selectAll(false);
            }
        }
        
        public LoadAction(String fileName) {
            this.destination = new File(Utils.getApplicationDir(), fileName);
            this.fileName = fileName;
            putValue(Action.NAME, MessageFormat.format(_("Load {0}"), fileName));
        }
    }
    
    class SaveAction extends ExcDialogAbstractAction {
        protected File destination;
        protected String fileName;
        
        @Override
        protected void actualActionPerformed(ActionEvent e) {
            if (JOptionPane.showConfirmDialog(SettingsTool.this, MessageFormat.format(_("Save current selection as {0}?"), fileName), getValue(Action.NAME).toString(), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                saveSettings(destination);
            }
        }
        
        public SaveAction(String fileName) {
            destination = new File(Utils.getApplicationDir(), fileName);
            this.fileName = fileName;
            putValue(Action.NAME, MessageFormat.format(_("Save {0}"), fileName));
        }
    }
}

