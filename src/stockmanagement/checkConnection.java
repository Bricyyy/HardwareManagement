/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stockmanagement;
import java.sql.*;
/**
 *
 * @author Cabuyao Col
 */
public class checkConnection {
    public static void main (String [] args){
        try{
           Connection con;
           con=DriverManager.getConnection("jdbc:ucanaccess://C:\\Users\\Admin\\Documents\\NetBeansProjects\\StockManagement\\Stock Management.accdb");
            if(con!=null)
                System.out.println("Connection Established");
            else
                System.out.println("Connection cannot be Established");
            
            con.close();
        }
        catch(SQLException e){
            e.printStackTrace(System.err);
        }
    }
}
