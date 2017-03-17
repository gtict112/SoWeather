package com.example.administrator.soweather.com.example.administrator.soweather.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.soweather.R;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.ChatListData;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.Result;
import com.example.administrator.soweather.com.example.administrator.soweather.service.CustomerService;
import com.example.administrator.soweather.com.example.administrator.soweather.utils.ResponseListenter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/11/10.
 */

public class CustomerServiceActivity extends Activity implements View.OnClickListener, ResponseListenter<String> {
    private List<ChatListData> list;
    private ListView lv;
    private Button send_btn;
    private EditText sendtext;
    private String content_str;
    private TextAdapter adapter;
    private String[] welcomeArray;
    private double currenttime, oldTime = 0;
    private ImageView topButton;
    private TextView topTv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_service);
        initView();
    }


    private void initView() {
        list = new ArrayList<ChatListData>();
        lv = (ListView) findViewById(R.id.lv);
        send_btn = (Button) findViewById(R.id.send_btn);
        sendtext = (EditText) findViewById(R.id.senText);
        topButton = (ImageView) findViewById(R.id.topButton);
        topTv = (TextView) findViewById(R.id.topTv);
        topTv.setText("智能助手");
        topButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        send_btn.setOnClickListener(this);
        adapter = new TextAdapter(list, this);
        lv.setAdapter(adapter);
        ChatListData listData = null;
        listData = new ChatListData(getRandomWelcomeTips(), listData.receiver, getTime());
        System.out.println("时间" + listData);
        list.add(listData);

    }

    public void parseText(String str) {
        try {
            ChatListData listData = null;
            listData = new ChatListData(str, listData.receiver, getTime());
            System.out.println("时间" + listData);
            list.add(listData);
            adapter.notifyDataSetChanged();
            lv.setSelection(adapter.getCount());
            lv.smoothScrollToPosition(adapter.getCount() - 1);
        } catch (Exception e) {
        }
    }

    public void onClick(View v) {
        content_str = sendtext.getText().toString();
        sendtext.setText("");
        String dropk = content_str.replace(" ", "");
        String droph = dropk.replace("\n", "");
        ChatListData listdata = null;
        listdata = new ChatListData(content_str, listdata.send, getTime());
        System.out.println("sfds" + listdata);
        list.add(listdata);
        if (list.size() > 30) {
            for (int i = 0; i < list.size(); i++) {
                list.remove(i);
            }
        }
        adapter.notifyDataSetChanged();
        lv.setSelection(adapter.getCount());
        senData(droph);
    }

    private void senData(String droph) {
        CustomerService service = new CustomerService();
        service.getCustomerService("http://www.tuling123.com/openapi/api?key=61a58c763ee044d289957571541d4801&info=", droph, this);
    }

    private String getRandomWelcomeTips() {
        String welcome_tipe = null;
        welcomeArray = this.getResources().getStringArray(R.array.welcome_tips);
        int index = (int) (Math.random() * (welcomeArray.length - 1));
        welcome_tipe = welcomeArray[index];
        return welcome_tipe;
    }

    private String getTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日  HH:mm:ss");
        Date curdata = new Date(System.currentTimeMillis());
        String str = format.format(curdata);
        return str;
    }

    @Override
    public void onReceive(Result<String> result) {
        if (result.isSuccess()) {
            parseText(result.getData());
        } else {
            Toast.makeText(this, result.getErrorMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private class TextAdapter extends BaseAdapter {
        private List<ChatListData> lists;
        private Context mContext;
        private RelativeLayout layout;

        public TextAdapter(List<ChatListData> lists, Context mContext) {
            this.lists = lists;
            this.mContext = mContext;
        }

        @Override
        public int getCount() {
            return lists.size();
        }

        @Override
        public Object getItem(int position) {
            return lists.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            if (lists.get(position).getFlag() == ChatListData.receiver) {
                layout = (RelativeLayout) inflater.inflate(R.layout.customer_service_leftitem, null);
            }
            if (lists.get(position).getFlag() == ChatListData.send) {
                layout = (RelativeLayout) inflater.inflate(R.layout.customer_service_right, null);
            }
            TextView tv = (TextView) layout.findViewById(R.id.tv);
            TextView time = (TextView) layout.findViewById(R.id.time);
            time.setText(lists.get(position).getTime());
            tv.setText(lists.get(position).getContent());
            return layout;
        }
    }
}
