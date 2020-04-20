/* ConnectFour
 * File: AiPlayer.java
 * Creation: 03.07.2009 15:00:44
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

import connectfour.game.ConnectFourAi;
import connectfour.game.LocalConnectFour;

/**
 * AI which makes a move automatically. This will run in an additional thread.
 * Therefore be careful with {@link ConnectFour#game ConnectFour.game}. It will
 * be locked by the AI while accessed.
 * In the current implementation it is not possible that another thread
 * accesses the object stored in ConnectFour.game while the AI is running.
 * Therefore you won't find locks/synchronization outside the AI.
 */
public class AiPlayer implements IPlayer
{
    ////////////////////////////////////////////////////////////////////////////
    // Member Variables
    ////////////////////////////////////////////////////////////////////////////
    /** The AI itself. */
    final private ConnectFourAi ai;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////
    /**
     * @param game Game to operate on.
     * @param recDepth Recursion depth.
     * @param difficulty Difficulty between 0 and 1 (or equal to 0 or 1).
     */
    public AiPlayer( LocalConnectFour game, int recDepth, double difficulty )
    {
        ai = new ConnectFourAi( game, recDepth, difficulty );
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // Methods
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void move()
    {
        ConnectFour.getGameView().setEnabled( false );
        ConnectFour.getStatusBar().setText(
            ConnectFour.tr( "AI is thinking." ) );
        new Thread( getAi() ).start();
    }

    @Override
    public void unregister()
    {
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // Simple Getters & Setters
    ////////////////////////////////////////////////////////////////////////////
    public ConnectFourAi getAi()
    {
        return ai;
    }
}
