package yajhfc.faxcover.fop;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;

import yajhfc.utils;
import yajhfc.FileConverter.ConversionException;
import yajhfc.faxcover.Faxcover;
import yajhfc.faxcover.MarkupFaxcover;

public class FOPFaxcover extends MarkupFaxcover {

    public FOPFaxcover(URL coverTemplate) {
        super(coverTemplate); 
    }
    
    @Override
    protected void convertMarkupToHyla(File tempFile, OutputStream out)
            throws IOException, ConversionException {
        FOPFileConverter.SHARED_INSTANCE.convertToHylaFormat(tempFile, out, pageSize);
    }
    
    // Testing code:
    public static void main(String[] args) throws Exception {
        System.out.println("Creating cover page...");
        Faxcover cov = new FOPFaxcover(new URL("file:/home/jonas/java/yajhfc/test.fo"));

        cov.comments = "foo\niniun iunuini uinini ninuin iuniuniu 9889hz h897h789 bnin uibiubui ubuib uibub ubiu bib bib ib uib i \nbar";
        cov.fromCompany = "foo �&� OHG";
        cov.fromFaxNumber = "989898";
        cov.fromLocation = "Bardorf";
        cov.fromVoiceNumber = "515616";
        cov.fromMailAddress = "a@bc.de";

        //cov.pageCount = 10;
//      String[] docs = { "/home/jonas/mozilla.ps", "/home/jonas/nssg.pdf" };
//      for (int i=0; i<docs.length; i++)
//      try {
//      System.out.println(docs[i] + " pages: " + cov.estimatePostscriptPages(new FileInputStream(docs[i])));
//      } catch (FileNotFoundException e) {
//      e.printStackTrace();
//      } catch (IOException e) {
//      e.printStackTrace();
//      }

        cov.pageCount = 55;
        cov.pageSize = utils.papersizes[0];
        cov.regarding = "Test fax";
        cov.sender = "Werner Mei�ner";

        cov.toCompany = "B�r GmbH & Co. KGaA";
        cov.toFaxNumber = "87878787";
        cov.toLocation = "Foost�dtle";
        cov.toName = "Otto M�ller";
        cov.toVoiceNumber = "4545454";

        try {
            String outName = "/tmp/test.pdf";
            cov.makeCoverSheet(new FileOutputStream(outName));
            Runtime.getRuntime().exec(new String[] { "xpdf", outName } );
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
