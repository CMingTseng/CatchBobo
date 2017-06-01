package c.min.tseng.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import c.min.tseng.BuildConfig;
import c.min.tseng.C;
import c.min.tseng.R;
import idv.neo.utils.FolderFileUtils;
import idv.neo.widget.Guides;

public class TakePhotoFragment extends Fragment implements TextureView.SurfaceTextureListener {
    private final static String TAG = "TakePhotoFragment";
    private Camera mCamera;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onHiddenChanged(false);
        final Bundle arguments = getArguments();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final Context context = container.getContext();
        final View child = inflater.inflate(R.layout.fragment_takephoto, container, false);
        final Guides guidesuides = new Guides(context);
        ((RelativeLayout) child.findViewById(R.id.camerarl02)).addView(guidesuides);
        final TextureView textureview = (TextureView) child.findViewById(R.id.textureview);
        textureview.setSurfaceTextureListener(this);
        final Button takephoto = (Button) child.findViewById(R.id.takephoto);
        takephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCamera.autoFocus(new Camera.AutoFocusCallback() {
                    /**
                     * 當自動對焦時候調用
                     */
                    @Override
                    public void onAutoFocus(boolean success, Camera camera) {
                        final LocationManager locationMgr = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
                        final Criteria staffreport = new Criteria();
                        staffreport.setAccuracy(Criteria.ACCURACY_FINE);
                        staffreport.setAltitudeRequired(false);
                        staffreport.setBearingRequired(false);
                        staffreport.setCostAllowed(false);
                        //使用省電模式
                        staffreport.setPowerRequirement(Criteria.POWER_LOW);

                        final Camera.Parameters parameters = camera.getParameters();
                        parameters.removeGpsData();
                        parameters.setGpsTimestamp(System.currentTimeMillis() / 1000);
                        final Location location = locationMgr.getLastKnownLocation(locationMgr.getBestProvider(staffreport, true));
                        if (location != null) {
                            Log.d(TAG, "Show Camera : " + location);
                            double lat = location.getLatitude();
                            double lon = location.getLongitude();
                            boolean hasLatLon = (lat != 0.0d) || (lon != 0.0d);

                            if (hasLatLon) {
                                Log.d(TAG, "Set gps location");
                                parameters.setGpsLatitude(lat);
                                parameters.setGpsLongitude(lon);
                                parameters.setGpsProcessingMethod(location.getProvider().toUpperCase());
                                if (location.hasAltitude()) {
                                    parameters.setGpsAltitude(location.getAltitude());
                                } else {
                                    // for NETWORK_PROVIDER location provider, we may have
                                    // no altitude information, but the driver needs it, so
                                    // we fake one.
                                    parameters.setGpsAltitude(0);
                                }
                                if (location.getTime() != 0) {
                                    // Location.getTime() is UTC in milliseconds.
                                    // gps-timestamp is UTC in seconds.
                                    long utcTimeSeconds = location.getTime() / 1000;
                                    parameters.setGpsTimestamp(utcTimeSeconds);
                                }
                            }
                            camera.setParameters(parameters);
                        }
                        if (success) {
                            camera.takePicture(new Camera.ShutterCallback() {
                                @Override
                                public void onShutter() {
                                }
                            }, null, new Camera.PictureCallback() {
                                @Override
                                public void onPictureTaken(final byte[] data, final Camera camera) {
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            savePhoto(data);
                                        }
                                    }).start();
                                }
                            });
                        }
                    }
                });
            }
        });
        return child;
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
        initCamera(surfaceTexture);
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int width, int height) {
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        try {
            if (mCamera != null) {
                mCamera.stopPreview();
                mCamera.release();
            }
            mCamera = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
    }

    private void savePhoto(byte[] _data) {
        final Context context = getContext();
        final Bundle arguments = getArguments();
        final Bitmap bmp = BitmapFactory.decodeByteArray(_data, 0, _data.length);
        //FIXME  not write EXIF to bitmap JPEG file  !!!  so need re-write
        final LocationManager locationMgr = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        final Criteria staffreport = new Criteria();
        staffreport.setAccuracy(Criteria.ACCURACY_FINE);
        staffreport.setAltitudeRequired(false);
        staffreport.setBearingRequired(false);
        staffreport.setCostAllowed(false);
        staffreport.setPowerRequirement(Criteria.POWER_LOW);
        final Location location = locationMgr.getLastKnownLocation(locationMgr.getBestProvider(staffreport, true));
        if (bmp != null) {
            if (!FolderFileUtils.checkSDCardExist()) {
                Toast.makeText(context, "SD卡不存在!無法保存相片,請插入SD卡。", Toast.LENGTH_LONG).show();
            } else {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (!!FolderFileUtils.checkExternalFolderFileExist(File.separator + BuildConfig.OCRPHOTOFOLDER)) {
                                FolderFileUtils.createFolderFile(File.separator + BuildConfig.OCRPHOTOFOLDER);
                            }
                            final File newcarphoto = new File(FolderFileUtils.getSDPath(), File.separator + BuildConfig.OCRPHOTOFOLDER + File.separator + getDateTime() + BuildConfig.TEMPPHOTOFILE);
                            arguments.putString(C.PHOTO_PATH, newcarphoto.toString());
                            FileOutputStream filestream = new FileOutputStream(newcarphoto);
                            bmp.compress(Bitmap.CompressFormat.JPEG, 100, filestream);
                            filestream.flush();
                            filestream.close();
                            if (location != null) {
                                final ExifInterface exif = new ExifInterface(newcarphoto.getCanonicalPath());
                                final double lat = location.getLatitude();
                                final double lon = location.getLongitude();
                                final String lats = makeLatLongString(lat);
                                final String longs = makeLatLongString(lon);
                                exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE, lats);
                                exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF, makeLatStringRef(lat));
                                exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, longs);
                                exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF, makeLonStringRef(lon));
                                exif.saveAttributes();
                            }
                            final BitmapFactory.Options options = new BitmapFactory.Options();
                            options.inSampleSize = 2;
                            Bitmap bmpt = BitmapFactory.decodeFile(newcarphoto.getAbsolutePath());
                            int w = bmpt.getWidth();
                            int h = bmpt.getHeight();
                            int[] pixels = new int[w * h];
                            for (int i = 0; i < w * h; i++) {
                                pixels[i] = -1000000;
                            }
                            bmpt.getPixels(pixels, 0, w, w / 2, 0, 300, h);
                            bmpt = Bitmap.createBitmap(pixels, 0, w, 300, h, Bitmap.Config.ARGB_8888);
                            final Matrix matrix = new Matrix();
                            matrix.setRotate(90);
                            final Bitmap vB2 = Bitmap.createBitmap(bmpt, 0, 0, bmpt.getWidth(), bmpt.getHeight(), matrix, true);
                            final File ocrphoto = new File(FolderFileUtils.getSDPath(), File.separator + BuildConfig.OCRPHOTOFOLDER + File.separator + BuildConfig.OCRPHOTOFILE);
                            if (ocrphoto.exists()) {
                                ocrphoto.delete();
                            }
                            arguments.putString(C.OCR_PATH, ocrphoto.toString());
                            filestream = new FileOutputStream(ocrphoto);
                            vB2.compress(Bitmap.CompressFormat.JPEG, 100, filestream);
                            filestream.flush();
                            filestream.close();
                            final Fragment fragment = new BillingFragment();
                            fragment.setArguments(arguments);
                            getFragmentManager().beginTransaction()
                                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out)
                                    .addToBackStack(TakePhotoFragment.class.getSimpleName())
                                    .replace(R.id.main_content, fragment)
                                    .commit();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        }
    }

    private String getDateTime() {
        return (new SimpleDateFormat(C.DATETIMEFORMAT)).format(new Date(System.currentTimeMillis()));
    }

    /* 相機初始化的method */
    private void initCamera(SurfaceTexture surfaceTexture) {
        try {
            mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
            // 取得相機參數
            final Camera.Parameters parameters = mCamera.getParameters();
            //https://developer.android.com/reference/android/hardware/Camera.Parameters.html#FOCUS_MODE_CONTINUOUS_VIDEO
            final List<Camera.Size> previewSizes = parameters.getSupportedPreviewSizes();
            final List<String> focusModes = parameters.getSupportedFocusModes();
            final List<String> sceneModes = parameters.getSupportedSceneModes();
            final List<String> flashModes = parameters.getSupportedFlashModes();
            for (Camera.Size size : previewSizes) {
                if (size != null) {
           /*
            * 設定相片大小為1024*768
            *  parameters.setPictureSize(1024, 768);//FIXME　will get setParameters failed not use  need get support size
			*/
                    // 設定最佳預覽尺寸
                    parameters.setPreviewSize(size.width, size.height);
                    // 設定照片輸出為90度
//                    parameters.setRotation(90);
                    parameters.setPictureFormat(ImageFormat.JPEG);
                    parameters.setPictureSize(size.width, size.height);
                    break;
                }
            }
            if (focusModes != null) {
                for (String focus : focusModes) {
                    parameters.setFocusMode(focus);
                    if (focus.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
//                     parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
                        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
                        break;
                    }
                }
            }
            if (sceneModes != null) {
                for (String scene : sceneModes) {
                    parameters.setFocusMode(scene);
                    if (scene.contains(Camera.Parameters.SCENE_MODE_AUTO)) {
                        parameters.setFocusMode(Camera.Parameters.SCENE_MODE_AUTO);
                        break;
                    }
                }
            }

            if (flashModes != null) {
                for (String flash : flashModes) {
                    parameters.setFocusMode(flash);
                    if (flash.contains(Camera.Parameters.FLASH_MODE_AUTO)) {
                        parameters.setFocusMode(Camera.Parameters.FLASH_MODE_AUTO);
                        break;
                    }
                }
            }
//          設定預覽畫面為90度
            mCamera.setDisplayOrientation(90);
            // https://developer.android.com/reference/android/hardware/Camera.Parameters.html#setGpsLatitude%28double%29
            // 設定相機參數
            mCamera.setParameters(parameters);
            // 設定顯示的Surface
            mCamera.setPreviewTexture(surfaceTexture);
            // 開始顯示
            mCamera.startPreview();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //https://stackoverflow.com/questions/5280479/how-to-save-gps-coordinates-in-exif-data-on-android
    //    private String makeLatLongString(double d)
    private synchronized static final String makeLatLongString(double d) {
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

    private static String makeLatStringRef(double lat) {
        ////N跟S代表南北半球
        return lat >= 0D ? "N" : "S";
    }

    private static String makeLonStringRef(double lon) {
        ////E跟W代表東西經
        return lon >= 0.0D ? "E" : "W";
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
