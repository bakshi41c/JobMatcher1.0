package helloworld.shubham.jobmatcher;

import android.graphics.Bitmap;

/**
 * Created by Shubham on 01/11/2014.
 */
public class Job {

    private String jobID;
    private String jobTitle;
    private String jobDescription;
    private Bitmap jobImage;
    private String category = "";
    private String locationLat = "";
    private String locationLon = "";


    public Job (String jobID, String jobTitle, String jobDescription,
                String category, String locationLat, String locationLon){
        this.jobID = jobID;
        this.jobTitle = jobTitle;
        this.jobDescription = jobDescription;
        this.category = category;
        this.locationLat = locationLat;
        this.locationLon = locationLon;
    }

    public String  getJobId(){
        return jobID;
    }

    public String getTitle(){
        return jobTitle;
    }

    public String getDescription(){
        return jobDescription;
    }

    public String getCategory(){
        return category;
    }

    public String getLocationLat() { return locationLat;}

    public String getLocationLon() { return locationLon;}

}
