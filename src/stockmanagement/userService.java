/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stockmanagement;
import java.awt.*;
import java.sql.*;
import javax.swing.*;
/**
 *
 * @author Cabuyao Col
 */
public class userService extends javax.swing.JFrame{
    Connection con;
    Statement st;
    ResultSet rs;
    
    JTextArea message;
    JLabel label1;;
    String name;
    JLabel txtID;
    JLabel txtName;
    JButton button;
    
    public userService(){
        initComponents();
        checkConnections();   
    }
    
    public userService(String id, String uname){
        txtID = new JLabel(id);
        txtName = new JLabel(uname);
    }
    
    private void initComponents(){
        Color clr1 = new Color(3,127,54);
        Color clr2 = new Color(210,180,64);
        
        JButton back = new JButton("HOME");
        back.setBounds(400, 440, 100, 50);
        back.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        back.addActionListener((java.awt.event.ActionEvent evt) -> {
            backActionPerformed(evt);
        });
        
        button = new JButton("SEND");
        button.setBounds(515, 440, 100, 50);
        button.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        button.addActionListener((java.awt.event.ActionEvent evt) -> {
            buttonActionPerformed(evt);
        });
        
        label1 = new JLabel("Good Day "+txtName+"! Send us your Concerns");
        label1.setFont(new Font("Times New Roman", Font.BOLD,30));
        label1.setBounds(25, 32, 600, 180);
        label1.setForeground(Color.BLACK);
         
        message = new JTextArea();
        message.setText("");
        message.setBounds(25,145,595,275);
        message.setFont(new Font("Times New Roman", Font.PLAIN,25));
        message.setLayout(new BorderLayout());
        message.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        message.setLineWrap(rootPaneCheckingEnabled);
       
        JLabel storeName1 = new JLabel("HARDWARE AND CONSTRUCTION SUPPLY                         ");
        storeName1.setFont(new Font("Times New Roman", Font.BOLD,25));
        storeName1.setForeground(clr2);
        storeName1.setHorizontalAlignment(JLabel.CENTER);
        storeName1.setVerticalAlignment(JLabel.CENTER);
         
        JPanel panel2 = new JPanel();
        panel2.setBackground(clr1);
        panel2.setLayout(new FlowLayout());
        panel2.setBounds(0, 500, 800, 650);
        panel2.add(storeName1);
         
        JLabel storeName = new JLabel("VIAZONE TRADING");
        storeName.setBounds(200, 200, 200, 200);
        storeName.setFont(new Font("Times New Roman", Font.BOLD,40));
        storeName.setForeground(clr2);
        storeName.setHorizontalAlignment(JLabel.CENTER);
        storeName.setVerticalAlignment(JLabel.CENTER);
         
        JPanel panel1 = new JPanel();
        panel1.setBackground(clr1);
        panel1.setLayout(new BorderLayout());
        panel1.setBounds(0, 0, 650, 90);
        panel1.add(storeName);
        
        this.setTitle("Service For Concerns");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(650, 600);
        this.setResizable(false);
        this.setLayout(null);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setBackground(Color.WHITE);
        this.add(panel1);
        this.add(panel2);
        this.add(message);
        this.add(label1);
        this.add(button);
        this.add(back);
    }
    
    private void backActionPerformed(java.awt.event.ActionEvent evt) {                                       
        Consumer c = new Consumer();
        c.setVisible(true);
        this.setVisible(false);
    }  
    
    public void buttonActionPerformed(java.awt.event.ActionEvent evt){
         int maxlength = 250;
         String concern = message.getText();
            if(concern.length()>0 && concern.length()<=maxlength){
                insertConcern();
            }
            else if(concern.length()>maxlength){
                JOptionPane.showMessageDialog(null, "You've Reached The Maximum Amount Of Text", "ERROR", JOptionPane.INFORMATION_MESSAGE);
            }
            else if(concern.length()==0){
                JOptionPane.showMessageDialog(null, "Fill in the blank", "ERROR", JOptionPane.INFORMATION_MESSAGE);
            }
            else{
                JOptionPane.showMessageDialog(null, "ERROR!");
            }
    }
    
    public void insertConcern(){
        try{
            String uID = txtID.getText().trim();
            String uName = txtName.getText().trim();
            String txt = message.getText();
                
            if(txt.length()!=0 && !"Enter Text".equals(txt)){
                String q1="INSERT INTO userConcerns(uid, uname, concern) VALUES('"+uID+" "+uName+"','"+txt+"')";
                int x = st.executeUpdate(q1);
                
                if(x>0){
                    JOptionPane.showMessageDialog(null, "Thank you for sending us feedback!", "Viazone Trading", JOptionPane.INFORMATION_MESSAGE);
                }
            } 
        }
        catch(SQLException e){
            e.printStackTrace(System.err);
        }
    }
    
    public void trackConcern(){
        String uID = txtID.getText().trim();
        String uName = txtName.getText().trim();
        try{
            String q1="SELECT * FROM Consumer WHERE ID = '"+uID+"', username = '"+uName+"'";
            rs = st.executeQuery(q1); 
                 
            if(rs.next()){
                insertConcern();
            }                 
        }catch(SQLException e){
            e.printStackTrace(System.err);
        }
    }
    
    public void checkConnections(){
        try{
            String db = "jdbc:ucanaccess://C:\\Users\\Admin\\Documents\\NetBeansProjects\\StockManagement\\Stock Management.accdb";
            con=DriverManager.getConnection(db);
            st = con.createStatement();
        }
        catch(SQLException e){
            e.printStackTrace(System.err);
        }
    }

    public static void main (String [] args){
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new userService().setVisible(true);
            }
        });
    }
}
