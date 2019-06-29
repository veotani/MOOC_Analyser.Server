package web.api

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import web.api.service.UserDetailsServiceImpl




@Configuration
@EnableWebSecurity
//@ComponentScan("web.api")
class BasicAuthConfiguration : WebSecurityConfigurerAdapter() {
    private val authProvider = CustomAuthenticationProvider()

    val userDetailsService = UserDetailsServiceImpl()
//
//    @Bean
//    fun authProvider(): DaoAuthenticationProvider
//    {
//        val authProvider = DaoAuthenticationProvider()
//        authProvider.setUserDetailsService(userDetailsService)
//        authProvider.setPasswordEncoder(encoder())
//        return authProvider
//    }

    override fun configure(auth: AuthenticationManagerBuilder?) {
        //auth!!
        auth!!.authenticationProvider(authProvider)
//            .authenticationProvider(authProvider())
//            .inMemoryAuthentication()
//            .withUser("user")
//            .password("password")
//            .roles("USER")
    }

    override fun configure(http: HttpSecurity) {
        http.cors().and().csrf().disable()
            .authorizeRequests()
            .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
            .antMatchers("/login").permitAll()
            .antMatchers("/register").permitAll()
            .antMatchers("/fileNames").authenticated()
            .antMatchers("/uploadLogs").permitAll()//.hasRole("ADMIN")
            .antMatchers("/usernames").authenticated()
            .antMatchers("/individualVideoAnalysis").authenticated()
            .antMatchers("/videos").authenticated()
            .anyRequest().permitAll()
            .and()
            .httpBasic()
    }
}
