package doclet;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.DocErrorReporter;
import com.sun.javadoc.FieldDoc;
import com.sun.javadoc.RootDoc;

public class PropsDoclet {
    private static File outputDir = new File(".");
    
    public static boolean start(RootDoc root) throws IOException {
        //Parse options:
        for (String[] opt : root.options()) {
            if (opt[0].equals("-d")) {
                outputDir = new File(opt[1]);
            }
        }
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }
        
        Properties classProp = new Properties();
        for (ClassDoc cd : root.classes()) {
            classProp.clear();
            for (FieldDoc fd : cd.fields()) {
                if (fd.isPublic() && !fd.isStatic()) {
                    classProp.put(fd.name(), fd.commentText());
                }
            }
            
            FileOutputStream outStream = new FileOutputStream(new File(outputDir,cd.name() + ".properties"));
            classProp.store(outStream, "Auto-generated field descriptions by PropsDoclet");
            outStream.close();
        }
        return true;
    }
    
    public static int optionLength(String option) {
        if ("-d".equals(option)) 
            return 2;
        return 0;
    }
    
    public static boolean validOptions(String[][] options,
            DocErrorReporter reporter) {
//        for (String[] opt : options) {
//            if (!opt[0].equals("-d")) {
//                reporter.printError("Invalid option " + opt[0]);
//                return false;
//            }
//        }
        return true;
    }
}