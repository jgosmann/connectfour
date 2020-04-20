/* ConnectFour
 * File: SettingColor.java
 * Creation: 28.06.2009 15:16:17
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

import java.awt.Color;

/**
 * Setting that stores a color.
 */
public class SettingColor extends SettingInPreferences
{
    ////////////////////////////////////////////////////////////////////////////
    // Member Variables
    ////////////////////////////////////////////////////////////////////////////
    /** Stored value. Each channel is stored separately. */
    private SettingInt red, green, blue, alpha;
    
    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Shorthand for {@link #SettingColor(String, Color, int) SettingColor(key,
     * defValue, 0)}.
     */
    public SettingColor( String key, Color defValue )
    {
        this( key, defValue, 0 );
    }
    
    /**
     * @param key key of setting.
     * @param defValue Default value of setting.
     * @param index Index for ordering.
     */
    public SettingColor( String key, Color defValue, int index )
    {
        super( key, index );
        red = new SettingInt( key + "/red", 255 );
        green = new SettingInt( key + "/green", 255 );
        blue = new SettingInt( key + "/blue", 255 );
        alpha = new SettingInt( key + "/alpha", 255 );
        setDefValue( defValue );
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
     *     SettingColor.
     */
    public static SettingColor get( Setting settings, String key )
    {
        return ((SettingColor) settings.get( key ));
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // Methods
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void putToPreferences()
    {

    }
    
    @Override
    public void restoreDefaults()
    {
        red.restoreDefaults();
        green.restoreDefaults();
        blue.restoreDefaults();
        alpha.restoreDefaults();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Simple Getters & Setters
    ////////////////////////////////////////////////////////////////////////////
    public void setDefValue( Color defValue )
    {
        setDefValue( defValue.getRed(), defValue.getGreen(), defValue.getBlue(),
                     defValue.getAlpha() );
    }
    
    public void setDefValue( int r, int g, int b, int a )
    {
        this.red.setDefValue( r );
        this.green.setDefValue( g );
        this.blue.setDefValue( b );
        this.alpha.setDefValue( a );
    }
    
    public void setValue( Color value )
    {
        setValue( value.getRed(), value.getGreen(), value.getBlue(),
                  value.getAlpha() );
    }
    
    public void setValue( int r, int g, int b, int a )
    {
        this.red.setValue( r );
        this.green.setValue( g );
        this.blue.setValue( b );
        this.alpha.setValue( a );
        putToPreferences();
    }
    
    public int getRed()
    {
        return red.getValue();
    }
    
    public int getGreen()
    {
        return green.getValue();
    }
    
    public int getBlue()
    {
        return blue.getValue();
    }
    
    public int getAlpha()
    {
        return alpha.getValue();
    }

    public Color getValue()
    {
        return new Color( getRed(), getGreen(), getBlue(), getAlpha() );
    }
}
