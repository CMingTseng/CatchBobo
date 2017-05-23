package c.min.tseng.xmpp;

import android.app.Activity;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.packet.Message;

import c.min.tseng.R;

public class ChatRoom extends Activity implements OnMessageListener,
        OnClickListener {
    private MessageReceiver mUpdateMessage;
    private String mContactAccount;
    private Thread mThread;
    private EditText metMessageList;
    private EditText metMessage;
    private Chat mChat;
    private LocationManager locationManager;

    @Override
    protected void onDestroy() {
        GTalk.mChattingContactMap.put(mContactAccount, false);
        mUpdateMessage.flag = false;
        mUpdateMessage.mCollector.cancel();
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        String msg = metMessage.getText().toString();
        if (!"".equals(msg)) {
            try {
                mChat.sendMessage(msg);
                metMessage.setText("");
                metMessageList.append("我：\n");
                metMessageList.append(msg + "\n\n");
            } catch (Exception e) {
                // TODO: handle exception
            }
        }

    }

    private void sendMsg(String from, String msg) {
        if ("#position".equals(msg.toLowerCase()) || "#位置".equals(msg)) {
            try {

                metMessageList.append("自動回復："
                        + GTalk.mUtil.getLeftString(from, "@") + "\n");

                Criteria criteria = new Criteria();
                // 獲得最好的定位效果
                criteria.setAccuracy(Criteria.ACCURACY_FINE);
                criteria.setAltitudeRequired(false);
                criteria.setBearingRequired(false);
                criteria.setCostAllowed(false);
                // 使用省電模式
                criteria.setPowerRequirement(Criteria.POWER_LOW);
                // 獲得當前的位置提供者
                String provider = locationManager.getBestProvider(criteria,
                        true);
                // 獲得當前的位置
                Location location = locationManager
                        .getLastKnownLocation(provider);
                // 獲得當前位置的緯度
                Double latitude = location.getLatitude();
                // 獲得當前位置的經度
                Double longitude = location.getLongitude();
                mChat.sendMessage("機器人：\n經度：" + longitude + "\n緯度："
                        + latitude + "\n\n");

            } catch (Exception e) {

            }
        } else {
            metMessageList.append(GTalk.mUtil.getLeftString(from, "/") + ":\n");
            metMessageList.append(msg + "\n\n");
        }

    }

    @Override
    public void processMessage(Message message) {
        sendMsg(message.getFrom(), message.getBody());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatroom);
        metMessageList = (EditText) findViewById(R.id.ET0013);
        metMessage = (EditText) findViewById(R.id.ET0014);
        Button btnSend = (Button) findViewById(R.id.BT008);
        btnSend.setOnClickListener(this);

        mContactAccount = getIntent().getStringExtra("contactAccount");
        String msg = getIntent().getStringExtra("msg");
        if (msg != null) {
            sendMsg(mContactAccount, msg);
        }

        setTitle(mContactAccount);
        mUpdateMessage = new MessageReceiver(mContactAccount);
        mUpdateMessage.setOnMessageListener(this);
        mThread = new Thread(mUpdateMessage);
        mThread.start();
        ChatManager chatmanager = GTalk.mConnection.getChatManager();
        mChat = chatmanager.createChat(mContactAccount, null);
        locationManager = (LocationManager) getSystemService(this.LOCATION_SERVICE);
    }

}
