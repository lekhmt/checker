package com.example.checker.security

import com.example.checker.service.UserService
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(
    private val userService: UserService,
) : UserDetailsService {

    override fun loadUserByUsername(email: String): UserDetails {
        val user = userService.findUserByEmail(email)
        return org.springframework.security.core.userdetails.User(
            user.email,
            user.password,
            setOf(SimpleGrantedAuthority(user.role.name)),
        )
    }

}
