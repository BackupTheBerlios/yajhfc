/**
 * 
 */
package yajhfc.settingstool;

import static yajhfc.settingstool.EntryPoint._;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.swing.table.AbstractTableModel;

import yajhfc.FaxOptions;
import yajhfc.Utils;

/**
 * @author jonas
 *
 */
public class SettingsTableModel extends AbstractTableModel {

    protected List<Setting> availableSettings = new ArrayList<Setting>();
    protected FaxOptions fo = new FaxOptions();
    
    protected static final String[] columnNames = {
        _("Selected"),
        _("Name"),
        _("Description"),
        _("Value")
    };
    
    
    private static ResourceBundle fieldDescriptions = ResourceBundle.getBundle("yajhfc.settingstool.FaxOptionsDescription");

    private static String getFieldDescription(Field f) {
        try {
            return fieldDescriptions.getString(f.getName());
        } catch (Exception e) {
            return f.getName();
        }
    }
    
    public void loadAvailableSettings() {
        Properties p = new Properties();
        Utils.getFaxOptions().storeToProperties(p);
        fo.loadFromProperties(p);
        
        availableSettings.clear();
        for (Field f : FaxOptions.class.getFields()) {
            if (Modifier.isStatic(f.getModifiers())) 
                continue;
            
            Setting s = new Setting(f, getFieldDescription(f));
            try {
                s.value = f.get(fo);
            } catch (Exception e) {
                s.value = null;
                e.printStackTrace();
            } 
            availableSettings.add(s);
        }
        Collections.sort(availableSettings);
        
        fireTableDataChanged();
    }

    public void loadFromProperties(Properties p) {
        fo.loadFromProperties(p);
        for (Setting s : availableSettings) {
            if (p.containsKey(s.getPropertyName())) {
                s.isSelected = true;
            } else {
                s.isSelected = false;
            }
        }
        
        fireTableDataChanged();
    }
    
    public void storeToProperties(Properties p) {
        p.clear();
        
        List<Field> selectedProps = new ArrayList<Field>(availableSettings.size());
        for (Setting s : availableSettings) {
            if (s.isSelected) {
                selectedProps.add(s.optionsField);
            }
        }
        fo.storeToProperties(p, selectedProps.toArray(new Field[selectedProps.size()]));
    }
    
    public void selectAll(boolean selected) {
        for (Setting s : availableSettings) {
            s.isSelected = selected;
        }
        fireTableDataChanged();
    }
    
    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getColumnCount()
     */
    public int getColumnCount() {
        return columnNames.length;
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getRowCount()
     */
    public int getRowCount() {
        return availableSettings.size();
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    public Object getValueAt(int rowIndex, int columnIndex) {
        Setting s = availableSettings.get(rowIndex);
        switch (columnIndex) {
        case 0:
            return s.isSelected;
        case 1:
            return s.optionsField.getName();
        case 2:
            return s.description;
        case 3:
            return s.value;
        default:
            return null;
        }
    }
    
    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
    
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
        case 0:
            return Boolean.class;
        case 1:
            return String.class;
        case 2:
            return String.class;
        case 3:
            return Object.class;
        default:
            return null;
        }
    }
    
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        switch (columnIndex) {
        case 0:
            return true;
        case 3:
        case 1:
        case 2:
        default:
            return false;
        }
    }
    
    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        Setting s = availableSettings.get(rowIndex);
        switch (columnIndex) {
        case 0:
            s.isSelected = (Boolean)value;
            break;
        case 3:
            s.value = value;
            break;
        default:
            // Not editable
        }
    }

}
