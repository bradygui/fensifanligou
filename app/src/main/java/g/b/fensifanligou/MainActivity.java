package g.b.fensifanligou;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import android.app.AlertDialog;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private WebView webview;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        webview = (WebView) findViewById(R.id.webView);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webview.getSettings().setSupportZoom(true);
        webview.getSettings().setBuiltInZoomControls(true);//support zoom
        webview.getSettings().setUseWideViewPort(true);
        webview.getSettings().setLoadWithOverviewMode(true);
        webview.getSettings().setSupportMultipleWindows(false);
        webview.loadUrl("http://www.fensifanligou.com");

        webview.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                if (url.contains("http://")||url.contains("https://")) {


                    webview.loadUrl(url);
                    return false;
                } else {

                       /*  new AlertDialog.Builder(MainActivity.this).setTitle("系统提示")//设置对话框标题

                            .setMessage(url)//设置显示的内容

                            .setPositiveButton("确定",new DialogInterface.OnClickListener() {//添加确定按钮



                                @Override

                                public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件

                                    // TODO Auto-generated method stub

                                    finish();

                                }

                            }).setNegativeButton("返回",new DialogInterface.OnClickListener() {//添加返回按钮



                        @Override

                        public void onClick(DialogInterface dialog, int which) {//响应事件

                            // TODO Auto-generated method stub

                            Log.i("alertdialog"," 请保存数据！");

                        }

                    }).show();//在按键响应事件中显示此对话框*/
                    if (url.contains("tbopen://") && checkPackage("com.taobao.taobao")) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(intent);
                    }
                    return true;
                }
                // return super.shouldOverrideUrlLoading(view,url);
            }


            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }

            @Override
            public void onPageFinished(WebView view, String url)
            {
                super.onPageFinished(view, url);
                // webview.loadUrl("javascript:window.location = \"weixin://item.taobao.com/item.htm?id=41700658839\"; ");

                //   Intent intent = new Intent();
                //    intent.setAction("android.intent.action.VIEW");

                //    String url2 = "https://uland.taobao.com/coupon/edetail?e=EGi%2Bnsuahc8GQASttHIRqSNdlzmaE3%2FGRMenv0eIfVwzzQbliShzjxxypvCpTTzIRRaWPN0dnjZgUjG4G%2FCiApQ5wfGz%2Fu%2BN1fm3aBfYuJJFZsD%2BskSig3vlGh6jLO1J&traceId=0bface9e15136022787802139e&activityId=53f29c617fc446eda583f929a9fad8e0";
                //     Uri uri = Uri.parse(url2);
                //   intent.setData(uri);
                //     startActivity(intent);
                //     Intent intent = new Intent();
                //   intent.setAction("android.intent.action.VIEW");
                //    Uri content_url = Uri.parse("taobao:uland.taobao.com/coupon/edetail?e=EGi%2Bnsuahc8GQASttHIRqSNdlzmaE3%2FGRMenv0eIfVwzzQbliShzjxxypvCpTTzIRRaWPN0dnjZgUjG4G%2FCiApQ5wfGz%2Fu%2BN1fm3aBfYuJJFZsD%2BskSig3vlGh6jLO1J&traceId=0bface9e15136022787802139e&activityId=53f29c617fc446eda583f929a9fad8e0");
                //   intent.setData(content_url);
                //    intent.setClassName("com.taobao.taobao", "com.taobao.tao.detail.activity.DetailActivity");
                //     startActivity(intent);


            }
        });


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webview.canGoBack()) {
                webview.goBack();//返回上一页面
                return true;
            } else {
                showDialog("","确定退出吗？");
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
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
                "Main Page", // TODO: Define a title for the content shown.
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
    final Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    Intent viewIntent = new Intent("android.intent.action.VIEW",Uri.parse("http://weixin.qq.com/"));
                    startActivity(viewIntent);
                    break;
                case 2:

                    break;

            }
        }

    };
    final Thread thread = new Thread(new Runnable(){

        @Override
        public void run() {

            while(true){
                try {
                    Thread.sleep(100);
                    Message message = new Message();
                    message.what = 1;
                    handler.sendMessage(message);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

    });
    protected void showDialog(String title,String msg) {
        AlertDialog.Builder builer = new AlertDialog.Builder(this) ;
        builer.setTitle(title);
        builer.setMessage(msg);
        //当点确定按钮时从服务器上下载 新的apk 然后安装
        builer.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //  Log.i(TAG,"下载apk,更新");
                System.exit(0);
            }
        });
        //当点取消按钮时进行登录
        builer.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub

            }
        });
        AlertDialog dialog = builer.create();
        dialog.setCancelable(false);
        dialog.show();
    }
    public boolean checkPackage(String packageName)

    {

        if (packageName == null || "".equals(packageName))

            return false;

        try

        {

            MainActivity.this.getPackageManager().getApplicationInfo(packageName, PackageManager.GET_META_DATA);

            return true;

        }

        catch (PackageManager.NameNotFoundException e)

        {

            return false;

        }

    }
}
