package c.min.tseng.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import c.min.tseng.R;
import c.min.tseng.dbfunction.DbHelper;

//public class BillingFragment extends Activity implements SurfaceHolder.Callback {
public class Search extends Activity implements LocationListener {

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
    Intent printerB = new Intent(), function = new Intent(), ocr = new Intent(), inspect = new Intent(), search = new Intent(), setting = new Intent(), adminmap = new Intent(), billing = new Intent();


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
    //存放Spinner選擇後變數
    public String Voucher, Rate, RateC, Models;
    public String[] BilSP001funcas, BilSP002funcas, BilSP010funcas;
    public String[] BilSP001funcast, BilSP002funcast, BilSP010funcast;
    public String[] Vouchert, Ratet, RateCt, Modelst;

    //切割資料用
    private String[] CarNO, BillingTime, OverdueTime; //放置車號年月日時間用
    //承接跨class的值用的變數--跨class有authData.strMyName.Loccalreport
    private String Toll;
    private String updataurl;
    //開單用變數
    //取得當前GPS點位與
    double lngBill, latBill;
    static String NowPT;

    //開單填入資訊
    public static String nBilET0011, nBilET0012, nBilET002, nBilET0021, nBilET0022, nBilET003, nBilET004, nBilET0061, nBilET0062, nBilET0063, nBilSP001, nBilSP002, nBilSP010func, nBilSP0011, nBilSP0012, nBilSP0021, nBilSP0022, nBilSP010func1, nBilSP010func2, nBilTime;


    ////////////////////////

    //撈取資料變數

    private ListView seLV001;
    public String BillSQLiteData = "";     //存放SQLite抓回來的資料
    List<Map<String, String>> mList;
    final ArrayList<HashMap<String, String>> data = new ArrayList();


    private String[] read, read_list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //強制開啟wifi
//        ForceturnOnWif();
        //檢測網路可否使用
//        NetworkAvailable();
        //強制開啟GPS
//        ForceopenGPS();//無效   


        //承接收費員基本資料
//        Log.d("WOWauthDataWOWMap", authData);
//        strMyUid = FunctionFragment.authData;
////        Log.d("WOWauthDataWOWMap2", strMyUid);
////
////        Log.d("BBBBNAME1", strMyName);
//        billname = FunctionFragment.strMyName;
////        Log.d("BBBBName2", billname);

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
        setContentView(R.layout.search);
        //設定SQLite管理者
//        DbHelper BillDbHp = new DbHelper(getApplicationContext(), DB_FILE4,    null, 1);
//      //
//        BillHelp = BillDbHp.getReadableDatabase();//getReadableDatabase僅可查詢


        //
//        HandlerThread mThread = new HandlerThread("name");			//將 HandlerThread 的變數 mThread 實做出來
//		mThread.start();											//啟動這個 HandlerThread
//		mThreadHandler = new Handler(mThread.getLooper());			//將 mThreadHandler 這個 Handler 綁定在  mThread 這個 HandlerThread 線程上
//		mThreadHandler.post(SearchBillData);							//mThreadHandler 指派啟動了 relistData 這個工作
        seLV001 = (ListView) findViewById(R.id.seLV001);


//      //讀取SQLite
        //建立一個BillSQLiteData資料的thread
        Thread t_SearchBillData = new Thread(SearchBillData);
        //開始query資料的thread
        t_SearchBillData.start();
        // 等待 query thread 完成才繼續作UI更新
        try {
            t_SearchBillData.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//
//


//         Thread   t_postdata=new Thread(postdata);
//         //開始query資料的thread
//          t_postdata.start();  
//         // 等待 query thread 完成才繼續作UI更新
//          try {
//              t_postdata.join();
//         } catch (InterruptedException e) {
//             e.printStackTrace();
//         }
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


    }


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
        mLocationMgr.requestLocationUpdates(mBestLocationProv, 60000, 100, this);//60秒或1公尺

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
        mLocationMgr.requestLocationUpdates(mBestLocationProv, 60000, 100, this);//60秒或1公尺
    }


    //////////////////thread的工作內容  ////////////////////////
    //BillSQLiteData的實際動作項目內容
    private Runnable SearchBillData = new Runnable() {
        public void run() {
            // TODO Auto-generated method stub
            DbHelper BillDbHp = new DbHelper(getApplicationContext(), DB_FILE4, null, 1);
            BillHelp = BillDbHp.getReadableDatabase();//getReadableDatabase僅可查詢
            try {

                Cursor Billc = null;
//  Billc = BillHelp.rawQuery("select Sernum2,Ctype,NYMD,NHMS,PlatePickname from"+DB_TABLE41+"where userid=?",new String[]{authData} );
                Billc = BillHelp.query(true, DB_TABLE41, new String[]{"Sernum2", "Ctype", "PlatePick", "NHMS"}, null, null, null, null, null, null);
                int rows_num = Billc.getCount(); //取得資料表列數
                Billc.moveToFirst();//查詢完SQLlite會位於最後.要重新移到最前取值
                String TempSQL;
                StringBuffer SQLiteDataB = new StringBuffer();
                for (int i = 0; i < rows_num; i++) {
                    //把SQLite查詢到的資料給變數TempSQL
                    TempSQL = Billc.getString(0) + "," + Billc.getString(1) + "," + Billc.getString(2) + "," + Billc.getString(3) + "#";
                    //Log.d("WoWTempSQL", TempSQL);
                    //利用StringBuffer連加String
                    SQLiteDataB.append(TempSQL);
                    Billc.moveToNext();   //將指標移至下一筆資料
                }
                //將StringBuffer轉給SQLiteData
                BillSQLiteData = SQLiteDataB.toString();
                read = BillSQLiteData.split("#");
//	Log.d("read", read[0]);
//	mUI_Handler.post(postdata);
                Billc.close();

//更新ListView內資料
                mList = new ArrayList<Map<String, String>>();
                List<Map<String, String>> mList = new ArrayList<Map<String, String>>();
                for (int i = 0; i < read.length; i++) {
                    read_list = read[i].trim().split(",");

//		if (read_list[1].equals("0")) {
//			read_list[1]="汽車";
//		}else {
//			read_list[1]="機車";
//		}
                    Map<String, String> item = new HashMap<String, String>();
                    item.put("txtView", read_list[0]);
                    item.put("txtView1", read_list[1]);
                    item.put("txtView2", read_list[2]);
                    item.put("txtView3", read_list[3].substring(0, 5));
                    mList.add(item);
                }
                SimpleAdapter adapter = new SimpleAdapter(Search.this, mList, R.layout.alist_item, new String[]{"txtView", "txtView1", "txtView2", "txtView3"}, new int[]{R.id.textView, R.id.textView1, R.id.textView2, R.id.textView3});
                seLV001.setAdapter(adapter);
                seLV001.setTextFilterEnabled(true);
            } catch (Exception e) {
                // TODO: handle exception
            }


        }
    };


    private Runnable postdata = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            mList = new ArrayList<Map<String, String>>();
            List<Map<String, String>> mList = new ArrayList<Map<String, String>>();
            for (int i = 0; i < read.length; i++) {
                read_list = read[i].trim().split(",");

                if (read_list[1].equals("0")) {
                    read_list[1] = "汽車";
                } else {
                    read_list[1] = "機車";
                }
                Map<String, String> item = new HashMap<String, String>();
                item.put("txtView", read_list[0]);
                item.put("txtView1", read_list[1]);
                item.put("txtView2", read_list[2]);
                item.put("txtView3", read_list[3].substring(0, 5));
                mList.add(item);
            }
            SimpleAdapter adapter = new SimpleAdapter(Search.this, mList, R.layout.alist_item, new String[]{"txtView", "txtView1", "txtView2", "txtView3"}, new int[]{R.id.textView, R.id.textView1, R.id.textView2, R.id.textView3});
            seLV001.setAdapter(adapter);
            seLV001.setTextFilterEnabled(true);
            // 監聽器


            // mThreadHandler.postDelayed(relistData, 20000);
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
            mThreadHandler.removeCallbacks(SearchBillData);

        }
        //解聘臨時工 (關閉Thread)
        if (mThread != null) {
            mThread.quit();
        }
    }


}
