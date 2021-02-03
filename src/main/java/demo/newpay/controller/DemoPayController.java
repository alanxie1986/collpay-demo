package demo.newpay.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import demo.newpay.model.APIData;
import demo.newpay.model.Commodity;
import demo.newpay.model.DemoOrder;
import demo.newpay.model.PayType;
import demo.newpay.service.CommodityService;
import demo.newpay.service.PaymentService;
import demo.newpay.util.MatrixToImageWriter;
import demo.newpay.util.SignHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

import demo.newpay.model.APIData.*;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DemoPayController {

    private final static String RESULT = "result";
    private final static String MSG = "msg";
    @Autowired
    private PaymentService paymentService;

    @Autowired
    private CommodityService commodityService;

    @Value("${newpay24.signkey}")
    private String signKey;

    @Value("${newpay24.merchantcode}")
    private String merchantCode;

    @Autowired
    private ObjectMapper objectMapper;

    @Value(("${newpay24.collpay.api}"))
    private String collpayApiUrl;

    //购买demo首页
    @RequestMapping({"/index.html", "/"})
    public String index(ModelMap modelMap) {
        return "open";
    }

    //下单链接
    @PostMapping("/payment")
    public String payment(Integer payType, Double price, HttpServletResponse response, ModelMap modelMap) {
        PayType payTypeVal = PayType.getByCode(payType);
        if (payTypeVal == null) {
            return setFail("payType 参数错误", modelMap);
        }
        //newpay24平台使用人民币分作为金额单位
        Long amount = new Double(price * 100l).longValue();
        DemoOrder order = new DemoOrder();
        order.setCommodityName("游戏点卡");
        order.setOrderNo(UUID.randomUUID().toString());
        order.setPrice(amount);
        order.setPayType(payType);
        order.setCreateTime(LocalDateTime.now(ZoneId.of("UTC")));
        //生成订单号
        String orderNo = new StringBuilder("DEMO")
                .append(System.currentTimeMillis())
                .append(merchantCode.substring(0, 4).toUpperCase())
                .append(demo.newpay.util.StringUtils.generateRandomString(4)).toString();
        order.setOrderNo(orderNo);
        order.setCreateTime(LocalDateTime.now(ZoneId.of("UTC+8")));
        order.setStatus(DemoOrder.Status.PENDING);
        //保存订单
        paymentService.saveOrder(order);


        switch (payTypeVal) {

            case ALI_WAP: //支付宝跳转页面

            case UNION_WAP: //银联 跳转页面

            case WX_WAP: //微信公众号方式需要提供下面的参数给页面js脚本
                return  getHtmlContent(order,modelMap,response);

            case ALI_NATIVE: //支付宝扫码

            case WX_NATIVE: //微信扫码

            case UNION_QR: //银联扫码

            case PIN_DUO_DUO: //银联扫码

            case JD_QR:

            case WX_PRIVATE:

            case ALI_PRIVATE:

                return setQrCodePage(order, modelMap,response);

        }


        return "orderDetail";
    }

    private String setFail(String reason, ModelMap modelMap) {
        modelMap.addAttribute(RESULT, "FAIL");
        modelMap.addAttribute(MSG, reason);
        return "orderDetail";
    }

    //通过在页面输出一个from 跳转网页支付方式
    private String getHtmlContent(DemoOrder order, ModelMap modelMap, HttpServletResponse response) {
//        response.setContentType("text/html;charset=UTF-8");
//        Map<String, String> paramMap = paymentService.constructParam(order);
//        modelMap.addAttribute("collpayApiUrl",collpayApiUrl);
//        modelMap.addAttribute("paramMap",paramMap);
//        return "jump2pay";
        APIData apiData = paymentService.pay(order);
        if (!apiData.getServer_code().equals(APICode.SUCCESS)) {
            setFail(apiData.getMessage(), modelMap);
        }
        modelMap.addAttribute(RESULT, "SUCCESS");
        modelMap.addAttribute("data", apiData);
        return "jumpToPay";
    }

    private String setQrCodePage(DemoOrder order, ModelMap modelMap, HttpServletResponse response) {
        response.setContentType("text/html;charset=UTF-8");
        APIData apiData = paymentService.pay(order);
        modelMap.addAttribute("payType", order.getPayType() );
        modelMap.addAttribute("data", apiData );
        if (!apiData.getServer_code().equals(APICode.SUCCESS)) {
            setFail(apiData.getMessage(), modelMap);
        }else if(order.getPayType()!=5001){
            modelMap.addAttribute("order", order);
            modelMap.addAttribute("amount", order.getPrice()/100d);
        }
        modelMap.addAttribute(RESULT, "SUCCESS");
        return "qr_code";
    }

    //把字符串链接数据转换成二维码显示
    @RequestMapping("/qrCode")
    public void queryOrders(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String qrCode = req.getParameter("qrCode");
        if (qrCode != null) {
            //            String qrCode =  apiResult.getData().getPay_data();
            resp.setContentType("image/jpg");
            int width = 300; // 二维码图片宽度
            int height = 300; // 二维码图片高度
            String format = "jpg";// 二维码的图片格式

            Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");    // 内容所使用字符集编码

            BitMatrix bitMatrix = new MultiFormatWriter().encode(qrCode, BarcodeFormat.QR_CODE, width, height, hints);
            MatrixToImageWriter.writeToStream(bitMatrix, format, resp.getOutputStream());
        }
    }


    //支付完成后跳回这个支付支付结果显示页
    @RequestMapping("/paySuccess")
    public String resultPage(HttpServletRequest request, ModelMap modelMap) {
        String orderNo = request.getParameter("merchant_no");
        DemoOrder order = paymentService.getDemoByNo(orderNo);
        if (order != null) {
            modelMap.addAttribute("order", order);
            double price = order.getPrice() / 100d;
            modelMap.addAttribute("price", price);
        }

        return "payResult";
    }

    //刷订单状态(被支付结果显示页面调用)
    @RequestMapping("/ajax/status")
    public void status(String orderNo, HttpServletResponse resp) throws IOException {
        DemoOrder order = paymentService.getDemoByNo(orderNo);
        resp.setContentType("text/html;charset=UTF-8");
        resp.getWriter().write(order.getStatus().name());
    }

    //支付记录
    @RequestMapping("/history")
    public String history(@RequestParam(required = false) String orderNo, ModelMap modelMap) {
        HashMap<String,Object> params = new HashMap<>();
        params.put("orderNo",orderNo);
        List<DemoOrder> list = paymentService.list(params);
        modelMap.addAttribute("list",list);
        return "order_list";
    }

    //支付记录
    @RequestMapping("/detail")
    public String detail( String orderNo, ModelMap modelMap) {
        DemoOrder order = paymentService.getDemoByNo(orderNo);
        modelMap.addAttribute("order",order);
        return "order_details";
    }

    //newpay24平台支付结果异步通知(支付结果以该接口为准)
    @PostMapping("/asynCallback")
    public void callback(@RequestBody String paramBody, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html;charset=UTF-8");
        if (StringUtils.isEmpty(paramBody)) {
            resp.getWriter().println("FAIL");
            return;
        }

        Map<String, String> params = objectMapper.readValue(paramBody, new TypeReference<Map<String, String>>() {
        });

        //验签
        String sign = params.get("sign");
        String caculateSign = SignHelper.getMd5Sign(params, signKey);
        if (!sign.equals(caculateSign)) {
            resp.getWriter().println("FAIL");
            return;
        }

        try {
            //更新订单状态
            String orderNo = params.get("merchant_no"); //商户系统订单
            String status = params.get("trade_status"); //支付状态结果
            String newpay24No = params.get("newpay24_no"); //上游newpay24的订单号
            DemoOrder order = paymentService.getDemoByNo(orderNo);
            switch (status) {
                case "COMPLETE":
                    order.setStatus(DemoOrder.Status.COMPLETE);
                    order.setNewpayNo(newpay24No);
                    order.setEndTime(LocalDateTime.now(ZoneId.of("UTC+8")));
                    break;
                case "FAIL":
                    order.setStatus(DemoOrder.Status.FAIL);
                    order.setNewpayNo(newpay24No);
                    order.setEndTime(LocalDateTime.now(ZoneId.of("UTC+8")));
                    break;
            }
            paymentService.updateOrder(order);
        } catch (Exception e) {
            resp.getWriter().println("FAIL");
            return;
        }

        resp.getWriter().println("SUCCESS");
    }

}
