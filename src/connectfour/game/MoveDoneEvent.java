/* ConnectFour
 * File: MoveDoneEvent.java
 * Creation: 26.06.2009 15:40:51
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

import java.util.EventObject;

/**
 * This event is emitted by {@link IConnectFourEventEmitting
 * IConnectFourEventEmitting} subclasses when a player has done a valid move.
 */
public class MoveDoneEvent extends EventObject
{
    private static final long serialVersionUID = 1L;

    ////////////////////////////////////////////////////////////////////////////
    // Member Variables
    ////////////////////////////////////////////////////////////////////////////
    /** Column the player has insert his disc. */
    private int column;
    
    /** Row to which the disc has fallen. */
    private int row;
    
    /** Player that has done the move. */
    private IConnectFour.Players player;
    
    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////
    /**
     * @param source Game emitting the event.
     * @param column Column the player inserted his disc.
     * @param player Player which has done the move. 
     */
    public MoveDoneEvent( IConnectFourEventEmitting source, int row, int column,
                          IConnectFour.Players player )
    {
        super( source );
        setRow( row );
        setColumn( column );
        setPlayer( player );
    }

    ////////////////////////////////////////////////////////////////////////////
    // Simple Getters & Setters
    ////////////////////////////////////////////////////////////////////////////
    protected void setColumn( int column )
    {
        this.column = column;
    }

    public int getColumn()
    {
        return column;
    }

    protected void setPlayer( IConnectFour.Players player )
    {
        this.player = player;
    }

    public IConnectFour.Players getPlayer()
    {
        return player;
    }
    
    protected void setRow( int row )
    {
        this.row = row;
    }
    
    public int getRow()
    {
        return row;
    }
}
