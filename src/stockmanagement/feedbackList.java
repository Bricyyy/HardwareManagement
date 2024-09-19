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
public class feedbackList {
    private String uName, uFeedback, uDate;
    
    public feedbackList(String uName, String uFeedback, String uDate){
        this.uName = uName;
        this.uFeedback = uFeedback;
        this.uDate = uDate;
    }
    
    public String getName(){
        return uName;
    }
    
    public String getFeedback(){
        return uFeedback;
    }
    
    public String getDate(){
        return uDate;
    }
}
