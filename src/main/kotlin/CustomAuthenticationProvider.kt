package web.api

import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component
import web.api.model.User
import web.api.service.UserDetailsServiceImpl

@Component
class CustomAuthenticationProvider : AuthenticationProvider
{
    val service = UserDetailsServiceImpl()

    override fun authenticate(authentication: Authentication): Authentication? {

        val user = User()
        user.userName = authentication.name
        user.password = authentication.credentials.toString()
        if (service.isAValidUser(user))
        {
            val role = service.getRoleByUserName(user.userName!!)
            var grantedAuthority = listOf<GrantedAuthority>(SimpleGrantedAuthority(role))
            var principal = org.springframework.security.core.userdetails.User(user.userName, user.password, grantedAuthority)
            var auth = UsernamePasswordAuthenticationToken(principal, user.password, grantedAuthority)
            return auth
        }
        else
        {
            return null
        }
    }

    override fun supports(authentication: Class<*>): Boolean {
        return authentication == UsernamePasswordAuthenticationToken::class.java
    }

}