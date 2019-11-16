package com.shop.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
/*启用拦截*/
@EnableWebSecurity
public class MySecurityConfig extends WebSecurityConfigurerAdapter {


    /**
     * 配置一个给密码加密的方式，为了支持security5必须给密码加密而做的操作。
     * @return
     */
    @Bean
    public PasswordEncoder createPasswordEncode(){
        //这里没有md5加密，security舍弃了它。不安全，
        //这里采用BCryptPasswordEncoder,会自动生产盐比较安全。
        return new BCryptPasswordEncoder();
    }


    /**
     * 配置认证资源以及认证的方式。
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //设置认证规则，在spring中是通过配置文件中的http标签设置的。
        //对所有请求进行认证
        http.authorizeRequests()

                //拦截的底部有所有资源的拦截，所以自定义登录页面以及异常页面也会拦截，需要放行
                //需要进行认证的资源以及那个角色可以访问这个资源

                .antMatchers("/login").permitAll()
                .antMatchers("/product/add").hasAnyAuthority("PRODUCT_ADD")
                .antMatchers("/product/update").hasAnyAuthority("PRODUCT_UPDATE")
                .antMatchers("/product/delete").hasAnyAuthority("PRODUCT_DELETE")
                .antMatchers("/product/select").hasAnyAuthority("PRODUCT_SELECT")
                //需要进行认证的资源以及那个角色可以访问这个资源
                .antMatchers("/**")
                //全认证
                .fullyAuthenticated()
                .and()
                //全部采用表单登录的认证方式，也就是默认的认证方式
                //四的时候默认是.httpBasic方式
                //.httpBasic();//弹框形式

                .formLogin().loginPage("/login")
        //禁用CSRF跨站访问，不然点击登录也没效果;
                .and().csrf().disable();
        //配置自定义的登录路径
    }
}
