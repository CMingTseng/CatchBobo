package c.min.tseng.fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import c.min.tseng.R;
import c.min.tseng.xmpp.login;


public class Map extends Activity implements LocationListener {

    ////////多執行緒-Handler和Thread  ///////////////////////
    //透過公會找到U成員的經紀人，這樣才能派遣工作  (找到顯示畫面的UI Thread上的Handler)    
    private Handler mUI_Handler = new Handler();
    private Handler mThreadHandler; ///宣告臨時工的經紀人
    private HandlerThread mThread; ///宣告臨時工
    ///////////////////////////////////////////////////////////////////////////////////////     


    //SQL資料處理變數
    public static String SQLPHP;     //存放網頁抓回來的資料
    public static String pageData = "";     //存放網頁抓回來的資料
    public static int nId = 0;     //資料表的 ID
    private Spinner spInfo;//選擇成員列表
    private String strToken1 = "\n"; //切割每筆資料
    private String strToken2 = ","; //切割每欄個人資料
    private static String[] mArea; //用於資料轉陣列存放
    private String[] sLocation;//暫時存放由陣列整理過用的成員陣列
    private String[] personal;//存放成員資料陣列
    //SQLlite處理變數
    private static final String DB_FILE1 = "auth.db", DB_TABLE11 = "auth", DB_FILE2 = "company.db", DB_TABLE21 = "company", DB_FILE3 = "Locus.db", DB_TABLE31 = "Locus";
    private SQLiteDatabase haHelp, coHelp, gpsHelp;
    public static String GPSlist;     //公司url
    public static String SQLiteData = "";     //存放SQLite抓回來的資料
    public static String[] SQLiteDataA;     //存放SQLite抓回來的資料
    public static boolean SQLiteSuc = false;//SQLite同步MySQL用
    private static String[] mPerson;   //存放由MySQL回來的每筆資料的陣列

    //Map用變數
    private GoogleMap map;
    private LocationManager lm;
    private Location loc;
    static LatLng VGPS;
    Address EndAddress, StartAddress;//導航用的地址
    static String STARTP, ENDP;


    //location
    private LocationManager mLocationMgr;
    private String mBestLocationProv;

    static double dLat, dLon, tdLat, tdLon;
    private String newUpdate = "";

    private Marker vgps[];

    //Telephone功能用

    TelephonyManager Tel;
    //個人資料變數


    //    private String strMyName;
    private String strMyUid; // uid
    private String strMyGrp;
    private String strMyTel; // uid
    private String strMyEmail;
    private String myimei;

    //成員資料變數
    private String strtel; // uid
    private String stremail;

    //承接跨class的值用的變數--跨class有authData.strMyName.Loccalreport
    private String Toll;
    private String updataurl;

    //測試功能.透過Line發送訊息
    static final int REQUEST_ACTION_PICK = 1;
    public static final String PACKAGE_NAME = "jp.naver.line.android";
    public static final String CLASS_NAME = "jp.naver.line.android.activity.selectchat.SelectChatActivity";
    private List<ApplicationInfo> m_appList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);

//        //取得跨class資料
//        updataurl = FunctionFragment.Loccalreport;
//
//        //取得本機資料 個人資料定義值
//        TelephonyManager Tel = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
//        WifiManager wifiinfo = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
//
//
////        strMyGrp=getString(R.string.gro2);
//
////        strMyTel=getString(R.string.mytel);
//
//        myimei = Tel.getDeviceId();
//
//        WifiInfo mymacadd = wifiinfo.getConnectionInfo();
//
//
////        Log.d("WOWauthDataWOWMap", authData);
//        strMyUid = FunctionFragment.authData;
////        Log.d("WOWauthDataWOWMap2", strMyUid);
//
////        //先取得公司url好回報與抓取資料
////        DbHelper CoDbHp = new DbHelper(getApplicationContext(), DB_FILE2,    null, 1);
////        coHelp = CoDbHp.getReadableDatabase();// 查詢資料庫，因為是查詢，所以使用唯讀模式 .getWritableDatabase可寫入
////        Cursor coHelpc=null;
////        coHelpc=coHelp.query(true, DB_TABLE21, new String[]{"url"}, null, null, null, null, null,null);
////        if (coHelpc==null) {
////          return;
////       }
////        else {
////            coHelpc.moveToFirst();//查詢完SQLlite會位於最後.要重新移到最前取值
////            GPSlist=coHelpc.getString(0);
//////            Log.d("GPSlistWOW", GPSlist);
////            SQLPHP=GPSlist;
////
//////            Log.d("SQLPHPWOW", SQLPHP);
////       }
////        coHelpc.close();
//
//        DbHelper GPSDbHp = new DbHelper(getApplicationContext(), DB_FILE3, null, 1);
//        gpsHelp = GPSDbHp.getWritableDatabase();//讀寫本機員工資料庫
//
//        // UI 的設定
//        spInfo = (Spinner) findViewById(R.id.SP001User);
//        //setOnItem陣列方式擺放Spinner資料
//        spInfo.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> arg0, View view, int position, long id) {
//                //int position: 會傳入user選到的item的位置
//                //int id: 會傳入user選到的item的id
//                // TODO Auto-generated method stub
//
//                //建立Spinner內的資料
//                //建立sLocation陣列存放使用strToken2方法切割mArea陣列後的資料
//                sLocation = mArea[position].split(strToken2);
//                //取得sLocation陣列1內的資料為成員userid
//                String Userid = sLocation[0]; // user name
//
//                //取得sLocation陣列1內的資料為成員名稱
//                String Usertitle = sLocation[1]; // user name
////                  Log.d("DDDDDDAAAAAA",Usertitle);
//                //dLat與dLon取得切割後為double的經緯度數值
//                dLat = Double.parseDouble(sLocation[2]); // 南北緯
//                dLon = Double.parseDouble(sLocation[3]); // 東西經
//                //取得切割後成員詳細資訊
////                   Log.d("sLocation",sLocation[1]);
////                   Log.d("sLocation",sLocation[3]);
//                if (sLocation.length >= 5)
//                    strtel = sLocation[4]; // Tel
//                if (sLocation.length >= 6)
//                    strtel = sLocation[5]; // Email
//                if (sLocation.length >= 7)
//                    strtel = sLocation[6]; // LastTime
//                //VGPS數值由dLat, dLon填入
//                VGPS = new LatLng(dLat, dLon);
//                //設定Marker
////                  if(Userid.equals(getString(R.string.uid1)) )//把自己設不同顏色
////                      vgps[position] = map.addMarker(new MarkerOptions().position(VGPS)
////                                .title(Usertitle).snippet(dLat+"#"+dLon+"#"+sLocation[4]+"#"+sLocation[5]).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
//
//                if (Userid.equals(strMyUid))//把自己設不同顏色
//                    vgps[position] = map.addMarker(new MarkerOptions().position(VGPS)
//                            .title(Usertitle).snippet(dLat + "#" + dLon + "#" + sLocation[4] + "#" + sLocation[5] + "#" + sLocation[6]).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
//                else
//                    vgps[position] = map.addMarker(new MarkerOptions().position(VGPS)
//                            .title(Usertitle).snippet(dLat + "#" + dLon + "#" + sLocation[4] + "#" + sLocation[5] + "#" + sLocation[6]));
//
//                map.moveCamera(CameraUpdateFactory.newLatLngZoom(VGPS, 12));
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> arg0) {
//                // TODO Auto-generated method stub
//            }
//        });
//
//        //Map準確程度
//        mLocationMgr = (LocationManager) getSystemService(LOCATION_SERVICE);
//        Criteria c = new Criteria();
//        mBestLocationProv = mLocationMgr.getBestProvider(c, true);
//        //一開始先把資料庫的資料顯示出來
//        showList();
//        Sqliteupdate();
//       onDestroy();        
    }


//測試功能.透過Line發送訊息    
//    public void galleryHandler(View view) {
//        if(checkLineInstalled()){
//            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//            intent.setType("image/*");
//            startActivityForResult(Intent.createChooser(intent,"select"), REQUEST_ACTION_PICK);
//        }else{
//            Toast toast = Toast.makeText(this, "LINE", Toast.LENGTH_SHORT);
//            toast.show();
//        }
//    }
//    public void sendTextHandler(View view) {
//        String sendText = ((TextView)findViewById(R.id.send_text)).getText().toString();
//        if(checkLineInstalled()){
//            Intent intent = new Intent(Intent.ACTION_SEND);
//            intent.setClassName(PACKAGE_NAME, CLASS_NAME);
//            intent.setType("text/plain");
//            intent.putExtra(Intent.EXTRA_TEXT, sendText);
//            startActivity(intent);
//        }else{
//            Toast toast = Toast.makeText(this, "LINEがインストールされていません", Toast.LENGTH_SHORT);
//            toast.show();
//        }
//    }
//    //設計一個boolean檢驗是否安裝某軟體
//    private boolean checkLineInstalled(){
//        PackageManager pm = getPackageManager();
//        m_appList = pm.getInstalledApplications(0);
//        boolean lineInstallFlag = false;
//        for (ApplicationInfo ai : m_appList) {
//            if(ai.packageName.equals(PACKAGE_NAME)){
//                lineInstallFlag = true;
//                break;
//            }
//        }
//        return lineInstallFlag;
//    }

    private String getDateTime() {
        // TODO Auto-generated method stub
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss");
        String currentDateandTime = sdf.format(new Date(System.currentTimeMillis()));
        return currentDateandTime;
    }


    //更新資料顯示區域
    private void showList() {

        //撈資料費時.造成ANR故使用thread執行
        //建立一個query資料的thread
//        Thread   t_queryData=new Thread(queryData);
//        //開始query資料的thread
//         t_queryData.start();  
//        // 等待 query thread 完成才繼續作UI更新
//         try {
//             t_queryData.join();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        //建立一個querySQLiteData資料的thread
        Thread t_querySQLiteData = new Thread(querySQLiteData);
        //開始query資料的thread
        t_querySQLiteData.start();
        // 等待 query thread 完成才繼續作UI更新
        try {
            t_querySQLiteData.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        // query資料後丟給Spinner的處理.用於UI顯示.
        //下面也可給thread動作
        //MySQL資料丟入mArea陣列並用strToken1方式處理
//        mArea=pageData.split(strToken1);
        //SQLite資料丟入mArea陣列並用strToken1方式處理
        mArea = SQLiteData.split(strToken1);
        //Spinner內容為一個陣列表
        ArrayList<String> alluser = new ArrayList<String>();
//        for(int i=0; i<mArea.length-1; i++){ // mArea.length-1 去掉多餘的空白列(跟  php 的設定有關)
        for (int i = 0; i < mArea.length; i++) {
            String userName[] = mArea[i].split(strToken2);

            alluser.add(userName[1]);
//      Log.d("AAuserName",userName[1]);
        }
        ArrayAdapter<String> aAdapterArea = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, alluser);
        aAdapterArea.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spInfo.setAdapter(aAdapterArea);
//        spInfo.setPrompt(getString(R.string.member));

//        Sqliteupdate();
        //設地圖
        setMapLocation();

        onDestroy();


    }


    private void Sqliteupdate() {
        // TODO Auto-generated method stub
        //SQLite與MySQL同步資料資料費時.造成ANR故使用thread執行
        //建立一個query資料的thread
        Thread t_Sqliteupdatet = new Thread(Sqliteupdatet);
        //開始query資料的thread
        t_Sqliteupdatet.start();
        // 等待 query thread 完成才繼續作UI更新
        try {
            t_Sqliteupdatet.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//          //若抓取資料成功               
        if (SQLiteSuc) {
            //1.把SQLite的資料清除掉
//              Toast.makeText(getApplicationContext(), "正在更新資料2刪除資料表", Toast.LENGTH_LONG).show();  
            gpsHelp.delete(DB_TABLE31, null, null);
            //2.把網頁抓到的資料分割後放入SQLite
            mPerson = pageData.split(strToken1); // parser 每一筆資料
            Log.d("AAAAWebDATAmPerson", pageData);
            for (int i = 0; i < mPerson.length; i++) {
                String userInfo[] = mPerson[i].split(strToken2);//parser出第 i 筆資料的每一個欄位
                ContentValues newRow = new ContentValues();
                newRow.put("userid", userInfo[0]);
                newRow.put("name", userInfo[1]);
//                  newRow.put("Gro",userInfo[3]);
                newRow.put("Lan", userInfo[2]);
//                  Log.d("AAAAAAAAALan", userInfo[2]);
                newRow.put("Lng", userInfo[3]);
//                  Log.d("BBBBBLng", userInfo[2]);
                newRow.put("Tel", userInfo[4]);
//                  newRow.put("Email",userInfo[5]);
//                  newRow.put("pic",userInfo[6]);
                newRow.put("Lasttime", userInfo[5]);
                gpsHelp.insert(DB_TABLE31, null, newRow);//寫到SQLite
//                  Toast.makeText(getApplicationContext(), "正在更新資料3寫入資料表", Toast.LENGTH_LONG).show();  
            }
//           showList();
            setMapLocation();
        } else {
            Toast.makeText(Map.this, getString(R.string.msg_fail), Toast.LENGTH_SHORT).show();

        }
    }


    public void setMapLocation() {
        //設定地圖
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        map.clear(); //清除標誌
        //  vgps[]設定要幾個座標; mArea:MySQL/SQLite抓到的字串陣列
        vgps = new Marker[mArea.length];
        //找出
        for (int i = 0; i < mArea.length; i++) {
            String[] sLocation = mArea[i].split(strToken2);
            //從MySQL來的資料分類
//              String Userid = sLocation[0]; // userid 
//              Log.d("info1",sLocation[0]);//set temp userid
//              String Usertitle = sLocation[1]; // user name 
//              double dLat = Double.parseDouble(sLocation[2]); // 南北緯
//              double dLon = Double.parseDouble(sLocation[3]); // 東西經
            //從SQLite來的資料分類
            String Userid = sLocation[0]; // userid
//              Log.d("info1",sLocation[0]);//set temp userid
            String Usertitle = sLocation[1]; // user name
//              Log.d("info2",sLocation[1]);//set temp username
            double dLat = Double.parseDouble(sLocation[2]); // 南北緯
            double dLon = Double.parseDouble(sLocation[3]); // 東西經
            String strtel = "";
            if (sLocation.length >= 5)
                strtel = sLocation[4]; // tel
            if (sLocation.length >= 6)
                strtel = sLocation[5]; // Email
            if (sLocation.length >= 7)
                strtel = sLocation[6]; // LastTime
//              Log.d("info5",sLocation[5]);//set temp passwd
            VGPS = new LatLng(dLat, dLon);
//              if(Userid.equals(getString(R.string.uid1)) )//把自己設不同顏色
//                  vgps[i] = map.addMarker(new MarkerOptions().position(VGPS)
//                          .title(Usertitle).snippet(dLat+"#"+dLon+"#"+sLocation[4]+"#"+sLocation[5]).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
            if (Userid.equals(strMyUid))//把自己設不同顏色
                vgps[i] = map.addMarker(new MarkerOptions().position(VGPS)
                        .title(Usertitle).snippet(dLat + "#" + dLon + "#" + sLocation[4] + "#" + sLocation[5] + "#" + sLocation[6]).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
            else
                vgps[i] = map.addMarker(new MarkerOptions().position(VGPS)
                        .title(Usertitle).snippet(dLat + "#" + dLon + "#" + sLocation[4] + "#" + sLocation[5] + "#" + sLocation[6]));
        }
        //移動至該位置
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(VGPS, 12));
        //自訂MapMaker的資訊
        setmymapAdapter();
        map.setOnInfoWindowClickListener(InfoWindowClickListener);//設定 click 監聽
    }

    //GPS相關程序
    @Override

//      public void onLocationChanged(Location arg0) {

    public void onLocationChanged(Location location) {
        // TODO Auto-generated method stub

//				//取得自己的緯度經度
        tdLat = location.getLatitude();
        tdLon = location.getLongitude();
//        	
//            Toast.makeText(getApplicationContext(), getString(R.string.Alert), Toast.LENGTH_SHORT).show();
//            String str=getString(R.string.Nowloca)+"\n"; //設定Toast用變數
//            str = str + getString(R.string.lat)+ + location.getLatitude() +"\n"+getString(R.string.lon) +location.getLongitude();//將經緯度設定給變數
//            Toast.makeText(getApplicationContext(), str, Toast.LENGTH_LONG).show();


        //變動時會丟位置資料進來a001=B001&d002=26.1111&d003=120.3333
        newUpdate = strMyUid + "&d002=" + location.getLatitude() + "&d003=" + location.getLongitude() + "&d004=" + getDateTime();//設定update字串
//            newUpdate=strMyUid+"&ad1="+location.getLatitude()+"&ad2="+location.getLongitude()
//                    +"&tel="+strMyTel+"&email="+strMyEmail;//設定update字串
        //更新GPS資訊到SQL內.費時.造成ANR故使用thread執行
        Thread t_updateBosslocData = new Thread(updateBosslocData);
        t_updateBosslocData.start();   // 指派臨時工開始工作
        try {
            t_updateBosslocData.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//             Toast.makeText(getApplicationContext(), "正在更新老闆個人GPS位置", Toast.LENGTH_LONG).show();   

//            抓資料回來更新 &更新地圖

        Sqliteupdate();
        showList();
    }

    @Override
//      public void onProviderDisabled(String arg0)
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub
//            Toast.makeText(getApplicationContext(),"您的GPS已關閉", Toast.LENGTH_LONG).show();
        mLocationMgr.removeUpdates(this);
    }

    @Override
//      public void onProviderEnabled(String arg0)
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub
//            Toast.makeText(getApplicationContext(), "您的GPS已開啟", Toast.LENGTH_LONG).show();
        mLocationMgr.requestLocationUpdates(mBestLocationProv, 120000, 60, this);//60秒或1公尺
    }

    @Override
//      public void onStatusChanged(String arg0, int arg1, Bundle arg2)
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
        Criteria c = new Criteria();
        mBestLocationProv = mLocationMgr.getBestProvider(c, true);
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

    //  自訂點下Marker的 Adapter
    private void setmymapAdapter() {
        // TODO Auto-generated method stub
        map.setInfoWindowAdapter(new InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                View v = getLayoutInflater().inflate(R.layout.marker, null);
                TextView infolat = (TextView) v.findViewById(R.id.infolat);
                TextView infolng = (TextView) v.findViewById(R.id.infolng);
                TextView infotitle = (TextView) v.findViewById(R.id.infotitle);
                TextView infotel = (TextView) v.findViewById(R.id.infotel);
                TextView infotime = (TextView) v.findViewById(R.id.infotime);


                personal = marker.getSnippet().split("#");
                infotitle.setText(marker.getTitle());
                infotitle.setTypeface(Typeface.DEFAULT_BOLD);
                infolat.setHint(getString(R.string.lat) + personal[0]);

                infolng.setHint(getString(R.string.lon) + personal[1]);

                infotel.setHint(getString(R.string.mapsni2) + personal[2]);
                infotime.setHint(getString(R.string.mapsni3) + personal[4]);


                return v;
            }
        });
    }


    //處理點下Maker後的進階部分
    private OnInfoWindowClickListener InfoWindowClickListener = new OnInfoWindowClickListener() {

        @Override
        public void onInfoWindowClick(Marker arg0) {
            // TODO Auto-generated method stub
            /** List選單 對話框 */

                
               
               
         /* 使用Sring item*/
            String[] funcString = Map.this.getResources().getStringArray(R.array.func);
            final String[] ListStr = {funcString[0], funcString[1], funcString[2], funcString[3], funcString[4], funcString[5]};
                
        /*final String[] ListStr = { getString(R.string.func1),  getString(R.string.func2),  getString(R.string.func3), getString(R.string.func4), getString(R.string.func5), getString(R.string.func6) };*/
            AlertDialog.Builder MyListAlertDialog = new AlertDialog.Builder(Map.this);

            MyListAlertDialog.setTitle(getText(R.string.function));


            MyListAlertDialog.setItems(ListStr, new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    switch (which) {
                        case 0://Call
                            Uri uri = Uri.parse(getString(R.string.dtotel) + personal[1]);
                            Intent it = new Intent(Intent.ACTION_CALL, uri);
                            startActivity(it);
                            break;
                        case 1://Email
//                                Uri uri1=Uri.parse(getString(R.string.dtomail)+personal[3]);
//                                Intent it2 = new Intent(Intent.ACTION_SENDTO,uri1);
//                                startActivity(it2);
                            break;
                        case 2://SMS
//                              Uri uri2 = Uri.parse(getString(R.string.dtotel)+personal[1]);
//                              Intent it3 = new Intent(Intent.ACTION_SENDTO,uri2);
                            Intent it3 = new Intent(Intent.ACTION_VIEW);
                            it3.putExtra("sms_body", getString(R.string.Nowloca) + personal[0]);
                            it3.setType("vnd.android-dir/mms-sms");
                            startActivity(it3);
//                              SmsManager sms=SmsManager.getDefault();
//                              PendingIntent mPI=PendingIntent.getBroadcast(MainActivity.this ,0,new Intent(),0);
////                              sms.sendTextMessage(destinationAddress, scAddress, text, sentIntent, deliveryIntent);
//                              sms.sendTextMessage(personal[1], null, "Text123",mPI , null);
                            break;

                        case 3://XMPP
                            Intent it4 = new Intent();
                            it4.setClass(Map.this, login.class);
                            startActivity(it4);
                            break;

                        case 4://Hangouts
                            Uri uri4 = Uri.parse("https://talkgadget.google.com/hangouts/extras/talk.google.com/myhangout");
                            Intent it6 = new Intent(Intent.ACTION_VIEW, uri4);
                            startActivity(it6);
                            break;

                        case 5://導航

                            //從點取視窗的資訊把緯度經度取出來轉成DOUBLE型態
                            Double Endlat = Double.parseDouble(personal[0]);
                            Double Endlng = Double.parseDouble(personal[1]);
                            Uri uri3 = Uri.parse("http://maps.google.com/maps?f=d&saddr=" + tdLat + "," + tdLon + "&daddr=" + Endlat + "," + Endlng);

                            Intent it5 = new Intent(Intent.ACTION_VIEW, uri3);
                            startActivity(it5);
                            break;

                    }
                }
            });


            // 建立按下取消什麼事情都不做的事件
            DialogInterface.OnClickListener OkClick = new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }
            };
//                MyListAlertDialog.setItems(ListStr, ListClick);
            MyListAlertDialog.setNeutralButton(getString(R.string.dcan), OkClick);
            MyListAlertDialog.show();
        }

    };

//////////////////   thread的工作內容  ////////////////////////    
    //query Data From MySQL 的實際動作項目內容
//        private Runnable queryData=new Runnable () {
//            public void run() {
//            // TODO Auto-generated method stub
//                try{
//                    SQLPHP=GPSlist;
//                    HttpClient client = new DefaultHttpClient();
//                    HttpGet get =  new HttpGet("http://"+SQLPHP+"Carq2.php"); 
//                    HttpResponse response = client.execute(get);
//                    HttpEntity entity = response.getEntity();
//                    String responseText = EntityUtils.toString(entity,"utf-8");
//                    pageData = responseText; //把整個網頁資料給變數pageData
////                    Log.d("AllPagedate",pageData);
//                }
//                catch (Exception ee){
//                }   
//            }
//        };          

    //query Data From SQLlite 的實際動作項目內容
    private Runnable querySQLiteData = new Runnable() {
        public void run() {
            // TODO Auto-generated method stub
            try {

                Cursor GPSHelpc = null;
                GPSHelpc = gpsHelp.query(true, DB_TABLE31, new String[]{"userid", "name", "Lan", "Lng", "Tel", "Email", "Lasttime"}, null, null, null, null, null, null);
                if (GPSHelpc == null) {
                    return;
                }
                if (GPSHelpc.getCount() == 0) {

                    Toast.makeText(Map.this, getString(R.string.msg_no_data), Toast.LENGTH_SHORT).show();
                } else {
                    int rows_num = GPSHelpc.getCount(); //取得資料表列數
                    GPSHelpc.moveToFirst();//查詢完SQLlite會位於最後.要重新移到最前取值
                    String TempSQL;
                    StringBuffer SQLiteDataB = new StringBuffer();
                    for (int i = 0; i < rows_num; i++) {
                        //把SQLite查詢到的資料給變數TempSQL
                        TempSQL = GPSHelpc.getString(0) + "," + GPSHelpc.getString(1) + "," + GPSHelpc.getString(2) + "," + GPSHelpc.getString(3) + "," + GPSHelpc.getString(4) + "," + GPSHelpc.getString(5) + "," + GPSHelpc.getString(6) + "\n";
                        //Log.d("WoWTempSQL", TempSQL);
                        //利用StringBuffer連加String
                        SQLiteDataB.append(TempSQL);
                        GPSHelpc.moveToNext();   //將指標移至下一筆資料
                    }
                    //將StringBuffer轉給SQLiteData
                    SQLiteData = SQLiteDataB.toString();

//                        SQLiteData=GPSHelpc.getString(0)+","+GPSHelpc.getString(1)+","+GPSHelpc.getString(2)+","+GPSHelpc.getString(3)+","+GPSHelpc.getString(4)+","+GPSHelpc.getString(5)+","+GPSHelpc.getString(6)+"\n"; 
//                      Log.d("WoWSQLiteDataWoW", SQLiteData);
//                    while (GPSHelpc.moveToNext())
//                    {
//                        SQLiteData=GPSHelpc.getString(0)+","+GPSHelpc.getString(1)+","+GPSHelpc.getString(2)+","+GPSHelpc.getString(3)+","+GPSHelpc.getString(4)+","+GPSHelpc.getString(5)+","+GPSHelpc.getString(6)+"\n"; 
//                        Log.d("NNNWoWSQLiteDataWoW", SQLiteData);
//                    }    
                }

            } catch (Exception ee) {
            }
        }
    };
    //updateBosslocData 的實際動作項目內容
    private Runnable updateBosslocData = new Runnable() {
        public void run() {
            // TODO Auto-generated method stub
            try {
                HttpClient client = new DefaultHttpClient();
//                    SQLPHP=GPSlist;
//                    HttpGet get =  new HttpGet("http://"+SQLPHP+"Caru2.php?a001="+newUpdate);
                HttpGet get = new HttpGet("http://" + updataurl + "Caru2.php?a001=" + newUpdate);
                Log.d("WWWWWupdateBosslocDataCCCCC", updataurl + "Caru2.php?a001=" + newUpdate);
                client.execute(get);
                Sqliteupdate();
            } catch (Exception ee) {
            }
        }
    };

    //Sqliteupdatet 的實際動作項目內容
    private Runnable Sqliteupdatet = new Runnable() {
        public void run() {
            // TODO Auto-generated method stub
            SQLiteSuc = false;
            try {
//                    SQLPHP=GPSlist;
//                    Log.d("AAAAAAAAAAAAAAAASQLPHPCCCCC", SQLPHP);
                HttpClient client = new DefaultHttpClient();
//                    HttpGet get =  new HttpGet("http://"+SQLPHP+"Carq2.php");
//                  HttpGet get =  new HttpGet("http://"+SQLPHP+"Carq2.php");
                HttpGet get = new HttpGet("http://" + updataurl + "Carq2.php");
                Log.d("SqliteupdatetCCCCC", updataurl + "Carq2.php");
                HttpResponse response = client.execute(get);
                HttpEntity entity = response.getEntity();
                String responseText = EntityUtils.toString(entity, "utf-8");
                pageData = responseText; //把整個網頁資料給變數pageData
                client.execute(get);

                SQLiteSuc = true;
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
            mThreadHandler.removeCallbacks(querySQLiteData);
            mThreadHandler.removeCallbacks(Sqliteupdatet);
            mThreadHandler.removeCallbacks(updateBosslocData);
//                mThreadHandler.removeCallbacks(queryData);
        }
        //解聘臨時工 (關閉Thread)
        if (mThread != null) {
            mThread.quit();
        }
    }
} 