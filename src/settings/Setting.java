/* ConnectFour
 * File: Settings.java
 * Creation: 27.06.2009 15:46:50
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

package settings;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * This class manages settings in a tree structure. You can use path-like keys
 * to access the elements: "key1/key2/key3".
 */
public class Setting implements Comparable<Setting>
{
    ////////////////////////////////////////////////////////////////////////////
    // Member Variables
    ////////////////////////////////////////////////////////////////////////////
    /** Key to access the setting. */
    private final String key;
    
    /** Subnodes of the setting. */
    private Hashtable<String, Setting> children =
        new Hashtable<String, Setting>();

    /** Index for ordering. */
    private int index;
    
    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////
    /** Shorthand for {@link #Setting(String, int) Setting(key, 0)}. */
    public Setting( String key )
    {
        this( key, 0 );
    }
    
    /**
     * @param key Key to access the setting.
     * @param index Index for ordering.
     */
    public Setting( String key, int index )
    {
        this.key = key;
        setIndex( index );
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // Methods
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Adds a new node to the SettingsTree. {@link Setting#key node.key} is used
     * as key.
     * @param node Node to add.
     * @throws InvalidKeyException if key is invalid.
     */
    public void add( Setting node )
    {
        add( "", node.getKey(), node );
    }
    
    /**
     * Adds a new node to the SettingsTree.
     * @param path Key of the current position in the tree.
     * @param key Key for the new node. May be a path with the slash as
     *     separator.
     * @param node Node to add.
     * @throws InvalidKeyException if key is invalid.
     */
    protected void add( String path, String key, Setting node )
    {
        String[] splitted = cutOffNextSubkey( key );
        if( splitted[0].length() == 0 )
            throw new InvalidKeyException( path + key );
        
        path += "/" + splitted[0];
        
        if( splitted[1].length() > 0 )
        {
            Setting child = getChild( splitted[0] );
            if( child == null )
            {
                child = new Setting( path );
                addChild( splitted[0], child );
            }
            
            child.add( path, splitted[1], node );
        }   
        else
            addChild( splitted[0], node );
    }

    /**
     * Adds a direct child.
     * @param key Key for the child to add, may not contain slashes.
     * @param child Object to add as child.
     */
    protected void addChild( String key, Setting child )
    {
        children.put( key, child );
    }
    
    /** Compares the index with the index of the passed Setting object. */
    @Override
    public int compareTo( Setting o )
    {
        if( getIndex() != o.getIndex() )
            return getIndex() - o.getIndex();
        return getKey().compareTo( o.getKey() );
    }
    
    /**
     * Splits the key string at the next sequence of slashes and returns an
     * array of the length two with the two elements. The slashes will be cut
     * out. 
     * @param key Key to split.
     * @return Array containing the two splitted parts.
     */
    protected String[] cutOffNextSubkey( String key )
    {
        String nextSubkey = "";
        while( nextSubkey.length() == 0 )
        {
            int subkeyLength = key.indexOf( "/" );
            if( subkeyLength < 0 )
                subkeyLength = key.length();
            nextSubkey = key.substring( 0, subkeyLength );
            if( subkeyLength < key.length() )
              key = key.substring( subkeyLength + 1 );
            else
              key = "";
        }
        return new String[] { nextSubkey, key };
    }
    
    /**
     * Retrieves a stored {@link Setting Setting} object. Use this function
     * also to retrieve settings to edit them.
     * @param key Key of the Setting object. 
     * @return The retrieved Setting object or null, if the key is invalid or
     *     non-existent.
     * @throws InvalidKeyException if key is invalid.
     */
    public Setting get( String key )
    {
        String[] splitted = cutOffNextSubkey( key );
        if( splitted[1].length() > 0 )
        {
            Setting child = getChild( splitted[0] );
            if( child != null )
                return child.get( splitted[1] );
            else
                return null;
        }   
        else if( splitted[0].length() > 0 )
                return getChild( splitted[0] );
        else
            throw new InvalidKeyException( key );
    }
    
    /**
     * Removes the node with the specified key from the tree. If the key does
     * not exists, the tree will not be changed.
     * @param key Key of node to remove, may be a path containing slashes.
     * @throws InvalidKeyException if the key is invalid.
     */
    public void remove( String key )
    {
        String[] splitted = cutOffNextSubkey( key );
        if( splitted[0].length() == 0 )
            throw new InvalidKeyException( key );
        
        if( splitted[1].length() > 0 )
        {
            Setting child = getChild( splitted[0] );
            if( child != null )
                child.remove( splitted[1] );
        }   
        else
            removeChild( splitted[0] );
    }
    
    /**
     * Removes a direct subnode. Does nothing if the passed key does not exists.
     * @param key Key of child to remove. Must not contain slashes.
     */
    protected void removeChild( String key )
    {
        children.remove( key );
    }
    
    /**
     * Restores the default value of the node and its subnodes. Should be
     * overridden by derived classes as needed. */
    public void restoreDefaults()
    {
        for( Setting child : getChildren() )
            child.restoreDefaults();
    }
    
    
    ////////////////////////////////////////////////////////////////////////////
    // Getters & Setters
    ////////////////////////////////////////////////////////////////////////////
    public String getKey()
    {
        return key;
    }
    
    public void setIndex( int index )
    {
        this.index = index;
    }

    public int getIndex()
    {
        return index;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // Advanced Getters
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Retrieves a top-level {@link Setting Setting} object or in other words
     * a direct child of this object. (But it is not possible to retrieve
     * subnodes of these childs with this function, use the {@link #get(String)
     * get()} function for this purpose instead.)
     * @param key Key of the child to retrieve. This key cannot be a path like
     *     in {@link #get(String) get()}.
     * @return Returns the child node with the key or null if no node with this
     *     key exists.
     */
    public Setting getChild( String key )
    {
        return children.get( key );
    }
    
    /** @return Returns all direct children. */
    public Collection<Setting> getChildren()
    {
        return children.values();
    }

    /** @return Returns all keys of the direct children. */
    public Enumeration<String> getKeys()
    {
        return children.keys();
    }
}
