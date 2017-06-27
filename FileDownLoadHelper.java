package com.baosight.iplat4mandroid.core.uitls.okHttpDownLoad;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;
import java.io.File;

/**
 * Created by weibinbin on 2017/6/6.
 */

public class FileDownLoadHelper {
    // 外存sdcard存放路径
    private static final String FILE_PATH = Environment.getExternalStorageDirectory() +"/" + "BaoWu" +"/";
    // 下载应用存放全路径
    private static  String FILE_NAME ="";
    // 下载应用的进度条
    private ProgressDialog progressDialog;

    public void showDownLoadProgress(String url,Context context){
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
                progressDialog.dismiss();//关闭进度条
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


}

//Dialog中确定按钮的监听器
class SureButtonListener implements android.content.DialogInterface.OnClickListener{

    public void onClick(DialogInterface dialog, int which) {
        //点击“确定按钮”取消对话框
        DownloadUtil.get().cancelDownLoad();
        dialog.cancel();
    }

}
