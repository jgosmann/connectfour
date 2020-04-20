/* ConnectFour
 * File: RemoteCfEvent.java
 * Creation: 08.07.2009 19:47:54
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

import java.util.EventObject;

/**
 * This event is emitted by {@link RemoteConnectFour RemoteConnectFour}
 * when special data was received or an error occurred.
 */
public class RemoteCfEvent extends EventObject
{
    private static final long serialVersionUID = 1L;

    ////////////////////////////////////////////////////////////////////////////
    // Enumerations
    ////////////////////////////////////////////////////////////////////////////
    /** Possible types of this event */
    public enum Types
    {
        GAME_CREATED,
        GAME_FINISHED,
        ERROR_OCCURED
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // Member Variables
    ////////////////////////////////////////////////////////////////////////////
    /** Type of the event. */
    private Types type;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////
    /**
     * @param source Game emitting the event.
     * @param type Type of the event. 
     */
    public RemoteCfEvent( RemoteConnectFour source, Types type )
    {
        super( source );
        setType( type );
    }

    ////////////////////////////////////////////////////////////////////////////
    // Simple Getters & Setters
    ////////////////////////////////////////////////////////////////////////////
    protected void setType( Types type )
    {
        this.type = type;
    }

    public Types getType()
    {
        return type;
    }
}
