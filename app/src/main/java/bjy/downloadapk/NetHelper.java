package bjy.downloadapk;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.MessageQueue;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * Created by 1229 on 2016/8/8.
 */
public class NetHelper extends AsyncTask<Object,Object,Integer> {
    private Context mcontext;
    private String saveAddress = Environment.getExternalStorageDirectory()+"/DownLoad/123.apk";
    private ProgressDialog pd;
    private DownloadDialog dd;

    public NetHelper(Context context){
        this.mcontext = context;
    }

    @Override
    protected void onPreExecute() {
//        pd = new ProgressDialog(mcontext);
//        pd.setProgressStyle(1);
//        pd.setProgressNumberFormat("");
//        pd.setMessage("正在下载...");
//        pd.show();
        progress2();

    }

    @Override
    protected Integer doInBackground(Object... params) {
        String downUrl = (String) params[0];

        URL url = null;
        try {
            url = new URL(downUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            int fileLength = conn.getContentLength();
            int perLengtn = fileLength/100;
//            pd.setMax(fileLength);
            conn.setConnectTimeout(5000);
            InputStream is = conn.getInputStream();
            File file = new File(saveAddress);
            FileOutputStream fos = new FileOutputStream(file);
            BufferedInputStream bis = new BufferedInputStream(is);
            byte[] buffer = new byte[1024];
            int leng;
            int total = 0;
            int x= 1;
            final int xxx = 0;
            while ((leng = bis.read(buffer))!=-1){
                fos.write(buffer,0,leng);
                total += leng;



//                    Looper looper=Looper.getMainLooper();
//                    Handler handle=new Handler(looper);
//                    handle.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//
//                            publishProgress();
//                        }
//                    },1000);


//                pd.setProgress(total);
                if(total>perLengtn*x){

                    publishProgress(x);

                    x++;
                }
            }
            fos.close();
            bis.close();
            is.close();
            return 100;

//            Toast.makeText(mcontext,"下载完成",Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onProgressUpdate(Object... values) {

        int progress = Integer.parseInt(String.valueOf(values[0]));
//        pd.setProgress(progress);
        DownloadDialog.mProgress = progress;
        dd.invalidate();

    }

    @Override
    protected void onPostExecute(Integer integer) {
//        if(integer ==100){
//            pd.dismiss();
//            Toast.makeText(mcontext,"下载完成",Toast.LENGTH_SHORT).show();
//        }
    }

    public void progress2(){
        Dialog dialog = new Dialog(mcontext);
        dialog.setContentView(View.inflate(mcontext,R.layout.download,null),
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT));
        dialog.show();
        dd = (DownloadDialog) dialog.findViewById(R.id.dd);
    }
}
