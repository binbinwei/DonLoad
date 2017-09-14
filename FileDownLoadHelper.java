package com.baosight.iplat4mandroid.core.uitls.okHttpDownLoad;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;
import java.io.File;
/**
 * Created by weibinbin on 2017/6/6.
 *
 */
public class FileDownLoadHelper {
    // 外存sdcard存放路径
    private static final String FILE_PATH = Environment.getExternalStorageDirectory() +"/" + "BaoWu" +"/";
    // 下载应用存放全路径
    private static  String FILE_NAME ="";
    // 下载应用的进度条
    private ProgressDialog progressDialog;
    private static FileDownLoadHelper fileDownLoadHelper;
    private Context context;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            progressDialog.dismiss();//关闭进度条
            Toast.makeText(context,"下载失败", Toast.LENGTH_SHORT).show();
        }
    };
    public static FileDownLoadHelper getDownLoadInstance(){
        if(fileDownLoadHelper == null){
            fileDownLoadHelper = new FileDownLoadHelper();
        }
        return fileDownLoadHelper;
    }
    public void showDownLoadProgress(String url,Context context){
        this.context = context;
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("正在下载...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        //设置ProgressDialog 的一个Button
        progressDialog.setButton("取消下载", new SureButtonListener());
        try {
            downLoadFile(url,FILE_PATH,context);
        }catch (Exception io){
            Toast.makeText(context,"创建文件下载路径失败",Toast.LENGTH_SHORT).show();
        }
    }
    public void downLoadFile(String url,String savePath,final Context context){
        progressDialog.show();
        DownloadUtil.get().download(url, savePath ,new DownloadUtil.OnDownloadListener() {
            @Override
            public void onDownloadSuccess(File file) {
                progressDialog.dismiss();//关闭进度条
                //安装应用
                installApp(context,file);
            }
            @Override
            public void onDownloading(int progress) {
                progressDialog.setProgress(progress);
            }
            @Override
            public void onDownloadFailed() {
              handler.sendEmptyMessage(1);
            }
        });
    }
    /**
     * 安装新版本应用
     */
    private void installApp(Context context,File file) {
        // 跳转到新版本应用安装页面
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//android 4.0之前可不加 4.0之后不加会出现安装成功后没有打开界面
        intent.setDataAndType(Uri.parse("file://" + file.toString()), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }
}

/**
 * 取消下载
 */
class SureButtonListener implements DialogInterface.OnClickListener{
    public void onClick(DialogInterface dialog, int which) {
        DownloadUtil.get().cancelDownLoad();
        dialog.cancel();
    }
}
