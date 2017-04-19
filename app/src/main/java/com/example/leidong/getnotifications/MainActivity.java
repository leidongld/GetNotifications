package com.example.leidong.getnotifications;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners";

    final static String UPDATE = "com.example.leidong.getnotifications.NOTIFICATION_LISTENER_EXAMPLE";

    private NotificationReceiver notificationReceiver;
    private ListView listView;
    private Button start;
    private Button stop;
    private Button clear;
    private Button show;

    private List<Bean> list;
    private ListViewAdapter listViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(notificationReceiver);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        notificationReceiver = new NotificationReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(UPDATE);
        registerReceiver(notificationReceiver, filter);

        list = new ArrayList<>();
        listViewAdapter = new ListViewAdapter(this, list);
        listView.setAdapter(listViewAdapter);
    }

    /**
     * 获取控件
     */
    private void initView() {
        start = (Button) findViewById(R.id.start);
        stop = (Button) findViewById(R.id.stop);
        clear = (Button) findViewById(R.id.clear);
        show = (Button) findViewById(R.id.show);
        listView = (ListView) findViewById(R.id.listView);

        start.setOnClickListener(MainActivity.this);
        stop.setOnClickListener(MainActivity.this);
        clear.setOnClickListener(MainActivity.this);
        show.setOnClickListener(MainActivity.this);
    }

    /**
     * 按钮点击事件监听
     */
    @Override
    public void onClick(View v) {
         switch (v.getId()){
             case R.id.start:
                 if(!isEnabled()){
                     Intent intent1 = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
                     startActivity(intent1);
                 }
                 else{
                     Toast.makeText(MainActivity.this, "已经拿到相关权限", Toast.LENGTH_SHORT).show();
                 }
                 break;
             case R.id.stop:
                 Intent intent2 = new Intent(MainActivity.this, MyService.class);
                 stopService(intent2);
                 break;
             case R.id.clear:
                 Intent intent3 = new Intent(MyService.COMMAND);
                 intent3.putExtra(MyService.COMMAND_EXTRA, MyService.CANCEL_ALL);
                 sendBroadcast(intent3);
                 break;
             case R.id.show:
                 Intent intent4 = new Intent(MyService.COMMAND);
                 intent4.putExtra(MyService.COMMAND_EXTRA, MyService.GET_LIST);
                 sendBroadcast(intent4);
                 v.setEnabled(false);
                 break;
             default:
                 break;
         }
    }

    /**
     * 监听通知权限是否打开
     * @return
     */
    private boolean isEnabled() {
        String pkgName = getPackageName();
        final String flat = Settings.Secure.getString(getContentResolver(),
                ENABLED_NOTIFICATION_LISTENERS);
        if (!TextUtils.isEmpty(flat)) {
            final String[] names = flat.split(":");
            for (int i = 0; i < names.length; i++) {
                final ComponentName cn = ComponentName.unflattenFromString(names[i]);
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.getPackageName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 内部类
     */
    class NotificationReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bean bean = new Bean();
            Bundle bundle = intent.getExtras();
            bean.title = bundle.getString(Notification.EXTRA_TITLE);
            bean.text = bundle.getString(Notification.EXTRA_TEXT);
            list.add(bean);
            listViewAdapter.notifyDataSetChanged();
        }
    }
}
