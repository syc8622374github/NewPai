package com.cyc.newpai;

import com.cyc.newpai.framework.BasicApp;
import com.cyc.newpai.util.Constant;
import com.cyc.newpai.util.SharePreUtil;
import com.tencent.bugly.crashreport.CrashReport;

public class App extends BasicApp {

    @Override
    public void onCreate() {
        super.onCreate();
        CrashReport.initCrashReport(getApplicationContext());
        CrashReport.setUserId(SharePreUtil.getPref(getApplicationContext(),Constant.UID,"defaultId"));
    }
}
