package c.min.tseng;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import c.min.tseng.dbfunction.DbHelper;

//import org.jivesoftware.smackx.packet.StreamInitiation.File;

public class Main extends Activity {

    ////////多執行緒-Handler和Thread  ///////////////////////
    //透過公會找到U成員的經紀人，這樣才能派遣工作  (找到顯示畫面的UI Thread上的Handler)    
    private Handler mUI_Handler = new Handler();
    private Handler mThreadHandler; ///宣告臨時工的經紀人
    private HandlerThread mThread; ///宣告臨時工
    ///////////////////////////////////////////////////////////////////////////////////////     


    //SQL資料處理變數
    public static String ASQLPHP = "http://cloud2014cloud.er-webs.com/";     //註冊公司網址//第2級//往後要隱藏
    //     public static  String  SQLPHP = "http://www.richiego.tw/cloud/10210/cvtc02/qrapp/";     //裝置啟用網址 //第3級
    public static String SQLPHP;     //裝置啟用網址
    public static String pageData, pageData1 = "";     //存放網頁抓回來的資料
    public static String checkParam1 = "";
    public static String checkParam2 = ""; //userauth用變數
    public static boolean bSuc = false, coSuc = false;
    public static int nId = 0;     //資料表的 ID
    private Spinner CompanyInfo;//選擇公司列表
    private String strToken1 = "\n"; //切割每筆資料
    private String strToken2 = ","; //切割每欄個人資料
    private static String[] mArea; //用於資料轉陣列存放
    private String[] sLocation;//暫時存放由陣列整理過用的公司陣列
    private String[] company;//存放公司資料陣列
    public static String Companyname, Companyurl;
    //SQLlite處理變數
    private static final String DB_FILE1 = "auth.db", DB_TABLE11 = "auth", DB_FILE2 = "company.db", DB_TABLE21 = "company", DB_FILE3 = "Locus.db", DB_TABLE31 = "Locus";
    ;
    private SQLiteDatabase mDbRW;
    //手機用變數
    TelephonyManager Tel;
    //個人資料變數
    private String strMyName;
    private String strMyUid; // uid
    private String strMyGrp;
    private String strMyTel; // uid
    private String strMyEmail;
    private String myimei;
    //登入頁面
    EditText ET0021, ET0022;
    Button BT0021, IMEI;
    Intent function = new Intent();
    //處理auth用
    private File authfile, companyfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        //取得本機資料 個人資料定義值
        TelephonyManager Tel = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        WifiManager wifiinfo = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
        myimei = Tel.getDeviceId();
        WifiInfo mymacadd = wifiinfo.getConnectionInfo();
        authfile = getBaseContext().getDatabasePath(DB_FILE1);//認證用檔案
        companyfile = getBaseContext().getDatabasePath(DB_FILE2);//認證用檔案
        loadRaw();

        //取得購買授權公司表單

        //先設定將購買授權公司表單列出的UI
        CompanyInfo = (Spinner) findViewById(R.id.SPCompany);
        CompanyInfo.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View view, int position, long id) {
                // TODO Auto-generated method stub
                //使用if選項製作預設Spinner無值
                if (coSuc) {
                    //int position: 會傳入user選到的item的位置
                    //int id : 會傳入user選到的item的id
                    //建立Spinner內的資料
                    //建立sLocation陣列存放使用strToken2方法切割mArea陣列後的資料
                    sLocation = mArea[position].split(strToken2);
                    //取得sLocation陣列1內的資料為Companyname
                    String Companynamet = sLocation[0]; // Company name
                    Companyname = Companynamet;
//                         Log.d("AAAAACompanyname",Companyname);
                    //取得sLocation陣列1內的資料為Company url
                    String Companyurlt = sLocation[1]; // Company url
                    Companyurl = Companyurlt;
                    SQLPHP = "http://" + Companyurl;
                    IMEI.setText(getText(R.string.IMEI001) + Companyname + getText(R.string.IMEI002));
                }
                coSuc = true;


            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });
        //取得購買授權公司表單的資料
        showCompanyList();

//         }


        //
        if (authfile.exists()) {
            openFunctionActivity();
//             function.setClass(Main.this,Function.class);
//             startActivity(function);
        } else {

            setupViewComponent();
        }

        onDestroy();

    }


    private void loadRaw() {
        // TODO Auto-generated method stub
            /* 檢查SDCard是否存在 */
        if (!Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {
    			/* SD卡不存在，顯示Toast資訊 */
            Toast.makeText(getApplicationContext(),
                    "SD卡不存在!請插入SD卡。", Toast.LENGTH_LONG)
                    .show();
        } else {
            //判斷OCRPath是否創建
            String OCRPath = "/mnt/sdcard/OCR-Photo";
            File OCRdir = new File(OCRPath);
            if (!OCRdir.exists()) {
                OCRdir.mkdir();
            }
            //判斷tessdata是否創建
            String tessPath = "/mnt/sdcard/OCR-Photo/tessdata";
            File tessPathdir = new File(tessPath);
            if (!tessPathdir.exists()) {
                tessPathdir.mkdir();
            }
            /**加載字庫,首先判斷字庫檔是否已創建     */
            File engFile = new File("/mnt/sdcard/OCR-Photo/tessdata/eng.traineddata");
            if (!engFile.exists()) {
                //字庫檔案eng.traineddata，先創造eng.traineddata file並將內容導入
                File file = new File(tessPath, "eng.traineddata");
                try {
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                    InputStream is = this.getApplicationContext().getResources().openRawResource(R.raw.eng);
                    FileOutputStream fos = new FileOutputStream(file);
                    byte[] buffere = new byte[is.available()];
                    is.read(buffere);
                    fos.write(buffere);
                    is.close();
                    fos.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }


    private void showCompanyList() {
        // TODO Auto-generated method stub
        //啟動query userauth Data的 thread
//            checkParam1="companyauth.php";
        checkParam1 = "companyauth.php?IMEI=" + myimei;
        Thread t_qcompauthData = new Thread(qcompauthData);
        t_qcompauthData.start();
        // 等待 query companyauth Data thread 完成才繼續作UI更新
        try {
            t_qcompauthData.join();
        } catch (InterruptedException e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        //將公司名單資料丟入mArea陣列並用strToken1方式處理
        mArea = pageData1.split(strToken1);

        //Spinner內容為一個陣列表
        ArrayList<String> alluser = new ArrayList<String>();
//             for(int i=0; i<mArea.length-1; i++){ // mArea.length-1 去掉多餘的空白列(跟  php 的設定有關)
        for (int i = 0; i < mArea.length; i++) { // mArea.length-1 去掉多餘的空白列(跟  php 的設定有關)
            String ACompanyName[] = mArea[i].split(strToken2);

            alluser.add(ACompanyName[0]);
//             Log.d("CompanyName",ACompanyName[0]);
        }
        ArrayAdapter<String> aAdapterArea = new ArrayAdapter<String>(this, R.layout.myspinner, alluser);
        aAdapterArea.setDropDownViewResource(R.layout.myspinner);
        CompanyInfo.setAdapter(aAdapterArea);

    }


    private void openFunctionActivity() {
        // TODO Auto-generated method stub
        function.setClass(Main.this, Function.class);
        startActivity(function);
        Main.this.finish();
    }


    private void setupViewComponent() {
        // TODO Auto-generated method stub
        //userauth;
//     		ET0021=(EditText)findViewById(R.id.ET0021);
//     		ET0022=(EditText)findViewById(R.id.ET0022);
//     		BT0021=(Button)findViewById(R.id.BT0021);
////     		BT0021.setOnClickListener(BT0021Clk);

        IMEI = (Button) findViewById(R.id.IMEI);
//     		IMEI.setText(getText(R.string.IMEI001)+myimei+getText(R.string.IMEI002));
        IMEI.setText(getText(R.string.IMEI000));
//     		IMEI.setText(getText(R.string.IMEI001)+"1"+getText(R.string.IMEI002));
        IMEI.setOnClickListener(IMEIClk);

    }

//        private Button.OnClickListener BT0021Clk=new Button.OnClickListener() {

    private Button.OnClickListener IMEIClk = new Button.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            //1.trim & check
//		String name=ET0021.getText().toString().trim();
//		String passwd=ET0022.getText().toString().trim();
//		if(name.isEmpty() ||passwd.isEmpty())
//		{
//			ET0021.setText("");
//			ET0022.setText("");
//			ET0021.setFocusable(true);
//			return;
//		} 
            //2.check ok, and set the php parameter
//		checkParam="?a001="+name+"&a002="+passwd;

            checkParam2 = "?a006=" + myimei;

            //啟動query userauth Data的 thread
            Thread t_quserauthData = new Thread(quserauthData);
            t_quserauthData.start();
            // 等待 query userauth Data thread 完成才繼續作UI更新
            try {
                t_quserauthData.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (bSuc) {
                if (pageData.equals("0")) // auth fail
                    showErro();
                else {
                    //建立自訂的 DbHelper 物件
                    DbHelper myDbHp = new DbHelper(getApplicationContext(), DB_FILE1, null, 1);
                    // 取得上面指定的檔名資料庫，如果該檔名不存在就會自動建立一個資料庫檔案
                    // 然後呼叫 DbHelper 物件的 onCreate() 方法，我們可以在該方法中
                    // 建立資料庫中的 table，或是後面再利用取得的 SQLiteDatabase
                    // 物件建立 table


                    // 設定建立 table 的指令
                    myDbHp.sCreateTableCommand = "CREATE TABLE " + DB_TABLE11 + "(" + "ID INTEGER PRIMARY KEY," + "userid TEXT," + "name TEXT," + "passwd TEXT,"
                            + "state TEXT," + "checkin TEXT);";
                    // 如果指定的資料庫檔案不存在，就會先建立該檔，然後執行上面的建立 table 指令
                    mDbRW = myDbHp.getWritableDatabase();//寫入資料庫使用getWritableDatabase可寫入.查詢可用唯讀模式getReadableDatabase
                    //處理拿到的auth資料
                    //資料丟入mArea陣列並用strToken1方式處理
                    mArea = pageData.split(strToken1);
                    for (int i = 0; i < mArea.length; i++) { // mArea.length-1 去掉多餘的空白列(跟  php 的設定有關)
                        String dataAnalysis[] = mArea[i].split(strToken2);
                        //寫到SQLite插入欄位內容
                        ContentValues authRow = new ContentValues();
                        authRow.put("userid", dataAnalysis[0]);
                        authRow.put("name", dataAnalysis[1]);
                        authRow.put("passwd", dataAnalysis[2]);
                        authRow.put("state", dataAnalysis[3]);
                        mDbRW.insert(DB_TABLE11, null, authRow);
                    }
                    //清空輸入值
//			ET0021.setText("");
//			ET0022.setText("");
//			ET0021.setFocusable(true);
                    //引導下一個class
                    openFunctionActivity();
//			function.setClass(Main.this,Function.class);
//			startActivity(function);

                }
            } else
                Toast.makeText(Main.this, getText(R.string.Alert1), Toast.LENGTH_SHORT).show();
            onDestroy();

        }
    };


    private void showErro() {
//	Toast.makeText(Main.this, "帳號or密碼錯誤!!", Toast.LENGTH_SHORT).show();
        Toast.makeText(Main.this, getText(R.string.Alert2), Toast.LENGTH_SHORT).show();
    }

    //////////////////thread的工作內容  ////////////////////////
//query companyauth Data 的實際動作項目內容
    private Runnable qcompauthData = new Runnable() {
        public void run() {
//TODO Auto-generated method stub
            try {
                HttpClient client = new DefaultHttpClient();
                HttpGet get = new HttpGet(ASQLPHP + checkParam1);
                HttpResponse response = client.execute(get);
                HttpEntity entity = response.getEntity();
                String responseText = EntityUtils.toString(entity, "utf-8");
                pageData1 = responseText; //把整個網頁資料給變數pageData
                Log.d("AllCompany", pageData1);
            } catch (Exception ee) {
            }
        }
    };


    //query userauth Data 的實際動作項目內容
    private Runnable quserauthData = new Runnable() {
        public void run() {
// TODO Auto-generated method stub
            try {

//將選取的公司資料寫入SQLite內等候調用 //放在已經選取完後要連線的區段.才可以成功的僅取得一組值
//建立自訂的 DbHelper 物件
                DbHelper myDbHp = new DbHelper(getApplicationContext(), DB_FILE2, null, 1);
/* 取得上面指定的檔名資料庫，如果該檔名不存在就會自動建立一個資料庫檔案
**然後呼叫 DbHelper 物件的 onCreate() 方法，我們可以在該方法中
**建立資料庫中的 table，或是後面再利用取得的 SQLiteDatabase 
**物件建立 table*/


// 設定建立 table 的指令
                myDbHp.sCreateTableCommand = "CREATE TABLE " + DB_TABLE21 + "(" + "ID INTEGER PRIMARY KEY," + "Companyname TEXT," + "url TEXT);";
// 如果指定的資料庫檔案不存在，就會先建立該檔，然後執行上面的建立 table 指令
                mDbRW = myDbHp.getWritableDatabase();//寫入資料庫使用getWritableDatabase可寫入.查詢可用唯讀模式getReadableDatabase
//寫到SQLite插入欄位內容
                ContentValues CompanyRow = new ContentValues();
                CompanyRow.put("Companyname", Companyname);
                CompanyRow.put("url", Companyurl);
                mDbRW.insert(DB_TABLE21, null, CompanyRow);


                HttpClient client = new DefaultHttpClient();
                HttpGet get = new HttpGet(SQLPHP + "userauth.php" + checkParam2);
                HttpResponse response = client.execute(get);
                HttpEntity entity = response.getEntity();
                String responseText = EntityUtils.toString(entity, "utf-8");
                pageData = responseText; //把整個網頁資料給變數pageData
                Log.d("AllPagedate", pageData);
                bSuc = true;
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
            mThreadHandler.removeCallbacks(qcompauthData);
            mThreadHandler.removeCallbacks(quserauthData);
        }
//解聘臨時工 (關閉Thread)
        if (mThread != null) {
            mThread.quit();
        }
    }

}