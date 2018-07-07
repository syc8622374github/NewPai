package com.cyc.newpai.http;

public class HttpUrl {
    public static final String UID = "11004";
    public static final String TOKEN = "75dad8287f233e4dd3a93ee16d7ebfcf";


    /**
     * 服务器地址
     */
    public static final String HTTP_SERVICE_URL = "http://pai.aimeichuang.cn";

    /*-------------------------------------------------------------------*/

    /**
     * 其他
     */
    public static final String HTTP_BANNER_URL = HttpUrl.HTTP_SERVICE_URL + "/Api/System/banner"; //banner

    public static final String HTTP_HEADLINE_URL = HttpUrl.HTTP_SERVICE_URL + "/Api/System/toutiao";//头条

    /*-------------------------------------------------------------------*/

    /**
     * 竞拍模块接口
     */
    public static final String HTTP_INDEX_URL = HttpUrl.HTTP_SERVICE_URL + "/Api/Jingpai/index";// 竞拍首页

    public static final String HTTP_SHOP_DETAIL_URL = HttpUrl.HTTP_SERVICE_URL + "/Api/Jingpai/detail";//商品详情

    public static final String HTTP_BID_URL = HttpUrl.HTTP_SERVICE_URL + "/Api/jingpai/bid";//竞拍出价

    public static final String HTTP_BID_RECORD_URL = HttpUrl.HTTP_SERVICE_URL + "/Api/Jingpai/bidRecord";//竞拍出价记录

    public static final String HTTP_BID_RECORD_AGO = HttpUrl.HTTP_SERVICE_URL + "/Api/Jingpai/dealRecordAgo";//竞拍以往成成交记录

    /*-------------------------------------------------------------------*/

    /**
     * 个人中心
     */
    public static final String HTTP_REGISTER＿URL = HTTP_SERVICE_URL + "/Api/Member/register";//注册

    public static final String HTTP_LOGIN_URL = HTTP_SERVICE_URL + "/Api/Member/login";//登录

    public static final String HTTP_RESET_PWD_URL = HTTP_SERVICE_URL + "/Api/Member/changePasswd"; // 修改密码

    public static final String HTTP_QUICK_LOGIN_URL = HTTP_SERVICE_URL + "/Api/Member/fastLogin"; //快捷登录

    /*-------------------------------------------------------------------*/

    /**
     * 系统通用
     */
    public static final String HTTP_SEND_SMS_URL = HTTP_SERVICE_URL + "/Api/System/send_sms"; //短信验证码发送

    /*-------------------------------------------------------------------*/

}
