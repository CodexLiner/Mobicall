package com.mobicall.call.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

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
            binding.customerEmail.setText(Constants.CallHistory.getEmail());
            binding.customerName.setText(Constants.CallHistory.getContact_name());
            List<CallHistory> list = callHistory.getLogsByNumber(this , new String[]{ "+91"+Constants.CallHistory.getPhone() });
            binding.callHistoryRec.setLayoutManager(new LinearLayoutManager(this , LinearLayoutManager.VERTICAL , false));
            adapter = new callHistoryAdapter(list);
            binding.callHistoryRec.setAdapter(adapter);
        }

    }
}