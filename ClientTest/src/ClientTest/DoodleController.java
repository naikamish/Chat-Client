/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClientTest;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javax.swing.JColorChooser;

/**
 * FXML Controller class
 *
 * @author Amish Naik
 */
public class DoodleController implements Initializable {

    @FXML private Canvas doodleCanvas;
    @FXML private Button sendButton;
    @FXML private TextField sizeTextField;
    
    ChatWindowController chatWindowController;
    GraphicsContext graphicsContext;
    int strokeWidth;
    
    /**
     * Initializes the controller class.
     */
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Image doodleImage = new Image(ClientTest.class.getResourceAsStream("images/doodle.png"));
        //saveButton.setImage(doodleImage);
        sizeTextField.setText("5");
        graphicsContext = doodleCanvas.getGraphicsContext2D();
        initDraw(graphicsContext);
    }   
    
    public void setChatWindow(ChatWindowController controller){
        chatWindowController = controller; 
    }
    
    @FXML
    private void mouseDown(MouseEvent event) {
        graphicsContext.beginPath();
        graphicsContext.moveTo(event.getX(), event.getY());
        graphicsContext.stroke();
    }
    
    @FXML
    private void mouseDragged(MouseEvent event) {
        graphicsContext.lineTo(event.getX(), event.getY());
        graphicsContext.stroke();
    }
    
    @FXML
    private void mouseUp(MouseEvent event) {
        
    }
    
    @FXML
    private void setStrokeWidth(KeyEvent event) {
        strokeWidth = Integer.parseInt(sizeTextField.getText());
        graphicsContext.setLineWidth(strokeWidth);
    }
    
    private void initDraw(GraphicsContext gc){
        double canvasWidth = gc.getCanvas().getWidth();
        double canvasHeight = gc.getCanvas().getHeight();
        System.out.println(canvasWidth+"w"+canvasHeight+"h");
        gc.setFill(Color.WHITE);
        gc.fillRect(0,0,canvasWidth,canvasHeight); 
        
        gc.setFill(Color.WHITE);
 
        gc.setStroke(Color.BLUE);
        gc.setLineWidth(1);
         
    }
    
    @FXML
    private void sendDoodle(ActionEvent event) {
        chatWindowController.sendDoodle(doodleCanvas);
        Stage stage = (Stage) sendButton.getScene().getWindow();
        stage.close();
    }
    
    @FXML
    private void changeColorButtonClick(ActionEvent event) {
        java.awt.Color color = JColorChooser.showDialog(null, null, java.awt.Color.BLACK);
        setActiveColor(color);
    }
    
    private void setActiveColor(java.awt.Color color){
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();
        int a = color.getAlpha();
        double opacity = a / 255.0 ;
        javafx.scene.paint.Color fxColor = javafx.scene.paint.Color.rgb(r, g, b, opacity);
        graphicsContext.setStroke(fxColor);
    }
    
    public void setDoodle(WritableImage writableImage){
        graphicsContext.drawImage(writableImage, 0, 0,graphicsContext.getCanvas().getWidth(),graphicsContext.getCanvas().getHeight());
    }
}
