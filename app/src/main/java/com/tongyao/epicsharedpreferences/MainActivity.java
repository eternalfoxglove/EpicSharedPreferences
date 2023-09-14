package com.tongyao.epicsharedpreferences;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    SharedPreferences appPreferences;
    String TAG = "com.tongyao.epicsharedpreferences";
    SharedPreferences.Editor preferencesEditor;

    ImageView bg;
    TextView top_left;
    TextView top_right;
    SeekBar text_sizer;
    Button bottom_left;
    Button bottom_right;

    View[] viewsList;
    int[] numDays;
    int[] backgrounds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bg = findViewById(R.id.background_image);
        bg.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                preferencesEditor.clear().apply();
                setInitialValues();
                return true;
            }
        });
        backgrounds = new int[]{R.drawable.springbg, R.drawable.summerbg, R.drawable.autumnbg, R.drawable.winterbg};

        top_left = findViewById(R.id.top_left_button);
        top_left.setOnClickListener(this);
        top_right = findViewById(R.id.top_right_button);
        top_right.setOnClickListener(this);
        bottom_left = findViewById(R.id.bottom_left_button);
        bottom_left.setOnClickListener(this);
        bottom_right = findViewById(R.id.bottom_right_button);
        bottom_right.setOnClickListener(this);

        text_sizer = findViewById(R.id.text_size_changer);
        text_sizer.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int last_val;
            @Override
            public void onStartTrackingTouch(SeekBar s) {
                last_val = s.getProgress();
            }

            @Override
            public void onStopTrackingTouch(SeekBar s) {
                Snackbar snack = Snackbar.make(bg, getString(R.string.changed_font_size_message, s.getProgress()), Snackbar.LENGTH_LONG);
                snack.show();
            }

            @Override
            public void onProgressChanged(SeekBar s, int progressVal, boolean from_user) {
                for (View v : viewsList) {
                    if (v instanceof TextView) {
                        ((TextView)v).setTextSize(progressVal);
                    }
                    else {
                        ((Button)v).setTextSize(progressVal);
                    }
                }
            }
        });

        appPreferences = getSharedPreferences(TAG, MODE_PRIVATE);
        preferencesEditor = appPreferences.edit();

//        Apparently viewsList Objects are null, fix next time
        viewsList = new View[]{top_left, top_right, bottom_left, bottom_right};
        numDays = new int[]{31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    }

    @Override
    protected void onResume() {
        super.onResume();
        setInitialValues();
    }

    @Override
    public void onClick(View v) {
        if (v instanceof TextView) {
            int count = Integer.parseInt(((TextView) v).getText().toString())+1;
            if (v.getTag().equals("topleft") && count > 12) {
                count = 1;
                System.out.println("testing TL :" +count);
            }
            // # days depends on month, make a list.
            else if (v.getTag().equals("topright") && count > numDays[Integer.parseInt(top_left.getText().toString())-1]) {
                count = 1;
                System.out.println("testing TR :" +count);
            }
            System.out.println("testing post :"+v.getTag().toString() +count);

            ((TextView)v).setText(""+count);
            System.out.println("testing post2 :"+v.getTag().toString() +count);
            preferencesEditor.putInt(v.getTag().toString(), count);
        }
        else {
            int count = Integer.parseInt(((Button)v).getText().toString())+1;
            ((Button)v).setText(""+count);
            preferencesEditor.putInt(v.getTag().toString(), count);
            System.out.println("testing else :"+v.getTag().toString() +count);
        }

//        if (Integer.parseInt(top_left.getText().toString())==12) {
//            bg.setImageResource(backgrounds[3]);
//            preferencesEditor.putInt(bg.getTag().toString(), backgrounds[3]);
//        }
//        else if (Integer.parseInt(top_left.getText().toString())==3) {
//            bg.setImageResource(backgrounds[0]);
//            preferencesEditor.putInt(bg.getTag().toString(), backgrounds[0]);
//        }
//        else if (Integer.parseInt(top_left.getText().toString())==6) {
//            bg.setImageResource(backgrounds[1]);
//            preferencesEditor.putInt(bg.getTag().toString(), backgrounds[1]);
//        }
//        else if (Integer.parseInt(top_left.getText().toString())==9){
//            bg.setImageResource(backgrounds[2]);
//            preferencesEditor.putInt(bg.getTag().toString(), backgrounds[2]);
//        }

        preferencesEditor.apply();
    }

    public void setInitialValues() {
        for (View v : viewsList) {
            if (v instanceof TextView) {
                int count = appPreferences.getInt(v.getTag().toString(), 1);
                ((TextView)v).setText(""+count);
            } else {
                int count = appPreferences.getInt(v.getTag().toString(), 0);
                ((Button)v).setText(""+count);
            }
        }
        bg.setImageResource(R.drawable.winterbg);
    }
}

// GitToken: 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