package com.weihui.finance.configure;

import org.apache.activemq.pool.PooledConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.ImportResource;

import javax.annotation.Resource;

/**
 * Created by fuyuanwu on 2017/3/2.
 */
@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
@ImportResource({"classpath:ucs-support-annotatiion.xml",
        "classpath:ucs-support-annotatiion-commonlistener.xml"})
public class MemCacheConfiguration {
    @Resource(name = "pooledConnectionFactory")
    org.apache.activemq.pool.PooledConnectionFactory pooledConnectionFactory;

    /**
     * 解决无法自动注入的问题。如下
     * org.springframework.beans.factory.NoUniqueBeanDefinitionException: No qualifying bean of type
     * 'javax.jms.ConnectionFactory' available: expected single matching bean but found 2: mqJndiConnectionFactory,pooledConnectionFactory
     * @return
     */
    @Bean("jmsListenerContainerFactory")
    public PooledConnectionFactory getPooledConnectionFactory(){
        return pooledConnectionFactory;
    }
}
