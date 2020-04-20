/* ConnectFour
 * File: JSettingSelectOneItem.java
 * Creation: 28.06.2009 18:27:48
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

import java.awt.Dimension;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import settings.Setting;
import settings.SettingSelectOneItem;

/**
 * Displays it child items and generates a RadioButton displayed be a
 * {@link JSettingSelectOne JSettingSelectOne} item.
 */
public class JSettingSelectOneItem extends JSetting
{
    private static final long serialVersionUID = 1L;
    
    ////////////////////////////////////////////////////////////////////////////
    // Member Variables
    ////////////////////////////////////////////////////////////////////////////
    /** RadioButton for setting the value. */
    private JRadioButton radioButton;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////
    public JSettingSelectOneItem()
    {
        super();
    }

    public JSettingSelectOneItem( SettingSelectOneItem settingObj )
    {
        super( settingObj );
        initRadioButton();
    }
    
    public JSettingSelectOneItem( SettingSelectOneItem settingsObj,
                                  ResourceBundle translations )
    {
        super( settingsObj, translations );
        initRadioButton();
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // Methods
    ////////////////////////////////////////////////////////////////////////////
    @Override
    protected void display()
    {
        setLayout( new BoxLayout( this, BoxLayout.X_AXIS ) );
        add( Box.createRigidArea( new Dimension(25, 0) ) );

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout( new BoxLayout( mainPanel, BoxLayout.Y_AXIS ) );
        add( mainPanel );
        
        if( getChildren().size() > 0 )
        {
            mainPanel.setBorder( BorderFactory.createEtchedBorder(
                EtchedBorder.LOWERED ) );
        
            for( JSetting child : getChildren() )
                 mainPanel.add( child );
        }
        
        if( !getRadioButton().isSelected() )
            setVisible( false );
    }

    protected void initRadioButton()
    {
        SettingSelectOneItem s = (SettingSelectOneItem) getSettingObj();

        setRadioButton( new JRadioButton( getLabel() ) );
        getRadioButton().setSelected( s.getValue() );

        getRadioButton().addChangeListener( new ChangeListener() {
            @Override
            public void stateChanged( ChangeEvent e )
            {
                /* We hide the inactive components. setEnabled does not work
                 * recursively on the children of a component and would leave
                 * mostly everything enabled. Why - for god's sake - do I
                 * always get the feeling that Java is far from complete?
                 * Moreover this setEnabled-Bug was already reported in 1998!
                 * I suppose this will never get fixed ... :-@
                 */
                if( getRadioButton().isSelected() )
                    setVisible( true );
                else
                    setVisible( false );
            }
        } );
    }
    
    @Override
    public void refresh()
    {
        getRadioButton().setSelected(
            ((SettingSelectOneItem) getSettingObj()).getValue() );
        super.refresh();
    }
    
    @Override
    public void store()
    {
        ((SettingSelectOneItem) getSettingObj()).setValue(
            getRadioButton().isSelected() );
        super.store();
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // Simple Getters & Setters
    ////////////////////////////////////////////////////////////////////////////
    public void setRadioButton( JRadioButton radioButton )
    {
        this.radioButton = radioButton;
    }

    public JRadioButton getRadioButton()
    {
        return radioButton;
    }
    
    @Override
    public void setTranslations( ResourceBundle tr )
    {
        super.setTranslations( tr );
        getRadioButton().setText( getLabel() );
    }
    
    protected void setSettingObj( Setting settingObj )
    {
        super.setSettingObj( settingObj );
        initRadioButton();
    }
}
