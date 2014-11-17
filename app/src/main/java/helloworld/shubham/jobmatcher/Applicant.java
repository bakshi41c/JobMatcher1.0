package helloworld.shubham.jobmatcher;

import android.graphics.Bitmap;

/**
 * Created by Shubham on 01/11/2014.
 */
public class Applicant {

    private String applicantID;
    private String applicantName;
    private String applicantEmail;
    private String applicantPhone;
    private String applicantStatement = "";
    private String applicantCategory = "";

    public Applicant(String applicantID, String applicantName, String applicantStatement){
        this.applicantID = applicantID;
        this.applicantName = applicantName;
        this.applicantStatement = applicantStatement;
    }

    public Applicant(String applicantID, String applicantName, String applicantEmail, String applicantPhone,
                     String applicantStatement, String applicantCategory){
        this.applicantID = applicantID;
        this.applicantName = applicantName;
        this.applicantEmail = applicantEmail;
        this.applicantPhone = applicantPhone;
        this.applicantStatement = applicantStatement;
        this.applicantCategory = applicantCategory;
    }

    public String  getId(){
        return applicantID;
    }

    public String getName(){
        return applicantName;
    }

    public String getEmail(){
        return applicantEmail;
    }

    public String getPhone(){
        return applicantPhone;
    }

    public String getStatement(){
        return applicantStatement;
    }

    public String getCategory(){
        return applicantCategory;
    }

}
