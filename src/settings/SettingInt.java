/* ConnectFour
 * File: SettingInt.java
 * Creation: 27.06.2009 18:21:44
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
 * Setting that stores an integer.
 */
public class SettingInt extends SettingInPreferences
{
    ////////////////////////////////////////////////////////////////////////////
    // Member Variables
    ////////////////////////////////////////////////////////////////////////////
    /** Stored value. */
    private int value;
    /** Minimum value. */
    private int min;
    /** Maximum value. */
    private int max;
    
    /** Default value. */
    private int defValue;
    
    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Shorthand for {@link #SettingInt(String, int, int, int) SettingInt(key,
     * defValue, Integer.MIN_VALUE, Integer.MAX_VALUE)}.
     */
    public SettingInt( String key, int defValue )
    {
        this( key, defValue, Integer.MIN_VALUE, Integer.MAX_VALUE );
    }
    
    /**
     * Shorthand for {@link #SettingInt(String, int, int, int, int)
     * SettingInt(key, defValue, min, max, 0)}.
     */
    public SettingInt( String key, int defValue, int min, int max )
    {
        this( key, defValue, min, max, 0 );
    }
    
    /**
     * @param key key of setting.
     * @param defValue Default value of setting.
     * @param min Minimum value of setting.
     * @param max Maximum value of setting.
     * @param index Index for ordering;
     */
    public SettingInt( String key, int defValue, int min, int max, int index )
    {
        super( key, index );
        setMin( min );
        setMax( max );
        setDefValue( defValue );
        if( getPreferencesObj() != null )
          setValue( getPreferencesObj().getInt( getKey(), defValue ) );
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
     *     SettingInt.
     */
    public static SettingInt get( Setting settings, String key )
    {
        return ((SettingInt) settings.get( key ));
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // Methods
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void putToPreferences()
    {
        if( getPreferencesObj() != null )
            getPreferencesObj().putInt( getKey(), this.value );
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
    public void setDefValue( int defValue )
    {
        this.defValue = defValue;
    }
    
    public int getDefValue()
    {
        return defValue;
    }
    
    public void setMax( int max )
    {
        this.max = max;
    }

    public int getMax()
    {
        return max;
    }

    public void setMin( int min )
    {
        this.min = min;
    }

    public int getMin()
    {
        return min;
    }

    /** @throws InvalidValueException if value < min or value > max. */
    public void setValue( int value )
    {
        if( value < getMin() || value > getMax() )
            throw new InvalidValueException( Integer.toString( value ) );
        this.value = value;
        putToPreferences();
    }

    public int getValue()
    {
        if( getPreferencesObj() != null )
            value = getPreferencesObj().getInt( getKey(), value );
        if( value < getMin() )
            value = getMin();
        if( value > getMax() )
            value = getMax();
        return value;
    }
}
