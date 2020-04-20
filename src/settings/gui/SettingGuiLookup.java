/* ConnectFour
 * File: SettingGuiLookup.java
 * Creation: 28.06.2009 11:04:22
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

import java.util.Hashtable;

import settings.Setting;

/**
 * Singleton which provides the lookup table in which is stored which class
 * inheriting {@link JSetting JSetting} is used to display which class
 * inheriting {@link Setting Setting}.
 */
public class SettingGuiLookup
{
    ////////////////////////////////////////////////////////////////////////////
    // Member Variables
    ////////////////////////////////////////////////////////////////////////////
    private static SettingGuiLookup instance = null;
    
    /** The lookup table. */
    private Hashtable<Class<? extends Setting>, Class<? extends JSetting>>
        lookup
        = new Hashtable<Class<? extends Setting>, Class<? extends JSetting>>();
    
    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////
    private SettingGuiLookup()
    {
        
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // Methods
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Adds an element to the lookup table.
     * @param key The Setting derived class you want to add the JSetting derived
     *     class for.
     * @param value The JSetting derived class displaying the Setting derived
     *     class.
     */
    public void add( Class<? extends Setting> key,
                        Class<? extends JSetting> value )
    {
        lookup.put( key, value );
    }
    
    /**
     * Looks up which JSetting derived class displays the passed Setting derived
     * class.
     * @param key Class to look up.
     * @return Class displaying the looked up class.
     */
    public Class<? extends JSetting> lookup( Class<? extends Setting> key )
    {
        return lookup.get( key );
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // Simple Getters & Setters
    ////////////////////////////////////////////////////////////////////////////
    public static SettingGuiLookup getInstance()
    {
        if( instance == null )
            instance = new SettingGuiLookup();
        return instance;
    }
}
