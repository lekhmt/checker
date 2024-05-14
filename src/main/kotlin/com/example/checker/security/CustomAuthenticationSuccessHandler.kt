package com.example.checker.security

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.web.DefaultRedirectStrategy
import org.springframework.security.web.WebAttributes
import org.springframework.security.web.authentication.AuthenticationSuccessHandler

class CustomAuthenticationSuccessHandler : AuthenticationSuccessHandler {

    private val redirectStrategy = DefaultRedirectStrategy()

    override fun onAuthenticationSuccess(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authentication: Authentication?
    ) {
        val targetUrl = determineTargetUrl(authentication)
        redirectStrategy.sendRedirect(request, response, targetUrl)
        clearAuthenticationAttributes(request)
    }

    private fun determineTargetUrl(authentication: Authentication?): String {
        val roleTargetUrlMap = mapOf(
            "STUDENT" to "/portal/student",
            "CURATOR" to "/portal/curator"
        )
        for (authority in authentication!!.authorities) {
            if (roleTargetUrlMap.containsKey(authority.authority)) return roleTargetUrlMap[authority.authority]!!
        }
        throw IllegalStateException()
    }

    private fun clearAuthenticationAttributes(request: HttpServletRequest?) {
        val session = request?.session ?: return
        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION)
    }

}