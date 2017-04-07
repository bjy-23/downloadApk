package bjy.downloadapk;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.drawable.AnimationDrawable;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.LineNumberReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {
    private ProgressDialog pd;
    private String downloadAddress = "http://180.166.102.48:80/android/legal_rights.apk";
    private String saveAddress = Environment.getExternalStorageDirectory()+"/DownLoad/123.apk";

    private ImageView pb;
    private AnimationDrawable ad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button button = (Button)findViewById(R.id.download);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                progress1();

                progress2();

//                pd = new ProgressDialog(MainActivity.this);
//                pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//                pd.setCancelable(false);
//                pd.setProgressNumberFormat("");
//                pd.setMessage("正在下载更新");
//                pd.show();
//
//                new Thread(){
//                    @Override
//                    public void run() {
//                        super.run();
//                        try {
//                            getFileFromServer(downloadAddress,saveAddress);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }.start();

                NetHelper netHelper = new NetHelper(MainActivity.this);
                netHelper.execute(downloadAddress);
            }
        });
    }

    public void progress1(){
        Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(View.inflate(MainActivity.this,R.layout.pb,null),
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT));
        pb = (ImageView) dialog.findViewById(R.id.pb);
        ad = (AnimationDrawable) pb.getDrawable();
        pb.postDelayed(new Runnable() {
            @Override
            public void run() {
                ad.start();
            }
        },100);
        dialog.setCancelable(false);
        dialog.show();
    }

    public void progress2(){
        Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(View.inflate(MainActivity.this,R.layout.download,null),
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT));
        dialog.show();
    }
    public void getFileFromServer(String downloadPath,String savePath) throws Exception{
        URL url = new URL(downloadPath);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5000);
        int fileLength = conn.getContentLength();
        int perLength = fileLength/100;
        pd.setMax(fileLength);
        InputStream is = conn.getInputStream();
        File file = new File(savePath);
        FileOutputStream fos = new FileOutputStream(file);
        BufferedInputStream bis = new BufferedInputStream(is);
        byte[] buffer = new byte[1024];
        int leng;
        int total = 0;
        int x = 1;
        while ((leng = bis.read(buffer))!=-1){
            fos.write(buffer,0,leng);
            total +=leng;

            pd.setProgress(total);

        }
        fos.close();
        bis.close();
        is.close();
        Log.d("11","22");
        pd.dismiss();
        Toast.makeText(MainActivity.this,"下载完成",Toast.LENGTH_SHORT).show();
    }

}
