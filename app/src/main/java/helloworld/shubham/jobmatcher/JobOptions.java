package helloworld.shubham.jobmatcher;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;



public class JobOptions extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.job_match_options);
        
        Button findJobBTN = (Button) findViewById(R.id.findJobBTN);
        Button postJobBTN = (Button) findViewById(R.id.postJobBTN);
        
        findJobBTN.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent jobListIntent = new Intent(JobOptions.this, JobListActivity.class);
				startActivity(jobListIntent);

			}
        });
        
        postJobBTN.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent jobPostingIntent = new Intent(JobOptions.this, JobPosting.class);
				startActivity(jobPostingIntent);

			}
        });

    }

}
