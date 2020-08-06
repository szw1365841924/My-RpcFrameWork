package api;

import api.service.Hello;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HiAndHelloServiceImpl implements HelloService, HiService {
    private static final Logger logger = LoggerFactory.getLogger(HiAndHelloServiceImpl.class);

    @Override
    public String hello(Hello hello) {
        logger.info("HelloServiceImpl收到: {}.", hello.getMessage());
        String result = "我是Hello " + hello.getDescription();
        logger.info("HelloServiceImpl返回: {}.", result);
        return result;
    }

    @Override
    public String hi(Hello hello) {
        logger.info("HelloServiceImpl收到: {}.", hello.getMessage());
        String result = "我是Hi " + hello.getDescription();
        logger.info("HelloServiceImpl返回: {}.", result);
        return result;
    }
}
