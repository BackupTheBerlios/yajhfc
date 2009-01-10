/**
 * 
 */
package yajhfc.settingstool;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;

import yajhfc.plugin.PluginManager;
import yajhfc.plugin.PluginUI;

/**
 * @author jonas
 *
 */
public class EntryPoint {

    public static final String AppShortName = "YajHFC Settings tool";
    public static final String AppCopyright = "Copyright © 2009 by Jonas Wolz";
    public static final String AppVersion = "0.1";
    public static final String AuthorEMail = "jwolz@freenet.de";
    public static final String HomepageURL = "http://yajhfc.berlios.de/"; 
    
    public static String _(String key) {
        return key;
    }
    
    /**
     * @param args
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
           public void run() {
               SettingsTool st = new SettingsTool();
               st.setVisible(true);
            } 
        });
    }

    public static boolean init() {
        final Action settingsToolAction = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                SettingsTool st = new SettingsTool();
                st.setVisible(true);
            }
        };
        settingsToolAction.putValue(Action.NAME, _("Settings tool..."));
        
        PluginManager.pluginUIs.add(new PluginUI() {
            public JMenuItem[] createMenuItems() {
                return new JMenuItem[] { new JMenuItem(settingsToolAction) };
            };
        });

        return true;
    }
}
