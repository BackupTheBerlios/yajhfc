// SendListener.java - a HylaFAX Job representation
// $Id: SendListener.java,v 1.1 2005/09/25 10:42:12 jonas Exp $
//
// Copyright 2003 Innovation Software Group, LLC - http://www.innovationsw.com
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

/**
 * This interface defines what a class interested in receiving 
 * send Job related events should implement.  A Listener should 
 * register for events from a Notifier.
 * @author $Author: jonas $
 * @version $Id: SendListener.java,v 1.1 2005/09/25 10:42:12 jonas Exp $
 * @see gnu.hylafax.job.Notifier
 * @see gnu.hylafax.job.Event
 **/
public interface SendListener
{

	/**
	 * This method is called when send Job state changes.
	 */
	public void onSendEvent(SendEvent details);

}// SendListener class
// SendListener.java
