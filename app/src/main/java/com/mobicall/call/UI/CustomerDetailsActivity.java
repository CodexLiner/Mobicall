package com.mobicall.call.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.mobicall.call.R;
import com.mobicall.call.database.callHistory;
import com.mobicall.call.databinding.ActivityCustomerDetailsBinding;
import com.mobicall.call.models.CallHistory;
import com.mobicall.call.recyclerViews.callHistoryAdapter;
import com.mobicall.call.stateManager.Constants;

import java.util.List;

public class CustomerDetailsActivity extends AppCompatActivity {
    ActivityCustomerDetailsBinding binding;
    callHistoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCustomerDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if (Constants.CallHistory!=null && Constants.CallHistory.getPhone() !=null){
            if (Constants.CallHistory.getDescription()!=null){
                binding.description.setText(Constants.CallHistory.getDescription());
            }
            binding.customerEmail.setText(Constants.CallHistory.getEmail());
            binding.customerName.setText(Constants.CallHistory.getContact_name());
            binding.customerMobile.setText(Constants.CallHistory.getPhone());
            Log.d("TAG", "getLogsByNumber: added");
            List<CallHistory> list = callHistory.getLogsByNumber(this , new String[]{Constants.CallHistory.getPhone() });
            binding.callHistoryRec.setLayoutManager(new LinearLayoutManager(this , LinearLayoutManager.VERTICAL , false));
            adapter = new callHistoryAdapter(list);
            binding.callHistoryRec.setAdapter(adapter);
        }
        binding.whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TemplateList.class);
                intent.putExtra("uriName" , "wp");
                intent.putExtra("send_to" , Constants.CallHistory.getPhone());
                startActivity(intent);
            }
        });
        binding.sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TemplateList.class);
                intent.putExtra("uriName" , "sms");
                intent.putExtra("send_to" , Constants.CallHistory.getPhone());
                startActivity(intent);
            }
        });
        binding.email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TemplateList.class);
                intent.putExtra("uriName" , "email");
                intent.putExtra("send_to" , Constants.CallHistory.getEmail());
                startActivity(intent);
            }
        });
        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}