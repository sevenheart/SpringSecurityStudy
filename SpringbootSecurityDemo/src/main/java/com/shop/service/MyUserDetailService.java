package com.shop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/*获取用户信息包含用户的密码等*/
@Configuration
public class MyUserDetailService  implements UserDetailsService {
    //加密方式，在配置类中有配置。会自动装配
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 可以通过这个方法传入一个用户名然后返回一个UserDetail可以包含密码等信息。
     * @param
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //UserDetail是个接口它的父类是user类
        //密码必须是加密的，security5之后密码不加密会报错
        //参数三是一个角色集合，可以采用创建集合或者用AuthorityUtils.commaSeparatedStringToAuthorityList()工具类，
        // 把字符串转成角色集合
        System.out.println("用户名是"+username);
        //对密码进行加密
        //这里的密码就可以从数据库取了。
        String encoderpassword = passwordEncoder.encode("123456");

        System.out.println("加密后的密码是"+encoderpassword);
        //调用添加角色的方法，生成角色集合。
        Collection<GrantedAuthority> authoritesList = authorities();

        //添加单一角色：
        //AuthorityUtils.commaSeparatedStringToAuthorityList("Role_User"）
        return new User(username,encoderpassword,authoritesList);
    }
    /**
     * 为当前用户指定一些角色
     * User的构造方法角色是一个角色Collection所以创建一个返回角色集合的方法，
     * AuthorityUtils.commaSeparatedStringToAuthorityList("Role_User")只能创建一个角色不适用。
     */
    private Collection<GrantedAuthority> authorities(){
        //添加两个角色，分别有添加和修改的权限
        List<GrantedAuthority> authorityList  = new ArrayList<GrantedAuthority>();
                authorityList.add(new SimpleGrantedAuthority("PRODUCT_ADD"));
                authorityList.add(new SimpleGrantedAuthority("PRODUCT_UPDATE"));
        return authorityList;
    }
}
