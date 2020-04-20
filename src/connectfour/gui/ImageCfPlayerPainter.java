/* ConnectFour
 * File: ImageCfPlayerPainter.java
 * Creation: 26.06.2009 20:07:17
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

import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Transparency;

import connectfour.game.IConnectFour;
import gamegui.IPlayerPainter;

/**
 * A {@link IPlayerPainter IPlayerPainter} loads two images which are used to
 * draw the players.
 */
public class ImageCfPlayerPainter implements
    IPlayerPainter<IConnectFour.Players>
{       
    ////////////////////////////////////////////////////////////////////////////
    // Member Variables
    ////////////////////////////////////////////////////////////////////////////
    /** The images used to draw the players. */
    private Image[] images = new Image[2];
    
    /** Cacheing for the scaled images. */
    private Image[] scaledImages = new Image[2];

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////
    
    /**
     * Shorthand for {@link #ImageCfPlayerPainter(Image, Image)
     * ImageCfPlayerPainter( images[0], images[1] )}.
     * @param images Array with the length 2 containing the images for the
     *     players.
     * @throws ArrayIndexOutOfBoundsException if images.length < 2.
     */
    public ImageCfPlayerPainter( Image[] images )
    {
        this( images[0], images[1] );
    }
    
    /**
     * Uses the two passed images for drawing the players.
     * @param image1 Image for the first player.
     * @param image2 Image for the second player.
     */
    public ImageCfPlayerPainter( Image image1, Image image2 )
    {
        setImages( image1, image2 );
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
            if( getScaledImage( player ) == null
                || getScaledImage( player ).getWidth( null ) != size
                || getScaledImage( player ).getHeight( null ) != size )
                setScaledImage( player, getImage( player ).getScaledInstance(
                    size, size, Image.SCALE_DEFAULT ) );
            g.drawImage( getImage( player ), x, y, size, size, null );
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // Simple Getters & Setters
    ////////////////////////////////////////////////////////////////////////////
    /**
     * @return Returns the image of a player.
     * @param player Player of whom the image should be returned.
     * @throws ArrayIndexOutOfBoundsException if player == {@link
     *     connectfour.game.IConnectFour.Players#UNDEFINED
     *     IConnectFour.Players.UNDEFINED}.
     */
    public Image getImage( IConnectFour.Players player )
    {
        return images[player.ordinal()];
    }
    
    /**
     * Sets the image for a player.
     * @param player Player of whom the image should be set.
     * @param image image which should be used.
     * @throws ArrayIndexOutOfBoundsException if player == {@link
     *     connectfour.game.IConnectFour.Players#UNDEFINED
     *     IConnectFour.Players.UNDEFINED}.
     */
    public void setImage( IConnectFour.Players player, Image image )
    {
        GraphicsEnvironment env =
            GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice dev = env.getDefaultScreenDevice();
        GraphicsConfiguration conf = dev.getDefaultConfiguration();
        this.images[player.ordinal()] = conf.createCompatibleImage(
            image.getWidth( null ), image.getHeight( null ),
            Transparency.TRANSLUCENT );
        Graphics g = this.images[player.ordinal()].getGraphics();
        g.drawImage( image, 0, 0, null );
    }
    
    /**
     * Sets the images used for drawing the players.
     * @param images Array with the length 2 containing the images.
     * @throws ArrayIndexOutOfBoundsException if the length of images is below
     *     2.
     */
    public void setImages( Image[] images )
    {
        setImages( images[0], images[1] );
    }
    
    public void setImages( Image image1, Image image2 )
    {
        setImage( IConnectFour.Players.FIRST, image1 );
        setImage( IConnectFour.Players.SECOND, image2 );
    }

    public Image[] getImages()
    {
        return images;
    }
    
    /**
     * @return Returns the scaled image of a player.
     * @param player Player of whom the image should be returned.
     * @throws ArrayIndexOutOfBoundsException if player == {@link
     *     connectfour.game.IConnectFour.Players#UNDEFINED
     *     IConnectFour.Players.UNDEFINED}.
     */
    protected Image getScaledImage( IConnectFour.Players player )
    {
        return scaledImages[player.ordinal()];
    }
    
    /**
     * Sets the scaled image for a player.
     * @param player Player of whom the image should be set.
     * @param image image which should be used.
     * @throws ArrayIndexOutOfBoundsException if player == {@link
     *     connectfour.game.IConnectFour.Players#UNDEFINED
     *     IConnectFour.Players.UNDEFINED}.
     */
    protected void setScaledImage( IConnectFour.Players player, Image image )
    {
        this.scaledImages[player.ordinal()] = image;
    }
    
    /**
     * Sets the scaled images used for drawing the players.
     * @param images Array with the length 2 containing the images.
     * @throws ArrayIndexOutOfBoundsException if the length of images is below
     *     2.
     */
    protected void setScaledImages( Image[] images )
    {
        setScaledImages( images[0], images[1] );
    }
    
    protected void setScaledImages( Image image1, Image image2 )
    {
        setScaledImage( IConnectFour.Players.FIRST, image1 );
        setScaledImage( IConnectFour.Players.SECOND, image2 );
    }

    protected Image[] getScaledImages()
    {
        return scaledImages;
    }
}
