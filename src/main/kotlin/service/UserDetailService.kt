package web.api.service

import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service


@Service
class UserDetailsServiceImpl : UserDetailsService {
//    @Transactional(readOnly = true)
    override fun loadUserByUsername(username: String): UserDetails {
        val grantedAuthority = setOf<GrantedAuthority>(SimpleGrantedAuthority("COOL_GUY"))
        return org.springframework.security.core.userdetails.User(
            "a",
            "b",
            grantedAuthority
        )
    }
}
