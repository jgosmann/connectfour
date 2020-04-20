/* ConnectFour
 * File: SettingFilename.java
 * Creation: 28.06.2009 15:00:11
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
 * Setting that stores a filename.
 */
public class SettingFilename extends SettingString
{
    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Shorthand for {@link #SettingFilename(String, String, int)
     * SettingFilename(key, defValue, 0)}.
     */
    public SettingFilename( String key, String defValue )
    {
        this( key, defValue, 0 );
    }
    
    /**
     * @param key key of setting.
     * @param defValue Default value of setting.
     * @param index Index for ordering.
     */
    public SettingFilename( String key, String defValue, int index )
    {
        super( key, defValue, index );
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // Static Methods
    ////////////////////////////////////////////////////////////////////////////
    public static SettingFilename get( Setting settings, String key )
    {
        return ((SettingFilename) settings.get( key ));
    }
}
