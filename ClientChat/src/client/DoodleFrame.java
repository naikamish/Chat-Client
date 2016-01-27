/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JFrame;

/**
 *
 * @author Amish Naik
 */
public class DoodleFrame extends JFrame{
    private DoodlePanel doodlePanel;
    private JButton sendButton;
    public DoodleFrame(){
        doodlePanel = new DoodlePanel();
        add(doodlePanel, BorderLayout.CENTER);
        
        sendButton = new JButton("Send");
        add(sendButton,BorderLayout.SOUTH);
        
        setSize(500,400);
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
}
