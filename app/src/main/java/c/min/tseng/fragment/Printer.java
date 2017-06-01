package c.min.tseng.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import c.min.tseng.R;
import c.min.tseng.dbfunction.DbHelper;

//開單填入資訊

//public class BillingFragment extends Activity implements SurfaceHolder.Callback {
public class Printer extends Activity implements LocationListener {

    ////////多執行緒-Handler和Thread  ///////////////////////
    //透過公會找到U成員的經紀人，這樣才能派遣工作? (找到顯示畫面的UI Thread上的Handler)    
    private Handler mUI_Handler = new Handler();
    private Handler mThreadHandler; ///宣告臨時工的經紀人
    private HandlerThread mThread; ///宣告臨時工
    ///////////////////////////////////////////////////////////////////////////////////////
    //SQLlite處理變數
    private static final String DB_FILE1 = "auth.db", DB_TABLE11 = "auth", DB_FILE2 = "company.db", DB_TABLE21 = "company", DB_FILE4 = "BillingFragment.db", DB_TABLE41 = "BillingFragment";
    private SQLiteDatabase haHelp, coHelp, gpsHelp, BillHelp;
    private DbHelper BillDbHp;


    //
    public SimpleDateFormat sdf;


    //手機用變數
    TelephonyManager Tel;
    WifiManager mowifi;
    //個人資料變數
//    private String strMyName;    
    private String strMyUid; // uid
    private String strMyGrp;
    private String billname;
    private String strMyTel; // tel
    private String strMyEmail;
    //    private String strMystate;
    private int strMystate;
    private String myimei;

    // 引導其他class
    Intent function = new Intent(), ocr = new Intent(), inspect = new Intent(), search = new Intent(), setting = new Intent(), adminmap = new Intent(), billing = new Intent(), printer = new Intent();


    public static String[] authMatix;


    //location
    private LocationManager mLocationMgr;
    private Location Billloca;
    private String mBestLocationProv;
    //  private Location staffloc;
    static double dLat, dLon, tLat, tLng;
    private String newUpdate = "";
    private String newUpdate2 = "";


    private Context mContext;
//  //存放Spinner選擇後變數
//  public String Voucher,Rate,RateC,Models;
//  public String[] BilSP001funcas,BilSP002funcas,BilSP010funcas;
//  public String[] BilSP001funcast,BilSP002funcast,BilSP010funcast;
//  public String[] Vouchert,Ratet,RateCt,Modelst;

    //切割資料用
    private String[] CarNO, BillingTime, OverdueTim; //放置車號年月日時間用
    //承接跨class的值用的變數--跨class有authData.strMyName.Loccalreport
    private String PToll;
    private String updataurl;
    //開單用變數
    //取得當前GPS點位與
    double lngBill, latBill;
    static String NowPT;


    ////////////////////////
    private LinearLayout PriLL001;

    // private Spinner PriSP001,PriSP002,PriSP010func;
    private TextView PriSP001, PriSP002, PriSP010func;

    private TableRow PriTB001, PriTB002, PriTB003, PriTB004, PriTB005, PriTB006, PriTB007, PriTB0081, PriTB0091;

    private TextView PriTV001, PriTV001N, PriTV002, PriTV002Y, PriTV002M, PriTV002D, PriTV003, PriTV004, PriTV006, PriTV006Y, PriTV006M, PriTV006D, PriTV0071, PriTV0072, PriTV0081, PriTV0082, PriTV0091, PriTV0092, PriTV010, PriTV011, PriTV012;

    // private EditText PriET0011,PriET0012,PriET002,PriET0021,PriET0022,PriET003,PriET004,PriET0061,PriET0062,PriET0063;
    private TextView PriET0011, PriET0012, PriET002, PriET0021, PriET0022, PriET003, PriET004, PriET0061, PriET0062, PriET0063;

    private Button PriBT001, PriBT002, PriBT003;

    private TextView PriTV00710, PriTV00711, PriTV00712, PriTV00810, PriTV00811, PriTV00812, PriTV00910, PriTV00911, PriTV00912, PriTV00720, PriTV00721, PriTV00722, PriTV00820, PriTV00821, PriTV00822, PriTV00920, PriTV00921, PriTV00922;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //強制開啟wifi
//        ForceturnOnWif();
        //檢測網路可否使用
//        NetworkAvailable();
        //強制開啟GPS
//        ForceopenGPS();//無效   

//        Log.d("WOWauthDataWOWMap", Loccalreport);
        //承接收費員基本資料
////        Log.d("WOWauthDataWOWMap", authData);
//        strMyUid = FunctionFragment.authData;
////        Log.d("WOWauthDataWOWMap2", strMyUid);
////
////        Log.d("BBBBNAME1", strMyName);
//        billname = FunctionFragment.strMyName;
//        Log.d("BBBBName2", billname);

        //取得位置
        mLocationMgr = (LocationManager) getSystemService(LOCATION_SERVICE);
        //實例化一個Criteria
        Criteria staffreport = new Criteria();
        //獲得最好的定位效果
        staffreport.setAccuracy(Criteria.ACCURACY_FINE);
        staffreport.setAltitudeRequired(false);
        staffreport.setBearingRequired(false);
        staffreport.setCostAllowed(false);
        //使用省電模式
        staffreport.setPowerRequirement(Criteria.POWER_LOW);

        //獲得能提供當前位置的提供者
        mBestLocationProv = mLocationMgr.getBestProvider(staffreport, true);
        Location staffloca = mLocationMgr.getLastKnownLocation(mBestLocationProv);
        getDateTime();
        setContentView(R.layout.printer);
        //設定SQLite管理者
        DbHelper BillDbHp = new DbHelper(getApplicationContext(), DB_FILE4, null, 1);

        BillHelp = BillDbHp.getWritableDatabase();//getWritableDatabase可寫入

        //////


        setupViewComponent();


    }


    public String getDateTime() {
        sdf = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
        String currentDateandTime = sdf.format(new Date(System.currentTimeMillis()));
        return currentDateandTime;
    }


    private void ForceturnOnWif() {
        // TODO Auto-generated method stub
        if (!mowifi.isWifiEnabled()) {
            mowifi.setWifiEnabled(true);
        }

    }

    private void ForceopenGPS() {
        // TODO Auto-generated method stub
//        final Intent GPSIntent = new Intent("android.location.GPS_ENABLED_CHANGE");
        final Intent GPSIntent = new Intent();
        GPSIntent.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
        GPSIntent.addCategory(Intent.CATEGORY_ALTERNATIVE);
        GPSIntent.setData(Uri.parse("3"));
//        GPSIntent.putExtra("enabled", true);
//        this.mContext.sendBroadcast(GPSIntent);
        sendBroadcast(GPSIntent);

//        String GPSprovider = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
//        if(!GPSprovider.contains("gps")){ //if gps is disabled
//        final Intent poke = new Intent();
//        poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
//        poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
//        poke.setData(Uri.parse("3"));
//        this.mContext.sendBroadcast(poke);

    }

    private void setupViewComponent() {
        // TODO Auto-generated method stub

        //
//        PriLL001 = (LinearLayout) findViewById(R.id.TCBF001);
        PriTB001 = (TableRow) findViewById(R.id.PriTB001);
        PriTB002 = (TableRow) findViewById(R.id.PriTB002);
        PriTB003 = (TableRow) findViewById(R.id.PriTB003);
        PriTB004 = (TableRow) findViewById(R.id.PriTB004);
        PriTB005 = (TableRow) findViewById(R.id.PriTB005);
        PriTB006 = (TableRow) findViewById(R.id.PriTB006);


        PriSP001 = (TextView) findViewById(R.id.PriSP001);
        PriSP002 = (TextView) findViewById(R.id.PriSP002);
        PriSP010func = (TextView) findViewById(R.id.PriSP010func);


        PriTV001 = (TextView) findViewById(R.id.PriTV001);
        PriTV001N = (TextView) findViewById(R.id.PriTV001N);
        PriTV002 = (TextView) findViewById(R.id.PriTV002);
        PriTV002Y = (TextView) findViewById(R.id.PriTV002Y);
        PriTV002M = (TextView) findViewById(R.id.PriTV002M);
        PriTV002D = (TextView) findViewById(R.id.PriTV002D);
        PriTV003 = (TextView) findViewById(R.id.PriTV003);
        PriTV004 = (TextView) findViewById(R.id.PriTV004);
        PriTV006 = (TextView) findViewById(R.id.PriTV006);
        PriTV006Y = (TextView) findViewById(R.id.PriTV006Y);
        PriTV006M = (TextView) findViewById(R.id.PriTV006M);
        PriTV006D = (TextView) findViewById(R.id.PriTV006D);
        PriTV010 = (TextView) findViewById(R.id.PriTV010);
        PriTV011 = (TextView) findViewById(R.id.PriTV011);
        PriTV012 = (TextView) findViewById(R.id.PriTV012);

        PriET0011 = (TextView) findViewById(R.id.PriET0011);
        PriET0012 = (TextView) findViewById(R.id.PriET0012);
        PriET002 = (TextView) findViewById(R.id.PriET002);
        PriET0021 = (TextView) findViewById(R.id.PriET0021);
        PriET0022 = (TextView) findViewById(R.id.PriET0022);
        PriET003 = (TextView) findViewById(R.id.PriET003);
        PriET004 = (TextView) findViewById(R.id.PriET004);
        PriET0061 = (TextView) findViewById(R.id.PriET0061);
        PriET0062 = (TextView) findViewById(R.id.PriET0062);
        PriET0063 = (TextView) findViewById(R.id.PriET0063);

        //這邊改自動生成
        PriTV00710 = (TextView) findViewById(R.id.PriTV00710);
        PriTV00711 = (TextView) findViewById(R.id.PriTV00711);
        PriTV00712 = (TextView) findViewById(R.id.PriTV00712);
        PriTV00810 = (TextView) findViewById(R.id.PriTV00810);
        PriTV00811 = (TextView) findViewById(R.id.PriTV00811);
        PriTV00812 = (TextView) findViewById(R.id.PriTV00812);
        PriTV00910 = (TextView) findViewById(R.id.PriTV00910);
        PriTV00911 = (TextView) findViewById(R.id.PriTV00911);
        PriTV00912 = (TextView) findViewById(R.id.PriTV00912);
        PriTV00720 = (TextView) findViewById(R.id.PriTV00720);
        PriTV00721 = (TextView) findViewById(R.id.PriTV00721);
        PriTV00722 = (TextView) findViewById(R.id.PriTV00722);
        PriTV00820 = (TextView) findViewById(R.id.PriTV00820);
        PriTV00821 = (TextView) findViewById(R.id.PriTV00821);
        PriTV00822 = (TextView) findViewById(R.id.PriTV00822);
        PriTV00920 = (TextView) findViewById(R.id.PriTV00920);
        PriTV00921 = (TextView) findViewById(R.id.PriTV00921);
        PriTV00922 = (TextView) findViewById(R.id.PriTV00922);


        PriBT001 = (Button) findViewById(R.id.PriBT001);
        PriBT002 = (Button) findViewById(R.id.PriBT002);
        PriBT003 = (Button) findViewById(R.id.PriBT003);

        PriBT001.setOnClickListener(PriBT001Clk);
        PriBT002.setOnClickListener(PriBT002Clk);
        PriBT003.setOnClickListener(PriBT003Clk);

//
//        //設定開單人員姓名
//        PToll = FunctionFragment.strMyName;
//        updataurl = FunctionFragment.Loccalreport;
//
//        //單號種類
//        PriSP001.setText(BillingFragment.nBilSP0011);
//        //費率類別
//        PriSP002.setText(BillingFragment.nBilSP002);
//
//        //車牌
//        PriET0011.setText(BillingFragment.nBilET0011);
////        PriET0012.setText(text);
//
//        //設定開單時間
//        PriET002.setText(BillingFragment.nBilET002);
//        PriET0021.setText(BillingFragment.nBilET0021);
//        PriET0022.setText(BillingFragment.nBilET0022);
//        //設定路段
//        PriET004.setText(BillingFragment.nBilET004);
//        //設定車種
//        PriSP010func.setText(BillingFragment.nBilSP010func1);
//        //設定基本費率
//        PriTV012.setText(BillingFragment.nBilSP0022);
//        //設定繳費截止逾期日期
//
/////////////逾期時間////////
//        String untildate = BillingFragment.nBilET002 + "/" + BillingFragment.nBilET0021 + "/" + BillingFragment.nBilET0022 + "/" + BillingFragment.nBilTime; //給Calender的時間

        Calendar cal = Calendar.getInstance();
        Calendar timeadd1 = Calendar.getInstance();
        Calendar timeadd2 = Calendar.getInstance();
        Calendar timeadd3 = Calendar.getInstance();
        Calendar timeadd4 = Calendar.getInstance();
        Calendar timeadd5 = Calendar.getInstance();
        Calendar timeadd6 = Calendar.getInstance();
        Calendar timeadd7 = Calendar.getInstance();
        Calendar timeadd8 = Calendar.getInstance();
        Calendar timeadd9 = Calendar.getInstance();
        Calendar timeadd10 = Calendar.getInstance();
        Calendar timeadd11 = Calendar.getInstance();
        Calendar timeadd12 = Calendar.getInstance();
        Calendar timeadd13 = Calendar.getInstance();
        Calendar timeadd14 = Calendar.getInstance();
        Calendar timeadd15 = Calendar.getInstance();
        Calendar timeadd16 = Calendar.getInstance();
        Calendar timeadd17 = Calendar.getInstance();
        Calendar timeadd18 = Calendar.getInstance();
        Calendar timeadd19 = Calendar.getInstance();
        Calendar timeadd20 = Calendar.getInstance();
        Calendar timeadd21 = Calendar.getInstance();
        Calendar timeadd22 = Calendar.getInstance();
        Calendar timeadd23 = Calendar.getInstance();
//        Calendar timeadd24 = Calendar.getInstance();
//        try {
//            cal.setTime(sdf.parse(untildate));
//        } catch (java.text.ParseException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        //加一個月
//        cal.add(Calendar.MONTH, 1);
//        //加小時
        timeadd1.add(Calendar.HOUR, 1);
        timeadd2.add(Calendar.HOUR, 2);
        timeadd3.add(Calendar.HOUR, 3);
        timeadd4.add(Calendar.HOUR, 4);
        timeadd5.add(Calendar.HOUR, 5);
        timeadd6.add(Calendar.HOUR, 6);
        timeadd7.add(Calendar.HOUR, 7);
        timeadd8.add(Calendar.HOUR, 8);
        timeadd9.add(Calendar.HOUR, 9);
        timeadd10.add(Calendar.HOUR, 10);
        timeadd11.add(Calendar.HOUR, 11);
        timeadd12.add(Calendar.HOUR, 12);
        timeadd13.add(Calendar.HOUR, 13);
        timeadd14.add(Calendar.HOUR, 14);
        timeadd15.add(Calendar.HOUR, 15);
        timeadd16.add(Calendar.HOUR, 16);
        timeadd17.add(Calendar.HOUR, 17);
        timeadd18.add(Calendar.HOUR, 18);
        timeadd19.add(Calendar.HOUR, 19);
        timeadd20.add(Calendar.HOUR, 20);
        timeadd21.add(Calendar.HOUR, 21);
        timeadd22.add(Calendar.HOUR, 22);
        timeadd23.add(Calendar.HOUR, 23);
//        timeadd24.add(Calendar.HOUR, 24);


        OverdueTim = sdf.format(cal.getTime()).split("-");

        String[] NextTime1, NextTime2, NextTime3, NextTime4;
//      
        NextTime1 = sdf.format(timeadd1.getTime()).split("-");
        NextTime2 = sdf.format(timeadd2.getTime()).split("-");
        NextTime3 = sdf.format(timeadd3.getTime()).split("-");
        NextTime4 = sdf.format(timeadd4.getTime()).split("-");

//      int i2 = Integer.parseInt(OverdueTim[0])-1911;
//      String s2 = String.valueOf(i2);


////        PriET0061.setText(s2);
//        PriET0061.setText(String.valueOf(Integer.parseInt(OverdueTim[0]) - 1911));
//        PriET0062.setText(OverdueTim[1]);
//        PriET0063.setText(OverdueTim[2]);
//        //設定費率加總
//        PriTV00710.setText(BillingFragment.nBilSP0022);
//
//        int i = Integer.parseInt(BillingFragment.nBilSP0022);
////        String s = String.valueOf(i);
//
//        PriTV00711.setText(String.valueOf(i * 2));
//        PriTV00712.setText(String.valueOf(i * 3));
//        //設定停車時間加總
//
//        PriTV00810.setText(BillingFragment.nBilTime);
//        PriTV00811.setText(NextTime1[3]);
//        PriTV00812.setText(NextTime2[3]);
////        PriTV00813.setText(NextTime3[3]);
//        
        //設定開單人
        PriTV00910.setText(PToll);
        PriTV00920.setText(PToll);


    }


    private Button.OnClickListener PriBT001Clk = new Button.OnClickListener() {

        @Override
        public void onClick(View v) {
//   列印

            Toast.makeText(getApplicationContext(), getString(R.string.PriTemp1), Toast.LENGTH_LONG).show();
            function.setClass(Printer.this, FunctionFragment.class);
            startActivity(function);
            Printer.this.finish();

        }
    };


    private Button.OnClickListener PriBT002Clk = new Button.OnClickListener() {

        @Override
        public void onClick(View v) {

            //預留隱藏


        }
    };


    private Button.OnClickListener PriBT003Clk = new Button.OnClickListener() {

        @Override
        public void onClick(View v) {
            //更新SQLite

            function.setClass(Printer.this, FunctionFragment.class);
            startActivity(function);
            Printer.this.finish();
        }
    };

    @Override
    public void onLocationChanged(Location staffloc) {
        // TODO Auto-generated method stub

        //取得自己的緯度經度
        tLat = staffloc.getLatitude();
        tLng = staffloc.getLongitude();


        //透過thread回報
        //更新GPS資訊到SQL內.費時.造成ANR故使用thread執行
        Thread t_updatelocData = new Thread(updatelocData);
        t_updatelocData.start();   // 指派臨時工開始工作
        try {

            t_updatelocData.join();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub
        mLocationMgr.requestLocationUpdates(mBestLocationProv, 120000, 60, this);//60秒或1公尺

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        mLocationMgr.removeUpdates(this);
        super.onStop();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        mLocationMgr.requestLocationUpdates(mBestLocationProv, 120000, 60, this);//60秒或1公尺
    }


//
//    @Override
//    public void surfaceChanged(SurfaceHolder holder, int format, int width,
//            int height)
//    {
//        // TODO Auto-generated method stub
//        
//    }
//
//    @Override
//    public void surfaceCreated(SurfaceHolder holder)
//    {
//        // TODO Auto-generated method stub
//        
//    }
//
//    @Override
//    public void surfaceDestroyed(SurfaceHolder holder)
//    {
//        // TODO Auto-generated method stub
//        
//    }


    //////////////////thread的工作內容  ////////////////////////
    //BillSQLiteData的實際動作項目內容
    private Runnable BillSQLiteData = new Runnable() {
        public void run() {
            // TODO Auto-generated method stub
            BillDbHp = new DbHelper(getApplicationContext(), DB_FILE4, null, 1);
            BillHelp = BillDbHp.getWritableDatabase();// 查詢資料庫，因為是查詢，所以使用唯讀模式 .getWritableDatabase可寫入

            //nBilET0011=BilET0011.getText().toString().trim();//車牌號碼

//             nBilET0012=BilET0012.getText().toString().trim();//車牌號碼
//             
//             nBilET002=BilET002.getText().toString().trim();  //停車年
//             nBilET0021=BilET0021.getText().toString().trim(); //停車月
//             nBilET0022=BilET0022.getText().toString().trim(); //停車日
//     
//            
//             nBilET004=BilET004.getText().toString().trim(); //路段
//             nBilET0061=BilET0061.getText().toString().trim(); //逾期年
//             nBilET0062=BilET0062.getText().toString().trim(); //逾期月
//             nBilET0063=BilET0063.getText().toString().trim(); //逾期日
            //,nBilSP001   單種  ,nBilSP002, 費率型態

//nBilSP010func;車種 費率金額

//             Cursor Billc=null;
////             Billc = BillHelp.rawQuery("select name,state from auth where userid=? and passwd=?",new String[]{nBET001, pBET002});
//             Billc = BillHelp.rawQuery("select name,state from auth where userid=? and passwd=?",new String[]{nBilET0011});
//
//             
//            int Billresult = Billc.getCount();
//             String auths = Integer.toString(authresult);
            //  Log.d("AAAWWWWWWauths", auths);

            String CarPicpath;
//            CarPicpath = TakePhotoFragment.OriginalPhoto;
            //暫時用亂數來產生開單單號條碼
            int i = 0;
            i = (int) (Math.random() * 100000) + 1;
            int k = 0;
            k = (int) (Math.random() * 400000) + 1;
            int l = 0;
            l = (int) (Math.random() * (30000 - 20 + 1)) + 20;
            //檢查車牌是否存在(可能要改成車牌與路段)
            //不存在全部資料新增
//             if(Billresult==0){

//             ContentValues newRow=new ContentValues();
////           newRow.put("PlatePick",nBilET0011+"-"+nBilET0012);
//           newRow.put("PlatePick",nBilET0011);
//            Log.d("PlatePick", nBilET0011);
//           
//           newRow.put("userid",strMyUid);
////           Log.d("userid", strMyUid);
//           
//           newRow.put("name",billname);
////           Log.d("name", billname);
//           
//           newRow.put("Btype",nBilSP0011);
////           Log.d("Btype", nBilSP0011);
//           
//           newRow.put("Ntype",nBilSP002);
////           Log.d("Ntype", nBilSP002);
//           
//           newRow.put("Ctype",nBilSP010func1);
////           Log.d("Ctype", nBilSP010func1);
//           
//           newRow.put("defaucost",nBilSP0022);
////           Log.d("defaucost", nBilSP0022);
//           
//           newRow.put("SYMD",nBilET002+"-"+nBilET0021+"-"+nBilET0022);
//           newRow.put("SHMS",BillingTime[3]);
//           newRow.put("NYMD",nBilET002+"-"+nBilET0021+"-"+nBilET0022);
//           newRow.put("NHMS",BillingTime[3]);
//           newRow.put("OYMD",OverdueTime[0]+"-"+OverdueTime[1]+"-"+OverdueTime[2]);
//           newRow.put("OHMS", BillingTime[3]);
//           newRow.put("RoadSec",nBilET004);
//           newRow.put("Lan", latBill);
//           newRow.put("Lng",lngBill);
//           newRow.put("Pic",CarPicpath);
//           newRow.put("Sernum1", nBilET002+"-"+nBilET0021+"-"+nBilET0022+i);
//           newRow.put("Sernum2", k+l);
//           BillHelp.insert(DB_TABLE41, null, newRow);
//                 
//                       }
//              else {
//           //車牌存在更新下次時間資料
            //存在.則更新時間

//             + "ID INTEGER PRIMARY KEY AUTOINCREMENT,"
//             + "PlatePick VARCHAR NOT NULL,"
//             + "userid VARCHAR NOT NULL,"
//             + "name VARCHAR,"
//             + "type VARCHAR,"
//             + "defaucost VARCHAR"
//             + "SYMD DATE NOT NULL," 
//             + "SHMS TIME NOT NULL,"
//             + "NYMD DATE NOT NULL," 
//             + "NHMS TIME NOT NULL,"
//             + "RoadSec VARCHAR NOT NULL,"
//             + "Lan VARCHAR NOT NULL,"
//             + "Lng VARCHAR NOT NULL,"
//             + "PIC VARCHAR,"
//             + "Sernum1 VARCHAR,"
//             + "Sernum2 VARCHAR);"; 


        }
    };

    //updatelocData的實際動作項目內容
    private Runnable updatelocData = new Runnable() {
        public void run() {
            //TODO Auto-generated method stub
            try {
                HttpClient client = new DefaultHttpClient();

//                HttpGet get = new HttpGet("http://" + FunctionFragment.Loccalreport + "Caru2.php?a001=" + newUpdate);
//                client.execute(get);
                //  reportlocation();
            } catch (Exception ee) {
            }
        }
    };


    /////////////////////////////////////////////////////////////////
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //移除臨時工的工作
        if (mThreadHandler != null) {

            mThreadHandler.removeCallbacks(updatelocData);
            mThreadHandler.removeCallbacks(BillSQLiteData);

        }
        //解聘臨時工 (關閉Thread)
        if (mThread != null) {
            mThread.quit();
        }
    }


}
