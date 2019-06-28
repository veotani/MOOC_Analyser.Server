package web.api.controller

import web.api.model.FileNamesDTO
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import repositories.VideoEventRepository
import service.LogInfoService
import web.api.service.UserDetailsServiceImpl
import java.util.*
import javax.servlet.http.HttpServletRequest

@RestController
@CrossOrigin
class FilenamesController
{
    private val userDetailsService = UserDetailsServiceImpl()
    private val service = LogInfoService()
    private val repository = VideoEventRepository()

    @GetMapping("/fileNames")
    fun fileNames(request: HttpServletRequest): List<String>
    {
        return this.service.getFileNames()
    }

    @GetMapping("/videos")
    fun videos(@RequestParam(name = "fileName") fileName: String): List<String>
    {
        return service.getVideoIds(fileName)
    }

    @GetMapping("/usernames")
    fun usernames(@RequestParam(name = "fileName") fileName: String, request: HttpServletRequest): List<String>
    {
        val authToken = request.getHeader("Authorization").substring("Basic".length).trim { it <= ' ' }
        val userName = String(Base64.getDecoder().decode(authToken)).split(":".toRegex()).dropLastWhile { it.isEmpty() }.
                toTypedArray()[0]
        val role = userDetailsService!!.getRoleByUserName(userName)
        val countLogsForUserAndFile = repository.countUsersInLogFile(fileName, userName)
        return if (role == "ADMIN" || role == "MANAGER")
            service.getUserNames(fileName)
        else if (countLogsForUserAndFile > 0)
            listOf(userName)
        else
            listOf()
    }
}