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
 * @version Dec 16, 2014
 */
public class StatusEvent extends EventObject {

    private final boolean syncing;
    private final double percent;

    public StatusEvent(Object source, boolean syncing, boolean paused) {
        super(source);
        this.syncing = syncing;
        this.percent = 0;
    }
    
    public StatusEvent(Object source, boolean syncing, double percent) {
        super(source);
        this.syncing = syncing;
        this.percent = percent;
    }

    public boolean isSyncing() {
        return syncing;
    }

    public double getPercent() {
        return percent;
    }
}
