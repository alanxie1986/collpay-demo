package demo.newpay.model;

/**
 * 封装newpay24接口返回的数据
 */
public class APIData {

    private APICode server_code; //接口执行结果
    private String message = ""; //错误信息
    private String nonce_str; //随机字符串(加密签名用)
    private String sign; //接口签名

    private String pay_data; //支付链接
    private String order_no; //本支付平台订单号
    private String merchant_no; //商家订单号
    private String commodity_name; //商品名称
    private Long amount; //支付金额
    private Long charge_fee; //本次交易手续费


    //私人银行转账才返回
    private String pay_user_name; //私人银行转账 付款人名称

    public String getPay_data() {
        return pay_data;
    }

    public void setPay_data(String pay_data) {
        this.pay_data = pay_data;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public String getMerchant_no() {
        return merchant_no;
    }

    public void setMerchant_no(String merchant_no) {
        this.merchant_no = merchant_no;
    }

    public String getCommodity_name() {
        return commodity_name;
    }

    public void setCommodity_name(String commodity_name) {
        this.commodity_name = commodity_name;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Long getCharge_fee() {
        return charge_fee;
    }

    public void setCharge_fee(Long charge_fee) {
        this.charge_fee = charge_fee;
    }

    public String getPay_user_name() {
        return pay_user_name;
    }

    public void setPay_user_name(String pay_user_name) {
        this.pay_user_name = pay_user_name;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public APICode getServer_code() {
        return server_code;
    }

    public void setServer_code(APICode server_code) {
        this.server_code = server_code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getNonce_str() {
        return nonce_str;
    }

    public void setNonce_str(String nonce_str) {
        this.nonce_str = nonce_str;
    }

    public static enum APICode {
        SUCCESS, //接口成功
        SYSTEM_BUSY, //系统繁忙

        SYSTEM_ERROR, //接口返回错误
        SIGN_INVALID, //检查签名失败

        PARAM_ERROR, //参数错误
        ORDER_PAID, //订单已支付

        NO_CHANNEL, //没有适用的渠道
        ORDER_NOT_EXIST, //订单不存在

        AUTH_CODE_EXPIRE, //二维码已过期，请用户在微信上刷新后再试
        NOT_ENOUGH, //余额不足
        NOTSUPORTCARD, //不支持卡类型
        ORDER_CLOSED, //订单已关闭
        ORDER_REVERSED, //订单已撤销
        BANK_ERROR, //银行系统异常
        USER_PAYING, //用户支付中，需要输入密码
        AUTH_CODE_ERROR, //授权码参数错误
        AUTH_CODE_INVALID, //授权码检验错误
        LACK_PARAMS, //缺少参数
        NOT_UTF8, //编码格式错误
        BUYER_MISMATCH, //支付帐号错误
        OUT_TRADE_NO_USED, //商户订单号重复
        TRADE_FAIL, //交易错误

    }
}
