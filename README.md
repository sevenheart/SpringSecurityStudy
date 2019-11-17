# SpringSecurityStudy
spring和springboot分别整合security框架的小例子
# Spring-security-Demo练习

## 一、强调：

**Spring Security**是一常见的安全框架，类似的框架还有：Shiro。

它为应用系统提供声明式的安全访问控制功能，减少了为企业系统安全控制编写大量重复代码的工作。

版本介绍：Security4和5的区别很大（着重说一下入门遇到的区别）。如果是Spring项目集成Security那么它的版本是4，如果是Spring boot项目，它的版本是根据springboot的版本而改变的，如果Springboot是2.0之前的，那么它就是4版本，如果是2.0后的就是5版本。下面的例子以5版本进行（也就是我的spring boot是2.0的）。

**区别1：** 登录的密码必须是加密后的进行校验，不然会报错。

**区别2：**对于错误页的配置更加精简。

**说明：** 角色：用来标识这个角色可以去访问那些资源。也就是给角色分配权限，让用户去拥有角色，然后达到用户有了权限的目的。

   			用户：登录的用户，一个用户可以去配置一个角色的集合，标识这个用户可以有这些角色的所有权限。

## 二、项目整合Demo

### Spring整合

1.新建maven项目

2.导入spring相关的依赖导入security依赖（pom中都有哦）

3.web.xml下配置一个filter相当于拦截到再交给securtiy去处理。再配置一个配置文件路径spring-security.xml这个文件用来配置一些security的配置信息。配置好后记得新建这个文件在rescourse目录下。这个web.xml可能会报错，它对servlet标签不支持!所以要换一下它的头（项目中都有）

```
<web-app xmlns="http://java.sun.com/xml/ns/javaee"
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xsi:schemaLocation="http://java.sun.com/xml/ns/javaee
        http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd"
        version="3.0">

```

4.配置spring-security.xml文件。

​	（1）改一下头

```
<?xml version="1.0" encoding="UTF-8"?>
<beans:beans xmlns="http://www.springframework.org/schema/security"
             xmlns:beans="http://www.springframework.org/schema/beans"
             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xsi:schemaLocation="http://www.springframework.org/schema/beans
              http://www.springframework.org/schema/beans/spring-beans.xsd
                  http://www.springframework.org/schema/security
                   http://www.springframework.org/schema/security/spring-security.xsd">

```

​	

（2）配置页面拦截规则，要拦截那些资源。（通过http标签配置拦截规则时要注意，xml文件可能会报错：

```
是否开启SPEL表达式,默认值是true，如果开启后，那么access中表达式的值应为: access="hasRole('ROLE_USER')"
```

 ）

（3）配置一个认证管理器 <**authentication-manager**>，内容是用户名及密码以及用户的角色。

<**form-login**>标签是开启登录拦截的标签。

（4）之后登录测试即可会有系统自己的登录页。

（5）security提供的登录页太丑可以配置自己的登录页，但是又严格的要求，form的methd是post，action是/login以及input的name是username和password。

（6）之后在http标签中配置自定义的页面就可以了。注意要对login和自己自定义的页面放行不然会一直拦截。

（7）CSRF（Cross-site request forgery）跨站请求伪造，也被称为“One Click Attack”或者Session Riding，通常缩写为CSRF或者XSRF，是一种对网站的恶意利用。



### Spring boot整合

**注意这时的版本时5了，密码必须是加密的**

1.配置pom文件

2.写好几个功能页方便测试。

3.默认的情况下：spring
boot 和spring
security已经集成 并拦截所有的请求，需要验证通过才能放行。导入security依赖后就可以启动项目了，默认的情况下：用户名为user 密码为每次启动项目是所生成的的随机字符串

4.编写security的配置类并加上EnableWebSecurity注解这样就会在拦截时去用这个配置了。

5.配置类**extends** WebSecurityConfigurerAdapter，然后重写方法，配置拦截的资源和

 http
            .authorizeRequests()
            .antMatchers(**"/\**"**)
            .fullyAuthenticated()
            .and()
            .httpBasic();//这个时弹窗的登录方式，默认不是这个。

6.写一个获取用户信息的类，用户的信息获取需要通过实现一个接口：UserDetailsService 来实现这个功能。

实现它根据用户名返回一个detailUSER的方法，它时user的接口，选择user的构造方法为（username.password,Collection<role>）的构造方法，放入相应的参数，但密码要加密。

7.通常密码在数据库中是加密存储的，如果不是就在配置类中写个加密方法（它也有自己的加密方法需要重写）

以往一般都用MD5加盐进行密码加密， 现在正好使用Spring Security安全框架，和其他加密方式相比，BCryptPasswordEncoder有着它自己的优势所在，首先加密的hash值每次都不同，就像md5的盐值加密一样，只不过盐值加密用到了随机数，前者用到的是其内置的算法规则，毕竟随机数没有设合适的话还是有一定几率被攻破的。其次BCryptPasswordEncoder的生成加密存储串也有60位之多。最重要的一点是，md5的加密不是spring security所推崇的加密方式了，所以我们还是要多了解点新的加密方式。

8.写好之后把他变成一个bean交给springIoc管理，然后再在那个获取用户信息的方法中调用这个方法进行密码的加密即可，对于返回地user对象还有一个参数是角色集合，如果是一个角色可以通过方法 AuthorityUtils.*commaSeparatedStringToAuthorityList*(**"ROLE_USER"**)生成，如果不是一个角色可以通过自己写一个相应的集合作为参数放入。（项目中都有说明）

9.再为每个不同的角色配置不同的权限，在配置文件中。

10如果要自定义登录页及错误页就要再写个配置类，**implements** ErrorPageRegistrar ，将相关的错误绑定后给出对应的跳转路径即可，还是要对这些自定义页做放行处理不然也会拦截，还有CSRF。
