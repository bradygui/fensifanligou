package g.b.fensifanligou;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.StrictMode;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Administrator on 2016/12/7.
 */
public class startup extends Activity {

    //延迟3秒
    private static final long SPLASH_DELAY_MILLIS = 3000;
    String imei,versionName,verSend="";
    private StringBuffer sb;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startup);
     //   wakeUpAndUnlock(startup.this);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads().detectDiskWrites().detectNetwork()
                .penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
                .penaltyLog().penaltyDeath().build());


        TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
       // imei = telephonyManager.getDeviceId();
        try {
            versionName = this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName;
        } catch (Exception e) {
            Log.e("asdfaaaaaadfsa", e.toString());
        }
        //    thread1.start();
        // 使用Handler的postDelayed方法，3秒后执行跳转到MainActivity
        new Handler().postDelayed(new Runnable() {
            public void run() {
                //goHome();
               // imeicheck();
                versioncheck();

            }
        }, SPLASH_DELAY_MILLIS);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            /*
            if(msg.what == 1){
            bt_view.setText(String.valueOf(count));
            goHome();}
            */

            switch (msg.what) {
                case 1:
                    goHome();
                    break;
                case 2:
                    //对话框通知用户升级程序
                    showUpdataDialog();
                    break;
                case 3:
                    //服务器超时
                    Toast.makeText(getApplicationContext(), "获取服务器更新信息失败", Toast.LENGTH_LONG).show();
               //     LoginMain();
                    break;
                case 4:
                    //下载apk失败
                    Toast.makeText(getApplicationContext(), "下载新版本失败", Toast.LENGTH_LONG).show();
                 //   LoginMain();
                    break;
                case 5:
                    //下载apk失败
                    showImeiCode();
                    //   LoginMain();
                    break;
            }

        }
    };

    private void goHome() {

        Intent intent = new Intent(startup.this, MainActivity.class);
        startup.this.startActivity(intent);
        startup.this.finish();
    }

    public static void wakeUpAndUnlock(Context context) {
        //  KeyguardManager km= (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        // KeyguardManager.KeyguardLock kl = km.newKeyguardLock("unLock");
        //解锁
        // kl.disableKeyguard();
        //获取电源管理器对象
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        //获取PowerManager.WakeLock对象,后面的参数|表示同时传入两个值,最后的是LogCat里用的Tag
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK, "bright");
        //点亮屏幕
        wl.acquire();
        //释放
        wl.release();
    }

    final Thread thread1 = new Thread(new Runnable() {
        public void run() {
            versioncheck();
        }
    });

    public void imeicheck() {
        String httpUrl = "http://cqtimes.gotoip2.com/limits.txt";

        // InputStream is = null;
        try {
            URL url = new URL(httpUrl);
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            urlConn.connect();
            InputStream input = urlConn.getInputStream();
            InputStreamReader inputreader = new InputStreamReader(input);
            BufferedReader reader = new BufferedReader(inputreader);
            String str = null;
            sb = new StringBuffer();
            Boolean getInSwitch=false;
            while ((str = reader.readLine()) != null) {
                //  sb.append(str).append("\n");
                if (str.equals(imei)) {
                    getInSwitch=true;
                }
            }
            if (!getInSwitch){
                reader.close();
                inputreader.close();
                input.close();
                reader = null;
                inputreader = null;
                input = null;
              //  System.exit(0);
                Message msg = new Message();
                msg.what = 5;
                handler.sendMessage(msg);

            }else{
            reader.close();
            inputreader.close();
            input.close();
            reader = null;
            inputreader = null;
            input = null;
                versioncheck();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
            Message msg = new Message();
            msg.what = 3;
            handler.sendMessage(msg);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Message msg = new Message();
            msg.what = 3;
            handler.sendMessage(msg);
        }
    }

    public void versioncheck() {
        String httpUrl = "http://www.fensifanligou.com/version.txt";

        // InputStream is = null;
        try {
            URL url = new URL(httpUrl);
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            urlConn.connect();
            InputStream input = urlConn.getInputStream();
            InputStreamReader inputreader = new InputStreamReader(input);
            BufferedReader reader = new BufferedReader(inputreader);
            String str = null;
            sb = new StringBuffer();
            Boolean getInSwitch=false;
            while ((str = reader.readLine()) != null) {
                //  sb.append(str).append("\n");
                if(str.indexOf("TimesApp")!=-1)
                {
                    if (str.substring(str.indexOf("/")+1).equals(versionName)) {
                        //  reader.close();
                        //   inputreader.close();
                        //   input.close();
                        //   reader = null;
                        //   inputreader = null;
                        //   input = null;
                        //  System.exit(0);
                        getInSwitch = true;
                    } else {
                        verSend = str.substring(str.indexOf("/")+1);
                    }
                }
            }
            if (getInSwitch){
                reader.close();
                inputreader.close();
                input.close();
                reader = null;
                inputreader = null;
                input = null;
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
            }else{
                reader.close();
                inputreader.close();
                input.close();
                reader = null;
                inputreader = null;
                input = null;
                Message msg = new Message();
                msg.what = 2;
                handler.sendMessage(msg);
            }



            if (sb != null) {
                // bt_view.setText(sb.toString());
                //    System.out.println("sb=" + sb);
                //   Message message = new Message();
                //   message.what = 1;
                //  handler.sendMessage(message);

            } else {
                //bt_view.setText("NULL");
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
            Message msg = new Message();
            msg.what = 3;
            handler.sendMessage(msg);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Message msg = new Message();
            msg.what = 3;
            handler.sendMessage(msg);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "startup Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://g.b.fensifanligou/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "startup Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://g.b.fensifanligou/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
    protected void showImeiCode() {
        AlertDialog.Builder builer = new AlertDialog.Builder(this) ;
        builer.setTitle("机器码");
        builer.setMessage(imei);
        //当点确定按钮时从服务器上下载 新的apk 然后安装
        builer.setPositiveButton("复制", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //  Log.i(TAG,"下载apk,更新");
                ClipboardManager myClipboard;
                myClipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
                ClipData myClip;
                myClip = ClipData.newPlainText("text", imei);
                myClipboard.setPrimaryClip(myClip);
                sendSMS(imei);
                startup.this.finish();
            }
        });
        //当点取消按钮时进行登录
        builer.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                System.exit(0);
            }
        });
        AlertDialog dialog = builer.create();
        dialog.setCancelable(false);
        dialog.show();
    }
    protected void showUpdataDialog() {
        AlertDialog.Builder builer = new AlertDialog.Builder(this) ;
        builer.setTitle("版本升级");
        builer.setMessage("检测到有新版本，按确定键下载升级！！！！");
        //当点确定按钮时从服务器上下载 新的apk 然后安装
        builer.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
              //  Log.i(TAG,"下载apk,更新");
                downLoadApk();
            }
        });
        //当点取消按钮时进行登录
        builer.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                System.exit(0);
            }
        });
        AlertDialog dialog = builer.create();
        dialog.setCancelable(false);
        dialog.show();
    }

    /*
     * 从服务器中下载APK
     */
    protected void downLoadApk() {
        final ProgressDialog pd;    //进度条对话框
        pd = new ProgressDialog(this);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setMessage("正在下载更新");
        pd.show();
        new Thread(){
            @Override
            public void run() {
                try {
                    File file = getFileFromServer("http://www.fensifanligou.com/fensifanligou.apk", pd);
                    sleep(3000);
                    installApk(file);
                    pd.dismiss(); //结束掉进度条对话框
                } catch (Exception e) {
                    Message msg = new Message();
                    msg.what = 4;
                    handler.sendMessage(msg);
                    e.printStackTrace();
                }
            }}.start();
    }

    //安装apk
    protected void installApk(File file) {
        Intent intent = new Intent();
        //执行动作
        intent.setAction(Intent.ACTION_VIEW);
        //执行的数据类型
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        startActivity(intent);
    }
    public static File getFileFromServer(String path, ProgressDialog pd) throws Exception {
        //如果相等的话表示当前的sdcard挂载在手机上并且是可用的
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            URL url = new URL(path);
            HttpURLConnection conn =  (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            //获取到文件的大小
            pd.setMax(conn.getContentLength());
            InputStream is = conn.getInputStream();
            File file = new File(Environment.getExternalStorageDirectory(), "updata.apk");
            FileOutputStream fos = new FileOutputStream(file);
            BufferedInputStream bis = new BufferedInputStream(is);
            byte[] buffer = new byte[1024];
            int len ;
            int total=0;
            while((len =bis.read(buffer))!=-1){
                fos.write(buffer, 0, len);
                total+= len;
                //获取当前下载量
                pd.setProgress(total);
            }
            fos.close();
            bis.close();
            is.close();
            return file;
        }
        else{
            return null;
        }
    }

    private void sendSMS(String smsBody)

    {

        Uri smsToUri = Uri.parse("smsto:18696687887");

        Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);

        intent.putExtra("sms_body", "Machine Code:"+smsBody);

        startActivity(intent);

    }
}

