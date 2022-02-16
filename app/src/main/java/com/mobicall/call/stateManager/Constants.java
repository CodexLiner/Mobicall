package com.mobicall.call.stateManager;

import com.mobicall.call.models.OtherCalls;
import com.mobicall.call.models.TemplateModel;
import com.mobicall.call.models.contacts;

import java.util.List;

public class Constants {
    public static final String TAG = "Call recorder";

    public static final String FILE_NAME_PATTERN = "^[\\d]{14}_[_\\d]*\\..+$";
    public static List<TemplateModel> whatsAppTemplates;
    public static List<TemplateModel> EmailTemplates;
    public static List<contacts> CustomerList;
    public static final int STATE_INCOMING_NUMBER = 1;
    public static final int STATE_CALL_START = 2;
    public static final int STATE_CALL_END = 3;
    public static final int RECORDING_ENABLED = 4;
    public static final int RECORDING_DISABLED = 5;
    public static final int OUTGOING = 7;
    public static final String mediaType = "application/json; charset=utf-8";
    public static final String baseUrlbackend= "https://mobicall.live/public/api/";
    public static boolean isWindowOpen = false;
    public static boolean byCallTask = false;
    public static boolean isLogin = false;
    public static String NOTIFICATION = "Play/Pause";
    public static int indexValue;
    public static List<contacts> windowContact;
    public static contacts CallHistory;
    public static OtherCalls otherCalls;
}
