package c.min.tseng;

import c.min.tseng.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import android.R.integer;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;


import c.min.tseng.domain.CharaterImage;
import c.min.tseng.domain.DrawCG;
import c.min.tseng.domain.EPAAirBean;
import c.min.tseng.domain.ImageGestureDetector;
import c.min.tseng.domain.LabelingBean;
import c.min.tseng.domain.Pixel;
import c.min.tseng.domain.PixelImage;
import c.min.tseng.domain.ServiceCheckBean;
import c.min.tseng.domain.ViewPixelBean;
import c.min.tseng.service.ImageService;
import c.min.tseng.util.FileUtiil;
import c.min.tseng.util.ImageUtil;

public class Setting extends Activity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.main);
    }



}