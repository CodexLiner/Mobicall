package com.mobicall.call.recyclerViews;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobicall.call.R;
import com.mobicall.call.UI.CustomerDetailsActivity;
import com.mobicall.call.UI.TemplateList;
import com.mobicall.call.models.contacts;
import com.mobicall.call.stateManager.Constants;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

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
        int i = position;
//        Log.d("TAG", "onBindViewHolder: "+mList.toString());
        if (mList.get(position)!=null){
            String name =mList.get(position).getContact_name();
            if (name !=null && name.length()>20){
                name = name.substring(0 , 19)+"...";
            }
            holder.customerName.setText(name);
            if (mList.get(i).getCall_status()!=null){
                if (mList.get(i).getCall_status().equalsIgnoreCase("connected")){
                    if (mList.get(i).getInterested()!=null){
                        if (mList.get(i).getInterested().equals("1")){
                            String status = mList.get(i).getCall_status();
                            holder.callStatus.setText(status);
                            holder.intrest.setText("(interested)");
                        }else if (mList.get(i).getInterested().equals("0")){
                            String status = mList.get(i).getCall_status();
                            holder.callStatus.setText(status);
                            holder.intrest.setTextColor(holder.intrest.getResources().getColor(R.color.design_default_color_error));
                            holder.intrest.setText("(Not interested)");
                        }
                    }else {
                        String status = mList.get(i).getCall_status();
                        holder.callStatus.setText(status);
                    }
                }else if (mList.get(i).getCall_status().equalsIgnoreCase("not connected")){
                   // Log.d("TAG", "onBindViewHolder: "+mList.get(i).getInterested()+"  status "+mList.get(i).getCall_status()+" mobile "+mList.get(i).getPhone());
                    if (mList.get(i).getInterested()!=null) {
                        if (mList.get(i).getInterested().equals("1")) {
                            String status = mList.get(i).getCall_status();
                            holder.callStatus.setText(status);
                            holder.intrest.setText("(interested)");
                        } else if (mList.get(i).getInterested().equals("0")) {
                            String status = mList.get(i).getCall_status();
                            holder.callStatus.setText(status);
                            holder.intrest.setTextColor(holder.intrest.getResources().getColor(R.color.design_default_color_error));
                            holder.intrest.setText("(Not interested)");
                        }
                    }
                }
            }
            if (mList.get(i).getPhone()!=null){
                holder.customerNumber.setText(mList.get(i).getPhone());
            }
            if (mList.get(i).getUpdated_at()!=null){
                DateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'");
                utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                Date date = null;
                try {
                    date = utcFormat.parse(mList.get(i).getUpdated_at());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String formattedDate = new SimpleDateFormat("dd MMM yyyy hh:mm a").format(date);
                holder.dateTime.setText(formattedDate);

            }
        }
        holder.whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), TemplateList.class);
                intent.putExtra("uriName" , "wp");
                intent.putExtra("send_to" , mList.get(i).getPhone());
                holder.itemView.getContext().startActivity(intent);
            }
        });
        holder.sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), TemplateList.class);
                intent.putExtra("uriName" , "sms");
                intent.putExtra("send_to" , mList.get(i).getPhone());
                holder.itemView.getContext().startActivity(intent);
            }
        });
        holder.listLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constants.CallHistory = mList.get(i);
                holder.listLayout.getContext().startActivity(new Intent(holder.listLayout.getContext() , CustomerDetailsActivity.class));
            }
        });
        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                callIntent.setData(Uri.parse("tel:"+mList.get(i).getPhone()));
                holder.call.getContext().startActivity(callIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    static class holder extends RecyclerView.ViewHolder {
        LinearLayout listLayout;
        ImageView whatsapp ,sms , call;
        TextView customerName , callStatus , intrest , dateTime ,customerNumber;
        public holder(@NonNull View itemView) {
            super(itemView);
            listLayout = itemView.findViewById(R.id.listLayout);
            customerName = itemView.findViewById(R.id.customerName);
            callStatus = itemView.findViewById(R.id.callStatus);
            intrest = itemView.findViewById(R.id.intrested);
            dateTime = itemView.findViewById(R.id.dateTime);
            customerNumber = itemView.findViewById(R.id.customerNumber);
            whatsapp = itemView.findViewById(R.id.whatsapp);
            sms = itemView.findViewById(R.id.sms);
            call = itemView.findViewById(R.id.call);
        }
    }
}
