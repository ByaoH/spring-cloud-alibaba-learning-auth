package com.byaoh.cloud.auth.config;

import com.byaoh.cloud.auth.config.etc.SecurityConstants;
import com.byaoh.cloud.auth.exception.JwtAccessDeniedHandler;
import com.byaoh.cloud.auth.exception.JwtAuthenticationEntryPoint;
import com.byaoh.cloud.auth.filter.JwtAuthorizationFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author l
 */
@Slf4j
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	/** 用户认证接口 */
	private final UserDetailsService userDetailsService;

	private final JwtAuthorizationFilter jwtAuthorizationFilter;

	public SecurityConfig(UserDetailsService userDetailsService, JwtAuthorizationFilter jwtAuthorizationFilter) {
		this.userDetailsService = userDetailsService;
		this.jwtAuthorizationFilter = jwtAuthorizationFilter;
	}

	@Bean
	public PasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		// 注入 AuthenticationManager 认证过滤器中要使用
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			// 关闭 cors
			.cors().and().csrf().disable()
			// 配置拦截策略
			.authorizeRequests()
			// 放行登陆
			.antMatchers(HttpMethod.POST, SecurityConstants.AUTH_LOGIN_URL).permitAll()
			// 白名单
			.antMatchers(SecurityConstants.FILTER_WHITELIST).permitAll()
			// 其他全部不放行
			.anyRequest().authenticated()
			.and()
			.headers().frameOptions().disable();
		http.addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
			// 禁用session
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			.exceptionHandling()
			// 未认证
			.authenticationEntryPoint(new JwtAuthenticationEntryPoint())
			// 未授权
			.accessDeniedHandler(new JwtAccessDeniedHandler());
	}

	/**
	 * 身份认证接口
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
	}
}
