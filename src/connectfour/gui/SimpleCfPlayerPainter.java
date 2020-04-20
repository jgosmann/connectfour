/* ConnectFour
 * File: SimpleCfPlayerPainter.java
 * Creation: 26.06.2009 08:49:11
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

package connectfour.gui;

import gamegui.IPlayerPainter;

import java.awt.Color;
import java.awt.Graphics;
import connectfour.game.IConnectFour;;

/**
 * A simple {@link IPlayerPainter IPlayerPainter} just drawing filled circles.
 */
public class SimpleCfPlayerPainter
    implements IPlayerPainter<IConnectFour.Players>
{
    ////////////////////////////////////////////////////////////////////////////
    // Static Member Variables
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Defines the default colors.
     * @see #colors
     */
    private final static Color[] stdColors;
    
    static
    {
        stdColors = new Color[] { Color.WHITE, Color.BLACK };
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // Member Variables
    ////////////////////////////////////////////////////////////////////////////
    private Color[] colors = new Color[2];
    
    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Shorthand for {@link #SimpleCfPlayerPainter(Color[])
     * SimpleCfPlayerPainter( getStdColors() )}.
     */
    public SimpleCfPlayerPainter()
    {
        this( getStdColors() );
    }
    
    /**
     * Shorthand for {@link #SimpleCfPlayerPainter(Color, Color)
     * SimpleCfPlayerPainter( colors[0], colors[1] )}.
     * @param colors Array with the length 2 containing the colors for the
     *     players.
     * @throws ArrayIndexOutOfBoundsException if colors.length < 2.
     */
    public SimpleCfPlayerPainter( Color[] colors )
    {
        this( colors[0], colors[1] );
    }
    
    /**
     * Sets the colors of the players to the passed values.
     * @param c1 Color of player one.
     * @param c2 Color of player two.
     */
    SimpleCfPlayerPainter( Color c1, Color c2 )
    {
        setColors( c1, c2 );
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // Methods
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void paintPlayer( int x, int y, int size,
                             IConnectFour.Players player, Graphics g )
    {
        if( player != IConnectFour.Players.UNDEFINED )
        {
            g.setColor( getColor( player ) );
            g.fillOval( x + 1, y + 1, size - 2, size - 2 );
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Simple Getters & Setters
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Copies the values in the array colors. Therefore colors may be changed
     * after calling this function without changing the colors of the
     * SimpleCfPlayerPainter.
     * @throws ArrayIndexOutOfBoundsException if colors.length < 2
     */
    public void setColors( Color[] colors )
    {
        setColors( colors[0], colors[1] );
    }
    
    public void setColors( Color c1, Color c2 )
    {
        setPlayerColor( IConnectFour.Players.FIRST, c1 );
        setPlayerColor( IConnectFour.Players.SECOND, c2 );
    }

    /**
     * @return Returns the color used for the player.
     * @param player Player of whom the color should be looked up.
     * @throws ArrayIndexOutOfBoundsException if player == {@link
     *     connectfour.game.IConnectFour.Players#UNDEFINED
     *     IConnectFour.Players.UNDEFINED}.
     */
    public Color getColor( IConnectFour.Players player )
    {
        return getColors()[player.ordinal()];
    }

    public Color[] getColors()
    {
        return colors;
    }
    
    public static Color getStdColor( IConnectFour.Players player )
    {
        return getStdColors()[player.ordinal()];
    }

    public static Color[] getStdColors()
    {
        return stdColors;
    }
    
    /**
     * Sets the color for a player.
     * @param player Player of whom the color should be set.
     * @param color color which should be set.
     * @throws ArrayIndexOutOfBoundsException if player == {@link
     *     connectfour.game.IConnectFour.Players#UNDEFINED
     *     IConnectFour.Players.UNDEFINED}.
     */
    public void setPlayerColor( IConnectFour.Players player, Color color )
    {
        this.colors[player.ordinal()] = color;
    }
}
