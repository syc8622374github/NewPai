package com.cyc.newpai.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NumUtil {
    private static final String PHONE_REG = "^[1][3-9][0-9]{9}$";

    public static boolean isPhoneNum(String phone){
        Pattern pattern = Pattern.compile(PHONE_REG);
        Matcher matcher = pattern.matcher(phone);
        if(matcher.matches()){
            return true;
        }
        return false;
    }
}
