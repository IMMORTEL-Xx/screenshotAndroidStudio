package com.example.screenshottest;
import static android.content.ContentValues.TAG;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.example.codeexemple.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
public class MainActivity extends AppCompatActivity {
    Button take;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] permissionstorage = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        take = findViewById(R.id.takeshot);
        checkpermissions(this); // check or ask for storage permission
        take.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //pass layout view to method to perform capture screenshot and save it.
                takeScreenshot(getWindow().getDecorView().getRootView());
            }
        });
    }
    protected File takeScreenshot(View view) {
        Date date = new Date();
        try {
            String dirpath;
            // Initialising the directory of storage
            dirpath= MainActivity.this.getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString() ;
            File file = new File(dirpath);
            if (!file.exists()) {
                boolean mkdir = file.mkdir();
            }
            // File name : keeping file name unique using data time.
            String path = dirpath + "/"+ date.getTime() + ".jpeg";
            view.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
            view.setDrawingCacheEnabled(false);
            File imageurl = new File(path);
            FileOutputStream outputStream = new FileOutputStream(imageurl);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);
            outputStream.flush();
            outputStream.close();
            Log.d(TAG, "takeScreenshot Path: "+imageurl);
            Toast.makeText(MainActivity.this,""+imageurl,Toast.LENGTH_LONG).show();
            return imageurl;
        } catch (FileNotFoundException io) {
            io.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    // check weather storage permission is given or not
    public static void checkpermissions(Activity activity) {
        int permissions = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        // If storage permission is not given then request for External Storage Permission
        if (permissions != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, permissionstorage, REQUEST_EXTERNAL_STORAGE);
        }
    }

    public void startBackGroundProcessButtonClick(View view){

        Intent intent = new Intent(this, myBackgroundProcess.class);
        intent.setAction("BackgroundProcess");

        //Set the repeated Task
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, 0, 10, pendingIntent);

        finish();
    }
}