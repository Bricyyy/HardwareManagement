/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stockmanagement;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import static java.awt.print.Printable.NO_SUCH_PAGE;
import static java.awt.print.Printable.PAGE_EXISTS;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.ListSelectionModel;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author Cabuyao Col
 */
public class Order extends javax.swing.JFrame {
    Connection con;
    ResultSet rs;
    ResultSet rs1;
    ResultSet rs2;
    ResultSet rs3;
    ResultSet rs4;
    ResultSet rs5;
    ResultSet rs6;
    ResultSet rs7;
    ResultSet rs8;
    ResultSet rs9;
    Statement st;
    PreparedStatement pst;
    PreparedStatement pst1;
    PreparedStatement pst2;
    PreparedStatement pst3;
    
    Double totalAmount=0.0;
    Double cash=0.0;
    Double balance=0.0;
    Double bHeight=0.0;
    
    ArrayList<String> itemNames = new ArrayList<>();
    ArrayList<String> quantity = new ArrayList<>();
    ArrayList<String> itemPrices = new ArrayList<>();
    ArrayList<String> subtotal = new ArrayList<>();
    ArrayList<String> arN = new ArrayList<>();

    /**
     * Creates new form Order
     */
    public Order() {
        initComponents();
        this.setLocationRelativeTo(null);
        checkConnections();
        selectionModeList();
        selectionModeCart();
        
        qtyCountSetter();
        columnHider();
        elementHider();
        tableSorter();
        searchProdKeyReleased(null);
    }
    
    public Order(String uname, String userID){
        initComponents();
        this.setLocationRelativeTo(null);
        checkConnections();
        selectionModeList();
        selectionModeCart();
        
        qtyCountSetter();
        columnHider();
        elementHider();
        tableSorter();
        searchProdKeyReleased(null);
        
        accName.setText(uname);
        hideID.setText(userID);
    }
    
    private void elementHider(){
        purCat.setVisible(false);
        catName.setVisible(false);
        purID.setVisible(false);
        prodTitleName.setVisible(false);
    }
    
    private void columnHider(){
        cart.getColumnModel().getColumn(3).setMinWidth(0);
        cart.getColumnModel().getColumn(3).setMaxWidth(0);
        
        cart.getColumnModel().getColumn(0).setMinWidth(0);
        cart.getColumnModel().getColumn(0).setMaxWidth(0);
        
        showItem.getColumnModel().getColumn(0).setMinWidth(0);
        showItem.getColumnModel().getColumn(0).setMaxWidth(0);
    }
    
    private void tableSorter(){
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(showItem.getModel());
        showItem.setRowSorter(sorter);
        List<RowSorter.SortKey> sortKeys = new ArrayList<>();

        int columnIndexForName = 1;
        sortKeys.add(new RowSorter.SortKey(columnIndexForName, SortOrder.ASCENDING));
        
        int columnIndexForQuan = 2;
        sortKeys.add(new RowSorter.SortKey(columnIndexForQuan, SortOrder.ASCENDING));
        
        int columnIndexForPrice = 3;
        sortKeys.add(new RowSorter.SortKey(columnIndexForPrice, SortOrder.ASCENDING));

//        sorter.setSortKeys(sortKeys);
//        sorter.sort();
    }
    
    public void selectionModeCart(){
        cart.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ListSelectionModel model = cart.getSelectionModel();
        
        model.addListSelectionListener((ListSelectionEvent e) -> {
            if(e.getValueIsAdjusting()){
                return;
            }
        });
    }
    
    public void selectionModeList(){
        showItem.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ListSelectionModel model = showItem.getSelectionModel();
        
        model.addListSelectionListener((ListSelectionEvent e) -> {
            if(e.getValueIsAdjusting()){
                return;
            }
        });
    }
    
    private void qtyCountSetter(){
        qtyCount.setModel(new javax.swing.SpinnerNumberModel(0, 0, 0, 1));
        ((JSpinner.DefaultEditor) qtyCount.getEditor()).getTextField().setEditable(false);
    }
    
    public class orderCommands extends Order{
        String itemCat = purCat.getText().trim();
        String ab = prodList.getSelectedItem().toString().trim();
        String itemID = purID.getText().trim();
        String itemName = purName.getText().trim();
        String itemPrice = amount.getText().trim();
        String totals;
        
        public ArrayList<itemLists> itemList() {
            ArrayList<itemLists> userList = new ArrayList<>();      
            try{
                String q2 = "SELECT * FROM " +ab+ "_tbl WHERE quantity <> 0";
                rs = st.executeQuery(q2);
                itemLists ilist;
                while (rs.next()) {
                    ilist = new itemLists(rs.getInt("ID"), rs.getString("prodname"), rs.getInt("quantity"), rs.getInt("buyingPrice"), rs.getInt("sellingPrice"));
                    userList.add(ilist);
                }
            }
            
            catch(SQLException e){
                e.printStackTrace(System.err);
            }
            return userList;
        }

        public void show_user() {
            ArrayList<itemLists> list = itemList();
            DefaultTableModel model = (DefaultTableModel) showItem.getModel();
            Object[] row = new Object[5];
            for (int i = 0; i < list.size(); i++) {
                row[0] = list.get(i).getID();
                row[1] = list.get(i).getName();
                row[2] = list.get(i).getQuantity();
                row[3] = list.get(i).getSellPrice();
                model.addRow(row);
            }
        }
        
//        private ArrayList<itemLists> autoSearch(){
//            ArrayList<itemLists> userList = new ArrayList<>();
//
//            try {
//                String iName = searchProd.getText();
//
//                String sql1 = "SELECT * FROM Bakal_tbl INNER JOIN Kahoy_tbl ON Bakal_tbl.ID = Kahoy_tbl.ID ORDER BY prodname = '"+iName+"'";
//                rs5 = st.executeQuery(sql1);
//
//                itemLists ilist1;
//                while (rs5.next()) {
//                    ilist1 = new itemLists(rs5.getInt("ID"), rs5.getString("prodname"), rs5.getInt("quantity"), rs5.getInt("buyingPrice"), rs5.getInt("sellingPrice"));
//                    userList.add(ilist1);
//                }
//
//            } catch (SQLException ex) {
//                Logger.getLogger(Order.class.getName()).log(Level.SEVERE, null, ex);
//            }
//
//            return userList;
//        }
//        
//        public void showUser(){       
//            try {
//                String iName = searchProd.getText().toLowerCase();
//  
//                String sql1 = "SELECT * FROM Bakal_tbl WHERE prodname LIKE '"+iName+"'";
//                rs5 = st.executeQuery(sql1);
//
//                while(rs5.next()) {
//                    DefaultTableModel model = new DefaultTableModel();
//                    model = (DefaultTableModel)showItem.getModel();
//                    model.addRow(new Object[]{
//                        rs5.getInt(1),
//                        rs5.getString(2),
//                        rs5.getInt(3),
//                        rs5.getInt(4),
//                        rs5.getInt(5)
//                    });
//                }
//
//            } catch (SQLException ex) {
//                Logger.getLogger(Order.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
        
        public void selectItem(){
            DefaultTableModel model = (DefaultTableModel) cart.getModel();
            String tblID = model.getValueAt(cart.getSelectedRow(), 0).toString();
            String tblName = model.getValueAt(cart.getSelectedRow(), 1).toString();
            String tblQuan = model.getValueAt(cart.getSelectedRow(), 4).toString();
            int tblCount = Integer.parseInt(tblQuan);
            
            try{
//                String sql = "SELECT * FROM "+itemCat+"_tbl WHERE ID = '"+tblID+"'";
//                rs = st.executeQuery(sql);
//                
//                if(rs.next()){
//                    String quanCount = rs.getString(3);
//                    int qtyCount1 = Integer.parseInt(quanCount);
//                    
//                    qtyCount.setModel(new javax.swing.SpinnerNumberModel(tblCount, 0, qtyCount1, 1));
//                    ((JSpinner.DefaultEditor) qtyCount.getEditor()).getTextField().setEditable(false);
//                }
                
                String sql = "SELECT * FROM Bakal_tbl WHERE prodname = '"+tblName+"'";
                rs2 = st.executeQuery(sql);
                
                if(rs2.next()){
                    String quanCount = rs2.getString(3);
                    int qtyCount1 = Integer.parseInt(quanCount);
                    
                    qtyCount.setModel(new javax.swing.SpinnerNumberModel(tblCount, 0, qtyCount1, 1));
                    ((JSpinner.DefaultEditor) qtyCount.getEditor()).getTextField().setEditable(false);
                    purCat.setText("Bakal");
                }
                else{
                    String sq7 = "SELECT * FROM Kahoy_tbl WHERE prodname = '"+tblName+"'";
                    rs4 = st.executeQuery(sq7);

                    if(rs4.next()){
                        String quanCount = rs4.getString(3);
                        int qtyCount1 = Integer.parseInt(quanCount);

                        qtyCount.setModel(new javax.swing.SpinnerNumberModel(tblCount, 0, qtyCount1, 1));
                        ((JSpinner.DefaultEditor) qtyCount.getEditor()).getTextField().setEditable(false);
                        purCat.setText("Kahoy");
                    }
                }
            }
            catch(SQLException e){
                e.printStackTrace(System.err);
            }
        }
        
        public void showI(){
            DefaultTableModel model = (DefaultTableModel) showItem.getModel();
            String tblName = model.getValueAt(showItem.getSelectedRow(), 1).toString();
            
            try{
                String sql = "SELECT * FROM Bakal_tbl WHERE prodname = '"+tblName+"'";
                rs6 = st.executeQuery(sql);
                
                if(rs6.next()){
                    purCat.setText("Bakal");
                }
                else{
                    String sq7 = "SELECT * FROM Kahoy_tbl WHERE prodname = '"+tblName+"'";
                    rs5 = st.executeQuery(sq7);

                    if(rs5.next()){
                        purCat.setText("Kahoy");
                    }
                }

            }
            catch(SQLException e){
                e.printStackTrace(System.err);
            }
        }
        
        public void checkoutItem(){
            try{
                //Add User Order History
                DateTimeFormatter dt = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                LocalDateTime now = LocalDateTime.now();
                String date = dt.format(now);
                
                DateTimeFormatter tm = DateTimeFormatter.ofPattern("HH:mm:ss");
                LocalDateTime now1 = LocalDateTime.now();
                String time = tm.format(now1);
                
                int amount = Integer.parseInt(Total.getText().trim());
                int pay = Integer.parseInt(Cash.getText().trim());
                int bal = Integer.parseInt(Balance.getText().trim());
                int uid = Integer.parseInt(hideID.getText().trim());
                String username = accName.getText().trim();
                
                String sql = "INSERT INTO UserOrderHistory(uid, uname, utotal, paid, bal, date, time) VALUES('"+uid+"', '"+username+"', '"+amount+"', '"+pay+"', '"+bal+"', '"+date+"', '"+time+"')";
                int save = st.executeUpdate(sql);
                
                if(save>0){
                    String sql5 = "SELECT * FROM UserOrderHistory WHERE uid = '"+uid+"'";
                    rs1 = st.executeQuery(sql5);
                    
                    if(rs1.next()){
                        int recid = rs1.getInt(2);
                        
                        if(recid == 0){
                            recid = recid + 1;

                            String sql6 = "UPDATE UserOrderHistory SET recID = ? WHERE uid = ?";
                            pst3 = con.prepareStatement(sql6);
                            
                            pst3.setInt(1, recid);
                            pst3.setInt(2, uid);

                            pst3.executeUpdate();
                        }
                        else{
                            String sql6 = "UPDATE UserOrderHistory SET recID = 1+ ? WHERE uid = ?";
                            pst3 = con.prepareStatement(sql6);
                            
                            pst3.setInt(1, recid);
                            pst3.setInt(2, uid);

                            pst3.executeUpdate();
                        }
                    }
                }
                else{
                    
                }
                //End of Adding User Order History
                
                //Add User Oder Purchase Individually
                String sql2 = "INSERT INTO ProductPurchased(oID, uID, uName, prodID, prodName, prodPrice, prodCat, prodQuan, prodSubT, dateT, TimeD) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                pst = con.prepareStatement(sql2);
                
                String sql4 = "SELECT * FROM UserOrderHistory WHERE uid = '"+uid+"'";
                rs = st.executeQuery(sql4);
                
                if(rs.next()){
                    int reid = rs.getInt(2);
                    pst.setInt(1, reid);
                        
                    for(int i = 0; i<cart.getRowCount(); i++){
                        String prodID = (String) cart.getValueAt(i, 0);
                        String prodName = (String) cart.getValueAt(i, 1);
                        String prodPrice = (String) cart.getValueAt(i, 2);
                        String prodCat = (String) cart.getValueAt(i, 3);
                        String prodQuan = (String) cart.getValueAt(i, 4);
                        String prodST = (String) cart.getValueAt(i, 5);

                        pst.setInt(2, uid);
                        pst.setString(3, username);
                        pst.setString(4, prodID);
                        pst.setString(5, prodName);
                        pst.setString(6, prodPrice);
                        pst.setString(7, prodCat);
                        pst.setString(8, prodQuan);
                        pst.setString(9, prodST);
                        pst.setString(10, date);
                        pst.setString(11, time);

                        pst.executeUpdate();
                    }
                    
//                    String sql5 = "UPDATE UserOrderHistory SET recID = '"+reid+"' WHERE date = '"+date+"' and time = '"+time+"'";
//                    int k = st.executeUpdate(sql5);
//                    
//                    if(k > 0){
//                        
//                    }
                }
                //End of Adding User Oder Purchase individually
                
                //Reducing the stock bought by user
                for(int i = 0; i<cart.getRowCount(); i++){
                    String cat = (String) cart.getValueAt(i, 3);
                    
                    String  sql3 = "UPDATE "+cat+"_tbl SET quantity = quantity - ? WHERE prodname = ?";
                    pst1 = con.prepareStatement(sql3);
                    
                    String productname = (String)cart.getValueAt(i, 1);
                    String qty = (String)cart.getValueAt(i, 4);
                    
                    pst1.setString(1, qty);
                    pst1.setString(2, productname);
                    
                    pst1.executeUpdate();
                }
                //End of reducing the stock bought by user
                
                //Changing the order number
//                String accountName = accName.getText();
                
//                String sql5 = "SELECT * FROM UserOrderHistory WHERE uname = '"+accountName+"'";
//                rs7 = st.executeQuery(sql5);
                
//                if(rs7.next()){
//                    String sql6 = "SELECT * FROM UserOrderHistory WHERE date = '"+date+"' and time = '"+time+"'";
//                    rs8 = st.executeQuery(sql6);

//                    if(rs8.next()){
//                        String sql7 = "SELECT * FROM ProductPurchased WHERE dateT = '"+date+"' and TimeD = '"+date+"'";
//                        rs9 = st.executeQuery(sql7);
//                        
//                        if(rs9.next()){
//                            String ordID = rs9.getString(2);
// 
//                            String sql6 = "SELECT * FROM UserOrderHistory WHERE date = '"+date+"' and time = '"+time+"'";
//                            rs8 = st.executeQuery(sql6);
//                                
//                            if(rs8.next()){
//                                String sql8 = "UPDATE UserOrderHistory SET recID = '"+ordID+"'";
//                                int x = st.executeUpdate(sql8);
//                                    
//                                if(x > 0){
//                                    
//                                }
//                            }
//                        }
//                    }
//                }
            }
            catch(SQLException e){
                e.printStackTrace(System.err);
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

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel7 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        accName = new javax.swing.JLabel();
        hideID = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        refresh1 = new javax.swing.JButton();
        prodList = new javax.swing.JComboBox<>();
        prodTitleName = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        purID = new javax.swing.JTextField();
        purName = new javax.swing.JTextField();
        addOrder = new javax.swing.JButton();
        home = new javax.swing.JButton();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        amount = new javax.swing.JTextField();
        subTotal = new javax.swing.JTextField();
        qtyCount = new javax.swing.JSpinner();
        jScrollPane3 = new javax.swing.JScrollPane();
        showItem = new javax.swing.JTable();
        jLabel16 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        cart = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        Total = new javax.swing.JTextField();
        Cash = new javax.swing.JTextField();
        Balance = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        delOrder = new javax.swing.JButton();
        upOrder = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        checkOut = new javax.swing.JButton();
        printBill = new javax.swing.JButton();
        catName = new javax.swing.JLabel();
        purCat = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        searchProd = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        setUndecorated(true);

        jPanel7.setBackground(new java.awt.Color(32, 34, 37));

        jLabel9.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(114, 118, 125));
        jLabel9.setText("VIAZONE TRADING");

        jLabel10.setFont(new java.awt.Font("Arial Black", 1, 20)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 51, 0));
        jLabel10.setText("X");
        jLabel10.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel10MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel10)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setBackground(new java.awt.Color(3, 127, 54));

        jLabel12.setFont(new java.awt.Font("Arial", 1, 60)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(210, 180, 64));
        jLabel12.setText("ORDER");

        jPanel1.setBackground(new java.awt.Color(3, 127, 54));
        jPanel1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 3, true));

        jLabel14.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("Logged in as:");

        accName.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        accName.setForeground(new java.awt.Color(210, 180, 64));
        accName.setText("CONSUMER");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addComponent(jLabel14))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(62, 62, 62)
                        .addComponent(accName)))
                .addContainerGap(40, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel14)
                .addGap(5, 5, 5)
                .addComponent(accName)
                .addContainerGap())
        );

        hideID.setForeground(new java.awt.Color(3, 127, 54));
        hideID.setText("jLabel8");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel12)
                .addGap(150, 150, 150)
                .addComponent(hideID)
                .addGap(107, 107, 107)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(jLabel12)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addComponent(hideID)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel8.setBackground(new java.awt.Color(3, 127, 54));

        jLabel17.setFont(new java.awt.Font("Arial", 1, 36)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(210, 180, 64));
        jLabel17.setText("VIAZONE Hardware and Construction Supply");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(273, 273, 273)
                .addComponent(jLabel17)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel17)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        refresh1.setBackground(new java.awt.Color(3, 127, 54));
        refresh1.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        refresh1.setForeground(new java.awt.Color(204, 255, 102));
        refresh1.setText("Load Items");
        refresh1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refresh1ActionPerformed(evt);
            }
        });

        prodList.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        prodList.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Bakal", "Kahoy" }));
        prodList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                prodListMouseClicked(evt);
            }
        });
        prodList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                prodListActionPerformed(evt);
            }
        });

        prodTitleName.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        prodTitleName.setForeground(new java.awt.Color(3, 127, 54));
        prodTitleName.setText("Product ID:");

        jLabel11.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(3, 127, 54));
        jLabel11.setText("Product Name:");

        jLabel13.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(3, 127, 54));
        jLabel13.setText("Quantity:");

        purID.setEditable(false);
        purID.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        purID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                purIDActionPerformed(evt);
            }
        });
        purID.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                purIDKeyTyped(evt);
            }
        });

        purName.setEditable(false);
        purName.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N

        addOrder.setBackground(new java.awt.Color(3, 127, 54));
        addOrder.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        addOrder.setForeground(new java.awt.Color(204, 255, 102));
        addOrder.setText("Add to Order");
        addOrder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addOrderActionPerformed(evt);
            }
        });

        home.setBackground(new java.awt.Color(3, 127, 54));
        home.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        home.setForeground(new java.awt.Color(204, 255, 102));
        home.setText("HOME");
        home.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                homeActionPerformed(evt);
            }
        });

        jLabel18.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(3, 127, 54));
        jLabel18.setText("Sub Total:");

        jLabel19.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(3, 127, 54));
        jLabel19.setText("Price:");

        amount.setEditable(false);
        amount.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        amount.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                amountKeyTyped(evt);
            }
        });

        subTotal.setEditable(false);
        subTotal.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N

        qtyCount.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        qtyCount.setModel(new javax.swing.SpinnerNumberModel(0, 0, 5, 1));
        qtyCount.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                qtyCountStateChanged(evt);
            }
        });
        qtyCount.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                qtyCountMouseClicked(evt);
            }
        });

        showItem.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        showItem.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Product Name", "Quantity", "Price"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Object.class, java.lang.Integer.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        showItem.getTableHeader().setReorderingAllowed(false);
        showItem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                showItemMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(showItem);
        if (showItem.getColumnModel().getColumnCount() > 0) {
            showItem.getColumnModel().getColumn(0).setPreferredWidth(10);
            showItem.getColumnModel().getColumn(1).setPreferredWidth(200);
        }

        jLabel16.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel16.setText("₱");

        jLabel20.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel20.setText("₱");

        jLabel21.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(3, 127, 54));
        jLabel21.setText("Filter:");

        cart.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        cart.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Product Name", "Price", "Category", "Quantity", "Sub Total"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Object.class, java.lang.Integer.class, java.lang.Object.class, java.lang.Integer.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        cart.getTableHeader().setReorderingAllowed(false);
        cart.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cartMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(cart);
        if (cart.getColumnModel().getColumnCount() > 0) {
            cart.getColumnModel().getColumn(0).setPreferredWidth(20);
            cart.getColumnModel().getColumn(1).setPreferredWidth(200);
            cart.getColumnModel().getColumn(3).setHeaderValue("Category");
        }

        jLabel1.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(3, 127, 54));
        jLabel1.setText("Total:");

        jLabel2.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(3, 127, 54));
        jLabel2.setText("Cash:");

        jLabel3.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(3, 127, 54));
        jLabel3.setText("Balance:");

        Total.setEditable(false);
        Total.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        Total.setText("0");
        Total.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TotalActionPerformed(evt);
            }
        });
        Total.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                TotalKeyTyped(evt);
            }
        });

        Cash.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        Cash.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                CashCaretUpdate(evt);
            }
        });
        Cash.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                CashMouseClicked(evt);
            }
        });
        Cash.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                CashKeyTyped(evt);
            }
        });

        Balance.setEditable(false);
        Balance.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        Balance.setText("0");
        Balance.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                BalanceKeyTyped(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel4.setText("₱");

        jLabel5.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel5.setText("₱");

        jLabel6.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel6.setText("₱");

        delOrder.setBackground(new java.awt.Color(3, 127, 54));
        delOrder.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        delOrder.setForeground(new java.awt.Color(204, 255, 102));
        delOrder.setText("Remove Order");
        delOrder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                delOrderActionPerformed(evt);
            }
        });

        upOrder.setBackground(new java.awt.Color(3, 127, 54));
        upOrder.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        upOrder.setForeground(new java.awt.Color(204, 255, 102));
        upOrder.setText("Update Order");
        upOrder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                upOrderActionPerformed(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(3, 127, 54));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 9, Short.MAX_VALUE)
        );

        checkOut.setBackground(new java.awt.Color(3, 127, 54));
        checkOut.setFont(new java.awt.Font("Arial", 1, 36)); // NOI18N
        checkOut.setForeground(new java.awt.Color(204, 255, 102));
        checkOut.setText("Checkout");
        checkOut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkOutActionPerformed(evt);
            }
        });

        printBill.setBackground(new java.awt.Color(3, 127, 54));
        printBill.setFont(new java.awt.Font("Arial", 1, 36)); // NOI18N
        printBill.setForeground(new java.awt.Color(204, 255, 102));
        printBill.setText("Print Bill");
        printBill.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printBillActionPerformed(evt);
            }
        });

        catName.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        catName.setForeground(new java.awt.Color(3, 127, 54));
        catName.setText("Category:");

        purCat.setEditable(false);
        purCat.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        purCat.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 240, 240)));

        jLabel8.setFont(new java.awt.Font("Arial", 1, 48)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(3, 127, 54));
        jLabel8.setText("CART");

        searchProd.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        searchProd.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                searchProdCaretUpdate(evt);
            }
        });
        searchProd.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                searchProdMouseClicked(evt);
            }
        });
        searchProd.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                searchProdKeyReleased(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(3, 127, 54));
        jLabel7.setText("Search:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(303, 303, 303)
                        .addComponent(jLabel8))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 783, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(44, 44, 44)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Total, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(Cash, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(Balance, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(checkOut)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(printBill))
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(delOrder)
                            .addGap(39, 39, 39)
                            .addComponent(upOrder))))
                .addGap(52, 52, 52))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 784, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel21)
                        .addGap(18, 18, 18)
                        .addComponent(prodList, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(refresh1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel7)
                        .addGap(18, 18, 18)
                        .addComponent(searchProd, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(home, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(69, 69, 69))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(103, 103, 103)
                                .addComponent(catName)
                                .addGap(18, 18, 18)
                                .addComponent(purCat, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(69, 69, 69)
                                .addComponent(addOrder))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(44, 44, 44)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel13)
                                    .addComponent(jLabel19)
                                    .addComponent(jLabel18)
                                    .addComponent(jLabel11)
                                    .addComponent(prodTitleName))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(qtyCount, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel20)
                                            .addComponent(jLabel16))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(subTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(amount, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(purID, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(purName, javax.swing.GroupLayout.PREFERRED_SIZE, 253, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addContainerGap(49, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(catName)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(9, 9, 9)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel21)
                                    .addComponent(prodList, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(refresh1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(searchProd, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel7))))
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 323, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(prodTitleName)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(purCat, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(purID, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(19, 19, 19)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(purName, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(qtyCount, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel13))
                        .addGap(14, 14, 14)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel20)
                            .addComponent(jLabel19)
                            .addComponent(amount, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel16)
                            .addComponent(jLabel18)
                            .addComponent(subTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(addOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(home, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(11, 11, 11)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(delOrder)
                            .addComponent(upOrder))
                        .addGap(66, 66, 66)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(Total, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6)
                            .addComponent(jLabel1))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(Cash, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4)
                            .addComponent(jLabel2))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(Balance, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5)
                            .addComponent(jLabel3))
                        .addGap(60, 60, 60)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(checkOut)
                            .addComponent(printBill)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 41, Short.MAX_VALUE)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void refresh1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refresh1ActionPerformed
        orderCommands go = new orderCommands();
        DefaultTableModel model = (DefaultTableModel) showItem.getModel();
        model.setRowCount(0);
        go.show_user();
        clearFields();
        
        cart.getSelectionModel().clearSelection();
    }//GEN-LAST:event_refresh1ActionPerformed
    
    private void prodListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_prodListMouseClicked

    }//GEN-LAST:event_prodListMouseClicked

    private void prodListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_prodListActionPerformed

    }//GEN-LAST:event_prodListActionPerformed

    private void purIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_purIDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_purIDActionPerformed

    private void homeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_homeActionPerformed
        String lbl = accName.getText();
        String lbl1 = hideID.getText();
        String lbl2 = "";
        Consumer c = new Consumer(lbl, lbl2, lbl1);
        c.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_homeActionPerformed

    private void purIDKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_purIDKeyTyped
        if(!Character.isDigit(evt.getKeyChar())){
            evt.consume();
        }
    }//GEN-LAST:event_purIDKeyTyped

    private void qtyCountStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_qtyCountStateChanged
        if(amount.getText().isEmpty()){
            return;
        }
        else{
            int qty = Integer.parseInt(qtyCount.getValue().toString());
            int price = Integer.parseInt(amount.getText());

            int tot = qty * price;

            subTotal.setText(String.valueOf(tot));
        }
    }//GEN-LAST:event_qtyCountStateChanged

    private void showItemMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_showItemMouseClicked
        if(prodList.getSelectedItem().equals("Bakal")){
            purCat.setText("Bakal");
        }
        else if(prodList.getSelectedItem().equals("Kahoy")){
            purCat.setText("Kahoy");
        }
        else{
            JOptionPane.showMessageDialog(null, "Please select a category!");
        }
        
        orderCommands go = new orderCommands();
        go.showI();

        int rowIndex = showItem.getSelectedRow();
 
        purID.setText(showItem.getValueAt(rowIndex, 0).toString());
        purName.setText(showItem.getValueAt(rowIndex, 1).toString());
        qtyCount.setModel(new javax.swing.SpinnerNumberModel(0, 0, Integer.parseInt(showItem.getValueAt(rowIndex, 2).toString()), 1));
        ((JSpinner.DefaultEditor) qtyCount.getEditor()).getTextField().setEditable(false);
        amount.setText(showItem.getValueAt(rowIndex, 3).toString());
        subTotal.setText(null);
        
        cart.getSelectionModel().clearSelection();
        
        addOrder.setEnabled(true);
        delOrder.setEnabled(false);
        upOrder.setEnabled(false);
        
        Cash.setText(null);
        Balance.setText(null);
    }//GEN-LAST:event_showItemMouseClicked

    private void amountKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_amountKeyTyped
        if(!Character.isDigit(evt.getKeyChar())){
            evt.consume();
        }
    }//GEN-LAST:event_amountKeyTyped

    private void clearFields(){
        purID.setText("");
        purCat.setText("");
        purName.setText("");
        qtyCount.setValue(0);
        amount.setText("");
        subTotal.setText("");
    }
    
    private boolean fieldsChecker(){
        return !(purID.getText().equals("") || purName.getText().equals("") || qtyCount.getValue().equals("") || amount.getText().equals("") || subTotal.getText().equals("") || qtyCount.getValue().equals(0));
    }
    
    private void addOrderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addOrderActionPerformed
        if(fieldsChecker()){
            ArrayList<String> list1 = new ArrayList<>();
            String name = purName.getText();
            
            itemNames.add(purName.getText());
            String[] arName = itemNames.toArray(new String[0]);
            for (String arName1 : arName) {
                if (!arN.contains(arName1)){
                    arN.add(arName1);
                    quantity.add(qtyCount.getValue().toString());
                    itemPrices.add(amount.getText());
                    subtotal.add(subTotal.getText());
                    
                    getCartTotal();
                    showItem.getSelectionModel().clearSelection();
                    
                    JOptionPane.showMessageDialog(null, "Item Added Successfully!");
                }
                else{
                    for(int row = 0; row < cart.getRowCount(); row++) {
                       list1.add(cart.getValueAt(row, 1).toString());  
                    }
                }
            }
            clearFields();
            qtyCount.setModel(new javax.swing.SpinnerNumberModel(0, 0, 0, 1));

            if(list1.contains(name)){
                JOptionPane.showMessageDialog(null, "Item Already Exist!");
            }
        }
        else{
            JOptionPane.showMessageDialog(null, "Some Fields are Empty!");
        }
        
        ((JSpinner.DefaultEditor) qtyCount.getEditor()).getTextField().setEditable(false);
        
    }//GEN-LAST:event_addOrderActionPerformed

    private void addCartList(){
        DefaultTableModel model = new DefaultTableModel();
        model = (DefaultTableModel)cart.getModel();
        model.addRow(new Object[]{
            purID.getText(),
            purName.getText(),
            amount.getText(),
            purCat.getText(),
            qtyCount.getValue().toString(),
            subTotal.getText()
        });
    }
    
    private void updateCartList(){
        DefaultTableModel model = (DefaultTableModel) cart.getModel();
        
        String pID = purID.getText();
        String pCat = purCat.getText();
        String pName = purName.getText();
        String pQuan = qtyCount.getValue().toString();
        String pAmount = amount.getText();
        String pSubTotal = subTotal.getText();
        
        model.setValueAt(pID, cart.getSelectedRow(), 0);
        model.setValueAt(pName, cart.getSelectedRow(), 1);
        model.setValueAt(pAmount, cart.getSelectedRow(), 2);
        model.setValueAt(pCat, cart.getSelectedRow(), 3);
        model.setValueAt(pQuan, cart.getSelectedRow(), 4);
        model.setValueAt(pSubTotal, cart.getSelectedRow(), 5);
        
        quantity.set(cart.getSelectedRow(), qtyCount.getValue().toString());
    }
    
    private void getTotal(){
        int sum = 0;
        for(int i = 0; i<cart.getRowCount(); i++){
            sum  = sum + Integer.parseInt(cart.getValueAt(i, 5).toString());
        }
        Total.setText(Integer.toString(sum));
    }
    
    private void getCartTotal(){     
        addCartList();
        getTotal();
        clearFields();
    }
    
    private void TotalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TotalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TotalActionPerformed

    private void cartMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cartMouseClicked
        DefaultTableModel model = (DefaultTableModel) cart.getModel();
        
        String tblID = model.getValueAt(cart.getSelectedRow(), 0).toString();
        String tblName = model.getValueAt(cart.getSelectedRow(), 1).toString();
        String tblAmount = model.getValueAt(cart.getSelectedRow(), 2).toString();
        String tblCat = model.getValueAt(cart.getSelectedRow(), 3).toString();
        String tblQuan = model.getValueAt(cart.getSelectedRow(), 4).toString();
        String tblST = model.getValueAt(cart.getSelectedRow(), 5).toString();
        
        int countQty = Integer.parseInt(tblQuan);
        
        purID.setText(tblID);
        purName.setText(tblName);
        amount.setText(tblAmount);
        purCat.setText(tblCat);
        qtyCount.setValue(countQty);
        subTotal.setText(tblST);
        
        showItem.getSelectionModel().clearSelection();
        
        addOrder.setEnabled(false);
        delOrder.setEnabled(true);
        upOrder.setEnabled(true);
        
        Cash.setText(null);
        Balance.setText(null);
        
        ((JSpinner.DefaultEditor) qtyCount.getEditor()).getTextField().setEditable(false);
        
        orderCommands go = new orderCommands();
        go.selectItem();
    }//GEN-LAST:event_cartMouseClicked

    private void delOrderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_delOrderActionPerformed
        DefaultTableModel model = (DefaultTableModel) cart.getModel();
        
        if(cart.getSelectedRow()>=0){
            model.removeRow(cart.getSelectedRow());
            getTotal();
            JOptionPane.showMessageDialog(null, "Item removed!");
        }
        else{
            JOptionPane.showMessageDialog(null, "Please select an item!");
        }
        
        arN.remove(purName.getText());
        quantity.remove(qtyCount.getValue().toString());
        itemPrices.remove(amount.getText());
        subtotal.remove(subTotal.getText());
        
        cart.getSelectionModel().clearSelection();
        
        getTotal();
        clearFields();
        qtyCountSetter();
    }//GEN-LAST:event_delOrderActionPerformed

    private void upOrderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_upOrderActionPerformed
        if(Integer.parseInt(qtyCount.getValue().toString())==0){
            JOptionPane.showMessageDialog(null, "Please select atleast 1 quantity or remove the item!");
            clearFields();
        }
        else{
            updateCartList();
            getTotal();
            clearFields();
            JOptionPane.showMessageDialog(null, "Item updated!");
        }
        cart.getSelectionModel().clearSelection();
        qtyCountSetter();
    }//GEN-LAST:event_upOrderActionPerformed

    private void TotalKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TotalKeyTyped
        if(!Character.isDigit(evt.getKeyChar())){
            evt.consume();
        }
    }//GEN-LAST:event_TotalKeyTyped

    private void CashKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_CashKeyTyped
        if(!Character.isDigit(evt.getKeyChar())){
            evt.consume();
        }
    }//GEN-LAST:event_CashKeyTyped

    private void BalanceKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BalanceKeyTyped
        if(!Character.isDigit(evt.getKeyChar())){
            evt.consume();
        }
    }//GEN-LAST:event_BalanceKeyTyped

    private void printBillActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printBillActionPerformed
        if(arN.isEmpty()){
            JOptionPane.showMessageDialog(this, "Please select atleast 1 product!");
        }
        else{
            getInvoice();
        }
    }//GEN-LAST:event_printBillActionPerformed

    private void getInvoice(){
        if(Total.getText().equals("") || Cash.getText().equals("") || Balance.getText().equals("") || Integer.parseInt(Balance.getText()) < 0 || Integer.parseInt(Total.getText()) > Integer.parseInt(Cash.getText())){
            JOptionPane.showMessageDialog(this, "Please Enter a Valid Amount!");
        }
        else{
            bHeight = Double.valueOf(arN.size());
            
            PrinterJob pj = PrinterJob.getPrinterJob();        
            pj.setPrintable(new BillPrintable(), getPageFormat(pj));
            try{
                pj.print();
            }
            catch (PrinterException ex) {
                ex.printStackTrace(System.err);
            }
            
            clearFields();
        }
    }
    
    private void CashCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_CashCaretUpdate
        if(Cash.getText().isEmpty()){
            return;
        }
        else{
            int totAmount = Integer.parseInt(Total.getText());
            int payCash = Integer.parseInt(Cash.getText());

            int bal = payCash - totAmount;

            Balance.setText(String.valueOf(bal));
        }
    }//GEN-LAST:event_CashCaretUpdate

    private void CashMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CashMouseClicked
        showItem.getSelectionModel().clearSelection();
        cart.getSelectionModel().clearSelection();
        
        clearFields();
        qtyCountSetter();
    }//GEN-LAST:event_CashMouseClicked

    private void qtyCountMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_qtyCountMouseClicked

    }//GEN-LAST:event_qtyCountMouseClicked

    private void clearArrayofOrder(){
        itemNames.clear();
        quantity.clear();
        itemPrices.clear();
        subtotal.clear();
        arN.clear();
    }
    
    private void checkOutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkOutActionPerformed
        if(arN.isEmpty()){
            JOptionPane.showMessageDialog(null, "Please select at least 1 product!");
        }
        if(Cash.getText().equals("")){
            JOptionPane.showMessageDialog(null, "Please Enter Amount to Pay!");
        }
        else{
            if(Integer.parseInt(Balance.getText()) >= 0){
                orderCommands go = new orderCommands();
                go.checkoutItem();
                JOptionPane.showMessageDialog(null, "Purchased Completed!");

                go.show_user();
                clearFields();
                getInvoice();
                clearArrayofOrder();

                DefaultTableModel model = (DefaultTableModel) cart.getModel();
                model.setRowCount(0);
                Total.setText("");
                Cash.setText("");
                Balance.setText("");
            }
            else{
                JOptionPane.showMessageDialog(null, "Please Enter a Valid Amount to Pay!");
            }
        }
    }//GEN-LAST:event_checkOutActionPerformed

    private void searchProdCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_searchProdCaretUpdate

    }//GEN-LAST:event_searchProdCaretUpdate

    private void searchProdKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchProdKeyReleased
//        String name = searchProd.getText();
//        
//        DefaultTableModel dtm = (DefaultTableModel) showItem.getModel();
//        
//        int i = showItem.getRowCount();
//        while(i-->0){
//            dtm.removeRow(i);
//        }
//        
//        try {
//            ResultSet rs9 = dbShow.pst(name);
//            while(rs9.next()){
//                java.util.Vector v = new java.util.Vector();
//                v.add(rs9.getInt("ID"));
//                v.add(rs9.getString("prodname"));
//                v.add(rs9.getInt("quantity"));
//                v.add(rs9.getInt("buyingPrice"));
//                v.add(rs9.getInt("sellingPrice"));
//                dtm.addRow(v);
//            }
//            
//            ResultSet rs10 = dbShow.pst1(name); 
//            while(rs10.next()){
//                java.util.Vector v = new java.util.Vector();
//                v.add(rs10.getInt("ID"));
//                v.add(rs10.getString("prodname"));
//                v.add(rs10.getInt("quantity"));
//                v.add(rs10.getInt("buyingPrice"));
//                v.add(rs10.getInt("sellingPrice"));
//                dtm.addRow(v);
//            }
//        } catch (SQLException ex) {
//            Logger.getLogger(Order.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }//GEN-LAST:event_searchProdKeyReleased

    private void searchProdMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_searchProdMouseClicked
        searchProdKeyReleased(null);
    }//GEN-LAST:event_searchProdMouseClicked

    private void jLabel10MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel10MouseClicked
        System.exit(0);
    }//GEN-LAST:event_jLabel10MouseClicked
    
    public PageFormat getPageFormat(PrinterJob pj){
        PageFormat pf = pj.defaultPage();
        Paper paper = pf.getPaper();    

        double bodyHeight = bHeight;  
        double headerHeight = 5.0;                  
        double footerHeight = 5.0;        
        double width = cm_to_pp(8); 
        double height = cm_to_pp(headerHeight+bodyHeight+footerHeight); 
        paper.setSize(width, height);
        paper.setImageableArea(0,10,width,height - cm_to_pp(1));  

        pf.setOrientation(PageFormat.PORTRAIT);  
        pf.setPaper(paper);    

        return pf;
    }
    
    protected static double cm_to_pp(double cm){            
	return toPPI(cm * 0.393600787);            
    }
 
    protected static double toPPI(double inch){            
	return inch * 72d;            
    }
    
    public class BillPrintable implements Printable{
        @Override
        public int print(Graphics graphics, PageFormat pageFormat,int pageIndex) 
            throws PrinterException{ 
                DateTimeFormatter dt = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();
                String date = dt.format(now);
                
                int r= arN.size();
                ImageIcon icon=new ImageIcon("C:\\Users\\Cabuyao Col\\OneDrive\\Documents\\NetBeansProjects\\StockManagement\\src\\images\\StoreLogo.png"); 
                int result = NO_SUCH_PAGE;    
                if (pageIndex == 0) {                    
                    Graphics2D g2d = (Graphics2D) graphics;                    
                    double width = pageFormat.getImageableWidth();                               
                    g2d.translate((int) pageFormat.getImageableX(),(int) pageFormat.getImageableY()); 
 
            try{
                int y=20;
                int yShift = 10;
                int headerRectHeight=15;       
                
                g2d.setFont(new Font("Monospaced",Font.PLAIN,9));
                g2d.drawImage(icon.getImage(),65, -17, 90, 90, rootPane);y+=yShift+30;
                g2d.drawString("-------------------------------------",12,y);y+=yShift;
                g2d.drawString("          VIAZONETrading.com         ",12,y);y+=yShift;
                g2d.drawString("     168 Mamatid Cabuyao, Laguna     ",12,y);y+=yShift;
                g2d.drawString("       www.facebook.com/VIAZONE      ",12,y);y+=yShift;
                g2d.drawString("            +639162964676            ",12,y);y+=yShift;
                g2d.drawString("         "+date+"           ",12,y);y+=yShift;
                g2d.drawString("-------------------------------------",12,y);y+=headerRectHeight;

                g2d.drawString(" Item Name                  Price    ",10,y);y+=yShift;
                g2d.drawString("-------------------------------------",10,y);y+=headerRectHeight;

                for(int s=0; s<r; s++){
                g2d.drawString(" "+arN.get(s)+"                            ",10,y);y+=yShift;
                g2d.drawString("      "+quantity.get(s)+" * ₱"+itemPrices.get(s),10,y);g2d.drawString(" ₱"+subtotal.get(s),160,y);y+=yShift;
                }
                
                g2d.drawString("-------------------------------------",10,y);y+=yShift;
                g2d.drawString(" Total     :                  ₱"+Total.getText()+"   ",10,y);y+=yShift;
                g2d.drawString("-------------------------------------",10,y);y+=yShift;
                g2d.drawString(" Cash      :                  ₱"+Cash.getText()+"   ",10,y);y+=yShift;
                g2d.drawString("-------------------------------------",10,y);y+=yShift;
                g2d.drawString(" Balance   :                  ₱"+Balance.getText()+"   ",10,y);y+=yShift;

                g2d.drawString("*************************************",10,y);y+=yShift;
                g2d.drawString("        THANK YOU COME AGAIN         ",10,y);y+=yShift;
                g2d.drawString("*************************************",10,y);y+=yShift;
                g2d.drawString("       SOFTWARE BY: BSCS201A         ",10,y);y+=yShift;
                g2d.drawString(" CONTACT: viazone.trading@gmail.com  ",10,y);y+=yShift;       
           
            }
            catch(Exception e){
                e.printStackTrace(System.err);
            }
            result = PAGE_EXISTS;    
          }    
          return result;    
      }
   }
    
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
            java.util.logging.Logger.getLogger(Order.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Order.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Order.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Order.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Order().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField Balance;
    private javax.swing.JTextField Cash;
    private javax.swing.JTextField Total;
    private javax.swing.JLabel accName;
    private javax.swing.JButton addOrder;
    private javax.swing.JTextField amount;
    private javax.swing.JTable cart;
    private javax.swing.JLabel catName;
    private javax.swing.JButton checkOut;
    private javax.swing.JButton delOrder;
    private javax.swing.JLabel hideID;
    private javax.swing.JButton home;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JButton printBill;
    private javax.swing.JComboBox<String> prodList;
    private javax.swing.JLabel prodTitleName;
    private javax.swing.JTextField purCat;
    private javax.swing.JTextField purID;
    private javax.swing.JTextField purName;
    private javax.swing.JSpinner qtyCount;
    private javax.swing.JButton refresh1;
    private javax.swing.JTextField searchProd;
    private javax.swing.JTable showItem;
    private javax.swing.JTextField subTotal;
    private javax.swing.JButton upOrder;
    // End of variables declaration//GEN-END:variables
}
