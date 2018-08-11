package com.cyc.newpai.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    /**
     * 格式化时间
     * @param longtime
     * @param format
     * @return
     */
    public static String formatDate(long longtime, String format) {
        SimpleDateFormat sDateFormat = new SimpleDateFormat(format);
        return sDateFormat.format(new Date(longtime));
    }
}
