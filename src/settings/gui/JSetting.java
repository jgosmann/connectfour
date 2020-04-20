/* ConnectFour
 * File: JSetting.java
 * Creation: 28.06.2009 10:31:13
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

import java.util.Collection;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.TreeSet;

import javax.swing.BoxLayout;
import javax.swing.JComponent;

import settings.Setting;

/**
 * Creates GUI elements from a {@link Setting Setting} object. Before using
 * this class {@link SettingGuiLookup SettingGuiLookup} has to be initialized.
 * See {@link SettingGuiEmptyInitializer SettingGuiEmptyInitializer} and
 * {@link SettingGuiBaseInitializer SettingGuiBaseInitializer} for this task.
 * GUI elements for new classes derived from Setting should derive this class
 * and need to add this class to {@link SettingGuiLookup SettingGuiLookup}.
 */
public class JSetting extends JComponent implements Comparable<JSetting>
{
    private static final long serialVersionUID = 1L;

    ////////////////////////////////////////////////////////////////////////////
    // Member Variables
    ////////////////////////////////////////////////////////////////////////////
    /** Represented {@link Setting Setting} object. */
    private Setting settingObj = null;
    
    /** Translations for the keys */
    private ResourceBundle translations = null;
    
    /** Children GUI elements. */
    private Collection<JSetting> children = null;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Empty constructor needed to create instances with Class.newInstance().
     * When using this constructor {@link #setSettingObj(Setting)
     * setSettingsObj()} has to be called next.
     */
    public JSetting()
    {
        
    }
    
    /**
     * Shorthand for {@link #JSetting(Setting, ResourceBundle)
     * JSetting( settingObj, null )}.
     */
    public JSetting( Setting settingObj )
    {
        this( settingObj, null );
    }
    
    /**
     * Normal constructor you should use.
     * @param settingObj Setting object to represent.
     * @param translations Gives the translations for the keys.
     */
    public JSetting( Setting settingObj, ResourceBundle translations )
    {
        setSettingObj( settingObj );
        setTranslations( translations );
        display();
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // Methods
    ////////////////////////////////////////////////////////////////////////////
    public int compareTo( JSetting o )
    {
        return getSettingObj().compareTo( o.getSettingObj() );
    }
    
    /**
     * Reads the children of settingsObj and creates JSetting children for
     * this object.
     */
    private void createChildren()
    {
        if( getSettingObj() == null )
            return;
        
        children = new TreeSet<JSetting>();
        for( Setting setting : getSettingObj().getChildren() )
        {   
            try
            {
                JSetting child = SettingGuiLookup.getInstance().lookup(
                                     setting.getClass() ).newInstance();
                if( child == null )
                {
                    System.err.println( "JSetting warning: Unregistered class: "
                                        + setting.getClass().toString() );
                    continue;
                }
                child.setSettingObj( setting );
                child.setTranslations( getTranslations() );
                child.setAlignmentX( JComponent.LEFT_ALIGNMENT );
                child.display();
                getChildren().add( child );
            }
            catch( Exception e )
            {
                System.err.println( "Ignoring JSetting Exception: "
                                    + e.toString() );
            }     
        }
    }
    
    /**
     * This method should be overridden in derived classes and creates all GUI
     * elements.
     */
    protected void display()
    {
        setLayout( new BoxLayout( this, BoxLayout.Y_AXIS ) );
        displayChildren();
    }
    
    /**
     * Creates the GUI elements for all children of this object and fills
     * {@link #children children}.
     */
    protected void displayChildren()
    {
        for( JSetting child : getChildren() )
            add( child );
    }
    
    /**
     * Refreshes the displayed values and calls this function recursivly for all
     * children. Should be called after changing the Setting object in the
     * background.
     * Deriving classes should overwrite this function.
     */
    public void refresh()
    {
        for( JSetting child : getChildren() )
            child.refresh();
    }
    
    /**
     * Stores the values set in the GUI back to the {@link Setting Setting}
     * objects.
     * Deriving classes should overwrite this function.
     */
    public void store()
    {
        for( JSetting child : getChildren() )
            child.store();
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // Simple Getters & Setters
    ////////////////////////////////////////////////////////////////////////////
    public Collection<JSetting> getChildren()
    {
        if( children == null )
            createChildren();
        return children;
    }
    
    protected void setSettingObj( Setting settingObj )
    {
        this.settingObj = settingObj;
    }
    
    public Setting getSettingObj()
    {
        return settingObj;
    }
    

    public void setTranslations( ResourceBundle translations )
    {
        this.translations = translations;
        for( JSetting child : getChildren() )
            child.setTranslations( translations );
    }
    

    public ResourceBundle getTranslations()
    {
        return translations;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // Advanced Getters
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Searches for a child node.
     * @param key The key in the {@link Setting Setting} object of the child
     *     node.
     * @return Returns the child node or null if no node with the key was found.
     */
    public JSetting getChild( String key )
    {
        if( key == getSettingObj().getKey() )
            return this;
        for( JSetting child : getChildren() )
        {
            JSetting ret = child.getChild( key );
            if( ret != null )
                return ret;
        }
        return null;
    }
    
    /**
     * @return Returns the translated key which can be used as label in the GUI.
     */
    public String getLabel()
    {
        Setting s = getSettingObj();
        String label = s.getKey();
        try
        {
            if( getTranslations() != null )
                label = getTranslations().getString( s.getKey() );
        }
        catch( MissingResourceException e )
        {   
        }
        return label;
    }
}
