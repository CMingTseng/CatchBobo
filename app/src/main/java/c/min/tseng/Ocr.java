package c.min.tseng;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import c.min.tseng.util.Guides;
import c.min.tseng.util.ImgPretreatment;

;

public class Ocr extends Activity implements SurfaceHolder.Callback, LocationListener {
    private static String TAG = "ScanBarZBarActivity";
    private Camera mCamera, mCamera2;
    //	private ImageButton cameraImgBtn01;
    private Button cameraImgBtn01, OCBT001, OCBT002;
    //	private ImageButton camImgBtn01;
    private SurfaceView mSurfaceView;
    private SurfaceHolder holder;
    private AutoFocusCallback mAutoFocusCallback = new AutoFocusCallback();

    private String path = "OCR-Photo";//SD Card的目錄
    public static String OriginalPhoto;//照片名稱

    private int cnt = 1;
    private Calendar c;
    private ImageView imageView1, img002;

    public native String getISBN(Bitmap bmp);

    Guides mGuides;//畫線

    private static final String LOGGER = Ocr.class.getName();

    // 引導其他class
    Intent billing = new Intent();

    //照片處理
    Bitmap bmp, bmpt;
    private File sdcardTempFile;

    //Tesseract字元特徵辨識
    public static Handler OCRhandler;
    private HandlerThread mThread; ///宣告臨時工
    private static String LANGUAGE = "eng";
    private static EditText etResult;
    private static ImageView ivSelected;
    private static ImageView ivTreated;
    //	private static String textResult;
    private static Bitmap bitmapSelected;
    private static Bitmap bitmapTreated;
    private static final int SHOWRESULT = 0x101;
    private static final int SHOWTREATEDIMG = 0x102;
    private static Button tessmotor001, tessmotor002;
    private String Motor, GetTF;
    //設定空值用於手動開單
    public static String textResult = "";

    //GPS與回報位置
    static double dLat, dLon, tLat, tLng;
    //location
    private LocationManager mLocationMgr;
    private String mBestLocationProv;
    private Location Billloca;
    //取得當前GPS點位與
    double lngBill, latBill;
    static String NowPT;


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);/* 去除TITLE PS:此段必須再宣告LAYOUT前面*/

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);// 設定全螢幕
        setContentView(R.layout.ocr);

//-------呼叫Guides class 對 指定layout 作 addView(mGuides)畫框框---------------------
        mGuides = new Guides(this, "");
        ((RelativeLayout) findViewById(R.id.camerarl02)).addView(mGuides);
        mGuides.getLayoutParams().width = LayoutParams.FILL_PARENT;
        mGuides.getLayoutParams().height = LayoutParams.FILL_PARENT;
///////////////////GPS設定////////////////////////////////////////////////

        //取得GPS

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

//-------- 隱藏狀態列------隱藏標題列-------設定螢幕顯示為橫向------------------------------	
        /* 隱藏狀態列 */
        // this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        // WindowManager.LayoutParams.FLAG_FULLSCREEN);
		/* 隱藏標題列 */
        // requestWindowFeature(Window.FEATURE_NO_TITLE);
		/* 設定螢幕顯示為橫向 */
        // this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);


//---------翻轉特定layout----------------------------------------------------------
//		Animation rotateAnim = AnimationUtils.loadAnimation(this,
//				R.anim.rotation);
//		LayoutAnimationController animController = new LayoutAnimationController(
//				rotateAnim, 0);
//		RelativeLayout layout = (RelativeLayout) findViewById(R.id.camerarl02);
//		layout.setLayoutAnimation(animController);


//--------- SurfaceHolder設定---------------------------------------------------
        mSurfaceView = (SurfaceView) findViewById(R.id.mSurfaceView);
        holder = mSurfaceView.getHolder();
        holder.addCallback(Ocr.this);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        c = Calendar.getInstance();

        //--------- Button初始化---------------------------------------------------
//		cameraImgBtn01=(ImageButton)findViewById(R.id.cameraImgBtn01);
        OCBT001 = (Button) findViewById(R.id.OCBT001);
        OCBT001.setRotation(270);
        OCBT001.setOnClickListener(OCBT001BC);
        OCBT002 = (Button) findViewById(R.id.OCBT002);
        OCBT002.setRotation(270);
        OCBT002.setOnClickListener(OCBT002BC);

    }

    /* 拍照Button的事件處理 */
//	private Button.OnClickListener CameraBtn01Click=new Button.OnClickListener() {
    private Button.OnClickListener OCBT001BC = new Button.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
					/* 自動對焦後拍照 */
            Handler mHandler = new Handler();
            mHandler.postDelayed(OCR_Data, 3000);
            mCamera.autoFocus(mAutoFocusCallback);


//			Thread   t_GetCamera=new Thread(GetCamera);
//            t_GetCamera.start();  
//           // 等待 GetCamera thread 完成才繼續作UI更新
//             try {
//                 t_GetCamera.join();
//           } catch (InterruptedException e) {
//               // TODO: handle exception
//               e.printStackTrace();
//           }


        }
    };

    private Runnable OCR_Data = new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub

            Ocr();


        }
    };


    private Button.OnClickListener OCBT002BC = new Button.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            Handler mHandler = new Handler();
            mHandler.postDelayed(Changclass, 3000);
            mCamera.autoFocus(mAutoFocusCallback);


        }
    };


    private Runnable Changclass = new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub

            billing.setClass(Ocr.this, Billing.class);
            Ocr.this.finish();
            startActivity(billing);
        }
    };


    public String getDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        String currentDateandTime = sdf.format(new Date(System.currentTimeMillis()));
        return currentDateandTime;
    }

    /**
     * 當自動對焦時候調用
     */
    public final class AutoFocusCallback implements
            android.hardware.Camera.AutoFocusCallback {
        public void onAutoFocus(boolean focused, Camera camera) {
			/* 對到焦點拍照 */
            if (focused) {
                takePicture();
//		
            }
        }
    }

    ;

    /**
     * 當拍攝相片的時候調用，該介面具有一個void onPictureTaken(byte[] data,Camera camera)函數;
     * 參數和預覽的一樣。在android中主要有三個類實現了這個介面，
     * 分別是PostViewPictureCallback、 	RawPictureCallback、
     * JepgPictureCallback。我們可以根據需要定義自己需要的類
     */
    private void takePicture() {
        if (mCamera != null) {
            Log.i(TAG, "takePicture");
            mCamera.takePicture(shutterCallback, rawCallback, jpegCallback);

        }
    }


    /**
     * 在圖像預覽的時候調用，這個介面具有一個void onShutter();可以在改函數中通知使用者快門已經關閉，例如播放一個聲音
     */
    private ShutterCallback shutterCallback = new ShutterCallback() {
        public void onShutter() {

        }
    };

    private PictureCallback rawCallback = new PictureCallback() {
        public void onPictureTaken(byte[] _data, Camera _camera) {
        }
    };

    private PictureCallback jpegCallback = new PictureCallback() {
        public void onPictureTaken(byte[] _data, Camera _camera) {
			/* 取得相仞 */
            try {
                bmp = BitmapFactory.decodeByteArray(_data, 0, _data.length);
                getOriginalPhoto();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    };

    /**
     * 直接儲存並切換至picture.class 並將裁切好之圖片存為camera2.jpg
     */
    private void getOriginalPhoto() {
        // TODO Auto-generated method stub
        Log.i(TAG, "getOriginalPhoto");
        if (bmp != null) {
		/* 檢查SDCard是否存在 */
            if (!Environment.MEDIA_MOUNTED.equals(Environment
                    .getExternalStorageState())) {
			/* SD卡不存在，顯示Toast資訊 */
                Toast.makeText(Ocr.this,
                        "SD卡不存在!無法保存相片,請插入SD卡。", Toast.LENGTH_LONG)
                        .show();
            } else {
                try {
				/* 檔不存在就創建 */
                    File f = new File(Environment
                            .getExternalStorageDirectory(), path);
                    Log.i(TAG, "click button2:" + f.getAbsolutePath());
                    if (!f.exists()) {
                        f.mkdir();
                    }
				/* 保存相片檔 String.valueOf(c.get(Calendar.MILLISECOND))+ */
//				path1 = getDateTime()+"camera.jpg";
                    OriginalPhoto = getDateTime() + "camera.jpg";
//				Log.d("TTTTTTTTTT", getDateTime());
//				File n = new File(f, path1);
                    File n = new File(f, OriginalPhoto);
                    FileOutputStream bos = new FileOutputStream(n
                            .getAbsolutePath());
				/* 檔轉換 */
                    bmp.compress(Bitmap.CompressFormat.JPEG, 100, bos);
				/* 調用flush()方法，更新BufferStream */
                    bos.flush();
				/* 結束OutputStream */
                    bos.close();

//				Toast.makeText(this,OriginalPhoto + "保存成功!", Toast.LENGTH_LONG).show();

                    //////準備GPS位置讓EXIF可以加入//////
                    ////////////////////////////////////////////////////////////////
                    Location staffloca = mLocationMgr.getLastKnownLocation(mBestLocationProv);
                    latBill = staffloca.getLatitude();
                    lngBill = staffloca.getLongitude();
                    //////開始EXIF資料///////
                    try {
                        File myFile = new File("/mnt/sdcard/OCR-Photo/" + OriginalPhoto);
                        ExifInterface exif = new ExifInterface(myFile.getCanonicalPath());

                        String LatiTude = makeLatLongString(latBill);
                        String LongTitude = makeLatLongString(lngBill);


//                  Toast.makeText(getApplicationContext(),"經緯度"+latBill+"經緯度"+lngBill, Toast.LENGTH_LONG).show();  


                        exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE, LatiTude);
                        exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF, makeLatStringRef(latBill));
                        exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, LongTitude);
                        exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF, makeLonStringRef(lngBill));

                        exif.saveAttributes();
                    } catch (Exception e) {
                        // TODO: handle exception
                        Log.d("eeeeeeeeEXIF", e.getMessage());
                        e.printStackTrace();
                    }

                    cnt++;
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }
	/* 重新設定Camera */
        stopCamera();
        initCamera();

//  開始處理照片	
        sdcardTempFile = new File("/mnt/sdcard/OCR-Photo/" + getDateTime() + "camera.jpg");
//bitmap 設置圖片尺寸，避免 記憶體溢出 OutOfMemoryError的優化方法
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;

//車牌位置照片選取處理	    
        bmpt = BitmapFactory.decodeFile(sdcardTempFile.getAbsolutePath());

        int w = bmpt.getWidth();
        int h = bmpt.getHeight();
        int[] pixels = new int[w * h];
        for (int i = 0; i < w * h; i++) {
            pixels[i] = -1000000;
        }
	
/*getPixels(int[] pixels, int offset, int stride, 
int x, int y, int width, int height)
pixels圖片背景色,offset偏移量,stride須大於bmp的寬度可為負值
x 坐標 , y 坐標, width bmp的寬度,height bmp的高度     */

        bmpt.getPixels(pixels, 0, w, w / 2, 0, 300, h);
        bmpt = Bitmap.createBitmap(pixels, 0, w, 300, h,
                Bitmap.Config.ARGB_8888);
// Bitmap 旋轉
        Matrix vMatrix = new Matrix();
        vMatrix.setRotate(90);

        Bitmap vB2 = Bitmap.createBitmap(bmpt, 0, 0, bmpt.getWidth()   // 寬度
                , bmpt.getHeight()  // 高度
                , vMatrix, true);
        try {
// 輸出的圖檔位置
            FileOutputStream fos = new FileOutputStream("/mnt/sdcard/OCR-Photo/PhotoProc.jpg");
// 將 Bitmap 儲存成 PNG / JPEG 檔案格式
            vB2.compress(Bitmap.CompressFormat.JPEG, 100, fos);
// 釋放
            fos.close();
        } catch (IOException e) {

        }

//	Intent it=new Intent();
//	it.setClass(Ocr.this,Tessmotor.class);
//	startActivity(it);	
//	}
//	
//	
//private void handOcr() {
//  建立辨識用handler
// 該handler用於處理修改结果的任務
// OCRhandler =new Handler() {
//
//	@Override
//	public void handleMessage(Message msg) {
//		switch (msg.what) {
//		case SHOWRESULT:
//			if (textResult.equals(""))
//			{
//;
//				etResult.setText("識別失敗");
////				tessmotor001.setText("識別失敗");
////				tessmotor001.setEnabled(false);
//			}
//			else
//			{
//				etResult.setText(textResult);
////				Log.d("CCCCCCCCCCCCCCCCCCCCCCCCCCCCC", textResult);
//		
//				if (etResult.getText().length()<10 && etResult.getText().length()>6) {
////					tessmotor001.setText("新增或註冊");
////					tessmotor001.setEnabled(true);
//					
//				}
//				else{
////				tessmotor001.setText("識別失敗");
////				tessmotor001.setEnabled(false);
//				}
//			}
//			break;
//		case SHOWTREATEDIMG:
//	
//			etResult.setText("識別中......");
////			tessmotor001.setText("識別中......");
////			tessmotor001.setEnabled(false);
////			showPicture(ivTreated, bitmapTreated);
//			break;
//		}
//		super.handleMessage(msg);
//	}
//	
//};
//
//setContentView(R.layout.tessmotor);
//etResult = (EditText) findViewById(R.id.tessmotoret001);


//ivSelected = (ImageView) findViewById(R.id.iv_selected);
//ivTreated = (ImageView) findViewById(R.id.iv_treated);
//tessmotor001=(Button)findViewById(R.id.tessmotorbt001);
//tessmotor002=(Button)findViewById(R.id.tessmotorbt002);
//tessmotor001.setEnabled(false);


//Ocr();


    }

    //    private String makeLatLongString(double d)
    synchronized public static final String makeLatLongString(double d) {
        // TODO Auto-generated method stub

        /**
         * convert latitude into DMS (degree minute second) format. For instance<br/>
         * -79.948862 becomes<br/>
         *  79/1,56/1,55903/1000<br/>
         * It works for latitude and longitude<br/>
         * @param latitude could be longitude.
         * @return
         */
        d = Math.abs(d);
        int degrees = (int) d;
        double remainder = d - degrees;
        int minutes = (int) (remainder * 60D);
        int seconds = (int) (((remainder * 60D) - minutes) * 60D * 1000D);
        String retVal = degrees + "/1," + minutes + "/1," + seconds + "/1000";
        return retVal;

///////////////另一個寫的角度轉換寫的仔細的方法
//         private static StringBuilder sb = new StringBuilder(20);
//         d = Math.abs(d);
////        latitude=Math.abs(latitude);
//        int degree = (int) d;
//        d *= 60;
//        d -= (degree * 60.0d);
//        int minute = (int) d;
//        d *= 60;
//        d -= (minute * 60.0d);
//        int second = (int) (d*1000.0d);
//
//        sb.setLength(0);
//        sb.append(degree);
//        sb.append("/1,");
//        sb.append(minute);
//        sb.append("/1,");
//        sb.append(second);
//        sb.append("/1000,");
//        return sb.toString();
    }

    public static String makeLatStringRef(double lat) {
        ////N跟S代表南北半球
        return lat >= 0D ? "N" : "S";
    }

    public static String makeLonStringRef(double lon) {
        ////E跟W代表東西經
        return lon >= 0.0D ? "E" : "W";
    }


    private void Ocr() {
        // TODO Auto-generated method stub
        bitmapSelected = BitmapFactory.decodeFile("/mnt/sdcard/OCR-Photo/PhotoProc.jpg");
        // 顯示選擇圖片
//			showPicture(ivSelected, bitmapSelected);			
        // 處理識別的緒
//			new Thread(new Runnable() {
//				@Override
//				public void run() {
//						bitmapTreated = ImgPretreatment
//								.converyToGrayImg(bitmapSelected);
//						Message msg = new Message();
//						msg.what = SHOWTREATEDIMG;
//						OCRhandler.sendMessage(msg);
//						textResult = doOcr(bitmapTreated, LANGUAGE);
//					Message msg2 = new Message();
//					msg2.what = SHOWRESULT;
//					OCRhandler.sendMessage(msg2);
//				}
//			}).start();

        Thread t_OcrCar = new Thread(OcrCar);
        t_OcrCar.start();
        // 等待 query companyauth Data thread 完成才繼續作UI更新
        try {
            t_OcrCar.join();
        } catch (InterruptedException e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        billing.setClass(Ocr.this, Billing.class);
        File df = new File("/mnt/sdcard/OCR-Photo/PhotoProc.jpg");
        df.delete();
        Ocr.this.finish();
        startActivity(billing);

    }

    // 將圖片顯示在view中
//		public static void showPicture(ImageView iv, Bitmap bmp){
//			iv.setImageBitmap(bmp);
//		}


    public String doOcr(Bitmap bitmap, String language) {
        TessBaseAPI baseApi = new TessBaseAPI();
        baseApi.init("/mnt/sdcard/OCR-Photo/", language);
        //baseApi.init(getSDPath(), language);

        // tess-two要求BMP須有此配置
        bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);

        baseApi.setImage(bitmap);//識別圖片

//			String text = baseApi.getUTF8Text();//轉成文字
        String text = baseApi.getUTF8Text();//轉成文字

        baseApi.clear();
        baseApi.end();

        return text;//回傳結果
    }

    public static String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED); // 判斷sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();// 獲取外存目錄
        }
        return sdDir.toString();//回傳路徑


    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceholder) {
        try {
				/* 打開相機， */
            mCamera = Camera.open();
            mCamera.setPreviewDisplay(holder);

        } catch (IOException exception) {
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceholder, int format, int w,
                               int h) {
			/* 相機初始化 */
//			Log.i(TAG, "init camera");
        initCamera();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceholder) {
//			Log.i(TAG, "destoryed camera");
        stopCamera();
        mCamera.release();
        mCamera = null;
    }

    /* 相機初始化的method */
    private void initCamera() {
        if (mCamera != null) {
            try {
                Camera.Parameters parameters = mCamera.getParameters();
					/*
					 * 設定相片大小為1024*768， 格式為JPG
					 */
                parameters.setPictureFormat(PixelFormat.JPEG);
                parameters.setPictureSize(1024, 768);
                mCamera.setParameters(parameters);
					/* 開啟預覽畫面 */
                mCamera.startPreview();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /* 停止相機的method */
    private void stopCamera() {
        if (mCamera != null) {
            try {
					/* 停止預覽 */
                mCamera.stopPreview();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

//	private final class ZoomListener implements android.hardware.Camera.OnZoomChangeListener { 
//		public void onZoomChange(int value, boolean stopped, android.hardware.Camera camera) 
//		{ 
//			Log.v(TAG, "Zoom changed: value=" + value + ". stopped=" + stopped); 
//			mZoomValue = value; 
//			// Keep mParameters up to date. We do not getParameter again in 
//			// takePicture.If we do not do this, wrong zoom value will be set. mParameters.setZoom(value);
//		 	// We only care if the zoom is stopped. mZooming is set to true when
//		 	// we start smooth zoom.
//		 
//		if (stopped && mZoomState != ZOOM_STOPPED)
//		{ 
//			if (value != mTargetZoomValue) 
//			{ 
//				mCameraDevice.startSmoothZoom(mTargetZoomValue); 
//				mZoomState = ZOOM_START; 
//			}
//		 		
//			else { mZoomState = ZOOM_STOPPED; } } } }


    //GPS相關程序
    @Override
    //  public void onLocationChanged(Location arg0)
    public void onLocationChanged(Location staffloc)


    {
        // TODO Auto-generated method stub
        //取得自己的緯度經度
//	        tLat = staffloc.getLatitude();
//	        tLng = staffloc.getLongitude();

        //   Toast.makeText(getApplicationContext(), getString(R.string.Alert), Toast.LENGTH_SHORT).show();
        //   String str=getString(R.string.Nowloca)+"\n"; //設定Toast用變數
        //   str = str + getString(R.string.lat)+ + staffloc.getLatitude() +"\n"+getString(R.string.lon) +staffloc.getLongitude();//將經緯度設定給變數
        //   Toast.makeText(getApplicationContext(), str, Toast.LENGTH_LONG).show();
        //開啟SQLite取得Loccalreport網址
//	        newUpdate=strMyUid+"&d002="+tLat+"&d003="+tLng+"&d004="+getDateTime();//設定update字串
//	        DbHelper CoDbHp = new DbHelper(getApplicationContext(), DB_FILE2,    null, 1);
//	        coHelp = CoDbHp.getReadableDatabase();// 查詢資料庫，因為是查詢，所以使用唯讀模式 .getWritableDatabase可寫入
//	        Cursor coHelpc=null;
//	        coHelpc=coHelp.query(true, DB_TABLE21, new String[]{"url"}, null, null, null, null, null,null); 
//	        if (coHelpc==null) {
//	          return;
//	       }
//	        else {
//	            coHelpc.moveToFirst();//查詢完SQLlite會位於最後.要重新移到最前取值
//	            Loccalreport=coHelpc.getString(0); 
//	            Log.d("Loccalreport", Loccalreport); 
//	       }
//	        coHelpc.close(); 
        //透過thread回報
        //更新GPS資訊到SQL內.費時.造成ANR故使用thread執行
//	        Thread   t_updatelocData=new Thread(updatelocData);
//	         t_updatelocData.start();   // 指派臨時工開始工作
//	         try {
////	             Thread.sleep(3000);//線程暫停30秒，單位毫秒
//	             t_updatelocData.join();
////	             Thread.sleep(10000);//線程暫停10秒，單位毫秒
//	        } catch (InterruptedException e) {
//	            e.printStackTrace();
//	        } 


        //   //更新GPS資訊到SQL內.費時.造成ANR故使用thread執行
        //   Thread   t_updatelocData=new Thread(updatelocData);
//	    t_updatelocData.start();   // 指派臨時工開始工作
//	    try {
//	        t_updatelocData.join();
        //   } catch (InterruptedException e) {
//	       e.printStackTrace();
        //   }
    }


    @Override
    public void onProviderDisabled(String arg0) {
        // TODO Auto-generated method stub

    }


    @Override
    public void onProviderEnabled(String arg0) {
        // TODO Auto-generated method stub
        mLocationMgr.requestLocationUpdates(mBestLocationProv, 120000, 60, this);//60秒或1公尺
    }


    @Override
    public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
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


    //////////////////thread的工作內容  ////////////////////////
    //GetCamera 的實際動作項目內容
    private Runnable GetCamera = new Runnable() {
        public void run() {
            //TODO Auto-generated method stub
            try {

                mCamera.autoFocus(mAutoFocusCallback);
            } catch (Exception ee) {
            }
        }
    };

    //OcrCar 的實際動作項目內容
    private Runnable OcrCar = new Runnable() {
        public void run() {
//TODO Auto-generated method stub
            try {
                bitmapTreated = ImgPretreatment
                        .converyToGrayImg(bitmapSelected);
//	Message msg = new Message();
//	msg.what = SHOWTREATEDIMG;
//	OCRhandler.sendMessage(msg);
                textResult = doOcr(bitmapTreated, LANGUAGE);
//Message msg2 = new Message();
//msg2.what = SHOWRESULT;
//OCRhandler.sendMessage(msg2);

            } catch (Exception ee) {
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
//移除臨時工的工作
        if (OCRhandler != null) {
            OCRhandler.removeCallbacks(OcrCar);

        }
//解聘臨時工 (關閉Thread)
        if (mThread != null) {
            mThread.quit();
        }
    }


}
