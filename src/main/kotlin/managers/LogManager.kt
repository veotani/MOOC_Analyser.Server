package managers

import repositories.VideoEventRepository
import services.*
import java.io.*

class LogManager(private val fileName: String)
{
    private val folderToSaveTempFiles = "/tmp/MOOC_logs/"
    private val repository = VideoEventRepository()

    fun processFile(file: InputStream)
    {
        saveReceivedFile(file)
        val videoEvents = extractVideoEvents()
        saveVideoEventsToDb(videoEvents)
        deleteReceivedFile()
    }

    private fun saveReceivedFile(file: InputStream)
    {
        // Create temp directory
        File(folderToSaveTempFiles).mkdirs()
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

    private fun extractVideoEvents(): MutableList<VideoEvent>
    {
        val logFileName = "$folderToSaveTempFiles$fileName"
        val parser = LogParser(logFileName)
        return parser.extractVideoEventsFromLogs()
    }

    private fun saveVideoEventsToDb(events: MutableList<VideoEvent>)
    {
        for (event in events)
            this.repository.insertVideoEvent(event, this.fileName)
    }

    private fun deleteReceivedFile()
    {
        val filePath = "$folderToSaveTempFiles$fileName"
        File(filePath).delete()
    }
}
