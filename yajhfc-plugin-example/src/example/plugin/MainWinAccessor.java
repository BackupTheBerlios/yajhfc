package example.plugin;

import java.lang.reflect.Method;

import yajhfc.MainWin;
import yajhfc.launch.Launcher2;
import yajhfc.model.FmtItem;
import yajhfc.model.TableType;
import yajhfc.model.TooltipJTable;

/**
 * Use this class to access the currently selected table (model).
 * 
 * Due to the non-existence of a clean plugin interface in V 0.4.4, this class
 * uses reflection to call the protected MainWin methods
 * @author jonas
 *
 */

public class MainWinAccessor {

	private static Object callMainWinMethod(String name, Class<?>[] sig, Object[] args) {
		if (!(Launcher2.application instanceof MainWin)) {
			throw new IllegalStateException("Only works in normal startup mode");
		}
		
		try {
			Object mw = Launcher2.application;
			Class<?> mwcls = mw.getClass();
			Method callee = mwcls.getDeclaredMethod(name, sig);
			callee.setAccessible(true);
			return callee.invoke(mw, args);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
	}
	
	/**
	 * Returns the currently selected table
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static TooltipJTable<? extends FmtItem> getSelectedTable() {
		return (TooltipJTable<? extends FmtItem>)callMainWinMethod("getSelectedTable", new Class[0], new Object[0]);
	}
	
	/**
	 * Returns the table with the given index
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static TooltipJTable<? extends FmtItem> getTableByIndex(int index) {
		return (TooltipJTable<? extends FmtItem>)callMainWinMethod("getTableByIndex", new Class[] { Integer.TYPE }, new Object[] { index });
	}
	
	/**
	 * Returns the table of the specified kind of faxes (Received, Sent, ...)
	 */
	public static TooltipJTable<? extends FmtItem> getTableFor(TableType tt) {
		int index;
		switch (tt) {
		case ARCHIVE:
			index = 3;
			break;
		case RECEIVED:
			index = 0;
			break;
		case SENDING:
			index = 2;
			break;
		case SENT:
			index = 1;
			break;
		default:
			return null;
		}
		return getTableByIndex(index);
	}
}
