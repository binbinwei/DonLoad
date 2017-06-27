package com.baosight.iplat4mandroid.core.uitls.okHttpDownLoad;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;


import com.baosight.iplat4mandroid.R;

import java.io.File;
import java.io.InputStream;

/**
 * Created by weibinbin on 2017/6/21.
 * 通知栏更新应用
 */

public class UpdateApp {
    // 外存sdcard存放路径
    private static final String FILE_PATH = Environment.getExternalStorageDirectory() +"/" + "BaoWu" +"/";
    public NotificationCompat.Builder mBuilder;
    NotificationManager mNotifyManager;

    /**
     * 入口
     * @param url
     * @param context
     */
    public void showDownLoadProgress(String url,Context context){
        createNotification(context);
        try {
            downLoadFile(url,FILE_PATH,context);
        }catch (Exception io){
            Toast.makeText(context,"下载失败",Toast.LENGTH_SHORT).show();
        }
    }

    public void downLoadFile(String url,String savePath,final Context context){
        DownloadUtil.get().download(url, savePath ,new DownloadUtil.OnDownloadListener() {
            @Override
            public void onDownloadSuccess(File file) {
                mBuilder.setProgress(100,100,false);
                mNotifyManager.notify(0, mBuilder.build());
                mNotifyManager.cancel(0);
                //安装应用
                installApp(context,file);
            }
            @Override
            public void onDownloading(int progress) {
                mBuilder.setProgress(100,progress,false);
                mNotifyManager.notify(0, mBuilder.build());
            }
            @Override
            public void onDownloadFailed() {
                mBuilder.setProgress(0,0,false);
                mNotifyManager.notify(0, mBuilder.build());
                mNotifyManager.cancel(0);
                Toast.makeText(context,"下载失败", Toast.LENGTH_SHORT).show();
            }
        });
    }
    /**
     * 安装新版本应用
     */
    private void installApp(Context context,File file) {
        // 跳转到新版本应用安装页面
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse("file://" + file.toString()), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }



    /**
     * @param url
     * @return
     * 从下载连接中解析出文件名
     */
    private String getNameFromUrl(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }


    public void createNotification(Context context){
        mNotifyManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(context);


        mBuilder.setContentTitle("宝武移动更新")//设置通知栏标题
                .setContentText("更新中..")
                //.setContentIntent(context.getDefalutIntent(Notification.FLAG_AUTO_CANCEL)) //设置通知栏点击意图
                //  .setNumber(number) //设置通知集合的数量
                .setTicker("宝武移动更新通知") //通知首次出现在通知栏，带上升动画效果的
                .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
                .setPriority(Notification.PRIORITY_DEFAULT) //设置该通知优先级
                //  .setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
                .setOngoing(true)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
                .setProgress(100,0,true)
                .setDefaults(Notification.DEFAULT_VIBRATE)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
                //Notification.DEFAULT_ALL  Notification.DEFAULT_SOUND 添加声音 // requires VIBRATE permission
                .setSmallIcon(R.drawable.ic_launcher);//设置通知小ICON

               mNotifyManager.notify(0, mBuilder.build());

    }

    public static String getPackageName(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.packageName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            Log.e("Exception StackTrace:", e.getMessage());
            return null;
        }
    }

    public static Bitmap getIconBitmap(Context context){
        return BitmapFactory.decodeResource(context.getResources(),R.drawable.add);
    }

}
