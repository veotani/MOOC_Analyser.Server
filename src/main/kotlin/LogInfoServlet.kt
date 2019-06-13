import services.LogParser
import java.io.BufferedReader
import java.io.File
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

        // Get file from request
        val filePart = req.getPart("uploadFile") // Retrieves <input type="file" name="file">
        val fileContent = filePart.inputStream
        val reader = BufferedReader (fileContent.reader())
        val fileName = "saved_logs/${filePart.submittedFileName}"

        // Save that file in TomCat dir
        // TODO: save this file somewhere else
        var line = reader.readLine()
        File(fileName).printWriter().use { out ->
            while (line != null)
            {
                out.println(line)
                line = reader.readLine()
            }
        }

        // Extract events from this logs
        val parser = LogParser(fileName)
        val events = parser.extractVideoEventsFromLogs()

        for (event in events)
            println(event.videoId)

        // Store this objects in the database
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
