package com.writeapkdata;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.zip.ZipFile;

import static com.writeapkdata.FileUtil.getSdCardPath;
import static com.writeapkdata.FileUtil.short2Stream;

public class MainActivity extends Activity {
    private EditText et_channel, et_write_apk_name;
    private Button button;
    private TextView tv_name;

    private String apkPath = "";
    private String etApkName;
    private String mChannelName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et_channel = (EditText) findViewById(R.id.et_channel);
        et_write_apk_name = (EditText) findViewById(R.id.et_write_apk_name);
        button = (Button) findViewById(R.id.btn_submit);
        tv_name = (TextView) findViewById(R.id.tv_name);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mChannelName = et_channel.getText().toString();
                if (mChannelName.equals("")) {
                    Toast.makeText(MainActivity.this, "请填写渠道号", Toast.LENGTH_LONG).show();
                    return;
                }
                etApkName = et_write_apk_name.getText().toString();
                if (etApkName.equals("")) {
                    Toast.makeText(MainActivity.this, "请填写APK名称(默认放到SD卡根目录)", Toast.LENGTH_LONG).show();
                    return;
                }
                tv_name.setText(mChannelName + "-" + etApkName);
                apkPath = getSdCardPath() + "/" + etApkName;
                final File file = new File(apkPath);
                if (file.exists()) {
                    Log.i("CAO", "文件路径" + apkPath);
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            copyAndWriteApk(file);
                        }
                    });
                } else {
                    Log.i("CAO", "文件不存在" + apkPath);
                    Toast.makeText(MainActivity.this, "APK文件不存在", Toast.LENGTH_LONG).show();
                }

            }
        });
    }


    public void copyAndWriteApk(File source) {
        String destPath = getSdCardPath() + "/" + "source" + etApkName;
        File dest = new File(destPath);
        try {
            FileUtil.copyFileUsingFileStreams(source, dest);
            Toast.makeText(MainActivity.this, "拷贝APK文件成功", Toast.LENGTH_LONG).show();
            writeApk(dest, mChannelName);
        } catch (IOException e) {
            Toast.makeText(MainActivity.this, "拷贝APK文件失败", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void writeApk(File file, String comment) {
        ZipFile zipFile = null;
        ByteArrayOutputStream outputStream = null;
        RandomAccessFile accessFile = null;
        try {
            zipFile = new ZipFile(file);
            String zipComment = zipFile.getComment();
            if (zipComment != null) {
                Toast.makeText(MainActivity.this, "该应用已经写入渠道号", Toast.LENGTH_LONG).show();
                return;
            }
            byte[] byteComment = comment.getBytes();
            outputStream = new ByteArrayOutputStream();
            outputStream.write(byteComment);
            outputStream.write(short2Stream((short) byteComment.length));
            byte[] data = outputStream.toByteArray();
            accessFile = new RandomAccessFile(file, "rw");
            accessFile.seek(file.length() - 2);
            accessFile.write(short2Stream((short) data.length));
            accessFile.write(data);
            String renameApkPath = getSdCardPath() + "/" + comment + "-" + etApkName;
            File file1 = new File(renameApkPath);
            file.renameTo(file1);
            Toast.makeText(MainActivity.this, "写入渠道成功", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.this, "写入渠道失败", Toast.LENGTH_LONG).show();
        } finally {
            FileUtil.closeStream(zipFile);
            FileUtil.closeStream(outputStream);
            FileUtil.closeStream(accessFile);

        }
    }


}
