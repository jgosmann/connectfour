/* ConnectFour
 * File: ColorIcon.java
 * Creation: 28.06.2009 17:44:27
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

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;

/**
 * Displays a Icon consisting only of one colored rectangle.
 */
public class ColorIcon implements Icon
{
    ////////////////////////////////////////////////////////////////////////////
    // Member Variables
    ////////////////////////////////////////////////////////////////////////////
    /** Color used for the icon. */
    private Color color;
    
    /** Specifies the icon size */
    private int iconWidth, iconHeight;
    
    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////
    /**
     * @param color Color for the icon.
     * @param w Width of the icon.
     * @param h Height of the icon. 
     */
    public ColorIcon( Color color, int w, int h )
    {
        setColor( color );
        setIconWidth( w );
        setIconHeight( h );
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void paintIcon( Component c, Graphics g, int x, int y )
    {
        g.setColor( getColor() );
        g.fillRect( x, y, getIconWidth(), getIconHeight() );
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // Simple Getters & Setters
    ////////////////////////////////////////////////////////////////////////////
    public void setColor( Color color )
    {
        this.color = color;
    }

    public Color getColor()
    {
        return color;
    }

    protected void setIconHeight( int iconHeight )
    {
        this.iconHeight = iconHeight;
    }
    
    @Override
    public int getIconHeight()
    {
        return iconHeight;
    }

    protected void setIconWidth( int iconWidth )
    {
        this.iconWidth = iconWidth;
    }
    
    @Override
    public int getIconWidth()
    {
        return iconWidth;
    }
}
