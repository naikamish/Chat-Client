/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servertest;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.Properties;
/**
 *
 * @author Amish Naik
 */
public class LoadDriver {
    Connection conn = null;
    Statement stmt = null;
    ResultSet rs = null;
    
    public LoadDriver(String address, String user, String password){
        try {
            Properties props = new Properties();
            props.put("user", user);
            props.put("password", password);

            props.put("autoReconnect", "true");
            conn =
               DriverManager.getConnection(address,props);
        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }
    
    public ResultSet selectQuery(String query){
        try{
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);
            return rs;
        }
        catch(Exception e){
            System.out.println(e.toString());
        }
        finally {
            //closeStatement();
        }
        return null;
    }
    
    public void insertQuery(String query){
        try{
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
        }
        catch(Exception e){
            System.out.println(e.toString());
        }
        finally {
            //closeStatement();
        }
    }
    
    public void prepareMessageQuery(String query, int groupID, int userID, String message, int emotion){
        try{
            PreparedStatement pstmt = conn.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, groupID);
            pstmt.setInt(2, userID);
            pstmt.setString(3, message);
            pstmt.setInt(4, emotion);
            pstmt.execute();
        }
        catch(Exception e){
            System.out.println(e.toString());
        }
    }
    
    public void prepareJoinExitQuery(String query, int groupID, int userID, int status){
        try{
            PreparedStatement pstmt = conn.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, groupID);
            pstmt.setInt(2, userID);
            pstmt.setInt(3, status);
            pstmt.execute();
        }
        catch(Exception e){
            System.out.println(e.toString());
        }
    }
    
    public int insertQueryReturnKey(String query, String groupName, int groupID, String fileName){
        try{
            PreparedStatement pstmt = conn.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, groupName);
            pstmt.setInt(2, groupID);
            pstmt.setString(3, fileName);
            pstmt.execute();
            
            //stmt = conn.createStatement();
            //stmt.executeUpdate(query,Statement.RETURN_GENERATED_KEYS);
            int autoIncKey=0;

            rs = pstmt.getGeneratedKeys();

            if (rs.next()) {
                autoIncKey = rs.getInt(1);
            }
            return autoIncKey;
        }
        catch(Exception e){
            System.out.println(e.toString());
        }
        return 0;
    }
    
    private void closeStatement(){
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException sqlEx) { } // ignore

            rs = null;
        }

        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException sqlEx) { } // ignore

            stmt = null;
        }
    }
}
