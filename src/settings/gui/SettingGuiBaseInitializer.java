/* ConnectFour
 * File: SettingGuiBaseInitializer.java
 * Creation: 28.06.2009 12:17:20
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

package settings.gui;

import settings.SettingBoolean;
import settings.SettingColor;
import settings.SettingDouble;
import settings.SettingFilename;
import settings.SettingInt;
import settings.SettingSelectOne;
import settings.SettingSelectOneItem;
import settings.SettingString;

/**
 * Contains a function used to initialize the lookup table of {@link
 * SettingGuiLookup SettingGuiLookup} with the base {@link JSetting JSetting}
 * elements.
 * For own sets  of classes derived from JSetting used additional to the base
 * elements you may want to derive this class and overwrite the {@link
 * #initialize() initialize()} function.
 */
public class SettingGuiBaseInitializer extends SettingGuiEmptyInitializer
{
    ////////////////////////////////////////////////////////////////////////////
    // Methods
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Initializes the lookup table of {@link SettingGuiLookup SettingGuiLookup}
     * with the base {@link JSetting JSetting} elements . For own sets  of
     * classes derived from JSetting used additional to the base elements you
     * may want to derive this class and overwrite the {@link #initialize()
     * initialize()} function. However you should call always the initialize
     * function from this class or of a superclass first.
     */
    public static void initialize()
    {
        SettingGuiEmptyInitializer.initialize();
        SettingGuiLookup l = SettingGuiLookup.getInstance();
        l.add( SettingBoolean.class, JSettingBoolean.class );
        l.add( SettingColor.class, JSettingColor.class );
        l.add( SettingDouble.class, JSettingDouble.class );
        l.add( SettingFilename.class, JSettingFilename.class );
        l.add( SettingInt.class, JSettingInt.class );
        l.add( SettingSelectOne.class, JSettingSelectOne.class );
        l.add( SettingSelectOneItem.class, JSettingSelectOneItem.class );
        l.add( SettingString.class, JSettingString.class );
    }
}
