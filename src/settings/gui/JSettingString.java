/* ConnectFour
 * File: JSettingString.java
 * Creation: 07.07.2009 14:43:15
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

import java.util.ResourceBundle;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;

import settings.SettingString;

/**
 * Displays a input field with a label for a {@link SettingString SettingString}.
 */
public class JSettingString extends JSetting
{
    private static final long serialVersionUID = 1L;
    
    ////////////////////////////////////////////////////////////////////////////
    // Member Variables
    ////////////////////////////////////////////////////////////////////////////
    /** Text field containing the filename. */
    private JTextField string;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////
    public JSettingString()
    {
        super();
    }

    public JSettingString( SettingString settingObj )
    {
        super( settingObj );
    }
    
    public JSettingString( SettingString settingsObj,
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
        SettingString s = (SettingString) getSettingObj();
        
        setLayout( new BoxLayout( this, BoxLayout.X_AXIS ) );
        
        add( new JLabel( getLabel() ) );
        setString( new JTextField( s.getValue() ) );
        add( getString() );
    }

    @Override
    public void refresh()
    {
        getString().setText( ((SettingString) getSettingObj()).getValue() );
        super.refresh();
    }
    
    @Override
    public void store()
    {
        ((SettingString) getSettingObj()).setValue( getString().getText() );
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // Simple Getters & Setters
    ////////////////////////////////////////////////////////////////////////////    
    protected void setString( JTextField string )
    {
        this.string = string;
    }

    public JTextField getString()
    {
        return string;
    }
}