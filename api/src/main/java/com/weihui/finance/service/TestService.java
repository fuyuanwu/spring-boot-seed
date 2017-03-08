package com.weihui.finance.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.weihui.basis.inf.ucs.support.annotation.CacheResult;
import com.weihui.finance.domain.City;
import com.weihui.finance.exception.ErrorCode;
import com.weihui.finance.exception.ServiceException;
import com.weihui.finance.mapper.CityMapper;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by fuyuanwu on 2017/3/2.
 */
@Service
public class TestService {
    private static Logger logger = LoggerFactory.getLogger(TestService.class);

    @Resource
    private CityMapper cityMapper;


    @CacheResult(namespace = "TEST", expireSecond = 60 * 60)
    public City testQueryWithCache(Long id) {
        logger.debug("这里应该有缓存，不会被重复执行");

        City city = cityMapper.findById(id);
        return city;
    }

    public Map<Integer, City> testQuery4Map() {
        Map<Integer, City> map = cityMapper.findPage4Map(new RowBounds(0, 10));
        return map;
    }

    public Integer insertBatch(List<City> cityList) {
        return cityMapper.insertCities(cityList);
    }


    @Transactional
    public int testUpdateWithTransactional(City city) {
        int count = cityMapper.update(city);

        if (city.getId() == 2) {
            throw new ServiceException("出错啦", ErrorCode.FORBIDDEN);
        }

        return count;
    }

    public void testUpdateWithCache(City city) {

    }


    @HystrixCommand(groupKey = "UserGroup", commandKey = "GetAddressCommand", fallbackMethod = "testHystrixFallback")
    public void testHystrix() {
        throw new ServiceException("出错啦", ErrorCode.FORBIDDEN);
    }

    // fallbackMethod 的 入参、返回值 必须要一致。
    public void testHystrixFallback() {
        System.out.println("这是 testHystrix() 的 fallbackMethod。");
    }
}
