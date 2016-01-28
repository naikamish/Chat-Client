/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.GeneralPath;
import java.util.LinkedList;
import javax.swing.JButton;
import javax.swing.JFrame;
import message.Message;

/**
 *
 * @author Amish Naik
 */
public class DoodleFrame extends JFrame{
    private DoodlePanel doodlePanel;
    private JButton sendButton;
    private ClientChat clientChat;
    public DoodleFrame(ClientChat clientChat){
        this.clientChat = clientChat;
        doodlePanel = new DoodlePanel();
        add(doodlePanel, BorderLayout.CENTER);
        
        sendButton = new JButton("Send");
        add(sendButton,BorderLayout.SOUTH);
        
        sendButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                clientChat.sendDoodle(doodlePanel.storedPaths);
                dispose();
            }
            
        });
        
        setSize(500,400);
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
    
    public void setDoodle(LinkedList<GeneralPath> doodle){
        doodlePanel.setDoodle(doodle);
    }
}
