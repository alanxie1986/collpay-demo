package demo.newpay.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

//订单实体类
public class DemoOrder {

    public static enum Status {
        PENDING,
        COMPLETE,
        FAIL,
    }


    private Long id;  //订单数据库id
    private String commodityName;  //商品名
    private Integer payType; //支付方式
    private String orderNo; //订单号
    private String newpayNo; //newpay24平台订单号
    private Status status = Status.PENDING; //订单状态
    private Long price; // 金额 (单位 人民币:分)
    private LocalDateTime createTime; //订单创建时间
    private LocalDateTime endTime; //订单完结时间

    private String payerName; // 银行卡方式必填

    private String priceDisplay;
    private String createTimeDisplay;
    private String endTimeDisplay;
    private String payTypeDisplay;

    private final static DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCommodityName() {
        return commodityName;
    }

    public void setCommodityName(String commodityName) {
        this.commodityName = commodityName;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getNewpayNo() {
        return newpayNo;
    }

    public void setNewpayNo(String newpayNo) {
        this.newpayNo = newpayNo;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getPriceDisplay() {
        if(this.price!=null){
            return String.valueOf(this.price/100d);
        }
        return "-";
    }

    public String getCreateTimeDisplay() {
        if(this.createTime!=null){
           return DATE_TIME_FORMATTER.format(this.createTime);
        }
        return "-";
    }

    public String getEndTimeDisplay() {
        if(this.endTime!=null){
            return DATE_TIME_FORMATTER.format(this.endTime);
        }
        return "-";
    }

    public String getPayTypeDisplay() {
        if(this.payType!=null){
           PayType payType = PayType.getByCode(this.payType);
           return  payType.getName();
        }
        return "-";
    }

    public String getPayerName() {
        return payerName;
    }

    public void setPayerName(String payerName) {
        this.payerName = payerName;
    }
}
