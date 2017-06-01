package c.min.tseng.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import c.min.tseng.BuildConfig;
import c.min.tseng.R;
import c.min.tseng.fragment.MainFragment;
import idv.neo.utils.FolderFileUtils;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Context context = getBaseContext();
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (!FolderFileUtils.checkExternalFolderFileExist(File.separator + BuildConfig.OCRPHOTOFOLDER)) {
                    FolderFileUtils.createFolderFile(File.separator + BuildConfig.OCRPHOTOFOLDER);
                }
                if (!FolderFileUtils.checkExternalFolderFileExist(File.separator + BuildConfig.OCRTESTDATAFOLDER)) {
                    FolderFileUtils.createFolderFile(File.separator + BuildConfig.OCRTESTDATAFOLDER);
                }
                final ArrayList<File> folders = FolderFileUtils.getExternalStorageDirectorys(new ArrayList<File>());
                File target = null;
                for (File folder : folders) {
                    if (FolderFileUtils.isTesseractOCRFolder(folder)) {
                        target = folder;
                        break;
                    }
                }
                //http://stackoverflow.com/questions/15912825/how-to-read-file-from-res-raw-by-name
                final File outFile = new File(target.toString(), "eng.traineddata");
                if (!outFile.exists()) {
                    InputStream in = null;
                    OutputStream out = null;
                    try {
                        in = context.getResources().openRawResource(R.raw.eng);
                        out = new FileOutputStream(outFile);
                        FolderFileUtils.writeFile(in, out);
                    } catch (IOException e) {
                        Log.e(TAG, "Failed to copy raw file: " + context.getResources().openRawResource(R.raw.eng).toString(), e);
                    } finally {
                        if (in != null) {
                            try {
                                in.close();
                            } catch (IOException e) {
                                // NOOP
                            }
                        }
                        if (out != null) {
                            try {
                                out.flush();
                                out.close();
                            } catch (IOException e) {
                                // NOOP
                            }
                        }
                    }
                }
            }
        }).start();
        setFragment(new MainFragment());
    }

    private void setFragment(Fragment fragment) {
        try {
            getSupportFragmentManager().beginTransaction().setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out)
                    .replace(R.id.main_content, fragment)
                    .commit();
        } catch (IllegalStateException ex) {
            Log.d(TAG, "setFragment(): illegal state: " + ex.getMessage());
        }
    }
}