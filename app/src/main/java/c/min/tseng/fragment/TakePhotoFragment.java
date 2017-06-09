package c.min.tseng.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import c.min.tseng.BuildConfig;
import c.min.tseng.C;
import c.min.tseng.R;
import c.min.tseng.domain.DrawCG;
import idv.neo.utils.CharaterImage;
import idv.neo.utils.DateTimeUtils;
import idv.neo.utils.FindMaxBoundaryTask;
import idv.neo.utils.FolderFileUtils;
import idv.neo.utils.ImageProcessingUtils;
import idv.neo.utils.LabelingBean;
import idv.neo.utils.PixelImage;
import idv.neo.utils.SaveBitmapToFileTask;
import idv.neo.utils.ViewPixelBean;
import idv.neo.widget.PlateWizardGuideLine;


public class TakePhotoFragment extends Fragment implements TextureView.SurfaceTextureListener {
    private final static String TAG = "TakePhotoFragment";
    private Camera mCamera;
    private Bundle mBundle = null;

    private DrawCG mDrawCG;
    private Map<Integer, ViewPixelBean> mCarPeaks = new HashMap<Integer, ViewPixelBean>();
    private int mTouchCount = 0;
    //最多僅可同時存在2點
    private final int mMaxCount = 2;
    //    private PixelImage pixelImage = null;
    private float mTop = -1;//上邊界
    private float mBottom = -1;//下邊界
    private float mLeft = -1;//左邊界
    private float mRight = -1;//右邊界
    //車牌切割區域
    private int segWidth = -1;
    private int segHeight = -1;
    private int[][] segment = null;
    //車牌粗定位區域
    private int detectWidth = -1;
    private int detectHeight = -1;
    private int[][] detectSeg = null;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onHiddenChanged(false);
        mBundle = getArguments();
        try {
            mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
            final Camera.Parameters parameters = mCamera.getParameters();
            final List<Camera.Size> previewSizes = parameters.getSupportedPreviewSizes();
            final List<String> focusModes = parameters.getSupportedFocusModes();
            final List<String> sceneModes = parameters.getSupportedSceneModes();
            final List<String> flashModes = parameters.getSupportedFlashModes();
            for (Camera.Size size : previewSizes) {
                if (size != null) {
                    parameters.setPreviewSize(size.width, size.height);
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
            mCamera.setParameters(parameters);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final Context context = container.getContext();
        final View child = inflater.inflate(R.layout.fragment_takephoto, container, false);
        final PlateWizardGuideLine guidesuides = new PlateWizardGuideLine(context);
        guidesuides.setTag(R.id.tag_mark_area);
        mDrawCG = new DrawCG(context);
        ((RelativeLayout) child.findViewById(R.id.camerarl02)).addView(guidesuides);

        final TextureView textureview = (TextureView) child.findViewById(R.id.textureview);
        textureview.setSurfaceTextureListener(this);
//        textureview.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent event) {
//                Log.d(TAG, " Show  Touch  Action " + event.getAction());
//                final int pointCount = event.getPointerCount();
//                Log.d(TAG, " Show  Touch  pointerCount " + pointCount);
//                Log.d(TAG, " Show  Touch  X  : " + event.getX());
//                Log.d(TAG, " Show  Touch  Y : " + event.getY());
//                doMark(view, event);
//                return false;
//            }
//        });
        final Button takephoto = (Button) child.findViewById(R.id.takephoto);
        takephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View uu = ((View) v.getParent()).findViewWithTag(R.id.tag_mark_area);
                Log.d(TAG, "Show V :  " + uu);
                Log.d(TAG, "Show location  X1; " + uu.getX());
                Log.d(TAG, "Show location  Y1 ; " + uu.getY());
                Log.d(TAG, "Show location  Left; " + uu.getLeft());
                Log.d(TAG, "Show location  Right ; " + uu.getRight());
                Log.d(TAG, "Show location  Top; " + uu.getTop());
                Log.d(TAG, "Show location  DOWN ; " + uu.getBottom());
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

    public void doMark(View view, MotionEvent e) {
        Bitmap textureviewbitmap = ((TextureView) view).getBitmap();
        PixelImage pixelImage = new PixelImage(textureviewbitmap.copy(Bitmap.Config.ARGB_8888, true));
        int eventaction = e.getAction();
        int pointerCount = e.getPointerCount();
        ViewPixelBean viewPixelBean = null;
        if (eventaction == MotionEvent.ACTION_DOWN) {
            float eventX = e.getX();
            float eventY = e.getY();
            Log.d(TAG, " int view [" + eventX + "][" + eventY + "]");
            float[] fs = new float[]{e.getX(), e.getY()};
            Matrix matrix = new Matrix();
            view.getMatrix().invert(matrix);
            matrix.postTranslate(view.getScrollX(), view.getScrollY());
            matrix.mapPoints(fs);
            Log.d(TAG, "in bitmap [" + fs[0] + "][" + fs[1] + "]");
            Log.d(TAG, "=========>carPeaks size before[" + mCarPeaks.size() + "] , count :  [" + mTouchCount + "]");
            if (mCarPeaks.containsKey(mTouchCount)) {
                Log.d(TAG, " get from carPeaks");
                viewPixelBean = mCarPeaks.get(mTouchCount);
                Log.d(TAG, " viewPixelBean [" + viewPixelBean + "]");
            } else {
                Log.d(TAG, " ===new carPeaks");
                viewPixelBean = new ViewPixelBean();
                mCarPeaks.put(mTouchCount, viewPixelBean);
            }
            Log.d(TAG, "=========>carPeaks size end[" + mCarPeaks.size() + "]count [" + mTouchCount + "]");

            //設定新的選取點
            viewPixelBean.setViewX(eventX);
            viewPixelBean.setViewY(eventY);
            viewPixelBean.setBitmapX(fs[0]);
            viewPixelBean.setBitmapY(fs[1]);
            mCarPeaks.put(mTouchCount, viewPixelBean);
            mDrawCG.setmBitmap(pixelImage.getBitmap().copy(Bitmap.Config.ARGB_8888, true));

            //兩點才可以畫線甚至矩型
            if (mCarPeaks.size() == 2) {
                Log.d(TAG, "ACTION_DOWN Start Draw -----circle-----");
                ViewPixelBean v1 = mCarPeaks.get(0);
                ViewPixelBean v2 = mCarPeaks.get(1);
                float v1x = v1.getBitmapX();
                float v1y = v1.getBitmapY();
                float v2x = v2.getBitmapX();
                float v2y = v2.getBitmapY();
                Log.d(TAG, "v1x [" + v1x + "] v1y[" + v1y + "]");
                Log.d(TAG, "v2x [" + v2x + "] v2y[" + v2y + "]");
                setMargin(v1, v2);
                mDrawCG.drawCircle(v1.getBitmapX(), v1.getBitmapY(), Color.RED);
                mDrawCG.drawCircle(v2.getBitmapX(), v2.getBitmapY(), Color.RED);
                doImageProcess(pixelImage);
            } else {
                Log.d(TAG, "=========~~~~~~~~~~~~~~~~~~~~~~");
                mDrawCG.drawCircle(viewPixelBean.getBitmapX(), viewPixelBean.getBitmapY(), Color.RED);
            }

            if (mTop != -1 && mBottom != -1 && mLeft != -1 && mRight != -1) {
                mDrawCG.drawRect((int) mLeft, (int) mTop, (int) mRight, (int) mBottom, Color.RED, 3);
            }
//        doScreenDrawView(mDrawCG.getmBitmap());
            Log.d(TAG, "============>count start[" + mTouchCount + "]");
            if (mTouchCount == 1) {
                mTouchCount = 0;
            } else {
                mTouchCount++;
            }

        } else if (eventaction == MotionEvent.ACTION_POINTER_DOWN) {
            if (pointerCount == 1) {
                float eventX = e.getX(0);
                float eventY = e.getY(0);
                Log.d(TAG, " int view [" + eventX + "][" + eventY + "]");
                float[] fs = new float[]{e.getX(), e.getY()};
                Matrix matrix = new Matrix();
                view.getMatrix().invert(matrix);
                matrix.postTranslate(view.getScrollX(), view.getScrollY());
                matrix.mapPoints(fs);
                Log.d(TAG, "in bitmap [" + fs[0] + "][" + fs[1] + "]");
                Log.d(TAG, "=========>carPeaks size before[" + mCarPeaks.size() + "] , count :  [" + mTouchCount + "]");
                if (mCarPeaks.containsKey(mTouchCount)) {
                    Log.d(TAG, " get from carPeaks");
                    viewPixelBean = mCarPeaks.get(mTouchCount);
                    Log.d(TAG, " viewPixelBean [" + viewPixelBean + "]");
                } else {
                    Log.d(TAG, " ===new carPeaks");
                    viewPixelBean = new ViewPixelBean();
                    mCarPeaks.put(mTouchCount, viewPixelBean);
                }
                Log.d(TAG, "=========>carPeaks size end[" + mCarPeaks.size() + "]count [" + mTouchCount + "]");

                //設定新的選取點
                viewPixelBean.setViewX(eventX);
                viewPixelBean.setViewY(eventY);
                viewPixelBean.setBitmapX(fs[0]);
                viewPixelBean.setBitmapY(fs[1]);
                mCarPeaks.put(mTouchCount, viewPixelBean);
                mDrawCG.setmBitmap(pixelImage.getBitmap().copy(Bitmap.Config.ARGB_8888, true));

                //兩點才可以畫線甚至矩型
                if (mCarPeaks.size() == 2) {
                    Log.d(TAG, "ACTION_POINTER_DOWN  Start Draw -----circle-----");
                    ViewPixelBean v1 = mCarPeaks.get(0);
                    ViewPixelBean v2 = mCarPeaks.get(1);
                    float v1x = v1.getBitmapX();
                    float v1y = v1.getBitmapY();
                    float v2x = v2.getBitmapX();
                    float v2y = v2.getBitmapY();
                    Log.d(TAG, "v1x [" + v1x + "] v1y[" + v1y + "]");
                    Log.d(TAG, "v2x [" + v2x + "] v2y[" + v2y + "]");
//            setMargin(v1, v2);
                    mDrawCG.drawCircle(v1.getBitmapX(), v1.getBitmapY(), Color.RED);
                    mDrawCG.drawCircle(v2.getBitmapX(), v2.getBitmapY(), Color.RED);
                    doImageProcess(pixelImage);
                } else {
                    Log.d(TAG, "=========~~~~~~~~~~~~~~~~~~~~~~");
                    mDrawCG.drawCircle(viewPixelBean.getBitmapX(), viewPixelBean.getBitmapY(), Color.RED);
                }

                if (mTop != -1 && mBottom != -1 && mLeft != -1 && mRight != -1) {
                    mDrawCG.drawRect((int) mLeft, (int) mTop, (int) mRight, (int) mBottom, Color.RED, 3);
                }
//        doScreenDrawView(mDrawCG.getmBitmap());
                Log.d(TAG, "============>count start[" + mTouchCount + "]");
                if (mTouchCount == 1) {
                    mTouchCount = 0;
                } else {
                    mTouchCount++;
                }
            } else if (pointerCount == 2) {
                float eventX = e.getX(1);
                float eventY = e.getY(1);
                Log.d(TAG, " int view [" + eventX + "][" + eventY + "]");
                float[] fs = new float[]{e.getX(), e.getY()};
                Matrix matrix = new Matrix();
                view.getMatrix().invert(matrix);
                matrix.postTranslate(view.getScrollX(), view.getScrollY());
                matrix.mapPoints(fs);
                Log.d(TAG, "in bitmap [" + fs[0] + "][" + fs[1] + "]");
                Log.d(TAG, "=========>carPeaks size before[" + mCarPeaks.size() + "] , count :  [" + mTouchCount + "]");
                if (mCarPeaks.containsKey(mTouchCount)) {
                    Log.d(TAG, " get from carPeaks");
                    viewPixelBean = mCarPeaks.get(mTouchCount);
                    Log.d(TAG, " viewPixelBean [" + viewPixelBean + "]");
                } else {
                    Log.d(TAG, " ===new carPeaks");
                    viewPixelBean = new ViewPixelBean();
                    mCarPeaks.put(mTouchCount, viewPixelBean);
                }
                Log.d(TAG, "=========>carPeaks size end[" + mCarPeaks.size() + "]count [" + mTouchCount + "]");

                //設定新的選取點
                viewPixelBean.setViewX(eventX);
                viewPixelBean.setViewY(eventY);
                viewPixelBean.setBitmapX(fs[0]);
                viewPixelBean.setBitmapY(fs[1]);
                mCarPeaks.put(mTouchCount, viewPixelBean);
                mDrawCG.setmBitmap(pixelImage.getBitmap().copy(Bitmap.Config.ARGB_8888, true));

                //兩點才可以畫線甚至矩型
                if (mCarPeaks.size() == 2) {
                    Log.d(TAG, " Start Draw -----circle-----");
                    ViewPixelBean v1 = mCarPeaks.get(0);
                    ViewPixelBean v2 = mCarPeaks.get(1);
                    float v1x = v1.getBitmapX();
                    float v1y = v1.getBitmapY();
                    float v2x = v2.getBitmapX();
                    float v2y = v2.getBitmapY();
                    Log.d(TAG, "v1x [" + v1x + "] v1y[" + v1y + "]");
                    Log.d(TAG, "v2x [" + v2x + "] v2y[" + v2y + "]");
                    setMargin(v1, v2);
                    mDrawCG.drawCircle(v1.getBitmapX(), v1.getBitmapY(), Color.RED);
                    mDrawCG.drawCircle(v2.getBitmapX(), v2.getBitmapY(), Color.RED);
                    doImageProcess(pixelImage);
                } else {
                    Log.d(TAG, "=========~~~~~~~~~~~~~~~~~~~~~~");
                    mDrawCG.drawCircle(viewPixelBean.getBitmapX(), viewPixelBean.getBitmapY(), Color.RED);
                }

                if (mTop != -1 && mBottom != -1 && mLeft != -1 && mRight != -1) {
                    mDrawCG.drawRect((int) mLeft, (int) mTop, (int) mRight, (int) mBottom, Color.RED, 3);
                }
//        doScreenDrawView(mDrawCG.getmBitmap());
                Log.d(TAG, "============>count start[" + mTouchCount + "]");
                if (mTouchCount == 1) {
                    mTouchCount = 0;
                } else {
                    mTouchCount++;
                }
            }
        }
    }

    //設定所點選於原圖的上下左右
    public void setMargin(ViewPixelBean v1, ViewPixelBean v2) {
        float v1x = v1.getBitmapX();
        float v1y = v1.getBitmapY();
        float v2x = v2.getBitmapX();
        float v2y = v2.getBitmapY();
        //==========設定上下左右
        if (v1x > v2x) {
            mLeft = v2x;
            mRight = v1x;
        } else {
            mLeft = v1x;
            mRight = v2x;
        }
        if (v1y > v2y) {
            mTop = v2y;
            mBottom = v1y;
        } else {
            mTop = v1y;
            mBottom = v2y;
        }
    }

    public void doImageProcess(PixelImage pixelImage) {
        //切割
        segWidth = (int) (mRight - mLeft);
        segHeight = (int) (mBottom - mTop);

        //因為為640X480畫面，以1/8當閥值
        int threshWidth = 240;
        int threshHeight = 135;
        if (segWidth >= threshWidth && segHeight >= threshHeight) {
            segment = new int[(int) segWidth][(int) segHeight];
            for (int x = (int) mLeft; x < (int) mRight; x++) {
                for (int y = (int) mTop; y < (int) mBottom; y++) {
                    segment[(int) (x - mLeft)][(int) (y - mTop)] = pixelImage.getPixels()[y * pixelImage.getWidth() + x];
                }
            }
            final int[][] grays = ImageProcessingUtils.toGrays(segment, segWidth, segHeight);
            int otsuThreshs = ImageProcessingUtils.otsuHresholdValue(grays, segWidth, segHeight);
            //做sobel
            if (otsuThreshs > 190) {
                otsuThreshs = ImageProcessingUtils.bestThreshValue(grays, segWidth, segHeight);
            }

            //轉黑白

            int white = Color.WHITE;
            int black = Color.BLACK;
            for (int x = 0; x < segWidth; x++) {
                for (int y = 0; y < segHeight; y++) {
                    if ((grays[x][y] & 0xff) > otsuThreshs) {
                        segment[x][y] = white;
                    } else {

                        segment[x][y] = black;
                    }
                }
            }

            /**連通區域標記**/
            //白色為前景，黑色為背景找出車牌

            LabelingBean labelingBean = LabelingBean.getTwoPassLabelingBean(segment, segWidth, segHeight, Color.BLACK, Color.WHITE);
            Log.d(TAG, " getLabelsCharMap size [" + labelingBean.getLabelsCharMap().size() + "]");
            //取出最大的邊界(因白色為前景的最大區域一般為車牌)
            CharaterImage maxCharImage = getMarkLabelingBeanMax(labelingBean);
            if (maxCharImage != null) {
                new FindMaxBoundaryTask(new FindMaxBoundaryTask.OnTaskCompleted() {
                    @Override
                    public void onTaskCompleted(Bitmap bitmap) {
                        Bitmap bmpt = bitmap;
                        final File newcarphoto = new File(FolderFileUtils.getSDPath(), File.separator + BuildConfig.OCRPHOTOFOLDER + File.separator + " mark_11_" + DateTimeUtils.getlongTimeToString(System.currentTimeMillis(), null) + "mark2" + BuildConfig.TEMPPHOTOFILE);
                        Log.d(TAG, "Save File");
                        new SaveBitmapToFileTask(new SaveBitmapToFileTask.OnTaskCompleted() {
                            @Override
                            public void onTaskCompleted(String s) {
                                Log.d(TAG, "Save File OK");
                            }
                        }).execute(bmpt, newcarphoto, null, Bitmap.CompressFormat.JPEG, 100);
                    }
                }).execute(pixelImage, (int) mTop, (int) mLeft, maxCharImage);
            }
        }
    }

    /**
     * 將得到的標記後圖片LabelingBean
     * 取出最大區塊
     */
    public static CharaterImage getMarkLabelingBeanMax(LabelingBean labelingBean) {
        Map<Integer, CharaterImage> map = labelingBean.getLabelsCharMap();
        CharaterImage maxCharImage = null;
        Log.d(TAG, "===>getTwoPassLabelingBean size [" + map.size() + "]");
        Set<Map.Entry<Integer, CharaterImage>> set = map.entrySet();

        for (Map.Entry<Integer, CharaterImage> entry : set) {
            CharaterImage charaterImage = entry.getValue();
            if (maxCharImage == null ||
                    (maxCharImage.getHeight() * maxCharImage.getWidth() < (charaterImage.getHeight() * charaterImage.getWidth()))) {
                maxCharImage = charaterImage;
            }

        }
        return maxCharImage;
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
        try {
            if (mCamera != null) {
                mCamera.setDisplayOrientation(90);
                mCamera.setPreviewTexture(surfaceTexture);
                mCamera.startPreview();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    @Override
    public void onResume() {
        super.onResume();
    }
}
