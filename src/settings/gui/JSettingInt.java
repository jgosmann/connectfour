/* ConnectFour
 * File: JSettingInt.java
 * Creation: 28.06.2009 11:36:06
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
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import settings.SettingInt;

/**
 * Displays a Slider and Spinbox with a label for a {@link SettingInt
 * SettingInt}.
 */
public class JSettingInt extends JSetting
{
    private static final long serialVersionUID = 1L;
    
    ////////////////////////////////////////////////////////////////////////////
    // Member Variables
    ////////////////////////////////////////////////////////////////////////////
    /** Slider for setting the value. */
    private JSlider slider;
    /** Spinbox for setting the value. */
    private JSpinner spinner;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////
    public JSettingInt()
    {
        super();
    }

    public JSettingInt( SettingInt settingObj )
    {
        super( settingObj );
    }
    
    public JSettingInt( SettingInt settingsObj, ResourceBundle translations )
    {
        super( settingsObj, translations );
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // Methods
    ////////////////////////////////////////////////////////////////////////////
    @Override
    protected void display()
    {
        SettingInt s = (SettingInt) getSettingObj();
        
        setLayout( new BoxLayout( this, BoxLayout.X_AXIS ) );
        
        add( new JLabel( getLabel() ) );
        setSlider( new JSlider( s.getMin(), s.getMax(), s.getValue() ) );
        add( getSlider() );
        setSpinner( new JSpinner( new SpinnerNumberModel(
            s.getValue(), s.getMin(), s.getMax(), 1 ) ) );
        add( getSpinner() );
        
        getSpinner().addChangeListener( new ChangeListener() {
            public void stateChanged( ChangeEvent e )
            {
                getSlider().setValue( (Integer) getSpinner().getValue() );
            }
        } );
        getSlider().addChangeListener( new ChangeListener() {
            public void stateChanged( ChangeEvent e )
            {
                getSpinner().setValue( getSlider().getValue() );
            }
        } );
    }

    @Override
    public void refresh()
    {
        getSpinner().setValue( ((SettingInt) getSettingObj()).getValue() );
        super.refresh();
    }
    
    @Override
    public void store()
    {
        ((SettingInt) getSettingObj()).setValue( getSlider().getValue() );
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // Simple Getters & Setters
    ////////////////////////////////////////////////////////////////////////////
    protected void setSlider( JSlider slider )
    {
        this.slider = slider;
    }

    public JSlider getSlider()
    {
        return slider;
    }

    protected void setSpinner( JSpinner spinner )
    {
        this.spinner = spinner;
    }

    public JSpinner getSpinner()
    {
        return spinner;
    }
}
