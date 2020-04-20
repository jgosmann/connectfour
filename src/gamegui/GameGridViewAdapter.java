/* ConnectFour
 * File: GameGridViewAdapter.java
 * Creation: 03.07.2009 17:22:35
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

package gamegui;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * Adapter to {@link GameGridViewListener GameGridViewListener}.
 */
public class GameGridViewAdapter extends ComponentAdapter implements
        GameGridViewListener
{
    @Override
    public void animationEnded( ComponentEvent e )
    {
    }
}
