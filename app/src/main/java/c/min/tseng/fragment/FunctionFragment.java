package c.min.tseng.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import c.min.tseng.R;

public class FunctionFragment extends Fragment {
    private final static String TAG = "FunctionFragment";

//    ////////多執行緒-Handler和Thread  ///////////////////////
//    //透過公會找到U成員的經紀人，這樣才能派遣工作  (找到顯示畫面的UI Thread上的Handler)
//    private Handler mUI_Handler = new Handler();
//    private Handler mThreadHandler; ///宣告臨時工的經紀人
//    private HandlerThread mThread; ///宣告臨時工
//    ///////////////////////////////////////////////////////////////////////////////////////
//    //SQLlite處理變數
//    private static final String DB_FILE1 = "auth.db", DB_TABLE11 = "auth", DB_FILE2 = "company.db", DB_TABLE21 = "company", DB_FILE3 = "Locus.db", DB_TABLE31 = "Locus", DB_FILE4 = "BillingFragment.db", DB_TABLE41 = "BillingFragment";
//
//    private SQLiteDatabase haHelp, coHelp, gpsHelp, BillHelp;
//
//    private DbHelper handauthHelp, CoDbHp, BillDbHp;
//    //切割用字元
//    private String[] temptime;
//    private String[] strToken3; //切割每欄個人資料
//
//    //手機用變數
//    TelephonyManager Tel;
//    WifiManager mowifi;
//    //個人資料變數
//    public static String strMyName;
//    private String strMyUid; // uid
//    private String strMyGrp;
//    private String strMyTel; // uid
//    private String strMyEmail;
//    //    private String strMystate;
//    private int strMystate;
//    private String myimei;
//
//
//    //功能頁面元件
//    private LinearLayout CRLL0014;
//    private Button BT001, BT002, BT003, BT004, BT005, BT006, BTA001, amainBT101, amainBT102;
//    private TextView TCBF001, TVN001;
//    private EditText BET001, BET002;
//    // 引導其他class
//    Intent function = new Intent(), ocr = new Intent(), inspect = new Intent(), search = new Intent(), setting = new Intent(), adminmap = new Intent(), billing = new Intent();
//
//    private Dialog booked;
//    //驗證用
//    private SharedPreferences settings;
//    public static boolean handSuc = false;
//    public static boolean checkinSuc = false;
//    private String nBET001, pBET002;
//    //    private Intent authi = new Intent();
//    public static String authData = ""; //傳遞資料用
//    public static String[] authMatix;
//
//
//    //location
//    private LocationManager mLocationMgr;
//    private String mBestLocationProv;
//    //    private Location staffloc;
//    static double dLat, dLon, tLat, tLng;
//    private String newUpdate = "";
//    private String newUpdate2 = "";
//    public static String Loccalreport;     //裝置啟用網址
//    private Context mContext;
//
//

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final Context context = container.getContext();
        final Bundle arguments = getArguments();
        final View child = inflater.inflate(R.layout.fragment_function, container, false);
        TextView currenttime = (TextView) child.findViewById(R.id.currenttime);//時間
        TextView trafficwarden_name = (TextView) child.findViewById(R.id.trafficwarden_name);//收費員姓名
        Button checkin = (Button) child.findViewById(R.id.checkin);//上班簽到booked
        Button traffic = (Button) child.findViewById(R.id.traffic);//開單Billing
        traffic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new TakePhotoFragment());
            }
        });
        Button inspection = (Button) child.findViewById(R.id.inspection);//稽查Inspect
        Button statistics = (Button) child.findViewById(R.id.statistics);//統計search
        Button setting = (Button) child.findViewById(R.id.setting);//設定setting
        Button logout = (Button) child.findViewById(R.id.logout);//登出logout
        Button advanced = (Button) child.findViewById(R.id.advanced);//管理者選項
        advanced.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                setFragment(new Map());
            }
        });

        EditText receive = (EditText) child.findViewById(R.id.receive);//時間
        EditText sendmessage = (EditText) child.findViewById(R.id.sendmessage);//時間
        Button send = (Button) child.findViewById(R.id.send);//管理者選項
        return child;
    }

    private void setFragment(Fragment fragment) {
        final Bundle arguments = getArguments();
        fragment.setArguments(arguments);
        try {
            getFragmentManager().beginTransaction().setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out)
                    .replace(R.id.main_content, fragment)
                    .addToBackStack(FunctionFragment.class.getSimpleName())
                    .commit();
        } catch (IllegalStateException ex) {
            Log.d(TAG, "setFragment(): illegal state: " + ex.getMessage());
        }
    }

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        booked = new Dialog(FunctionFragment.this);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);/* 去除TITLE PS:此段必須再宣告LAYOUT前面*/
//
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);// 設定全螢幕
//        //取得本機資料 個人資料定義值
//        TelephonyManager Tel = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
////        WifiManager mowifi = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
//        mowifi = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
//        myimei = Tel.getDeviceId();
//        WifiInfo mymacadd = mowifi.getConnectionInfo();
//
//        //驗證用取得員工資料(從sqlite中)
////        DbHelper handauthHelp = new DbHelper(getApplicationContext(), DB_FILE1,    null, 1);
//        handauthHelp = new DbHelper(getApplicationContext(), DB_FILE1, null, 1);
////        haHelp = handauthHelp.getWritableDatabase();// 讀寫資料庫，因為要checkin .getWritableDatabase可寫入
//        haHelp = handauthHelp.getReadableDatabase();// 讀寫資料庫，因為要checkin .getWritableDatabase可寫入
//        //開啟SQLite取得Loccalreport網址
////        DbHelper CoDbHp = new DbHelper(getApplicationContext(), DB_FILE2,    null, 1);
//        CoDbHp = new DbHelper(getApplicationContext(), DB_FILE2, null, 1);
//        coHelp = CoDbHp.getReadableDatabase();// 查詢資料庫，因為是查詢，所以使用唯讀模式 .getWritableDatabase可寫入
//        Cursor coHelpc = null;
//        coHelpc = coHelp.query(true, DB_TABLE21, new String[]{"url"}, null, null, null, null, null, null);
//        if (coHelpc == null) {
//            return;
//        } else {
//            coHelpc.moveToFirst();//查詢完SQLlite會位於最後.要重新移到最前取值
//            Loccalreport = coHelpc.getString(0);
////            Log.d("WWWWWWWWWWWLoccalreport", Loccalreport);
//        }
//
//        coHelpc.close();
//
//        //
//        setContentView(R.layout.fragment_function);
//
//        //先要強制開啟GPS與網路
//
//        //強制開啟wifi
//        ForceturnOnWif();
//        //檢測網路可否使用
////        NetworkAvailable();
//        //強制開啟GPS
//        ForceopenGPS();//無效
//
//
//        //
//
//        //取得位置
//        mLocationMgr = (LocationManager) getSystemService(LOCATION_SERVICE);
//        //實例化一個Criteria
//        Criteria staffreport = new Criteria();
//        //獲得最好的定位效果
//        staffreport.setAccuracy(Criteria.ACCURACY_FINE);
//        staffreport.setAltitudeRequired(false);
//        staffreport.setBearingRequired(false);
//        staffreport.setCostAllowed(false);
//        //使用省電模式
//        staffreport.setPowerRequirement(Criteria.POWER_LOW);
//
//        //獲得能提供當前位置的提供者
//        mBestLocationProv = mLocationMgr.getBestProvider(staffreport, true);
//        Location staffloca = mLocationMgr.getLastKnownLocation(mBestLocationProv);
//        //
//        getDateTime();
//        TCBF001 = (TextView) findViewById(R.id.TCBF001);//時間
//        TVN001 = (TextView) findViewById(R.id.TVN001);//收費員姓名
//        BT001 = (Button) findViewById(R.id.BT001);//上班簽到booked
//        BT002 = (Button) findViewById(R.id.BT002);//開單Billing
//        BT003 = (Button) findViewById(R.id.BT003);//稽查Inspect
//        BT004 = (Button) findViewById(R.id.BT004);//統計search
//        BT005 = (Button) findViewById(R.id.BT005);//設定setting
//        BT006 = (Button) findViewById(R.id.BT006);//登出logout
//        BTA001 = (Button) findViewById(R.id.BTA001);//管理者選項
//        CRLL0014 = (LinearLayout) findViewById(R.id.CRLL0014);
////        TCBF001.setText(getDateTime());//TextView顯示年月日時分秒
//        strToken3 = getDateTime().split("%20");//用一個getDateTime()取得完整年月日時分秒.自行切割使用
//        TCBF001.setText(strToken3[0]);//TextView顯示年月日
//        //閃爍
//        final Animation animation = new AlphaAnimation(1, 0); // Change alpha from fully visible to invisible
//        animation.setDuration(500); // duration - half a second
//        animation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
//        animation.setRepeatCount(Animation.INFINITE); // Repeat animation infinitely
//        animation.setRepeatMode(Animation.REVERSE); // Reverse animation at the end so the button will fade back in
//        BT001.startAnimation(animation);
//
//
//        setupViewComponent();
//
//
//    }
//
//
//    private void ForceturnOnWif() {
//        // TODO Auto-generated method stub
//        if (!mowifi.isWifiEnabled()) {
//            mowifi.setWifiEnabled(true);
//        }
//
//    }
//
//    private void ForceopenGPS() {
//        // TODO Auto-generated method stub
////        final Intent GPSIntent = new Intent("android.location.GPS_ENABLED_CHANGE");
//        final Intent GPSIntent = new Intent();
//        GPSIntent.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
//        GPSIntent.addCategory(Intent.CATEGORY_ALTERNATIVE);
//        GPSIntent.setData(Uri.parse("3"));
////        GPSIntent.putExtra("enabled", true);
////        this.mContext.sendBroadcast(GPSIntent);
//        sendBroadcast(GPSIntent);
//
////        String GPSprovider = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
////        if(!GPSprovider.contains("gps")){ //if gps is disabled
////        final Intent poke = new Intent();
////        poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
////        poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
////        poke.setData(Uri.parse("3"));
////        this.mContext.sendBroadcast(poke);
//
//    }
//
////	private void NetworkAvailable() {
////		// TODO Auto-generated method stub
////
////	}
//
//
//    public String getDateTime() {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd%20HH:mm:ss");
//        String currentDateandTime = sdf.format(new Date(System.currentTimeMillis()));
//        return currentDateandTime;
//    }
//
//
//    private void setupViewComponent() {
//        // TODO Auto-generated method stub
//        //bookedauth;
//        BT001 = (Button) findViewById(R.id.BT001);
//        BT001.setOnClickListener(BT001Clk);
//        BT002 = (Button) findViewById(R.id.BT002);
//        BT002.setOnClickListener(BT002Clk);
//        BT003 = (Button) findViewById(R.id.BT003);
//        BT003.setOnClickListener(BT003Clk);
//        BT004 = (Button) findViewById(R.id.BT004);
//        BT004.setOnClickListener(BT004Clk);
//        BT005 = (Button) findViewById(R.id.BT005);
//        BT005.setOnClickListener(BT005Clk);
//        BT006 = (Button) findViewById(R.id.BT006);
//        BT006.setOnClickListener(BT006Clk);
//        BTA001 = (Button) findViewById(R.id.BTA001);
//        BTA001.setOnClickListener(BTA001Clk);
//
//
//        settings = getSharedPreferences("CheckinData", 0);
//        int CHECKpass = settings.getInt("CHECK", 0);
//
//
//        if (CHECKpass == 0) {
//
//            BT001.setEnabled(true);
//            BT002.setEnabled(false);
//            BT003.setEnabled(false);
//            BT004.setEnabled(false);
//            BT005.setEnabled(false);
//            BT006.setEnabled(false);
//            BTA001.setEnabled(false);
//        } else {
//
//            BT001.setEnabled(false);
//            BT002.setEnabled(true);
//            BT002.setEnabled(true);
//            BT003.setEnabled(true);
//            BT004.setEnabled(true);
//            BT005.setEnabled(true);
//            BT006.setEnabled(true);
//            BT001.clearAnimation();
//            CRLL0014.setVisibility(View.INVISIBLE);
//            if (CHECKpass > 50) {
//                BTA001.setEnabled(true);
//                BTA001.setVisibility(View.VISIBLE);
//
//            }
//            String Admin = settings.getString("Admin", "null");
//            String TEMPname = settings.getString("TEMPname", "null");
//            authData = Admin;
//            strMyName = TEMPname;
//            TVN001.setText(TEMPname);
//        }
//
//
//    }
//
//
//    private Button.OnClickListener BT001Clk = new Button.OnClickListener() {
//
//        @Override
//        public void onClick(View v) {
//            // TODO Auto-generated method stub
//
//
//            //booked用Dialog
//            booked.setCancelable(true);
//            booked.setTitle(getText(R.string.title));
//            booked.setContentView(R.layout.booked);
//            BET001 = (EditText) booked.findViewById(R.id.BET001);
//            BET002 = (EditText) booked.findViewById(R.id.BET002);
//            amainBT101 = (Button) booked.findViewById(R.id.amainBT101);
//            amainBT101.setOnClickListener(authok);
//            amainBT102 = (Button) booked.findViewById(R.id.amainBT102);
//            amainBT102.setOnClickListener(cancle);
//
//            //本機員工驗證設定Dialog的大小
//            Window dialogWindow = booked.getWindow();
//            WindowManager m = getWindowManager();
//            Display d = m.getDefaultDisplay(); // 抓取螢幕宽、高用
//            WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 抓取Dialog目前的參數值
//            p.height = (int) (d.getHeight() * 0.9); // 高度設為螢幕的1.0
//            p.width = (int) (d.getWidth() * 0.9); // 寬度設為螢幕的0.9
//            dialogWindow.setAttributes(p);
//            booked.show();
//            booked.getWindow().setSoftInputMode(
//                    LayoutParams.SOFT_INPUT_STATE_VISIBLE);// 強制停止鍵盤自動跳出
//
//        }
//
//    };
//
//    private Button.OnClickListener BT002Clk = new Button.OnClickListener() {
//
//        @Override
//        public void onClick(View v) {
//            // TODO Auto-generated method stub
//
//            ////////////////////
//            ocr.setClass(FunctionFragment.this, TakePhotoFragment.class);
//            startActivity(ocr);
//            FunctionFragment.this.finish();
//
////		    fragment_billing.setClass(FunctionFragment.this,BillingFragment.class);
////            startActivity(fragment_billing);
//
//
//        }
//
//    };
//
//    private Button.OnClickListener BT003Clk = new Button.OnClickListener() {
//
//        @Override
//        public void onClick(View v) {
//            // TODO Auto-generated method stub
//            Toast.makeText(getApplicationContext(), getString(R.string.demo), Toast.LENGTH_SHORT).show();
////		    inspect.setClass(FunctionFragment.this,Inspect.class);
////            startActivity(inspect);
////            FunctionFragment.this.finish();
//        }
//
//    };
//
//    private Button.OnClickListener BT004Clk = new Button.OnClickListener() {
//
//        @Override
//        public void onClick(View v) {
//            // TODO Auto-generated method stub
//            //設定SQLite管理者
//            DbHelper BillDbHp = new DbHelper(getApplicationContext(), DB_FILE4, null, 1);
//
//            // 設定建立 開單table 的指令
//            BillDbHp.sCreateTableCommand = "CREATE TABLE " + DB_TABLE41 + "(" +
//                    "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
//                    "PlatePick TEXT," +
//                    "userid TEXT," +
//                    "name TEXT," +
//                    "Btype TEXT," +
//                    "Ntype TEXT," +
//                    "Ctype TEXT," +
//                    "defaucost TEXT," +
//                    "RoadSec TEXT," +
//                    "SYMD TEXT NOT NULL," +
//                    "SHMS TEXT NOT NULL," +
//                    "NYMD TEXT NOT NULL," +
//                    "NHMS TEXT NOT NULL," +
//                    "OYMD TEXT NOT NULL," +
//                    "OHMS TEXT NOT NULL," +
//                    "Lan TEXT," +
//                    "Lng TEXT," +
//                    "Pic TEXT," +
//                    "Sernum1 TEXT," +
//                    "Sernum2 TEXT);";
//
//            //
//            BillHelp = BillDbHp.getWritableDatabase();//getWritableDatabase可寫入
//            search.setClass(FunctionFragment.this, Search.class);
//            startActivity(search);
//            FunctionFragment.this.finish();
//
//        }
//
//    };
//    private Button.OnClickListener BT005Clk = new Button.OnClickListener() {
//
//        @Override
//        public void onClick(View v) {
//            // TODO Auto-generated method stub
//            Toast.makeText(getApplicationContext(), getString(R.string.demo), Toast.LENGTH_SHORT).show();
////		    setting.setClass(FunctionFragment.this,Setting.class);
////            startActivity(setting);
////            FunctionFragment.this.finish();
//
//        }
//
//    };
//
//    private Button.OnClickListener BT006Clk = new Button.OnClickListener() {
//
//        @Override
//        public void onClick(View v) {
//            // TODO Auto-generated method stub
////
////            opeLogout();
//            settings = getSharedPreferences("CheckinData", 0);
//            SharedPreferences.Editor preEdit = settings.edit();
//            preEdit.clear();
//            preEdit.commit();
//            function.setClass(FunctionFragment.this, FunctionFragment.class);
//            FunctionFragment.this.finish();
//            startActivity(function);
//
//        }
//
//        private void opeLogout() {
//            // TODO Auto-generated method stub
////		    settings = getSharedPreferences("CheckinData",0);
////		    SharedPreferences.Editor preEdit = settings.edit();
////		    preEdit.clear();
////		    preEdit.commit();
////		    fragment_function.setClass(FunctionFragment.this,FunctionFragment.class);
////		    startActivity(fragment_function);
//
//        }
//
//    };
//
//    private Button.OnClickListener BTA001Clk = new Button.OnClickListener() {
//
//        @Override
//        public void onClick(View v) {
//            // TODO Auto-generated method stub
////			Log.d("SSSSSSSSSSSSSSSSSS", authData);
//            //建立本機存放與查詢成員的SQLlite用db
//            DbHelper GPSDbHp = new DbHelper(getApplicationContext(), DB_FILE3, null, 1);
//            // 取得上面指定的檔名資料庫，如果該檔名不存在就會自動建立一個資料庫檔案
//            // 然後呼叫 DbHelper 物件的 onCreate() 方法，我們可以在該方法中
//            // 建立資料庫中的 table，或是後面再利用取得的 SQLiteDatabase
//            // 物件建立 table
//
//
//            // 設定建立 table 的指令
//            GPSDbHp.sCreateTableCommand = "CREATE TABLE " + DB_TABLE31 + "(" +
//                    "ID INTEGER PRIMARY KEY," +
//                    "userid TEXT NOT NULL," +
//                    "name TEXT NOT NULL," +
//                    "Gro TEXT," +
//                    "Lan TEXT," +
//                    "Lng TEXT," +
//                    "Tel TEXT," +
//                    "Email TEXT," +
//                    "Pic TEXT," +
//                    "Lasttime TEXT);";
//            // 如果指定的資料庫檔案不存在，就會先建立該檔，然後執行上面的建立 table 指令
//            gpsHelp = GPSDbHp.getWritableDatabase();
//            //先設定一筆自己的假資料
//            insertBossData();
////			Log.d("WOWauthDataWOW", authData);
//            adminmap.setClass(FunctionFragment.this, Map.class);
//            startActivity(adminmap);
//        }
//
//        private void insertBossData() {
//            // TODO Auto-generated method stub
//            ContentValues newRow = new ContentValues();
//            newRow.put("userid", authData);
//            newRow.put("name", strMyName);
//            newRow.put("Gro", "2");
//            newRow.put("Lan", tLat);
//            newRow.put("Lng", tLng);
//            newRow.put("Tel", "0911111111");
//            newRow.put("Email", "www@www.com");
//            newRow.put("Pic", "http://wwww.wwww/1.jpg");
//            newRow.put("Lasttime", getDateTime());
//            gpsHelp.insert(DB_TABLE31, null, newRow);
//            ////
////            Cursor GPSHelpc=null;
////            GPSHelpc=gpsHelp.query(true, DB_TABLE31, new String[]{"userid","name","Lan","Lng","Tel","Email","Lasttime"}, null, null, null, null, null,null);
////            GPSHelpc.moveToFirst();//查詢完SQLlite會位於最後.要重新移到最前取值
////            String SQLiteData;
////            SQLiteData=GPSHelpc.getString(0)+","+GPSHelpc.getString(1)+","+GPSHelpc.getString(2)+","+GPSHelpc.getString(3)+","+GPSHelpc.getString(4)+","+GPSHelpc.getString(5)+","+GPSHelpc.getString(6);
////            Log.d("FWoWSQLiteDataWoWF", SQLiteData);
//        }
//
//    };
//
//    //
//
//    private Button.OnClickListener authok = new Button.OnClickListener() {
//        public void onClick(View v) {
//            //開始員工驗證
////1.trim & check
//            nBET001 = BET001.getText().toString().trim();
//            pBET002 = BET002.getText().toString().trim();
//            strMyUid = BET001.getText().toString();
//            if (nBET001.isEmpty() || pBET002.isEmpty()) {
//                BET001.setText("");
//                BET002.setText("");
//                BET001.setFocusable(true);
//                return;
//            }
////2.check ok, and set the php parameter
//
//
//            //啟動query authsql Data的 thread
//            Thread t_authsql = new Thread(authsql);
//            t_authsql.start();
//            // 等待 authsql thread 完成才繼續作UI更新
//            try {
//                t_authsql.join();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//
//            if (handSuc) {
//
//
//                //3.上班簽到
//                checkin();
////忘記做驗證機制 造成null值一樣可以啟用功能
//                BT001.clearAnimation();
//                BT001.setClickable(false);
//                BT002.setClickable(true);
//                BT003.setClickable(true);
//                BT004.setClickable(true);
//                BT005.setClickable(true);
//                BT006.setClickable(true);
////      getCheckDateTime();//上班簽到用//改用切割的
//                booked.dismiss(); // 關閉DIALOG
//                setupViewComponent();
//
//                //4.設定姓名與開起隱藏功能
//                TVN001.setText(strMyName);
//                Toast.makeText(getApplicationContext(), strMyName + getString(R.string.welcome), Toast.LENGTH_SHORT).show();
//                //設定驗證檔案
//                settings = getSharedPreferences("CheckinData", 0);
//                SharedPreferences.Editor preEdit = settings.edit();
//                preEdit.putString("Admin", nBET001);
//                preEdit.putString("TEMPname", strMyName);
//                preEdit.putInt("CHECK", strMystate);
//                preEdit.commit();
//                authData = BET001.getText().toString();
//                if (strMystate > 50)//int比值
//                {
//                    BTA001.setEnabled(true);
//                    BTA001.setVisibility(View.VISIBLE);
//
//                }
//                BT001.setEnabled(false);
//                BT002.setEnabled(true);
//                BT003.setEnabled(true);
//                BT004.setEnabled(true);
//                BT005.setEnabled(true);
//                BT006.setEnabled(true);
//                CRLL0014.setVisibility(View.INVISIBLE);
//            } else {
//
//                BET001.setText("");
//                BET002.setText("");
//                BET001.setFocusable(true);
//                Toast.makeText(getApplicationContext(), getString(R.string.Alert3), Toast.LENGTH_SHORT).show();
//
//            }
//
//        }
//
//
//        private void checkin() {
//            // TODO Auto-generated method stub
//
//            newUpdate2 = strMyUid + "&d005=" + getDateTime();
//
//            //透過thread回報上班簽到時間
//
//            //更新GPS資訊到SQL內.費時.造成ANR故使用thread執行
//            Thread t_updatecheckin = new Thread(updatecheckin);
//            t_updatecheckin.start();   // 指派臨時工開始工作
//            try {
//                t_updatecheckin.join();
////               Thread.sleep(1000);//線程暫停10秒，單位毫秒
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//
//
//        }
//
//
////		private void getCheckDateTime() {
////			// TODO Auto-generated method stub
////
////		}
//
//
////      private void ForceopenGPS()
////      {
////          // TODO Auto-generated method stub
////
////      }
//
//
//    };
//
//
//    //
//    private Button.OnClickListener cancle = new Button.OnClickListener() {
//        public void onClick(View v) {
//            booked.dismiss(); // 關閉DIALOG
//        }
//    };
//
//
//    //GPS相關程序
//    @Override
////	public void onLocationChanged(Location arg0)
//    public void onLocationChanged(Location staffloc)
//
//
//    {
//        // TODO Auto-generated method stub
//        //取得自己的緯度經度
//        tLat = staffloc.getLatitude();
//        tLng = staffloc.getLongitude();
//
////   Toast.makeText(getApplicationContext(), getString(R.string.Alert), Toast.LENGTH_SHORT).show();
////   String str=getString(R.string.Nowloca)+"\n"; //設定Toast用變數
////   str = str + getString(R.string.lat)+ + staffloc.getLatitude() +"\n"+getString(R.string.lon) +staffloc.getLongitude();//將經緯度設定給變數
////   Toast.makeText(getApplicationContext(), str, Toast.LENGTH_LONG).show();
//        //開啟SQLite取得Loccalreport網址
//        newUpdate = strMyUid + "&d002=" + tLat + "&d003=" + tLng + "&d004=" + getDateTime();//設定update字串
////        DbHelper CoDbHp = new DbHelper(getApplicationContext(), DB_FILE2,    null, 1);
////        coHelp = CoDbHp.getReadableDatabase();// 查詢資料庫，因為是查詢，所以使用唯讀模式 .getWritableDatabase可寫入
////        Cursor coHelpc=null;
////        coHelpc=coHelp.query(true, DB_TABLE21, new String[]{"url"}, null, null, null, null, null,null);
////        if (coHelpc==null) {
////          return;
////       }
////        else {
////            coHelpc.moveToFirst();//查詢完SQLlite會位於最後.要重新移到最前取值
////            Loccalreport=coHelpc.getString(0);
////            Log.d("Loccalreport", Loccalreport);
////       }
////        coHelpc.close();
//        //透過thread回報
//        //更新GPS資訊到SQL內.費時.造成ANR故使用thread執行
//        Thread t_updatelocData = new Thread(updatelocData);
//        t_updatelocData.start();   // 指派臨時工開始工作
//        try {
////      	   Thread.sleep(3000);//線程暫停30秒，單位毫秒
//            t_updatelocData.join();
////             Thread.sleep(10000);//線程暫停10秒，單位毫秒
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//
////   //更新GPS資訊到SQL內.費時.造成ANR故使用thread執行
////   Thread   t_updatelocData=new Thread(updatelocData);
////    t_updatelocData.start();   // 指派臨時工開始工作
////    try {
////        t_updatelocData.join();
////   } catch (InterruptedException e) {
////       e.printStackTrace();
////   }
//    }
//
//
//    @Override
//    public void onProviderDisabled(String arg0) {
//        // TODO Auto-generated method stub
//
//    }
//
//
//    @Override
//    public void onProviderEnabled(String arg0) {
//        // TODO Auto-generated method stub
//        mLocationMgr.requestLocationUpdates(mBestLocationProv, 120000, 60, this);//60秒或1公尺
//    }
//
//
//    @Override
//    public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
//        // TODO Auto-generated method stub
//
//    }
//
//    @Override
//    protected void onStop() {
//        // TODO Auto-generated method stub
//        mLocationMgr.removeUpdates(this);
//        super.onStop();
//    }
//
//    @Override
//    protected void onResume() {
//        // TODO Auto-generated method stub
//        super.onResume();
//        mLocationMgr.requestLocationUpdates(mBestLocationProv, 120000, 60, this);//60秒或1公尺
//    }
//
//
//    //////////////////thread的工作內容  ////////////////////////
////authsql 的實際動作項目內容
//    private Runnable authsql = new Runnable() {
//        public void run() {
////TODO Auto-generated method stub
//            try {
//                auth_from_sqlite();
//                //把整個Auth資料給變數authData//authData是一個final的變數.用於傳遞認證過userid.讓admin可在map中使用
////    Log.d("AllauthData",authData);
//
////handSuc=true;
//            } catch (Exception ee) {
//            }
//        }
//
//        private void auth_from_sqlite() {
//            // TODO Auto-generated method stub
//            Cursor hauthc = null;
//            hauthc = haHelp.rawQuery("select name,state from auth where userid=? and passwd=?", new String[]{nBET001, pBET002});
//
//
//            if (hauthc == null) {
//                return;
//            }
//            int authresult = hauthc.getCount();
////	  String auths = Integer.toString(authresult);
////  Log.d("AAAWWWWWWauths", auths);
//
//            if (authresult == 0) {
//
//                handSuc = false;
//                return;
////	    FunctionFragment.this.finish();
//            } else {
//
//
//                //3.取得name與state
//                hauthc.moveToFirst();//查詢完SQLlite會位於最後.要重新移到最前取值
//                strMyName = hauthc.getString(0);
//
////   	        Log.d("SharedPreferencesSSSSSSS", hauthc.getString(0));
//
////	       Log.d("AAAastrMyName", strMyName);
//                hauthc.moveToFirst();
//                strMystate = Integer.parseInt(hauthc.getString(1));//String轉int
////	        Log.d("AAAAAstrMystate", hauthc.getString(1));
//                hauthc.close();
//                handSuc = true;
//                return;
//            }
//
//        }
//    };
//
//
//    //updatecheckin的實際動作項目內容
//    private Runnable updatecheckin = new Runnable() {
//        public void run() {
////TODO Auto-generated method stub
//            try {
//////SQLite確認
////String[] args={nBET001};
////ContentValues checkinRow = new ContentValues();
////checkinRow.put("userid",nBET001);
////checkinRow.put("checkin",1);
////haHelp.update(DB_TABLE11, checkinRow, "userid=?", args);
//
//
////settings = getSharedPreferences("CheckinData", 0);
////SharedPreferences.Editor preEdit = settings.edit();
////preEdit.putString("CHECK", "1");
////preEdit.commit();
//
////遠端MySQL寫入
//                HttpClient client = new DefaultHttpClient();
//                HttpGet get = new HttpGet("http://" + Loccalreport + "CarcheckTime.php?a001=" + newUpdate2);
//                client.execute(get);
//
//
//            } catch (Exception ee) {
//            }
//        }
//    };
//
//
//    //updatelocData的實際動作項目內容
//    private Runnable updatelocData = new Runnable() {
//        public void run() {
////TODO Auto-generated method stub
//            try {
//                HttpClient client = new DefaultHttpClient();
//
//                HttpGet get = new HttpGet("http://" + Loccalreport + "Caru2.php?a001=" + newUpdate);
//                client.execute(get);
////  reportlocation();
//            } catch (Exception ee) {
//            }
//        }
//    };
//
//
//    /////////////////////////////////////////////////////////////////
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
////移除臨時工的工作
//        if (mThreadHandler != null) {
//            mThreadHandler.removeCallbacks(authsql);
//            mThreadHandler.removeCallbacks(updatecheckin);
//            mThreadHandler.removeCallbacks(updatelocData);
//
//        }
////解聘臨時工 (關閉Thread)
//        if (mThread != null) {
//            mThread.quit();
//        }
//    }
//
//
////protected void ForceopenGPS()
////{
////    // TODO Auto-generated method stub
////
////}


}