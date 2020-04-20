/* ConnectFour
 * File: JSettingDialog.java
 * Creation: 30.06.2009 15:27:22
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

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.MissingResourceException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 * Wraps a {@link JSetting JSetting} tree into a JDialog. The buttons like
 * "Ok" will be translated with the same translation used for the JSetting
 * object passed to the constructor.
 */
public class JSettingDialog extends JDialog
{
    private static final long serialVersionUID = 1L;

    ////////////////////////////////////////////////////////////////////////////
    // Member Variables
    ////////////////////////////////////////////////////////////////////////////
    /** Accept button */
    JButton accept;
    /** Cancel button */
    JButton cancel;
    /** Restore Defaults button */
    JButton restore;
    /** Apply Button */
    JButton apply;
    
    /** Was the dialog accepted? */
    private boolean accepted = false;
    
    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////
    public JSettingDialog( JSetting settings )
    {
        super();
        initialize( settings );
    }
    
    public JSettingDialog( JSetting settings, Dialog owner )
    {
        super( owner );
        initialize( settings );
    }
    
    public JSettingDialog( JSetting settings, Dialog owner, boolean modal )
    {
        super( owner, modal );
        initialize( settings );
    }
    
    public JSettingDialog( JSetting settings, Dialog owner, String title )
    {
        super( owner, title );
        initialize( settings );
    }
    
    public JSettingDialog( JSetting settings, Dialog owner, String title,
                           boolean modal )
    {
        super( owner, title, modal );
        initialize( settings );
    }
    
    public JSettingDialog( JSetting settings, Frame owner )
    {
        super( owner );
        initialize( settings );
    }
    
    public JSettingDialog( JSetting settings, Frame owner, boolean modal )
    {
        super( owner, modal );
        initialize( settings );
    }
    
    public JSettingDialog( JSetting settings, Frame owner, String title )
    {
        super( owner, title );
        initialize( settings );
    }
    
    public JSettingDialog( JSetting settings, Frame owner, String title,
                           boolean modal )
    {
        super( owner, title, modal );
        initialize( settings );
    }
    
    public JSettingDialog( JSetting settings, Frame owner, String title,
                           boolean modal, GraphicsConfiguration gc )
    {
        super( owner, title, modal, gc );
        initialize( settings );
    }
    
    public JSettingDialog( JSetting settings, Window owner )
    {
        super( owner );
        initialize( settings );
    }
    
    public JSettingDialog( JSetting settings, Window owner,
                           Dialog.ModalityType modalityType )
    {
        super( owner, modalityType );
        initialize( settings );
    }
    
    public JSettingDialog( JSetting settings, Window owner, String title )
    {
        super( owner, title );
        initialize( settings );
    }
    
    public JSettingDialog( JSetting settings, Window owner, String title,
                    Dialog.ModalityType modalityType )
    {
        super( owner, title, modalityType );
        initialize( settings );
    }
    
    public JSettingDialog( JSetting settings, Window owner, String title,
                           Dialog.ModalityType modalityType,
                           GraphicsConfiguration gc )
    {
        super( owner, title, modalityType, gc );
        initialize( settings );
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Initializes the GUI of the dialog. Uses the translations of the passed
     * settings also for the buttons.
     * @param settings JSetting GUI structure to display as dialog.
     */
    private void initialize( final JSetting settings )
    {
        setLayout( new BorderLayout() );

        settings.setBorder( new EmptyBorder( 10, 10, 10, 10 ) );
        add( settings, BorderLayout.CENTER );
        
        String okLabel = "Ok";
        String cancelLabel = "Cancel";
        String restoreLabel = "Restore Defaults";
        String applyLabel = "Apply";
        try
        {
            if( settings.getTranslations() != null )
            {
                okLabel = settings.getTranslations().getString( okLabel );
                cancelLabel = settings.getTranslations().getString(
                    cancelLabel );
                restoreLabel = settings.getTranslations().getString(
                    restoreLabel );
                applyLabel = settings.getTranslations().getString( applyLabel );
            }
        }
        catch( MissingResourceException e )
        {   
        }
        
        JPanel buttonPanel = new JPanel();
        FlowLayout fl = new FlowLayout();
        fl.setAlignment( FlowLayout.RIGHT );
        buttonPanel.setLayout( fl );
        accept = new JButton( okLabel );
        cancel = new JButton( cancelLabel );
        restore = new JButton( restoreLabel );
        apply = new JButton( applyLabel );
        buttonPanel.add( accept );
        buttonPanel.add( cancel );
        buttonPanel.add( restore );
        buttonPanel.add( apply );
        add( buttonPanel, BorderLayout.SOUTH );
        
        accept.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed( ActionEvent e )
            {
                settings.store();
                setAccepted( true );
                setVisible( false );
            }
        } );
        
        cancel.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed( ActionEvent e )
            {
                setAccepted( false );
                setVisible( false );
            }
        } );
        
        restore.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed( ActionEvent e )
            {
                settings.getSettingObj().restoreDefaults();
                settings.refresh();
            }
        } );
        
        apply.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed( ActionEvent e )
            {
                settings.store();
            }
        } );
        
        getRootPane().setDefaultButton( accept );
        pack();
        setMinimumSize( getSize() );
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // Simple Getter & Setter
    ////////////////////////////////////////////////////////////////////////////
    protected void setAccepted( boolean accepted )
    {
        this.accepted = accepted;
    }

    public boolean isAccepted()
    {
        return accepted;
    }
    
    /**
     * Sets the visibility of the apply button.
     * @param visible Visibility will be set tot the passed value.
     */
    public void setApplyVisibility( boolean visible )
    {
        apply.setVisible( visible );
    }
    
    /**
     * Sets the visibility of the restore defaults button.
     * @param visible Visibility will be set to the passed value.
     */
    public void setRestoreDefaultsVisibility( boolean visible )
    {
        restore.setVisible( visible );
    }
}
