/* ConnectFour
 * File: SettingsTreeToPreferences.java
 * Creation: 27.06.2009 17:45:30
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
 * This setting stores and reads its settings values from a Preferences
 * object. Note that all settings which do not derived of this class will not be
 * stored in the PreferencesObject.
 */
public class SettingInPreferences extends Setting
{
    ////////////////////////////////////////////////////////////////////////////
    // Member Variables
    ////////////////////////////////////////////////////////////////////////////
    /** Preferences object used to store the data. */
    private Preferences preferencesObj;
    
    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Shorthand for {@link #SettingInPreferences(String, int)
     * SettingInPreferences(key, 0)}.
     */
    public SettingInPreferences( String key )
    {
        this( key, 0 );
    }
    
    /**
     * Shorthand for {@link #SettingInPreferences(String, int, Preferences)
     * SettingInPreferences(key, index, null)}. 
     */
    public SettingInPreferences( String key, int index )
    {
        this( key, index, null );
    }
    
    /**
     * @param key Key to access this setting.
     * @param index Index for ordering.
     * @param preferencesObj Preferences object to store data.
     */
    public SettingInPreferences( String key, int index,
                                 Preferences preferencesObj )
    {
        super( key, index );
        this.preferencesObj = preferencesObj;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // Methods
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Adds a new node to the SettingsTree. {@link Setting#key node.key} is used
     * as key. The {@link SettingInPreferences#preferencesObj preferencesObj}
     * of the node will be set to the same of the executing class.
     * @param node Node to add.
     * @throws InvalidKeyException if key is invalid.
     */
    public void add( SettingInPreferences node )
    {
        node.setPreferencesObj( getPreferencesObj() );
        add( "", node.getKey(), node );
    }
    
    /**
     * When this function is called a inherited class should store its
     * data in the passed preferences object. For this it should use its
     * {@link Setting#key key}.
     * The class should call this function itself, when data stored by this
     * function is changed.
     */
    public void putToPreferences()
    {
        // This object does not have any data to store.
    }
    
    @Override
    protected void removeChild( String key )
    {
        Setting node = getChild( key );
        if( node != null && node instanceof SettingInPreferences )
        {
            getPreferencesObj().remove( node.getKey() );
        }
        super.removeChild( key );
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // Simple Getters & Setters
    ////////////////////////////////////////////////////////////////////////////
    public void setPreferencesObj( Preferences preferencesObj )
    {
        this.preferencesObj = preferencesObj;
    }
    
    public Preferences getPreferencesObj()
    {
        return preferencesObj;
    }
}
