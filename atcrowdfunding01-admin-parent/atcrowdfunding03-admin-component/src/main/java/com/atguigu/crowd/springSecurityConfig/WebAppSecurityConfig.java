package com.atguigu.crowd.springSecurityConfig;

import com.atguigu.crowd.constant.CrowdConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// 注意！这个类一定要放在自动扫描的包下，否则所有配置都不会生效！

// 将当前类标记为配置类
//@Configuration
// 启用Web环境下权限控制功能
@EnableWebSecurity
// 启用全局方法权限控制功能，并且设置prePostEnabled = true。保证@PreAuthority、@PostAuthority、@PreFilter、@PostFilter生效
// 巨坑!!!，此注解必须使用同一个IOC容器注入才有效，比如在这里配置是使 用APPLICATIONCONTEXT注入，就对DispatcherServlet注入的bean无效，也就是说
// 对于此项目的handler方法无效，解决办法是随便找一个DispatcherServlet下的bean打上@EnableGlobalMethodSecurity(prePostEnabled = true)标签
// 或者使用xml配置标签<security:global-method-security pre-post-annotations="enabled"/>，详见spring-web-mvc.xml
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebAppSecurityConfig extends WebSecurityConfigurerAdapter {

    //从数据库中得到用户信息
    @Autowired
    private UserDetailsService userDetailsService;

    //因为是配置类，所以也可以配置一些bean，默认这个bean是单例(single)的
    //在spring-persist-security.xml中配置了，显得更有逼格
//    @Bean
//    public BCryptPasswordEncoder getPasswordEncoder() {
//        return new BCryptPasswordEncoder();
//    }

    //加密，带盐的
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    @Override
    protected void configure(AuthenticationManagerBuilder builder) throws Exception {
        // 临时使用内存版登录的模式测试代码
//        builder.inMemoryAuthentication().withUser("tom").password("123123").roles("ADMIN");

        builder
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder)
        ;
    }

    //也可以使用注解的形式，来控制，注意要在这个配置类前加上@EnableGlobalMethodSecurity
    @Override
    protected void configure(HttpSecurity security) throws Exception {

        security
                .authorizeRequests()	// 对请求进行授权
                .antMatchers("/admin/to/login/page.html")	// 针对登录页进行设置
                .permitAll()			// 无条件访问
                .antMatchers("/bootstrap/**")	// 针对静态资源进行设置，无条件访问
                .permitAll()                    // 针对静态资源进行设置，无条件访问
                .antMatchers("/crowd/**")       // 针对静态资源进行设置，无条件访问
                .permitAll()                    // 针对静态资源进行设置，无条件访问
                .antMatchers("/css/**")         // 针对静态资源进行设置，无条件访问
                .permitAll()                    // 针对静态资源进行设置，无条件访问
                .antMatchers("/fonts/**")       // 针对静态资源进行设置，无条件访问
                .permitAll()                    // 针对静态资源进行设置，无条件访问
                .antMatchers("/img/**")         // 针对静态资源进行设置，无条件访问
                .permitAll()                    // 针对静态资源进行设置，无条件访问
                .antMatchers("/jquery/**")      // 针对静态资源进行设置，无条件访问
                .permitAll()                    // 针对静态资源进行设置，无条件访问
                .antMatchers("/layer/**")       // 针对静态资源进行设置，无条件访问
                .permitAll()                    // 针对静态资源进行设置，无条件访问
                .antMatchers("/script/**")      // 针对静态资源进行设置，无条件访问
                .permitAll()                    // 针对静态资源进行设置，无条件访问
                .antMatchers("/ztree/**")       // 针对静态资源进行设置，无条件访问
                .permitAll()
//                .antMatchers("/admin/get/page.html")	// 针对分页显示Admin数据设定访问控制
//                .access("hasRole('经理') OR hasAuthority('user')")	// 要求具备“经理”角色和“user”权限二者之一
                // .hasRole("经理")					// 要求具备经理角色
                .anyRequest()					// 其他任意请求
                .authenticated()				// 认证后访问
                .and()
                .exceptionHandling()
                .accessDeniedHandler(new AccessDeniedHandler() {

                    // 注意，这个只和在本配置的访问限制的时候才有效果(此句话待验证，因为此bean和mvc的bean不是使用同一ioc容器注入)
                    // 如果在本配置上配置了相关访问权限，比如上面的.access("hasRole('经理') OR hasAuthority('user')")，当权限访问受限时，
                    // springMVC的异常检测是无法捕获异常的，因为这个权限是直接走FIlter的，不经过后面的DispatcherServlet处理
                    // 如果在Handler方法中配置注解的方式，比如AdminHandler类的getPageInfo方法中配置访问注解，那么此异常处理方法无效
                    // 在Handler方法中的解决方法是使用全局的异常处理捕获抛出的AccessDeniedException异常(可以get请求的url再进行后续判断)
                    // 方便使用，要么统一采用注解的方式，另写异常处理方法处理权限访问失败；要么同一在此bean下配置，在此方法中处理权限访问失败
                    @Override
                    public void handle(HttpServletRequest request, HttpServletResponse response,
                                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
                        request.setAttribute("exception", new Exception(CrowdConstant.MESSAGE_ACCESS_DENIED));
                        request.getRequestDispatcher("/WEB-INF/system-error.jsp").forward(request, response);
                    }
                })
                .and()
                .csrf()							// 防跨站请求伪造功能
                .disable()						// 禁用
                .formLogin()					// 开启表单登录的功能
                .loginPage("/admin/to/login/page.html")	// 指定登录页面
                .loginProcessingUrl("/security/do/login.html")	// 指定处理登录请求的地址
                .defaultSuccessUrl("/admin/to/main/page.html",true)	// 指定登录成功后前往的地址
                .usernameParameter("loginAcct")	// 账号的请求参数名称
                .passwordParameter("userPswd")	// 密码的请求参数名称
                .and()
                .logout()						// 开启退出登录功能
                .logoutUrl("/seucrity/do/logout.html")			// 指定退出登录地址
                .logoutSuccessUrl("/admin/to/login/page.html")	// 指定退出成功以后前往的地址
        ;
    }
}