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
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.File;
import java.util.List;

import c.min.tseng.BuildConfig;
import c.min.tseng.C;
import c.min.tseng.R;
import idv.neo.utils.DateTimeUtils;
import idv.neo.utils.FolderFileUtils;
import idv.neo.utils.SaveBitmapToFileTask;
import idv.neo.widget.Guides;

public class TakePhotoFragment extends Fragment implements TextureView.SurfaceTextureListener {
    private final static String TAG = "TakePhotoFragment";
    private Camera mCamera;
    private Bundle mBundle = null;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onHiddenChanged(false);
        mBundle = getArguments();
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
                        staffreport.setPowerRequirement(Criteria.POWER_LOW);
                        final Camera.Parameters parameters = camera.getParameters();
                        parameters.removeGpsData();
                        parameters.setGpsTimestamp(System.currentTimeMillis() / 1000);
                        final Location location = locationMgr.getLastKnownLocation(locationMgr.getBestProvider(staffreport, true));
                        if (location != null) {
                            final double lat = location.getLatitude();
                            final double lon = location.getLongitude();
                            final boolean hasLatLon = (lat != 0.0d) || (lon != 0.0d);
                            if (hasLatLon) {
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
                                    final long utcTimeSeconds = location.getTime() / 1000;
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
                                public void onPictureTaken(final byte[] _data, final Camera camera) {
                                    final Bitmap bmp = BitmapFactory.decodeByteArray(_data, 0, _data.length);
                                    if (bmp != null) {
                                        if (!FolderFileUtils.checkSDCardExist()) {
                                            Toast.makeText(context, context.getString(R.string.check_sd), Toast.LENGTH_LONG).show();
                                        } else {
                                            if (!FolderFileUtils.checkExternalFolderFileExist(File.separator + BuildConfig.OCRPHOTOFOLDER)) {
                                                FolderFileUtils.createFolderFile(File.separator + BuildConfig.OCRPHOTOFOLDER);
                                            }
                                            savePhoto(bmp, location);
                                            Bitmap bmpt = bmp;
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
                                            saveOCRPrePhoto(vB2);
                                        }
                                    }
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

    private void savePhoto(final Bitmap bitmap, final Location location) {
        final File newcarphoto = new File(FolderFileUtils.getSDPath(), File.separator + BuildConfig.OCRPHOTOFOLDER + File.separator + DateTimeUtils.getlongTimeToString(System.currentTimeMillis(), null) + BuildConfig.TEMPPHOTOFILE);
        new SaveBitmapToFileTask(new SaveBitmapToFileTask.OnTaskCompleted() {
            @Override
            public void onTaskCompleted(String s) {
                transactionPage(true, s);
            }
        }).execute(bitmap, newcarphoto, location, Bitmap.CompressFormat.JPEG, 100);
    }

    private void saveOCRPrePhoto(final Bitmap bitmap) {
        final File ocrphoto = new File(FolderFileUtils.getSDPath(), File.separator + BuildConfig.OCRPHOTOFOLDER + File.separator + BuildConfig.OCRPHOTOFILE);
        new SaveBitmapToFileTask(new SaveBitmapToFileTask.OnTaskCompleted() {
            @Override
            public void onTaskCompleted(String s) {
                transactionPage(false, s);
            }
        }).execute(bitmap, ocrphoto, null, Bitmap.CompressFormat.JPEG, 100);
    }

    private void transactionPage(boolean isPhoto, String path) {
        if (isPhoto) {
            mBundle.putString(C.PHOTO_PATH, path);
        }
        if (!isPhoto) {
            mBundle.putString(C.OCR_PATH, path);
        }
        if (mBundle.get(C.PHOTO_PATH) != null && mBundle.get(C.OCR_PATH) != null) {
            final Fragment fragment = new BillingFragment();
            fragment.setArguments(mBundle);
            getFragmentManager().beginTransaction()
                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out)
                    .addToBackStack(TakePhotoFragment.class.getSimpleName())
                    .replace(R.id.main_content, fragment)
                    .commit();
        }
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
                    parameters.setRotation(90);
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

    @Override
    public void onResume() {
        super.onResume();
    }
}
