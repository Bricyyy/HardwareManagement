///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package stockmanagement;
//
///**
// *
// * @author Cabuyao Col
// */
//import java.awt.Font;
//import java.awt.FontMetrics;
//import java.awt.Graphics;
//import java.awt.Graphics2D;
//import java.awt.print.PageFormat;
//import java.awt.print.Paper;
//import java.awt.print.Printable;
//import static java.awt.print.Printable.NO_SUCH_PAGE;
//import static java.awt.print.Printable.PAGE_EXISTS;
//import java.awt.print.PrinterException;
//import java.awt.print.PrinterJob;
//import java.util.ArrayList;
//import javax.swing.ImageIcon;
//import javax.swing.JOptionPane;
//
//public class SampleInvoice extends javax.swing.JFrame {
//
//    /**
//     * Creates new form Home
//     */
//    Double totalAmount=0.0;
//    Double cash=0.0;
//    Double balance=0.0;
//    Double bHeight=0.0;
//    
//    ArrayList<String> itemName = new ArrayList<>();
//    ArrayList<String> quantity = new ArrayList<>();
//    ArrayList<String> itemPrice = new ArrayList<>();
//    ArrayList<String> subtotal = new ArrayList<>();
//    
//    
//    public SampleInvoice() {
//        initComponents();
//    }
//
//    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
//        itemName.add(txtitemname.getText());
//        quantity.add(txtquantity.getText());
//        itemPrice.add(txtprice.getText());
//        subtotal.add(txtsubtotal.getText());
//        totalAmount = totalAmount+ Double.valueOf(txtsubtotal.getText());
//        txttotalAmount.setText(totalAmount+"");
//        clear();
//    }
//
//    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {
//        bHeight = Double.valueOf(itemName.size());
//        
//        PrinterJob pj = PrinterJob.getPrinterJob();        
//        pj.setPrintable(new BillPrintable(),getPageFormat(pj));
//        try{
//            pj.print();  
//        }
//        catch (PrinterException ex) {
//            ex.printStackTrace(System.err);
//        }
//    }
//    
//    public PageFormat getPageFormat(PrinterJob pj){
//        PageFormat pf = pj.defaultPage();
//        Paper paper = pf.getPaper();    
//
//        double bodyHeight = bHeight;  
//        double headerHeight = 5.0;                  
//        double footerHeight = 5.0;        
//        double width = cm_to_pp(8); 
//        double height = cm_to_pp(headerHeight+bodyHeight+footerHeight); 
//        paper.setSize(width, height);
//        paper.setImageableArea(0,10,width,height - cm_to_pp(1));  
//
//        pf.setOrientation(PageFormat.PORTRAIT);  
//        pf.setPaper(paper);    
//
//        return pf;
//    }
//    
//    protected static double cm_to_pp(double cm){            
//	return toPPI(cm * 0.393600787);            
//    }
// 
//    protected static double toPPI(double inch){            
//	return inch * 72d;            
//    }
//    
//    public class BillPrintable implements Printable{
//        @Override
//        public int print(Graphics graphics, PageFormat pageFormat,int pageIndex) 
//            throws PrinterException{     
//                int r= itemName.size();
//                ImageIcon icon=new ImageIcon("C:UsersccsDocumentsNetBeansProjectsvideo TestPOSInvoicesrcposinvoicemylogo.jpg"); 
//                int result = NO_SUCH_PAGE;    
//                if (pageIndex == 0) {                    
//                    Graphics2D g2d = (Graphics2D) graphics;                    
//                    double width = pageFormat.getImageableWidth();                               
//                    g2d.translate((int) pageFormat.getImageableX(),(int) pageFormat.getImageableY()); 
// 
//            try{
//                int y=20;
//                int yShift = 10;
//                int headerRectHeight=15;       
//                
//                g2d.setFont(new Font("Monospaced",Font.PLAIN,9));
//                g2d.drawImage(icon.getImage(), 50, 20, 90, 30, rootPane);y+=yShift+30;
//                g2d.drawString("-------------------------------------",12,y);y+=yShift;
//                g2d.drawString("      VIAZONETrading.com     ",12,y);y+=yShift;
//                g2d.drawString(" 168 Mamatid Cabuyao, Laguna ",12,y);y+=yShift;
//                g2d.drawString("        CALABARZON 4-A       ",12,y);y+=yShift;
//                g2d.drawString("   www.facebook.com/VIAZONE  ",12,y);y+=yShift;
//                g2d.drawString("        +639162964676        ",12,y);y+=yShift;
//                g2d.drawString("-------------------------------------",12,y);y+=headerRectHeight;
//
//                g2d.drawString(" Item Name                  Price   ",10,y);y+=yShift;
//                g2d.drawString("-------------------------------------",10,y);y+=headerRectHeight;
//
//                for(int s=0; s<r; s++){
//                g2d.drawString(" "+itemName.get(s)+"                            ",10,y);y+=yShift;
//                g2d.drawString("      "+quantity.get(s)+" * "+itemPrice.get(s),10,y);
//                g2d.drawString(subtotal.get(s),160,y);y+=yShift;
//                }
//                
//                g2d.drawString("-------------------------------------",10,y);y+=yShift;
//                g2d.drawString(" Total amount:               "+txttotalAmount.getText()+"   ",10,y);y+=yShift;
//                g2d.drawString("-------------------------------------",10,y);y+=yShift;
//                g2d.drawString(" Cash      :                 "+txtcash.getText()+"   ",10,y);y+=yShift;
//                g2d.drawString("-------------------------------------",10,y);y+=yShift;
//                g2d.drawString(" Balance   :                 "+txtbalance.getText()+"   ",10,y);y+=yShift;
//
//                g2d.drawString("*************************************",10,y);y+=yShift;
//                g2d.drawString("       THANK YOU COME AGAIN            ",10,y);y+=yShift;
//                g2d.drawString("*************************************",10,y);y+=yShift;
//                g2d.drawString("      SOFTWARE BY: BSCS201A          ",10,y);y+=yShift;
//                g2d.drawString(" CONTACT: abryan.delossantosa@gmail.com",10,y);y+=yShift;       
//           
//
//            }
//            catch(Exception e){
//                e.printStackTrace(System.err);
//            }
//            result = PAGE_EXISTS;    
//          }    
//          return result;    
//      }
//   }
//}