package com.mobicall.call.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;

import com.mobicall.call.R;
import com.mobicall.call.databinding.ActivityTemplateListBinding;
import com.mobicall.call.recyclerViews.TemplateAdapter;
import com.mobicall.call.stateManager.Constants;

public class TemplateList extends AppCompatActivity {
    String senderUri = null;
    String sendNumber = null;
    ActivityTemplateListBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTemplateListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        senderUri = getIntent().getStringExtra("uriName");
        sendNumber = getIntent().getStringExtra("send_to");
        binding.templatesView.setLayoutManager(new LinearLayoutManager(this , LinearLayoutManager.VERTICAL , false));
        if (senderUri.equals("sms")){
            TemplateAdapter templateAdapter = new TemplateAdapter("sms" ,sendNumber , Constants.whatsAppTemplates);
            binding.templatesView.setAdapter(templateAdapter);
        }else if (senderUri.equals("email")){
            TemplateAdapter templateAdapter = new TemplateAdapter("email" ,sendNumber , Constants.whatsAppTemplates);
            binding.templatesView.setAdapter(templateAdapter);
        }else if (senderUri.equals("wp")){
            TemplateAdapter templateAdapter = new TemplateAdapter("wp" ,sendNumber , Constants.whatsAppTemplates);
            binding.templatesView.setAdapter(templateAdapter);
        }


    }
}