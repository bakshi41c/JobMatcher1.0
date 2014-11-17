package helloworld.shubham.jobmatcher;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

/**
 * Created by Shubham on 02/11/2014.
 */
public class GPSCoordinates {

    public static double calculateDistanceBetweenCurrentLocationAndCoordinates(Context context, double latitude1, double longitude1) {

        // Calculate current location of device (current location is latitude2 and longitude 2)
        LocationManager lm = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
         /*Default Location for UK parliament*/
        double longitude2 = -0.12464760000000297;
        double latitude2 = 51.4996367;

        if (location!= null){
            longitude2 = location.getLongitude();
            latitude2 = location.getLatitude();
        }

        // Calculate distance
        double theta = longitude1 - longitude2;
        double dist = Math.sin(deg2rad(latitude1)) * Math.sin(deg2rad(latitude2)) + Math.cos(deg2rad(latitude1)) * Math.cos(deg2rad(latitude2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        dist = dist * 1.609344;
        return (dist);
    }

    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }


    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }

}
