package helloworld.shubham.jobmatcher;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Shubham on 01/11/2014.
 */
public class JobListAdapter extends ArrayAdapter<Job> {

    private List<Job> jobs;
    private int cardLayoutID;
    private Context context;

    public JobListAdapter(Context context, int layoutID, ArrayList<Job> jobs) {
        super(context, layoutID, jobs);
        this.jobs = jobs;
        this.cardLayoutID = layoutID;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TextView job_title_tv;
        TextView job_description_tv;
        TextView distance_tv;
        ImageView job_image_iv;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = null;
        if (convertView == null){
            rowView = inflater.inflate(cardLayoutID, parent, false);
            job_title_tv = (TextView) rowView.findViewById(R.id.job_title_tv);
            job_description_tv = (TextView) rowView.findViewById(R.id.job_description_tv);
            distance_tv = (TextView) rowView.findViewById(R.id.distance_tv);
            job_image_iv = (ImageView)rowView.findViewById(R.id.job_image_iv);
        }else{

            job_title_tv = (TextView) convertView.findViewById(R.id.job_title_tv);
            job_description_tv = (TextView) convertView.findViewById(R.id.job_description_tv);
            distance_tv = (TextView) convertView.findViewById(R.id.distance_tv);
            job_image_iv = (ImageView)convertView.findViewById(R.id.job_image_iv);
        }

        Job job = jobs.get(position);

        job_title_tv.setText(job.getTitle());
        job_description_tv.setText(job.getDescription());
        Double distance;
        try {
            distance = GPSCoordinates.calculateDistanceBetweenCurrentLocationAndCoordinates(
                    context, Double.valueOf(job.getLocationLat()),
                    Double.valueOf(job.getLocationLon()));
        }catch (NumberFormatException e){
            distance = 0.0;
        }
        distance_tv.setText(String.valueOf(Math.round(distance)) + " km");

        ArrayList<String> categories = new ArrayList( Arrays.asList(context.getResources()
                                        .getStringArray(R.array.categories_array)) );
        ArrayList<String> colors = new ArrayList( Arrays.asList(context.getResources()
                .getStringArray(R.array.categories_colors)) );



        int i = categories.indexOf(job.getCategory());
        if (i < 0) {
            i = 8;
        }

        job_image_iv.setBackgroundColor(Color.parseColor(colors.get(i)));
        switch(i){
            case 0:
                job_image_iv.setImageResource(R.drawable.icon_charity_volunteering);
               break;

            case 1:
                job_image_iv.setImageResource(R.drawable.icon_education_teaching);
                break;

            case 2:
                job_image_iv.setImageResource(R.drawable.icon_call_centre);
                break;

            case 3:
                job_image_iv.setImageResource(R.drawable.icon_sales);
                break;

            case 4:
                job_image_iv.setImageResource(R.drawable.icon_repairs);
                break;

            case 5:
                job_image_iv.setImageResource(R.drawable.icon_personal_care);
                break;

            case 6:
                job_image_iv.setImageResource(R.drawable.icon_gardening);
                break;

            case 7:
                job_image_iv.setImageResource(R.drawable.icon_housekeeping);
                break;

            case 8:
                job_image_iv.setImageResource(R.drawable.icon_other);
                break;
        }






        if (convertView != null){
            return convertView;
        }

        return rowView;
    }


}
/*
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        CardView v = (CardView) LayoutInflater.from(viewGroup.getContext()).inflate(cardLayout, viewGroup, false);



        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Job job = jobs.get(i);
        Log.v("onBindViewer", "Loc: " + String.valueOf(i));
        Log.v("onBindViewer", job.getTitle());

        viewHolder.job_title_tv.setText(job.getTitle());
        viewHolder.job_description_tv.setText(job.getDescription());
    }

    @Override
    public int getItemCount() {
        return jobs == null ? 0 : jobs.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView job_title_tv;
        public TextView job_description_tv;
        //public ImageView countryImage;

        public ViewHolder(View itemView) {
            super(itemView);
            job_title_tv = (TextView) itemView.findViewById(R.id.job_title_tv);
            job_description_tv = (TextView) itemView.findViewById(R.id.job_description_tv);
            //countryImage = (ImageView)itemView.findViewById(R.id.countryImage);
        }

    }
}
*/