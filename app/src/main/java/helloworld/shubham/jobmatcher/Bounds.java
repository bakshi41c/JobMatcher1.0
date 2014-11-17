package helloworld.shubham.jobmatcher;

/**
 * Created by Shubham on 01/11/2014.
 */
public class Bounds {
    public double minLon,maxLon,minLat,maxLat;

    private double radToDeg(double rad){
        return (180*rad)/Math.PI;
    }

    private double degToRad(double deg){
        return deg*(Math.PI/180);
    }

    public Bounds(String[] centerPoint, int radius){
        final double MIN_LAT, MAX_LAT, MIN_LON, MAX_LON, R;
        double radDist, degLat, degLon, radLat, radLon, minLat, maxLat, minLon, maxLon, deltaLon;
        if(radius<0){
            throw new IllegalArgumentException();
        }
        // coordinate limits
        MIN_LAT = degToRad(-90);
        MAX_LAT = degToRad(90);
        MIN_LON = degToRad(-180);
        MAX_LON = degToRad(180);
        // radius of the earth
        R = 6378.1;
        // angular distance in radirans on a great circle
        radDist = radius / R;
        // center point coordinates (deg)
        degLat = Double.parseDouble(centerPoint[0]);
        degLon = Double.parseDouble(centerPoint[1]);
        // center point coordinates (rad)
        radLat = degToRad(degLat);
        radLon = degToRad(degLon);
        // minimum and maximum latitudes for given distance
        minLat = radLat - radDist;
        maxLat = radLat + radDist;
        // minimum and maximum longitudes for given distance
        minLon = 0;
        maxLon = 0;
        // define deltaLon to help determine min and max longitudes
        deltaLon = Math.asin(Math.sin(radDist)/Math.cos(radLat));
        if(minLat > MIN_LAT && maxLat < MAX_LAT){
            minLon = radLon - deltaLon;
            maxLon = radLon + deltaLon;
            if(minLon < MIN_LON){
                minLon = minLon + 2 * Math.PI;
            }
            if(maxLon > MAX_LON){
                maxLon = maxLon - 2 * Math.PI;
            }
        }
        else{
            minLat = Math.max(minLat, MIN_LAT);
            maxLat = Math.min(maxLat, MAX_LAT);
            minLon = MIN_LON;
            maxLon = MAX_LON;
        }
        this.minLon = radToDeg(minLon);
        this.minLat = radToDeg(minLat);
        this.maxLon = radToDeg(maxLon);
        this.maxLat = radToDeg(maxLat);
    }
}