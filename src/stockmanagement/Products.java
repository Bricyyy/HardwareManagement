/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stockmanagement;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
/**
 *
 * @author Cabuyao Col
 */
public class Products extends javax.swing.JFrame {
    Connection con;
    ResultSet rs;
    ResultSet rs3;
    ResultSet rs4;
    ResultSet rs5;
    ResultSet rs6;
    ResultSet rs7;
    Statement st;

    /**
     * Creates new form Products
     */
    
    
    public Products() {
        checkConnections();
        initComponents();
        selectionModeList();
        columnHider();
        elementHider();
        tableSorter();
        
        searchKeyReleased(null);
        this.setLocationRelativeTo(null);
    }
    
    public Products(String uname){
        checkConnections();
        initComponents();
        columnHider();
        elementHider();
        selectionModeList();
        tableSorter();
        
        searchKeyReleased(null);
        this.setLocationRelativeTo(null);
        
        accountName.setText(uname);
    }
    
    public void elementHider(){
        pID.setVisible(false);
        idTitle.setVisible(false);
    }
    
    private void tableSorter(){
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(itemList.getModel());
        itemList.setRowSorter(sorter);
        List<RowSorter.SortKey> sortKeys = new ArrayList<>();

        int columnIndexForName = 1;
        sortKeys.add(new RowSorter.SortKey(columnIndexForName, SortOrder.ASCENDING));
    }
    
    public void showI(){
        DefaultTableModel model = (DefaultTableModel) itemList.getModel();
        String tblName = model.getValueAt(itemList.getSelectedRow(), 1).toString();
            
        try{
            String sql = "SELECT * FROM Bakal_tbl WHERE prodname = '"+tblName+"'";
            rs6 = st.executeQuery(sql);
                
            if(rs6.next()){
                category.setSelectedItem("Bakal");
            }
            else{
                String sq7 = "SELECT * FROM Kahoy_tbl WHERE prodname = '"+tblName+"'";
                rs5 = st.executeQuery(sq7);

                if(rs5.next()){
                    category.setSelectedItem("Kahoy");
                }
            }

        }
        catch(SQLException e){
            e.printStackTrace(System.err);
        }
    }
            
        public class Command extends Products {

        String type = category.getSelectedItem().toString();
        String prodID = pID.getText().trim();
        String pName = pname.getText().trim();
        String quantity = quan.getText().trim();
        String buyPrice = bprice.getText().trim();
        String sellPrice = sprice.getText().trim();


        public void update(){
            String cat = category.getSelectedItem().toString();
            String pid = pID.getText().trim();
            String prodname = pname.getText().trim();
            String quant = quan.getText().trim();
            String buyp = bprice.getText().trim();
            String sellp = sprice.getText().trim();

                try {
                    String q1 = "UPDATE "+cat+"_tbl SET prodName = '"+prodname+"', quantity = '"+quant+"', buyingPrice = '"+buyp+"', sellingPrice = '"+sellp+"' WHERE ID = '"+pid+"'";
                    int x = st.executeUpdate(q1);
                    if (x > 0) {
                        DefaultTableModel model = (DefaultTableModel) itemList.getModel();
                        model.setRowCount(0);
                        show_user();
                        
                        JOptionPane.showMessageDialog(null, "Updated Succesfully!", "UPDATED", 2);
                    } else {
                        JOptionPane.showMessageDialog(null, "Updated Failed!", "ERROR", 2);
                    }
                } catch (SQLException e) {
                    e.printStackTrace(System.err);
                }
        }
        
        public void delete(){
                try {
                    String q1 = "DELETE FROM " +type+"_tbl WHERE ID = '"+prodID+"'";
                    int x = st.executeUpdate(q1);
                    if (x > 0) {
                        DefaultTableModel model = (DefaultTableModel) itemList.getModel();
                        model.setRowCount(0);
                        show_user();

                        JOptionPane.showMessageDialog(null, "Deleted Succesfully!", "DELETED", 2);
                    } else {
                        JOptionPane.showMessageDialog(null, "Delete Failed!", "ERROR", 2);
                    }
                } catch (SQLException e) {
                    e.printStackTrace(System.err);
                }
        }
        

        public void add(){
            String cat = category.getSelectedItem().toString();
            String pid = pID.getText().trim();
            String prodname = pname.getText().trim().toUpperCase();
            String quant = quan.getText().trim();
            String buyp = bprice.getText().trim();
            String sellp = sprice.getText().trim();
            
            
//            if(pName.equals("") || quantity.equals("") || buyPrice.equals("") || sellPrice.equals("")){
//                JOptionPane.showMessageDialog(rootPane, "Some Fields are Empty", "Empty Fields", 2);
//            }
//            else{
//                try {
//                    String q1 = "INSERT INTO "+cat+"_tbl(prodname, quantity, buyingPrice, sellingPrice) VALUES('"+prodname+"','"+quant+"','"+buyp+"','"+ sellp+"')";
//                    int x = st.executeUpdate(q1);
//                    
//                    if (x > 0) {
//                        DefaultTableModel model = (DefaultTableModel) itemList.getModel();
//                        model.setRowCount(0);
//                        show_user();
//
//                        JOptionPane.showMessageDialog(null, "Added Succesfully!", "ADDED", 2);
//                    }
//                    else {
//                        JOptionPane.showMessageDialog(null, "Add Failed!", "ERROR", 2);
//                    }
//                } catch (SQLException e) {
//                    e.printStackTrace(System.err);
//                }
//            }
            
//            if(fieldsChecker()){
                try {
                    String q1 = "INSERT INTO "+cat+"_tbl(prodname, quantity, buyingPrice, sellingPrice) VALUES('"+prodname+"','"+quant+"','"+buyp+"','"+ sellp+"')";
                    int x = st.executeUpdate(q1);
                    
                    if (x > 0) {
                        DefaultTableModel model = (DefaultTableModel) itemList.getModel();
                        model.setRowCount(0);
                        show_user();

                        JOptionPane.showMessageDialog(null, "Added Succesfully!", "ADDED", 2);
                    }
                    else {
                        JOptionPane.showMessageDialog(null, "Add Failed!", "ERROR", 2);
                    }
                } catch (SQLException e) {
                    e.printStackTrace(System.err);
                }
//            }
        }

        public ArrayList<itemLists> itemList() {
            ArrayList<itemLists> userList = new ArrayList<>();

            try {
                String ab = refType.getSelectedItem().toString();

                if(ab.equals("All")){
                    itemLists ilist;
                    itemLists i1list;
                    
                    String q2 = "SELECT * FROM Bakal_tbl";
                    rs4 = st.executeQuery(q2);
                    while (rs4.next()) {
                        ilist = new itemLists(rs4.getInt("ID"), rs4.getString("prodName"), rs4.getInt("quantity"), rs4.getInt("buyingPrice"), rs4.getInt("sellingPrice"));
                        userList.add(ilist);
                    }
                    
                    String q3 = "SELECT * FROM Kahoy_tbl";
                    rs3 = st.executeQuery(q3);
                    while (rs3.next()) {
                        i1list = new itemLists(rs3.getInt("ID"), rs3.getString("prodName"), rs3.getInt("quantity"), rs3.getInt("buyingPrice"), rs3.getInt("sellingPrice"));
                        userList.add(i1list);
                    }
                }
                
                String q2 = "SELECT * FROM " + ab + "_tbl";
                rs = st.executeQuery(q2);
                itemLists ilist;
                while (rs.next()) {
                    ilist = new itemLists(rs.getInt("ID"), rs.getString("prodName"), rs.getInt("quantity"), rs.getInt("buyingPrice"), rs.getInt("sellingPrice"));
                    userList.add(ilist);
                }
            } catch (SQLException e) {
                e.printStackTrace(System.err);
            }
            return userList;
        }

        public void show_user() {
            ArrayList<itemLists> list = itemList();
            DefaultTableModel model = (DefaultTableModel) itemList.getModel();
            Object[] row = new Object[5];
            for (int i = 0; i < list.size(); i++) {
                row[0] = list.get(i).getID();
                row[1] = list.get(i).getName();
                row[2] = list.get(i).getQuantity();
                row[3] = list.get(i).getBuyPrice();
                row[4] = list.get(i).getSellPrice();
                model.addRow(row);
            }
        }
    }
        
    public boolean itemChecker(String pname, String cat){
        boolean itemExist = false;
        try{
            String sql1 = "SELECT * FROM "+cat+"_tbl WHERE prodname = '"+pname+"'";
            rs7 = st.executeQuery(sql1);
            
            if(rs7.next()){
                itemExist = true;
            }
        }
        catch(SQLException e){
            e.printStackTrace(System.err);
        }
        return itemExist;
    }
    
    private void columnHider(){
        itemList.getColumnModel().getColumn(0).setMinWidth(0);
        itemList.getColumnModel().getColumn(0).setMaxWidth(0);
    }
        
    public boolean fieldsChecker(){
        String option = category.getSelectedItem().toString();
        String prodID = pID.getText();
        String pName = pname.getText();
        String quantity = quan.getText();
        String buyPrice = bprice.getText();
        String sellPrice = sprice.getText();
        
        if(pName.equals("") || quantity.equals("") || buyPrice.equals("") || sellPrice.equals("")){
            JOptionPane.showMessageDialog(rootPane, "Some Fields are Empty", "Empty Fields", 2);
            return false;
        }
        else if(itemChecker(pName, option)){
            JOptionPane.showMessageDialog(rootPane, "This item already exist!", "Item Failed", 2);
            clearFields();
            return false;
        }
        else{
            return true;
        }
    }
    
    public boolean fieldsCheckerUD(){
        String option = category.getSelectedItem().toString();
        String prodID = pID.getText();
        String pName = pname.getText();
        String quantity = quan.getText();
        String buyPrice = bprice.getText();
        String sellPrice = sprice.getText();
        
        if(pName.equals("") || quantity.equals("") || buyPrice.equals("") || sellPrice.equals("")){
            JOptionPane.showMessageDialog(rootPane, "Some Fields are Empty", "Empty Fields", 2);
            return false;
        }
        else{
            return true;
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

        jPanel3 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        itemList = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        accountName = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        pname = new javax.swing.JTextField();
        quan = new javax.swing.JTextField();
        bprice = new javax.swing.JTextField();
        sprice = new javax.swing.JTextField();
        category = new javax.swing.JComboBox<>();
        add = new javax.swing.JButton();
        update = new javax.swing.JButton();
        delete = new javax.swing.JButton();
        idTitle = new javax.swing.JLabel();
        pID = new javax.swing.JTextField();
        refType = new javax.swing.JComboBox<>();
        refresh = new javax.swing.JButton();
        back = new javax.swing.JButton();
        search = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        showAll = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

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

        jPanel6.setBackground(new java.awt.Color(32, 34, 37));
        jPanel6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel6MouseClicked(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(114, 118, 125));
        jLabel2.setText("VIAZONE TRADING");

        jLabel9.setFont(new java.awt.Font("Arial Black", 1, 20)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 51, 0));
        jLabel9.setText("X");
        jLabel9.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel9MouseClicked(evt);
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
                .addComponent(jLabel9)
                .addGap(25, 25, 25))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel9))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        itemList.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        itemList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Product Name", "Quantity", "Buying Price", "Selling Price"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.Integer.class, java.lang.Double.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        itemList.getTableHeader().setReorderingAllowed(false);
        itemList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                itemListMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(itemList);
        if (itemList.getColumnModel().getColumnCount() > 0) {
            itemList.getColumnModel().getColumn(1).setPreferredWidth(200);
        }

        jPanel2.setBackground(new java.awt.Color(3, 127, 54));

        jLabel5.setFont(new java.awt.Font("Arial", 1, 60)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(210, 180, 64));
        jLabel5.setText("ITEM LIST");

        jPanel5.setBackground(new java.awt.Color(3, 127, 54));
        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 3));

        jLabel15.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("Logged in as:");

        accountName.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        accountName.setForeground(new java.awt.Color(210, 180, 64));
        accountName.setText("Admin");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap(23, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel15)
                        .addGap(21, 21, 21))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addComponent(accountName)
                        .addGap(72, 72, 72))))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(accountName)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel5)
                .addGap(260, 260, 260)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel1.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(3, 127, 54));
        jLabel1.setText("Product Name:");

        jLabel3.setBackground(new java.awt.Color(0, 102, 0));
        jLabel3.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(3, 127, 54));
        jLabel3.setText("Product Type:");

        jLabel4.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(3, 127, 54));
        jLabel4.setText("Quantity:");

        jLabel6.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(3, 127, 54));
        jLabel6.setText("Buying Price:");

        jLabel7.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(3, 127, 54));
        jLabel7.setText("Selling Price:");

        pname.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N

        quan.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        quan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                quanKeyTyped(evt);
            }
        });

        bprice.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        bprice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bpriceActionPerformed(evt);
            }
        });
        bprice.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                bpriceKeyTyped(evt);
            }
        });

        sprice.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        sprice.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                spriceKeyTyped(evt);
            }
        });

        category.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        category.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Bakal", "Kahoy" }));
        category.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                categoryActionPerformed(evt);
            }
        });

        add.setBackground(new java.awt.Color(3, 127, 54));
        add.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        add.setForeground(new java.awt.Color(204, 255, 102));
        add.setText("Add ");
        add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addActionPerformed(evt);
            }
        });

        update.setBackground(new java.awt.Color(3, 127, 54));
        update.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        update.setForeground(new java.awt.Color(204, 255, 102));
        update.setText("Update ");
        update.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateActionPerformed(evt);
            }
        });

        delete.setBackground(new java.awt.Color(3, 127, 54));
        delete.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        delete.setForeground(new java.awt.Color(204, 255, 102));
        delete.setText("Delete ");
        delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteActionPerformed(evt);
            }
        });

        idTitle.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        idTitle.setForeground(new java.awt.Color(3, 127, 54));
        idTitle.setText("Product ID:");

        pID.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        pID.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                pIDKeyTyped(evt);
            }
        });

        refType.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        refType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Bakal", "Kahoy" }));
        refType.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                refTypeMouseClicked(evt);
            }
        });
        refType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refTypeActionPerformed(evt);
            }
        });

        refresh.setBackground(new java.awt.Color(3, 127, 54));
        refresh.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        refresh.setForeground(new java.awt.Color(204, 255, 102));
        refresh.setText("Filter");
        refresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshActionPerformed(evt);
            }
        });

        back.setBackground(new java.awt.Color(3, 127, 54));
        back.setFont(new java.awt.Font("Arial", 1, 48)); // NOI18N
        back.setForeground(new java.awt.Color(204, 255, 102));
        back.setText("HOME");
        back.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backActionPerformed(evt);
            }
        });

        search.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        search.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                searchKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                searchKeyReleased(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(3, 127, 54));
        jLabel8.setText("Search:");

        showAll.setBackground(new java.awt.Color(3, 127, 54));
        showAll.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        showAll.setForeground(new java.awt.Color(204, 255, 102));
        showAll.setText("Show All");
        showAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showAllActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(idTitle)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel7))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(category, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(pID, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(quan, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(bprice, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(sprice, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(pname, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(65, 65, 65))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(add)
                                .addGap(61, 61, 61)
                                .addComponent(update)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(delete)
                                .addGap(43, 43, 43))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(152, 152, 152)
                        .addComponent(back)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addGap(18, 18, 18)
                        .addComponent(search, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(refType, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(42, 42, 42))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 557, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(87, 87, 87)
                        .addComponent(showAll)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(refresh)
                        .addGap(104, 104, 104))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(category, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(idTitle)
                            .addComponent(pID, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(refresh)
                            .addComponent(showAll))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(refType, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(search, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 396, Short.MAX_VALUE)
                        .addGap(18, 18, 18))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(11, 11, 11)
                                .addComponent(pname, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(quan, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(15, 15, 15)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(bprice, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(sprice, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7))
                        .addGap(37, 37, 37)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(update)
                            .addComponent(add)
                            .addComponent(delete))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 73, Short.MAX_VALUE)
                        .addComponent(back)
                        .addGap(27, 27, 27)))
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bpriceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bpriceActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_bpriceActionPerformed

    public void selectionModeList(){
        itemList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ListSelectionModel model = itemList.getSelectionModel();
        
        model.addListSelectionListener((ListSelectionEvent e) -> {
            if(e.getValueIsAdjusting()){
                return;
            }
        });
    }
    
    private void quanKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_quanKeyTyped
        if(!Character.isDigit(evt.getKeyChar())){
            evt.consume();
        }
    }//GEN-LAST:event_quanKeyTyped

    private void bpriceKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_bpriceKeyTyped
        if(!Character.isDigit(evt.getKeyChar())){
            evt.consume();
        }
    }//GEN-LAST:event_bpriceKeyTyped

    private void spriceKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_spriceKeyTyped
        if(!Character.isDigit(evt.getKeyChar())){
            evt.consume();
        }
    }//GEN-LAST:event_spriceKeyTyped

    private void refreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshActionPerformed
        Command go = new Command();
        DefaultTableModel model = (DefaultTableModel) itemList.getModel();
        model.setRowCount(0);
        go.show_user();
        clearFields();
        
        add.setEnabled(true);
        category.setEnabled(true);
        pID.setEditable(true);
        pname.setEditable(true);
    }//GEN-LAST:event_refreshActionPerformed

    private void addActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addActionPerformed
        String name = pname.getText();
        String cat = category.getSelectedItem().toString();
        if(fieldsChecker()){
            if(!itemChecker(name, cat)){
                Command go = new Command();
                go.add();
                clearFields();
            }
        }
    }//GEN-LAST:event_addActionPerformed

    private void deleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteActionPerformed
        if(fieldsCheckerUD()){
            Command go = new Command();
            go.delete();
            clearFields();
        }
        
        add.setEnabled(true);
        category.setEnabled(true);
        pID.setEditable(true);
        pname.setEditable(true);
    }//GEN-LAST:event_deleteActionPerformed

    private void updateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateActionPerformed
        if(fieldsCheckerUD()){
            Command go = new Command();
            go.update();
            clearFields();
        }

        add.setEnabled(true);
        category.setEnabled(true);
        pID.setEditable(true);
        pname.setEditable(true);
    }//GEN-LAST:event_updateActionPerformed

    private void backActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backActionPerformed
        String lbl = accountName.getText();
        Admin a = new Admin(lbl);
        a.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_backActionPerformed

    private void itemListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_itemListMouseClicked
        int rowIndex = itemList.getSelectedRow();
        
        if(refType.getSelectedItem().equals("Bakal")){
            category.setSelectedItem("Bakal");
        }
        else if(refType.getSelectedItem().equals("Kahoy")){
            category.setSelectedItem("Kahoy");
        }
        else{
            category.setSelectedItem("Select");
        }
        
        showI();
        
        pID.setText(itemList.getValueAt(rowIndex, 0).toString());
        pname.setText(itemList.getValueAt(rowIndex, 1).toString());
        quan.setText(itemList.getValueAt(rowIndex, 2).toString());
        bprice.setText(itemList.getValueAt(rowIndex, 3).toString());
        sprice.setText(itemList.getValueAt(rowIndex, 4).toString());
        
        add.setEnabled(false);
        category.setEnabled(false);
        pID.setEditable(false);
        pname.setEditable(false);
    }//GEN-LAST:event_itemListMouseClicked

    private void refTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refTypeActionPerformed

    }//GEN-LAST:event_refTypeActionPerformed

    private void refTypeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_refTypeMouseClicked
        
    }//GEN-LAST:event_refTypeMouseClicked

    private void categoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_categoryActionPerformed
        pID.setEditable(true);
        pname.setEditable(true);
    }//GEN-LAST:event_categoryActionPerformed

    private void pIDKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pIDKeyTyped
        if(!Character.isDigit(evt.getKeyChar())){
            evt.consume();
        }
    }//GEN-LAST:event_pIDKeyTyped

    private void searchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchKeyReleased
        String name = search.getText();
        
        DefaultTableModel dtm = (DefaultTableModel) itemList.getModel();
        
        int i = itemList.getRowCount();
        while(i-->0){
            dtm.removeRow(i);
        }
        
        try {
            ResultSet rs9 = itemShow.pst(name);
            while(rs9.next()){
                java.util.Vector v = new java.util.Vector();
                v.add(rs9.getInt("ID"));
                v.add(rs9.getString("prodname"));
                v.add(rs9.getInt("quantity"));
                v.add(rs9.getInt("buyingPrice"));
                v.add(rs9.getInt("sellingPrice"));
                dtm.addRow(v);
            }
            
            ResultSet rs10 = itemShow.pst1(name); 
            while(rs10.next()){
                java.util.Vector v = new java.util.Vector();
                v.add(rs10.getInt("ID"));
                v.add(rs10.getString("prodname"));
                v.add(rs10.getInt("quantity"));
                v.add(rs10.getInt("buyingPrice"));
                v.add(rs10.getInt("sellingPrice"));
                dtm.addRow(v);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Order.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_searchKeyReleased

    private void searchKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchKeyPressed
        
    }//GEN-LAST:event_searchKeyPressed

    private void showAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showAllActionPerformed
        search.setText("");
        searchKeyReleased(null);
    }//GEN-LAST:event_showAllActionPerformed

    private void jPanel6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel6MouseClicked

    }//GEN-LAST:event_jPanel6MouseClicked

    private void jLabel9MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel9MouseClicked
        System.exit(0);
    }//GEN-LAST:event_jLabel9MouseClicked

    private void clearFields(){
        pID.setText(null);
        pname.setText(null);
        quan.setText(null);
        bprice.setText(null);
        sprice.setText(null);
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
            java.util.logging.Logger.getLogger(Products.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Products.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Products.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Products.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new Products().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel accountName;
    private javax.swing.JButton add;
    private javax.swing.JButton back;
    private javax.swing.JTextField bprice;
    private javax.swing.JComboBox<String> category;
    private javax.swing.JButton delete;
    private javax.swing.JLabel idTitle;
    private javax.swing.JTable itemList;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField pID;
    private javax.swing.JTextField pname;
    private javax.swing.JTextField quan;
    private javax.swing.JComboBox<String> refType;
    private javax.swing.JButton refresh;
    private javax.swing.JTextField search;
    private javax.swing.JButton showAll;
    private javax.swing.JTextField sprice;
    private javax.swing.JButton update;
    // End of variables declaration//GEN-END:variables
}
