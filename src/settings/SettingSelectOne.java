/* ConnectFour
 * File: SettingSelectOne.java
 * Creation: 28.06.2009 15:23:00
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
 * Groups {@link SettingSelectOneItem SettingSelectOneItem} and indicates that
 * only one of these should be active.
 */
public class SettingSelectOne extends Setting
{
    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////
    public SettingSelectOne( String key )
    {
        this( key, 0 );
    }
    
    public SettingSelectOne( String key, int index )
    {
        super( key, index );
    }
}
