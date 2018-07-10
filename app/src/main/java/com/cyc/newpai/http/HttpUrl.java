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

    public static final String HTTP_BID_RECORD_AGO_URL = HttpUrl.HTTP_SERVICE_URL + "/Api/Jingpai/dealRecordAgo";//竞拍以往成成交记录

    public static final String HTTP_LUCKY_SHOW_URL = HttpUrl.HTTP_SERVICE_URL + "/Api/JIngpai/lucyShow"; //竞拍商品的幸运晒台记录

    public static final String HTTP_LUCKY_POST_URL = HttpUrl.HTTP_SERVICE_URL + "/Api/JIngpai/lucyPost"; //商品晒单评论提交

    public static final String HTTP_CATE_LIST_URL = HttpUrl.HTTP_SERVICE_URL + "/Api/Jingpai/cateList"; //竞拍分类列表

    public static final String HTTP_GOODS_BY_CATE_ID_URL = HttpUrl.HTTP_SERVICE_URL + "/Api/Jingpai/goodsByCateId"; //竞拍分类下正在竞拍的商品

    public static final String HTTP_BID_SEARCH_HISTORY_URL = HttpUrl.HTTP_SERVICE_URL + "/Api/Jingpai/searchHistory"; //竞拍产品搜素关联记录

    public static final String HTTP_BID_SEARCH_URL = HttpUrl.HTTP_SERVICE_URL + "/Api/Jingpai/search"; //搜索商品

    public static final String HTTP_BID_SEARCH_DELETE_URL = HTTP_SERVICE_URL + "/Api/Jingpai/searchDelete"; //商品搜索记录清除

    /*-------------------------------------------------------------------*/

    /**
     * 个人中心
     */
    public static final String HTTP_REGISTER＿URL = HTTP_SERVICE_URL + "/Api/Member/register";//注册

    public static final String HTTP_LOGIN_URL = HTTP_SERVICE_URL + "/Api/Member/login";//登录

    public static final String HTTP_RESET_PWD_URL = HTTP_SERVICE_URL + "/Api/Member/changePasswd"; // 修改密码

    public static final String HTTP_QUICK_LOGIN_URL = HTTP_SERVICE_URL + "/Api/Member/fastLogin"; //快捷登录

    public static final String HTTP_PROPERTY_URL = HTTP_SERVICE_URL + "/Api/Member/property"; //个人收支中心

    /*-------------------------------------------------------------------*/

    /**
     * 系统通用
     */
    public static final String HTTP_SEND_SMS_URL = HTTP_SERVICE_URL + "/Api/System/send_sms"; //短信验证码发送

    /*-------------------------------------------------------------------*/

}
