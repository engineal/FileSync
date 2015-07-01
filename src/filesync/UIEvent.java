/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package filesync;

import java.util.EventObject;

/**
 *
 * @author Aaron Lucia
 * @version Dec 19, 2014
 */
public class UIEvent extends EventObject {
    private final UIAction action;

    public UIEvent(Object source, UIAction action) {
        super(source);
        this.action = action;
    }

    public UIAction getAction() {
        return action;
    }
}
