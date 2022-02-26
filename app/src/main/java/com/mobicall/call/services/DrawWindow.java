package com.mobicall.call.services;

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Build;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.mobicall.call.R;
import com.mobicall.call.models.contacts;
import com.mobicall.call.others.staticFunctions;
import com.mobicall.call.stateManager.Constants;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class DrawWindow {
    private static final String TAG = "TAG";
    private Context context;
    private View mView;
    private WindowManager.LayoutParams mParams;
    private WindowManager mWindowManager;
    private LayoutInflater layoutInflater;
    private  WindowManager.LayoutParams layoutParams;
    public static List<contacts> inWindow;
    TextView callType , callNum , callerNum;
    EditText name , email , desc , mobile ;
    static String mNumber;

    public static List<contacts> getInWindow() {
        return inWindow;
    }

    public static void setInWindow(List<contacts> inWindow) {
        DrawWindow.inWindow = inWindow;
    }

    public DrawWindow(Context context) {
        this.context = context;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // set the layout parameters of the window
            mParams = new WindowManager.LayoutParams(
                    // Shrink the window to wrap the content rather
                    // than filling the screen
                    WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT,
                    // Display it on top of other application windows
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                    // Don't let it grab the input focus
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    // Make the underlying application window visible
                    // through any transparent parts
                    PixelFormat.TRANSLUCENT);

            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // inflating the view with the custom layout we created
            mView = layoutInflater.inflate(R.layout.window_layout, null);
            // set onClickListener on the remove button, which removes
            // the view from the window
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x;
            int height = size.y;
            Log.d("TAG", "DrawWindow: "+width+" "+height);

            mParams.verticalMargin = -0.3f;
            mParams.width = width -10;
            mParams.gravity = Gravity.CENTER_HORIZONTAL;
            mWindowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
            name = mView.findViewById(R.id.intName);
            email = mView.findViewById(R.id.intEmail);
            mobile = mView.findViewById(R.id.intMobile);
            callerNum = mView.findViewById(R.id.callerNum);
            callNum = mView.findViewById(R.id.callNum);
            desc = mView.findViewById(R.id.intDesc);
            // filling field data in form
            staticFunctions.getContactinfo(mNumber ,null, context);
            contacts u = null;
            if (Constants.MN !=null && Constants.MN.startsWith("+")){
                Constants.MN = Constants.MN.substring(3);
            }
            if (Constants.CustomerList!=null){
                for (int i = 0; i < Constants.CustomerList.size(); i++) {
                    if (Constants.CustomerList.get(i).getPhone()!=null){
                        if (Constants.CustomerList.get(i).getPhone().equals(Constants.MN)){
                            u = Constants.CustomerList.get(i);
                        }
                    }
                }
            }
            if (u!=null){
                name.setText(u.getContact_name());
                email.setText(u.getEmail());
                mobile.setText(u.getPhone());
                callerNum.setText(u.getPhone());
                callNum.setText(u.getContact_name());
            }else if (Constants.UserDetails!=null){
                u = Constants.UserDetails;
                name.setText(u.getContact_name());
                email.setText(u.getEmail());
                mobile.setText(u.getPhone());
                callerNum.setText(u.getPhone());
                callNum.setText(u.getContact_name());
            }else {
                Log.d(TAG, "DrawWindow: all null "+Constants.MN);
                callerNum.setText(Constants.MN);
                mobile.setText(Constants.MN);
            }
//            RelativeLayout layout = mView.findViewById(R.id.layoutWindow);
            mView.setOnTouchListener(new View.OnTouchListener() {
                  private int initialX;
                  private int initialY;
                  private float initialTouchX;
                  private float initialTouchY;
                  @Override
                  public boolean onTouch(View v, MotionEvent event) {
                      switch (event.getAction()) {
                          case MotionEvent.ACTION_DOWN:
                              initialX = mParams.x;
                              initialY = mParams.y;
                              initialTouchX = event.getRawX();
                              initialTouchY = event.getRawY();
                              return true;
                          case MotionEvent.ACTION_UP:
                              int Xdiff = (int) (event.getRawX() - initialTouchX);
                              int Ydiff = (int) (event.getRawY() - initialTouchY);
                              if (Xdiff < 10 && Ydiff < 10) {
                              }
                              return true;
                          case MotionEvent.ACTION_MOVE:
                              mParams.x = initialX + (int) (event.getRawX() - initialTouchX);
                              mParams.y = initialY + (int) (event.getRawY() - initialTouchY);
                              mWindowManager.updateViewLayout(mView, mParams);

                              return true;
                      }
                      return false;
                  }
              });
//            submit data on server
            mView.findViewById(R.id.buttonNotIntrested).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Constants.byCallTask && inWindow!=null){
                        String time = staticFunctions.getLastCallTime(context ,null);
                        UpdaterClass updaterClass = new UpdaterClass(Constants.UserDetails.getId() ,
                                null , null, "connected" , time , context,
                                 Constants.UserDetails.getContact_name()
                                ,Constants.UserDetails.getPhone()
                                , Constants.UserDetails.getEmail());
                        updaterClass.execute();
                        new Timer().schedule(new TimerTask() {
                            @Override
                            public void run() {
                                if (Constants.byCallTask){
                                    Constants.byCallTask = false;
                                    callTask callTask = new callTask(Constants.windowContact ,  Constants.indexValue , context);
                                    callTask.execute();
                                }

                            }
                        },5000);
                    }else {
                        String time = staticFunctions.getLastCallTime(context , null);
                        String callStatus ="connected";
                        UpdaterClass updaterClass = new UpdaterClass(Constants.UserDetails.getId() ,
                                desc.getText().toString().trim() ,
                                "0", callStatus , time, context ,
                                name.getText().toString()
                                ,Constants.UserDetails.getPhone()
                                ,email.getText().toString());
                        updaterClass.execute();
                    }
                    close();
                }
            });
            mView.findViewById(R.id.closeButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            if (Constants.byCallTask){
                                Constants.byCallTask = false;
                                callTask callTask = new callTask(Constants.windowContact ,  Constants.indexValue , context);
                                callTask.execute();
                            }

                        }
                    },5000);
                    close();
                }
            });
            mView.findViewById(R.id.buttonIntrested).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mParams.flags = 0;
                    mWindowManager.updateViewLayout(mView, mParams);
                    mView.findViewById(R.id.buttonIntrested).setVisibility(View.INVISIBLE);
                    mView.findViewById(R.id.expandLayout).setVisibility(View.VISIBLE);
                }
            });
            //  submit button click
            mView.findViewById(R.id.buttonSubmit).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Constants.byCallTask && inWindow!=null){
                        String time = staticFunctions.getLastCallTime(context , null);
                        String callStatus ="connected";
                        String id = null;
                        if (inWindow.get(Constants.indexValue- 1) !=null && Constants.UserDetails.getId()!=null){
                            id = Constants.UserDetails.getId();
                        }
                        UpdaterClass updaterClass = new UpdaterClass(id ,
                                desc.getText().toString().trim() ,
                                "1", callStatus , time, context ,
                                Constants.UserDetails.getContact_name()
                                ,Constants.UserDetails.getPhone()
                                , Constants.UserDetails.getEmail());
                        updaterClass.execute();
                        new Timer().schedule(new TimerTask() {
                            @Override
                            public void run() {
                                callTask callTask = new callTask(Constants.windowContact ,  Constants.indexValue , context);
                                callTask.execute();
                            }
                        },5000);
                    } else {
                        String id = null;
                        String time = staticFunctions.getLastCallTime(context , null);
                        String callStatus ="connected";
                        UpdaterClass updaterClass = new UpdaterClass(id ,
                                desc.getText().toString().trim() ,
                                "1", callStatus , time, context ,
                                name.getText().toString()
                                ,Constants.UserDetails.getPhone()
                                ,email.getText().toString());
                        updaterClass.execute();
                    }
                    close();
                }
            });

        }
    }

    public void close() {
        Constants.UserDetails = null;
        Constants.isWindowOpen = false;
        Log.d(TAG, "callstate: ");
        try {
            ((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).removeView(mView);
            mView.invalidate();
           if (mView!=null){
//               ((ViewGroup)mView.getParent()).removeAllViews();
           }

        } catch (Exception e) {
            Log.d("callstate",e.toString());
        }
    }
    public void openWith(){
        callType = mView.findViewById(R.id.callType);
        try {
            Thread.sleep(3000);
            // variables
            name = mView.findViewById(R.id.intName);
            email = mView.findViewById(R.id.intEmail);
            mobile = mView.findViewById(R.id.intMobile);
            callerNum = mView.findViewById(R.id.callerNum);
            callNum = mView.findViewById(R.id.callNum);
            desc = mView.findViewById(R.id.intDesc);
            callType = mView.findViewById(R.id.callType);
            if (Constants.otherCalls!=null){
                Log.d(TAG, "openWith: if");
                // assign variables
                callType.setText(Constants.otherCalls.getCallType());
                callNum.setText(Constants.otherCalls.getName());
                name.setText(Constants.otherCalls.getName());
                mobile.setText(Constants.otherCalls.getNumber());
                callerNum.setText(Constants.otherCalls.getNumber());
                //set window
                if (!Constants.isWindowOpen){
                    Log.d(TAG, "openWith: else else");
                    mWindowManager.addView(mView, mParams);
                    Constants.isWindowOpen = true;
                }
            }else {
                Log.d(TAG, "openWith: else");
                // set window
//                callType.setText(number);
//                callerNum.setText(number);
//                callType.setText(mCallType);
                if(mView.getParent()==null) {
                    if (!Constants.isWindowOpen){
                        Log.d(TAG, "openWith: else else");
                        mWindowManager.addView(mView, mParams);
                        Constants.isWindowOpen = true;
                    }
                }
            }
        }catch (Exception e){
            Log.d(TAG, "openWith: "+e);
        }
    }

    private void updateUi(contacts user) {
        if (user!=null){
            callNum.setText(user.getContact_name());
            name.setText(user.getContact_name());
            email.setText(user.getEmail());
            mobile.setText(user.getPhone());
            callerNum.setText(user.getPhone());
        }
    }

    public void open(String cType , String cNum) {
    try {
        Constants.MN = cNum;
        staticFunctions.getContactinfo(mNumber ,null, context);
        contacts u = null;
        if (Constants.MN !=null && Constants.MN.startsWith("+")){
            Constants.MN = Constants.MN.substring(3);
        }
        if (Constants.CustomerList!=null){
            for (int i = 0; i < Constants.CustomerList.size(); i++) {
                if (Constants.CustomerList.get(i).getPhone()!=null){
                    if (Constants.CustomerList.get(i).getPhone().equals(cNum)){
                        u = Constants.CustomerList.get(i);
                        Constants.UserDetails = u;
                    }
                }
            }
        }
        if (u!=null){
            Log.d(TAG, "open: is 1");
            name.setText(u.getContact_name());
            email.setText(u.getEmail());
            mobile.setText(u.getPhone());
            callerNum.setText(u.getPhone());
            callNum.setText(u.getContact_name());
        }
        else if (Constants.UserDetails!=null){
            Log.d(TAG, "open: is 2");

            u = Constants.UserDetails;
            name.setText(u.getContact_name());
            email.setText(u.getEmail());
            mobile.setText(u.getPhone());
            callerNum.setText(u.getPhone());
            callNum.setText(u.getContact_name());
        }else {
            Constants.UserDetails = new contacts();
            Constants.UserDetails.setEmail("");
            Constants.UserDetails.setContact_name("");
            Constants.UserDetails.setId("");
            Constants.UserDetails.setPhone(cNum);

            Log.d(TAG, "open: is 3");
            name.setText("");
            email.setText("");
            callNum.setText("");
            Log.d(TAG, "DrawWindow: all null "+Constants.MN);
            callerNum.setText(Constants.MN);
            mobile.setText(Constants.MN);
        }
        mNumber = cNum;
        Constants.MN = cNum;
        TextView callType , callNum;
        callNum = mView.findViewById(R.id.callNum);
        if (u!=null && u.getContact_name()!=null){
            callNum.setText(u.getContact_name());
        }else {
            callNum.setText(cNum);
        }
        callType = mView.findViewById(R.id.callType);
        // check if the view is already
        // inflated or present in the window
        if(mView.getWindowToken()==null) {
            if(mView.getParent()==null) {
            if (!Constants.isWindowOpen){
                callType.setText(cType);
                mWindowManager.addView(mView, mParams);
                Constants.isWindowOpen = true;
            }
        }
    }
    } catch (Exception e) {
        Log.d("Error1",e.toString());
    }
  }
}
