package com.client.metyassara.ui.home_page.HomeFragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.client.metyassara.R;
import com.client.metyassara.model.RestaurantModel;
import com.client.metyassara.model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class OtherPlaceFragment extends Fragment
{
    private View otherPlaceFragment;
    private ImageView back;

    private FirebaseAuth auth;
    private DatabaseReference databaseReference;

    private ProgressDialog enterDialog;

    private UserModel myModel;

    private EditText restaurantNameField, restaurantSubTitleField;
    private RatingBar restaurantRate;
    private Button verification;

    private float rate;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        otherPlaceFragment=inflater.inflate ( R.layout.fragment_other_place,null );
        return otherPlaceFragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated ( savedInstanceState );

        IntialSharedPreferences();
        initFirebase();
        initDialog();

        String uid = auth.getUid ();

        getMyModel( uid );

    }

    private void initViews()
    {
        restaurantNameField=otherPlaceFragment.findViewById ( R.id.restaurant_mame );
        restaurantSubTitleField=otherPlaceFragment.findViewById ( R.id.restaurant_sub_title );
        restaurantRate=otherPlaceFragment.findViewById ( R.id.restaurant_rate );

        TextView userName = otherPlaceFragment.findViewById ( R.id.user_name );
        userName.setText ( "please "+myModel.getUserName ()+", enter full details" );

        verification=otherPlaceFragment.findViewById ( R.id.verification_order );

        back = otherPlaceFragment.findViewById ( R.id.back_to_restaurant );

        getRate();

        click();
    }

    private void getRate()
    {
        restaurantRate.setOnRatingBarChangeListener ( new RatingBar.OnRatingBarChangeListener ()
        {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser)
            {
                rate=rating;
            }
        } );
    }


    private void click()
    {
        back.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                requireActivity ().onBackPressed ();
            }
        } );

        verification.setOnClickListener ( new View.OnClickListener ()
        {
            @Override
            public void onClick(View v)
            {
                String restaurantName = restaurantNameField.getText ().toString ();
                String restaurantSubTitle = restaurantSubTitleField.getText ().toString ();
                rate = restaurantRate.getRating ();

                if(restaurantName.isEmpty ())
                {
                    Toast.makeText ( getContext (), "please enter place name", Toast.LENGTH_SHORT ).show ();
                    restaurantNameField.requestFocus ();
                    return;
                }

                if(restaurantSubTitle.isEmpty ())
                {
                    Toast.makeText ( getContext (), "please enter ", Toast.LENGTH_SHORT ).show ();
                    restaurantSubTitleField.requestFocus ();
                    return;
                }

                if(rate == 0)
                {
                    Toast.makeText ( getContext (), "please enter your rate", Toast.LENGTH_SHORT ).show ();
                    return;
                }

                editor.putString("Restaurant_name",restaurantName);
                editor.commit();
                enterDialog.show ();
                checkRestaurants(restaurantName,restaurantSubTitle,rate);

            }
        } );
    }

    private void checkRestaurants(final String restaurantName, final String restaurantSubTitle, final float rate)
    {
        //make query
        Query query = FirebaseDatabase.getInstance().getReference().child("restaurants").orderByChild("restaurant_name")
                .startAt(restaurantName)
                .endAt(restaurantName + "\uf8ff");
        //get data
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChildren())
                {
                    uploadRestaurant ( restaurantName,restaurantSubTitle,rate );
                }
                else
                    {
                        loadFragment ();
                    }
                enterDialog.dismiss ();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                Toast.makeText ( getContext (), databaseError.getMessage (), Toast.LENGTH_SHORT ).show ();
                enterDialog.dismiss ();
            }
        });
    }

    private void uploadRestaurant(final String restaurantName, final String restaurantSubTitle, final float rate)
    {
        String key=databaseReference.child ( "restaurants" ).push ().getKey ();
        RestaurantModel restaurantModel = new RestaurantModel ( 1,1,rate,restaurantName,restaurantSubTitle,"1" );
        if(key != null)
            databaseReference.child ( "restaurants" ).child ( key ).setValue ( restaurantModel ).addOnCompleteListener ( new OnCompleteListener<Void> ()
            {
                @Override
                public void onComplete(@NonNull Task<Void> task)
                {
                    if(task.isSuccessful ())
                    {
                        Toast.makeText ( getContext (), "the restaurant is added successfully", Toast.LENGTH_SHORT ).show ();

                        loadFragment();
                    }
                    else
                        {
                            Toast.makeText ( getContext (), task.getException ().getMessage (), Toast.LENGTH_SHORT ).show ();
                        }
                }
            } );
    }

    private void loadFragment()
    {
        requireActivity ()
                .getSupportFragmentManager ()
                .beginTransaction ()
                .replace ( R.id.order_container,new VerificationOrderFragment () )
                .commit ();
    }

    private void getMyModel(final String uid)
    {
        enterDialog.show();
        databaseReference.child("users").child(uid).addValueEventListener(new ValueEventListener () {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myModel = dataSnapshot.getValue( UserModel.class);
                initViews();
                enterDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                enterDialog.dismiss();
            }
        });
    }

    private void initDialog()
    {
        //dialog appeared when enter to fragment
        enterDialog = new ProgressDialog ( getContext () );
        enterDialog.setMessage ( "please wait..." );
        enterDialog.setCancelable ( false );
    }

    //definition firebase
    private void initFirebase()
    {
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }
    private void IntialSharedPreferences() {
        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        editor = preferences.edit();
    }
}
