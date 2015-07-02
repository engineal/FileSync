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

import filesync.UIAction;
import static filesync.UIAction.Sync;
import filesync.UIEvent;
import filesync.UIListener;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Aaron Lucia
 * @version Dec 18, 2014
 */
public class Console {

    private final List<UIListener> _uiListeners;
    private boolean gui;

    public Console(String[] args) {
        _uiListeners = new ArrayList<>();
        gui = args.length == 0;

        for (String arg : args) {
            switch (arg) {
                case "no-gui":
                    gui = false;
                    break;
                case "gui":
                    gui = true;
                    break;
                case "sync":
                    fireUIEvent(Sync);
                    break;
            }
        }
    }

    public boolean isGui() {
        return gui;
    }

    /**
     *
     * @param listener
     */
    public synchronized void addUIListener(UIListener listener) {
        if (!_uiListeners.contains(listener)) {
            _uiListeners.add(listener);
        }
    }

    /**
     *
     * @param listener
     */
    public synchronized void removeUIListener(UIListener listener) {
        _uiListeners.remove(listener);
    }

    /**
     *
     * @param status
     */
    private synchronized void fireUIEvent(UIAction action) {
        UIEvent event = new UIEvent(this, action);
        for (UIListener listener : _uiListeners) {
            listener.actionPerformed(event);
        }
    }
}
