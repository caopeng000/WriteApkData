package com.writeapkdata;

import com.writeapkdatasdk.core.WriteApkDataBean;
import com.writeapkdatasdk.core.WriteApkDataUtil;

/******************************************
 * 类名称：WriteApk
 * 类描述：
 *
 * @version: 2.3.1
 * @author: caopeng
 * @time: 2017/4/21 16:12
 ******************************************/
public class WriteApk {
    private static final String APK_OUTPUT_PATH = "";
    private static final String APK_NAME = "";

    private static String mChannel = "1244";

    //修改VM OPTION
    public static void main(String[] args) {

        WriteApkDataBean writeApkDataBean = new WriteApkDataBean();
        writeApkDataBean.sourceApkPath = APK_OUTPUT_PATH;
        writeApkDataBean.soureAPkName = APK_NAME;
        writeApkDataBean.mChannel = mChannel;

        WriteApkDataUtil.writeDataToApk(writeApkDataBean);
    }

}
