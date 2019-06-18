import managers.LogManager
import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.servlet.annotation.MultipartConfig


@WebServlet(name = "UploadLogs", value = ["/uploadLogs"])
@MultipartConfig
class UploadLogsController: HttpServlet() {

    override fun doGet(req: HttpServletRequest, res: HttpServletResponse)
    {
        res.writer.println("yo")
    }

    override fun doPost(req: HttpServletRequest, res: HttpServletResponse)
    {
        setAccessControlHeaders(res)
        val filePart = req.getPart("uploadFile") // Retrieves <input type="file" name="file">
        val fileContent = filePart.inputStream
        val fileName = filePart.submittedFileName
        val manager = LogManager(fileName)
        manager.processFile(fileContent)
    }

    override fun doOptions(request: HttpServletRequest?, response: HttpServletResponse) {
        setAccessControlHeaders(response) // required by angular framework; detailed CORS can be set within the servlet
    }

    private fun setAccessControlHeaders(res: HttpServletResponse) {
        res.setHeader("Access-Control-Allow-Origin", "http://localhost:4200")
        res.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS")
        res.setHeader(
                "Access-Control-Allow-Headers",
                "Origin, Accept, X-Requested-With, " +
                        "Content-Type, Access-Control-Request-Method, " +
                        "Access-Control-Request-Headers, " +
                        "User-Agent, Referer, Content-Length, Content-Language, " +
                        "Date"
        )
    }
}
