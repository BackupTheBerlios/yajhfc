/**
 * 
 */
package yajhfc.settingstool;

import java.lang.reflect.Field;
import java.util.List;

import yajhfc.FmtItemList;
import yajhfc.Password;

/**
 * @author jonas
 *
 */
public class Setting implements Comparable<Setting> {
    public boolean isSelected;
    public final Field optionsField;
    public final String description;
    public Object value;

    public int compareTo(Setting o) {
        return optionsField.getName().compareToIgnoreCase(o.optionsField.getName());
    }

    //  @SuppressWarnings("unchecked")
    //  private static String[] getPropertyNames(Setting s, FaxOptions fo) {
    //      if (List.class.isAssignableFrom(s.optionsField.getType()) &&
    //              !FmtItemList.class.isAssignableFrom(s.optionsField.getType())) {
    //          int count;
    //          try {
    //              count = ((List)s.optionsField.get(fo)).size();
    //          } catch (Exception e) {
    //              e.printStackTrace();
    //              count = 0;
    //          } 
    //          String[] res = new String[count];
    //          String name = s.optionsField.getName();
    //          for (int i = 0; i < count; i++) {
    //              res[i] = name + '.' + (i+1);
    //          }
    //          return res;
    //      } else {
    //          return new String[] { getPropertyName(s) };
    //      }
    //  }

    public String getPropertyName() {
        if (List.class.isAssignableFrom(optionsField.getType()) && !FmtItemList.class.isAssignableFrom(optionsField.getType())) {
            return optionsField.getName() + ".1";
        } else if (Password.class.isAssignableFrom(optionsField.getType())) {
            return optionsField.getName() + "-obfuscated";
        } else {
            return optionsField.getName();
        }
    }

    public Setting(Field optionsField, String description) {
        this(false, optionsField, description, null);
    }

    public Setting(boolean isSelected, Field optionsField, String description, Object value) {
        super();
        this.isSelected = isSelected;
        this.optionsField = optionsField;
        this.description = description;
        this.value = value;
    } 
}
