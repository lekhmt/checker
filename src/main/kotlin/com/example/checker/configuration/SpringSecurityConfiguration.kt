package com.example.checker.configuration

import com.example.checker.security.CustomAuthenticationSuccessHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain


@Configuration
@EnableWebSecurity
class SpringSecurityConfiguration(
    private val userDetailsService: UserDetailsService,
    private val encoder: PasswordEncoder,
) {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http.invoke {
            csrf { disable() }
            authorizeRequests {
                // login
                authorize("/login", permitAll)
                authorize("/login-error", permitAll)
                authorize("/logout", authenticated)
                // portal
                authorize("/portal/**", authenticated)
                authorize("/portal/curator", hasAuthority("CURATOR"))

            }
            formLogin {
                loginPage = "/login"
                failureUrl = "/login-error"
                authenticationSuccessHandler = customAuthenticationSuccessHandler()
            }
            logout {
                logoutSuccessUrl = "/login"
            }
            httpBasic {  }
        }
        return http.build()
    }

    @Bean
    fun customAuthenticationSuccessHandler() = CustomAuthenticationSuccessHandler()

    fun configureGlobal(auth: AuthenticationManagerBuilder) {
        auth
            .userDetailsService(userDetailsService)
            .passwordEncoder(encoder)
    }

}
