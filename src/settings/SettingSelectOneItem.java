/* ConnectFour
 * File: SettingSelectOneItem.java
 * Creation: 28.06.2009 15:24:03
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
 * One boolean option of a group of which only one can be selected.
 */
public class SettingSelectOneItem extends SettingBoolean
{
    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Shorthand for {@link #SettingSelectOneItem(String, boolean, int)
     * SettingSelectOneItem(key, def, 0)}.
     */
    public SettingSelectOneItem( String key, boolean def )
    {
        this( key, def, 0 );
    }
    /**
     * @param key key of setting.
     * @param def Default value of setting.
     * @param index Index for ordering
     */
    public SettingSelectOneItem( String key, boolean def, int index )
    {
        super( key, def, index );
    }
}
