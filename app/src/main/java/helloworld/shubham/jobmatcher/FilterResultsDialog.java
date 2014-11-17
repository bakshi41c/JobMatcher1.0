package helloworld.shubham.jobmatcher;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Shubham on 02/11/2014.
 */

public class FilterResultsDialog extends Dialog implements
        android.view.View.OnClickListener {

    public Activity c;
    public Dialog d;
    public Button ok;
    public Spinner spinner;
    public TextView seekbar_textview_tv;
    public SeekBar seekbar;

    public FilterResultsDialog(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.job_filter_dialog);
        ok = (Button) findViewById(R.id.done_btn);
        spinner = (Spinner) findViewById(R.id.spinner);
        seekbar = (SeekBar) findViewById(R.id.seekBar);
        seekbar_textview_tv = (TextView)findViewById(R.id.seekbar_tv);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(c);
        String category_preference = preferences.getString("category","");
        int distance_preference = preferences.getInt("distance",0);

        seekbar.setProgress(distance_preference);
        seekbar_textview_tv.setText(String.valueOf(distance_preference));

        ArrayList<String> categories_spinner = new ArrayList( Arrays.asList(c.getResources()
                .getStringArray(R.array.categories_array_spinner)) );

        int i = categories_spinner.indexOf(category_preference);

        if (i < 0 ){ i =1;}
        spinner.setSelection(i);

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekbar_textview_tv.setText(Integer.toString(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        ok.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.done_btn:

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(c);
                SharedPreferences.Editor editor = preferences.edit();
                if (spinner.getSelectedItem().toString().equals("NONE")){
                    editor.putString("category", "");
                }else{
                    editor.putString("category", spinner.getSelectedItem().toString());
                }
                editor.putInt("distance", seekbar.getProgress());

                editor.apply();
                dismiss();
                break;

            default:
                dismiss();
                break;
        }

    }
}