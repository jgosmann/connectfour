/* ConnectFour
 * File: SettingString.java
 * Creation: 07.07.2009 14:41:26
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
 * Setting that stores a string.
 */
public class SettingString extends SettingInPreferences
{
    ////////////////////////////////////////////////////////////////////////////
    // Member Variables
    ////////////////////////////////////////////////////////////////////////////
    /** Stored value. */
    private String value;
    
    /** Default value. */
    private String defValue;
    
    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Shorthand for {@link #SettingString(String, String, int)
     * SettingString(key, defValue, 0)}.
     */
    public SettingString( String key, String defValue )
    {
        this( key, defValue, 0 );
    }
    
    /**
     * @param key key of setting.
     * @param defValue Default value of setting.
     * @param index Index for ordering.
     */
    public SettingString( String key, String defValue, int index )
    {
        super( key, index );
        setDefValue( defValue );
        if( getPreferencesObj() != null )
          setValue( getPreferencesObj().getByteArray(
                        getKey(), defValue.getBytes() ).toString() );
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
     *     SettingString.
     */
    public static SettingString get( Setting settings, String key )
    {
        return ((SettingString) settings.get( key ));
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // Methods
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void putToPreferences()
    {
        if( getPreferencesObj() != null )
            getPreferencesObj().putByteArray( getKey(), this.value.getBytes() );
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
    public void setDefValue( String defValue )
    {
        this.defValue = defValue;
    }
    
    public String getDefValue()
    {
        return defValue;
    }
    
    public void setValue( String value )
    {
        this.value = value;
        putToPreferences();
    }
    
    public String getValue()
    {
        if( getPreferencesObj() != null )
            value = new String( getPreferencesObj().getByteArray( getKey(),
                this.value.getBytes() ) );
        return value;
    }
}
