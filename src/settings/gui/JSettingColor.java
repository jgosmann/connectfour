/* ConnectFour
 * File: JSettingColor.java
 * Creation: 28.06.2009 17:38:24
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JColorChooser;

import settings.SettingColor;

/**
 * Displays a button with a label for a {@link SettingColor SettingColor} to
 * chose a color.
 */
public class JSettingColor extends JSetting
{
    private static final long serialVersionUID = 1L;
    
    ////////////////////////////////////////////////////////////////////////////
    // Member Variables
    ////////////////////////////////////////////////////////////////////////////
    /** Button for opening the color choser. */
    private JButton openColorChoserBtn;
    /** Icon indicating the color. */
    private ColorIcon icon;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////
    public JSettingColor()
    {
        super();
    }

    public JSettingColor( SettingColor settingObj )
    {
        super( settingObj );
    }
    
    public JSettingColor( SettingColor settingsObj,
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
        SettingColor s = (SettingColor) getSettingObj();
        
        setLayout( new BoxLayout( this, BoxLayout.X_AXIS ) );
        
        setIcon( new ColorIcon( s.getValue(), 24, 16 ) );
        setOpenColorChoserBtn( new JButton( getLabel(), getIcon() ) );
        getOpenColorChoserBtn().setIcon( getIcon() );
        getOpenColorChoserBtn().addActionListener(
            onOpenColorChoserButtonClick );
        add( getOpenColorChoserBtn() );
    }

    ActionListener onOpenColorChoserButtonClick = new ActionListener() {
        @Override
        public void actionPerformed( ActionEvent e )
        {
            Color newColor = JColorChooser.showDialog(
                (JButton) e.getSource(), getLabel(), getIcon().getColor() );
            if( newColor != null )
                getIcon().setColor( newColor );
        }
    };
    
    @Override
    public void refresh()
    {
        getIcon().setColor( ((SettingColor) getSettingObj()).getValue() );
        super.refresh();
    }
    
    @Override
    public void store()
    {
        ((SettingColor) getSettingObj()).setValue( getIcon().getColor() );
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // Simple Getters & Setters
    ////////////////////////////////////////////////////////////////////////////
    protected void setIcon( ColorIcon icon )
    {
        this.icon = icon;
    }

    public ColorIcon getIcon()
    {
        return icon;
    }
    
    public void setOpenColorChoserBtn( JButton openColorChoserBtn )
    {
        this.openColorChoserBtn = openColorChoserBtn;
    }

    public JButton getOpenColorChoserBtn()
    {
        return openColorChoserBtn;
    }
    
    
    public void setEnabled( boolean enabled )
    {
        getOpenColorChoserBtn().setEnabled( enabled );
    }
}
