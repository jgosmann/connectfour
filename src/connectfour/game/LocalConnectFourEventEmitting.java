/* ConnectFour
 * File: LocalConnectFourEventEmitting.java
 * Creation: 26.06.2009 15:55:06
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

import java.util.LinkedList;

/**
 *
 */
public class LocalConnectFourEventEmitting extends LocalConnectFour implements
    IConnectFourEventEmitting
{
    ////////////////////////////////////////////////////////////////////////////
    // Member Variables
    ////////////////////////////////////////////////////////////////////////////
    private LinkedList<MoveDoneListener> moveDoneListener =
        new LinkedList<MoveDoneListener>();
    
    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////
    /** @see LocalConnectFour#LocalConnectFour() */
    public LocalConnectFourEventEmitting()
    {
        super();
    }

    /** @see LocalConnectFour#LocalConnectFour(int, int) */
    public LocalConnectFourEventEmitting( int row, int column )
    {
        super( row, column );
    }

    /** @see LocalConnectFour#LocalConnectFour(int, int, int) */
    public LocalConnectFourEventEmitting( int row, int column, int winLength )
    {
        super( row, column, winLength );
    }

    /**
     * @see LocalConnectFour#LocalConnectFour(int, int, int,
     * connectfour.game.IConnectFour.Players)
     */
    public LocalConnectFourEventEmitting( int row, int column, int winLength,
                                          Players startPlayer )
    {
        super( row, column, winLength, startPlayer );
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public boolean doMove( int column )
    {
        IConnectFour.Players player = getCurrentPlayer();
        if( !super.doMove( column ) )
            return false;
        int row = getRows() - 1;
        for( ; row > 0 && getField( row, column )
                          == IConnectFour.Players.UNDEFINED;
             row-- )
            ;
        MoveDoneEvent e = new MoveDoneEvent( this, row, column, player );
        for( MoveDoneListener l : moveDoneListener )
            l.moveDone( e );
        return true;
    }
    
    @Override
    public void addMoveDoneListener( MoveDoneListener listener )
    {
        moveDoneListener.add( listener ); 
    }

    @Override
    public void removeMoveDoneListener( MoveDoneListener listener )
    {
        moveDoneListener.remove( listener );
    }
}
