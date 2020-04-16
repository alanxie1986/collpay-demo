package demo.newpay.service;

import demo.newpay.model.Commodity;
import demo.newpay.util.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CommodityService {

    final static Logger logger = LoggerFactory.getLogger(CommodityService.class);

    private Map<Long, Commodity> data = new HashMap<>();

    @PostConstruct
    public void init(){
        Commodity c1 = new Commodity();
        c1.setName("测试商品1");
        c1.setId(1l);
        c1.setPrice(10l);

        Commodity c2 = new Commodity();
        c2.setName("test2");
        c2.setId(2l);
        c2.setPrice(20l);

        data.put(c1.getId(),c1);
        data.put(c2.getId(),c2);
    }


    public List<Commodity> findAll(){
        List<Commodity> list = new ArrayList<>();
        list.add(data.get(1l));
        list.add(data.get(2l));
        return list;
    }

    public Commodity findById(Long id){
        return data.get(id);
    }

}
