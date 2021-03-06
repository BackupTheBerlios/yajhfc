package yajhfc.filters.ui;
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
import info.clearthought.layout.TableLayout;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.text.ParseException;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;

import yajhfc.Utils;
import yajhfc.filters.AndFilter;
import yajhfc.filters.CombinationFilter;
import yajhfc.filters.Filter;
import yajhfc.filters.FilterKey;
import yajhfc.filters.FilterKeyList;
import yajhfc.filters.FilterableObject;
import yajhfc.filters.OrFilter;

public class CustomFilterDialog<V extends FilterableObject,K extends FilterKey> extends JDialog 
    implements ActionListener {

    private JButton buttonOK, buttonCancel, buttonAdd;
    private JRadioButton radAll, radAny;
    private ButtonGroup groupAllAny;
    private JScrollPane scrollConditions;
    private JPanel panelConditions, panelCondButtons;
    
    private final String noConditionsEnteredPrompt;
    private final String invalidDateTimeMsg;
    private final String invalidNumberMsg = Utils._("Please enter a valid number!");
    private FilterKeyList<K> columns;
    
    public boolean okClicked = false;
    public Filter<V,K> returnValue = null;
    
    private void initialize(String title, String prompt, FilterKeyList<K> columns, Filter<V,K> init) {
        final int border = 12;
        double[][] dLay = {
                { border, TableLayout.FILL, border, TableLayout.PREFERRED, border},
                { border, TableLayout.PREFERRED, border / 2, TableLayout.PREFERRED, border / 2, TableLayout.PREFERRED, border, TableLayout.FILL, border}
        };
        this.columns = columns;
        
        JPanel contentPane = new JPanel(new TableLayout(dLay));
        
        buttonOK = new JButton(Utils._("OK"));
        buttonOK.setActionCommand("ok");
        buttonOK.addActionListener(this);
        /*buttonCancel = new JButton(Utils._("Cancel"));
        buttonCancel.setActionCommand("cancel");
        buttonCancel.addActionListener(this);*/
        Action actCancel = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                returnValue = null;
                okClicked = false;
                dispose();
            };
        };
        actCancel.putValue(Action.NAME, Utils._("Cancel"));
        buttonCancel = new JButton(actCancel);
        buttonCancel.getActionMap().put("EscapePressed", actCancel);
        buttonCancel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "EscapePressed");
        
        Dimension maxButtonDim = new Dimension(buttonOK.getPreferredSize());
        maxButtonDim.width = Integer.MAX_VALUE;
        buttonOK.setMaximumSize(maxButtonDim);
        buttonCancel.setMaximumSize(maxButtonDim);
        
        Box boxButtons = Box.createVerticalBox();
        boxButtons.add(buttonOK);
        boxButtons.add(Box.createVerticalStrut(border));
        boxButtons.add(buttonCancel);
        boxButtons.add(Box.createVerticalGlue());
        
        buttonAdd = new JButton(Utils._("Add"));
        buttonAdd.setActionCommand("add");
        buttonAdd.addActionListener(this);
        /*buttonLess = new JButton(Utils._("Less"));
        buttonLess.setActionCommand("less");
        buttonLess.addActionListener(this);*/
        panelCondButtons = new JPanel(null, false);
        panelCondButtons.setBorder(BorderFactory.createEmptyBorder(border, border, border, border));
        panelCondButtons.setLayout(new BoxLayout(panelCondButtons, BoxLayout.LINE_AXIS));
        panelCondButtons.add(buttonAdd);
        /*panelCondButtons.add(Box.createHorizontalStrut(border));
        panelCondButtons.add(buttonLess);*/
        panelCondButtons.add(Box.createHorizontalGlue());
        panelCondButtons.setMaximumSize(new Dimension(Integer.MAX_VALUE, panelCondButtons.getPreferredSize().height));
        
        JLabel lblPrompt = new JLabel(prompt);
        
        radAny = new JRadioButton(Utils._("Any of the following conditions"));
        radAny.setSelected(true);
        radAll = new JRadioButton(Utils._("All of the following conditions"));
        groupAllAny = new ButtonGroup();
        groupAllAny.add(radAny);
        groupAllAny.add(radAll);
        
        panelConditions = new JPanel(null);
        panelConditions.setLayout(new BoxLayout(panelConditions, BoxLayout.PAGE_AXIS));        
        scrollConditions = new JScrollPane(panelConditions, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        if (init != null && (init instanceof CombinationFilter<?,?>)) {
            for (Filter<V,K> yjf : ((CombinationFilter<V, K>)init).getChildList()) {
                FilterPanel<V,K> fp = newFilterPanel();
                fp.initFromFilter(yjf);
                panelConditions.add(fp);
            }
            if (init instanceof OrFilter<?,?>) {
                radAny.setSelected(true);
            } else {
                radAll.setSelected(true);
            }
        }
        // Ensure that at least 3 panels are visible:
        if (panelConditions.getComponentCount() < 3) {
            for (int i = panelConditions.getComponentCount(); i < 3; i++)
                panelConditions.add(newFilterPanel());
        }
        panelConditions.add(panelCondButtons);
        panelConditions.add(Box.createVerticalGlue());
        
        contentPane.add(lblPrompt, "1, 1");
        contentPane.add(radAll, "1, 3");
        contentPane.add(radAny, "1, 5");
        contentPane.add(scrollConditions, "1, 7");
        contentPane.add(new JSeparator(JSeparator.VERTICAL), "2, 0, 2, 8, c, f");
        contentPane.add(boxButtons, "3, 1, 3, 7");
        
        this.setTitle(title);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setContentPane(contentPane);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                returnValue = null;
                okClicked = false;
            };
            
            public void windowClosed(java.awt.event.WindowEvent e) {
                Utils.getFaxOptions().customFilterBounds = getBounds();
            };
        });
        
        //this.setMinimumSize(this.getSize());
        if (Utils.getFaxOptions().customFilterBounds != null) {
            this.setBounds(Utils.getFaxOptions().customFilterBounds);
        } else {
            this.pack();
            //this.setLocationByPlatform(true);
            Utils.setDefWinPos(this);
        }
    }
    
    private FilterPanel<V,K> newFilterPanel() {
        FilterPanel<V,K> fp = new FilterPanel<V,K>(columns);
        fp.setDeleteActionCommand("fp_delete");
        fp.addDeleteActionListener(this);
        
        return fp;
    }
    
    @SuppressWarnings("unchecked")
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if (cmd.equals("ok")) {
            CombinationFilter<V,K> af;
            if (radAll.isSelected())
                af = new AndFilter<V,K>();
            else
                af = new OrFilter<V,K>();
            
            for (Component comp : panelConditions.getComponents()) {
                if (comp instanceof FilterPanel<?,?>) {
                    try {
                        Filter<V,K> ch = ((FilterPanel<V,K>)comp).getFilter();
                        if (ch != null)
                            af.addChild(ch);
                    } catch (ParseException e1) {
                        JOptionPane.showMessageDialog(this, invalidDateTimeMsg, Utils._("Error"), JOptionPane.ERROR_MESSAGE);
                        ((FilterPanel<V,K>)comp).focusInput();
                        return;
                    } catch (NumberFormatException e1) {
                        JOptionPane.showMessageDialog(this, invalidNumberMsg, Utils._("Error"), JOptionPane.ERROR_MESSAGE);
                        ((FilterPanel<V,K>)comp).focusInput();
                        return;
                    }
                }
            }
            if (af.childCount() == 0) {
                if (JOptionPane.showConfirmDialog(this, noConditionsEnteredPrompt , Utils._("Question"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    af = null;
                } else {
                    return;
                }
            }
            returnValue = af;
            okClicked = true;
            dispose();
        } /*else if (cmd.equals("cancel")) {
            returnValue = null;
            dispose();
        } */ else if (cmd.equals("add")) {
            panelConditions.add(newFilterPanel(), panelConditions.getComponentCount() - 2);
            panelConditions.revalidate();
            panelCondButtons.scrollRectToVisible(panelCondButtons.getBounds());
        } else if (cmd.equals("fp_delete")) {
            panelConditions.remove(((JComponent)e.getSource()).getParent());
            panelConditions.revalidate();
        }
    }
    
    public CustomFilterDialog(Frame owner, String title, String prompt, String noConditionsEnteredPrompt, String invalidDateTimeMsg,
            FilterKeyList<K> columns, Filter<V,K> init) throws HeadlessException {
        super(owner, true);
        this.invalidDateTimeMsg = invalidDateTimeMsg;
        this.noConditionsEnteredPrompt = noConditionsEnteredPrompt;
        initialize(title, prompt, columns, init);
    }

    public CustomFilterDialog(Dialog owner, String title, String prompt, String noConditionsEnteredPrompt, String invalidDateTimeMsg,
            FilterKeyList<K> columns, Filter<V,K> init) throws HeadlessException {
        super(owner,  true);
        this.invalidDateTimeMsg = invalidDateTimeMsg;
        this.noConditionsEnteredPrompt = noConditionsEnteredPrompt;
        initialize(title, prompt, columns, init);
    }
}
