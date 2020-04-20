/* ConnectFour
 * File: ConnectFourAi.java
 * Creation: 01.07.2009 09:38:02
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

import java.util.ArrayList;
import java.util.Arrays;

import connectfour.game.IConnectFour.Players;

/**
 * Provides an artificial intelligence for connect four.
 */
public class ConnectFourAi implements Runnable
{
    ////////////////////////////////////////////////////////////////////////////
    // Member Variables
    ////////////////////////////////////////////////////////////////////////////
    /** Game in which the AI is a players. */
    private LocalConnectFour game;

    /**
     * Copy of the game on which the AI operates to ensure that {@link #game
     * game} is unchanged until the best move has been found.
     */
    private LocalConnectFour gameCopy;
    
    /**
     * Rating for winning the game. Because in two's complement the range of
     * negative number is not the same as the range of the positive numbers,
     * we subtract one to be on the safe side (even if the negative number range
     * should be one larger than the positive number range).
     */
    private final static int winRating = Integer.MAX_VALUE - 1;
    
    /** Recursion depth */
    private int recDepth;
    
    /**
     * Difficulty. Has to be 0 <= difficulty <= 1. 1 is hardest, 0 is easiest. 
     */
    private double difficulty;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Shorthand for {@link #ConnectFourAi(LocalConnectFour, int)
     * ConnectFourAi(game, 5)}.
     */
    public ConnectFourAi( LocalConnectFour game )
    {
        this( game, 5 );
    }
    
    /**
     * Shorthand for {@link #ConnectFourAi(LocalConnectFour, int)
     * ConnectFourAi(game, recDepth, 1)}.
     */
    public ConnectFourAi( LocalConnectFour game, int recDepth )
    {
        this( game, recDepth, 1 );
    }
    
    /**
     * @param game The in game in which the AI is a player.
     * @param recDepth Recursion depth.
     * @param difficulty Difficulty of AI.
     */
    public ConnectFourAi( LocalConnectFour game, int recDepth,
                          double difficulty )
    {
        setGame( game );
        setRecDepth( recDepth );
        setDifficulty( difficulty );
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // Methods
    ////////////////////////////////////////////////////////////////////////////
    /**
     * This function is used to sum the discs of a player in a row. It behaves
     * slightly different the {@link LocalConnectFour#countInRow(int, int)
     * LocalConnectfour.countInRow()} and is <strong>only</strong> used for
     * rows and not columns or diagonals.
     * @param game Game for which the number of discs in a row is counted.
     * @param x Already counted discs. Sign determines player.
     * @param y Player of which is the next disc in the row. Absolute value
     *     should be one. The sign determines the player.
     * @return x if it is > game.getWinLength(); x+y if both have the same sign
     * or one of x or y is zero, otherwise y.
     */
    protected int countInRow( LocalConnectFour game, int x, int y )
    {
        if( Math.abs( x ) >= game.getWinLength() )
            return x;
        if( (x == 0) || (y == 0) )
            return x + y;
        return ((x < 0) == (y < 0)) ? (x + y) : y;
    }
    
    /**
     * This function is used to sum the discs of a player in a column or
     * diagonal. It behaves like {@link LocalConnectFour#countInRow(int, int)
     * LocalConnectFour.countInRow()}.
     * @param game Game for which the number of discs in a row is counted.
     * @param x Already counted discs. Sign determines the player.
     * @param y Player of which is the next disc in the column or diagonal. The
     *     absolute value should be one and the sign determines the player.
     * @return x if it is > game.getWinLength(); 0 if y is zero.; x+y if both
     *     have the same sign, otherwise y.
     */
    protected int countInCols( LocalConnectFour game, int x, int y )
    {
        if( Math.abs( x ) >= game.getWinLength() )
            return x;
        if( y == 0 )
            return 0;
        return ((x < 0) == (y < 0)) ? (x + y) : y;
    }
    
    /**
     * Minmax algorithm with Alpha/Beta cut offs for finding the best move.
     * @param recdepth Recursion depth.
     * @param alpha Alpha value.
     * @param beta Beta value. No move with a rating above this would be chosen
     *     by the parent node of the search tree.
     * @param lowerWinRating If set to true the win rating will be divided by
     *     the number of possible moves which do not end the game. The value
     *     of this argument will be toggled in each recursion step.
     * @return An array with the length two. The first element is the rating
     *     for the best move found. The second is the column of this move.
     */
    protected int[] minmax( int recdepth, int alpha, int beta,
                            boolean lowerWinRating )
    {
        int ret[] = null;
        int bestMove = -1;

        /* Rate if the recursion base is reached. */
        if( recdepth <= 0 )
        {
            ret = new int[2];
            ret[0] = rate( getGameCopy() );
            if( getGameCopy().getCurrentPlayer() == Players.SECOND )
                ret[0] *= -1;
            return ret;
        }
        
        /* Search all possible moves. */
        int wouldEndGame = 0; // Number moves which would end the game.
                              // Used to improve the rating.
        for( int i = 0; i < getGameCopy().getColumns(); i++ )
        {
            if( !getGameCopy().doMove( i ) )
                continue;

            if( getGameCopy().isGameFinished() )
            {
                /* If game is finished rate directly. */
                ret = minmax( 0, -beta, -alpha, !lowerWinRating );
                wouldEndGame++;
            }
            else
                ret = minmax( recdepth - 1, -beta, -alpha, !lowerWinRating );
            getGameCopy().undo();
            
            if( ret == null )
                continue;
            ret[0] *= -1;
            ret[1] = i;
            
            if( ret[0] > beta && wouldEndGame == 0 )
            {

                return ret;
            }
            
            /* Because the AI should not always use the same moves, there is
             * a probability to replace the best move if the rating is equal.
             */
            if( (ret[0] > alpha && Math.random() < getDifficulty())
                || (ret[0] == alpha && (bestMove == -1
                                        || Math.random() < 0.3 ) ) )
            {
                alpha = ret[0];
                bestMove = ret[1];
            }
        }
        
        if( bestMove >= 0 )
        {
            if( ret == null )
                ret = new int[2];
            ret[0] = alpha;
            if( lowerWinRating && getRecDepth() - recdepth > 2
                && wouldEndGame > 0 )
                ret[0] /= game.getColumns() - wouldEndGame;
            ret[1] = bestMove;
        }
        return ret;
    }

    /**
     * Tells the AI to make a move.
     */
    public void move()
    {
        synchronized( getGame() )
        {
            setGameCopy( getGame().clone() );
        }
        
        int move[] = minmax( getRecDepth(), -getWinRating(), getWinRating(),
                             false );
        
        /* Ensure that we have a move. Nothing would be worse if the minmax
         * function fails, but there would be a possibility for a move and the
         * AI does not move. Of course the minmax function should always return
         * a move (if there is one possible), but it's better to be save.
         */
        if( move == null )
        {
            ArrayList<Integer> possibleMoves;
            synchronized( getGame() )
            {
                possibleMoves = new ArrayList<Integer>( getGame().getColumns() );
                for( int i = 0; i < getGame().getColumns(); i++ )
                {
                    if( getGame().isValidMove( i ) )
                        possibleMoves.add( i );
                }
            }
            
            move = new int[2];
            move[1] = possibleMoves.get( (int) (Math.random()
                                               * possibleMoves.size()) );
        }
        
        synchronized( getGame() )
        {
            getGame().doMove( move[1] );
        }
    }
    
    /**
     * Rates a sequence of disks in a Row.
     * @param freeBefore Number of free fields before the sequence.
     * @param inRow Number of discs in a row. The sign determines the player.
     * @param freeAfter Number of free fields after the sequence.
     * @return Rating for the row sequence.
     */
    public int  rateRowSequence( int freeBefore, int inRow, int freeAfter )
    {
        if( freeBefore + Math.abs( inRow ) + freeAfter < game.getWinLength() )
            return 0;
        if( inRow > game.getWinLength() )
            inRow = game.getWinLength();
        else if( inRow < game.getWinLength() )
            inRow = -game.getWinLength();
        int rating = inRow * inRow;
        if( freeBefore > 0 && freeAfter > 0 )
            rating *= 3;
        if( inRow < 0 )
            rating *= -1;
        return rating;
    }
    
    /**
     * Rating function. Maximizes for the first player. There are many parallels
     * to {@link LocalConnectFour#checkAndSetFinished()
     * LocalConnectFour.checkAndSetFinished()}.
     * @param game Game in which the current situation should be rated.
     * @return Rating for current game situation.
     */
    public int rate( LocalConnectFour game )
    {
        int rating = 0;
        
        if( game.isGameFinished() )
        {
            if( game.getWinner() == Players.FIRST )
                return getWinRating();
            else if( game.getWinner() == Players.SECOND )
                return -getWinRating();
            return 0;
        }
        
        final int possibleDiags = game.getColumns() + game.getRows()
                                  - 2 * (game.getWinLength()-1);

        /* For rating we have also to count the free fields and not only the 
         * number of discs in a row. Otherwise it could not be determined if
         * there is enough space to complete a row.
         */
        int freeBeforeInRow = 0;
        int inRow = 0;
        int freeAfterInRow = 0;
        int[] inColumns = new int[game.getColumns()];
        int[] freeInColumns = new int[game.getColumns()];
        /* Down/Up as viewed from left to right: */
        int[] inDiagsDown = new int[possibleDiags];
        int[] inDiagsUp   = new int[possibleDiags];
        int[] freeInDiagsDown = new int[possibleDiags];
        int[] freeInDiagsUp   = new int[possibleDiags];
        Arrays.fill( inColumns, 0 );
        Arrays.fill( freeInColumns, 0 );
        Arrays.fill( inDiagsDown, 0 );
        Arrays.fill( inDiagsUp, 0 );
        Arrays.fill( freeInDiagsDown, 0 );
        Arrays.fill( freeInDiagsUp, 0 );
        

        for( int i = 0; i < game.getRows(); i++ ) // Rows
        {
            freeBeforeInRow = inRow = freeAfterInRow = 0;
            for( int j = 0; j < game.getColumns(); j++ ) // Columns
            {
                final int idxDiagDown = j+i - (game.getWinLength()-1);
                final int idxDiagUp   = j+(game.getRows()-1-i)
                                        - (game.getWinLength()-1); 
                
                int occupiedBy; // Representing the player occupying the current
                    // field. Players.FIRST is 1, Players.SECOND is -1, and
                    // in all other cases (Players.UNDEFINED).
                switch( game.getField( i, j ) )
                {
                    case FIRST:  occupiedBy =  1; break;
                    case SECOND: occupiedBy = -1; break;
                    default:
                        occupiedBy =  0;
                        freeInColumns[j]++;
                        if( 0 <= idxDiagDown && idxDiagDown < possibleDiags )
                            freeInDiagsDown[idxDiagDown]++;
                        if( 0 <= idxDiagUp && idxDiagUp < possibleDiags )
                            freeInDiagsUp[idxDiagUp]++;
                        break;
                }

                /* Count in row */
                if( occupiedBy != 0 )
                {
                    if( freeAfterInRow > 0 )
                    {
                        rating += rateRowSequence( freeBeforeInRow, inRow,
                                                   freeAfterInRow );
                        freeBeforeInRow = freeAfterInRow;
                        inRow = freeAfterInRow = 0;    
                    }
                    else if( inRow != 0 && (inRow < 0) == (occupiedBy > 0) )
                    {
                        rating += rateRowSequence( freeBeforeInRow, inRow,
                                                   freeAfterInRow );
                        freeBeforeInRow = 0;
                        inRow = freeAfterInRow = 0;
                    }
                    
                    inRow = countInRow( game, inRow, occupiedBy );
                }
                else
                {
                    if( inRow == 0)
                        freeBeforeInRow++;
                    else
                        freeAfterInRow++;
                }
                
                /* Count in column and diagonals */
                inColumns[j] = countInRow( game, inColumns[j], occupiedBy );
                if( Math.abs( inColumns[j] ) + freeInColumns[j] + game.getRows()
                    - i < game.getWinLength() )
                    inColumns[j] = 0;
                if( 0 <= idxDiagDown && idxDiagDown < possibleDiags )
                {
                    inDiagsDown[idxDiagDown] = countInCols(
                        game, inDiagsDown[idxDiagDown], occupiedBy );
                    if( Math.abs( inDiagsDown[idxDiagDown] )
                        + freeInDiagsDown[idxDiagDown] + game.getRows() - i
                        < game.getWinLength() )
                        inDiagsDown[idxDiagDown] = 0;
                }
                if( 0 <= idxDiagUp && idxDiagUp < possibleDiags )
                {
                    inDiagsUp[idxDiagUp] = countInCols(
                        game, inDiagsUp[idxDiagUp], occupiedBy );
                    if( Math.abs( inDiagsUp[idxDiagUp] )
                        + freeInDiagsUp[idxDiagUp] + game.getRows() - i
                        < game.getWinLength() )
                        inDiagsUp[idxDiagUp] = 0;
                }
            }
            
            if( freeBeforeInRow < game.getColumns() )
                rating += rateRowSequence( freeBeforeInRow, inRow,
                                           freeAfterInRow );
        }
        
        /* Evaluate columns and diagonals. */
        for( int mode = 0; mode < 3; mode++ )
        {
            int[] array = null;
            switch( mode )
            {
                case 0: array = inColumns;   break;
                case 1: array = inDiagsDown; break;
                case 2: array = inDiagsUp;   break;
            }

            for( int i = 0; i < array.length; i++ )
            {
                if( array[i] >= 0 )
                    rating += array[i] * array[i];
                else
                    rating -= array[i] * array[i];
            }
        }
        
        return rating;
    }

    @Override
    public void run()
    {
        move();
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // Simple Getters & Setters
    ////////////////////////////////////////////////////////////////////////////
    public void setDifficulty( double difficulty )
    {
        this.difficulty = difficulty;
    }

    public double getDifficulty()
    {
        return difficulty;
    }
    
    public void setGame( LocalConnectFour game )
    {
        this.game = game;
    } 

    public LocalConnectFour getGame()
    {
        return game;
    }
    
    protected void setGameCopy( LocalConnectFour gameCopy )
    {
        this.gameCopy = gameCopy;
    }

    protected LocalConnectFour getGameCopy()
    {
        return gameCopy;
    }

    public void setRecDepth( int recDepth )
    {
        this.recDepth = recDepth;
    }

    public int getRecDepth()
    {
        return recDepth;
    }

    protected static int getWinRating()
    {
        return winRating;
    }
}
