/* ConnectFour
 * File: JSettingFilename.java
 * Creation: 28.06.2009 15:33:07
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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextField;

import settings.SettingFilename;

/**
 * Displays a input field and a browse button with a label for an
 * {@link SettingFilename SettingFilename}.
 */
public class JSettingFilename extends JSetting
{
    private static final long serialVersionUID = 1L;
    
    ////////////////////////////////////////////////////////////////////////////
    // Member Variables
    ////////////////////////////////////////////////////////////////////////////
    /** Text field containing the filename. */
    private JTextField filename;
    /** Button for browsing the filesystem. */
    private JButton browseButton;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////
    public JSettingFilename()
    {
        super();
    }

    public JSettingFilename( SettingFilename settingObj )
    {
        super( settingObj );
    }
    
    public JSettingFilename( SettingFilename settingsObj,
                             ResourceBundle translations )
    {
        super( settingsObj, translations );
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // Methods
    ////////////////////////////////////////////////////////////////////////////
    @Override
    protected void display()
    {
        SettingFilename s = (SettingFilename) getSettingObj();
        
        setLayout( new BoxLayout( this, BoxLayout.X_AXIS ) );
        
        add( new JLabel( getLabel() ) );
        setFilename( new JTextField( s.getValue() ) );
        add( getFilename() );
        setBrowseButton( new JButton( "..." ) );
        getBrowseButton().addActionListener( new ActionListener() {
            @Override
            public void actionPerformed( ActionEvent e )
            {
                JFileChooser fc = new JFileChooser( getFilename().getText() );
                if( fc.showOpenDialog( (JButton) e.getSource() )
                        == JFileChooser.APPROVE_OPTION )
                    getFilename().setText( fc.getSelectedFile().getPath() );
            }
        } );
        add( getBrowseButton() );
    }

    @Override
    public void refresh()
    {
        getFilename().setText( ((SettingFilename) getSettingObj()).getValue() );
        super.refresh();
    }
    
    @Override
    public void store()
    {
        ((SettingFilename) getSettingObj()).setValue( getFilename().getText() );
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // Simple Getters & Setters
    ////////////////////////////////////////////////////////////////////////////
    protected void setBrowseButton( JButton browseButton )
    {
        this.browseButton = browseButton;
    }

    public JButton getBrowseButton()
    {
        return browseButton;
    }
    
    protected void setFilename( JTextField filename )
    {
        this.filename = filename;
    }

    public JTextField getFilename()
    {
        return filename;
    }
}
