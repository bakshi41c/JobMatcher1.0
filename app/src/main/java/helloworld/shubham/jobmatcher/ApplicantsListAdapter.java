package helloworld.shubham.jobmatcher;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shubham on 01/11/2014.
 */
public class ApplicantsListAdapter extends ArrayAdapter<Applicant> {

    private List<Applicant> applicants;
    private int cardLayoutID;
    private Context context;

    public ApplicantsListAdapter(Context context, int layoutID, ArrayList<Applicant> applicants) {
        super(context, layoutID, applicants);
        this.applicants = applicants;
        this.cardLayoutID = layoutID;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TextView applicant_name_tv;
        TextView applicant_statement_tv;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = null;
        if (convertView == null){
            rowView = inflater.inflate(cardLayoutID, parent, false);
            applicant_name_tv = (TextView) rowView.findViewById(R.id.applicant_name_tv);
            applicant_statement_tv = (TextView) rowView.findViewById(R.id.applicant_statement_tv);
        }else{

            applicant_name_tv = (TextView) convertView.findViewById(R.id.applicant_name_tv);
            applicant_statement_tv = (TextView) convertView.findViewById(R.id.applicant_statement_tv);
        }

        Applicant applicant = applicants.get(position);

        applicant_name_tv.setText(applicant.getName());
        applicant_statement_tv.setText(applicant.getStatement());



        if (convertView != null){
            return convertView;
        }

        return rowView;
    }

}