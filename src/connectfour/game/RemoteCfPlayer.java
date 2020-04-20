/* ConnectFour
 * File: RemoteCfPlayer.java
 * Creation: 08.07.2009 21:28:41
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
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.LinkedList;

/**
 * Listens at a port for a remote player (at the remote side is the {@link
 * RemoteConnectFour RemoteConnectFour} class used) at executes the transmitted
 * moves when the class is ordered so via a function.
 */
public class RemoteCfPlayer
{
    ////////////////////////////////////////////////////////////////////////////
    // Subclasses
    ////////////////////////////////////////////////////////////////////////////
    /** Callback class */
    public interface Callback
    {
        public void connectionEstablished( RemoteCfPlayer source );
        public void connectionClosed( RemoteCfPlayer source );
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // Member Variables
    ////////////////////////////////////////////////////////////////////////////
    /** The game in which the moves are executed. */
    private LocalConnectFour game;
    private Object gameMutex;
    
    /** Password to establish connection. */
    private String password;
    
    /**
     * Callback, called when connection has been established or has been
     * closed.
     */
    private Callback callback = null;
    
    /** The communicator for network transmission. */
    private ServerCommunicator communicator = new ServerCommunicator( this );
    
    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////
    public RemoteCfPlayer( LocalConnectFour game, Object gameMutex, int port,
                           String password )
       throws IOException
    {
        setPassword( password );
        setGame( game );
        setGameMutex( gameMutex );
        getCommunicator().setListenSocket( new ServerSocket( port ) );
        new Thread( getCommunicator() ).start();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods
    ////////////////////////////////////////////////////////////////////////////
    /** Class implementing the thread with the communication methods. */
    protected class ServerCommunicator extends Communicator implements Runnable
    { 
        private ServerSocket listenSocket;
        private RemoteCfPlayer enclosingClass;
        
        public ServerCommunicator( RemoteCfPlayer enclosingClass )
        {
            this.enclosingClass = enclosingClass;
        }
        
        public ServerCommunicator( RemoteCfPlayer enclosingClass,
                                   ServerSocket listenSocket )
        {
            this.enclosingClass = enclosingClass;
            setListenSocket( listenSocket );    
        }
        
        /** Main cycle: Wait for input and process it. */
        @Override
        public void run()
        {
            try
            {
                final LinkedList<Socket> connections = new LinkedList<Socket>();
                
                /* Thread for accepting connections */
                Thread connectionAccepter = new Thread() {
                     @Override
                     public void run()
                     {
                         while( !listenSocket.isClosed() )
                         {
                             Socket socket = null;
                             try
                             {
                                 socket = listenSocket.accept();
                             }
                             catch( Exception e )
                             {
                             }
                             if( socket != null )
                             {
                                 synchronized( connections )
                                 {
                                     connections.add( socket );   
                                 }
                             }
                         }
                     }
                };

                connectionAccepter.start();
                
                /* Wait for a connection with correct password */
                while( getConnection() == null )
                {
                    try
                    {
                        Thread.sleep( 1000 );
                    }
                    catch( InterruptedException e )
                    {
                    }
                    
                    synchronized( connections )
                    {
                        LinkedList<Socket> toRemove = new LinkedList<Socket>();
                        for( Socket socket : connections )
                        {
                            if( socket.isClosed()
                                || socket.getInputStream().available() <= 0 )
                                continue;
                            byte[] buffer = new byte[
                                getPassword().getBytes( Charset.forName( 
                                "UTF-8" ) ).length];
                            socket.getInputStream().read( buffer );
                            String readPw = new String( buffer,
                                Charset.forName( "UTF-8" ) );
                            if( readPw.equals( getPassword() ) )
                            {
                                listenSocket.close();
                                setConnection( socket );
                                break;
                            }
                            else
                                toRemove.add( socket );
                        }

                        /* Some cleanup to prevent DOS attacks */
                        for( Socket socket : toRemove )
                        {
                            socket.getOutputStream().write( "P\n".getBytes() );
                            socket.close();
                        }
                        connections.remove( toRemove );
                        while( connections.size() > 16 )
                            connections.removeFirst();
                    }
                }

                /* Connection established, send data */
                synchronized( getGameMutex() )
                {
                    String msg = "G";
                    msg += " " + getGame().getRows();
                    msg += " " + getGame().getColumns();
                    msg += " " + getGame().getWinLength();
                    /* Client and server act as second player, therefore the
                     * players have to be swapped for one side.
                     */
                    msg += " " + getGame().getNextPlayer().ordinal();
                    msg += " " + getGame().getMaxTurns();
                    send( msg );
                }
                
                getCallback().connectionEstablished( enclosingClass );
                
                while( !getConnection().isClosed() )
                {
                    String msg = read();
                    if( msg.length() <= 0 )
                        continue;
                    switch( msg.charAt( 0 ) )
                    {                            
                        case 'M': // Move
                            int column = Integer.parseInt( msg.substring( 1 ) );
                            synchronized( getGameMutex() )
                            {
                                if( getGame().getCurrentPlayer() !=
                                    IConnectFour.Players.SECOND )
                                {
                                    send( "I" );
                                    break;
                                }
                                boolean success = getGame().doMove( column );
                                if( !success )
                                {
                                    send( "I" );
                                    break;
                                }
                                if( getGame().isGameFinished() )
                                    return;
                            }
                            break;
                            
                        default:
                            send( "I" );
                            return;
                    }
                }
            }
            catch( IOException e )
            {
            }
            finally
            {
                try
                {
                    getConnection().close();
                    getCallback().connectionClosed( enclosingClass );
                }
                catch( IOException e )
                {
                }
            }
        }

        public void setListenSocket( ServerSocket listenSocket )
        {
            this.listenSocket = listenSocket;
        }

        public ServerSocket getListenSocket()
        {
            return listenSocket;
        }      
    };
    
    /** Transmits the last move. Does nothing if move list is empty */
    public void sendLastMove() throws IOException
    {
        synchronized( getGameMutex() )
        {
            if( getGame().getMoves().size() > 0 )
            {
                getCommunicator().send( "M" + getGame().getMoves().peek() );
                if( getGame().isGameFinished() )
                {
                    getCommunicator().getConnection().close();
                }
            }
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // Simple Getters & Setters
    ////////////////////////////////////////////////////////////////////////////
    public void setCommunicator( ServerCommunicator communicator )
    {
        this.communicator = communicator;
    }

    public ServerCommunicator getCommunicator()
    {
        return communicator;
    }
    
    public void setCallback( Callback callback )
    {
        this.callback = callback;
    }

    public Callback getCallback()
    {
        return callback;
    }

    public void setGame( LocalConnectFour game )
    {
        this.game = game;
    }
    
    public LocalConnectFour getGame()
    {
        return game;
    }

    public void setGameMutex( Object gameMutex )
    {
        this.gameMutex = gameMutex;
    }

    public Object getGameMutex()
    {
        return gameMutex;
    }

    public void setPassword( String password )
    {
        this.password = password;
    }

    public String getPassword()
    {
        return password;
    }
}
