package com.ykgroup.dayco.uaa.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.ykgroup.dayco.uaa.auth.application.UaaUserDetailService;
import com.ykgroup.dayco.uaa.manager.application.ManagerService;

@Configuration
@EnableWebSecurity
public class UaaWebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private JwtTokenProvider jwtTokenProvider;
    private ManagerService managerService;

    public UaaWebSecurityConfiguration(JwtTokenProvider jwtTokenProvider,
                                       ManagerService managerService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.managerService = managerService;
    }

    @Bean
    public AccessDecisionManager accessDecisionManager() {
        return new DefaultAccessDecisionManager();
    }

    @Bean
    public UrlResourceMapFactoryBean urlResourceMapFactoryBean() {
        return new UrlResourceMapFactoryBean(managerService);
    }

    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/h2-console/**", "/resources/**");
    }

    protected void configure(HttpSecurity http) throws Exception {
        http.headers()
            .frameOptions().sameOrigin()
            .httpStrictTransportSecurity().disable();

        http
            .csrf().disable()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
            .antMatchers("/auth/signin", "/auth/signup", "/manager/**").permitAll()
            .anyRequest().authenticated()
            .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                public <O extends FilterSecurityInterceptor> O postProcess(O fsi) {
                    fsi.setSecurityMetadataSource(
                            new DefaultFilterInvocationMetadataSource(managerService.findRoleResources()));
                    fsi.setAccessDecisionManager(accessDecisionManager());
                    return fsi;
                }
            })
            .and()
            .httpBasic()
            .and()
            .apply(new JwtConfigurer(jwtTokenProvider));

        http
            .addFilterBefore(new JwtTokenFilter(jwtTokenProvider),
                             UsernamePasswordAuthenticationFilter.class);

    }

    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Configuration
    static class AuthenticationConfiguration
            extends GlobalAuthenticationConfigurerAdapter {

        private UserDetailsService userDetailsService;

        public AuthenticationConfiguration(UaaUserDetailService userDetailsService) {
            this.userDetailsService = userDetailsService;
        }

        @Bean
        PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }

        public void init(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
        }
    }
}
