package c.min.tseng.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import c.min.tseng.R;

public class BillingFragment extends Fragment {
    private final static String TAG = "OCRResultFragment";
    //OCR處理部分
    private static String LANGUAGE = "eng";
//public class BillingFragment extends Activity implements SurfaceHolder.Callback {
//public class BillingFragment extends Activity implements LocationListener {

//    ////////多執行緒-Handler和Thread  ///////////////////////
//    //透過公會找到U成員的經紀人，這樣才能派遣工作? (找到顯示畫面的UI Thread上的Handler)
//    private Handler mUI_Handler = new Handler();
//    private Handler mThreadHandler; ///宣告臨時工的經紀人
//    private HandlerThread mThread; ///宣告臨時工
//    ///////////////////////////////////////////////////////////////////////////////////////
//    //SQLlite處理變數
//    private static final String DB_FILE1 = "auth.db", DB_TABLE11 = "auth", DB_FILE2 = "company.db", DB_TABLE21 = "company", DB_FILE4 = "BillingFragment.db", DB_TABLE41 = "BillingFragment";
//    private SQLiteDatabase haHelp, coHelp, gpsHelp, BillHelp;
//    private DbHelper BillDbHp;
//
//
//    //
//    public SimpleDateFormat sdf;
//
//
//    //手機用變數
//    TelephonyManager Tel;
//    WifiManager mowifi;
//    //個人資料變數
////    private String strMyName;
//    private String strMyUid; // uid
//    private String strMyGrp;
//    private String billname;
//    private String strMyTel; // tel
//    private String strMyEmail;
//    //    private String strMystate;
//    private int strMystate;
//    private String myimei;
//
//    // 引導其他class
//    Intent printerB = new Intent(), fragment_function = new Intent(), fragment_takephoto = new Intent(), inspect = new Intent(), search = new Intent(), setting = new Intent(), adminmap = new Intent(), fragment_billing = new Intent();
//
//
//    public static String[] authMatix;
//
//
//    //location
//    private LocationManager mLocationMgr;
//    private Location Billloca;
//    private String mBestLocationProv;
//    //  private Location staffloc;
//    static double dLat, dLon, tLat, tLng;
//    private String newUpdate = "";
//    private String newUpdate2 = "";
//
//
//    private Context mContext;
//    //存放Spinner選擇後變數
//    public String Voucher, Rate, RateC, Models;
//    public String[] BilSP001funcas, BilSP002funcas, BilSP010funcas;
//    public String[] BilSP001funcast, BilSP002funcast, BilSP010funcast;
//    public String[] Vouchert, Ratet, RateCt, Modelst;
//
//    //切割資料用
//    private String[] CarNO, BillingTime, OverdueTime; //放置車號年月日時間用
//    //承接跨class的值用的變數--跨class有authData.strMyName.Loccalreport
//    private String Toll;
//    private String updataurl;
//    //開單用變數
//    //取得當前GPS點位與
//    double lngBill, latBill;
//    static String NowPT;
//
//    //開單填入資訊
//    public static String nBilET0011, nBilET0012, nBilET002, nBilET0021, nBilET0022, nBilET003, nBilET004, nBilET0061, nBilET0062, nBilET0063, nBilSP001, nBilSP002, nBilSP010func, nBilSP0011, nBilSP0012, nBilSP0021, nBilSP0022, nBilSP010func1, nBilSP010func2, nBilTime;
//
//
//    ////////////////////////
//
//
//    private LinearLayout BilLL001;
//
//    private Spinner BilSP001, BilSP002, BilSP010func;
//
//    private TableRow BilTB001, BilTB002, BilTB003, BilTB004, BilTB005, BilTB006, BilTB007, BilTB0081, BilTB0091;
//
//    private TextView BilTV001, BilTV001N, BilTV002, BilTV002Y, BilTV002M, BilTV002D, BilTV003, BilTV004, BilTV006, BilTV006Y, BilTV006M, BilTV006D, BilTV0071, BilTV0072, BilTV0081, BilTV0082, BilTV0091, BilTV0092, BilTV010, BilTV011, BilTV012;
//
//    private EditText BilET0011, BilET0012, BilET002, BilET0021, BilET0022, BilET003, BilET004, BilET0061, BilET0062, BilET0063;
//
//    private Button BilBT001, BilBT002, BilBT003;
//
//    private static String[] BilSP001funca, BilSP002funca, BilSP010funca;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final Context context = container.getContext();
        final Bundle arguments = getArguments();
        final View child = inflater.inflate(R.layout.fragment_billing, container, false);
        return child;
    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        //強制開啟wifi
////        ForceturnOnWif();
//        //檢測網路可否使用
////        NetworkAvailable();
//        //強制開啟GPS
////        ForceopenGPS();//無效
//
//
//        //承接收費員基本資料
////        Log.d("WOWauthDataWOWMap", authData);
//        strMyUid = Function.authData;
////        Log.d("WOWauthDataWOWMap2", strMyUid);
////
////        Log.d("BBBBNAME1", strMyName);
//        billname = Function.strMyName;
////        Log.d("BBBBName2", billname);
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
//        getDateTime();
//        setContentView(R.layout.fragment_billing);
//        //設定SQLite管理者
//        DbHelper BillDbHp = new DbHelper(getApplicationContext(), DB_FILE4, null, 1);
//
////       // 設定建立 開單table 的指令
////        BillDbHp.sCreateTableCommand = "CREATE TABLE " + DB_TABLE41 + "("+
////                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
////                "PlatePick TEXT," +
////                "userid TEXT," +
////                "name TEXT," +
////                "Btype TEXT," +
////                "Ntype TEXT," +
////                "Ctype TEXT," +
////                "defaucost TEXT," +
////                "RoadSec TEXT," +
////                "SYMD TEXT NOT NULL," +
////                "SHMS TEXT NOT NULL," +
////                "NYMD TEXT NOT NULL," +
////                "NHMS TEXT NOT NULL," +
////                "OYMD TEXT NOT NULL," +
////                "OHMS TEXT NOT NULL," +
////                "Lan TEXT," +
////                "Lng TEXT," +
////                "Pic TEXT," +
////                "Sernum1 TEXT," +
////                "Sernum2 TEXT);";
//
//        //
//        BillHelp = BillDbHp.getWritableDatabase();//getWritableDatabase可寫入
//
//        setupViewComponent();
//
//
//    }
//
//
//    public String getDateTime() {
//        sdf = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
//        String currentDateandTime = sdf.format(new Date(System.currentTimeMillis()));
//        return currentDateandTime;
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
//    private void setupViewComponent() {
//        // TODO Auto-generated method stub
//        BilLL001 = (LinearLayout) findViewById(R.id.TCBF001);
//
//        BilSP001 = (Spinner) findViewById(R.id.BilSP001);
//        BilSP002 = (Spinner) findViewById(R.id.BilSP002);
//        BilSP010func = (Spinner) findViewById(R.id.BilSP010func);
//
//        BilTB001 = (TableRow) findViewById(R.id.BilTB001);
//        BilTB002 = (TableRow) findViewById(R.id.BilTB002);
//        BilTB003 = (TableRow) findViewById(R.id.BilTB003);
//        BilTB004 = (TableRow) findViewById(R.id.BilTB004);
//        BilTB005 = (TableRow) findViewById(R.id.BilTB005);
//        BilTB006 = (TableRow) findViewById(R.id.BilTB006);
//
//
//        BilTV001 = (TextView) findViewById(R.id.BilTV001);
//        BilTV001N = (TextView) findViewById(R.id.BilTV001N);
//        BilTV002 = (TextView) findViewById(R.id.BilTV002);
//        BilTV002Y = (TextView) findViewById(R.id.BilTV002Y);
//        BilTV002M = (TextView) findViewById(R.id.BilTV002M);
//        BilTV002D = (TextView) findViewById(R.id.BilTV002D);
//        BilTV003 = (TextView) findViewById(R.id.BilTV003);
//        BilTV004 = (TextView) findViewById(R.id.BilTV004);
//        BilTV006 = (TextView) findViewById(R.id.BilTV006);
//        BilTV006Y = (TextView) findViewById(R.id.BilTV006Y);
//        BilTV006M = (TextView) findViewById(R.id.BilTV006M);
//        BilTV006D = (TextView) findViewById(R.id.BilTV006D);
//        BilTV010 = (TextView) findViewById(R.id.BilTV010);
//        BilTV011 = (TextView) findViewById(R.id.BilTV011);
//        BilTV012 = (TextView) findViewById(R.id.BilTV012);
//
//        BilET0011 = (EditText) findViewById(R.id.BilET0011);
//        BilET0012 = (EditText) findViewById(R.id.BilET0012);
//        BilET002 = (EditText) findViewById(R.id.BilET002);
//        BilET0021 = (EditText) findViewById(R.id.BilET0021);
//        BilET0022 = (EditText) findViewById(R.id.BilET0022);
//        BilET003 = (EditText) findViewById(R.id.BilET003);
//        BilET004 = (EditText) findViewById(R.id.BilET004);
//        BilET0061 = (EditText) findViewById(R.id.BilET0061);
//        BilET0062 = (EditText) findViewById(R.id.BilET0062);
//        BilET0063 = (EditText) findViewById(R.id.BilET0063);
//        BilBT001 = (Button) findViewById(R.id.BilBT001);
//        BilBT002 = (Button) findViewById(R.id.BilBT002);
//        BilBT003 = (Button) findViewById(R.id.BilBT003);
//
//        BilBT001.setOnClickListener(BilBT001Clk);
//        BilBT002.setOnClickListener(BilBT002Clk);
//        BilBT003.setOnClickListener(BilBT003Clk);
//
////		Spinner資料array的處理.宣告取得array資源
//        BilSP001funca = getResources().getStringArray(R.array.BilSP001func);
//        BilSP002funca = getResources().getStringArray(R.array.BilSP002func);
//        BilSP010funca = getResources().getStringArray(R.array.BilSP010func);
//        //***********************************憑單種類Spinner資料*******************************************//
//        //設定憑單種類Spinner的樣式內容
//        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, R.layout.myspinner);
//        //分割地區字串陣列,長度為6,為0到5,將每個字串的位置[0]的值,也就是 "中區職訓"."台灣大學"等等....存到adapter
//        for (int i = 0; i < BilSP001funca.length; i++) {
//            BilSP001funcas = BilSP001funca[i].split(",");
//            adapter1.add(BilSP001funcas[0]);
//
//
//        }
//        //設定Spinner的樣式內容      費率選擇車種
//        adapter1.setDropDownViewResource(R.layout.myspinner);
//        //將設定好的adapter的內容給予Spinner物件
//        BilSP001.setAdapter(adapter1);
//        //做Spinner的監聽器
//        BilSP001.setOnItemSelectedListener(BilSP001OnItemSelLis);//憑單種類的Listener
//        //*******************************************************************************************//
//
//        //***********************************費率種類Spinner資料*******************************************//
//        //設定費率種類Spinner的樣式內容
//        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, R.layout.myspinner);
//        //分割地區字串陣列,長度為6,為0到5,將每個字串的位置[0]的值,也就是 "中區職訓"."台灣大學"等等....存到adapter
//        for (int i = 0; i < BilSP002funca.length; i++) {
//            BilSP002funcas = BilSP002funca[i].split(",");
//            adapter2.add(BilSP002funcas[0]);
//
//        }
//        //設定Spinner的樣式內容
//        adapter2.setDropDownViewResource(R.layout.myspinner);
//        //將設定好的adapter的內容給予Spinner物件
//        BilSP002.setAdapter(adapter2);
//        //做Spinner的監聽器
//        BilSP002.setOnItemSelectedListener(BilSP002OnItemSelLis);//憑單種類的Listener
//        //*******************************************************************************************//
//
//
//        //***********************************車種Spinner資料*******************************************//
//        //設定車種Spinner的樣式內容
//        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this, R.layout.myspinner);
//        //分割地區字串陣列,長度為6,為0到5,將每個字串的位置[0]的值,也就是 "中區職訓"."台灣大學"等等....存到adapter
//        for (int i = 0; i < BilSP010funca.length; i++) {
//            BilSP010funcas = BilSP010funca[i].split(",");
//            adapter3.add(BilSP010funcas[0]);
//
//        }
//        //設定Spinner的樣式內容
//        adapter3.setDropDownViewResource(R.layout.myspinner);
//        //將設定好的adapter的內容給予Spinner物件
//        BilSP010func.setAdapter(adapter3);
//        //做Spinner的監聽器
//        BilSP010func.setOnItemSelectedListener(BilSP010OnItemSelLis);//憑單種類的Listener
//        //*******************************************************************************************//
//
//
//        //設定開單人員姓名
//        Toll = Function.strMyName;
//        updataurl = Function.Loccalreport;
////        Toast.makeText(getApplicationContext(),"WWWWWWWWWW.."+updataurl, Toast.LENGTH_LONG).show();
//
//        //車號
//
//
//        if (TakePhotoFragment.textResult.equals("")) {
//            BilET0011.setText("");
//        } else {
//            BilET0011.setText(TakePhotoFragment.textResult);
//            BilET0012.setVisibility(View.GONE);
////          CarNO=textResult.split("-");
//////          Toast.makeText(getApplicationContext(),CarNO[0], Toast.LENGTH_LONG).show();
//////          Log.d("CCCCCCCCCCCCCCCCCCCCCCCCCCCC", textResult);
////        BilET0011.setText(CarNO[0]);
////        BilET0012.setText(CarNO[1]);
//
//        }
//
//        //設定開單時間
//        BillingTime = getDateTime().split("-");
//        int i1 = Integer.parseInt(BillingTime[0]) - 1911;
//        String s1 = String.valueOf(i1);
//        BilET002.setText(s1);//EditText設定開單時間
//        BilET0021.setText(BillingTime[1]);
//        BilET0022.setText(BillingTime[2]);
//
//        nBilTime = BillingTime[3];
//
//
//        ///////////逾期時間////////
//        String untildate = BillingTime[0] + "/" + BillingTime[1] + "/" + BillingTime[2] + "/" + BillingTime[3]; //給Calender的時間
//
//        Calendar cal = Calendar.getInstance();
//        try {
//            cal.setTime(sdf.parse(untildate));
//        } catch (java.text.ParseException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        cal.add(Calendar.MONTH, 1);
//        OverdueTime = sdf.format(cal.getTime()).split("-");
//        int i2 = Integer.parseInt(OverdueTime[0]) - 1911;
//        String s2 = String.valueOf(i2);
//        BilET0061.setText(s2);//EditText設定逾期時間
//        BilET0062.setText(OverdueTime[1]);
//        BilET0063.setText(OverdueTime[2]);//EditText設定開單時間
//        //經緯度反解
//        Location staffloca = mLocationMgr.getLastKnownLocation(mBestLocationProv);
//        Geocoder geocoder = new Geocoder(BillingFragment.this, Locale.getDefault());
//        latBill = staffloca.getLatitude();
//        lngBill = staffloca.getLongitude();
//
//        List<Address> ListAddr;
//
//        try {
//            ListAddr = geocoder.getFromLocation(latBill, lngBill, 1);
//            Address MyAddr = ListAddr.get(0);
//
//            String MyAddr_str = MyAddr.getThoroughfare();
//
//            BilET004.setText(MyAddr_str);
//        } catch (Exception e) {
//            // TODO: handle exception
//            Toast.makeText(BillingFragment.this, getString(R.string.Alert4) + getString(R.string.lat) + latBill + getString(R.string.lon) + lngBill, 3000).show();
////			Log.d("location",e.toString());
//        }
//    }
//
//
//    //	監聽按下憑單種類的Spinner
//    private OnItemSelectedListener BilSP001OnItemSelLis = new OnItemSelectedListener() {
//
//        @Override
//        // parent = 事件發生的母體 spinner_items
//        // position = 被選擇的項目index = parent.getSelectedItemPosition()
//        // id = row id，通常給資料庫使用
////		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
//        public void onItemSelected(AdapterView<?> arg0, View BilSP001V, int position, long id) {
//            // TODO Auto-generated method stub
//
//            BilSP001funcast = BilSP001funca[position].split(",");
//            Voucher = BilSP001funcast[1];
//            nBilSP0011 = BilSP001funcast[0].trim();
//            nBilSP0012 = BilSP001funcast[1].trim();
////	      			Toast.makeText(BillingFragment.this, "憑單類型KKKK.."+Voucher, 3000).show();
//
//
//        }
//
//        @Override
//        public void onNothingSelected(AdapterView<?> arg0) {
//            // TODO Auto-generated method stub
//
//        }
//
//
//    };
//
//
//    //	監聽按下費率類型的Spinner
//    private OnItemSelectedListener BilSP002OnItemSelLis = new OnItemSelectedListener() {
//
//        @Override
//        public void onItemSelected(AdapterView<?> arg0, View BilSP002V, int BilSP002i,
//                                   long arg3) {
//            // TODO Auto-generated method stub
//
//            BilSP002funcast = BilSP002funca[BilSP002i].split(",");
//            Rate = BilSP002funcast[1];
//            RateC = BilSP002funcast[2];
//            BilTV012.setText(RateC);
//
//
//            nBilSP002 = BilSP002funcast[0].trim();
//            nBilSP0021 = BilSP002funcast[1].trim();
//            nBilSP0022 = BilSP002funcast[2].trim();
//
////				Toast.makeText(BillingFragment.this, "費率類型.."+Rate+"TTTTT"+RateC, 3000).show();
//
//
//        }
//
//        @Override
//        public void onNothingSelected(AdapterView<?> arg0) {
//            // TODO Auto-generated method stub
//
//        }
//
//
//    };
//
//
//    //	監聽按下車型類型的Spinner
//    private OnItemSelectedListener BilSP010OnItemSelLis = new OnItemSelectedListener() {
//
//        @Override
//        public void onItemSelected(AdapterView<?> arg0, View BilSP010V, int BilSP010i,
//                                   long arg3) {
//            // TODO Auto-generated method stub
//
//            BilSP010funcast = BilSP010funca[BilSP010i].split(",");
//            Models = BilSP010funcast[1];
//            nBilSP010func1 = BilSP010funcast[0].toString().trim();
//            nBilSP010func2 = BilSP010funcast[1].toString().trim();
//
//
////				Toast.makeText(BillingFragment.this, "車型.."+Models, 3000).show();
//
//        }
//
//        @Override
//        public void onNothingSelected(AdapterView<?> arg0) {
//            // TODO Auto-generated method stub
//
//        }
//
//
//    };
//
//
//    private Button.OnClickListener BilBT001Clk = new Button.OnClickListener() {
//
//        @Override
//        public void onClick(View v) {
//            //清除全部資料
//
//            BilET0011.setText("");
//            BilET0012.setText("");
//            BilET002.setText("");
//            BilET0021.setText("");
//            BilET0022.setText("");
//            BilET004.setText("");
//            BilET0061.setText("");
//            BilET0062.setText("");
//            BilET0063.setText("");
//            BilTV012.setText("");
//
//
//        }
//    };
//
//
//    private Button.OnClickListener BilBT002Clk = new Button.OnClickListener() {
//
//        @Override
//        public void onClick(View v) {
//
//            nBilET0011 = BilET0011.getText().toString().trim();//車牌號碼
//
//            nBilET0012 = BilET0012.getText().toString().trim();//車牌號碼
//
//            nBilET002 = BilET002.getText().toString().trim();  //停車年
//            nBilET0021 = BilET0021.getText().toString().trim(); //停車月
//            nBilET0022 = BilET0022.getText().toString().trim(); //停車日
//
//
//            nBilET004 = BilET004.getText().toString().trim(); //路段
//            nBilET0061 = BilET0061.getText().toString().trim(); //逾期年
//            nBilET0062 = BilET0062.getText().toString().trim(); //逾期月
//            nBilET0063 = BilET0063.getText().toString().trim(); //逾期日
//
//
//            if (nBilET0011.isEmpty() || nBilET004.isEmpty()) {
//                Toast.makeText(BillingFragment.this, getString(R.string.Alert5), 3000).show();
//                BilET0011.setFocusable(true);
//                return;
//            }
//
//            //寫入SQLite
//            //建立一個BillSQLiteData資料的thread
//            Thread t_BillSQLiteData = new Thread(BillSQLiteData);
//            //開始query資料的thread
//            t_BillSQLiteData.start();
//            // 等待 query thread 完成才繼續作UI更新
//            try {
//                t_BillSQLiteData.join();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//
//            printerB.setClass(BillingFragment.this, Printer.class);
//            startActivity(printerB);
//            BillingFragment.this.finish();
//
//
//        }
//    };
//
//
//    private Button.OnClickListener BilBT003Clk = new Button.OnClickListener() {
//
//        @Override
//        public void onClick(View v) {
//            //關閉開單返回功能選單
//            fragment_function.setClass(BillingFragment.this, Function.class);
//            startActivity(fragment_function);
//            BillingFragment.this.finish();
//
//        }
//    };
//
//    @Override
//    public void onLocationChanged(Location staffloc) {
//        // TODO Auto-generated method stub
//
//        //取得自己的緯度經度
//        tLat = staffloc.getLatitude();
//        tLng = staffloc.getLongitude();
//
//
//        //透過thread回報
//        //更新GPS資訊到SQL內.費時.造成ANR故使用thread執行
//        Thread t_updatelocData = new Thread(updatelocData);
//        t_updatelocData.start();   // 指派臨時工開始工作
//        try {
//
//            t_updatelocData.join();
//
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//
//    }
//
//    @Override
//    public void onProviderDisabled(String provider) {
//        // TODO Auto-generated method stub
//
//    }
//
//    @Override
//    public void onProviderEnabled(String provider) {
//        // TODO Auto-generated method stub
//        mLocationMgr.requestLocationUpdates(mBestLocationProv, 120000, 60, this);//60秒或1公尺
//
//    }
//
//    @Override
//    public void onStatusChanged(String provider, int status, Bundle extras) {
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
////
////    @Override
////    public void surfaceChanged(SurfaceHolder holder, int format, int width,
////            int height)
////    {
////        // TODO Auto-generated method stub
////
////    }
////
////    @Override
////    public void surfaceCreated(SurfaceHolder holder)
////    {
////        // TODO Auto-generated method stub
////
////    }
////
////    @Override
////    public void surfaceDestroyed(SurfaceHolder holder)
////    {
////        // TODO Auto-generated method stub
////
////    }
//
//
//    //////////////////thread的工作內容  ////////////////////////
//    //BillSQLiteData的實際動作項目內容
//    private Runnable BillSQLiteData = new Runnable() {
//        public void run() {
//            // TODO Auto-generated method stub
//            BillDbHp = new DbHelper(getApplicationContext(), DB_FILE4, null, 1);
//            BillHelp = BillDbHp.getWritableDatabase();// 查詢資料庫，因為是查詢，所以使用唯讀模式 .getWritableDatabase可寫入
//
//            //nBilET0011=BilET0011.getText().toString().trim();//車牌號碼
//
////             nBilET0012=BilET0012.getText().toString().trim();//車牌號碼
////
////             nBilET002=BilET002.getText().toString().trim();  //停車年
////             nBilET0021=BilET0021.getText().toString().trim(); //停車月
////             nBilET0022=BilET0022.getText().toString().trim(); //停車日
////
////
////             nBilET004=BilET004.getText().toString().trim(); //路段
////             nBilET0061=BilET0061.getText().toString().trim(); //逾期年
////             nBilET0062=BilET0062.getText().toString().trim(); //逾期月
////             nBilET0063=BilET0063.getText().toString().trim(); //逾期日
//            //,nBilSP001   單種  ,nBilSP002, 費率型態
//
////nBilSP010func;車種 費率金額
//
//            Cursor Billc = null;
////             Billc = BillHelp.rawQuery("select name,state from auth where userid=? and passwd=?",new String[]{nBET001, pBET002});
////             Billc = BillHelp.rawQuery("select NYMD,NHMS from"+DB_TABLE41+ "where PlatePick=? and RoadSec=?",new String[]{nBilET0011,nBilET004});
//            Billc = BillHelp.rawQuery("select NYMD,NHMS from " + DB_TABLE41 + " where PlatePick=?", new String[]{nBilET0011});
////
////
//            int Billresult = Billc.getCount();
//
//
//            String CarPicpath;
//            CarPicpath = TakePhotoFragment.OriginalPhoto;
//            //暫時用亂數來產生開單單號條碼
//            int i = 0;
//            i = (int) (Math.random() * 100000) + 1;
//            int k = 0;
//            k = (int) (Math.random() * 400000) + 1;
//            int l = 0;
//            l = (int) (Math.random() * (30000 - 20 + 1)) + 20;
//            //檢查車牌是否存在(可能要改成車牌與路段)
//            //不存在全部資料新增
//            if (Billresult == 0) {
//
//                ContentValues newRow = new ContentValues();
////           newRow.put("PlatePick",nBilET0011+"-"+nBilET0012);
//                newRow.put("PlatePick", nBilET0011);
//                Log.d("PlatePick", nBilET0011);
//
//                newRow.put("userid", strMyUid);
////           Log.d("userid", strMyUid);
//
//                newRow.put("name", billname);
////           Log.d("name", billname);
//
//                newRow.put("Btype", nBilSP0011);
////           Log.d("Btype", nBilSP0011);
//
//                newRow.put("Ntype", nBilSP002);
////           Log.d("Ntype", nBilSP002);
//
//                newRow.put("Ctype", nBilSP010func1);
////           Log.d("Ctype", nBilSP010func1);
//
//                newRow.put("defaucost", nBilSP0022);
////           Log.d("defaucost", nBilSP0022);
//
//                newRow.put("SYMD", nBilET002 + "-" + nBilET0021 + "-" + nBilET0022);
//                newRow.put("SHMS", BillingTime[3]);
//                newRow.put("NYMD", nBilET002 + "-" + nBilET0021 + "-" + nBilET0022);
//                newRow.put("NHMS", BillingTime[3]);
//                newRow.put("OYMD", OverdueTime[0] + "-" + OverdueTime[1] + "-" + OverdueTime[2]);
//                newRow.put("OHMS", BillingTime[3]);
//                newRow.put("RoadSec", nBilET004);
//                newRow.put("Lan", latBill);
//                newRow.put("Lng", lngBill);
//                newRow.put("Pic", CarPicpath);
//                newRow.put("Sernum1", nBilET002 + "-" + nBilET0021 + "-" + nBilET0022 + i);
//                newRow.put("Sernum2", k + l);
//                BillHelp.insert(DB_TABLE41, null, newRow);
////
//            } else {
////           //車牌存在更新下次時間資料
//                //存在.則更新時間
//
////             + "ID INTEGER PRIMARY KEY AUTOINCREMENT,"
////             + "PlatePick VARCHAR NOT NULL,"
////             + "userid VARCHAR NOT NULL,"
////             + "name VARCHAR,"
////             + "type VARCHAR,"
////             + "defaucost VARCHAR"
////             + "SYMD DATE NOT NULL,"
////             + "SHMS TIME NOT NULL,"
////             + "NYMD DATE NOT NULL,"
////             + "NHMS TIME NOT NULL,"
////             + "RoadSec VARCHAR NOT NULL,"
////             + "Lan VARCHAR NOT NULL,"
////             + "Lng VARCHAR NOT NULL,"
////             + "PIC VARCHAR,"
////             + "Sernum1 VARCHAR,"
////             + "Sernum2 VARCHAR);";
//                String[] args = {nBilET0011};
//                ContentValues newRow = new ContentValues();
////            	  newRow.put("PlatePick",nBilET0011);
//                newRow.put("NYMD", nBilET002 + "-" + nBilET0021 + "-" + nBilET0022);
//                newRow.put("NHMS", BillingTime[3]);
//                BillHelp.update(DB_TABLE41, newRow, "PlatePick=?", args);
//
//            }
//
//
//        }
//    };
//
//    //updatelocData的實際動作項目內容
//    private Runnable updatelocData = new Runnable() {
//        public void run() {
//            //TODO Auto-generated method stub
//            try {
//                HttpClient client = new DefaultHttpClient();
//
//                HttpGet get = new HttpGet("http://" + Function.Loccalreport + "Caru2.php?a001=" + newUpdate);
//                client.execute(get);
//                //  reportlocation();
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
//        //移除臨時工的工作
//        if (mThreadHandler != null) {
//
//            mThreadHandler.removeCallbacks(updatelocData);
//            mThreadHandler.removeCallbacks(BillSQLiteData);
//
//        }
//        //解聘臨時工 (關閉Thread)
//        if (mThread != null) {
//            mThread.quit();
//        }
//    }


}
