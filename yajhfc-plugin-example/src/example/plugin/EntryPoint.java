package example.plugin;

import gnu.hylafax.HylaFAXClient;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.Properties;

import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import yajhfc.HylaClientManager;
import yajhfc.MainWin;
import yajhfc.Utils;
import yajhfc.launch.Launcher2;
import yajhfc.model.FmtItem;
import yajhfc.model.table.FaxListTableModel;
import yajhfc.options.PanelTreeNode;
import yajhfc.plugin.PluginManager;
import yajhfc.plugin.PluginUI;
import yajhfc.server.ServerManager;
import yajhfc.util.ExcDialogAbstractAction;

/**
 * Example initialization class for a YajHFC plugin.
 * 
 * The name of this class can be chosen freely, but must match the name
 * set in the YajHFC-Plugin-InitClass entry in the jar file.
 * @author jonas
 *
 */
public class EntryPoint {

	/**
	 * Plugin initialization method.
	 * The name and signature of this method must be exactly as follows 
	 * (i.e. it must always be "public static boolean init(int)" )
	 * @param startupMode the mode YajHFC is starting up in. The possible
	 *    values are one of the STARTUP_MODE_* constants defined in yajhfc.plugin.PluginManager
	 * @return true if the initialization was successful, false otherwise.
	 */
	public static boolean init(int startupMode) {

		// Do your initialization here instead of the println...
		System.out.println("Doing initialization stuff...");

		// Add some UI elements
		// You need only to override the methods for the UI elements you wish to use
		//  (i.e. if you do not want an options page, do not override getOptionsPanelParent
		//    and createOptionsPanel)
		PluginManager.pluginUIs.add(new PluginUI() {
			@Override
			public int getOptionsPanelParent() {
				// This method sets where the options panel will be put.
				// Currently OPTION_PANEL_ADVANCED and OPTION_PANEL_ROOT are supported
				return OPTION_PANEL_ADVANCED;
			}

			@Override
			public PanelTreeNode createOptionsPanel(PanelTreeNode parent) {
				/*
				 * This method must return a PanelTreeNode as shown below
				 * or null to not create an options page
				 */
				return new PanelTreeNode(
						parent, // Always pass the parent as first parameter
						new ExampleOptionsPanel(), // The actual UI component that implements the options panel. 
						                        // This object *must* implement the OptionsPage interface.
						"Example plugin", // The text displayed in the tree view for this options page
						null);            // The icon displayed in the tree view for this options page
			}

			@Override
			public void saveOptions(Properties p) {
				// This method is called at exit of YajHFC to allow for saving your settings
				getOptions().storeToProperties(p);
			}

			@Override
			public JMenuItem[] createMenuItems() {
				Action exampleAction = new ExcDialogAbstractAction() {
					@Override
					public void actualActionPerformed(ActionEvent e) {
						// Show a simple message box
						JOptionPane.showMessageDialog((Component)e.getSource(), "Hello world!");
					}
				};
				exampleAction.putValue(Action.NAME, "Example plugin...");
				
				Action countAction = new ExcDialogAbstractAction() {
					@Override
					public void actualActionPerformed(ActionEvent e) {
						
						FaxListTableModel<? extends FmtItem> tableModel = ((MainWin)Launcher2.application).getSelectedTable().getRealModel();
						JOptionPane.showMessageDialog((Component)e.getSource(), 
							"Number of rows: " + tableModel.getRowCount() + "\n" + 
							"Number of cols: " + tableModel.getColumns().size()
								 );
					}
				};
				countAction.putValue(Action.NAME, "Count number of faxes...");
				
				Action serverAction = new ExcDialogAbstractAction() {
					@Override
					public void actualActionPerformed(ActionEvent e) {
						HylaClientManager cliMan = ServerManager.getDefault().getCurrent().getClientManager();
						// Get a HylaFAXClient instance by calling beginServerTransaction
						HylaFAXClient hyfc = cliMan.beginServerTransaction(Launcher2.application.getDialogUI());
						
						try {
							JOptionPane.showMessageDialog((Component)e.getSource(), 
								hyfc.getServerVersion());
						} finally {
							// Make sure that for each beginServerTransaction call
							// endServerTransaction is called *exactly* once
							cliMan.endServerTransaction();
						}
					}
				};
				serverAction.putValue(Action.NAME, "Show server info...");
				
				JMenu exampleMenu = new JMenu("Example plugin");
				exampleMenu.add(new JMenuItem(exampleAction));
				exampleMenu.add(new JMenuItem(countAction));
				exampleMenu.add(new JMenuItem(serverAction));
				
				// Return an array of menu items that should be added to the Extras menu
				// If you want to add many menu items, please create and return
				// a single menu item that creates a sub menu here.
				return new JMenuItem[] {
						exampleMenu
				};
			}
		});

		return true;
	}
	
	
	private static ExampleOptions options;
	/**
	 * Lazily load some options (optional, only if you want to save settings)
	 * @return
	 */
    public static ExampleOptions getOptions() {
        if (options == null) {
            options = new ExampleOptions();
            options.loadFromProperties(Utils.getSettingsProperties());
        }
        return options;
    }
    
    /**
     * Launches YajHFC including this plugin (for debugging purposes)
     * @param args
     */
    public static void main(String[] args) {
		PluginManager.internalPlugins.add(EntryPoint.class);
		Launcher2.main(args);
	}
}
