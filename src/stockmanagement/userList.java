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
    public class userList{
        private int UserID;
        private String uName, uPass, fName, lName, uGender, UserPhone, uEmail;
    
        public userList(int UserID, String uName, String uPass, String fName, String lName, String uGender, String UserPhone, String uEmail){
            this.UserID = UserID;
            this.uName = uName;
            this.uPass = uPass;
            this.fName = fName;
            this.lName = lName;
            this.uGender = uGender;
            this.UserPhone = UserPhone;
            this.uEmail = uEmail;
        }
    
        public int getuID(){
            return UserID;
        }
   
        public String getuName(){
            return uName;
        }
        
        public String getuPass(){
            return uPass;
        }
        
        public String getfName(){
            return fName;
        }
        
        public String getlName(){
            return lName;
        }
        
        public String getuGender(){
            return uGender;
        }
        
        public String getuPhone(){
            return UserPhone;
        }
        
        public String getuEmail(){
            return uEmail;
        }
    }
