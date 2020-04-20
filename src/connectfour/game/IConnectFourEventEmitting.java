/* ConnectFour
 * File: IConnectFourEventEmitting.java
 * Creation: 26.06.2009 15:27:55
 * Author: Jan Gosmann (jan@hyper-world.de)
 * Copyright (C) 2009 Jan Gosmann
 */

/*****************************************************************************
 *  This program is free software: you can redistribute it and/or modify     *
 *  it under the terms of the GNU General Public License as published by     *
 *  the Free Software Foundation, either version 3 of the License, or        *
 *  (at your option) any later version.                                      *
 *                                                                           *
 *  This program is distributed in the hope that it will be useful,          *
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of           *
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the            *
 *  GNU General Public License for more details.                             *
 *                                                                           *
 *  You should have received a copy of the GNU General Public License        *
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.    *       
 *****************************************************************************/

package connectfour.game;

/**
 * Interface for Connect Four games which provides functions which emit events.
 */
public interface IConnectFourEventEmitting extends IConnectFour
{
    /**
     * Adds a new listener for {@link MoveDoneEvent MoveDoneEvent}.
     * @param listener Listener to add.
     */
    public void addMoveDoneListener( MoveDoneListener listener );
    
    /**
     * Removes a listener for {@link MoveDoneEvent MoveDoneEvent}.
     * @param listener Listener to remove.
     */
    public void removeMoveDoneListener( MoveDoneListener listener );
}
