package com.client.metyassara.ui.home_page.HomeFragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.circularprogressbar.CircularProgressBar;
import com.client.metyassara.R;
import com.client.metyassara.model.RequestOderModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shuhart.stepview.StepView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class OrderTrackingFragment extends Fragment {
    private View view;
    private Context context;
    private StepView mStepView;
    private CircularProgressBar circularProgressBar;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private String StartOderTime, my_id, status;
    private int EndOrderHour, EndOrderMiute;
    private TextView textminuteleft, statusview;
    private RequestOderModel requestOderModel;
    private Button next_to_rate;
    private ImageView imageView;
    Animation animation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return view = inflater.inflate(R.layout.fragment_order_tracking, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        IntialSharedPreferences();
        IntialViews();
        imageView = view.findViewById(R.id.rrrr);
        TranslateAnimation animation = new TranslateAnimation(0.0f, 800.0f,
                0.0f, 100.0f);          //  new TranslateAnimation(xFrom,xTo, yFrom,yTo)
        animation.setDuration(3000);  // animation duration
        animation.setRepeatCount(10000);  // animation repeat count
        animation.setRepeatMode(1);   // repeat animation (left to right, right to left )
        //animation.setFillAfter(true);
        imageView.startAnimation(animation);

        ItialStepView();
        status = preferences.getString("status", "");
        SetStep(status);
        GetStepFromDataBase();
        IntialCircularBrogressBar();
        timer();
    }

    private void IntialViews() {
        textminuteleft = view.findViewById(R.id.minute_left);
        statusview = view.findViewById(R.id.status);
        next_to_rate = view.findViewById(R.id.next_to_rateing);

        next_to_rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("status", "Rating");
                FirebaseDatabase.getInstance().getReference().child("in process").child(my_id).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            next_to_rate.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });
    }

    private void SetStep(String s) {
        if (s.isEmpty()) {
            return;
        }
        switch (s) {
            case "search":
                mStepView.go(0, true);
                statusview.setText("No Search For Delivery");
                break;
            case "go to store":
                mStepView.go(1, true);
                statusview.setText("Now your delivery go to store");
                break;
            case "In Store":
                mStepView.go(2, true);
                statusview.setText("Now your delivery IN store");
                break;
            case "in way":
                mStepView.go(3, true);
                statusview.setText("Now your delivery is on your");
                break;
            case "cash":
                mStepView.go(4, true);
                //statusview.setText();
                next_to_rate.setVisibility(View.VISIBLE);
                break;
            case "Rating":
                mStepView.go(5, true);
                // statusview.setText("Now your delivery is on your");
                editor.putString("in order", "no");
                //set check if data in in process or no be equal null
                editor.putInt("has", 1);
                editor.commit();
                break;
        }
    }

    private void GetStepFromDataBase() {
        my_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        //check this node in in process or no
        int has = preferences.getInt("has", 0);
        if (has != 2) {
            FirebaseDatabase.getInstance().getReference().child("in process").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild(my_id)) {
                        editor.putInt("has", 2);
                        editor.commit();
                    } else {
                        editor.putInt("has", 1);
                        editor.commit();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        //if in inprocess i will get data if no i do not get data
        int has2 = preferences.getInt("has", 0);
        if (has2 == 2) {
            FirebaseDatabase.getInstance().getReference().child("in process").child(my_id).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    requestOderModel = dataSnapshot.getValue(RequestOderModel.class);
                    status = requestOderModel.getStatus();
                    editor.putString("status", status);
                    editor.commit();
                    SetStep(status);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private void IntialSharedPreferences() {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = preferences.edit();
        //get start order time from shared preference
        StartOderTime = preferences.getString("order time", "");
        EndOrderHour = preferences.getInt("end hour", 0);
        EndOrderMiute = preferences.getInt("end minute", 0);

        editor.putString("in order", "in order");
        editor.commit();
    }

    private void IntialCircularBrogressBar() {
        circularProgressBar = view.findViewById(R.id.progress_bar);

    }

    private void ItialStepView() {
        mStepView = view.findViewById(R.id.step_vieww);
        mStepView.getState()
                .steps(new ArrayList<String>() {{
                    add("SEARCH For Delivery");
                    add("Go to Store");
                    add("In Store");
                    add("IN WAY");
                    add("CASH");
                    add("Rating");
                }})
                .commit();
        mStepView.go(0, true);
    }

    private void timer() {
        final int timeinminutes = 40 * 60;
        CountDownTimer timer = new CountDownTimer(timeinminutes * 1000, 1000) {
            int minuteleft;
            int check = preferences.getInt("check", 0);

            public void onTick(long millisUntilFinished) {
                if (check == 11) {
                    return;
                }
                Calendar currenttime = Calendar.getInstance();
                if (currenttime.get(Calendar.HOUR) < EndOrderHour) {
                    int leftminute = 60 - (currenttime.get(Calendar.MINUTE));
                    minuteleft = leftminute + EndOrderMiute;
                } else {
                    minuteleft = EndOrderMiute - currenttime.get(Calendar.MINUTE);
                }
                // if time left ended and save in shared pref to check are was end or no
                if (minuteleft == 0) {
                    editor.putInt("check", 11);
                    editor.commit();
                    return;
                }
                textminuteleft.setText(minuteleft + "");
                circularProgressBar.setProgress(minuteleft);
            }

            public void onFinish() {
            }
        }.start();
    }

}
