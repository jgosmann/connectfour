/* ConnectFour
 * File: ConnectFour.java
 * Creation: 26.06.2009 08:33:28
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

package application;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Event;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.PropertyResourceBundle;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.border.EtchedBorder;

import settings.SettingBoolean;
import settings.SettingColor;
import settings.SettingDouble;
import settings.SettingFilename;
import settings.SettingInPreferences;
import settings.SettingInt;
import settings.SettingSelectOne;
import settings.SettingSelectOneItem;
import settings.SettingString;
import settings.UserSettingsTree;
import settings.gui.JSetting;
import settings.gui.JSettingDialog;
import settings.gui.JSettingFilename;
import settings.gui.SettingGuiBaseInitializer;
import connectfour.game.IConnectFour;
import connectfour.game.IConnectFourEventEmitting;
import connectfour.game.LocalConnectFourEventEmitting;
import connectfour.game.MoveDoneAdapter;
import connectfour.game.MoveDoneEvent;
import connectfour.game.RemoteCfAdapter;
import connectfour.game.RemoteCfEvent;
import connectfour.game.RemoteCfListener;
import connectfour.game.RemoteCfPlayer;
import connectfour.game.RemoteConnectFour;
import connectfour.gui.ImageCfPlayerPainter;
import connectfour.gui.SimpleCfPlayerPainter;
import gamegui.GameGridView;
import gamegui.GameGridViewAdapter;
import gamegui.IPlayerPainter;

/**
 * Class loading the user interface and controls the connections between
 * the game model and view.
 */
public class ConnectFour
{
    ////////////////////////////////////////////////////////////////////////////
    // Static Member Variables
    ////////////////////////////////////////////////////////////////////////////
    /** About message */
    private static String aboutMsg;
    
    /** Contains the translations. */
    private static PropertyResourceBundle translations;
    
    /** Provides the application settings. */
    protected final static SettingInPreferences s =
        UserSettingsTree.getInstance();
    
    /** Statusbar */
    private static JLabel statusBar = new JLabel();
    
    /** Mainwindow */
    private static JFrame mainWindow;
    
    /** Game view */
    private static GameGridView<IConnectFour.Players> gameView;
    
    /** Player painter */
    private static IPlayerPainter<IConnectFour.Players> playersPainter;
    
    /** Game model */
    private static IConnectFourEventEmitting game = null;
    
    /** Players */
    private static IPlayer[] players = new IPlayer[] { null , null };
    
    ////////////////////////////////////////////////////////////////////////////
    // Static Methods
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Initializes and generates the default values for the programs settings
     * in {@link UserSettingsTree UserSettingsTree}. 
     */
    private static void initializeSettings()
    {
        s.add( new SettingBoolean( "firststart", true ) );
        
        s.add( new SettingFilename( "languagefile", "en_US.properties" ) );
        
        s.add( new SettingInt( "mainwindow/x", 50 ) );
        s.add( new SettingInt( "mainwindow/y", 50 ) );
        s.add( new SettingInt( "mainwindow/width", 640 ) );
        s.add( new SettingInt( "mainwindow/height", 480 ) );
        
        s.add( new SettingSelectOne( "gameview/players", 0 ) );
        s.add( new SettingSelectOneItem( "gameview/players/simple", false, 0 ) );
        s.add( new SettingSelectOneItem( "gameview/players/image", true, 1 ) );
        s.add( new SettingColor( "gameview/players/simple/first",
                                 Color.RED, 0 ) );
        s.add( new SettingColor( "gameview/players/simple/second",
                                 new Color( 0, 153, 0 ), 1 ) );
        s.add( new SettingFilename( "gameview/players/image/first",
                                    "player1.png", 0 ) );
        s.add( new SettingFilename( "gameview/players/image/second",
                                    "player2.png", 1 ) );
       
        s.add( new SettingFilename( "gameview/foregroundtile", "front.png", 1 ) );
        s.add( new SettingFilename( "gameview/backgroundtile", "back.png", 2 ) );
        
        s.add( new SettingColor( "gameview/highlightcolor",
                                 new Color( 153, 204, 255 ), 3 ) );
        s.add( new SettingColor( "gameview/markcolor", Color.ORANGE, 4 ) );
        
        s.add( new SettingBoolean( "gameview/animation/enable", true, 5 ) );
        s.add( new SettingDouble( "gameview/animation/speed",
                                  GameGridView.getStdAnimationSpeed(),
                                  0.001, 0.05, 1 ) );
        s.add( new SettingInt( "gameview/animation/interval",
                               GameGridView.getStdAnimationInterval(),
                               5, 250, 2 ) );
        
        s.add( new SettingString( "newgame/player1", "Player 1", -2 ) );
        s.add( new SettingString( "newgame/player2", "Player 2", -1 ) );
        s.add( new SettingSelectOne( "newgame/mode", 0 ) );
        s.add( new SettingSelectOneItem( "newgame/mode/human", true, 0 ) );
        s.add( new SettingSelectOneItem( "newgame/mode/ai", false, 1 ) );
        s.add( new SettingInt( "newgame/mode/ai/recdepth", 5, 1, 10, 0 ) );
        s.add( new SettingDouble( "newgame/mode/ai/difficulty", 1, 0.75, 1, 1 ) );
        s.add( new SettingSelectOneItem( "newgame/mode/host", false, 2 ) );
        s.add( new SettingInt( "newgame/mode/host/port", 34444, 1024, 65535, 0 ) );
        s.add( new SettingString( "newgame/mode/host/password", "", 1 ) );
        s.add( new SettingSelectOneItem( "newgame/mode/join", false, 3 ) );
        s.add( new SettingString( "newgame/mode/join/host", "", 0 ) );
        s.add( new SettingInt( "newgame/mode/join/port", 34444, 1024, 65535, 0 ) );
        s.add( new SettingInt( "newgame/rows", 6, 3, 20, 1 ) );
        s.add( new SettingInt( "newgame/columns", 7, 3, 20, 2 ) );
        s.add( new SettingInt( "newgame/winlength", 4, 3, 20, 3 ) );
        s.add( new SettingBoolean( "newgame/limitchips", true, 4 ) );
        s.add( new SettingInt( "newgame/chips", 32, 4, 256, 5 ) );
    }
    
    /**
     * Loads the translations.
     */
    public static void loadTranslations()
    {
        SettingFilename langFile = SettingFilename.get( s, "languagefile" );
        try
        {
            
            setTranslations( new PropertyResourceBundle( new FileInputStream(
                new File( langFile.getValue() ) ) ) );
        }
        catch( FileNotFoundException e )
        {
            JOptionPane.showMessageDialog(
                null, "Translation file could not be found: " + e.toString(),
                "Error", JOptionPane.ERROR_MESSAGE );
            if( langFile.getValue().compareTo( langFile.getDefValue() ) != 0 )
            {
                langFile.restoreDefaults();
                loadTranslations();
            }
        }
        catch( IOException e )
        {
            JOptionPane.showMessageDialog(
                null, "Translation file could not be loaded: " + e.toString(),
                "Error", JOptionPane.ERROR_MESSAGE );
            if( langFile.getValue().compareTo( langFile.getDefValue() ) != 0 )
            {
                langFile.restoreDefaults();
                loadTranslations();
            }
        }
    }
    
    /**
     * Loads the GUI.
     */
    public static void main( String[] args )
    {        
        /* Settings stuff */
        initializeSettings();
        SettingGuiBaseInitializer.initialize();
        
        /* Load translations */
        loadTranslations();
        
        aboutMsg = tr(
              "Connect Four\nVersion 1.0.1\nCopyright (C) 2009 Jan Gosmann\n"
            + "http://www.hyper-world.de\nSee COPYING for license details." );
        
        /* Create main window */
        mainWindow = new JFrame( tr( "mainwindow_title" ) );
        mainWindow.setIconImage( getAppIcon() );
        mainWindow.setLayout( new BorderLayout() );
        mainWindow.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        mainWindow.addWindowListener( new WindowAdapter() {  
            @Override
            public void windowClosing( WindowEvent e )
            {
                SettingInt.get( s, "mainwindow/width" ).setValue(
                    ((JFrame) e.getSource()).getWidth() );
                SettingInt.get( s, "mainwindow/height" ).setValue(
                    ((JFrame) e.getSource()).getHeight() );
            }
        } );

        /* Create menu */
        JMenuBar menu = new JMenuBar();
        JMenu filemenu = new JMenu( tr( "File" ) );
        JMenuItem newgame = new JMenuItem( tr( "New Game ..." ) );
        JMenuItem undoMove = new JMenuItem( tr( "Undo Move" ) );
        JMenuItem quit = new JMenuItem( tr( "Quit" ) );
        JMenu settings = new JMenu( tr( "Settings" ) );
        JMenuItem language = new JMenuItem( tr( "Language ..." ) );
        JMenuItem displaySettings = new JMenuItem( tr( "Display Settings ..." ) );
        JMenu help = new JMenu( tr( "?" ) );
        JMenuItem about = new JMenuItem( tr( "About" ) );
        newgame.setAccelerator( KeyStroke.getKeyStroke(
            KeyEvent.VK_N, Event.CTRL_MASK ) );
        undoMove.setAccelerator( KeyStroke.getKeyStroke(
            KeyEvent.VK_Z, Event.CTRL_MASK ) );
        quit.setAccelerator( KeyStroke.getKeyStroke(
            KeyEvent.VK_Q, Event.CTRL_MASK ) );
        menu.add( filemenu );
        filemenu.add( newgame );
        filemenu.add( undoMove );
        filemenu.addSeparator();
        filemenu.add( quit );
        menu.add( settings );
        settings.add( language );
        settings.add( displaySettings );
        menu.add( help );
        help.add( about );
        mainWindow.setJMenuBar( menu );
        
        newgame.addActionListener( onNewGameClicked );
        undoMove.addActionListener( onUndoMoveClicked );
        language.addActionListener( onLanguageClicked );
        displaySettings.addActionListener( onDisplaySettingsClicked );
        quit.addActionListener( new ActionListener() {  
            @Override
            public void actionPerformed( ActionEvent e )
            {
                mainWindow.dispatchEvent(
                    new WindowEvent( mainWindow, WindowEvent.WINDOW_CLOSING ) );
            }
        } );
        about.addActionListener( onAboutClicked );
        
        /* Create the game view */
        setGameView( new GameGridView<IConnectFour.Players>(
                     8, 8, IConnectFour.Players.FIRST,
                     new SimpleCfPlayerPainter() ) );
        getGameView().setHighlightMode( GameGridView.HighlightMode.COLUMN );
        getGameView().setEnabled( false );
        getGameView().addComponentListener( onAnimationEnd );
        updateSettings();
        mainWindow.add( getGameView() );
        
        /* Status bar */
        getStatusBar().setBorder( BorderFactory.createEtchedBorder(
                EtchedBorder.LOWERED ) );
        mainWindow.add( getStatusBar(), BorderLayout.SOUTH );
        getStatusBar().setText( tr(
            "Welcome! To start a new game take a look into the File menu." ) );
        
        /* Get the whole stuff visible. */
        mainWindow.pack();
        mainWindow.setSize( SettingInt.get( s, "mainwindow/width" ).getValue(), 
                            SettingInt.get( s, "mainwindow/height" ).getValue() );
        mainWindow.setLocation( SettingInt.get( s, "mainwindow/x" ).getValue(),
                                SettingInt.get( s, "mainwindow/y" ).getValue() );
        mainWindow.setVisible( true );
        
        if( SettingBoolean.get( s, "firststart" ).getValue() )
        {
            JOptionPane.showMessageDialog( mainWindow, 
                tr( "Connect Four Copyright (C) 2009 Jan Gosmann\n"
                    + "This program comes with ABSOLUTELY NO WARRANTY; for\n"
                    + "details see COPYING. This is free software, and you are\n"
                    + "welcome to redistribute it under certain conditions;\n"
                    + "see COPYING for details." ), 
                tr( "Welcome!" ), JOptionPane.INFORMATION_MESSAGE );
            SettingBoolean.get( s, "firststart" ).setValue( false );
        }
    }
    
    /** @return Returnds the application icon. */
    public static BufferedImage getAppIcon()
    {
        BufferedImage icon = new BufferedImage( 128, 128,
                                                BufferedImage.TYPE_4BYTE_ABGR );
        Graphics g = icon.getGraphics();
        g.setColor( Color.RED );
        g.fillOval(  0,  0, 63, 63 );
        g.fillOval( 64, 64, 63, 63 );
        g.setColor( Color.BLUE );
        g.fillOval( 64,  0, 63, 63 );
        g.fillOval(  0, 64, 63, 63 );
        return icon;
    }
    
    /**
     * Translates a string with the in {@link #translations translations} loaded
     * translations.
     * @param key String to translate.
     * @return Returns the translated string or key itself it no translation is
     *     available.
     */
    public static String tr( String key )
    {
        try
        {
            return getTranslations().getString( key );   
        }
        catch( Exception e )
        {
            return key;
        }
    }
    
    /**
     * Updates the game view when settings have been changed.
     */
    public static void updateSettings()
    {
        SettingInPreferences s = UserSettingsTree.getInstance();
        
        if( SettingBoolean.get( s, "gameview/animation/enable" ).getValue() )
            getGameView().setAnimationDirection(
                GameGridView.AnimationDirection.FROM_TOP );
        else
            getGameView().setAnimationDirection(
                GameGridView.AnimationDirection.NO_ANIMATION );
        getGameView().setAnimationSpeed( SettingDouble.get(
            s, "gameview/animation/speed" ).getValue() );
        getGameView().setAnimationInterval( SettingInt.get(
            s, "gameview/animation/interval" ).getValue() );
        
        Color[] playerColors = new Color[] {
            SettingColor.get( s, "gameview/players/simple/first" ).getValue(),
            SettingColor.get( s, "gameview/players/simple/second" ).getValue()
        };
        if( SettingSelectOneItem.get( s, "gameview/players/simple" ).getValue() )
        {
            setPlayersPainter( new SimpleCfPlayerPainter( playerColors ) );
        }
        else
        {
            try
            {
                setPlayersPainter( new ImageCfPlayerPainter(
                    ImageIO.read( new File( SettingFilename.get( s,
                        "gameview/players/image/first" ).getValue() ) ),
                    ImageIO.read( new File( SettingFilename.get( s,
                        "gameview/players/image/second" ).getValue() ) ) ) );
            }
            catch( IOException e )
            {
                JOptionPane.showMessageDialog( getGameView(),
                    tr( "Error reading player images: " ) + e.toString(),
                    tr( "Input read error" ), JOptionPane.ERROR_MESSAGE );
                setPlayersPainter( new SimpleCfPlayerPainter() );
            }
        }
        getGameView().setPlayersPainter( getPlayersPainter() );
        
        getGameView().setHighlightColor(
            SettingColor.get( s, "gameview/highlightcolor" ).getValue() );
        Color markColor = SettingColor.get( s, "gameview/markcolor" ).getValue();
        Color markColorAlpha = new Color( 
            markColor.getRed(), markColor.getGreen(), markColor.getBlue(), 127 );
        getGameView().setMarkColor( markColorAlpha );
        getGameView().setGridLineColor( new Color( 0, 0, 0, 0 ) );
        try
        {
            String backgroundTile = SettingFilename.get( s,
                "gameview/backgroundtile" ).getValue();
            if( !backgroundTile.isEmpty() )
                getGameView().setBackgroundTile(
                    ImageIO.read( new File( backgroundTile ) ) );
            else
                getGameView().setBackgroundTile( null );
        }
        catch( IOException e )
        {
            JOptionPane.showMessageDialog( getGameView(),
                tr( "Error reading background tile images: " ) + e.toString(),
                tr( "Input read error" ), JOptionPane.ERROR_MESSAGE );
        }
        try
        {
            String foregroundTile = SettingFilename.get( s,
                "gameview/foregroundtile" ).getValue();
            if( !foregroundTile.isEmpty() )
                getGameView().setForegroundTile(
                    ImageIO.read( new File( foregroundTile ) ) );
            else
            {
                getGameView().setForegroundTile( null );
                getGameView().setGridLineColor( new Color( 0, 0, 0 ) );
            }
        }
        catch( IOException e )
        {     
            JOptionPane.showMessageDialog( getGameView(),
                tr( "Error reading foreground tile images: " ) + e.toString(),
                tr( "Input read error" ), JOptionPane.ERROR_MESSAGE );
            getGameView().setGridLineColor( new Color( 0, 0, 0 ) );
        }
        
    }

    ////////////////////////////////////////////////////////////////////////////
    // Event Listeners
    ////////////////////////////////////////////////////////////////////////////
    private static ActionListener onAboutClicked = new ActionListener() {
        @Override
        public void actionPerformed( ActionEvent e )
        {
            JOptionPane.showMessageDialog(
                getMainWindow(), aboutMsg , tr( "About" ),
                JOptionPane.INFORMATION_MESSAGE );
        }
    };
    
    private static GameGridViewAdapter onAnimationEnd =
        new GameGridViewAdapter() {
            @Override
            public void animationEnded( ComponentEvent e )
            {
                if( getGame().isGameFinished() )
                {
                    if( getPlayers()[1] instanceof JoinedPlayer )
                    {
                        try
                        {
                            ((JoinedPlayer) getPlayers()[1]).getRemote()
                            .sendLastMove();
                        }
                        catch( IOException ioe )
                        {
                        }
                    }
                    
                    int startRow = getGame().getWinStartRow();
                    int startColumn = getGame().getWinStartColumn();
                    int endRow = getGame().getWinEndRow();
                    int endColumn = getGame().getWinEndColumn();
                    
                    while( startRow != endRow || startColumn != endColumn )
                    {
                        if( startRow > -1 && startColumn > -1 )
                            getGameView().setMarked( startRow, startColumn,
                                                     true );
                        if( startRow != endRow )
                            startRow += (startRow < endRow) ? 1 : -1;
                        if( startColumn != endColumn )
                            startColumn += (startColumn < endColumn) ? 1 : -1;
                    }
                    if( endRow > -1 && endColumn > -1 )
                        getGameView().setMarked( endRow, endColumn, true );
                    
                    String winner = tr( " wins the game." );
                    switch( getGame().getWinner() )
                    {
                        case FIRST:
                            winner = SettingString.get( s, "newgame/player1" )
                                     .getValue() + winner;
                            break;
                        case SECOND:
                            winner = SettingString.get( s, "newgame/player2" )
                                     .getValue() + winner;
                            break;
                        default:
                            winner = tr( "Draw game." );
                            break;
                    }
                    getStatusBar().setText( winner );
                    JOptionPane.showMessageDialog(
                        getMainWindow(), winner, tr( "Game finished" ),
                        JOptionPane.INFORMATION_MESSAGE );
                    return;
                }
                else
                    getPlayers()[getGame().getCurrentPlayer().ordinal()].move();
            }
        };
    
    private static ActionListener onDisplaySettingsClicked =
        new ActionListener() {
            @Override
            public void actionPerformed( ActionEvent e )
            {
                JSettingDialog dlg = new JSettingDialog(
                    new JSetting( s.get( "gameview" ), getTranslations() ),
                    getMainWindow(), tr( "Display Settings" ), true );
                dlg.setLocationByPlatform( true );
                dlg.setVisible( true );
                updateSettings();
            }
        };
  
    private static ActionListener onLanguageClicked = new ActionListener() {
        @Override
        public void actionPerformed( ActionEvent e )
        {
            JSettingDialog dlg = new JSettingDialog(
                new JSettingFilename( SettingFilename.get( s, "languagefile" ),
                                      getTranslations() ),
                getMainWindow(), tr( "Language Settings" ), true );
            dlg.setLocationByPlatform( true );
            dlg.setVisible( true );
            if( dlg.isAccepted() )
            {
                JOptionPane.showMessageDialog( getMainWindow(),
                    tr( "Please restart program to take changes effect." ),
                    tr( "Program restart needed" ),
                    JOptionPane.INFORMATION_MESSAGE );
            }
        }
    };
    
    private static MoveDoneAdapter onMoveDone = new MoveDoneAdapter() {
        @Override
        public void moveDone( MoveDoneEvent e )
        {
            getGameView().setEnabled( false );
            
            getGameView().insert( e.getRow(), e.getColumn(), e.getPlayer() );
        }
    };
        
    private static ActionListener onNewGameClicked = new ActionListener() {
        @Override
        public void actionPerformed( ActionEvent e )
        {
            JSettingDialog dlg = new JSettingDialog(
                new JSetting( s.get( "newgame" ), getTranslations() ),
                getMainWindow(), tr( "New Game" ), true );
            dlg.setApplyVisibility( false );
            dlg.setLocationByPlatform( true );
            dlg.setVisible( true );
            
            if( !dlg.isAccepted() )
                return;
            
            for( int i = 0; i < getPlayers().length; i++ )
            {
                if( getPlayers()[i] != null )
                {
                    getPlayers()[i].unregister();
                    getPlayers()[i] = null;
                }
            }
            
            int rows = SettingInt.get( s, "newgame/rows" ).getValue();
            int columns = SettingInt.get( s, "newgame/columns" ).getValue();
            int winlength = SettingInt.get( s, "newgame/winlength" ).getValue();
            if( SettingBoolean.get( s, "newgame/mode/join" ).getValue() )
            {
                try
                {
                    getStatusBar().setText( tr( "Please wait while connection "
                                                + "is being established." ) );
                    String host = SettingString.get( s, "newgame/mode/join/host" )
                                  .getValue();
                    int port = SettingInt.get( s, "newgame/mode/join/port" )
                               .getValue();
                    String password = JOptionPane.showInputDialog(
                        tr( "Please enter host password:" ) );
                    final RemoteConnectFour game =
                        new RemoteConnectFour( host, port, password );
                    setGame( game );
                    game.addRemoteCfListener( onRemoteCfEventOccurred );              
                }
                catch( IOException ioe )
                {
                    JOptionPane.showMessageDialog( ConnectFour.getMainWindow(),
                        tr( "Network I/O error, game stopped: " )
                            + ioe.toString(),
                        tr( "Network error" ), JOptionPane.WARNING_MESSAGE );
                    ConnectFour.getGameView().setEnabled( false );
                }
                return;
            }
            else
                setGame( new LocalConnectFourEventEmitting( rows, columns,
                                                            winlength ) );
            LocalConnectFourEventEmitting game =
                (LocalConnectFourEventEmitting) getGame();
            
            getGameView().setGridSize( rows, columns,
                                       IConnectFour.Players.UNDEFINED );
            game.addMoveDoneListener( onMoveDone );
            
            if( SettingBoolean.get( s, "newgame/limitchips" ).getValue() )
                game.setMaxTurns(
                    2 * SettingInt.get( s, "newgame/chips" ).getValue() );
            else
                game.setMaxTurns( -1 );
            
            IPlayer[] players = new IPlayer[] {
                new HumanPlayer(
                    SettingString.get( s, "newgame/player1" ).getValue() ),
                null
            };
            if( SettingBoolean.get( s, "newgame/mode/human" ).getValue() )
                players[1] = new HumanPlayer(
                    SettingString.get( s, "newgame/player2" ).getValue() );
            else if( SettingBoolean.get( s, "newgame/mode/ai" ).getValue() )
                players[1] = new AiPlayer( game,
                    SettingInt.get( s, "newgame/mode/ai/recdepth" ).getValue(),
                    SettingDouble.get( s, "newgame/mode/ai/difficulty" )
                        .getValue() );
            else if( SettingBoolean.get( s, "newgame/mode/host" ).getValue() )
            {
                int port = SettingInt.get( s, "newgame/mode/host/port" )
                           .getValue();
                String password = SettingString.get(
                    s, "newgame/mode/host/password" ).getValue();
                try
                {
                    getStatusBar().setText( tr( "Waiting for player to join." ) );
                    RemoteCfPlayer remote = new RemoteCfPlayer(
                        game, game, port, password );
                    players[1] = new JoinedPlayer( remote );
                    setPlayers( players );
                    remote.setCallback( new RemoteCfPlayer.Callback() {
                        @Override
                        public void connectionEstablished(
                            RemoteCfPlayer source )
                        {
                            getPlayers()[getGame().getCurrentPlayer()
                                         .ordinal()].move();
                        }
                            
                        @Override
                        public void connectionClosed(
                            RemoteCfPlayer source )
                        {
                            if( !getGame().isGameFinished() )
                            {
                                JOptionPane.showMessageDialog(
                                    ConnectFour.getMainWindow(),
                                    tr( "Network I/O error, game stopped." ),
                                    tr( "Network error" ),
                                JOptionPane.WARNING_MESSAGE );
                                ConnectFour.getGameView().setEnabled( false );
                            }
                        }
                    } );             
                }
                catch( IOException ioe )
                {
                    JOptionPane.showMessageDialog(
                        ConnectFour.getMainWindow(),
                        tr( "Network I/O error, game stopped: " )
                            + ioe.toString(),
                        tr( "Network error" ), JOptionPane.WARNING_MESSAGE );
                    ConnectFour.getGameView().setEnabled( false );
                }
                return;
            }
            setPlayers( players );
            
            getPlayers()[game.getCurrentPlayer().ordinal()].move();
        }
    };
    
    final static private RemoteCfListener onRemoteCfEventOccurred =
        new RemoteCfAdapter() {
            @Override
            public void eventOccurred( RemoteCfEvent e )
            {
                RemoteConnectFour game = (RemoteConnectFour) getGame();
                switch( e.getType() )
                {
                    case GAME_CREATED:
                        getGameView().setGridSize(
                            game.getRows(), game.getColumns(),
                            IConnectFour.Players.UNDEFINED );
                        game.addMoveDoneListener( onMoveDone );
    
                        IPlayer[] players = new IPlayer[] {
                             new HumanPlayer(
                                 SettingString.get( s, "newgame/player1" ).
                                 getValue() ),
                             new HostingPlayer()
                        };
    
                        setPlayers( players );
    
                        getPlayers()[game.getCurrentPlayer().ordinal()].move();
                        break;
    
                    case ERROR_OCCURED:
                        switch( game.getError() )
                        {
                            case INVALID_PASSWORD:
                                getStatusBar().setText( tr( "Wrong password." ) );
                                JOptionPane.showMessageDialog(
                                    getMainWindow(), tr( "Wrong password."),
                                    tr( "Connection refused" ),
                                    JOptionPane.WARNING_MESSAGE );
                                break;
                            default:
                                if( getGame().isGameFinished() )
                                    break;
                                JOptionPane.showMessageDialog(
                                    getMainWindow(),
                                    tr( "Transmission error. Game has been "
                                        + "stopped." ),
                                    tr( "Network error" ),
                                    JOptionPane.WARNING_MESSAGE );
                                break;
                        }
                }
            }
        }; 
    
    private static ActionListener onUndoMoveClicked = new ActionListener() {   
        @Override
        public void actionPerformed( ActionEvent e )
        {
            if( getGame() == null )
                return;
            if( !getGame().isUndoPossible() )
            {
                JOptionPane.showMessageDialog( getMainWindow(),
                    tr( "It is not possible to undo a move at the moment." ),
                    tr( "Not possible" ), JOptionPane.WARNING_MESSAGE );
                return;
            }
            
            getPlayers()[getGame().getCurrentPlayer().ordinal()].unregister();
            
            int[] move = getGame().undo();
            getGameView().setField( move[0], move[1],
                                    IConnectFour.Players.UNDEFINED );
            if( getPlayers()[getGame().getCurrentPlayer().ordinal()]
                instanceof AiPlayer )
            {
                move = getGame().undo();
                getGameView().setField( move[0], move[1],
                                        IConnectFour.Players.UNDEFINED );
            }
            getPlayers()[getGame().getCurrentPlayer().ordinal()].move();
        }
    };
    
    
    ////////////////////////////////////////////////////////////////////////////
    // Simple Getters & Setters
    ////////////////////////////////////////////////////////////////////////////
    public static void setGame( IConnectFourEventEmitting game )
    {
        ConnectFour.game = game;
    }

    public static IConnectFourEventEmitting getGame()
    {
        return game;
    }
    
    public static void setGameView( GameGridView<IConnectFour.Players> gameView )
    {
        ConnectFour.gameView = gameView;
    }

    public static GameGridView<IConnectFour.Players> getGameView()
    {
        return gameView;
    }
    
    public static JFrame getMainWindow()
    {
        return mainWindow;
    }

    /** @param players has to be an array of length 2. */
    protected static void setPlayers( IPlayer[] players )
    {
        ConnectFour.players = players;
    }

    protected static IPlayer[] getPlayers()
    {
        return players;
    }

    protected static void setPlayersPainter(
        IPlayerPainter<IConnectFour.Players> playersPainter )
    {
        ConnectFour.playersPainter = playersPainter;
    }

    public static IPlayerPainter<IConnectFour.Players> getPlayersPainter()
    {
        return playersPainter;
    }

    public static JLabel getStatusBar()
    {
        return statusBar;
    }
    
    protected static void setTranslations( PropertyResourceBundle translations )
    {
        ConnectFour.translations = translations;
    }

    public static PropertyResourceBundle getTranslations()
    {
        return translations;
    }
}
