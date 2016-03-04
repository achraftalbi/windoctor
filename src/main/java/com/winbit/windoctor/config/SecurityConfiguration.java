package com.winbit.windoctor.config;

import com.winbit.windoctor.security.*;
import com.winbit.windoctor.web.filter.CsrfCookieGeneratorFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import org.springframework.data.repository.query.spi.EvaluationContextExtension;
import org.springframework.data.repository.query.spi.EvaluationContextExtensionSupport;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.data.repository.query.SecurityEvaluationContextExtension;

import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.csrf.CsrfFilter;

import javax.inject.Inject;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Inject
    private Environment env;

    @Inject
    private AjaxAuthenticationSuccessHandler ajaxAuthenticationSuccessHandler;

    @Inject
    private AjaxAuthenticationFailureHandler ajaxAuthenticationFailureHandler;

    @Inject
    private AjaxLogoutSuccessHandler ajaxLogoutSuccessHandler;

    @Inject
    private Http401UnauthorizedEntryPoint authenticationEntryPoint;

    @Inject
    private UserDetailsService userDetailsService;

    @Inject
    private RememberMeServices rememberMeServices;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Inject
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
            .antMatchers("/scripts/**/*.{js,html}")
            .antMatchers("/bower_components/**")
            .antMatchers("/i18n/**")
            .antMatchers("/assets/**")
            .antMatchers("/swagger-ui/index.html")
            .antMatchers("/test/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf()
            .ignoringAntMatchers("/websocket/**")
        .and()
            .addFilterAfter(new CsrfCookieGeneratorFilter(), CsrfFilter.class)
            .exceptionHandling()
            .authenticationEntryPoint(authenticationEntryPoint)
        .and()
            .rememberMe()
            .rememberMeServices(rememberMeServices)
            .rememberMeParameter("remember-me")
            .key(env.getProperty("jhipster.security.rememberme.key"))
        .and()
            .formLogin()
            .loginProcessingUrl("/api/authentication")
            .successHandler(ajaxAuthenticationSuccessHandler)
            .failureHandler(ajaxAuthenticationFailureHandler)
            .usernameParameter("j_username")
            .passwordParameter("j_password")
            .permitAll()
        .and()
            .logout()
            .logoutUrl("/api/logout")
            .logoutSuccessHandler(ajaxLogoutSuccessHandler)
            .deleteCookies("JSESSIONID")
            .permitAll()
        .and()
            .headers()
            .frameOptions()
            .disable()
        .and()
            .authorizeRequests()
            //public acess
            .antMatchers("/api/authenticate").permitAll()
            .antMatchers("/api/account/reset_password/init").permitAll()
            .antMatchers("/api/account/reset_password/finish").permitAll()

            .antMatchers("/api/account/sessions").hasAuthority(AuthoritiesConstants.ADMIN)
            .antMatchers("/api/register").hasAuthority(AuthoritiesConstants.ADMIN)
            .antMatchers("/api/activate").hasAuthority(AuthoritiesConstants.ADMIN)
            .antMatchers("/api/logs/**").hasAuthority(AuthoritiesConstants.ADMIN)
            .antMatchers("/api/audits/**").hasAuthority(AuthoritiesConstants.ADMIN)
            //begin : windoctor security rules management
            .antMatchers("/api/events/**").hasAuthority(AuthoritiesConstants.PATIENT)
            .antMatchers("/api/eventDTO/**").hasAuthority(AuthoritiesConstants.PATIENT)
            .antMatchers("/api/eventsAll/**").hasAuthority(AuthoritiesConstants.PATIENT)
            .antMatchers("/api/statuss/**").hasAuthority(AuthoritiesConstants.PATIENT)
            .antMatchers("/api/patients/**").hasAuthority(AuthoritiesConstants.PATIENT)
            .antMatchers("/api/eventsNotification/**").hasAuthority(AuthoritiesConstants.PATIENT)
            .antMatchers("/api/eventsBlock/**").hasAuthority(AuthoritiesConstants.ASSISTANT)
            .antMatchers("/api/structures/**").hasAuthority(AuthoritiesConstants.ASSISTANT)
            .antMatchers("/api/funds/**").hasAuthority(AuthoritiesConstants.ASSISTANT)
            .antMatchers("/api/supply_types/**").hasAuthority(AuthoritiesConstants.ASSISTANT)
            .antMatchers("/api/treatments/**").hasAuthority(AuthoritiesConstants.ASSISTANT)
            .antMatchers("/api/treatments/**").hasAuthority(AuthoritiesConstants.ASSISTANT)
            .antMatchers("/api/event_reasons/**").hasAuthority(AuthoritiesConstants.ASSISTANT)
            .antMatchers("/api/fund_historys/**").hasAuthority(AuthoritiesConstants.ASSISTANT)
            .antMatchers("/api/attachments/**").hasAuthority(AuthoritiesConstants.ASSISTANT)
            .antMatchers("/api/mailTypes/**").hasAuthority(AuthoritiesConstants.ASSISTANT)
            .antMatchers("/api/categorys/**").hasAuthority(AuthoritiesConstants.ASSISTANT)
            .antMatchers("/api/products/**").hasAuthority(AuthoritiesConstants.ASSISTANT)
            .antMatchers("/api/users/**").hasAuthority(AuthoritiesConstants.ASSISTANT)
            .antMatchers("/api/doctors/**").hasAuthority(AuthoritiesConstants.ASSISTANT)
            .antMatchers("/api/dashboards/**").hasAuthority(AuthoritiesConstants.DOCTOR)
            //end
            .antMatchers("/api/**").authenticated()
            .antMatchers("/metrics/**").hasAuthority(AuthoritiesConstants.ADMIN)
            .antMatchers("/health/**").hasAuthority(AuthoritiesConstants.ADMIN)
            .antMatchers("/trace/**").hasAuthority(AuthoritiesConstants.ADMIN)
            .antMatchers("/dump/**").hasAuthority(AuthoritiesConstants.ADMIN)
            .antMatchers("/shutdown/**").hasAuthority(AuthoritiesConstants.ADMIN)
            .antMatchers("/beans/**").hasAuthority(AuthoritiesConstants.ADMIN)
            .antMatchers("/configprops/**").hasAuthority(AuthoritiesConstants.ADMIN)
            .antMatchers("/info/**").hasAuthority(AuthoritiesConstants.ADMIN)
            .antMatchers("/autoconfig/**").hasAuthority(AuthoritiesConstants.ADMIN)
            .antMatchers("/env/**").hasAuthority(AuthoritiesConstants.ADMIN)
            .antMatchers("/trace/**").hasAuthority(AuthoritiesConstants.ADMIN)
            .antMatchers("/v2/api-docs/**").permitAll()
            .antMatchers("/configuration/security").permitAll()
            .antMatchers("/configuration/ui").permitAll()
            .antMatchers("/swagger-ui/index.html").hasAuthority(AuthoritiesConstants.ADMIN)
            .antMatchers("/protected/**").authenticated() ;

    }

    @Bean
    public SecurityEvaluationContextExtension securityEvaluationContextExtension() {
        return new SecurityEvaluationContextExtension();
    }
}
