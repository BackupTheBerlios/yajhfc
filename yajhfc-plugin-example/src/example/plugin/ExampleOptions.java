package example.plugin;

import yajhfc.AbstractFaxOptions;

/**
 * Example class to save options in the YajHFC settings file if you need to.
 * 
 * To load/save options, you just need to create a subclass of AbstractFaxOptions
 * and add public fields for your options to it.
 * 
 * Then you can call the loadFromProperties/storeToProperties methods 
 * to load/store them to a properties file (e.g. the one returned
 * by Utils.getSettingsProperties()).
 * @author jonas
 *
 */
public class ExampleOptions extends AbstractFaxOptions {
	
	public String exampleOption1 = "foo";
	public int exampleOption2 = 42;
	public boolean exampleOption3 = true;
	
	/**
	 * Call the super constructor with the prefix that should be prepended
	 * to the options name.
	 */
	public ExampleOptions() {
		super("exampleplugin");
	}
}
