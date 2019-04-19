# DonLoad

download
使用步骤：

 1：   android studio gradle配置:
       compile 'com.squareup.okhttp3:okhttp:3.4.1'
       compile 'com.squareup.okio:okio:1.8.0'
       
 2： 引用三个文件
 
 3：
    （1）弹窗进度条更新  new FileDownLoadHelper().showDownLoadProgress(url,context) 
    
    （2）消息提醒窗口更新  new UpdateApp().showDownLoadProgress(url,context)
