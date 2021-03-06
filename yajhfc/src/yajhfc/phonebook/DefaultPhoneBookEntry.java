package yajhfc.phonebook;

import yajhfc.Utils;
import yajhfc.phonebook.convrules.PBEntryFieldContainer;

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

/**
 * 
 *  Phone book entry
 *  Phone book implementations can override this in order to
 *  save status information.
 *  New PhoneBookEntry classes are only to be created by (non-abstract) PhoneBook implementations.
 *  
 */
public abstract class DefaultPhoneBookEntry implements PhoneBookEntry {
    
    public abstract PhoneBook getParent();
    
    public abstract String getField(PBEntryField field);
    public abstract void setField(PBEntryField field, String value);
    
    public Object getFilterData(Object key) {
        return getField((PBEntryField)key);
    }
    
    /**
     * Deletes this entry from the phonebook
     */
    public abstract void delete();

    /**
     * Commits all changes made by the get/set-Methods
     */
    public abstract void commit();

    /**
     * Just update the displayed position (don't necessarily write through)
     */
    public void updateDisplay() {
        commit();
    }

    public void copyFrom(PBEntryFieldContainer other) {
        for (PBEntryField field : PBEntryField.values()) {
            setField(field, other.getField(field));
        }
    }

    public void refreshToStringRule() {
        // Do nothing here...
    }
    
    public String toString() {
        return getParent().getEntryToStringRule().applyRule(this);
//        String surname = getField(PBEntryField.Name);
//        String givenname = getField(PBEntryField.GivenName);
//
//        if (surname != null && surname.length() > 0) {
//            if (givenname != null && givenname.length() > 0)
//                //return surname + ", " + givenname;
//                return MessageFormat.format(Utils._("{0} {1}"), givenname, surname);
//            else
//                return surname;
//        } else {
//            if (givenname != null && givenname.length() > 0) {
//                return givenname;
//            } else {
//                String company = getField(PBEntryField.Company);
//                if (company != null && company.length() > 0) {
//                    return company;
//                } else {
//                    return Utils._("<no name>");
//                }
//            }
//        }
    }

    // The order items are compared in compareTo
    protected static final PBEntryField[] sortOrder;
    static {
        // Build sort order:
        // The prefix of fields that should preferably be compared
        final PBEntryField[] prefix = {
                PBEntryField.Name, PBEntryField.GivenName, PBEntryField.Company, PBEntryField.Location
        };
        // Append the rest of the fields to the prefix, so all fields are compared
        final PBEntryField[] vals = PBEntryField.values();
        sortOrder = new PBEntryField[vals.length];
        System.arraycopy(prefix, 0, sortOrder, 0, prefix.length);
        
        int j = prefix.length;
        for (PBEntryField field : vals) {
            // Add any field not in the prefix
            if (Utils.indexOfArray(prefix, field) < 0) {
                sortOrder[j++] = field;
            }
        }
    }

    public int compareTo(PhoneBookEntry o) {
        for (PBEntryField entry : sortOrder) {
            String val1 = getField(entry);
            String val2 = o.getField(entry);
            int cmp;
            if (val1 == val2) {
                cmp = 0;
            } else if (val1 != null) {
                if (val2 != null) {
                    cmp = val1.compareToIgnoreCase(val2);
                } else {
                    cmp = 1;
                }
            } else { // val1 == null
                if (val2 != null) {
                    cmp = -1;
                } else {
                    cmp = 0;
                }
            }
            
            if (cmp != 0) {
                return cmp;
            }
        }
        
        return 0;
    }
    
    @Override
    public int hashCode() {
        int hashCode = 0;
        for (PBEntryField entry : sortOrder) {
            String val = getField(entry);
            if (val != null) {
                hashCode ^= val.hashCode();
            }
        }
        return hashCode;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (obj instanceof PhoneBookEntry) {
            return compareTo((PhoneBookEntry)obj) == 0;
        } else {
            return false;
        }
    }
}
