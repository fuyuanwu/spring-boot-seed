package com.weihui.finance.configure;

import com.netflix.hystrix.contrib.javanica.aop.aspectj.HystrixCommandAspect;
import com.netflix.hystrix.contrib.metrics.eventstream.HystrixMetricsStreamServlet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * hystrix 的配置
 * Created by fuyuanwu on 2017/3/2.
 */
@Configuration
@ConditionalOnExpression("${hystrix.enabled:false}")
public class HystrixConfiguration {
    @Value("${hystrix.streamUrl:/hystrix.stream}")
    String streamUrl;

    @Bean
    @ConditionalOnClass(HystrixCommandAspect.class)
    public HystrixCommandAspect hystrixCommandAspect() {
        return new HystrixCommandAspect();
    }

    @Bean
    @ConditionalOnClass(HystrixMetricsStreamServlet.class)
    @ConditionalOnExpression("${hystrix.streamEnabled:false}")
    public ServletRegistrationBean hystrixStreamServlet() {
        return new ServletRegistrationBean(new HystrixMetricsStreamServlet(), streamUrl);
    }
}