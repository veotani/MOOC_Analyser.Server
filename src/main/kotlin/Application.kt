package web.api

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.web.multipart.commons.CommonsMultipartResolver
import org.springframework.web.multipart.MultipartResolver
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder




@SpringBootApplication
class Application
{
    @Bean("multipartResolver")
    fun multipartResolver(): MultipartResolver {
        val multipartResolver = CommonsMultipartResolver()
        multipartResolver.setMaxUploadSize(-1)
        return multipartResolver
    }
}

fun main(args: Array<String>)
{
    SpringApplication.run(Application::class.java, *args)
}
