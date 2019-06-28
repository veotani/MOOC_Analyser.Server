package web.api.service


import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import web.api.model.User
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import repositories.UserRepository
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import web.api.BasicAuthConfiguration

@Service
class UserDetailsServiceImpl : UserDetailsService {
    private val encryptionService = EncryptionService()

    private val repository = UserRepository()

    override fun loadUserByUsername(username: String): UserDetails {
        val grantedAuthority = setOf<GrantedAuthority>(SimpleGrantedAuthority("COOL_GUY"))
        return org.springframework.security.core.userdetails.User(
            "a",
            "b",
            grantedAuthority
        )
    }

    fun isAValidUser(user: User): Boolean
    {
        val userName = user.userName!!
        val passwordEncoded = repository.getPasswordByUserName(userName)
        return encryptionService.matches(user.password!!, passwordEncoded)
    }

    fun registerNewUserAccount(account: User)
    {
        account.password = encryptionService.encode(account.password!!)
        if (repository.isUserExists(account.userName!!))
            throw Exception("User exists")
        if (repository.isEmailExists(account.email!!))
            throw Exception("Email exists")
        account.role = "NEW"
        repository.save(account)
    }

    fun getRoleByUserName(userName: String): String
    {
        return repository.getUserRole(userName)
    }
}
