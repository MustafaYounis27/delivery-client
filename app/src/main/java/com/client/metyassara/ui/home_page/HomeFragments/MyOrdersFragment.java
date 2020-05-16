package com.client.metyassara.ui.home_page.HomeFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.client.metyassara.R;
import com.client.metyassara.model.UserModel;
import com.client.metyassara.ui.home_page.HomePageActivity;

import java.util.ArrayList;
import java.util.List;

public class MyOrdersFragment extends Fragment
{
    private View myOrdersFragment;
    private ImageView userSetting;
    private RecyclerView recyclerView;
     List<UserModel> userModels =new ArrayList<>();
    
    

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        myOrdersFragment=inflater.inflate ( R.layout.fragment_my_orders,null );
        return myOrdersFragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated ( savedInstanceState );
            //initiazlize Orders Model
    
        userModels.add(new UserModel( "Order1",  "Price1",  "Date",  "Adress"));
        userModels.add(new UserModel( "Order1",  "Price1",  "Date",  "Adress"));
        userModels.add(new UserModel( "Order1",  "Price1",  "Date",  "Adress"));
        userModels.add(new UserModel( "Order1",  "Price1",  "Date",  "Adress"));
        userModels.add(new UserModel( "Order1",  "Price1",  "Date",  "Adress"));
        userModels.add(new UserModel( "Order1",  "Price1",  "Date",  "Adress"));
        userModels.add(new UserModel( "Order1",  "Price1",  "Date",  "Adress"));
    
    
        initViews();
        

    }

    private void initViews()
    {
        //to gone visible image icon
        userSetting=getActivity().findViewById ( R.id.user_setting );
        userSetting.setVisibility ( View.GONE );
        // Initialize Recycler
        recyclerView =myOrdersFragment.findViewById(R.id.orders_recy);
        recyclerView.setAdapter(new OrdersAdapter(userModels));
    }
    
    // Adapter Class
    class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.VH> {
    List<UserModel> orderModel;
    
        public OrdersAdapter(List<UserModel> orderModel) {
            this.orderModel = orderModel;
        }
    
        @NonNull
        @Override
        public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(getContext()).inflate(R.layout.orders_item,parent,false);
            return new VH(v);
        }
    
        @Override
        public void onBindViewHolder(@NonNull VH holder, int position) {
        
        UserModel userModel =orderModel.get(position);
            holder.orderName.setText(userModel.getImgeUrl());
            holder.orderPrice.setText(userModel.getUserName());
            holder.orderDate.setText(userModel.getUid());
            holder.orderAdress.setText(userModel.getPhone());
    
        }
    
        @Override
        public int getItemCount() {
            return orderModel.size();
        }
    
        
        class VH extends RecyclerView.ViewHolder
        {
            TextView orderName,orderPrice,orderDate,orderAdress;
            
            public VH(@NonNull View itemView) {
                super(itemView);
                orderName = itemView.findViewById(R.id.order_item);
                orderPrice = itemView.findViewById(R.id.Price_item);
                orderDate = itemView.findViewById(R.id.date_item);
                orderAdress = itemView.findViewById(R.id.address_item);
           
        }
        }
    }
}
