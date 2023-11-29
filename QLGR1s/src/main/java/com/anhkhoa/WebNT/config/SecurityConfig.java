package com.anhkhoa.WebNT.config;

import java.beans.Customizer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import com.anhkhoa.WebNT.service.CustomerUserDetailService;


@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	CustomerUserDetailService customerUserDetailService;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.csrf().disable();
		http.authorizeRequests().antMatchers("/","/home", "/login", "/logout", "/laylaimk", "/buoc2", "/setmatkhau", "/setmatkhauok").permitAll();
		http.authorizeRequests().antMatchers("/assets/**").permitAll();
		
		//Quyền admin
		http.authorizeRequests().antMatchers("/banthanhphan").access("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')");
		http.authorizeRequests().antMatchers("/banthanhphan/**").access("hasRole('ROLE_ADMIN')");
		http.authorizeRequests().antMatchers("/banthanhpham/edit/**").access("hasRole('ROLE_ADMIN')");
		http.authorizeRequests().antMatchers("/banthanhpham/delete/**").access("hasRole('ROLE_ADMIN')");
		http.authorizeRequests().antMatchers("/phanxuong**").access("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')");
		http.authorizeRequests().antMatchers("/phanxuong/**").access("hasRole('ROLE_ADMIN')");
		
		//Quyền Manager_Admin
		
		http.authorizeRequests().antMatchers("/chitietphieunhap").access("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')");
		
		http.authorizeRequests().antMatchers("/userInfo").access("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')");
		
		http.authorizeRequests().antMatchers("/manager/**").access("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')");
		http.authorizeRequests().and().exceptionHandling().accessDeniedPage("/404");
		http.authorizeRequests().anyRequest().authenticated();
		
		http.authorizeRequests().and().formLogin()
				
				.loginProcessingUrl("/j_spring_security_check") 
							
				.loginPage("/login")//
				.defaultSuccessUrl("/home")
														
										
				.failureUrl("/login?error=true")
				.usernameParameter("username")
				.passwordParameter("password")
				.and().logout().logoutUrl("/logout").logoutSuccessUrl("/login");

	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {

		auth.userDetailsService(customerUserDetailService).passwordEncoder(passwordEncoder());


	}
}
