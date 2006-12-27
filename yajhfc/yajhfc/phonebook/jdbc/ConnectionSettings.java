package yajhfc.phonebook.jdbc;
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

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import yajhfc.utils;

public class ConnectionSettings {

    public String driver = "";
    public String dbURL = "";
    public String user = "";
    public String pwd = "";
    public boolean askForPWD = false;
    public String table = "";
    
    public String name = "";
    public String givenName = "";
    public String title = "";
    public String location = "";
    public String company = "";
    public String faxNumber = "";
    public String voiceNumber = "";
    public String comment = "";
    
    private static final String separator = ";";
    private static final char escapeChar = '~';
    
    public static String noField = "<none>";
    public static String noField_translated = utils._("<none>");
    
    public static boolean isNoField(String fieldName) {
        return (fieldName.length() == 0 || fieldName.equals(noField));
    }
    
    public void copyFrom(ConnectionSettings other) {
        if (other == null)
            return;
        
        for (Field f : getClass().getFields()) {
            if (Modifier.isStatic(f.getModifiers()))
                continue;
            if (Modifier.isFinal(f.getModifiers()))
                continue;
            
            try {
                f.set(this, f.get(other));
            } catch (Exception e) {
                //NOP
            }
        }
    }
    
    public String saveToString() {
        StringBuilder builder = new StringBuilder();
        for (Field f : getClass().getFields()) {
            if (Modifier.isStatic(f.getModifiers()))
                continue;
            if (Modifier.isFinal(f.getModifiers()))
                continue;
            
            try {
                String val;
                val = f.get(this).toString();
                val = utils.escapeChars(val, separator, escapeChar) ;
                builder.append(f.getName());
                builder.append('=');
                builder.append(val);
                builder.append(separator);
            } catch (Exception e) {
                //NOP
            }
        }
        
        return builder.toString();
    }
    
    public void loadFromString(String input) {
        String[] tokens = input.split(separator);
        for (String line : tokens) {
            int pos = line.indexOf('=');
            if (pos < 0)
                continue;
            
            String fieldName = line.substring(0, pos);
            String value = utils.unEscapeChars(line.substring(pos+1), separator, escapeChar);
            try {
                Field f = getClass().getField(fieldName);
                Class<?> f_class = f.getType();
                if (f_class == String.class) {
                    f.set(this, value);
                } else if (f_class == Boolean.TYPE || f_class == Boolean.class) {
                    f.set(this, Boolean.valueOf(value));
                } else {
                    System.err.println("Unsupported field type: " + f_class.getName());
                }
            } catch (NoSuchFieldException e) {
                System.err.println("Unknown field " + fieldName);
            } catch (Exception e) {
                System.err.println("Exception loading fields:");
                e.printStackTrace();
            }
        }
    }
    
    public ConnectionSettings() {
        super();
    }
    
    public ConnectionSettings(String serialized) {
        this();
        loadFromString(serialized);
    }
    
    public ConnectionSettings(ConnectionSettings src) {
        this();
        copyFrom(src);
    }
}
