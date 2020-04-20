/* ConnectFour
 * File: JSettingDouble.java
 * Creation: 28.06.2009 17:02:24
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

import settings.SettingDouble;

/**
 * Displays a Slider and Spinbox with a label for a {@link SettingDouble
 * SettingDouble}.
 */
public class JSettingDouble extends JSetting
{
    private static final long serialVersionUID = 1L;
    
    ////////////////////////////////////////////////////////////////////////////
    // Member Variables
    ////////////////////////////////////////////////////////////////////////////
    /** Slider for setting the value. */
    private JSlider slider;
    /** Spinbox for setting the value. */
    private JSpinner spinner;
    
    /**
     * To prevent endless recursion in the connection between the slider and the
     * spinner.
     */
    private boolean sliderJustChanged = false, spinnerJustChanged = false; 

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////
    public JSettingDouble()
    {
        super();
    }

    public JSettingDouble( SettingDouble settingObj )
    {
        super( settingObj );
    }
    
    public JSettingDouble( SettingDouble settingsObj,
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
        final SettingDouble s = (SettingDouble) getSettingObj();
        
        setLayout( new BoxLayout( this, BoxLayout.X_AXIS ) );
        
        add( new JLabel( getLabel() ) );
        setSlider( new JSlider( 0, Integer.MAX_VALUE, 0 ) );
        add( getSlider() );
        setSpinner( new JSpinner( new SpinnerNumberModel(
            s.getValue(), s.getMin(), s.getMax(),
            (s.getMax() - s.getMin()) / 100.0 ) ) );
        add( getSpinner() );
        
        ChangeListener spinnerChanged = new ChangeListener() {
            public void stateChanged( ChangeEvent e )
            {
                if( spinnerJustChanged )
                    return;
                spinnerJustChanged = true;
                int value =
                    (int) ( ( ((Double) getSpinner().getValue()) - s.getMin() )
                    / (s.getMax() - s.getMin()) * Integer.MAX_VALUE );
                if( value == getSlider().getValue() )
                    return;
                if( value < getSlider().getMinimum() )
                    value = getSlider().getMinimum();
                else if( value > getSlider().getMaximum() )
                    value = getSlider().getMaximum();
                getSlider().setValue( value );
                spinnerJustChanged = false;
            }
        };
        getSpinner().addChangeListener( spinnerChanged );
        spinnerChanged.stateChanged( new ChangeEvent( getSpinner() ) );
        getSlider().addChangeListener( new ChangeListener() {
            public void stateChanged( ChangeEvent e )
            {
                if( sliderJustChanged )
                    return;
                sliderJustChanged = true;
                double value =
                    ((double) getSlider().getValue()) / Integer.MAX_VALUE
                    * (s.getMax() - s.getMin()) + s.getMin();
                if( value < s.getMin() )
                    value = s.getMin();
                else if( value > s.getMax() )
                    value = s.getMax();
                getSpinner().setValue(
                    ((double) getSlider().getValue()) / Integer.MAX_VALUE
                    * (s.getMax() - s.getMin()) + s.getMin() );
                sliderJustChanged = false;
            }
        } );
  
    }

    @Override
    public void refresh()
    {
        double value = ((SettingDouble) getSettingObj()).getValue();
        getSpinner().setValue( value );
        super.refresh();
    }
    
    @Override
    public void store()
    {
        ((SettingDouble) getSettingObj()).setValue(
            (Double) getSpinner().getValue() );
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
