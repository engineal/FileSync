/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filesync;

import java.util.EventListener;

/**
 *
 * @author Aaron Lucia
 * @version Dec 19, 2014
 */
public interface UIListener extends EventListener {

    public void actionPerformed(UIEvent event);
}
