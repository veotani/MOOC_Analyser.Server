package web.api.controllers

import java.security.Principal
import java.util.Base64

import javax.servlet.http.HttpServletRequest

import web.api.model.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.web.bind.annotation.*
import web.api.service.UserDetailsServiceImpl


@RestController
@CrossOrigin
class UserController {
    @Autowired
    private val userDetailsService: UserDetailsServiceImpl? = null

    @RequestMapping("/login")
    fun login(@RequestBody user: User): Boolean {
        return userDetailsService!!.isAValidUser(user)
    }

    @RequestMapping("/user")
    fun user(request: HttpServletRequest): Principal {
        val authToken = request.getHeader("Authorization").substring("Basic".length).trim { it <= ' ' }
        return Principal { String(
            Base64
                .getDecoder()
                .decode(authToken))
                .split(
                    ":"
                        .toRegex()
                )
                .dropLastWhile {
                    it.isEmpty()
                }
                .toTypedArray()[0]
        }
    }

    @GetMapping("/currentUserRole")
    fun currentUserRole(request: HttpServletRequest): User
    {
        val authToken = request.getHeader("Authorization").substring("Basic".length).trim { it <= ' ' }
        val userName = String(Base64.getDecoder().decode(authToken)).split(":".toRegex()).dropLastWhile { it.isEmpty() }.
                toTypedArray()[0]
        val userToReturn = User()
        userToReturn.role = userDetailsService!!.getRoleByUserName(userName)
        return userToReturn
    }
}
