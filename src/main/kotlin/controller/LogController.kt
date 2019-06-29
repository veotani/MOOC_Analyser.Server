package web.api.controller

import managers.LogManager
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.multipart.MultipartFile
import service.LogInfoService
import web.api.model.UploadLogStatusDTO

@CrossOrigin(origins = ["http://localhost:4200"])
@RestController
class LogController {

    @PostMapping("/uploadLogs")
    fun uploadLogs(@RequestParam(value = "uploadFile") file: MultipartFile): UploadLogStatusDTO
    {
        val fileStream = file.inputStream
        val fileName = file.originalFilename
        val manager = LogManager(fileName)
        try
        {
            manager.processFile(fileStream)
        }
        catch (e: Exception)
        {
            return UploadLogStatusDTO("Fail! Error: ${e.stackTrace}")
        }
        return UploadLogStatusDTO("success")
    }

}
