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
public class purchaseList {
    private int ID;
    private String itemID, itemName, itemPrice, itemCat, itemQuan, itemSubT;
    
    public purchaseList(int ID, String itemID, String itemName, String itemPrice, String itemCat, String itemQuan, String itemSubT){
        this.ID = ID;
        this.itemID = itemID;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemCat = itemCat;
        this.itemQuan = itemQuan;
        this.itemSubT = itemSubT;
    }
    
    public int getID(){
        return ID;
    }
    
    public String getItemID(){
        return itemID;
    }
    
    public String getItemName(){
        return itemName;
    }
    
    public String getItemPrice(){
        return itemPrice;
    }
    
    public String getItemCat(){
        return itemCat;
    }
    
    public String getItemQuan(){
        return itemQuan;
    }
    
    public String getItemSubT(){
        return itemSubT;
    }
}
