/* ConnectFour
 * File: SettingGuiInitializer.java
 * Creation: 28.06.2009 12:14:11
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

import settings.Setting;

/**
 * Contains a function used to intialize the lookup table of {@link
 * SettingGuiLookup SettingGuiLookup}. It just adds {@link JSetting JSetting}.
 * For own sets of classes derived from JSetting you may want to derive this
 * class and overwrite the {@link #initialize() initialize()} function.
 */
public class SettingGuiEmptyInitializer
{
    ////////////////////////////////////////////////////////////////////////////
    // Methods
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Initializes the lookup table of {@link SettingGuiLookup SettingGuiLookup}.
     * It just adds {@link JSetting JSetting}. For own sets of classes derived
     * from JSetting you may want to derive this class and overwrite the
     * {@link #initialize() initialize()} function. However you should call
     * always the initialize function from this class first.
     * If you use no own classes derived from JSetting the {@link
     * SettingGuiBaseInitializer#initialize()
     * SettingGuiBaseInitializer.initialize()} function probably suits you more.
     */
    public static void initialize()
    {
        SettingGuiLookup.getInstance().add( Setting.class, JSetting.class );
    }
}
