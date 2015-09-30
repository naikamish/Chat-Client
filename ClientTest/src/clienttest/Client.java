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
import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.*;

public class Client extends JFrame{
    
  //  private JTextField userText;
    private ClientChat chatWindow;
    
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private String message = "";
    private String serverIP;
 //   private Socket connection;
    private String clientName;
    private String groupName;
   // private ArrayList<String> data;
    private String[] data;
    private DefaultListModel listModel = new DefaultListModel();
    
    Connection connection;

    
    public Client(){
        super("Client Chat");
       /* userText = new JTextField();
        userText.setEditable(false);
        userText.addActionListener(
            new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    sendMessage(clientName + " - " + e.getActionCommand());
                    userText.setText("");
                }
            }
        );
        add(userText, BorderLayout.NORTH);
        
        chatWindow = new JTextArea();
        chatWindow.setEditable(false);
        add(new JScrollPane(chatWindow), BorderLayout.CENTER);
      */ 
        String myip="";
        try{
        myip = InetAddress.getLocalHost().getHostAddress();
        }
        catch(Exception e){}

        serverIP = JOptionPane.showInputDialog(this, "What IP do you want to connect to", myip+"");
        
        //groupName = JOptionPane.showInputDialog(this, "What Group do you want to connect to?", "group1");

        JList groupsList = new JList(listModel);
        add(new JScrollPane(groupsList), BorderLayout.CENTER);
        
        JButton connectButton = new JButton("Connect");
        add(connectButton,BorderLayout.SOUTH);
        
        connectButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage(groupsList.getSelectedValue().toString());
                chatWindow = new ClientChat(connection, groupsList.getSelectedValue().toString());
             //   chatWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            }
            
        });
        
        setSize(10,200);
        setVisible(true);
    }
    
    public void startRunning(){
        try{
           // listModel.addElement("item1");
            connectToServer();
            getGroupList();
            whileChatting();
        }
        catch(EOFException eofException){
            //showMessage("\nClient terminated connection");
        }
        catch(IOException ioException){
            ioException.printStackTrace();
           // showMessage(ioException+"");
        }
        finally{
            closeStreams();
        }
    }
    
    //Connect to Server
    private void connectToServer() throws IOException{
        connection = new Connection(new Socket(InetAddress.getByName(serverIP),5000));
    }
    
    private void getGroupList(){
        try{
        message = (String) connection.input.readObject();
        //data = new ArrayList(Arrays.asList(message.split(" , ")));
        data = message.split(",");
          // String[] data = {"1","2","3"};
        
        for(String s : data){
        listModel.addElement(s);
    }
        }
        catch(Exception e){}
    }
    
    private void whileChatting() throws IOException{
        ableToType(true);
        do{
            try{
                message = (String) input.readObject();
                showMessage(message);
            }
            catch(Exception e){
               // showMessage(e+"");
            }
        }
        while(!message.equals("SERVER - END"));
    }
    
    private void closeStreams(){
        showMessage("\nClosing streams...");
        ableToType(false);
        connection.close();
    }
    
    private void sendMessage(String message){
        connection.sendMessage(message);
    }
    
    //Update chat window
    private void showMessage(final String text){
        SwingUtilities.invokeLater(
            new Runnable(){
                public void run(){
                  //  chatWindow.append(text);
                }
            }
        );
    }
    
    private void ableToType(final boolean tof){
        SwingUtilities.invokeLater(
            new Runnable(){
                public void run(){
                  //  userText.setEditable(tof);
                }
            }
        );
    }
}
