/*
 * YAJHFC - Yet another Java Hylafax client
 * Copyright (C) 2011 Jonas Wolz
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
package yajhfc.console;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author jonas
 *
 */
public class BOMInputStream extends FilterInputStream {
    protected byte[] buf = new byte[16];
    protected int bufpos = 0;
    protected int buflen = 0;
    
    protected String detectedCharset = null;

    public BOMInputStream(InputStream in) throws IOException {
        super(in);
        detectBOM();
    }

    protected void detectBOM() throws IOException {
        buflen = in.read(buf);
        
        if (buf[0] == (byte)0xEF && buf[1] == (byte)0xBB && buf[2] == (byte)0xBF) { // UTF-8 BOM
            detectedCharset = "UTF-8";
            bufpos = 3; // skip BOM
        } else if (buf[0] == (byte)0xFE && buf[1] == (byte)0xFF) { // UTF-16 BE BOM
            detectedCharset = "UTF-16BE";
            bufpos = 2; // skip BOM
        } else if (buf[0] == (byte)0xFF && buf[1] == (byte)0xFE) { // UTF-16 LE BOM
            detectedCharset = "UTF-16LE";
            bufpos = 2; // skip BOM
        } else {
            detectedCharset = null;
            bufpos = 0;
        }
    }
    
    /**
     * Returns the charset detected by this stream. If no BOM was found, this returns null.
     * @return the detectedCharset
     */
    public String getDetectedCharset() {
        return detectedCharset;
    }
    
    @Override
    public int read() throws IOException {
        if (bufpos >= buflen) {
            return in.read();
        } else {
            return buf[bufpos++];
        }
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int remainingBuf = buflen - bufpos;
        if (remainingBuf <= 0) {
            return in.read(b, off, len);
        } else {
            int copyLen = Math.min(remainingBuf, len);
            System.arraycopy(buf, bufpos, b, off, copyLen);
            bufpos += copyLen;
            
            if (copyLen < len) {
                return copyLen + in.read(b, off+copyLen, len-copyLen);
            } else {
                return copyLen;
            }
        }
    }

    @Override
    public long skip(long n) throws IOException {
        int remainingBuf = buflen - bufpos;
        if (remainingBuf <= 0) {
            return in.skip(n);
        } else {
            long remainingSkip = n - remainingBuf;
            bufpos += (int)n;
            if (remainingSkip >= 0) {
                return in.skip(remainingSkip) + remainingBuf;
            } else {
                return n;
            }
        }
    }
}
