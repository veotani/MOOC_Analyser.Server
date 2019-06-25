package web.api.controllers

import java.security.Principal
import java.util.Base64

import javax.servlet.http.HttpServletRequest

import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

import web.api.model.User

@RestController
@CrossOrigin
class UserController {

    @RequestMapping("/login")
    fun login(@RequestBody user: User): Boolean {
        return user.userName.equals("user") && user.password.equals("password")
    }

    @RequestMapping("/user")
    fun user(request: HttpServletRequest): Principal {
        val authToken = request.getHeader("Authorization").substring("Basic".length).trim { it <= ' ' }
        return Principal {
            Base64
                .getDecoder()
                .decode(authToken)
                .toString()
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
}
