package web.api.service

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.security.SecureRandom
import java.security.interfaces.ECKey

class EncryptionService
{
    private val encoder = BCryptPasswordEncoder(11, SecureRandom("salt".toByteArray()))

    fun encode(password: String): String =
        encoder.encode(password)

    fun matches(suggestedPassword: String, encodedPassword: String): Boolean =
        encoder.matches(suggestedPassword, encodedPassword)
}