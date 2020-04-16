package demo.newpay.model;

/**
 * 支付方式参数定义
 */
public enum  PayType {

    WX_NATIVE(1001,"微信NATIVE"),
    WX_WAP(1002,"微信WAP"),
    ALI_NATIVE(1003,"支付宝NATIVE"),
    ALI_WAP(1004,"支付宝WAP"),
    JD_QR(3001,"京东扫码"),
    JD_WAP(3004,"京东在线"),
    UNION_QR(3002,"银联扫码"),
    UNION_WAP(3003,"银联网关"),
    ;

    private Integer code;
    private String name;

    PayType(Integer code, String name){
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public static PayType getByCode(Integer code ) {
        for (PayType payType : PayType.values()) {
            if (payType.getCode().equals(code)) {

                return payType;
            }
        }
        return null;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
