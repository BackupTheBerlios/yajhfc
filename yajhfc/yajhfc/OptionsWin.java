package yajhfc;
/*
 * YAJHFC - Yet another Java Hylafax client
 * Copyright (C) 2005 Jonas Wolz
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

import info.clearthought.layout.TableLayout;
import info.clearthought.layout.TableLayoutConstraints;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SpinnerNumberModel;

public class OptionsWin extends JDialog {
    
    private JPanel jContentPane = null;
    
    private JTabbedPane TabMain = null;
    private JPanel PanelCommon = null;
    private JPanel panelSendSettings = null;
    private fmtEditor PanelRecvFmt = null, PanelSentFmt = null, PanelSendingFmt = null;
        
    private JPanel PanelButtons;
    private JButton ButtonOK, ButtonCancel;
    
    private JTextField textNotifyAddress, textHost, textUser, /*textViewer,*/ textPort;
    private JPasswordField textPassword, textAdminPassword;
    private JComboBox comboTZone, comboNotify, comboPaperSize, comboResolution, comboNewFaxAction;
    private JCheckBox checkPasv, checkPCLBug, checkAskPassword, checkAskAdminPassword, checkUseCustomDefCover;
    private JSpinner spinMaxTry, spinMaxDial;
    //private JButton buttonBrowseViewer;
    private FileTextField ftfFaxViewer, ftfPSViewer, ftfCustomDefCover;
    
    private JTextField textFromFaxNumber, textFromName, textFromCompany, textFromLocation, textFromVoicenumber;
    private ClipboardPopup clpDef;
    private JPanel panelServer, panelSend, panelPaths, panelCover, panelMisc;
    
    private FaxOptions foEdit = null;
    private Vector<FmtItem> recvfmt, sentfmt, sendingfmt;
    
    private boolean modalResult;
    private static double border = 5;
    
    // true if OK, false otherwise
    public boolean getModalResult() {
        return modalResult;
    }
    
    private String _(String key) {
        return utils._(key);
    }
    
    private void initialize() {
        this.setSize(600, 430);
        this.setResizable(false);
        this.setTitle(_("Options"));
        this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        clpDef = new ClipboardPopup();
        this.setContentPane(getJContentPane());
        
        modalResult = false;
        
        // Load values
        textNotifyAddress.setText(foEdit.notifyAddress);
        textHost.setText(foEdit.host);
        textPort.setText(String.valueOf(foEdit.port));
        textUser.setText(foEdit.user);
        textPassword.setText(foEdit.pass);
        textAdminPassword.setText(foEdit.AdminPassword);
        ftfFaxViewer.setText(foEdit.faxViewer);
        ftfPSViewer.setText(foEdit.psViewer);
        
        comboNotify.setSelectedItem(foEdit.notifyWhen);
        comboPaperSize.setSelectedItem(foEdit.paperSize);
        comboResolution.setSelectedItem(foEdit.resolution);
        comboTZone.setSelectedItem(foEdit.tzone);
        comboNewFaxAction.setSelectedItem(foEdit.newFaxAction);
        
        checkPasv.setSelected(foEdit.pasv);
        checkPCLBug.setSelected(foEdit.pclBug);
        checkAskPassword.setSelected(foEdit.askPassword);
        checkAskAdminPassword.setSelected(foEdit.askAdminPassword);
        
        spinMaxDial.setValue(Integer.valueOf(foEdit.maxDial));
        spinMaxTry.setValue(Integer.valueOf(foEdit.maxTry));
        
        ftfCustomDefCover.setText(foEdit.defaultCover);
        checkUseCustomDefCover.setSelected(foEdit.useCustomDefaultCover);
        
        textFromCompany.setText(foEdit.FromCompany); 
        textFromFaxNumber.setText(foEdit.FromFaxNumber);
        textFromLocation.setText(foEdit.FromLocation);
        textFromName.setText(foEdit.FromName );
        textFromVoicenumber.setText(foEdit.FromVoiceNumber);
        
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
               foEdit.optWinPos = getLocation();
            }
        });
        
        if (foEdit.optWinPos != null)
            this.setLocation(foEdit.optWinPos);
        else
            //this.setLocationByPlatform(true);
            utils.setDefWinPos(this);
        
    }
    
    private JPanel getJContentPane() {
        if (jContentPane == null) {
            jContentPane = new JPanel(new BorderLayout());
            jContentPane.add(getTabMain(), BorderLayout.CENTER);
            jContentPane.add(getPanelButtons(), BorderLayout.SOUTH);
        }
        return jContentPane;
    }
    
    private JPanel getPanelButtons() {
        if (PanelButtons == null) {
            PanelButtons = new JPanel();
            PanelButtons.setLayout(new BoxLayout(PanelButtons, BoxLayout.LINE_AXIS));
            
            Dimension buttonSize = new Dimension(120, 30);
            
            PanelButtons.add(Box.createHorizontalGlue());
            
            ButtonOK = new JButton(_("OK"));
            ButtonOK.addActionListener(new ButtonOKActionListener());
            ButtonOK.setPreferredSize(buttonSize);
            PanelButtons.add(ButtonOK);
            
            PanelButtons.add(Box.createRigidArea(new Dimension(20, 1)));
            
            Action actCancel = new AbstractAction() {
                public void actionPerformed(ActionEvent e) {
                    dispose();
                };
            };
            actCancel.putValue(Action.NAME, _("Cancel"));
            ButtonCancel = new JButton(actCancel);
            ButtonCancel.getActionMap().put("EscapePressed", actCancel);
            ButtonCancel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "EscapePressed");
            ButtonCancel.setPreferredSize(buttonSize);
            PanelButtons.add(ButtonCancel);
        }
        return PanelButtons;
    }
    
    private JTabbedPane getTabMain() {
        if (TabMain == null) {
            TabMain = new JTabbedPane(JTabbedPane.BOTTOM, JTabbedPane.WRAP_TAB_LAYOUT);
            
            TabMain.addTab(_("Common"), getPanelCommon());
            TabMain.addTab(_("Delivery"), getPanelSendSettings());
            TabMain.addTab(MessageFormat.format(_("Table \"{0}\""), _("Received")), getPanelRecvFmt());
            TabMain.addTab(MessageFormat.format(_("Table \"{0}\""), _("Sent")), getPanelSentFmt());
            TabMain.addTab(MessageFormat.format(_("Table \"{0}\""), _("Transmitting")), getPanelSendingFmt());
        }
        return TabMain;
    }
    
    private JPanel getPanelCommon() {        
        if (PanelCommon == null) {
            double[][] tablelay = {
                    {border, TableLayout.FILL, border, 0.35, border},
                    { border, 0.65, border, TableLayout.FILL, border }
            };
            PanelCommon = new JPanel(new TableLayout(tablelay));
            
            PanelCommon.add(getPanelServer(), "1,1");
            PanelCommon.add(getPanelMisc(), "3,1");
            PanelCommon.add(getPanelPaths(), "1,3,3,3");
        }
        return PanelCommon;
    }
    
    
    private JPanel getPanelSendSettings() {
        if (panelSendSettings == null) {
            double[][] tablelay = {
                    {border, 0.6, border, TableLayout.FILL, border},
                    { border, TableLayout.FILL, border }
            };
            
            panelSendSettings = new JPanel(new TableLayout(tablelay));
            
            panelSendSettings.add(getPanelSend(), "1, 1"); 
            panelSendSettings.add(getPanelCover(), "3, 1");
        }
        return panelSendSettings;
    }
    
    private JPanel getPanelServer() {
        if (panelServer == null) {
            double[][] tablelay = {
                    {border, 0.22, border, 0.22, border, 0.22, border, TableLayout.FILL, border},
                    new double[11]
            };
            double rowh = 1 / (double)(tablelay[1].length - 2);
            tablelay[1][0] = 0;
            tablelay[1][tablelay[1].length - 1] = border;
            Arrays.fill(tablelay[1], 1, tablelay[1].length - 2, rowh);
            tablelay[1][tablelay[1].length - 2] = TableLayout.FILL;
            
            panelServer = new JPanel(new TableLayout(tablelay));
            panelServer.setBorder(BorderFactory.createTitledBorder(_("Server settings")));
                        
            
            textHost = new JTextField();
            textHost.addMouseListener(clpDef);
            textPort = new JTextField();
            textPort.addMouseListener(clpDef);
            textPort.setInputVerifier(new IntVerifier(1, 65536));
            textUser = new JTextField();
            textUser.addMouseListener(clpDef);
            textPassword = new JPasswordField();
            textAdminPassword = new JPasswordField();
            checkAskPassword = new JCheckBox(_("Always ask"));
            checkAskPassword.addItemListener(new ItemListener() {
               public void itemStateChanged(ItemEvent e) {
                   textPassword.setEnabled(!checkAskPassword.isSelected());
                } 
            });
            checkAskAdminPassword = new JCheckBox(_("Always ask"));
            checkAskAdminPassword.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    textAdminPassword.setEnabled(!checkAskAdminPassword.isSelected());
                 } 
             });
            
            checkPasv = new JCheckBox(_("Use passive mode to fetch faxes"));
            
            addWithLabel(panelServer, textHost, _("Host name:"), "1, 2, 5, 2, f, c");
            addWithLabel(panelServer, textPort, _("Port:"), "7, 4, f, c");
            addWithLabel(panelServer, textUser, _("Username:"), "1, 4, 5, 4, f, c");
            //addWithLabel(panelServer, textPassword, _("Password:"), "5, 4, 7, 4, f, c");
            addWithLabel(panelServer, textPassword, _("Password:"), "1, 6, 5, 6, f, c");
            panelServer.add(checkAskPassword, "6, 6, 7, 6, f, c");
            addWithLabel(panelServer, textAdminPassword, _("Admin Password:"), "1, 8, 5, 8, f, c");
            panelServer.add(checkAskAdminPassword, "6, 8, 7, 8, f, c");
            
            panelServer.add(checkPasv, "1, 9, 7, 9");
        }
        return panelServer;
    }
    
    private JPanel getPanelMisc() {
        if (panelMisc == null) {
            double[][] tablelay = {
                    {border, TableLayout.FILL, border},
                    new double[5]
            };
            double rowh = 1 / (double)(tablelay[1].length - 2);
            tablelay[1][0] = border;
            tablelay[1][tablelay[1].length - 1] = border;
            Arrays.fill(tablelay[1], 1, tablelay[1].length - 2, rowh);
            tablelay[1][tablelay[1].length - 2] = TableLayout.FILL;
            
            panelMisc = new JPanel(new TableLayout(tablelay));
            panelMisc.setBorder(BorderFactory.createTitledBorder(_("Miscellaneous settings")));
            
            comboNewFaxAction = new JComboBox(utils.newFaxActions);
            checkPCLBug = new JCheckBox("<html>" + _("Use PCL file type bugfix") + "</html>");
             
            addWithLabel(panelMisc, comboNewFaxAction, "<html>" + _("When a new fax is received:") + "</html>", "1, 2, 1, 2, f, c");
            panelMisc.add(checkPCLBug, "1, 3");
        }
        return panelMisc;
    }
    
    private JPanel getPanelSend() {
        if (panelSend == null) {
            double[][] tablelay = {
                    {border,  0.5, border, TableLayout.FILL, border},
                    new double[14]
            };
            double rowh = 1 / (double)(tablelay[1].length - 2);
            tablelay[1][0] = border;
            tablelay[1][tablelay[1].length - 1] = border;
            Arrays.fill(tablelay[1], 1, tablelay[1].length - 2, rowh);
            tablelay[1][tablelay[1].length - 2] = TableLayout.FILL;
            
            panelSend = new JPanel(new TableLayout(tablelay));
            panelSend.setBorder(BorderFactory.createTitledBorder(_("Delivery settings")));
           
            textNotifyAddress = new JTextField();
            textNotifyAddress.addMouseListener(clpDef);
            
            comboTZone = new JComboBox(utils.timezones);
            comboNotify = new JComboBox(utils.notifications);
            comboPaperSize = new JComboBox(utils.papersizes);
            comboResolution = new JComboBox(utils.resolutions);
            
            spinMaxDial = new JSpinner(new SpinnerNumberModel(12, 1, 100, 1));
            spinMaxTry = new JSpinner(new SpinnerNumberModel(6, 1, 100, 1));
            
            checkUseCustomDefCover = new JCheckBox(_("Use a custom default cover page:"));
            checkUseCustomDefCover.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    ftfCustomDefCover.setEnabled(checkUseCustomDefCover.isSelected());
                 } 
             });
            ftfCustomDefCover = new FileTextField();
            ftfCustomDefCover.getJTextField().addMouseListener(clpDef);
            ftfCustomDefCover.setFileFilters(new ExampleFileFilter("ps", _("Postscript files")));
            ftfCustomDefCover.setEnabled(false);
            
            addWithLabel(panelSend, textNotifyAddress, _("E-mail address for notifications:"), "1, 2, 3, 2, f, c");
            addWithLabel(panelSend, comboNotify, _("Notify when:"), "1, 4, 3, 4, f, c");
            addWithLabel(panelSend, comboTZone, _("Time zone:"), "1, 6, 3, 6, f, c");
            addWithLabel(panelSend, comboResolution, _("Resolution:"), "1, 8, f, c");
            addWithLabel(panelSend, comboPaperSize, _("Paper size:"), "3, 8, f, c" );
            addWithLabel(panelSend, spinMaxDial, _("Maximum dials:"), "1, 10, f, c");
            addWithLabel(panelSend, spinMaxTry, _("Maximum tries:"), "3, 10, f, c");    
            
            panelSend.add(checkUseCustomDefCover, "1, 11, 3, 11, l, b");
            panelSend.add(ftfCustomDefCover, "1, 12, 3, 12, f, c");
        }
        return panelSend;
    }
    
    private JPanel getPanelCover() {
        if (panelCover == null) {
            double[][] tablelay = {
                    {border,  TableLayout.FILL, border},
                    new double[12]
            };
            double rowh = 1 / (double)(tablelay[1].length - 2);
            tablelay[1][0] = border;
            tablelay[1][tablelay[1].length - 1] = border;
            Arrays.fill(tablelay[1], 1, tablelay[1].length - 2, rowh);
            tablelay[1][tablelay[1].length - 2] = TableLayout.FILL;
            
            panelCover = new JPanel(new TableLayout(tablelay));
            panelCover.setBorder(BorderFactory.createTitledBorder(_("Fax cover page from:")));
            
            textFromCompany = new JTextField();
            textFromCompany.addMouseListener(clpDef);
            textFromFaxNumber = new JTextField();
            textFromFaxNumber.addMouseListener(clpDef);
            textFromLocation = new JTextField();
            textFromLocation.addMouseListener(clpDef);
            textFromName = new JTextField();
            textFromName.addMouseListener(clpDef);
            textFromVoicenumber = new JTextField();
            textFromVoicenumber.addMouseListener(clpDef);
            
            addWithLabel(panelCover, textFromName, _("Name:"), "1, 2, f, c");
            addWithLabel(panelCover, textFromCompany, _("Company:"), "1, 4, f, c");
            addWithLabel(panelCover, textFromLocation, _("Location:"), "1, 6, f, c");
            addWithLabel(panelCover, textFromVoicenumber, _("Voice number:"), "1, 8, f, c");
            addWithLabel(panelCover, textFromFaxNumber, _("Fax number:"), "1, 10, f, c");
        }
        return panelCover;
    }
    
    private JPanel getPanelPaths() {
        if (panelPaths == null) {         
            double[][] tablelay = {
                    {border, TableLayout.FILL, border},
                    new double[6]
            };
            double rowh = 1 / (double)(tablelay[1].length - 2);
            tablelay[1][0] = 0;
            tablelay[1][tablelay[1].length - 1] = border;
            Arrays.fill(tablelay[1], 1, tablelay[1].length - 2, rowh);
            tablelay[1][tablelay[1].length - 2] = TableLayout.FILL;
            
            panelPaths = new JPanel(new TableLayout(tablelay));
            panelPaths.setBorder(BorderFactory.createTitledBorder(_("Path settings")));
            
            ftfFaxViewer = new ExeFileTextField();
            ftfFaxViewer.getJTextField().addMouseListener(clpDef);
            ftfPSViewer = new ExeFileTextField();
            ftfPSViewer.getJTextField().addMouseListener(clpDef);
            
            panelPaths.add(new JLabel(_("Command line for fax viewer: (insert %s as a placeholder for the filename)")), "1, 1 f b");
            panelPaths.add(ftfFaxViewer, "1, 2, f, c");
            panelPaths.add(new JLabel(_("Command line for Postscript viewer: (insert %s as a placeholder for the filename)")), "1, 3 f b");
            panelPaths.add(ftfPSViewer, "1, 4, f, c");
        }
        return panelPaths;
    }
    
    private JLabel addWithLabel(JPanel pane, JComponent comp, String text, String layout) {
        TableLayoutConstraints c = new TableLayoutConstraints(layout);
        
        pane.add(comp, c);
        
        JLabel lbl = new JLabel(text);
        lbl.setLabelFor(comp);
        c.row1 = c.row2 = c.row1 - 1;
        c.vAlign = TableLayoutConstraints.BOTTOM;
        c.hAlign = TableLayoutConstraints.LEFT;
        pane.add(lbl, c); 
        
        return lbl;
    }
    
    private fmtEditor getPanelRecvFmt() {
        if (PanelRecvFmt == null) {
            PanelRecvFmt = new fmtEditor(utils.recvfmts, recvfmt, Arrays.asList(utils.requiredRecvFmts));
        }
        return PanelRecvFmt;
    }
    
    private fmtEditor getPanelSendingFmt() {
        if (PanelSendingFmt == null) {
            PanelSendingFmt = new fmtEditor(utils.jobfmts, sendingfmt, Arrays.asList(utils.requiredSendingFmts));
        }
        return PanelSendingFmt;
    }
    
    private fmtEditor getPanelSentFmt() {
        if (PanelSentFmt == null) {
            PanelSentFmt = new fmtEditor(utils.jobfmts, sentfmt, Arrays.asList(utils.requiredSentFmts));
        }
        return PanelSentFmt;
    }
    
    public OptionsWin(FaxOptions foEdit, Frame owner) {
        super(owner);
        this.foEdit = foEdit;
        recvfmt = new Vector<FmtItem>(foEdit.recvfmt);
        sentfmt = new Vector<FmtItem>(foEdit.sentfmt);
        sendingfmt = new Vector<FmtItem>(foEdit.sendingfmt);
        
        initialize();
    }
    
    class ExeFileTextField extends FileTextField {
        protected String readTextFieldFileName() {
            return this.getText().replaceAll("\"|%s", "").trim();
        };
        
        @Override
        protected void writeTextFieldFileName(String fName) {
            if (fName.contains(" ")) 
                this.setText("\"" + fName + "\" %s");
            else
                this.setText(fName + " %s");
        }
    }
    
    class ButtonOKActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            
            if (checkUseCustomDefCover.isSelected()) {
                if (!(new File(ftfCustomDefCover.getText()).canRead())) {
                    JOptionPane.showMessageDialog(ButtonOK, _("The selected default cover page can not be read."), _("Error"), JOptionPane.ERROR_MESSAGE);
                    ftfCustomDefCover.getJTextField().requestFocus();
                    return;
                }
            }
            
            try {
                foEdit.port = Integer.parseInt(textPort.getText());
                
                foEdit.maxDial = ((Integer)spinMaxDial.getValue()).intValue();
                foEdit.maxTry = ((Integer)spinMaxTry.getValue()).intValue();
                
                foEdit.notifyAddress = textNotifyAddress.getText();
                foEdit.host = textHost.getText();
                foEdit.user = textUser.getText();
                foEdit.pass = textPassword.getText();
                foEdit.faxViewer = ftfFaxViewer.getText();
                foEdit.psViewer = ftfPSViewer.getText();
                foEdit.AdminPassword = textAdminPassword.getText();
                
                foEdit.notifyWhen = (FaxStringProperty)comboNotify.getSelectedItem();
                foEdit.paperSize = (PaperSize)comboPaperSize.getSelectedItem();
                foEdit.resolution = (FaxIntProperty)comboResolution.getSelectedItem();
                foEdit.tzone = (FaxStringProperty)comboTZone.getSelectedItem();
                foEdit.newFaxAction = (FaxIntProperty)comboNewFaxAction.getSelectedItem();
                
                foEdit.pasv = checkPasv.isSelected();
                foEdit.pclBug = checkPCLBug.isSelected();
                foEdit.askPassword = checkAskPassword.isSelected();
                foEdit.askAdminPassword = checkAskAdminPassword.isSelected();
                
                foEdit.recvfmt = recvfmt;
                foEdit.sentfmt = sentfmt;
                foEdit.sendingfmt = sendingfmt;
                
                foEdit.FromCompany = textFromCompany.getText();
                foEdit.FromFaxNumber = textFromFaxNumber.getText();
                foEdit.FromLocation = textFromLocation.getText();
                foEdit.FromName = textFromName.getText();
                foEdit.FromVoiceNumber = textFromVoicenumber.getText();
                
                foEdit.defaultCover = ftfCustomDefCover.getText();
                foEdit.useCustomDefaultCover = checkUseCustomDefCover.isSelected();
            } catch (NumberFormatException e1) {
                JOptionPane.showMessageDialog(ButtonOK, _("Please enter a number."));
                return;
            }
            
            modalResult = true;
            dispose();
        }
    }
}
