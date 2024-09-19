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
public class dbShow {
    public static Statement st;
    public static Connection con;
    public static PreparedStatement pst;
    public static PreparedStatement pst1;
    
    static{
        try{
            String db = "jdbc:ucanaccess://C:\\Users\\Admin\\Documents\\NetBeansProjects\\StockManagement\\Stock Management.accdb";
            con=DriverManager.getConnection(db);
            st = con.createStatement();
            
            pst = con.prepareStatement("SELECT * FROM Bakal_tbl WHERE prodname LIKE ? and quantity <> 0");
            pst1 = con.prepareStatement("SELECT * FROM Kahoy_tbl WHERE prodname LIKE ? and quantity <> 0");
        }
        catch(SQLException e){
            e.printStackTrace(System.err);
        }
    }
    
    public static ResultSet pst(String name) throws SQLException{
        pst.setString(1, "%"+name+"%");
        return pst.executeQuery();
    }
    
    public static ResultSet pst1(String name) throws SQLException{
        pst1.setString(1, "%"+name+"%");
        return pst1.executeQuery();
    }
}
