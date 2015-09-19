/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servertest;

import javax.swing.Timer;

/**
 *
 * @author Amish Naik
 */
public class ServerThread implements Runnable{
    
    @Override
    public void run() {
        try{
            Thread.sleep(100);
        }
        catch(Exception e){}
    }
    
}
