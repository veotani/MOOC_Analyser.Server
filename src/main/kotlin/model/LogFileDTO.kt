package web.api.model

import org.springframework.web.multipart.MultipartFile

class LogFileDTO(var uploadFile: MultipartFile, var fileName: String)