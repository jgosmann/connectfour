/* ConnectFour
 * File: JSettingBoolean.java
 * Creation: 28.06.2009 17:21:58
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
import javax.swing.JCheckBox;

import settings.SettingBoolean;

/**
 * Displays a Checkbox with a label for a {@link SettingBoolean
 * SettingBoolean}.
 */
public class JSettingBoolean extends JSetting
{
    private static final long serialVersionUID = 1L;
    
    ////////////////////////////////////////////////////////////////////////////
    // Member Variables
    ////////////////////////////////////////////////////////////////////////////
    /** Checkbox for setting the value. */
    private JCheckBox checkbox;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////
    public JSettingBoolean()
    {
        super();
    }

    public JSettingBoolean( SettingBoolean settingObj )
    {
        super( settingObj );
    }
    
    public JSettingBoolean( SettingBoolean settingsObj,
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
        SettingBoolean s = (SettingBoolean) getSettingObj();
        
        setLayout( new BoxLayout( this, BoxLayout.X_AXIS ) );
                
        setCheckbox( new JCheckBox( getLabel() ) );
        getCheckbox().setSelected( s.getValue() );
        add( getCheckbox() );
    }

    @Override
    public void refresh()
    {
        getCheckbox().setSelected(
            ((SettingBoolean) getSettingObj()).getValue() );
        super.refresh();
    }
    
    @Override
    public void store()
    {
        ((SettingBoolean) getSettingObj()).setValue(
            getCheckbox().isSelected() );
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // Simple Getters & Setters
    ////////////////////////////////////////////////////////////////////////////
    protected void setCheckbox( JCheckBox checkbox )
    {
        this.checkbox = checkbox;
    }

    public JCheckBox getCheckbox()
    {
        return checkbox;
    }
}
