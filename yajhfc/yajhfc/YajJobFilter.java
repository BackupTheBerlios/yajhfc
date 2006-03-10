package yajhfc;

import java.util.Vector;

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

public interface YajJobFilter {
    /**
     * Should return true if job is to be shown (i.e. not filtered), false otherwise. 
     * @param job
     * @return
     */
    public boolean jobIsVisible(YajJob job);
    
    /**
     * Initialize filter. It is guaranteed that the columns or filter properties
     * are not changed during the subsequent jobIsVisible() calls.
     * @param columns
     */
    public void initFilter(Vector<FmtItem> columns);
    
    /**
     * Validates this filter against the new set of columns.
     * Returns true if this filter still applies to them, false if it should be removed.
     * @param columns
     * @return
     */
    public boolean validate(Vector<FmtItem> columns);
}