/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servertest;

import javax.swing.JFrame;

/**
 *
 * @author Amish Naik
 */
public class ServerTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Server myServer = new Server();
        myServer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myServer.startRunning();
    }
    
}
