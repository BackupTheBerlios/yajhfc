package yajhfc.filters;
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
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;

import yajhfc.FmtItem;
import yajhfc.FmtItemRenderer;
import yajhfc.YajJobFilter;
import yajhfc.utils;

public class FilterPanel extends JPanel implements ActionListener {

    private JComboBox comboColumns, comboOperator;
    private JTextField textValue;
    private JButton buttonDelete;
    private DefaultComboBoxModel colModel;
    private Class<?> oldClass;
    
    private static final FmtItem voidFmtItem = new FmtItem("", utils._("(none)"), Void.class);
    private static final String[] comboOperatorDummy = { "                         " };
    
    public void addDeleteActionListener(ActionListener al) {
        buttonDelete.addActionListener(al);
    }
    
    public void removeDeleteActionListener(ActionListener al) {
        buttonDelete.removeActionListener(al);
    }
    
    public boolean isDeleteEnabled() {
        return buttonDelete.isEnabled();
    }
    
    public void setDeleteEnabled(boolean value) {
        buttonDelete.setEnabled(value);
    }
    
   public void setDeleteActionCommand(String cmd) {
        buttonDelete.setActionCommand(cmd);
    }
    
    public void initFromFilter(YajJobFilter filter) {
        if (filter == null)
            return;
        
        FmtItem col = FilterCreator.columnFromFilter(filter);
        if (col != null) {
            comboColumns.setSelectedItem(col);
            Object op = FilterCreator.operatorFromFilter(filter);
            if (op != null) {
                comboOperator.setSelectedItem(op);
                if (FilterCreator.isInputEnabled(col.dataClass))
                    textValue.setText(FilterCreator.inputFromFilter(filter));
                else
                    textValue.setText("");
            }
        } else
            comboColumns.setSelectedIndex(0);
    }
    
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("columnsel")) {
            Class<?> colClass = ((FmtItem)comboColumns.getSelectedItem()).dataClass;
            if (oldClass != colClass) {
                oldClass = colClass;
                Object [] ops = FilterCreator.getOperators(colClass);
                if (ops == null) {
                    comboOperator.setEnabled(false);
                    textValue.setEnabled(false);
                } else {
                    comboOperator.setEnabled(true);
                    comboOperator.setModel(new DefaultComboBoxModel(ops));
                    textValue.setEnabled(FilterCreator.isInputEnabled(colClass));
                }
            }
        }
    }
    
    public void focusInput() {
        textValue.selectAll();
        textValue.requestFocus();
    }
    
    public YajJobFilter getFilter() throws ParseException {
        return FilterCreator.getFilter((FmtItem)comboColumns.getSelectedItem(), comboOperator.getSelectedItem(), textValue.getText());
    }
    
    @Override
    public Dimension getMaximumSize() {
        return new Dimension(Integer.MAX_VALUE, getPreferredSize().height);
    }
    
    public FilterPanel(Vector<FmtItem> columns) {
        super(null, false);
        
        final int border = 12;
        
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        setBorder(BorderFactory.createCompoundBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED), BorderFactory.createEmptyBorder(border, border, border, border)));
        
        colModel = new DefaultComboBoxModel(new Vector<FmtItem>(columns));
        colModel.insertElementAt(voidFmtItem, 0);
        comboColumns = new JComboBox(colModel);
        comboColumns.setRenderer(new FmtItemRenderer());
        comboColumns.setActionCommand("columnsel");
        comboColumns.addActionListener(this);
        
        comboOperator = new JComboBox(comboOperatorDummy);
        comboOperator.setEnabled(false);
        Dimension dim = new Dimension(comboOperator.getPreferredSize());
        comboOperator.setMaximumSize(dim);
        comboOperator.setMinimumSize(dim);
        comboOperator.setPreferredSize(dim);
        
        textValue = new JTextField(20);
        textValue.setEnabled(false);
        
        buttonDelete = new JButton(utils.loadIcon("general/Delete"));
        buttonDelete.setToolTipText(utils._("Remove this condition"));
        
        add(comboColumns);
        add(Box.createHorizontalStrut(border));
        add(comboOperator);
        add(Box.createHorizontalStrut(border));
        add(textValue);
        add(Box.createHorizontalStrut(border));
        add(buttonDelete);
        
        comboColumns.setSelectedIndex(0);
    }

    public FilterPanel(Vector<FmtItem> columns, YajJobFilter initValue) {
        this(columns);
        initFromFilter(initValue);
    }
}