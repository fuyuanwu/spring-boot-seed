package com.weihui.finance.resource;

import com.weihui.finance.domain.City;
import com.weihui.finance.service.TestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// Spring Restful MVC Controller的标识, 直接输出内容，不调用template引擎.
@RestController
public class AccountEndPoint {
    private static Logger logger = LoggerFactory.getLogger(AccountEndPoint.class);

    @Autowired
    TestService testService;

    @Value("${logging.config}")
    String logPath;

    @RequestMapping(value = "/api/accounts/login")
    public Object login(@RequestParam("email") String email, @RequestParam("password") String password) {

        City city = testService.testQueryWithCache(1L);
        Map<Integer, City> map = testService.testQuery4Map();
        testService.testHystrix();


        City temp;
        List<City> cityList = new ArrayList<City>();
        for (int i = 0; i < 2; i++) {
            temp = new City();
            temp.setName("批量测试");
            temp.setState("批量测试");
            temp.setCountry("批量测试");
            cityList.add(temp);
        }
        int insertCount = testService.insertBatch(cityList);
        logger.debug("insertCount: {}", insertCount);

        city.setId(2L);
        city.setState("ERROR");
        int updateCount = testService.testUpdateWithTransactional(city);
        logger.debug("updateCount: {}", updateCount);

        return city;
    }

}
