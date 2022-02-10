package com.mobicall.call.services;

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mobicall.call.R;
import com.mobicall.call.database.userDatabaseHelper;
import com.mobicall.call.database.userDatabaseModel;
import com.mobicall.call.models.contacts;
import com.mobicall.call.models.totalCount;
import com.mobicall.call.stateManager.Constants;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.sax2.Driver;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DrawWindow {
    private Context context;
    private View mView;
    private WindowManager.LayoutParams mParams;
    private WindowManager mWindowManager;
    private LayoutInflater layoutInflater;
    private  WindowManager.LayoutParams layoutParams;
    public static List<contacts> inWindow;
    TextView callType , callNum;
    EditText name , email , desc , mobile;

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
            mParams.width = width - 50;
            mParams.gravity = Gravity.CENTER_HORIZONTAL;
            mWindowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
            name = mView.findViewById(R.id.intName);
            email = mView.findViewById(R.id.intEmail);
            mobile = mView.findViewById(R.id.intMobile);
            desc = mView.findViewById(R.id.intDesc);
            // filling field data in form
            if (inWindow !=null && Constants.indexValue - 1 <= inWindow.size()  && inWindow.get(Constants.indexValue - 1) !=null){
                name.setText(inWindow.get(Constants.indexValue-1).getContact_name());
                email.setText(inWindow.get(Constants.indexValue-1).getEmail());
                mobile.setText(inWindow.get(Constants.indexValue-1).getPhone());
            }
            //  submit button click
            mView.findViewById(R.id.buttonSubmit).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   if (inWindow!=null){
                       UpdaterClass updaterClass = new UpdaterClass(inWindow.get(Constants.indexValue- 1).getId() ,
                               desc.getText().toString().trim() , "1", "connected" , "4" , context);
                       updaterClass.execute();
                       callTask callTask = new callTask(Constants.windowContact ,  Constants.indexValue , context);
                       callTask.execute();
                   }
                    close();
                }
            });
            mView.findViewById(R.id.buttonNotIntrested).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (inWindow!=null){
                        UpdaterClass updaterClass = new UpdaterClass(inWindow.get(Constants.indexValue- 1).getId() ,
                               null , null, "connected" , null , context);
                        updaterClass.execute();
                        callTask callTask = new callTask(Constants.windowContact ,  Constants.indexValue , context);
                        callTask.execute();
                    }
                    close();
                }
            });
            mView.findViewById(R.id.closeButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callTask callTask = new callTask(Constants.windowContact ,  Constants.indexValue , context);
                    callTask.execute();
                    close();
                }
            });
//            RelativeLayout layout = mView.findViewById(R.id.layoutWindow);
            mView.setOnTouchListener(new View.OnTouchListener() {
                  private int initialX;
                  private int initialY;
                  private float initialTouchX;
                  private float initialTouchY;
                  @Override
                  public boolean onTouch(View v, MotionEvent event) {
                      Log.d("AD","Action E" + event);
                      switch (event.getAction()) {
                          case MotionEvent.ACTION_DOWN:
                              Log.d("AD","Action Down");
                              initialX = mParams.x;
                              initialY = mParams.y;
                              initialTouchX = event.getRawX();
                              initialTouchY = event.getRawY();
                              return true;
                          case MotionEvent.ACTION_UP:

                              Log.d("AD","Action Up");
                              int Xdiff = (int) (event.getRawX() - initialTouchX);
                              int Ydiff = (int) (event.getRawY() - initialTouchY);
                              if (Xdiff < 10 && Ydiff < 10) {
//                                  if (isViewCollapsed()) {
//                                      collapsedView.setVisibility(View.GONE);
//                                      expandedView.setVisibility(View.VISIBLE);
//                                  }
                              }
                              return true;
                          case MotionEvent.ACTION_MOVE:

                              Log.d("AD","Action Move");
                              mParams.x = initialX + (int) (event.getRawX() - initialTouchX);
                              mParams.y = initialY + (int) (event.getRawY() - initialTouchY);
                              mWindowManager.updateViewLayout(mView, mParams);

                              return true;
                      }
                      return false;
                  }
              });
            // Define the position of the
            // window within the screen


//            submit data on server
            mView.findViewById(R.id.buttonIntrested).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mParams.flags = 0;
                    mWindowManager.updateViewLayout(mView, mParams);
                    mView.findViewById(R.id.buttonIntrested).setVisibility(View.INVISIBLE);
                    mView.findViewById(R.id.expandLayout).setVisibility(View.VISIBLE);
                }
            });

        }
    }

    private void close() {
        Constants.isWindowOpen = false;
        try {
            // remove the view from the window
            ((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).removeView(mView);
            // invalidate the view
            mView.invalidate();
            // remove all views
           if (mView!=null){
//               ((ViewGroup)mView.getParent()).removeAllViews();
           }

            // the above steps are necessary when you are adding and removing
            // the view simultaneously, it might give some exceptions
        } catch (Exception e) {
            Log.d("Error2",e.toString());
        }
    }
    public void openWith(String mNumber){
        Gson gson = new Gson();
        userDatabaseHelper db = new userDatabaseHelper(context);
        userDatabaseModel model = db.getUser(0);
        if (model.getAuth()==null){
            return;
        }
        Request request = new Request.Builder().url(Constants.baseUrlbackend +"check/user/"+mNumber).addHeader("authorization" , "Bearer "+model.getAuth()).get().build();
        new OkHttpClient()
                .newCall(request)
                .enqueue(
                        new Callback() {
                            @Override
                            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                Log.d("TAG", "onFailure: a" + e);
                            }

                            @Override
                            public void onResponse(@NonNull Call call, @NonNull Response response)
                                    throws IOException {
                                Type listType = new TypeToken<List<contacts>>() {}.getType();
                                try {
                                    JSONObject jsonResponse = new JSONObject(response.body().string());
                                    Type type = new TypeToken<List<contacts>>(){}.getType();
                                    totalCount count = gson.fromJson(jsonResponse.toString(), totalCount.class);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
    }
    public void open(String cType , String cNum) {
    try {
        TextView callType , callNum;
        callNum = mView.findViewById(R.id.callNum);
        callType = mView.findViewById(R.id.callType);
        // check if the view is already
        // inflated or present in the window
        if(mView.getWindowToken()==null) {
            if(mView.getParent()==null) {
            if (!Constants.isWindowOpen){
                callNum.setText(cNum);
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
