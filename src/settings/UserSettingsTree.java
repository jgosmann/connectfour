/* ConnectFour
 * File: UserSettingsTree.java
 * Creation: 27.06.2009 17:42:21
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

import java.util.prefs.Preferences;

/**
 * This class stores it's settings in the Java User Preferences object. This
 * class is a singleton. It will be created on the first request.
 */
public class UserSettingsTree extends SettingInPreferences
{
    ////////////////////////////////////////////////////////////////////////////
    // Static variables
    ////////////////////////////////////////////////////////////////////////////
    private static UserSettingsTree instance;
    
    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////
    private UserSettingsTree()
    {
        super( "UserSettings", 0, Preferences.userRoot() );
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // Static Methods
    ////////////////////////////////////////////////////////////////////////////
    public synchronized static UserSettingsTree getInstance()
    {
        if( instance == null )
            instance = new UserSettingsTree();
        return instance;
    }
}
