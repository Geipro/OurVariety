package com.ssafy.config;

import com.ssafy.api.service.UserService;
import com.ssafy.common.auth.JwtAuthenticationFilter;
import com.ssafy.common.auth.SsafyUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * ����(authentication) �� �ΰ�(authorization) ó���� ���� ������ ��ť��Ƽ ���� ����.
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private SsafyUserDetailService ssafyUserDetailService;
    
    @Autowired
    private UserService userService;
    
    // Password ���ڵ� ��Ŀ� BCrypt ��ȣȭ ��� ���
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // DAO ������� Authentication Provider�� ����
    // BCrypt Password Encoder�� UserDetailService ����ü�� ����
    @Bean
    DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(this.ssafyUserDetailService);
        return daoAuthenticationProvider;
    }

    // DAO ����� Authentication Provider�� ����ǵ��� ����
    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // ��ū ��� �����̹Ƿ� ���� ��� ��������
                .and()
                .addFilter(new JwtAuthenticationFilter(authenticationManager(), userService)) //HTTP ��û�� JWT ��ū ���� ���͸� ��ġ���� ���͸� �߰�
                .authorizeRequests()
                .antMatchers("/api/v1/users/me").authenticated()       //������ �ʿ��� URL�� �ʿ����� ���� URL�� ���Ͽ� ����
    	        	    .anyRequest().permitAll()
                .and().cors();
    }
}