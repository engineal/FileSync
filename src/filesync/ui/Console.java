/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filesync.ui;

import filesync.UIAction;
import static filesync.UIAction.Sync;
import filesync.UIEvent;
import filesync.UIListener;
import java.util.ArrayList;

/**
 *
 * @author Aaron Lucia
 * @version Dec 18, 2014
 */
public class Console {

    private final ArrayList<UIListener> _uiListeners;
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
