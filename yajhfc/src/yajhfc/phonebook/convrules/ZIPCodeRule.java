/*
 * YAJHFC - Yet another Java Hylafax client
 * Copyright (C) 2005-2008 Jonas Wolz
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
package yajhfc.phonebook.convrules;

import yajhfc.phonebook.PBEntryField;

/**
 * @author jonas
 *
 */
public enum ZIPCodeRule implements EntryToStringRule {
    
    ZIPCODE_LOCATION(new ConcatRule(PBEntryField.ZIPCode, " ", PBEntryField.Location)),
    LOCATION_STATE_ZIPCODE(new ConcatRule(PBEntryField.Location, " ", PBEntryField.State, " ", PBEntryField.ZIPCode)),
    ZIPCODE_LOCATION_STATE(new ConcatRule(PBEntryField.ZIPCode, " ", PBEntryField.Location, ", ", PBEntryField.State)),
    ;
    
    private final String displayName;
    private final EntryToStringRule rule;
    
    public String getDisplayName() {
        return displayName;
    }
    
    @Override
    public String toString() {
        return displayName;
    }

    public String applyRule(PBEntryFieldContainer entry) {
        return rule.applyRule(entry);
    }

    public int applyRule(PBEntryFieldContainer entry, StringBuilder appendTo) {
        return rule.applyRule(entry, appendTo);
    }

    private ZIPCodeRule(EntryToStringRule rule) {
        this.rule = rule;
        displayName = rule.toString();
    }
}
