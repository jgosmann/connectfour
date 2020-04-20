/* ConnectFour
 * File: IConnectFour.java
 * Creation: 25.06.2009 10:56:06
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
 * Interface providing methods which have to supported by a Connect Four game.
 * Implementations can vary in the grid size and how many discs a player needs
 * in a row to win (e.g. you could also implement a Connect Five class or a
 * class which allows to set these parameters).
 */
public interface IConnectFour
{
    ////////////////////////////////////////////////////////////////////////////
    // Enumerations
    ////////////////////////////////////////////////////////////////////////////
    /** Indexes the players */
    public enum Players
    {
        FIRST,
        SECOND,
        UNDEFINED
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // Methods
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Drops a disc in a specified column for the current player.
     * @param column Where to drop the disc. 
     * @return true, if successful; false, if move is not valid.
     */
    public boolean doMove( int column );
    
    /**
     * Checks whether dropping a disc in the specified column is a valid move.
     * @param column Where to drop the disc.
     * @return true, if the move is valid, otherwise false.
     */
    public boolean isValidMove( int column );
    
    /**
     * Undoes the last move if this is possible, otherwise the function does
     * nothing.
     * @return Array with the length to containing row and column of the undone
     *     move (or null if undo is not possible).
     */
    public int[] undo();
    
    ////////////////////////////////////////////////////////////////////////////
    // Simple Getters & Setters
    ////////////////////////////////////////////////////////////////////////////
    /** @return Returns the number of columns of the grid. */
    public  int getColumns();
    
    /** @return Returns the current player which can do a move. */
    public Players getCurrentPlayer();

    /** @return Returns whether the game is finished. */
    public boolean isGameFinished();
    
    /** @return Maximum number of turns allowed. */
    public int getMaxTurns();
    
    /** @return Returns the number of rows of the grid. */
    public int getRows();
    
    /**
     * @return Returns whether it is possible to undo moves.
     */
    public boolean isUndoPossible();

    /**
     * @return Returns the number of discs a player has to get in a row to win
     *     the game.
     */
    public int getWinLength();
    
    /** @return Returns the winner of the game. */
    public Players getWinner();
    
    /** @return Returns the column of the last field in the winning row. */
    public int getWinEndColumn();

    /** @return Returns the row of the last field in the winning row. */
    public int getWinEndRow();

    /** @return Returns the column of the first field in the winning row. */
    public int getWinStartColumn();
    
    /** @return Returns the row of the first field in the winning row. */
    public int getWinStartRow();   
    
    ////////////////////////////////////////////////////////////////////////////
    // Advanced Getters
    ////////////////////////////////////////////////////////////////////////////    
    /**
     * @param row Row of the field which should be returned. The indices start
     *     at the bottom of the grid.
     * @param column Column of the field which should be returned. The indices
     *     start at the left side of the grid.
     * @return Returns which players occupies the specified field in the
     *     grid or {@link Players Players.UNDEFINED} if unoccupied.
     */
    public Players getField( int row, int column )
        throws IndexOutOfBoundsException;
    
    /** @return Returns the next player. */
    public Players getNextPlayer();
}
