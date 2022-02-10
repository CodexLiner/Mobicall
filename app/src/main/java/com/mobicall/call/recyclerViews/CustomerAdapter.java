package com.mobicall.call.recyclerViews;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobicall.call.R;
import com.mobicall.call.UI.CustomerDetailsActivity;
import com.mobicall.call.models.contacts;
import com.mobicall.call.stateManager.Constants;

import java.util.List;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.holder> {
    List<contacts> mList;

    public CustomerAdapter(List<contacts> mList) {
        this.mList = mList;
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_list_layout , parent , false);
        return new holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull holder holder, int position) {

        holder.customerName.setText(mList.get(position).getContact_name());
        if (mList.get(position).getCall_status()!=null){
            holder.callStatus.setText(mList.get(position).getCall_status());
        }


        holder.listLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constants.CallHistory = mList.get(position);
                holder.listLayout.getContext().startActivity(new Intent(holder.listLayout.getContext() , CustomerDetailsActivity.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class holder extends RecyclerView.ViewHolder {
        LinearLayout listLayout;
        TextView customerName , callStatus;
        public holder(@NonNull View itemView) {
            super(itemView);
            listLayout = itemView.findViewById(R.id.listLayout);
            customerName = itemView.findViewById(R.id.customerName);
            callStatus = itemView.findViewById(R.id.callStatus);
        }
    }
}
