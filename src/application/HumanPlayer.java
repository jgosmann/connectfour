/* ConnectFour
 * File: HumanPlayer.java
 * Creation: 03.07.2009 14:41:36
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

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Human player, gets the move via the game grid view.
 */
class HumanPlayer implements IPlayer
{
    ////////////////////////////////////////////////////////////////////////////
    // Member Variables
    ////////////////////////////////////////////////////////////////////////////
    /** Player name */
    private String playerName;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////
    /** Shorthand for {@link #HumanPlayer(String) HumanPlayer("")}. */
    public HumanPlayer()
    {
        this( "" );
    }
    
    public HumanPlayer( String playerName )
    {
        setPlayerName( playerName );
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // Methods
    ////////////////////////////////////////////////////////////////////////////    
    @Override
    public void move()
    {  
        ConnectFour.getGameView().setEnabled( true );
        ConnectFour.getGameView().addMouseListener( onGameGridViewClick );
        ConnectFour.getStatusBar().setText(
            getPlayerName() + ": " + ConnectFour.tr( "Please move." ) );
    }

    @Override
    public void unregister()
    {
        ConnectFour.getGameView().removeMouseListener( onGameGridViewClick );    
    }

    private MouseAdapter onGameGridViewClick = new MouseAdapter() {
        @Override
        public void mouseClicked( MouseEvent e )
        {
            if( ConnectFour.getGame() == null )
                return;
            
            ConnectFour.getGameView().setEnabled( false );
            ConnectFour.getStatusBar().setText( " " );
            ConnectFour.getGameView().removeMouseListener(
                onGameGridViewClick );
            
            if( !ConnectFour.getGame().doMove(
                     ConnectFour.getGameView().coordToColumn( e.getX() ) ) )
            {
                move();
                ConnectFour.getStatusBar().setText( 
                     getPlayerName() + ConnectFour.tr( ": Illegal move." ) );
            }
            
        }
    };
    
    ////////////////////////////////////////////////////////////////////////////
    // Simple Getters & Setters
    ////////////////////////////////////////////////////////////////////////////
    public void setPlayerName( String playerName )
    {
        this.playerName = playerName;
    }

    public String getPlayerName()
    {
        return playerName;
    }
}
