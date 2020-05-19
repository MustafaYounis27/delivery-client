package com.client.metyassara.ui.home_page.HomeFragments;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.client.metyassara.R;
import com.client.metyassara.model.RequestOderModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class VerificationOrderFragment extends Fragment {

    private View view;
    private Context context;
    private ProgressDialog dialog;
    private FusedLocationProviderClient fusedLocationClient;
    private EditText describe_order, phone_in_requestordr, address_field, building, floor;
    private Button ConfirmButton;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    //get user id
    private String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return view = inflater.inflate(R.layout.fragment_verification_order, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //local data base
        IntialSharedPreferences();
        IntialDialog();
        IntialViews();
        GetLocation();
        GetPhoneNumber();
    }

    private void IntialSharedPreferences() {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = preferences.edit();
    }

    private void GetPhoneNumber() {
        //get number from database
        FirebaseDatabase.getInstance().getReference().child("users").child(user_id).child("phone").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //get phone
                String phone = dataSnapshot.getValue(String.class);
                //put phone in phone edit text
                phone_in_requestordr.setText(phone);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void IntialViews() {
        describe_order = view.findViewById(R.id.describe_order);
        phone_in_requestordr = view.findViewById(R.id.phone_in_requestordr);
        address_field = view.findViewById(R.id.address);
        building = view.findViewById(R.id.building);
        floor = view.findViewById(R.id.floor);

        ConfirmButton = view.findViewById(R.id.confirm_request_order);
        ConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Verification();
            }
        });
    }

    private void GetLocation() {
        //check location permission is allow or no
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        100);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
            dialog.show();
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
            fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    // Got last known location. In some rare situations this can be null.
                    if (location != null) {
                        //get get complete address from latitude and longitude
                        GetAddrese(location.getLatitude(), location.getLongitude());
                    }
                    else {
                        dialog.dismiss();
                    }
                }
            });
        }
    }

    private void GetAddrese(double latitude, double longitude) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(context, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            // get complete address from latitude and longitude
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();

            //pass address in edit text
            address_field.setText(address);
            dialog.dismiss();
        } catch (IOException e) {
            e.printStackTrace();
            dialog.dismiss();
        }
    }

    private void IntialDialog() {
        dialog = new ProgressDialog(context);
        dialog.setMessage("plese wait...");
        dialog.setCancelable(false);

    }

    private void Verification() {
        //check all field are have data or no
        String describe_order_string, phone_string, address, building_string, floor_string;
        describe_order_string = describe_order.getText().toString();
        phone_string = phone_in_requestordr.getText().toString();
        address = address_field.getText().toString();
        building_string = building.getText().toString();
        floor_string = floor.getText().toString();

        if (describe_order_string.isEmpty()) {
            Toast.makeText(context, "please Describe order", Toast.LENGTH_SHORT).show();
            describe_order.requestFocus();
            return;
        }

        if (phone_string.isEmpty()) {
            Toast.makeText(context, "please enter your phone", Toast.LENGTH_SHORT).show();
            phone_in_requestordr.requestFocus();
            return;
        }

        if (address.isEmpty()) {
            Toast.makeText(context, "please enter address", Toast.LENGTH_SHORT).show();
            address_field.requestFocus();
            return;
        }

        if (building_string.isEmpty()) {
            Toast.makeText(context, "please enter building", Toast.LENGTH_SHORT).show();
            building.requestFocus();
            return;
        }

        if (floor_string.isEmpty()) {
            Toast.makeText(context, "please enter floor", Toast.LENGTH_SHORT).show();
            building.requestFocus();
            return;
        }
        //get current date
        String currentDate = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault()).format(new Date());

        RequestOderModel requestOderModel = new RequestOderModel(RestaurantsFragment.Restaurant_name, address, phone_string, describe_order_string, user_id, "", currentDate,"search",0);
        //upload in data base
        dialog.show();
        FirebaseDatabase.getInstance().getReference().child("requests").child(user_id).setValue(requestOderModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    dialog.dismiss();
                    String currentordertime = new SimpleDateFormat(" hh:mm").format(new Date());
                    Calendar calendar = Calendar.getInstance(); //A calendar set to the current time
                    calendar.add(Calendar.MINUTE, 40); //Add one hour
                    editor.putString("order time",currentordertime);
                    editor.putInt("end hour",calendar.get(Calendar.HOUR));
                    editor.putInt("end minute",calendar.get(Calendar.MINUTE));
                    editor.commit();
                    OrderTrackingFragment orderTrackingFragment=new OrderTrackingFragment();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.order_container, orderTrackingFragment);
                    fragmentTransaction.commit();
                } else {
                    dialog.dismiss();
                    Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
