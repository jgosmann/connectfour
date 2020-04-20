/* ConnectFour
 * File: SettingBoolean.java
 * Creation: 28.06.2009 15:29:39
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
 * Setting that stores a boolean.
 */
public class SettingBoolean extends SettingInPreferences
{
    ////////////////////////////////////////////////////////////////////////////
    // Member Variables
    ////////////////////////////////////////////////////////////////////////////
    /** Stored value. */
    private boolean value;
    
    /** Default value. */
    private boolean defValue;
    
    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Shorthand for {@link #SettingBoolean(String, boolean, int)
     * SettingBoolean(key, defValue, 0)}.
     */
    public SettingBoolean( String key, boolean defValue )
    {
        this( key, defValue, 0 );
    }
    
    /**
     * @param key key of setting.
     * @param defValue Default value of setting.
     * @param index Index for ordering.
     */
    public SettingBoolean( String key, boolean defValue, int index )
    {
        super( key, index );
        setDefValue( defValue );
        if( getPreferencesObj() != null )
          setValue( getPreferencesObj().getBoolean( getKey(), defValue ) );
        else
          setValue( defValue );
    }

    ////////////////////////////////////////////////////////////////////////////
    // Static Methods
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Function to avoid casts.
     * @param settings Settings to search.
     * @param key Key to find.
     * @return Returns the the child with the key key in settings as
     *     SettingBoolean.
     */
    public static SettingBoolean get( Setting settings, String key )
    {
        return ((SettingBoolean) settings.get( key ));
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // Methods
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void putToPreferences()
    {
        if( getPreferencesObj() != null )
            getPreferencesObj().putBoolean( getKey(), this.value );
    }
    
    @Override
    public void restoreDefaults()
    {
        setValue( getDefValue() );
        super.restoreDefaults();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Simple Getters & Setters
    ////////////////////////////////////////////////////////////////////////////
    public void setDefValue( boolean defValue )
    {
        this.defValue = defValue;
    }

    public boolean getDefValue()
    {
        return defValue;
    }
    
    public void setValue( boolean value )
    {
        this.value = value;
        putToPreferences();
    }

    public boolean getValue()
    {
        if( getPreferencesObj() != null )
            value = getPreferencesObj().getBoolean(getKey(), this.value );
        return value;
    }
}
