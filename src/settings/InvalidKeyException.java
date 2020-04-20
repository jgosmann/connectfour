/* ConnectFour
 * File: InvalidKeyException.java
 * Creation: 27.06.2009 17:05:25
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

package settings;

/**
 * Indicates that an invalid key was used in an operation of a {@link Setting
 * Setting} or {@link Setting SettingsTree} object.
 */
public class InvalidKeyException extends RuntimeException
{
    ////////////////////////////////////////////////////////////////////////////
    // Static Variables.
    ////////////////////////////////////////////////////////////////////////////
    private static final long serialVersionUID = 1L;
    
    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Shorthand for {@link #InvalidKeyException(String)
     * InvalidKeyException("")}.
     */
    public InvalidKeyException()
    {
        this( "" );
    }
    
    /** @param key The invalid key. */
    public InvalidKeyException( String key )
    {
        super( key );
    }
}
