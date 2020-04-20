/* ConnectFour
 * File: LocalConnectFour.java
 * Creation: 25.06.2009 17:05:49
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

import java.util.Arrays;
import java.util.Stack;

import connectfour.game.IConnectFour.Players;

/**
 * Implementation of a Connect Four game running locally.
 */
public class LocalConnectFour implements Cloneable
{
    ////////////////////////////////////////////////////////////////////////////
    // Static Member Variables
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Specifies the standard value for the corresponding member variable
     * without the "std" prefix.
     * @see #columns
     * @see #rows
     * @see #winLength
     */
    private final static int stdColumns = 8, stdRows = 8, stdWinLength = 4;
    
    ////////////////////////////////////////////////////////////////////////////
    // Member Variables
    ////////////////////////////////////////////////////////////////////////////
    /** Specifies the size of the game grid. */
    private int rows, columns;
    
    /** Specifies how many discs a player must get in a row to win. */
    private int winLength;
    
    /** Specifies the current player which has to do a move. */
    private Players currentPlayer;
    
    /** Specifies whether the game has already ended. */
    private boolean gameFinished = false;
    
    /** Specifies the winner if the game has already ended. */
    private Players winner = Players.UNDEFINED;
    
    /**
     * Saves at which positions discs of which player are. The entries in the
     * first dimension are the rows of the grid; in the second dimension the
     * columns. 
     */
    Players[][] grid;
    
    /** Saves the start row position of the winning row. */
    private int winStartRow;
    /** Saves the start column position of the winning row. */
    private int winStartColumn;
    /** Saves the end row position of the winning row. */
    private int winEndRow;
    /** Saves the end column position of the winning row. */
    private int winEndColumn;
    
    /** Maximum number of allowed turns. Set to -1 to do not limit the turns. */
    private int maxTurns = -1;
    
    /**
     * Saves columns the moves of the players for the undo function.
     */
    private final Stack<Integer> moves = new Stack<Integer>();

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Shorthand for {@link #LocalConnectFour(int, int)
     * LocalConnectFour(getStdRows(), getStdColumns())}.
     */
    LocalConnectFour()
    {
        this( getStdRows(), getStdColumns() );
    }
    
    /**
     * Shorthand for {@link #LocalConnectFour(int, int, int)
     * LocalConnectFour(rows, columns, IConnectFour.getStdWinLength())}.
     */
    LocalConnectFour( int rows, int columns )
    {
        this( rows, columns, getStdWinLength() );
    }
    
    /**
     * Shorthand for {@link #LocalConnectFour(int, int, int,
     * IConnectFour.Players) LocalConnectFour(rows, columns, winLength,
     * IConnectFour.Players.UNDEFINED)}.
     */
    LocalConnectFour( int rows, int columns, int winLength )
    {
        this( rows, columns, winLength, Players.UNDEFINED );
    }

    /**
     * @param rows Number of rows of the game's grid.
     * @param columns Number of columns of the game's grid.
     * @param winLength How many discs a player must get in a row to win.
     * @param startPlayer Player which begins the game. If set to
     *     {@link IConnectFour.Players IConnectFour.Players.UNDEFINED} the
     *     player beginning the game will be determined randomly.
     */
    public LocalConnectFour( int rows, int columns, int winLength,
                             Players startPlayer )
    {
        setGridSize( rows, columns );
        setWinLength( winLength );
        if( startPlayer == Players.UNDEFINED )
        {
            if( Math.random() < 0.5 )
                startPlayer = Players.FIRST;
            else
                startPlayer = Players.SECOND;
        }
        setCurrentPlayer( startPlayer );
        initGrid( rows, columns );
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // Methods
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Clears {@link #winStartRow winStartRow}, {@link #winStartColumn
     * winStartColumn}, {@link #winEndRow winEndRow}, {@link #winEndColumn
     * winEndColumn}.
     */
    protected void clearWinPosition()
    {
        setWinStartRow( -1 );
        setWinStartColumn( -1 );
        setWinEndRow( -1 );
        setWinEndColumn( -1 );
    }
    
    /**
     * Checks whether the game has ended, sets {@link #gameFinished
     * gameFinished} and {@link #winner winner}.
     * If m and n are the width and height of the grid the used algorithm
     * needs O(m*n) time. Every field of the grid will be checked once.
     */
    protected void checkAndSetFinished()
    {
        if( getMaxTurns() > -1 && getMoves().size() >= getMaxTurns() )
        {
            setWinner( Players.UNDEFINED );
            setGameFinished( true );
            return;
        }
        
        /* How the algorithm works:
         * The algorithm uses some helper variables. To count how many discs
         * a player has in a row. A positive value means Players.FIRST has that
         * many discs in a row, a negative value means Players.SECOND has the
         * absolute value of discs in a row. These helper variables are always
         * set to zero if the row of one player ends.
         * There are helper variables for the row currently processed, the
         * columns (as an array) and the diagonals (also as array).
         * Notice that the diagonals can be treated as columns if you shift the
         * rows. Each row has to be shifted one field to right relative to the
         * row below it for diagonals from the top left to the bottom right.
         * This shift can be calculated via the indices for the rows (i) and
         * columns (j).
         */
        int possibleDiags = getColumns() + getRows()
                            - 2 * (getWinLength()-1);
        if( possibleDiags < 0 )
            possibleDiags = 0;
        
        int inRow = 0;
        int[] inColumns = new int[getColumns()];
        /* Down/Up as viewed from left to right: */
        int[] inDiagsDown = new int[possibleDiags];
        int[] inDiagsUp   = new int[possibleDiags];
        Arrays.fill( inColumns, 0 );
        Arrays.fill( inDiagsDown, 0 );
        Arrays.fill( inDiagsUp, 0 );
        
        boolean emptyFieldDiscoverd = false;
        
        for( int i = 0; i < getRows(); i++ ) // Rows
        {
            inRow = 0;
            for( int j = 0; j < getColumns(); j++ ) // Columns
            {
                int occupiedBy; // Representing the player occupying the current
                    // field. Players.FIRST is 1, Players.SECOND is -1, and
                    // in all other cases (Players.UNDEFINED).
                switch( getField( i, j ) )
                {
                    case FIRST:  occupiedBy =  1; break;
                    case SECOND: occupiedBy = -1; break;
                    default:
                        occupiedBy =  0;
                        emptyFieldDiscoverd = true;
                        break;
                }
                
                final int idxDiagDown = j+i - (getWinLength()-1);
                final int idxDiagUp   = j+(getRows()-1-i)
                                        - (getWinLength()-1); 
                
                inRow = countInRow( inRow, occupiedBy );
                inColumns[j] = countInRow( inColumns[j], occupiedBy );
                if( 0 <= idxDiagDown && idxDiagDown < possibleDiags )
                    inDiagsDown[idxDiagDown] = countInRow(
                        inDiagsDown[idxDiagDown], occupiedBy );
                if( 0 <= idxDiagUp && idxDiagUp < possibleDiags )
                    inDiagsUp[idxDiagUp] = countInRow(
                        inDiagsUp[idxDiagUp], occupiedBy );
                
                /* Check rows */
                if( Math.abs( inRow ) >= getWinLength() )
                {
                    setWinner( (inRow > 0) ? Players.FIRST : Players.SECOND );
                    setGameFinished( true );
                    setWinStartRow( i );
                    setWinStartColumn( j - getWinLength() + 1 );
                    setWinEndRow( i );
                    setWinEndColumn( j );
                    return;
                }
                /* Check columns */
                if( Math.abs( inColumns[j] ) >= getWinLength() )
                {
                    setWinner( (inColumns[j] > 0) ? Players.FIRST
                                                  : Players.SECOND );
                    setGameFinished( true );
                    setWinStartRow( i - getWinLength() + 1 );
                    setWinStartColumn( j );
                    setWinEndRow( i );
                    setWinEndColumn( j );
                    return;
                }
                /* Check diagonals */
                if( 0 <= idxDiagDown && idxDiagDown < possibleDiags
                    && Math.abs( inDiagsDown[idxDiagDown] ) >= getWinLength() )
                {
                    setWinner( (inDiagsDown[idxDiagDown] > 0) ? Players.FIRST
                                                              : Players.SECOND );
                    setGameFinished( true );
                    setWinStartRow( i - getWinLength() + 1 );
                    setWinStartColumn( j + getWinLength() - 1  );
                    setWinEndRow( i );
                    setWinEndColumn( j );
                    return;
                }
                if( 0 <= idxDiagUp && idxDiagUp < possibleDiags
                        && Math.abs( inDiagsUp[idxDiagUp] ) >= getWinLength() )
                {
                    setWinner( (inDiagsUp[idxDiagUp] > 0) ? Players.FIRST
                                                          : Players.SECOND );
                    setGameFinished( true );
                    setWinStartRow( i - getWinLength() + 1 );
                    setWinStartColumn( j - getWinLength() + 1 );
                    setWinEndRow( i );
                    setWinEndColumn( j );
                    return;
                }
            }
        }
        
        if( !emptyFieldDiscoverd )
        {
            setWinner( Players.UNDEFINED );
            setGameFinished( true );
        }
    }
    
    @Override
    public LocalConnectFour clone()
    {
        LocalConnectFour cloned = new LocalConnectFour(
            getRows(), getColumns(), getWinLength(), getCurrentPlayer() );
        for( int i = 0; i < getRows(); i++ )
        {
            for( int j = 0; j < getColumns(); j++ )
                cloned.setField( i, j, getField( i, j ) );
        }
        for( Integer move : getMoves() )
            cloned.getMoves().push( move );
        cloned.setGameFinished( isGameFinished() );
        cloned.setWinner( getWinner() );
        return cloned;
    }
    
    /**
     * This function is used to count if a player has enough discs in a row
     * (or column or diagonal).
     * @param x Already counted discs. Sign determines the player.
     * @param y Player of which is the next disc in the row. The
     *     absolute value should be one and the sign determines the player.
     * @return x if it is > game.getWinLength(); 0 if y is zero.; x+y if both
     *     have the same sign, otherwise y.
     */
    protected int countInRow( int x, int y )
    {
        if( Math.abs( x ) >= getWinLength() )
            return x;
        if( y == 0 )
            return 0;
        return ((x < 0) == (y < 0)) ? (x + y) : y;
    }
    
    public boolean doMove( int column )
    {
        if( !isValidMove( column ) )
            return false;
        
        int i = 0;
        for( ; i < getRows() && getField( i, column ) != Players.UNDEFINED;
             i++ )
            ;
        setField( i, column, getCurrentPlayer() );
        setCurrentPlayer( getNextPlayer() );
        
        getMoves().push( column );
        
        checkAndSetFinished();
        return true;
    }
    
    /**
     * Initializes the grid with the passed size. Old values in the grid, if
     * any, are lost.
     * @param rows Width of the game's grid.
     * @param columns Height of the game's grid.
     */
    private void initGrid( int rows, int columns )
    {
        grid = new Players[rows][columns];
        for( int i = 0; i < rows; i++ )
            Arrays.fill( grid[i], IConnectFour.Players.UNDEFINED );
        clearWinPosition();
    }

    public boolean isValidMove( int column )
    {
        if( getMaxTurns() > -1 && getMoves().size() >= getMaxTurns() )
            return false;
        
        if( 0 <= column && column < getColumns()
            && getField( getRows() - 1, column ) == Players.UNDEFINED
            && !isGameFinished() )
        {
            return true;
        }
        return false;
    }

    public int[] undo()
    {
        if( !isUndoPossible() )
            return null;
        int column = getMoves().pop();
        int row;
        for( row = getRows() - 1;
             row >= 0 && getField( row, column ) == Players.UNDEFINED; row-- )
            ;
        setField( row, column, Players.UNDEFINED );
        setCurrentPlayer( getNextPlayer() );
        setGameFinished( false );
        setWinner( Players.UNDEFINED );
        clearWinPosition();
        return new int[] { row, column };
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // Simple Getters & Setters
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Old values in the game's grid are deleted when calling this function.
     */
    protected void setColumns( int columns )
    {
        this.columns = columns;
        initGrid( getRows(), getColumns() );
    }
    
    public Stack<Integer> getMoves()
    {
        return moves;
    }

    /**
     * Old values in the game's grid are deleted when calling this function.
     */
    protected void setRows( int rows )
    {
        this.rows = rows;
        initGrid( getRows(), getColumns() );
    }
    
    public boolean isUndoPossible()
    {
        return !getMoves().empty();
    }

    public int getColumns()
    {
        return columns;
    }
    
    public Players getCurrentPlayer()
    {
        return currentPlayer;
    }
    
    public void setCurrentPlayer( Players currentPlayer )
    {
        this.currentPlayer = currentPlayer;
    }

    protected void setGameFinished( boolean gameFinished )
    {
        this.gameFinished = gameFinished;
    }

    public boolean isGameFinished()
    {
        return gameFinished;
    }

    public int getRows()
    {
        return rows;
    }
    
    public static int getStdColumns()
    {
        return stdColumns;
    }
    
    public static int getStdRows()
    {
        return stdRows;
    }
    
    public static int getStdWinLength()
    {
        return stdWinLength;
    }
    
    public void setWinLength( int winLength )
    {
        this.winLength = winLength;
    }

    public int getWinLength()
    {
        return winLength;
    }

    protected void setWinner( Players winner )
    {
        this.winner = winner;
    }

    public Players getWinner()
    {
        return winner;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // Advanced Getters
    ////////////////////////////////////////////////////////////////////////////
    public Players getField( int row, int column )
        throws IndexOutOfBoundsException
    {
        return grid[row][column];
    }
    
    public Players getNextPlayer()
    {
        if( getCurrentPlayer() == Players.UNDEFINED )
            return Players.UNDEFINED;
        else
            return Players.class.getEnumConstants()[
                1 - getCurrentPlayer().ordinal()];
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // Advanced Setters
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Sets a field in the grid.
     * @param row The row in which the field lies.
     * @param column The column in which the field lies.
     * @param player Player to which the field should be set.
     */
    protected void setField( int row, int column, Players player )
        throws IndexOutOfBoundsException
    {
        grid[row][column] = player;
    }
    
    /**
     * Sets the game's grid size.
     * Old values in the game's grid are deleted when calling this function.
     * @param rows Number of rows of the game's grid.
     * @param columns Number of columns of the game's grid.
     */
    protected void setGridSize( int rows, int columns )
    {
        setRows( rows );
        setColumns( columns );
        initGrid( getRows(), getColumns() );
    }

    public void setMaxTurns( int maxTurns )
    {
        this.maxTurns = maxTurns;
    }

    public int getMaxTurns()
    {
        return maxTurns;
    }

    protected void setWinEndColumn( int winEndColumn )
    {
        this.winEndColumn = winEndColumn;
    }
    
    public int getWinEndColumn()
    {
        return winEndColumn;
    }
    
    protected void setWinEndRow( int winEndRow )
    {
        this.winEndRow = winEndRow;
    }

    public int getWinEndRow()
    {
        return winEndRow;
    }

    protected void setWinStartColumn( int winStartColumn )
    {
        this.winStartColumn = winStartColumn;
    }

    public int getWinStartColumn()
    {
        return winStartColumn;
    }
    
    protected void setWinStartRow( int winStartRow )
    {
        this.winStartRow = winStartRow;
    }
    
    public int getWinStartRow()
    {
        return winStartRow;
    }    
}
