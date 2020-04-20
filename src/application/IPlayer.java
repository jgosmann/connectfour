/* ConnectFour
 * File: Player.java
 * Creation: 03.07.2009 14:37:56
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

package application;

/**
 * Interface for deriving classes which set up the GUI for a player and do the
 * move.
 */
interface IPlayer
{
    ////////////////////////////////////////////////////////////////////////////
    // Methods
    ////////////////////////////////////////////////////////////////////////////
    /**
     * If this method is called the GUI should set to the right state for
     * the player type and do the move.
     */
    public void move();

    /**
     * This method is called to unregister the player. After calling this
     * method any events of the GUI should have no effect via this class.
     */
    public void unregister();
}
