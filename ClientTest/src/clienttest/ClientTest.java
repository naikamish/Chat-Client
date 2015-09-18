/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clienttest;

/**
 *
 * @author Amish Naik
 */
import javax.swing.JFrame;

public class ClientTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Client myClient;
        myClient = new Client();
        myClient.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myClient.startRunning();
        
    }
    
}
