/* ConnectFour
 * File: JSettingSelectOne.java
 * Creation: 28.06.2009 18:10:42
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
import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;

import settings.SettingSelectOne;

/**
 * Displays a panel in which {@link JSettingSelectOneItem
 * JSettingSelectOneItems} should be inserted.
 */
public class JSettingSelectOne extends JSetting
{
    private static final long serialVersionUID = 1L;
    
    ////////////////////////////////////////////////////////////////////////////
    // Member Variables
    ////////////////////////////////////////////////////////////////////////////
    /** ButtonGroup containing the children. */
    private ButtonGroup buttonGroup;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////
    public JSettingSelectOne()
    {
        super();
    }

    public JSettingSelectOne( SettingSelectOne settingObj )
    {
        super( settingObj );
    }
    
    public JSettingSelectOne( SettingSelectOne settingsObj,
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
        setLayout( new BoxLayout( this, BoxLayout.Y_AXIS ) );
        
        ButtonGroup bg = new ButtonGroup();
        setButtonGroup( bg );
        for( JSetting child : getChildren() )
        {
            if( child instanceof JSettingSelectOneItem )
            {
                JRadioButton rb = ((JSettingSelectOneItem) child)
                                  .getRadioButton();
                bg.add( rb );
                add( rb );
            }
            add( child );
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Simple Getters & Setters
    ////////////////////////////////////////////////////////////////////////////
    protected void setButtonGroup( ButtonGroup buttonGroup )
    {
        this.buttonGroup = buttonGroup;
    }

    public ButtonGroup getButtonGroup()
    {
        return buttonGroup;
    }
}
