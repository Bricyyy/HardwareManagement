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
    public class itemLists{
        private int ProductID, ProductQuantity;
        private String ProductName;
        private int BuyingPrice, SellingPrice;
    
        public itemLists(int ProductID, String ProductName, int ProductQuantity, int BuyingPrice, int SellingPrice){
            this.ProductID=ProductID;
            this.ProductName=ProductName;
            this.ProductQuantity=ProductQuantity;
            this.BuyingPrice=BuyingPrice;
            this.SellingPrice=SellingPrice;
        }
    
        public int getID(){
            return ProductID;
        }
   
        public String getName(){
            return ProductName;
        }
        
        public int getQuantity(){
            return ProductQuantity;
        }
        
        public int getBuyPrice(){
            return BuyingPrice;
        }
        
        public int getSellPrice(){
            return SellingPrice;
        }
    }
