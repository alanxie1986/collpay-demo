package demo.newpay.repository;

import demo.newpay.model.DemoOrder;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Mapper
@Repository
public interface DemoOrderMapper {

    List<DemoOrder> findAll(HashMap<String,Object> params);

    DemoOrder findById(Long id);

    DemoOrder findByNo(String orderNo);

    void save(DemoOrder order);

    void update(DemoOrder order);

}
