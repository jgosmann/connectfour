/* ConnectFour
 * File: RemoteConnectFour.java
 * Creation: 08.07.2009 16:48:35
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

import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;

/**
 * Class for a remote Connect Four game over a socket network connection.
 */
public class RemoteConnectFour implements IConnectFourEventEmitting
{
    /** Error types */
    public enum Errors
    {
        NO_ERROR,
        INVALID_DATA,
        IO_ERROR,
        INVALID_PASSWORD,
        INVALID_MOVE,
        WRONG_PLAYER
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // Member Variables
    ////////////////////////////////////////////////////////////////////////////
    /** Local copy of the game. */
    private LocalConnectFourEventEmitting game = null;
    final private Object gameMutex = new Object();
    
    /** Thread for communication over the socket. */
    final private ClientCommunicator communicator = new ClientCommunicator( this );
    
    /** Last error occurred. */
    private Errors error = Errors.NO_ERROR;
    final private Object errorMutex = new Object();
    
    LinkedList<RemoteCfListener> remoteCfEventListener =
        new LinkedList<RemoteCfListener>();

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////
    /**
     * @param host Host which is hosting the game.
     * @param port Port to connect to at host.
     * @param password Password of game.
     */
    public RemoteConnectFour( String host, int port, String password )
        throws IOException
    {
        getCommunicator().setConnection( new Socket( host, port ) );
        getCommunicator().send( password );
        new Thread( getCommunicator() ).start();
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // Methods
    ////////////////////////////////////////////////////////////////////////////
    /** Class implementing the thread with the communication methods. */
    protected class ClientCommunicator extends Communicator implements Runnable
    { 
        private RemoteConnectFour enclosingClass;
        
        public ClientCommunicator( RemoteConnectFour enclosingClass )
        {
            this.enclosingClass = enclosingClass;
        }
        
        /** Main cycle: Wait for input and process it. */
        @Override
        public void run()
        {
            try
            {
                while( !getConnection().isClosed() )
                {
                    String msg = read();
                    switch( msg.charAt( 0 ) )
                    {
                        case 'G': // create Game
                            String splitted[] = msg.split( " " );
                            if( splitted.length != 6 )
                            {
                                setError( Errors.INVALID_DATA );
                                return;
                            }
                            int[] params = new int[5];
                            for( int i = 0; i < 5; i++ )
                                params[i] = Integer.parseInt( splitted[i+1] );
                            synchronized( getGameMutex() )
                            {
                                setGame( new LocalConnectFourEventEmitting(
                                    params[0], params[1], params[2],
                                    IConnectFour.Players.values()[params[3]] ) );
                                getGame().setMaxTurns( params[4] );
                            }
                            dispatchRemoteCfEvent( new RemoteCfEvent(
                                enclosingClass, RemoteCfEvent.Types.GAME_CREATED ) );
                            break;
                            
                        case 'I': // Invalid move
                            setError( Errors.INVALID_MOVE );
                            break;
                            
                        case 'M': // Move
                            int column = Integer.parseInt( msg.substring( 1 ) );
                            synchronized( getGameMutex() )
                            {
                                if( getGame().getCurrentPlayer() !=
                                    IConnectFour.Players.SECOND )
                                {
                                    setError( Errors.WRONG_PLAYER );
                                    break;
                                }
                                boolean success = getGame().doMove( column );
                                if( !success )
                                {
                                    setError( Errors.INVALID_DATA );
                                    return;
                                }
                                if( getGame().isGameFinished() )
                                    return;
                            }
                            break;
                            
                        case 'P': // invalid Password
                            setError( Errors.INVALID_PASSWORD );
                            return;
                            
                        default:
                            setError( Errors.INVALID_DATA );
                            return;
                    }
                }
            }
            catch( IOException e )
            {
                setError( Errors.IO_ERROR );
            }
            catch( Exception e )
            {
                setError( Errors.INVALID_DATA );
            }
            finally
            {
                try
                {
                    getConnection().close();
                }
                catch( IOException e )
                {
                }
            }
        }
    }; 
    
    public void dispatchRemoteCfEvent( RemoteCfEvent e )
    {
        for( RemoteCfListener listener : remoteCfEventListener )
            listener.eventOccurred( e );
    }
    
    @Override
    public void addMoveDoneListener( MoveDoneListener listener )
    {
        getGame().addMoveDoneListener( listener );
    }
    
    public void addRemoteCfListener( RemoteCfListener listener )
    {
        remoteCfEventListener.add( listener );
    }

    @Override
    public void removeMoveDoneListener( MoveDoneListener listener )
    {
        getGame().addMoveDoneListener( listener );
    }
    
    public void removeRemoteCfListener( RemoteCfListener listener )
    {
        remoteCfEventListener.remove( listener );
    }
    
    @Override
    public boolean doMove( int column )
    {
        synchronized( getGameMutex() )
        {
            if( getGame().isValidMove( column ) )
            {
                getGame().doMove( column );
                try
                {
                    getCommunicator().send( "M" + column );
                    if( getGame().isGameFinished() )
                        getCommunicator().getConnection().close();
                    return true;
                }
                catch( IOException e )
                {
                    setError( Errors.IO_ERROR );
                    return false;
                }
            }
        }
        return false;
    }

    @Override
    public int[] undo()
    {
        return null;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Simple Getters & Setters
    ////////////////////////////////////////////////////////////////////////////
    protected ClientCommunicator getCommunicator()
    {
        return communicator;
    }
    
    protected void setError( Errors error )
    {
        synchronized( getErrorMutex() )
        {
            this.error = error;
        }
        dispatchRemoteCfEvent( new RemoteCfEvent( this,
            RemoteCfEvent.Types.ERROR_OCCURED ) );
    }

    public Errors getError()
    {
        synchronized( getErrorMutex() )
        {
            return error;
        }
    }

    protected Object getErrorMutex()
    {
        return errorMutex;
    }

    protected void setGame( LocalConnectFourEventEmitting game )
    {
        this.game = game;
    }

    protected LocalConnectFourEventEmitting getGame()
    {
        return game;
    }

    protected Object getGameMutex()
    {
        return gameMutex;
    }
    
    @Override
    public int getColumns()
    {
        synchronized( getGameMutex() )
        {
            if( getGame() == null )
                return 0;
            return getGame().getColumns();   
        }
    }

    @Override
    public Players getCurrentPlayer()
    {
        synchronized( getGameMutex() )
        {
            if( getGame() == null )
                return null;
            return getGame().getCurrentPlayer();   
        }
    }

    @Override
    public Players getField( int row, int column )
        throws IndexOutOfBoundsException
    {
        synchronized( getGameMutex() )
        {
            if( getGame() == null )
                return IConnectFour.Players.UNDEFINED;
            return getGame().getField( row, column );   
        }
    }

    @Override
    public int getMaxTurns()
    {
        synchronized( getGameMutex() )
        {
            if( getGame() == null )
                return 0;
            return getGame().getMaxTurns();   
        }
    }

    @Override
    public Players getNextPlayer()
    {
        synchronized( getGameMutex() )
        {
            if( getGame() == null )
                return IConnectFour.Players.UNDEFINED;
            return getGame().getNextPlayer();   
        }
    }

    @Override
    public int getRows()
    {
        synchronized( getGameMutex() )
        {
            if( getGame() == null )
                return 0;
            return getGame().getRows();   
        }
    }

    @Override
    public int getWinEndColumn()
    {
        synchronized( getGameMutex() )
        {
            if( getGame() == null )
                return 0;
            return getGame().getWinEndColumn();   
        }
    }

    @Override
    public int getWinEndRow()
    {
        synchronized( getGameMutex() )
        {
            if( getGame() == null )
                return 0;
            return getGame().getWinEndRow();   
        }
    }

    @Override
    public int getWinLength()
    {
        synchronized( getGameMutex() )
        {
            if( getGame() == null )
                return 0;
            return getGame().getWinLength();   
        }
    }

    @Override
    public int getWinStartColumn()
    {
        synchronized( getGameMutex() )
        {
            if( getGame() == null )
                return 0;
            return getGame().getWinStartColumn();   
        }
    }

    @Override
    public int getWinStartRow()
    {
        synchronized( getGameMutex() )
        {
            if( getGame() == null )
                return 0;
            return getGame().getWinStartRow();   
        }
    }

    @Override
    public Players getWinner()
    {
        synchronized( getGameMutex() )
        {
            if( getGame() == null )
                return null;
            return getGame().getWinner();   
        }
    }

    @Override
    public boolean isGameFinished()
    {
        synchronized( getGameMutex() )
        {
            if( getGame() == null )
                return false;
            return getGame().isGameFinished();   
        }
    }

    @Override
    public boolean isUndoPossible()
    {
        return false;
    }

    @Override
    public boolean isValidMove( int column )
    {
        synchronized( getGameMutex() )
        {
            if( getGame() == null )
                return false;
            return getGame().isValidMove( column );   
        }
    }
}
