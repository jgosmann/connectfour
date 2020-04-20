/* ConnectFour
 * File: Communicator.java
 * Creation: 08.07.2009 21:37:24
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
import java.nio.charset.Charset;

/**
 * Basic class for a "communicator" a class managing network transmission for
 * the ConnectFour network protocol. The communicators used for the
 * implementation of those protocol classes can derive from this class.
 */
public class Communicator
{
    ////////////////////////////////////////////////////////////////////////////
    // Member Variables
    ////////////////////////////////////////////////////////////////////////////
    /** Connection used for transmitting and receiving. */
    private Socket connection = null;
    
    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////
    public Communicator()
    {
    }
    
    public Communicator( Socket connection )
    {
        setConnection( connection );
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // Methods
    ////////////////////////////////////////////////////////////////////////////
    private String readBuffer = "";
    /**
     * Reads the next line, but a maximum of 256 bytes.
     * @return Returns the read string without the newline character.
     */
    public String read() throws IOException
    {
        while( !readBuffer.matches( "(?ds).*\\n.*" )
               && readBuffer.length() < 256
               && !getConnection().isClosed() )
        {
            byte[] byteBuffer = new byte[256];
            int nRead = getConnection().getInputStream().read( byteBuffer );
            if( nRead > 0 )
                readBuffer += new String( byteBuffer, 0, nRead,
                                          Charset.forName( "UTF-8" ) );
        }

        String[] splitted = readBuffer.split( "(?ds)\\n", 2 );
        if( splitted.length == 2 )
        {
            readBuffer = splitted[1];
            return splitted[0];
        }
        String ret = readBuffer.substring( 0, 256 );
        readBuffer = readBuffer.substring( 256 );
        return ret;
    }
    
    /**
     * Sends the string with a newline at the end.
     * @param msg String to send.
     */
    public void send( String msg ) throws IOException
    {
        msg += "\n";
        getConnection().getOutputStream().write( msg.getBytes(
            Charset.forName( "UTF-8" ) ) );
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // Simple Getters & Setters
    ////////////////////////////////////////////////////////////////////////////
    public void setConnection( Socket connection )
    {
        this.connection = connection;
    }

    public Socket getConnection()
    {
        return connection;
    }
}
