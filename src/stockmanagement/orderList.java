/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stockmanagement;

/**
 *
 * @author Cabuyao Col
 */
public class orderList {
    private int userID;
    private String orderID, totalAmount, paidAmount, balanceAmount, uDate, uTime;
    
    public orderList(int userID,String orderID, String totalAmount, String paidAmount, String balanceAmount, String uDate, String uTime){
        this.userID = userID;
        this.orderID = orderID;
        this.totalAmount = totalAmount;
        this.paidAmount = paidAmount;
        this.balanceAmount = balanceAmount;
        this.uDate = uDate;
        this.uTime = uTime;
    }
    
    public int getUserID(){
        return userID;
    }
    
    public String getOrderID(){
        return orderID;
    }
    
    public String getTotalAmount(){
        return totalAmount;
    }
    
    public String getPaidAmount(){
        return paidAmount;
    }
    
    public String getBalanceAmount(){
        return balanceAmount;
    }
    
    public String getDate(){
        return uDate;
    }
    
    public String getTime(){
        return uTime;
    }
}
