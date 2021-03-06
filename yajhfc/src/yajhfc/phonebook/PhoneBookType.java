package yajhfc.phonebook;

import java.awt.Dialog;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

/*
 * YAJHFC - Yet another Java Hylafax client
 * Copyright (C) 2006 Jonas Wolz
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

public class PhoneBookType {

    protected Class<? extends PhoneBook> targetClass;
    
    public Class<? extends PhoneBook> getTargetClass() {
        return targetClass;
    }
    
    public boolean canExport() {
        try {
            Field f = targetClass.getField("PB_CanExport");
            return (Boolean)f.get(null);
        } catch (Exception e) {
            return false;
        }
    }
    
    public String getPrefix() {
        try {
            Field f = targetClass.getField("PB_Prefix");
            return (String)f.get(null);
        } catch (Exception e) {
            return targetClass.getCanonicalName() ;
        }
    }
    
    public String getDisplayName() {
        try {
            Field f = targetClass.getField("PB_DisplayName");
            return (String)f.get(null);
        } catch (Exception e) {
            return targetClass.getName();
        }
    }
    
    public String getDescription() {
        try {
            Field f = targetClass.getField("PB_Description");
            return (String)f.get(null);
        } catch (Exception e) {
            return targetClass.toString();
        }
    }
    
    public PhoneBook createInstance(Dialog parent) {
        try {
            Constructor<? extends PhoneBook> cons = targetClass.getConstructor(new Class[] { Dialog.class } );
            return cons.newInstance(new Object[] { parent } );
        } catch (Exception e) {
            return null;
        }
    }
    
    public PhoneBookType(Class<? extends PhoneBook> targetClass) {
        this.targetClass = targetClass;
    }
}
