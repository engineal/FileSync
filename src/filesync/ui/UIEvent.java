/*
 * Copyright (C) 2015 Aaron Lucia
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package filesync.ui;

import java.util.EventObject;

/**
 *
 * @author Aaron Lucia
 * @version Dec 19, 2014
 */
public class UIEvent extends EventObject {

    private final UIAction action;

    /**
     * Create a new UI event
     *
     * @param source object event originated from
     * @param action action that caused the event
     */
    public UIEvent(Object source, UIAction action) {
        super(source);
        this.action = action;
    }

    /**
     * Get the action that caused the event
     *
     * @return action that caused the event
     */
    public UIAction getAction() {
        return action;
    }
}
