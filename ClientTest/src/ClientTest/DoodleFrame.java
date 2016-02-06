/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClientTest;

import message.DoodlePath;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.GeneralPath;
import java.util.LinkedList;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import message.Message;

/**
 *
 * @author Amish Naik
 */
public class DoodleFrame extends JFrame{
    private DoodlePanel doodlePanel;
    private JButton sendButton, colorPickerButton;
    private JPanel optionsPanel;
    private SpinnerNumberModel doodleSize;
    private JSpinner doodleSizeSpinner;
    private ChatWindowController clientChat;
    public DoodleFrame(ChatWindowController clientChat){
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
        
        colorPickerButton = new JButton("Change Color");
        colorPickerButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                Color color = JColorChooser.showDialog(rootPane, null, Color.BLACK);
                doodlePanel.setActiveColor(color);
            }
            
        });
        
        doodleSize = new SpinnerNumberModel(3, 1, 20, 1); //initial, min, max, step
        doodleSizeSpinner = new JSpinner(doodleSize);
        doodleSizeSpinner.addChangeListener(new ChangeListener(){

            @Override
            public void stateChanged(ChangeEvent e) {
                doodlePanel.setBrushSize((Integer)doodleSizeSpinner.getValue());
            }
            
        });
        
        optionsPanel = new JPanel();
        optionsPanel.add(colorPickerButton);
        optionsPanel.add(new JLabel("Brush Size:"));
        optionsPanel.add(doodleSizeSpinner);
        add(optionsPanel, BorderLayout.NORTH);
        
        
        setSize(500,400);
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
    
    public void setDoodle(LinkedList<DoodlePath> doodle){
        doodlePanel.setDoodle(doodle);
    }
}
