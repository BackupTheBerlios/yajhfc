// BasicSendNotifier.java - a HylaFAX Job representation
// $Id: BasicSendNotifier.java,v 1.1 2005/09/25 10:42:12 jonas Exp $
//
// Copyright 2003, Innovation Software Group, LLC - http://www.innovationsw.com
//                Joe Phillips <joe.phillips@innovationsw.com>
//
// for information on the HylaFAX FAX server see
//  http://www.hylafax.org/
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Library General Public
// License as published by the Free Software Foundation; either
// version 2 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Library General Public License for more details.
//
// You should have received a copy of the GNU Library General Public
// License along with this library; if not, write to the Free
// Software Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
//
package gnu.hylafax.job;

// system includes
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

// home-grown includes
import gnu.inet.ftp.ServerResponseException;

/**
 * This interface defines what a class should implement in order to notify others of job related events.
 * @author $Author: jonas $
 * @version $Id: BasicSendNotifier.java,v 1.1 2005/09/25 10:42:12 jonas Exp $
 * @see gnu.hylafax.job.SendListener
 * @see gnu.hylafax.job.SendEvent
 **/
public class BasicSendNotifier implements SendNotifier
{
	private List listeners= null;

	public BasicSendNotifier()
	{
		listeners= new ArrayList();
	}

	/**
	 * This method is called when Job state changes.
	 */
	public void notifySendListeners(SendEvent details)
	{
		if(details == null){
			return;
		}
		synchronized(listeners){
			Iterator i= listeners.iterator();
			while(i.hasNext()){
				SendListener l= (SendListener)i.next();
				l.onSendEvent(details);
			}
		}
	}// notifyListeners

	/**
	 * This method is called to register a Job Listener.
	 */
	public void addSendListener(SendListener l)
	{
		if(l == null){
			return;
		}
		synchronized(listeners){
			listeners.add(l);
		}
	}

	/**
	 * This method is used to deregister a Job Listener.
	 */
	public void removeSendListener(SendListener l)
	{
		if(l == null){
			return;
		}
		synchronized(listeners){
			listeners.remove(l);
		}
	}

}// BasicSendNotifier class
// BasicSendNotifier.java
