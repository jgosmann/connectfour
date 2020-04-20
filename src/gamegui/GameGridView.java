/* ConnectFour
 * File: GameGridView.java
 * Creation: 26.06.2009 08:24:23
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

package gamegui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Transparency;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

import javax.swing.JComponent;
import javax.swing.Timer;

/**
 * Displays a game grid.
 */
public class GameGridView<PlayersType> extends JComponent
{
    ////////////////////////////////////////////////////////////////////////////
    // Classes
    ////////////////////////////////////////////////////////////////////////////
    /**
     * This class stores all information of an animated (player) object which
     * is needed during the animation.
     */
    protected class AnimatedObject
    {
        /** The current coordinates. */
        public int x, y;
        /** The destination coordinates. */
        public int destX, destY;
        /**
         * The animation direction (in case this is changed in GameGridView
         * during the animation).
         */
        AnimationDirection direction;
        /** Destination in the game's field. */
        public int destRow, destColumn;
        /**
         * Player which has set this object. It will be used the corresponding
         * IPlayerPainter to draw this object.
         */
        public PlayersType player;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // Enumerations
    ////////////////////////////////////////////////////////////////////////////
    /** Specifies constants for mouseover highlighting modes. */
    public enum HighlightMode
    {
        FIELD,
        ROW,
        COLUMN,
        DISABLE
    }
    
    /**
     * Specifies constants for from which side an animation starts (or if no
     * animation should be used).
     */
    public enum AnimationDirection
    {
        FROM_LEFT,
        FROM_RIGHT,
        FROM_TOP,
        FROM_BOTTOM,
        NO_ANIMATION
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // Static Member Variables
    ////////////////////////////////////////////////////////////////////////////
    private final static long serialVersionUID = 1L;
    
    /**
     * Defines a constant for the ChangeEvent which says that one animation
     * has ended.
     */
    public final static int GAMEGRIDVIEW_ANIMATION_END =
        ComponentEvent.RESERVED_ID_MAX + 1;
    
    /**
     * Defines the default colors.
     * @see #stdGridLineColor
     * @see #highlightColor
     * @see #markColor
     */
    private final static Color stdGridLineColor = Color.BLACK,
        stdHighlightColor = Color.YELLOW, stdMarkColor = Color.YELLOW;

    /**
     * Defines the default animation speed.
     * @see #animationSpeed
     */
    private final static double stdAnimationSpeed = 0.03;
    
    /**
     * Defines the default animation interval in milliseconds.
     * @see #animationInterval
     */
    private final static int stdAnimationInterval = 16;
    
    ////////////////////////////////////////////////////////////////////////////
    // Member Variables
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Specifies which player has to be painted at which position.
     * Because Java does not support Arrays of generic types (which IMO sucks)
     * this has to be a ArrayList of an ArrayList.
     */
    private ArrayList<ArrayList<PlayersType>> grid;
    
    /**
     * Specifies which fields are marked.
     */
    private boolean marked[][];
    
    /**
     * Specifies the class which implements the functions to paint the 
     * players
     */
    private IPlayerPainter<PlayersType> playersPainter;
    
    /**
     * This list contains all objects currently animated which have not reached
     * there final field.
     */
    final LinkedList<AnimatedObject> animate = new LinkedList<AnimatedObject>();
    /** Timer for the animation. */
    Timer animationTimer;
    
    /** Background tile image */
    private Image backgroundTile = null;
    /** Foreground tile image */
    private Image foregroundTile = null;
    /** Scaled background tile image. */
    private Image scaledBackgroundTile = null;
    /** Scaled foreground tile image. */
    private Image scaledForegroundTile = null;
    
    /** Specifies the color of the grid. */
    private Color gridLineColor;
    /** Specifies the color of the hover highlight. */
    private Color highlightColor;
    /** Specifies the mark color. */
    private Color markColor;
    /** Specifies the highlight mode. */
    private HighlightMode highlightMode = HighlightMode.DISABLE;
    /** Specifies the animation direction. */
    private AnimationDirection animationDirection =
        AnimationDirection.NO_ANIMATION;
    /**
     * Specifies the animation speed in percent of the components side length
     * per {@link #animationInterval time interval}. */
    private double animationSpeed;
    /** Specifies the update rate for animation in milliseconds. */
    private int animationInterval;
    
    /**
     * Needed to get the animations right while the compononent is being
     * resized.
     */
    private int lastFieldSize;
    
    /** Image for buffering the drawing process. */
    private Image buffer;
    /** Defines the area to clear for the highlight bar. */
    private int lastHighlightFromRow, lastHighlightToRow,
                lastHighlightFromColumn, lastHighlightToColumn;
    
    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////
    /**
     * @param rows Number of rows of the games grid.
     * @param columns Number of columns of the games grid.
     * @param init Value used to initialize the game's grid.
     * @param playersPainter Class implementing {@link IPlayerPainter
     *     IPlayerPainter} which is used to draw the players.
     */
    public GameGridView( int rows, int columns, PlayersType init,
                  IPlayerPainter<PlayersType> playersPainter )
    {
        setGridSize( rows, columns, init );
        setPlayersPainter( playersPainter );
        setGridLineColor( getStdGridLineColor() );
        setHighlightColor( getStdHighlightColor() );
        setMarkColor( getStdMarkColor() );
        setAnimationSpeed( getStdAnimationSpeed() );
        setAnimationInterval( getStdAnimationInterval() );
        
        setLastFieldSize( getFieldSize() );
        
        addMouseMotionListener( new MouseMotionAdapter()
        {
            public void mouseMoved( MouseEvent e )
            {
                paintComponentBufferedMousePos();
            }
        } );
        addMouseListener( new MouseAdapter()
        {
            public void mouseExited( MouseEvent e )
            {
                paintComponentBufferedMousePos();
            }
        } );
        
        setBufferSize( getWidth(), getHeight() );
        paintComponentBuffered( 0, 0, getGridRows() - 1, getGridColumns() -1  );
        
        addComponentListener( onResize );
        
        animationTimer = new Timer( getAnimationInterval(), animator );
        animationTimer.stop();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Converts a pixel coordinate to the corresponding column in the game grid.
     * @param x x pixel coordinate.
     * @return corresponding column.
     */
    public int coordToColumn( int x )
    {
        return (int) (getGridColumns() * (double) (x - getXOffset())
                                       / getGridPixelWidth());
    }
    
    /**
     * Converts a pixel coordinate to the corresponding row in the game grid.
     * @param y y pixel coordinate.
     * @return corresponding row.
     */
    public int coordToRow( int y )
    {
        return (int) (getGridRows()
                      * (double) (getGridPixelHeight() - y + getYOffset())
                                 / getGridPixelHeight());
    }
    
    /** Instance of animation handler. */
    protected final Animator animator = new Animator( this );
    /** Handler for animation which is called every several milliseconds. */
    protected class Animator implements ActionListener
    {
        private GameGridView<PlayersType> workingOn;
        
        public Animator( GameGridView<PlayersType> workingOn )
        {
            this.workingOn = workingOn;    
        }
        
        public void actionPerformed( ActionEvent e )
        {
            if( animate.size() <= 0 )
                return;
            
            LinkedList<AnimatedObject> toRemove =
                new LinkedList<AnimatedObject>();
            int minX = Integer.MAX_VALUE, maxX = 0;
            int minY = Integer.MAX_VALUE, maxY = 0;
            
            for( AnimatedObject obj : animate )
            {
                boolean destReached = false;
                if( obj.x < minX )
                    minX = obj.x;
                if( obj.x > maxX )
                    maxX = obj.x;
                if( obj.y < minY )
                    minY = obj.y;
                if( obj.y > maxY )
                    maxY = obj.y;
                switch( obj.direction )
                {
                    case FROM_LEFT:
                        obj.x += getPixelAnimationSpeed();
                        if( obj.x > maxX )
                            maxX = obj.x;
                        if( obj.x >= obj.destX )
                            destReached = true;
                        break;
                    case FROM_RIGHT:
                        obj.x -= getPixelAnimationSpeed();
                        if( obj.x < minX )
                            minX = obj.x;
                        if( obj.x <= obj.destX )
                            destReached = true;
                        break;
                    case FROM_TOP:
                        obj.y += getPixelAnimationSpeed();
                        if( obj.y > maxY )
                            maxY = obj.y;
                        if( obj.y >= obj.destY )
                            destReached = true;
                        break;
                    case FROM_BOTTOM:
                        obj.y -= getPixelAnimationSpeed();
                        if( obj.y < minY )
                            minY = obj.y;
                        if( obj.y <= obj.destY )
                            destReached = true;
                        break;
                }
                
                if( destReached )
                {
                    setField( obj.destRow, obj.destColumn, obj.player );
                    toRemove.add( obj );                    
                }
            }
            
            boolean dispatchEvent = false;
            if( toRemove.size() > 0 )
            {
                animate.removeAll( toRemove );
                dispatchEvent = true;
            }
            
            minY += getYOffset();
            maxY += getYOffset();
            minX += getXOffset();
            maxX += getXOffset();
            
            int fromRow = coordToRow( maxY + getFieldSize() );
            if( fromRow < 0 )
                fromRow = 0;
            int fromColumn = coordToColumn( minX );
            if( fromColumn < 0 )
                fromColumn = 0;
            int toRow = coordToRow( minY );
            if( toRow > getGridRows() - 1 )
                toRow = getGridRows() - 1;
            int toColumn = coordToColumn( maxX + getFieldSize() );
            if( toColumn > getGridColumns() - 1 )
                toColumn = getGridColumns() - 1;
            paintComponentBuffered( fromRow, fromColumn, toRow, toColumn );
            
            if( dispatchEvent )
                dispatchEvent( new ComponentEvent(
                    (Component) workingOn, GAMEGRIDVIEW_ANIMATION_END ) );
        }
    };
    
    /**
     * Inserts a new disc into the game's grid. If {@link #animationDirection
     * animation are enabled} it will be animated. An ComponentEvent will be
     * dispatched as soon as the animation ended. This may be instantly if
     * animations are deactivated.
     * @param row The row which is the destination of the disc.
     * @param column The column which the destination of the disc.
     * @param player Player who inserted the disc.
     */
    public void insert( int row, int column, PlayersType player )
        throws IndexOutOfBoundsException
    {
        if( animationDirection == AnimationDirection.NO_ANIMATION )
        {
            grid.get( row ).set( column, player );
            dispatchEvent( new ComponentEvent( this,
                                               GAMEGRIDVIEW_ANIMATION_END ) );
            paintComponentBuffered( row, column, row, column );
        }
        else
        {
            if( row < 0 || row >= getGridRows()
                || column < 0 || column >= getGridColumns() )
                throw new IndexOutOfBoundsException(
                    "(" + row + ", " + column + ")" );
            
            AnimatedObject a = new AnimatedObject();
            switch( animationDirection )
            {
                case FROM_LEFT:
                    a.x = - getFieldSize();
                    a.y = (getGridRows()-1 - row) * getFieldSize();
                    break;
                case FROM_RIGHT:
                    a.x = getGridPixelWidth() + getFieldSize();
                    a.y = (getGridRows()-1 - row) * getFieldSize();
                    break;
                case FROM_TOP:
                    a.x = column * getFieldSize();
                    a.y = - getFieldSize();
                    break;
                case FROM_BOTTOM:
                    a.x = column * getFieldSize();
                    a.y = getGridPixelHeight() + getFieldSize();
                    break;
            }
            a.destX = column * getFieldSize();
            a.destY = (getGridRows()-1 - row) * getFieldSize();
            a.direction = getAnimationDirection();
            a.destRow = row;
            a.destColumn = column;
            a.player = player;
            animate.add( a );
            // We do not repaint here. This will be done in some milliseconds
            // through the timer.
        }
    }
    
    protected ComponentAdapter onResize = new ComponentAdapter()
    {
        public void componentResized( ComponentEvent e )
        {
            double factor = (double) getFieldSize() / getLastFieldSize();
            for( AnimatedObject obj : animate )
            {
                switch( obj.direction )
                {
                    case FROM_LEFT:
                    case FROM_RIGHT:
                        obj.x = (int) (factor * obj.x);
                        obj.destX = (int) (factor * obj.destX);
                        obj.y = obj.destY = (getGridRows()-1 - obj.destRow)
                                            * getFieldSize();
                        break;
                    case FROM_TOP:
                    case FROM_BOTTOM:
                        obj.x = obj.destX = obj.destColumn * getFieldSize();
                        obj.y = (int) (factor * obj.y);
                        obj.destY = (int) (factor * obj.destY);
                        break;
                }
            }
            setLastFieldSize( getFieldSize() );
            
            setBufferSize( getWidth(), getHeight() );
            paintComponentBuffered( 0, 0, getGridRows() -1 ,
                                    getGridColumns() - 1 );
        }
    };
    
    /**
     * Shorthand for {@link #paintComponentBuffered(int, int, int, int)
     * paintComponentBuffered(0, 0, getGridRows()-1, getGridColumns()-1)}.
     */
    protected void paintComponentBuffered()
    {
        paintComponentBuffered( 0, 0, getGridRows() - 1, getGridColumns() -1 );
    }
    
    /**
     * Shorthand for {@link #paintComponentBuffered(int, int, int, int, boolean)
     * paintComponentBuffered(fromRow, fromColumn, toRow, toColumn, true )}.
     */
    protected void paintComponentBuffered( int fromRow, int fromColumn,
                                           int toRow, int toColumn )
    {
        paintComponentBuffered( fromRow, fromColumn, toRow, toColumn, true );
    }
    
    /**
     * Repaints the component in the area needed to display the current
     * highlight bar. This area depends on the current mouse position.
     */
    protected void paintComponentBufferedMousePos()
    {
        Point mousePosition = getMousePosition();
        if( mousePosition == null )
            return;
        int row = coordToRow( mousePosition.y );
        int column = coordToColumn( mousePosition.x );
        if( row < 0 )
            row = 0;
        if( row >= getGridRows() )
            row = getGridRows() - 1;
        if( column < 0 )
            column = 0;
        if( column >= getGridColumns() )
            column = getGridColumns() - 1;
        switch( getHighlightMode() )
        {
            case ROW:
                paintComponentBuffered( row, 0, row, getGridColumns() - 1 );
                break;
            case COLUMN:
                paintComponentBuffered( 0, column, getGridRows() - 1, column );
                break;
            default:
                paintComponentBuffered( row, column, row, column );
                break;
        }
    }
    
    /**
     * Paints the component to the buffer in the specified area.
     * @param fromRow Begin painting from this row on.
     * @param fromColumn Begin painting from this column on.
     * @param toRow End painting in this row.
     * @param toColumn End painting in this column
     * @param display If true the buffer will displayed at the screen after
     *     drawing.
     */
    protected void paintComponentBuffered( int fromRow, int fromColumn,
        int toRow, int toColumn, boolean display )
    {
        if( getBuffer() == null )
            return;
        
        /* Create graphics object and translate Coordinates */
        Graphics g = getBuffer().getGraphics().create(
            getXOffset(), getYOffset(), getGridPixelWidth(),
            getGridPixelHeight() );
        Rectangle bounds = g.getClipBounds();
        
        /* Calculate drawing area. */
        int clipX = fromColumn * getFieldSize();
        int clipY = (getGridRows()-1 - toRow) * getFieldSize();
        int clipWidth = (toColumn - fromColumn + 1) * getFieldSize();
        int clipHeight = (toRow - fromRow + 1) * getFieldSize();
        Rectangle clip = new Rectangle( clipX, clipY, clipWidth, clipHeight);
        
        /* Normally the background would be cleared here, but because
         * there may be a recursive call of this function when painting the
         * highlight bar, we can clear the background not before this call
         * is done. Otherwise there may be two layers of the graphics which
         * makes strange effects if the images contain transparency.
         */
        
        /* Scale the background tiles. */
        if( getBackgroundTile() != null )
        {
            if( getScaledBackgroundTile() == null
                || getScaledBackgroundTile().getWidth( null ) != getFieldSize()
                || getScaledBackgroundTile().getHeight( null ) != getFieldSize() )
                setScaledBackgroundTile( getBackgroundTile().getScaledInstance(
                    getFieldSize(), getFieldSize(), Image.SCALE_DEFAULT ) );
        }
        
        /* Draw highlight bar */
        Point mousePosition = getMousePosition();
        if( display && mousePosition != null
            && bounds.contains( mousePosition.x - getXOffset(),
                                mousePosition.y - getYOffset() )
            && getHighlightMode() != HighlightMode.DISABLE && isEnabled() )
        {
            int row = coordToRow( mousePosition.y );
            int column = coordToColumn( mousePosition.x );
            
            if( getLastHighlightFromRow() > row || getLastHighlightToRow() < row
                || getLastHighlightFromColumn() > column
                || getLastHighlightToColumn() < column )
                paintComponentBuffered( getLastHighlightFromRow(),
                                        getLastHighlightFromColumn(),
                                        getLastHighlightToRow(),
                                        getLastHighlightToColumn(), false );
            
            int x = getFieldSize() * column;
            int y = getFieldSize() * row;
            int w = getFieldSize();
            int h = getFieldSize();
            switch( getHighlightMode() )
            {
                case ROW:
                    x = clipX;
                    w = clipWidth;
                    setLastHighlightArea( row, 0, row, getGridColumns() - 1 );
                    break;
                case COLUMN:
                    y = clipY;
                    h = clipHeight;
                    setLastHighlightArea( 0, column, getGridRows() - 1, column );
                    break;
                default:
                    setLastHighlightArea( row, column, row, column );
                    break;
            }

            /* Clear background and draw highlight bar */
            g.setColor( getBackground() );
            g.fillRect( clipX, clipY, clipWidth, clipHeight );
            g.setColor( getHighlightColor() );
            g.fillRect( x, y, w, h );
        }
        else // Clear background
        {
            g.fillRect( clipX, clipY, clipWidth, clipHeight );
            g.setColor( getHighlightColor() );
        }
            
        /* Draw the background tiles, marks and players. */
        for( int i = fromRow; i <= toRow; i++ )
        {
            for( int j = fromColumn; j <= toColumn; j++ )
            {
                int x = j * getFieldSize();
                int y = (getGridRows()-1 - i) * getFieldSize();
                int size = getFieldSize();
                if( getScaledBackgroundTile() != null )
                    g.drawImage( getScaledBackgroundTile(), x, y, null );
                getPlayersPainter().paintPlayer( x, y, size, getField( i, j ),
                                                 g );
                g.setColor( getMarkColor() );  
                if( isMarked( i, j ) )
                    g.fillRect( x, y, size, size );
            }
        }
        
        /* Draw the animated players */
        for( AnimatedObject obj : animate )
        {
            if( clip.intersects( new Rectangle( obj.x, obj.y, getFieldSize(),
                                                getFieldSize() ) ) )
                getPlayersPainter().paintPlayer( obj.x, obj.y, getFieldSize(),
                                                 obj.player, g );
        }
        
        /* Draw the foreground tiles. */
        if( getForegroundTile() != null )
        {
            if( getScaledForegroundTile() == null
                || getScaledForegroundTile().getWidth( null ) != getFieldSize()
                || getScaledForegroundTile().getHeight( null ) != getFieldSize() )
                setScaledForegroundTile( getForegroundTile().getScaledInstance(
                    getFieldSize(), getFieldSize(), Image.SCALE_DEFAULT ) );
            
            for( int i = fromRow; i <= toRow; i++ )
            {
                for( int j = fromColumn; j <= toColumn; j++ )
                {
                    int x = j * getFieldSize();
                    int y = (getGridRows()-1 - i) * getFieldSize();
                    g.drawImage( getScaledForegroundTile(), x, y, null );
                }
            }
        }
        
        /* Draw the grid.  */
        g = getBuffer().getGraphics().create(
             getXOffset(), getYOffset(), getGridPixelWidth(),
             getGridPixelHeight() );
        g.setColor( getGridLineColor() );
        for( int i = 0; i <= getGridColumns(); i++ )
            g.drawLine( i * getFieldSize(), 0,
                        i * getFieldSize(), bounds.height );
        for( int i = 0; i <= getGridRows(); i++ )
            g.drawLine( 0, i * getFieldSize(), 
                        bounds.width, i * getFieldSize() );
        
        /* Draw buffer to screen */
        if( display )
            repaint();
    }
    
    @Override
    protected void paintComponent( Graphics g )
    {
        if( getBuffer() != null );
            g.drawImage( getBuffer(), 0, 0, null );
    }
    
    @Override
    protected void processComponentEvent( ComponentEvent e )
    {
        if( e.getID() == GAMEGRIDVIEW_ANIMATION_END )
        {
            for( int i = 0; i < getComponentListeners().length; i++ )
            {
                if( getComponentListeners()[i] instanceof GameGridViewListener )
                    ((GameGridViewListener) getComponentListeners()[i])
                        .animationEnded( e );
            }
        }
        else
        {
            super.processComponentEvent( e );
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Simple Getters & Setters
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Sets the direction of the animation and enables or disables the timer.
     */
    public void setAnimationDirection( AnimationDirection animationDirection )
    {
        this.animationDirection = animationDirection;
        if( animationDirection == AnimationDirection.NO_ANIMATION )
            animationTimer.stop();
        else
            animationTimer.start();
    }
    
    public AnimationDirection getAnimationDirection()
    {
        return animationDirection;
    }
    
    /** Sets the interval of the animation and updates the timer. */
    public void setAnimationInterval( int animationInterval )
    {
        this.animationInterval = animationInterval;
        animationTimer = new Timer( animationInterval, animator );
        if( getAnimationDirection() == AnimationDirection.NO_ANIMATION )
            animationTimer.stop();
    }

    public int getAnimationInterval()
    {
        return animationInterval;
    }

    public void setAnimationSpeed( double animationSpeed )
    {
        this.animationSpeed = animationSpeed;
    }

    public double getAnimationSpeed()
    {
        return animationSpeed;
    }

    public void setBackgroundTile( Image backgroundTile )
    {
        if( backgroundTile == null )
        {
            this.backgroundTile = null;
            this.scaledBackgroundTile = null;
            paintComponentBuffered();
            return;
        }
        
        GraphicsEnvironment env =
            GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice dev = env.getDefaultScreenDevice();
        GraphicsConfiguration conf = dev.getDefaultConfiguration();
        this.backgroundTile = conf.createCompatibleImage(
            backgroundTile.getWidth( null ), backgroundTile.getHeight( null ),
            Transparency.TRANSLUCENT );
        Graphics g = this.backgroundTile.getGraphics();
        g.drawImage( backgroundTile, 0, 0, null );
        paintComponentBuffered();
    }

    public Image getBackgroundTile()
    {
        return backgroundTile;
    }

    public Image getBuffer()
    {
        return buffer;
    }

    @Override
    public void setEnabled( boolean enabled )
    {
        super.setEnabled( enabled );
        paintComponentBufferedMousePos();
    }
    
    public void setForegroundTile( Image foregroundTile )
    {
        if( foregroundTile == null )
        {
            this.foregroundTile = null;
            this.scaledForegroundTile = null;
            paintComponentBuffered();
            return;
        }
        
        GraphicsEnvironment env =
            GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice dev = env.getDefaultScreenDevice();
        GraphicsConfiguration conf = dev.getDefaultConfiguration();
        this.foregroundTile = conf.createCompatibleImage(
            foregroundTile.getWidth( null ), foregroundTile.getHeight( null ),
            Transparency.TRANSLUCENT );
        Graphics g = this.foregroundTile.getGraphics();
        g.drawImage( foregroundTile, 0, 0, null );
        paintComponentBuffered();
    }

    public Image getForegroundTile()
    {
        return foregroundTile;
    }

    public void setGridLineColor( Color gridLineColor )
    {
        this.gridLineColor = gridLineColor;
        paintComponentBuffered();
    }

    public Color getGridLineColor()
    {
        return gridLineColor;
    }

    public void setHighlightColor( Color highlightColor )
    {
        this.highlightColor = highlightColor;
        paintComponentBufferedMousePos();
    }

    public Color getHighlightColor()
    {
        return highlightColor;
    }

    public void setHighlightMode( HighlightMode highlightMode )
    {
        this.highlightMode = highlightMode;
        paintComponentBufferedMousePos();
    }
 
    public HighlightMode getHighlightMode()
    {
        return highlightMode;
    }
    
    protected void setLastFieldSize( int lastFieldSize )
    {
        this.lastFieldSize = lastFieldSize;
    }

    protected int getLastFieldSize()
    {
        return lastFieldSize;
    }

    protected int getLastHighlightFromColumn()
    {
        return lastHighlightFromColumn;
    }

    protected int getLastHighlightFromRow()
    {
        return lastHighlightFromRow;
    }
    
    protected int getLastHighlightToColumn()
    {
        return lastHighlightToColumn;
    }
    
    protected int getLastHighlightToRow()
    {
        return lastHighlightToRow;
    }
    
    public void setMarkColor( Color markColor )
    {
        this.markColor = markColor;
    }

    public Color getMarkColor()
    {
        return markColor;
    }
    
    /**
     * @param row Row to mark or unmark.
     * @param column Column to mark or unmark.
     * @param marked Whether to mark or unmark.
     */
    public void setMarked( int row, int column, boolean marked )
        throws IndexOutOfBoundsException
    {
        this.marked[row][column] = marked;
        paintComponentBuffered( row, column, row, column );
    }

    /**
     * @param row Row of which to return the marked state.
     * @param column Column of which to return the marked state.
     * @return Returns the marked state of the field.
     */
    public boolean isMarked( int row, int column )
        throws IndexOutOfBoundsException
    {
        return marked[row][column];
    }
    
    protected void setScaledBackgroundTile( Image scaledBackgroundTile )
    {
        this.scaledBackgroundTile = scaledBackgroundTile;
    }

    protected Image getScaledBackgroundTile()
    {
        return scaledBackgroundTile;
    }

    protected void setScaledForegroundTile( Image scaledForegroundTile )
    {
        this.scaledForegroundTile = scaledForegroundTile;
    }

    protected Image getScaledForegroundTile()
    {
        return scaledForegroundTile;
    }

    public static int getStdAnimationInterval()
    {
        return stdAnimationInterval;
    }

    public static double getStdAnimationSpeed()
    {
        return stdAnimationSpeed;
    }

    protected static Color getStdGridLineColor()
    {
        return stdGridLineColor;
    }
    
    public static Color getStdHighlightColor()
    {
        return stdHighlightColor;
    } 
    
    public static Color getStdMarkColor()
    {
        return stdMarkColor;
    }

    public void setPlayersPainter( IPlayerPainter<PlayersType> playersPainter )
    {
        this.playersPainter = playersPainter;
        paintComponentBuffered();
    }

    public IPlayerPainter<PlayersType> getPlayersPainter()
    {
        return playersPainter;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // Advanced Getters
    ////////////////////////////////////////////////////////////////////////////
    /**
     * @param row Row of the field which should be returned. The indices start
     *     at the bottom of the grid.
     * @param column Column of the field which should be returned. The indices
     *     start at the left side of the grid.
     * @return Returns which players occupies the specified field in the grid.
     */
    public PlayersType getField( int row, int column )
        throws IndexOutOfBoundsException
    {
        return grid.get( row ).get( column );
    }    
    
    /**
     * @return Returns the side length of a field in pixels. Is always greater
     *     or equal 1.
     */
    public int getFieldSize()
    {
        /* Width and height each minus one, because we need an additional one
         * pixel line for the borders.
         */
        int w = (getWidth()-1) / getGridColumns();
        int h = (getHeight()-1) / getGridRows();
        if( w <= 0 || h <= 0 )
            return 1;
        return (w < h) ? w : h;
    }
    
    /** @return Returns the number of rows of the game grid. */
    public int getGridRows()
    {
        return grid.size();
    }
    
    /** @return Returns the height of the game grid in pixels */
    public int getGridPixelHeight()
    {
        /* +1 for the border */
        return getGridRows() * getFieldSize() + 1;
    }
    
    /** @return Returns the width of the game grid in pixels */
    public int getGridPixelWidth()
    {
        /* +1 for the border */
        return getGridColumns() * getFieldSize() + 1;
    }
    
    /** @return Returns the number of columns of the game grid. */
    public int getGridColumns()
    {
        return grid.get( 0 ).size();
    }
    
    /**
     * @return Returns the animation speed in pixels per second. The returned
     *     value will not below 1.
     */
    public int getPixelAnimationSpeed()
    {
        int pixelSpeed = (int) (getGridPixelHeight() * getAnimationSpeed());
        return (pixelSpeed > 0) ? pixelSpeed : 1;
    }
    
    /**
     * @return Returns the x offset of the game grid to the components
     *     coordinates.
     */
    public int getXOffset()
    {
        /* The plus 1 is because we need an additional one pixel line for the
         * border.
         */
        return (getWidth() - getGridColumns() * getFieldSize() + 1) / 2;
    }
    
    /**
     * @return Returns the y offset of the game grid to the components
     *     coordinates.
     */
    public int getYOffset()
    {
        /* The plus 1 is because we need an additional one pixel line for the
         * border.
         */
        return (getHeight() - getGridRows() * getFieldSize() + 1) / 2;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Advanced Setters
    ////////////////////////////////////////////////////////////////////////////
    /** Sets the size of the buffer image. */
    protected void setBufferSize( int width, int height )
    {
        if( width <= 0 || height <= 0 )
            return;
        GraphicsEnvironment env =
            GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice dev = env.getDefaultScreenDevice();
        GraphicsConfiguration conf = dev.getDefaultConfiguration();
        this.buffer = conf.createCompatibleImage( width, height,
                                                  Transparency.OPAQUE );
        Graphics g = this.buffer.getGraphics();
        g.setColor( getBackground() );
        g.fillRect( 0, 0, width, height );
    }
    
    /**
     * Sets a field in the grid.
     * @param row The row in which the field lies.
     * @param column The column in which the field lies.
     * @param player Player to which the field should be set.
     */
    public void setField( int row, int column, PlayersType player )
        throws IndexOutOfBoundsException
    {
        grid.get( row ).set( column, player );
        paintComponentBuffered( row, column, row, column );
    }
    
    /**
     * Sets the size of the game grid and initializes it. Sets all fields to
     * unmarked.
     * @param rows Rows of the game grid.
     * @param columns Columns of the game grid.
     * @param init Value which is used for initializing.
     */
    public void setGridSize( int rows, int columns, PlayersType init )
    {
        grid = new ArrayList<ArrayList<PlayersType>>( rows );
        marked = new boolean[rows][columns];
        for( int i = 0; i < rows; i++ )
        {
            Arrays.fill( marked[i], false );
            grid.add( new ArrayList<PlayersType>( columns ) );
            for( int j = 0; j < columns; j++ )
                grid.get( i ).add( init );
        }
        setPreferredSize( new Dimension( 64 * columns, 64 * rows ) );
        setLastHighlightArea( 0, 0, 0, 0 );
        if( getBuffer() != null )
        {
            Graphics g = getBuffer().getGraphics();
            g.setColor( getBackground() );
            g.fillRect( 0, 0, getBuffer().getWidth( null ),
                        getBuffer().getHeight( null ) );
        }
        paintComponentBuffered( 0, 0, getGridRows() - 1, getGridColumns() - 1 );
    }

    /**
     * Sets the last highlight area.
     * @see #lastHighlightFromRow
     * @see #lastHighlightToRow
     * @see #lastHighlightFromColumn
     * @see #lastHighlightToColumn
     */
    protected void setLastHighlightArea( int lastHighlightFromRow,
        int lastHightlightFromColumn, int lastHightlightToRow,
        int lastHighlightToColumn )
    {
        this.lastHighlightFromRow = lastHighlightFromRow;
        this.lastHighlightFromColumn = lastHightlightFromColumn;
        this.lastHighlightToRow = lastHightlightToRow;
        this.lastHighlightToColumn = lastHighlightToColumn;
    }
}
