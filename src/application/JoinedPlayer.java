/* ConnectFour
 * File: JoinedPlayer.java
 * Creation: 08.07.2009 22:44:39
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

import java.io.IOException;

import javax.swing.JOptionPane;

import connectfour.game.RemoteCfPlayer;

/**
 * Joined player for a hosted game.
 */
public class JoinedPlayer implements IPlayer
{
    private RemoteCfPlayer remote;
    
    public JoinedPlayer( RemoteCfPlayer remote )
    {
        setRemote( remote );
    }
    
    public void move()
    {
        ConnectFour.getGameView().setEnabled( false );
        ConnectFour.getStatusBar().setText(
            ConnectFour.tr( "Waiting for move from opponent." ) );
        
        try
        {
            remote.sendLastMove();
        }
        catch( IOException e )
        {
            JOptionPane.showMessageDialog( ConnectFour.getMainWindow(),
                ConnectFour.tr( "Network I/O error, game stopped: " )
                    + e.toString(),
                ConnectFour.tr( "Network error" ),
                JOptionPane.WARNING_MESSAGE );
            ConnectFour.getGameView().setEnabled( false );
        }
    }

    public void unregister()
    {
    }

    public void setRemote( RemoteCfPlayer remote )
    {
        this.remote = remote;
    }

    public RemoteCfPlayer getRemote()
    {
        return remote;
    }
}
