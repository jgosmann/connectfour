/* ConnectFour
 * File: PlayerPainter.java
 * Creation: 26.06.2009 08:43:32
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

package gamegui;

import java.awt.Graphics;

/**
 * Interface which is inherited by classes used to paint the discs of the
 * players in a game.
 */
public interface IPlayerPainter<PlayersType>
{
    /**
     * Paints one disc of a specified player at a specified position.
     * @param x Coordinate of left border.
     * @param y Coordinate of top border.
     * @param size Side length (width and height are equal).
     * @param player Player owning the discs.
     * @param g Graphics object to paint on.
     */
    public void paintPlayer( int x, int y, int size, PlayersType player,
                             Graphics g );
}
