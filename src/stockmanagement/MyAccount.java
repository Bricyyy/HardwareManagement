/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stockmanagement;
import java.sql.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.*;
/**
 *
 * @author Cabuyao Col
 */
public class MyAccount extends javax.swing.JFrame {
    Connection con;
    ResultSet rs;
    ResultSet rs1;
    ResultSet rs2;
    ResultSet rs3;
    ResultSet rs4;
    Statement st;
    
    private String accName;

    /**
     * Creates new form accountSettings
     */
    public MyAccount() {
        initComponents();
        checkConnections();
        groupButton();
        
        this.setLocationRelativeTo(null);
    }
    
    public MyAccount(String userName, String uID){
        initComponents();
        checkConnections();
        groupButton();
        
        this.setLocationRelativeTo(null);
        
        this.accName = userName;
        
        userID.setText(uID);
        hideuname.setText(userName);
        
        try{
            String sql1 = "SELECT * FROM Users WHERE username = '"+userName+"'";
            rs = st.executeQuery(sql1);
            
            if(rs.next()){
                uname.setText(rs.getString("username"));
                ufname.setText(rs.getString("ufname"));
                ulname.setText(rs.getString("ulname"));
                String gender = rs.getString("gender");
                if(gender.equals("Female")){
                    femaleB.doClick();
                }
                unum.setText(rs.getString("phone"));
                uemail.setText(rs.getString("email"));
            }
        }
        catch(SQLException e){
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
    
    private void groupButton(){
        ButtonGroup bg = new ButtonGroup();
        bg.add(maleB);
        bg.add(femaleB);
    }
    
    private void updateInfo(){
        String uid = userID.getText().trim();
        String userN = uname.getText();
        String password = upass.getText().trim();
        String firstname = ufname.getText().trim();
        String lastname = ulname.getText().trim();
        String gender = "Male";
        String phonenum = unum.getText().trim();
        String email = uemail.getText().trim();
        
        if(femaleB.isSelected()){
            gender = "Female";
        }
                
        try{
            String sql3 = "UPDATE Users SET username = '"+userN+"', upass = '"+password+"', ufname = '"+firstname+"', ulname = '"+lastname+"', gender = '"+gender+"', phone = '"+phonenum+"', email = '"+email+"' WHERE ID = '"+uid+"'";
            int x = st.executeUpdate(sql3);
                
            if(x > 0){
                JOptionPane.showMessageDialog(null, "Account Updated Succesfully!", "UPDATED", 2);
                hideuname.setText(uname.getText().trim());
                uname.setText(null);
                curPass.setText(null);
                upass.setText(null);
                ucpass.setText(null);
                ufname.setText(null);
                ulname.setText(null);
                unum.setText(null);
                uemail.setText(null);
            }
            else{
                JOptionPane.showMessageDialog(null, "Account Updated Failed!", "ERROR", 2);
            }
        }
        catch(SQLException e){
            e.printStackTrace(System.err);
        }
    }
    
    private boolean valEmail(String email){
        String emailCheck = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
        Pattern ePat = Pattern.compile(emailCheck, Pattern.CASE_INSENSITIVE);
        Matcher matcher = ePat.matcher(email);
        return matcher.find();
    }
    
    public boolean accountChecker(String ID, String uname){
        boolean usernameExist = false;
                
        try{
            String sql1 = "SELECT * FROM Users WHERE ID = '"+ID+"' AND username != '"+uname+"'";
            rs2=st.executeQuery(sql1);
            
            if(rs2.next()){
                usernameExist = true;
            }
        }
        catch(SQLException e){
            e.printStackTrace(System.err);
        }
        return usernameExist;
    }
    
    public boolean phoneChecker(String ID, String phone){
        boolean phoneExist = false;
                
        try{
            String sql1 = "SELECT * FROM Users WHERE ID = '"+ID+"' AND phone != '"+phone+"'";
            rs3=st.executeQuery(sql1);
            
            if(rs3.next()){
                phoneExist = true;
            }
        }
        catch(SQLException e){
            e.printStackTrace(System.err);
        }
        return phoneExist;
    }
    
    public boolean emailChecker(String ID, String email){
        boolean phoneExist = false;
                
        try{
            String sql1 = "SELECT * FROM Users WHERE ID = '"+ID+"' AND email != '"+email+"'";
            rs4=st.executeQuery(sql1);
            
            if(rs4.next()){
                phoneExist = true;
            }
        }
        catch(SQLException e){
            e.printStackTrace(System.err);
        }
        return phoneExist;
    }
    
    private void clearFields(){
        String accID = userID.getText().trim();
        String userN = uname.getText();
        String password = upass.getText().trim();
        String conpassword = ucpass.getText().trim();
        String phonenum = unum.getText().trim();
        String email = uemail.getText().trim();
        
        if(!password.equals(conpassword)){
            upass.setText(null);
            ucpass.setText(null);
        }
//        else if(accountChecker(accID, userN)){
//            uname.setText(null);
//        }
//        else if(phoneChecker(accID, phonenum)){
//            unum.setText(null);
//        }
//        else if(emailChecker(accID, email)){
//            uemail.setText(null);
//        }
        else if(userN.contains(" ")){
            uname.setText(null);
            upass.setText(null);
            ucpass.setText(null);
        }
        else if(phonenum.length() != 11){
            unum.setText(null);
        }
        else if(password.length()<8){
            upass.setText(null);
            ucpass.setText(null);
        }
        else if(valEmail(email)){
            uemail.setText(null);
        }
        else{
//            uname.setText(null);
//            upass.setText(null);
//            ucpass.setText(null);
//            ufname.setText(null);
//            ulname.setText(null);
//            unum.setText(null);
//            uemail.setText(null);
        }
    }
    
    private boolean fieldsChecker(){
        String accID = userID.getText().trim();
        String userN = uname.getText();
        String password = upass.getText().trim();
        String conpassword = ucpass.getText().trim();
        String firstname = ufname.getText().trim();
        String lastname = ulname.getText().trim();
        String gender = "Male";
        String phonenum = unum.getText().trim();
        String email = uemail.getText().trim();
        
        if(userN.equals("") || password.equals("") || conpassword.equals("") || firstname.equals("") || lastname.equals("") || gender.equals("") || phonenum.equals("") || email.equals("")){
            JOptionPane.showMessageDialog(null, "Some fields are empty!", "ERROR", 2);
            return false;
        }
        else if(!password.equals(conpassword)){
            JOptionPane.showMessageDialog(null, "New password does not match!", "ERROR", 2);
            clearFields();
            return false;
        }
//        else if(accountChecker(accID, userN)){
//            JOptionPane.showMessageDialog(null, "Username already exist!", "ERROR", 2);
//            clearFields();
//            return false;
//        }
//        else if(phoneChecker(accID, phonenum)){
//            JOptionPane.showMessageDialog(null, "Phone number already exist!", "ERROR", 2);
//            clearFields();
//            return false;
//        }
//        else if(emailChecker(accID, email)){
//            JOptionPane.showMessageDialog(null, "Email address already exist!", "ERROR", 2);
//            clearFields();
//            return false;
//        }
        else if(userN.contains(" ")){
            JOptionPane.showMessageDialog(null, "Username must not contain spaces!", "ERROR", 2);
            clearFields();
            return false;
        }
        else if(phonenum.length() != 11){
            JOptionPane.showMessageDialog(null, "Enter a valid phone number!", "ERROR", 2);
            clearFields();
            return false;
        }
        else if(password.length()<8){
            JOptionPane.showMessageDialog(null, "Password must be at least 8 characters!", "ERROR!", 2);
            clearFields();
            return false;
        }
        else if(!valEmail(email)){
            JOptionPane.showMessageDialog(null,"Please enter a valid email address!", "ERROR", 2);
            clearFields();
            return false;
        }
        else{
            return true;
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        userID = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        uname = new javax.swing.JTextField();
        ufname = new javax.swing.JTextField();
        unum = new javax.swing.JTextField();
        ulname = new javax.swing.JTextField();
        uemail = new javax.swing.JTextField();
        maleB = new javax.swing.JRadioButton();
        femaleB = new javax.swing.JRadioButton();
        save = new javax.swing.JButton();
        showPass = new javax.swing.JCheckBox();
        home = new javax.swing.JButton();
        upass = new javax.swing.JPasswordField();
        hideuname = new javax.swing.JLabel();
        ucpass = new javax.swing.JPasswordField();
        jLabel11 = new javax.swing.JLabel();
        curPass = new javax.swing.JPasswordField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jPanel3.setBackground(new java.awt.Color(32, 34, 37));

        jLabel1.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(114, 118, 125));
        jLabel1.setText("VIAZONE TRADING");

        jLabel12.setFont(new java.awt.Font("Arial Black", 1, 20)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 51, 0));
        jLabel12.setText("X");
        jLabel12.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel12MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel12)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel12))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setBackground(new java.awt.Color(3, 127, 54));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );

        jPanel5.setBackground(new java.awt.Color(3, 127, 54));

        jLabel2.setFont(new java.awt.Font("Arial", 1, 60)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(210, 180, 64));
        jLabel2.setText("MY ACCOUNT");

        userID.setForeground(new java.awt.Color(3, 127, 54));
        userID.setText("userid");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(207, 207, 207)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 135, Short.MAX_VALUE)
                .addComponent(userID)
                .addGap(48, 48, 48))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(17, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addContainerGap())
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(userID)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel3.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(3, 127, 54));
        jLabel3.setText("Username:");

        jLabel4.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(3, 127, 54));
        jLabel4.setText("New Password:");

        jLabel5.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(3, 127, 54));
        jLabel5.setText("Confirm Password:");

        jLabel6.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(3, 127, 54));
        jLabel6.setText("First Name:");

        jLabel7.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(3, 127, 54));
        jLabel7.setText("Last Name:");

        jLabel8.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(3, 127, 54));
        jLabel8.setText("Phone Number:");

        jLabel9.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(3, 127, 54));
        jLabel9.setText("Email:");

        jLabel10.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(3, 127, 54));
        jLabel10.setText("Gender:");

        uname.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N

        ufname.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N

        unum.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        unum.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                unumKeyTyped(evt);
            }
        });

        ulname.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N

        uemail.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N

        maleB.setBackground(new java.awt.Color(255, 255, 255));
        maleB.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        maleB.setSelected(true);
        maleB.setText("Male");

        femaleB.setBackground(new java.awt.Color(255, 255, 255));
        femaleB.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        femaleB.setText("Female");

        save.setBackground(new java.awt.Color(3, 127, 54));
        save.setFont(new java.awt.Font("Arial", 1, 36)); // NOI18N
        save.setForeground(new java.awt.Color(204, 255, 102));
        save.setText("Save");
        save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveActionPerformed(evt);
            }
        });

        showPass.setBackground(new java.awt.Color(255, 255, 255));
        showPass.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        showPass.setText("Show Password");
        showPass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showPassActionPerformed(evt);
            }
        });

        home.setBackground(new java.awt.Color(3, 127, 54));
        home.setFont(new java.awt.Font("Arial", 1, 36)); // NOI18N
        home.setForeground(new java.awt.Color(204, 255, 102));
        home.setText("HOME");
        home.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                homeActionPerformed(evt);
            }
        });

        upass.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N

        hideuname.setForeground(new java.awt.Color(255, 255, 255));
        hideuname.setText("username");

        ucpass.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N

        jLabel11.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(3, 127, 54));
        jLabel11.setText("Current Password:");

        curPass.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(188, 188, 188)
                        .addComponent(home)
                        .addGap(103, 103, 103)
                        .addComponent(save))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(109, 109, 109)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel10)
                            .addComponent(jLabel11)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addGap(43, 43, 43)
                                    .addComponent(jLabel4))
                                .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING))
                            .addComponent(jLabel3))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(maleB)
                                .addGap(18, 18, 18)
                                .addComponent(femaleB)
                                .addGap(24, 24, 24)
                                .addComponent(hideuname))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(curPass, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(uname, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                                    .addComponent(ufname, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                                    .addComponent(ulname, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(unum, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(uemail, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(upass, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(ucpass, javax.swing.GroupLayout.Alignment.LEADING))
                                .addGap(18, 18, 18)
                                .addComponent(showPass)))))
                .addContainerGap(139, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(uname, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(curPass, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(upass, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel5)
                            .addComponent(ucpass, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(ufname, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6))
                        .addGap(19, 19, 19)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(ulname, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(unum, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(uemail, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(maleB)
                                .addComponent(femaleB)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(179, 179, 179)
                        .addComponent(hideuname)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 61, Short.MAX_VALUE)
                        .addComponent(showPass)
                        .addGap(318, 318, 318)))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(save)
                    .addComponent(home))
                .addGap(35, 35, 35)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void homeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_homeActionPerformed
        String lbl = hideuname.getText();
        String lbl1 = userID.getText();
        String lbl2 = "";
        Consumer c = new Consumer(lbl, lbl2, lbl1);
        c.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_homeActionPerformed

    private void showPassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showPassActionPerformed
        if(showPass.isSelected()){
            upass.setEchoChar((char)0);
            ucpass.setEchoChar((char)0);
        }
        else{
            upass.setEchoChar('*');
            ucpass.setEchoChar('*');
        }
    }//GEN-LAST:event_showPassActionPerformed

    private void saveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveActionPerformed
        String uid = userID.getText().trim();
        String email = uemail.getText().trim();
        String userPass = curPass.getText().trim();
        String accNames = hideuname.getText().trim();
        String userPhone = unum.getText().trim();
        
        try{
            String sql = "SELECT * FROM Users WHERE ID = '"+uid+"'";
            rs1 = st.executeQuery(sql);
            
            if(rs1.next()){
                String currentPass = rs.getString("upass");
                
                
                if(fieldsChecker()){
                    if(valEmail(email)){
                        if(userPass.equals(currentPass)){
//                            if(!accountChecker(uid, accNames)){
//                                if(!phoneChecker(uid, userPhone)){
//                                    if(!emailChecker(uid, email)){
                                        updateInfo();
//                                    }
//                                }
//                            }
                        }
                        else{
                            JOptionPane.showMessageDialog(null, "Current password is incorrect!", "ERROR", 2);
                            curPass.setText(null);
                        }
                    }
                }
            }
        }
        catch(SQLException e){
            e.printStackTrace(System.err);
        }
    }//GEN-LAST:event_saveActionPerformed

    private void unumKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_unumKeyTyped
        if(!Character.isDigit(evt.getKeyChar())){
            evt.consume();
        }
        if (unum.getText().length() == 11 ){
            evt.consume();
        }
    }//GEN-LAST:event_unumKeyTyped

    private void jLabel12MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel12MouseClicked
        System.exit(0);
    }//GEN-LAST:event_jLabel12MouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MyAccount.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MyAccount.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MyAccount.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MyAccount.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MyAccount().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPasswordField curPass;
    private javax.swing.JRadioButton femaleB;
    private javax.swing.JLabel hideuname;
    private javax.swing.JButton home;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JRadioButton maleB;
    private javax.swing.JButton save;
    private javax.swing.JCheckBox showPass;
    private javax.swing.JPasswordField ucpass;
    private javax.swing.JTextField uemail;
    private javax.swing.JTextField ufname;
    private javax.swing.JTextField ulname;
    private javax.swing.JTextField uname;
    private javax.swing.JTextField unum;
    private javax.swing.JPasswordField upass;
    private javax.swing.JLabel userID;
    // End of variables declaration//GEN-END:variables
}
