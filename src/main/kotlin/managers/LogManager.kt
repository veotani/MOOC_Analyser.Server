package managers

import services.*
import java.io.*

class LogManager(val fileName: String)
{
    val folderToSaveTempFiles = "/tmp/MOOC_logs/"

    fun saveRecievedFile(file: InputStream)
    {
        // Create temp directory
        File("/tmp/MOOC_logs/").mkdirs()
        val filePath = "$folderToSaveTempFiles$fileName"
        val reader = BufferedReader(file.reader())
        var line = reader.readLine()
        File(filePath).printWriter().use { out ->
            while (line != null)
            {
                out.println(line)
                line = reader.readLine()
            }
        }
    }

    fun doParsing(): MutableList<VideoEvent>
    {
        val logFileName = "$folderToSaveTempFiles$fileName"
        val parser = LogParser(logFileName)
        return parser.extractVideoEventsFromLogs()
    }

    fun saveVideoEventsToDb(events: MutableList<VideoEvent>)
    {

    }

    fun deleteRecievedFile()
    {
        val filePath = "$folderToSaveTempFiles$fileName"
        File(filePath).delete()
    }
}