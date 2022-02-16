package com.mobicall.call.recyclerViews;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobicall.call.R;
import com.mobicall.call.UI.CustomerActivity;
import com.mobicall.call.database.userDatabaseHelper;
import com.mobicall.call.database.userDatabaseModel;
import com.mobicall.call.models.TemplateModel;

import java.util.List;

public class TemplateAdapter extends RecyclerView.Adapter<TemplateAdapter.Holder>{
    String type , mNumber;
    List<TemplateModel> mList;

    public TemplateAdapter(String type,String mNumber , List<TemplateModel> mList) {
        this.type = type;
        this.mList = mList;
        this.mNumber = mNumber;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.template_layout , parent , false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {
        userDatabaseHelper db = new userDatabaseHelper(holder.layout.getContext());
        userDatabaseModel user = db.getUser(0);
        String message =" ";
        int position = i;
        if (user.getName()!=null && user.getMobile()!=null){
            String sms = mList.get(position).getMessage();
            String changed = sms.replace("[myName]" , user.getName());
            sms = changed.replace("[myPhone]" , user.getMobile());
            message = sms;
            holder.tContent.setText(sms);
        }
        String changedMsg = "";

        if (mList.get(position).getName()!=null){
            holder.tHead.setText(mList.get(position).getName());
        }
        String finalMessage1 = message;
        holder.expand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WindowManager wm = (WindowManager) holder.expand.getContext().getSystemService(Context.WINDOW_SERVICE);
                Display display = wm.getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                int width = size.x;
                int height = size.y;

                Dialog dialog = new Dialog(holder.layout.getContext());
                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
                layoutParams.width = width - 10;
                dialog.getWindow().setAttributes(layoutParams);
                dialog.setContentView(R.layout.dialog_layout_2);
                TextView tc = dialog.findViewById(R.id.tcontent);
                TextView th =dialog.findViewById(R.id.tHead);
                tc.setText(finalMessage1);
                dialog.show();
                dialog.findViewById(R.id.close).setOnClickListener((View vc)->{
                    dialog.dismiss();
                });
            }
        });
        String finalMessage = message;
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
          public void onClick(View v) {
            if (type!=null && mNumber!=null && type.equals("wp")){
                String phoneNumberWithCountryCode = "+91"+mNumber;
                Log.d("TAG", "onClick: eed");
                holder.itemView.getContext().startActivity(
                    new Intent(Intent.ACTION_VIEW,
                            Uri.parse(
                                    String.format("https://api.whatsapp.com/send?phone=%s&text=%s", phoneNumberWithCountryCode, finalMessage)
                            )
                    )
                );
            }else if (type!=null && mNumber!=null && type.equals("sms")){
                Uri uri = Uri.parse("smsto:+91"+mNumber);
                Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                intent.putExtra("sms_body", finalMessage);
                holder.itemView.getContext().startActivity(intent);

            }else if (type!=null && mNumber!=null && type.equals("email")){
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:")); // only email apps should handle this
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{mNumber});
                intent.putExtra(Intent.EXTRA_SUBJECT, "Enter Subject Here");
                intent.putExtra(Intent.EXTRA_TEXT, finalMessage);
                holder.itemView.getContext().startActivity(intent);
            }

          }

        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    static class Holder extends RecyclerView.ViewHolder {
        TextView tHead , tContent;
        ImageView expand;
        LinearLayout layout;
        public Holder(@NonNull View itemView) {
            super(itemView);
            tContent = itemView.findViewById(R.id.tcontent);
            tHead = itemView.findViewById(R.id.tHead);
            layout = itemView.findViewById(R.id.templateMain);
            expand = itemView.findViewById(R.id.expand);
        }
    }
}
