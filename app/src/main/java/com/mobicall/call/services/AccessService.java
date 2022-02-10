package com.mobicall.call.services;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Intent;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import com.mobicall.call.stateManager.CallState;

public class AccessService extends AccessibilityService {
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
//        Intent intent1 = new Intent(this , ForegroundService.class);
//        startService(intent1);
//         Log.d("TAG", "onAccessibilityEvent: ");
//        startService(new Intent(this.getApplicationContext() , CallState.class));
    }

    @Override
    public void onInterrupt() {
        onServiceConnected();
    }

    @Override
    protected void onServiceConnected() {
        Log.d("TAG", "onAccessibilityEvent: ");

        super.onServiceConnected();
        AccessibilityServiceInfo info = new AccessibilityServiceInfo();

        info.eventTypes = AccessibilityEvent.TYPE_VIEW_CLICKED | AccessibilityEvent.TYPE_VIEW_FOCUSED;
        info.packageNames = new String[] {getPackageName(), getPackageName()};
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_VISUAL;
        info.notificationTimeout = 1;
        this.setServiceInfo(info);
    }
}
