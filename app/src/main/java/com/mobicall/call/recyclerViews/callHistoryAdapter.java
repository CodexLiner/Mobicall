package com.mobicall.call.recyclerViews;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobicall.call.R;
import com.mobicall.call.models.CallHistory;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class callHistoryAdapter extends RecyclerView.Adapter<callHistoryAdapter.Holder>{
    List<CallHistory> list;

    public callHistoryAdapter(List<CallHistory> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.call_history_layout , parent , false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        if (list!=null && list.size()>0){
            String time = list.get(position).getTime().toString().trim() +" seconds";
            holder.duration.setText(time);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(Long.parseLong(list.get(position).getDate()));

            int mYear = calendar.get(Calendar.HOUR);
            int mMonth = calendar.get(Calendar.DAY_OF_MONTH);
            int mDay = calendar.get(Calendar.MONTH);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("h:mm \na dd\nMMM");
            SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("dd - MMM - YYYY ");
            String timeMinute = simpleDateFormat.format(calendar.getTime());
            String fullDate = simpleDateFormat2.format(calendar.getTime());
            holder.date.setText(fullDate);
            Log.d("TAG", "onBindViewHolder: "+mYear+" "+mMonth+" "+mDay);

            holder.time.setText(timeMinute+"\n");

            if (list.get(position).getName() == null || list.get(position).getName().equals("")  ){
                holder.name.setText(list.get(position).getNumber());
            }else {
                holder.name.setText(list.get(position).getName());
            }
            if (list.get(position).getType().equals("1")){
                holder.type.setText("Incoming");
            }else if (list.get(position).getType().equals("2")){
                holder.type.setText("Outgoing");
            }else {
                holder.type.setText("Missed Call");
            }

        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class  Holder extends RecyclerView.ViewHolder {
        TextView time , date , type , duration , name;
        public Holder(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.callDate);
            name = itemView.findViewById(R.id.callName);
            date = itemView.findViewById(R.id.callFulldate);
            duration = itemView.findViewById(R.id.callDuration);
            type = itemView.findViewById(R.id.callType);
        }
    }
}
