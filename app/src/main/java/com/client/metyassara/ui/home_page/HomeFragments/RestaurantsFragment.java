package com.client.metyassara.ui.home_page.HomeFragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.client.metyassara.R;
import com.client.metyassara.model.RestaurantModel;
import com.client.metyassara.ui.map.MapsActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RestaurantsFragment extends Fragment {

    private View view;
    private SearchView searchView;
    private Context context;
    private RecyclerView raecyclerView;
    private List<RestaurantModel> restaurantModels = new ArrayList<>();
    private RestaurantAdapter restaurantAdapter;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    // i make it public static to can get Restaurant_name in confirm_order_fragment
    public static String Restaurant_name;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return view = inflater.inflate(R.layout.fragment_restaurants, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        IntialRecyclerView();
        GetRestaurantFromFireBase();
        IntialSharedPreferences();
        initViews();
    }

    private void initViews()
    {
        //when click otherPlace will replace OtherPlaceFragment instead of RestaurantFragment
        Button otherPlace = view.findViewById ( R.id.other_place );
        otherPlace.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v)
            {

                loadFragment();
            }
        } );
    }

    private void loadFragment()
    {
        requireActivity ()
                .getSupportFragmentManager ()
                .beginTransaction ()
                .replace ( R.id.order_container,new OtherPlaceFragment () )
                .addToBackStack ( null )
                .commit ();
    }

    private void GetRestaurantFromFireBase() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("restaurants").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                restaurantModels.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    RestaurantModel restaurantModel = snapshot.getValue(RestaurantModel.class);
                    restaurantModels.add(restaurantModel);
                }
                restaurantAdapter = new RestaurantAdapter(restaurantModels);
                raecyclerView.setAdapter(restaurantAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void IntialRecyclerView() {
        raecyclerView = view.findViewById(R.id.recyclerview_restaurant);
        raecyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
        //seacrview
        searchView = view.findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String word) {
                search(word);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String word) {
                search(word);
                return false;
            }
        });
    }

    private void search(String word) {
        //make query
        Query query = FirebaseDatabase.getInstance().getReference().child("restaurants").orderByChild("restaurant_name")
                .startAt(word)
                .endAt(word + "\uf8ff");
        //get data
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    restaurantModels.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        RestaurantModel restaurantModel = snapshot.getValue(RestaurantModel.class);
                        restaurantModels.add(restaurantModel);
                    }
                    restaurantAdapter = new RestaurantAdapter(restaurantModels);
                    raecyclerView.setAdapter(restaurantAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.ViewHolder> {
        List<RestaurantModel> restaurantModels;

        public RestaurantAdapter(List<RestaurantModel> restaurantModels) {
            this.restaurantModels = restaurantModels;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.restaurant_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            final RestaurantModel restaurantModel = restaurantModels.get(position);

            //get image_url and display in image view by useing picasso
            Picasso.get().load(restaurantModel.getImage_url()).into(holder.Restaurant_image);
            Restaurant_name=restaurantModel.getRestaurant_name();
            editor.putString("Restaurant_name",Restaurant_name);
            editor.commit();
            holder.Restaurant_title.setText(Restaurant_name);
            holder.Restaurant_sub_title.setText(restaurantModel.getSub_title());
            holder.Restaurant_RatingBar.setRating(restaurantModel.getRate());
            //on click item
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //inten to map
                    Intent map=new Intent(context, MapsActivity.class);
                    //send restaurant name to map activity
                    map.putExtra("restaurantname",restaurantModel.getRestaurant_name());
                    //send lat and longt to map activity
                    map.putExtra("lat",restaurantModel.getLat());
                    map.putExtra("longt",restaurantModel.getLongt());
                    startActivity(map);
                }
            });
        }

        @Override
        public int getItemCount() {
            return restaurantModels.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            //intial vairable in restaurant_item
            ImageView Restaurant_image;
            TextView Restaurant_title, Restaurant_sub_title;
            RatingBar Restaurant_RatingBar;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                Restaurant_image = itemView.findViewById(R.id.Restaurant_image);
                Restaurant_title = itemView.findViewById(R.id.Restaurant_title);
                Restaurant_sub_title = itemView.findViewById(R.id.Restaurant_sub_title);
                Restaurant_RatingBar = itemView.findViewById(R.id.Restaurant_RatingBar);
            }
        }
    }

    private void IntialSharedPreferences() {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = preferences.edit();
    }
}
