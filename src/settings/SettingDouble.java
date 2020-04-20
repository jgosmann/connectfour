/* ConnectFour
 * File: SettingDouble.java
 * Creation: 28.06.2009 14:56:23
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
 * Setting that stores an double.
 */
public class SettingDouble extends SettingInPreferences
{
    ////////////////////////////////////////////////////////////////////////////
    // Member Variables
    ////////////////////////////////////////////////////////////////////////////
    /** Stored value. */
    private double value;
    /** Minimum value. */
    private double min;
    /** Maximum value. */
    private double max;
    
    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Shorthand for {@link #SettingDouble(String, double, double, double)
     * SettingDouble(key, def, Double.MIN_VALUE, Double.MAX_VALUE)}.
     */
    public SettingDouble( String key, double def )
    {
        this( key, def, Double.MIN_VALUE, Double.MAX_VALUE );
    }
    
    /**
     * Shorthand for {@link #SettingDouble(String, double, double, double, int)
     * SettingInt(key, def, min, max, 0)}.
     */
    public SettingDouble( String key, double def, double min, double max )
    {
        this( key, def, min, max, 0 );
    }
    
    /**
     * @param key key of setting.
     * @param def Default value of setting.
     * @param min Minimum value of setting.
     * @param max Maximum value of setting.
     * @param index Index for ordering;
     */
    public SettingDouble( String key, double def, double min, double max,
                          int index )
    {
        super( key, index );
        setMin( min );
        setMax( max );
        if( getPreferencesObj() != null )
          setValue( getPreferencesObj().getDouble( getKey(), def ) );
        else
          setValue( def );
    }

    ////////////////////////////////////////////////////////////////////////////
    // Static Methods
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Function to avoid casts.
     * @param settings Settings to search.
     * @param key Key to find.
     * @return Returns the the child with the key key in settings as
     *     SettingDouble.
     */
    public static SettingDouble get( Setting settings, String key )
    {
        return ((SettingDouble) settings.get( key ));
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // Methods
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void putToPreferences()
    {
        if( getPreferencesObj() != null )
            getPreferencesObj().putDouble( getKey(), this.value );
    }

    ////////////////////////////////////////////////////////////////////////////
    // Simple Getters & Setters
    ////////////////////////////////////////////////////////////////////////////
    public void setMax( double max )
    {
        this.max = max;
    }

    public double getMax()
    {
        return max;
    }

    public void setMin( double min )
    {
        this.min = min;
    }

    public double getMin()
    {
        return min;
    }

    /** @throws InvalidValueException if value < min or value > max. */
    public void setValue( double value )
    {
        if( value < getMin() || value > getMax() )
            throw new InvalidValueException( Double.toString( value ) );
        this.value = value;
        putToPreferences();
    }

    public double getValue()
    {
        if( getPreferencesObj() != null )
            value = getPreferencesObj().getDouble( getKey(), value );
        if( value < getMin() )
            value = getMin();
        if( value > getMax() )
            value = getMax();
        return value;
    }
}
