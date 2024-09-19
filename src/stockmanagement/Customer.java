/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stockmanagement;

import java.sql.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.*;
import javax.swing.ButtonGroup;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author Cabuyao Col
 */
public class Customer extends javax.swing.JFrame {
    Connection con;
    ResultSet rs;
    ResultSet rs1;
    ResultSet rs2;
    ResultSet rs3;
    ResultSet rs4;
    Statement st;

    /**
     * Creates new form Customer
     */
    public Customer() {
        initComponents();
        checkConnections();
        selectionModeList();
        groupButton();
        elementHider();
        columnHider();
        
        this.setLocationRelativeTo(null);
        
        DefaultTableModel model = (DefaultTableModel) userTable.getModel();
        model.setRowCount(0);
        showUser();
    }
    
    public Customer(String uname) {
        initComponents();
        checkConnections();
        selectionModeList();
        groupButton();
        elementHider();
        columnHider();
        
        this.setLocationRelativeTo(null);
        
        DefaultTableModel model = (DefaultTableModel) userTable.getModel();
        model.setRowCount(0);
        showUser();
        
        accUname.setText(uname);
    }
    
    private void elementHider(){
        idTitle.setVisible(false);
        uID.setVisible(false);
    }
    
    private void columnHider(){
        userTable.getColumnModel().getColumn(0).setMinWidth(0);
        userTable.getColumnModel().getColumn(0).setMaxWidth(0);
        
        userTable.getColumnModel().getColumn(2).setMinWidth(0);
        userTable.getColumnModel().getColumn(2).setMaxWidth(0);
    }
    
    public class Commands extends Customer{
        String uid = uID.getText().trim();
        String firstname = uFname.getText().trim();
        String lastname = uLname.getText().trim();
        String username = uName.getText().trim().replace(" ", "");
        String password = uPass.getText().trim();
        String phonenum = phoneNum.getText().trim();
        String email = uEmail.getText().trim();
        String gender = "Male";
        String position = "Consumer";
            
        public void add(){
            if(female.isSelected()){
                gender = "Female";
            }
            
            try{
                String sql2 = "INSERT INTO Users(username, upass, ufname, ulname, gender, phone, email, position) VALUES('"+username+"', '"+password+"', '"+firstname+"', '"+lastname+"', '"+gender+"', '"+phonenum+"', '"+email+"','"+position+"')";
                int i = st.executeUpdate(sql2);
                
                if(i>0){
                    JOptionPane.showMessageDialog(null,"Account Added!");
                    uFname.setText(null);
                    uLname.setText(null);
                    uName.setText(null);
                    uPass.setText(null);
                    phoneNum.setText(null);
                    uEmail.setText(null);
                 }
                 else{
                    JOptionPane.showMessageDialog(null,"Please Check The Information!");
                 }
            }
            catch(SQLException c){
                c.printStackTrace(System.err);
            }
        }

        public void update(){
            if(female.isSelected()){
                gender = "Female";
            }
            
            try{
                String sql3 = "UPDATE Users SET username = '"+username+"', upass = '"+password+"', ufname = '"+firstname+"', ulname = '"+lastname+"', gender = '"+gender+"', phone = '"+phonenum+"', email = '"+email+"' WHERE ID = '"+uid+"'";
                int x = st.executeUpdate(sql3);
                
                if(x > 0){
                    JOptionPane.showMessageDialog(null, "User Updated Succesfully!", "UPDATED", 2);
                }
                else{
                    JOptionPane.showMessageDialog(null, "Updated Failed!", "ERROR", 2);
                }
            }
            catch(SQLException e){
                e.printStackTrace(System.err);
            }
        }
        
        public void delete(){
            try{
                String sql4 = "DELETE FROM Users WHERE ID = '"+uid+"'";
                int j = st.executeUpdate(sql4);
                
                if(j > 0){                
                    JOptionPane.showMessageDialog(null, "User Deleted Succesfully!", "DELETED", 2);
                }
                else{
                    JOptionPane.showMessageDialog(null, "Delete Failed!", "ERROR", 2);
                }
            }
            catch(SQLException ex){
                ex.printStackTrace(System.err);
            }
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
        bg.add(male);
        bg.add(female);
    }
       
    private void clearFields(){
        String username = uName.getText().trim();
        
        if(accountChecker(username)){
            uName.setText(null);
            uPass.setText(null);
        }
        else if(uName.getText().contains(" ")){
            uName.setText(null);
            uPass.setText(null);
        }
        else if(phoneNum.getText().length() != 11){
            phoneNum.setText(null);
        }
        else if(uPass.getText().length()<8){
            uPass.setText(null);
        }
        else{
            uFname.setText(null);
            uLname.setText(null);
            uName.setText(null);
            uPass.setText(null);
            phoneNum.setText(null);
        }
    }
    
    public boolean fieldsChecker(){
        String firstname = uFname.getText().trim();
        String lastname = uLname.getText().trim();
        String username = uName.getText().trim();
        String password = uPass.getText().trim();
        String phonenum = phoneNum.getText().trim();
        
        if(firstname.equals("") || lastname.equals("") || username.equals("") || password.equals("") || phonenum.equals("")){
            JOptionPane.showMessageDialog(rootPane, "Some Fields are Empty", "Empty Fields", 2);
            return false;
        }
        else if(accountChecker(username)){
            JOptionPane.showMessageDialog(rootPane, "This username already exist!", "Username Failed", 2);
            clearFields();
            return false;
        }
        else if(uName.getText().trim().contains(" ")){
            JOptionPane.showMessageDialog(rootPane, "No space is allowed in username!", "Username Failed", 2);
            clearFields();
            return false;
        }
        else if(phoneNum.getText().length() != 11){
            JOptionPane.showMessageDialog(rootPane, "Enter a valid phone number!", "ERROR!", 2);
            clearFields();
            return false;
        }
        else if(uPass.getText().length()<8){
            JOptionPane.showMessageDialog(rootPane, "Password must be longer than 8 characters!", "ERROR!", 2);
            clearFields();
            return false;
        }
        else{
            return true;
        }
    }
    
    private void clearFieldsUpdate(){
        if(uName.getText().contains(" ")){
            uName.setText(null);
            uPass.setText(null);
        }
        else if(phoneNum.getText().length() != 11){
            phoneNum.setText(null);
        }
        else if(uPass.getText().length()<8){
            uPass.setText(null);
        }
        else{
            uFname.setText(null);
            uLname.setText(null);
            uName.setText(null);
            uPass.setText(null);
            phoneNum.setText(null);
        }
    }
    
    public boolean fieldsCheckerUpdate(){
        String firstname = uFname.getText().trim();
        String lastname = uLname.getText().trim();
        String username = uName.getText().trim();
        String password = uPass.getText().trim();
        String phonenum = phoneNum.getText().trim();
        
        if(firstname.equals("") || lastname.equals("") || username.equals("") || password.equals("") || phonenum.equals("")){
            JOptionPane.showMessageDialog(rootPane, "Some Fields are Empty", "Empty Fields", 2);
            return false;
        }
        else if(uName.getText().trim().contains(" ")){
            JOptionPane.showMessageDialog(rootPane, "No space is allowed in username!", "Username Failed", 2);
            clearFieldsUpdate();
            return false;
        }
        else if(phoneNum.getText().length() != 11){
            JOptionPane.showMessageDialog(rootPane, "Enter a valid phone number!", "ERROR!", 2);
            clearFieldsUpdate();
            return false;
        }
        else if(uPass.getText().length()<8){
            JOptionPane.showMessageDialog(rootPane, "Password must be longer than 8 characters!", "ERROR!", 2);
            clearFieldsUpdate();
            return false;
        }
        else{
            return true;
        }
    }
    
    public boolean accountChecker(String uname){
        boolean usernameExist = false;
        try{
            String sql1 = "SELECT * FROM Users WHERE username = '"+uname+"'";
            rs1=st.executeQuery(sql1);
            
            if(rs1.next()){
                usernameExist = true;
            }
        }
        catch(SQLException e){
            e.printStackTrace(System.err);
        }
        return usernameExist;
    }
    
    public boolean phoneNumberChecker(String phone){
        boolean phonenumExist = false;
        try{
            String sql1 = "SELECT * FROM Users WHERE phone = '"+phone+"'";
            rs3=st.executeQuery(sql1);
            
            if(rs3.next()){
                phonenumExist = true;
            }
        }
        catch(SQLException e){
            e.printStackTrace(System.err);
        }
        return phonenumExist;
    }
    
    public boolean emailChecker(String email){
        boolean emailExist = false;
        try{
            String sql1 = "SELECT * FROM Users WHERE email = '"+email+"'";
            rs4=st.executeQuery(sql1);
            
            if(rs4.next()){
                emailExist = true;
            }
        }
        catch(SQLException e){
            e.printStackTrace(System.err);
        }
        return emailExist;
    }
    
    public boolean valEmail(String acc){
        String emailCheck = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
        Pattern ePat = Pattern.compile(emailCheck, Pattern.CASE_INSENSITIVE);
        Matcher matcher = ePat.matcher(acc);
        return matcher.find();
    }
       
    public ArrayList<userList> userLists() {
        ArrayList<userList> userT = new ArrayList<>();

        try{
            String q2 = "SELECT * FROM Users WHERE position = '"+"Consumer"+"'";
            rs = st.executeQuery(q2);
            userList ulist;
            while (rs.next()) {
                ulist = new userList(rs.getInt("ID"), rs.getString("username"), rs.getString("upass"), rs.getString("ufname"), rs.getString("ulname"), rs.getString("gender"), rs.getString("phone"), rs.getString("email"));
                userT.add(ulist);
            }
        }
        catch(SQLException e){
            e.printStackTrace(System.err);
        }
        return userT;
    }

    public void showUser() {
        ArrayList<userList> list = userLists();
        DefaultTableModel model = (DefaultTableModel) userTable.getModel();
        Object[] row = new Object[8];
        
        for (int i = 0; i < list.size(); i++) {
            row[0] = list.get(i).getuID();
            row[1] = list.get(i).getuName();
            row[2] = list.get(i).getuPass();
            row[3] = list.get(i).getfName();
            row[4] = list.get(i).getlName();
            row[5] = list.get(i).getuGender();
            row[6] = list.get(i).getuPhone();
            row[7] = list.get(i).getuEmail();
            model.addRow(row);
        }
    }
        
    public void selectionModeList(){
        userTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ListSelectionModel model = userTable.getSelectionModel();
        
        model.addListSelectionListener((ListSelectionEvent e) -> {
            if(e.getValueIsAdjusting()){
                return;
            }
        });
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
        jPanel6 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        accUname = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        userTable = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        idTitle = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        add = new javax.swing.JButton();
        update = new javax.swing.JButton();
        delete = new javax.swing.JButton();
        uID = new javax.swing.JTextField();
        uName = new javax.swing.JTextField();
        uFname = new javax.swing.JTextField();
        uLname = new javax.swing.JTextField();
        phoneNum = new javax.swing.JTextField();
        searchField = new javax.swing.JTextField();
        male = new javax.swing.JRadioButton();
        female = new javax.swing.JRadioButton();
        home = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        uEmail = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        uPass = new javax.swing.JPasswordField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel6.setBackground(new java.awt.Color(32, 34, 37));

        jLabel2.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(114, 118, 125));
        jLabel2.setText("VIAZONE TRADING");

        jLabel3.setFont(new java.awt.Font("Arial Black", 1, 20)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 51, 0));
        jLabel3.setText("X");
        jLabel3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel3MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addGap(20, 20, 20))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(3, 127, 54));

        jLabel5.setFont(new java.awt.Font("Arial", 1, 60)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(210, 180, 64));
        jLabel5.setText("CUSTOMERS");

        jPanel4.setBackground(new java.awt.Color(3, 127, 54));
        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 3));

        jLabel11.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Logged in as:");

        accUname.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        accUname.setForeground(new java.awt.Color(210, 180, 64));
        accUname.setText("Admin");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap(23, Short.MAX_VALUE)
                .addComponent(jLabel11)
                .addGap(20, 20, 20))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(71, 71, 71)
                .addComponent(accUname)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(accUname, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(396, Short.MAX_VALUE)
                .addComponent(jLabel5)
                .addGap(303, 303, 303)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel3.setBackground(new java.awt.Color(3, 127, 54));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 22, Short.MAX_VALUE)
        );

        userTable.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        userTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Username", "Password", "First Name", "Last Name", "Gender", "Phone #", "Email"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Integer.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        userTable.getTableHeader().setReorderingAllowed(false);
        userTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                userTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(userTable);

        jLabel1.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(3, 127, 54));
        jLabel1.setText("Search User:");

        idTitle.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        idTitle.setForeground(new java.awt.Color(3, 127, 54));
        idTitle.setText("ID:");

        jLabel4.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(3, 127, 54));
        jLabel4.setText("Username:");

        jLabel6.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(3, 127, 54));
        jLabel6.setText("Password:");

        jLabel7.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(3, 127, 54));
        jLabel7.setText("First Name:");

        jLabel8.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(3, 127, 54));
        jLabel8.setText("Last Name:");

        jLabel9.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(3, 127, 54));
        jLabel9.setText("Gender:");

        jLabel10.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(3, 127, 54));
        jLabel10.setText("Phone Number:");

        add.setBackground(new java.awt.Color(3, 127, 54));
        add.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        add.setForeground(new java.awt.Color(204, 255, 102));
        add.setText("Add");
        add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addActionPerformed(evt);
            }
        });

        update.setBackground(new java.awt.Color(3, 127, 54));
        update.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        update.setForeground(new java.awt.Color(204, 255, 102));
        update.setText("Update");
        update.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateActionPerformed(evt);
            }
        });

        delete.setBackground(new java.awt.Color(3, 127, 54));
        delete.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        delete.setForeground(new java.awt.Color(204, 255, 102));
        delete.setText("Delete");
        delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteActionPerformed(evt);
            }
        });

        uID.setEditable(false);
        uID.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N

        uName.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N

        uFname.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N

        uLname.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N

        phoneNum.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        phoneNum.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                phoneNumKeyTyped(evt);
            }
        });

        searchField.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        searchField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                searchFieldKeyReleased(evt);
            }
        });

        male.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        male.setSelected(true);
        male.setText("Male");

        female.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        female.setText("Female");

        home.setBackground(new java.awt.Color(3, 127, 54));
        home.setFont(new java.awt.Font("Arial", 1, 36)); // NOI18N
        home.setForeground(new java.awt.Color(204, 255, 102));
        home.setText("HOME");
        home.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                homeActionPerformed(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(3, 127, 54));
        jLabel12.setText("Email:");

        uEmail.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N

        jButton1.setBackground(new java.awt.Color(3, 127, 54));
        jButton1.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jButton1.setForeground(new java.awt.Color(204, 255, 102));
        jButton1.setText("CLEAR");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        uPass.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(47, 47, 47)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(add)
                                .addGap(58, 58, 58)
                                .addComponent(update)
                                .addGap(51, 51, 51)
                                .addComponent(delete))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel7)
                                    .addComponent(idTitle)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel10)
                                    .addComponent(jLabel12))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(uID)
                                    .addComponent(uFname)
                                    .addComponent(uLname)
                                    .addComponent(phoneNum)
                                    .addComponent(uEmail)
                                    .addComponent(uPass)
                                    .addComponent(uName, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(138, 138, 138)
                        .addComponent(jLabel9)
                        .addGap(18, 18, 18)
                        .addComponent(male)
                        .addGap(18, 18, 18)
                        .addComponent(female))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(76, 76, 76)
                        .addComponent(home)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 750, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(searchField, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(214, 214, 214))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(home)
                            .addComponent(jButton1))
                        .addGap(46, 46, 46)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(idTitle)
                            .addComponent(uID, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(20, 20, 20)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel4)
                            .addComponent(uName, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(24, 24, 24)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel6)
                            .addComponent(uPass, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(uFname, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGap(19, 19, 19)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(uLname, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8))
                        .addGap(20, 20, 20)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(phoneNum, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10))
                        .addGap(23, 23, 23)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(uEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(male)
                            .addComponent(female)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(searchField, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addGap(36, 36, 36)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 489, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 37, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(add)
                    .addComponent(update)
                    .addComponent(delete))
                .addGap(30, 30, 30)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
        String lbl = accUname.getText();
        Admin a = new Admin(lbl);
        a.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_homeActionPerformed

    private void searchFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchFieldKeyReleased
        DefaultTableModel model = (DefaultTableModel) userTable.getModel();
        String search = searchField.getText();
        TableRowSorter<DefaultTableModel> sort = new TableRowSorter<>(model);
        
        userTable.setRowSorter(sort);
        sort.setRowFilter(RowFilter.regexFilter("(?i)"+search));
    }//GEN-LAST:event_searchFieldKeyReleased

    private void phoneNumKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_phoneNumKeyTyped
        if(!Character.isDigit(evt.getKeyChar())){
            evt.consume();
        }
        if (phoneNum.getText().length() == 11 ){
            evt.consume();
        }
    }//GEN-LAST:event_phoneNumKeyTyped

    private void userTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_userTableMouseClicked
        int rowIndex = userTable.getSelectedRow();
        
        uID.setText(userTable.getValueAt(rowIndex, 0).toString());
        uName.setText(userTable.getValueAt(rowIndex, 1).toString());
        uPass.setText(userTable.getValueAt(rowIndex, 2).toString());
        uFname.setText(userTable.getValueAt(rowIndex, 3).toString());
        uLname.setText(userTable.getValueAt(rowIndex, 4).toString());
        String gend = userTable.getValueAt(rowIndex, 5).toString();
        if(gend.equals("Male")){
            male.setSelected(true);
        }
        else{
            female.setSelected(true);
        }
        phoneNum.setText(userTable.getValueAt(rowIndex, 6).toString());
        if(userTable.getValueAt(rowIndex, 7).toString().equals("")){
            uEmail.setText(null);
        }
        else{
            uEmail.setText(userTable.getValueAt(rowIndex, 7).toString());
        }
    }//GEN-LAST:event_userTableMouseClicked

    private void addActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addActionPerformed
        String username = uName.getText().trim().replace(" ", "");
        String email = uEmail.getText().trim();
        String phone = phoneNum.getText().trim();
        
        if(fieldsChecker()){
            if(!accountChecker(username)){
                if(valEmail(email)){
                    if(!phoneNumberChecker(phone)){
                        if(!emailChecker(email)){
                            Commands go = new Commands();
                            go.add();

                            DefaultTableModel model = (DefaultTableModel) userTable.getModel();
                            model.setRowCount(0);
                            showUser();
                        }
                        else{
                            JOptionPane.showMessageDialog(null,"Email already exist!");
                            uEmail.setText(null);
                        }
                    }
                    else{
                        JOptionPane.showMessageDialog(null,"Phone number already exist!");
                        phoneNum.setText(null);
                    }
                }
                else{
                    JOptionPane.showMessageDialog(null,"Please enter a valid email address!");
                    uEmail.setText("");
                }
            }
        }
    }//GEN-LAST:event_addActionPerformed

    private void updateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateActionPerformed
        String email = uEmail.getText().trim();
        
        if(!uID.getText().equals("")){
            if(fieldsCheckerUpdate()){
                if(valEmail(email)){
                    Commands go = new Commands();
                    go.update();

                    DefaultTableModel model = (DefaultTableModel) userTable.getModel();
                    model.setRowCount(0);
                    showUser();

                    uID.setText(null);
                    uName.setText(null);
                    uPass.setText(null);
                    uFname.setText(null);
                    uLname.setText(null);
                    phoneNum.setText(null);
                    uEmail.setText(null);

                    userTable.getSelectionModel().clearSelection();
                }
                else{
                    JOptionPane.showMessageDialog(null,"Please enter a valid email address!");
                    uEmail.setText("");
                }
            }
        }
        else{
            JOptionPane.showMessageDialog(null,"Please select a user!");
        }
    }//GEN-LAST:event_updateActionPerformed

    private void deleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteActionPerformed
        if(!uID.getText().equals("")){
            if(fieldsCheckerUpdate()){
                Commands go = new Commands();
                go.delete();

                DefaultTableModel model = (DefaultTableModel) userTable.getModel();
                model.setRowCount(0);
                showUser();

                uID.setText(null);
                uName.setText(null);
                uPass.setText(null);
                uFname.setText(null);
                uLname.setText(null);
                phoneNum.setText(null);
                uEmail.setText(null);

                userTable.getSelectionModel().clearSelection();
            }
        }
        else{
            JOptionPane.showMessageDialog(null,"Please select a user!");
        }
    }//GEN-LAST:event_deleteActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        uID.setText(null);
        uName.setText(null);
        uPass.setText(null);
        uFname.setText(null);
        uLname.setText(null);
        phoneNum.setText(null);
        uEmail.setText(null);
        
        userTable.getSelectionModel().clearSelection();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jLabel3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseClicked
        System.exit(0);
    }//GEN-LAST:event_jLabel3MouseClicked

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
            java.util.logging.Logger.getLogger(Customer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Customer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Customer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Customer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Customer().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel accUname;
    private javax.swing.JButton add;
    private javax.swing.JButton delete;
    private javax.swing.JRadioButton female;
    private javax.swing.JButton home;
    private javax.swing.JLabel idTitle;
    private javax.swing.JButton jButton1;
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
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JRadioButton male;
    private javax.swing.JTextField phoneNum;
    private javax.swing.JTextField searchField;
    private javax.swing.JTextField uEmail;
    private javax.swing.JTextField uFname;
    private javax.swing.JTextField uID;
    private javax.swing.JTextField uLname;
    private javax.swing.JTextField uName;
    private javax.swing.JPasswordField uPass;
    private javax.swing.JButton update;
    private javax.swing.JTable userTable;
    // End of variables declaration//GEN-END:variables
}
