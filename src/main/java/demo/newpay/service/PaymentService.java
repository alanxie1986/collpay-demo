package demo.newpay.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import demo.newpay.model.APIData;
import demo.newpay.model.DemoOrder;
import demo.newpay.model.DemoOrder.Status;
import demo.newpay.repository.DemoOrderMapper;
import demo.newpay.util.HttpUtil;
import demo.newpay.util.SignHelper;
import demo.newpay.util.StringUtils;
import org.apache.http.Header;
import org.apache.http.client.methods.RequestBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class PaymentService {

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${newpay24.merchantcode}")
    private String merchantCode;

    @Value("${newpay24.signkey}")
    private String signKey;

    @Value("${demo.return.url}")
    private String returnUrl;

    @Value("${demo.backend.url}")
    private String backendUrl;

    @Value(("${newpay24.collpay.api}"))
    private String collpayApiUrl;

    @Autowired
    private DemoOrderMapper dao;

    public APIData pay(DemoOrder order)   {

        String apiResult = null;
        APIData apiData = new APIData();
        try {
            apiResult = postCollpay(order);
            apiData = objectMapper.readValue(apiResult,APIData.class);
        } catch (IllegalAccessException e) {
             apiData.setServer_code(APIData.APICode.SYSTEM_ERROR);
             apiData.setMessage(e.getMessage());
        } catch (IOException e){
            apiData.setServer_code(APIData.APICode.SYSTEM_ERROR);
            apiData.setMessage("接口调用失败");
        }
        return apiData;
    }

    //构造页面请求参数
    public Map<String,String> constructParam(DemoOrder order){
        Map<String, String> paramMap = new HashMap<>();
        //构造接口参数
        paramMap.put("code", merchantCode);
        paramMap.put("amount",String.valueOf(order.getPrice()));
        paramMap.put("commodity_name", order.getCommodityName());
        paramMap.put("merchant_no", order.getOrderNo());
        paramMap.put("pay_type", String.valueOf(order.getPayType()));
        paramMap.put("return_url", returnUrl);
        paramMap.put("back_end_url", backendUrl);
        paramMap.put("nonce_str", SignHelper.nonceStr());
        String sign = SignHelper.getMd5Sign(paramMap, signKey);
        paramMap.put("sign", sign);
        return paramMap;
    }

    //请求代收下单接口
    private String postCollpay(DemoOrder order) throws IllegalAccessException, IOException {
        Map<String, String> paramMap = constructParam(order);

        String postData = objectMapper.writeValueAsString(paramMap);
        String response = HttpUtil.postData(collpayApiUrl,postData);
        if(!response.startsWith("{") && response.endsWith("}")){
            throw new IllegalAccessException("接口调用失败");
        }
        //对结果进行验签
        Map<String,String> responseMap = objectMapper.readValue(response, new TypeReference<Map<String, String>>() {});
        String responseSign = responseMap.get("sign");
        String sign = SignHelper.getMd5Sign(responseMap, signKey);
        if(!sign.equals(responseSign)){
            throw new IllegalAccessException("接口返回签名验证不通过");
        }
        return response;
    }


    //使用订单号查询订单
    public DemoOrder getDemoByNo(String orderNo){
        return dao.findByNo(orderNo);
    }

    @Transactional
    public void updateOrder(DemoOrder order) {
        dao.update(order);
    }

    @Transactional
    public void saveOrder(DemoOrder order) {
        dao.save(order);
    }

    public List<DemoOrder> list(HashMap<String,Object> params){
      return   dao.findAll(params);
    }
}
