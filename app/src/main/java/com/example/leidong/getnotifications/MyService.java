package com.example.leidong.getnotifications;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.annotation.RequiresApi;

/**
 * Created by leidong on 2017/4/19.
 */

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
@SuppressLint("OverrideAbstract")
public class MyService extends NotificationListenerService {
    private static final String TAG = "MyService";

    public static final String COMMAND = "com.example.leidong.COMMAND_NOTIFICATION_LISTENER_SERVICE";
    public static final String COMMAND_EXTRA = "command";
    public static final String CANCEL_ALL = "clear";
    public static final String GET_LIST = "show";

    private MyServiceReceiver myServiceReceiver;

    @Override
    public void onCreate(){
        super.onCreate();
        myServiceReceiver = new MyServiceReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(COMMAND);
        registerReceiver(myServiceReceiver, filter);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        unregisterReceiver(myServiceReceiver);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onNotificationPosted(StatusBarNotification sbn){
        Notification notification = sbn.getNotification();
        Intent intent = new Intent(MainActivity.UPDATE);
        intent.putExtras(notification.extras);
        sendBroadcast(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onNotificationRemoved(StatusBarNotification sbn){
        Intent intent = new Intent(MainActivity.UPDATE);
        Notification notification = sbn.getNotification();
        intent.putExtras(notification.extras);
        sendBroadcast(intent);
    }

    /**
     * 内部类
     */
    private class MyServiceReceiver extends BroadcastReceiver {
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void onReceive(Context context, Intent intent) {
            String command = intent.getStringExtra(COMMAND_EXTRA);
            if (command.equals(CANCEL_ALL)) {
                MyService.this.cancelAllNotifications();
            }
            else if (command.equals(GET_LIST)) {
                int i = 1;
                for (StatusBarNotification sbn : MyService.this.getActiveNotifications()) {
                    Intent i2 = new Intent(MainActivity.UPDATE);
                    Notification notification = sbn.getNotification();
                    i2.putExtras(notification.extras);
                    sendBroadcast(i2);
                    i++;
                }
            }
        }
    }
}
