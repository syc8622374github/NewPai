package com.cyc.newpai.http;

public class HttpUrl {
    public static final String UID = "11004";
    public static final String TOKEN = "75dad8287f233e4dd3a93ee16d7ebfcf";


    /**
     * 服务器地址
     */
    public static final String HTTP_SERVICE_URL = "http://app.zhideting.cn";

    /*-------------------------------------------------------------------*/

    /**
     * 其他
     */
    public static final String HTTP_BANNER_URL = HttpUrl.HTTP_SERVICE_URL + "/Api/System/banner"; //banner

    public static final String HTTP_HEADLINE_URL = HttpUrl.HTTP_SERVICE_URL + "/Api/System/toutiao";//头条

    public static final String HTTP_NEW_DEAL = HttpUrl.HTTP_SERVICE_URL + "/Api/System/newDeal";//最新成交

    public static final String HTTP_SUGGESTION_URL = HttpUrl.HTTP_SERVICE_URL + "/Api/Member/feedbackAdd";//反馈意见

    public static final String HTTP_RECHARGE_AMOUNT_URL = HttpUrl.HTTP_SERVICE_URL + "/Api/System/chargeConfig"; //充值金额

    /*-------------------------------------------------------------------*/

    /**
     * 竞拍模块接口
     */
    public static final String HTTP_INDEX_URL = HttpUrl.HTTP_SERVICE_URL + "/Api/Jingpai/index";// 竞拍首页

    public static final String HTTP_SHOP_DETAIL_URL = HttpUrl.HTTP_SERVICE_URL + "/Api/Jingpai/detail";//商品详情

    public static final String HTTP_BID_URL = HttpUrl.HTTP_SERVICE_URL + "/Api/jingpai/bid";//竞拍出价

    public static final String HTTP_BID_RECORD_URL = HttpUrl.HTTP_SERVICE_URL + "/Api/Jingpai/bidRecord";//竞拍出价记录

    public static final String HTTP_BID_RECORD_AGO_URL = HttpUrl.HTTP_SERVICE_URL + "/Api/Jingpai/dealRecordAgo";//竞拍以往成成交记录

    public static final String HTTP_LUCKY_SHOW_URL = HttpUrl.HTTP_SERVICE_URL + "/Api/Jingpai/lucyShow"; //竞拍商品的幸运晒台记录

    public static final String HTTP_LUCKY_POST_URL = HttpUrl.HTTP_SERVICE_URL + "/Api/Jingpai/lucyPost"; //商品晒单评论提交

    public static final String HTTP_CATE_LIST_URL = HttpUrl.HTTP_SERVICE_URL + "/Api/Jingpai/cateList"; //竞拍分类列表

    public static final String HTTP_GOODS_BY_CATE_ID_URL = HttpUrl.HTTP_SERVICE_URL + "/Api/Jingpai/goodsByCateId"; //竞拍分类下正在竞拍的商品

    public static final String HTTP_BID_SEARCH_HISTORY_URL = HttpUrl.HTTP_SERVICE_URL + "/Api/Jingpai/searchHistory"; //竞拍产品搜素关联记录

    public static final String HTTP_BID_SEARCH_URL = HttpUrl.HTTP_SERVICE_URL + "/Api/Jingpai/search"; //搜索商品

    public static final String HTTP_BID_SEARCH_DELETE_URL = HTTP_SERVICE_URL + "/Api/Jingpai/searchDelete"; //商品搜索记录清除

    public static final String HTTP_MAIN_LUCKY_SHOW_URL = HttpUrl.HTTP_SERVICE_URL + "/Api/Jingpai/lucyList"; //首页幸运晒单

    /*-------------------------------------------------------------------*/

    /**
     * 个人中心
     */
    public static final String HTTP_REGISTER＿URL = HTTP_SERVICE_URL + "/Api/Member/register";//注册

    public static final String HTTP_LOGIN_URL = HTTP_SERVICE_URL + "/Api/Member/login";//登录

    public static final String HTTP_RESET_PWD_URL = HTTP_SERVICE_URL + "/Api/Member/changePasswd"; // 修改密码

    public static final String HTTP_QUICK_LOGIN_URL = HTTP_SERVICE_URL + "/Api/Member/fastLogin"; //快捷登录

    public static final String HTTP_PROPERTY_URL = HTTP_SERVICE_URL + "/Api/Member/property"; //个人收支中心

    public static final String HTTP_AUCTION_URL = HTTP_SERVICE_URL + "/Api/Member/myAuction"; //个人竞拍记录

    public static final String HTTP_EDIT_ADDRESS_URL = HTTP_SERVICE_URL + "/Api/Member/addressUpdate"; //收货地址编辑

    public static final String HTTP_ADDRESS_LIST_URL = HTTP_SERVICE_URL + "/Api/Member/addressList"; //收货地址列表

    public static final String HTTP_USER_INFO_URL = HTTP_SERVICE_URL + "/Api/member/userInfo"; //用户详情

    public static final String HTTP_MY_LUCKY_LIST = HTTP_SERVICE_URL + "/Api/Member/myLucyList"; //我的幸运晒单记录

    public static final String HTTP_PAY_SUCESS_RECOMMEND_GOODS_LIST = HTTP_SERVICE_URL + "/Api/Jingpai/tuijian"; //支付成功推荐

    public static final String HTTP_PAY_SUCCESS_SHOP_DETAIL_URL = HTTP_SERVICE_URL + "/Api/Member/auctionOrder"; //竞拍成功商品详情

    public static final String HTTP_PAY_SUBMIT_ORDER_URL = HTTP_SERVICE_URL + "/Api/Jingpai/createOrder"; //竞拍订单支付发起

    /*-------------------------------------------------------------------*/

    /**
     * 系统通用
     */
    public static final String HTTP_SEND_SMS_URL = HTTP_SERVICE_URL + "/Api/System/send_sms"; //短信验证码发送

    /**
     * 支付
     */
    public static final String HTTP_PAY_REQUEST_URL = "https://pay.bbbapi.com/?format=json"; // 支付发起接口

    public static final String HTTP_PAY_PAGE_QEQUEST_URL = "https://pay.bbbapi.com/"; //支付页面

    public static final String HTTP_RECHATGE_URL = HTTP_SERVICE_URL + "/Api/System/payCharge"; //充值

    public static final String HTTP_RECHARGE_STATUS_URL = HTTP_SERVICE_URL + "/Api/System/chargeCheckOrder"; //充值状态

    /*-------------------------------------------------------------------*/

}
