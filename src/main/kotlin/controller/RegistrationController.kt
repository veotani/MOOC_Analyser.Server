package web.api.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import web.api.model.User
import web.api.service.EncryptionService
import service.RegistrationService
import web.api.model.RegistrationStatusDTO
import web.api.service.UserDetailsServiceImpl

@RestController
@CrossOrigin
class RegistrationController {

    var service = UserDetailsServiceImpl()

    @RequestMapping("/register")
    fun register(@RequestBody user: User): RegistrationStatusDTO {
        user.role = "NEW"
        println(user.password)
        println(user.email)
        println(user.role)
        println(user.userName)
        try
        {
            service.registerNewUserAccount(user)
        }
        catch (e: Exception) {
            return when {
                e.message == "User exists" -> RegistrationStatusDTO("login_exists")
                e.message == "Email exists" -> RegistrationStatusDTO("email_exists")
                else -> {
                    e.printStackTrace()
                    RegistrationStatusDTO("unexpected error")
                }
            }
        }
        return RegistrationStatusDTO("success")
    }
}
