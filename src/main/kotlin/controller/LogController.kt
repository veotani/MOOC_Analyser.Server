package web.api.controller

import managers.LogManager
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.multipart.MultipartFile
import service.LogInfoService

@CrossOrigin
@Controller
class LogController {

    @PostMapping("/uploadLogs")
    fun uploadLogs(@RequestParam(value = "uploadFile") file: MultipartFile)
    {
        val modelAndView = ModelAndView("file")
        val fileStream = file.inputStream
        val fileName = file.originalFilename
        val manager = LogManager(fileName)
        manager.processFile(fileStream)
    }

}
