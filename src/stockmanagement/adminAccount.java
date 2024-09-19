/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stockmanagement;
import java.sql.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
/**
 *
 * @author Cabuyao Col
 */
class Users{
    
}
class adminAccount extends Users{
    Connection con;
    Statement st;
    ResultSet rs;
    
    public adminAccount(){
        checkConnection();
        Login();
    }
    
    public void checkConnection(){
        try{
            String db = "jdbc:ucanaccess://C:\\Users\\Admin\\Documents\\NetBeansProjects\\StockManagement\\Stock Management.accdb";
            con=DriverManager.getConnection(db);
            st = con.createStatement();
        }
        catch(SQLException e){
            e.printStackTrace(System.err);
        }
    }
    
    public void Login(){
        JFrame f = new JFrame("Admin Login");
        f.setSize(500,500);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
        
        Container content = f.getContentPane();
        content.setLayout(new GridLayout(2, 2));
        
        JPanel p = new JPanel();
        
        JLabel l1 = new JLabel("User ID: ");
        l1.setHorizontalTextPosition(JLabel.CENTER);
        l1.setVerticalTextPosition(JLabel.CENTER);
        
        JLabel l2 = new JLabel("Password: ");
        l2.setHorizontalTextPosition(JLabel.CENTER);
        l2.setVerticalTextPosition(JLabel.CENTER);
        
        l2.setHorizontalTextPosition(JLabel.CENTER);
        l2.setVerticalTextPosition(JLabel.CENTER);
        
        JTextField t1 = new JTextField(10);
        JTextField t2 = new JTextField(10);
        
        JButton b = new JButton("Login");
        
        p.add(l1);
        p.add(t1);
        p.add(l2);
        p.add(t2);
        p.add(b);
        f.add(p);
        
        b.addActionListener((ActionEvent e) -> {
            try{
                String id = t1.getText().trim();
                String pass = t2.getText().trim();
                
                String sql = "SELECT aid,apass FROM Admin WHERE aid = '"+id+"' and apass = '"+pass+"'";
                rs = st.executeQuery(sql);
                
                int i = 0;
                while(rs.next()){
                    i++;
                }
                
                if(i == 1){
                    JOptionPane.showMessageDialog(null, "Succesfully Logged in!");
                }
                else if(i >1){
                    JOptionPane.showMessageDialog(null, "Duplicate User, Logged in Failed!");
                }
                else{
                    JOptionPane.showMessageDialog(null, "User not Found!");
                }
                
            }catch(SQLException ex){
                
            }
        });
        
    }
    
    class consumerAccount extends Users{
        
    }
    
    public static void main (String [] args){
        adminAccount obj1 = new adminAccount();
    }
    
}
