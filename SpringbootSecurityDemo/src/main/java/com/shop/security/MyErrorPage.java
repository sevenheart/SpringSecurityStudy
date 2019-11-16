package com.shop.security;

import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.boot.web.server.ErrorPageRegistry;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
@Configuration
public class MyErrorPage implements ErrorPageRegistrar {

    //可以拦截所有的错误页面
    //选择参数是错误页码的构造方式取构造errorpage通过选择相应的枚举类就可以指定那类错误交给什么去吹
    @Override
    public void registerErrorPages(ErrorPageRegistry registry) {
        ErrorPage errorPage = new ErrorPage(HttpStatus.FORBIDDEN,"/403");
        registry.addErrorPages(errorPage);
    }
}
