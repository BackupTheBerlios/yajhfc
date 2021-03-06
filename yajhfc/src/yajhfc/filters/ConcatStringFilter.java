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
package yajhfc.filters;

import java.text.FieldPosition;
import java.text.Format;

/**
 * @author jonas
 *
 */
public class ConcatStringFilter<V extends FilterableObject, K extends FilterKey> extends AbstractStringFilter<V, K> {

    protected Object[] concatVals;
    protected Object[] resolvedConcatVals;
    protected Class<K> keyClass;
    
    protected StringBuffer concatBuffer = new StringBuffer();
    private FieldPosition dummyFieldPos;
    protected FieldPosition getDummyFieldPos() {
        if (dummyFieldPos == null) {
            dummyFieldPos = new FieldPosition(0);
        }
        return dummyFieldPos;
    }
    
    /**
     * Creates a new ConcatStringFilter. When a match is performed the objects specified
     * in concatVals are concatenated first as follows:
     * If an element is instanceof keyClass, it is treated as column and the respective value is fetched,
     * else simply the element's toString() method is called
     * @param concatVals
     * @param operator
     * @param compareValue
     * @param caseSensitive
     */
    public ConcatStringFilter(Class<K> keyClass, Object[] concatVals, StringFilterOperator operator,
            String compareValue, boolean caseSensitive) {
        super(operator, compareValue, caseSensitive);
        this.concatVals = concatVals;
        this.keyClass = keyClass;
    }

    @SuppressWarnings("unchecked")
    public void initFilter(FilterKeyList<K> columns) {
        resolvedConcatVals = new Object[concatVals.length];
        for (int i=0; i < concatVals.length; i++) {
            Object val = concatVals[i];
            if (keyClass.isInstance(val)) {
                resolvedConcatVals[i] = columns.translateKey((K)val);
            } else {
                resolvedConcatVals[i] = null;
            }
        }
    }

    public boolean matchesFilter(V filterObj) {
        concatBuffer.setLength(0);
        for (int i=0; i < resolvedConcatVals.length; i++) {
            Object key = concatVals[i];
            Object resolvedKey = resolvedConcatVals[i];
            if (resolvedKey == null) {
                concatBuffer.append(key);
            } else {
                Object v = filterObj.getFilterData(resolvedKey);
                if (v != null) {
                    Format colFormat = ((FilterKey)key).getFormat();
                    if (colFormat == null) {
                        concatBuffer.append(v);
                    } else {
                        colFormat.format(v, concatBuffer, getDummyFieldPos());
                    }
                } // else append nothing...
            }
        }
        return doActualMatch(concatBuffer.toString());
    }

    @SuppressWarnings("unchecked")
    public boolean validate(FilterKeyList<K> columns) {
        for (int i=0; i < concatVals.length; i++) {
            Object val = concatVals[i];
            if (keyClass.isInstance(val)) {
                if (!columns.containsKey((K)val)) {
                    return false;
                }
            }
        }
        return true;
    }

    public Object[] getConcatVals() {
        return concatVals;
    }
    
    @Override
    protected void fieldToString(StringBuilder appendTo) {
        appendTo.append('[');
        for (int i=0; i < concatVals.length; i++) {
            Object val = concatVals[i];
            if (keyClass.isInstance(val)) {
                appendTo.append(val);
            } else {
                appendTo.append('\"').append(val).append('\"');
            }
            if (i < concatVals.length - 1) {
                appendTo.append('+');
            }
        }
        appendTo.append(']');
    }
}
