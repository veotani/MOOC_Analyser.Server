package repositories

import services.VideoEvent
import java.io.FileInputStream
import java.util.*
import java.sql.*

class VideoEventRepository
{
    private val dbConfig = Properties()
    private val propertiesFilePath = "properties.xml"
    private var connection: Connection? = null

    init
    {
        val inputStream = FileInputStream(propertiesFilePath)
        dbConfig.loadFromXML(inputStream)
        this.connection = connectToDb()
        if (this.connection == null)
            throw Exception("Couldn't connect to database on ${dbConfig["db.url"]}")
        createVideoEventsTableIfNotExists()
    }

    private fun getConnectionProperties(): Properties
    {
        val username = dbConfig["db.username"].toString()
        val password = dbConfig["db.password"].toString()
        val connectionProperties = Properties()
        connectionProperties["user"] = username
        connectionProperties["password"] = password
        return connectionProperties
    }

    private fun connectToDb(): Connection?
    {
        try
        {
            Class.forName(dbConfig["db.driver"].toString())
            val connectionString = dbConfig["db.url"].toString()
            val connectionProperties = getConnectionProperties()
            return DriverManager.getConnection(connectionString, connectionProperties)
        }
        catch (ex: SQLException)
        {
            ex.printStackTrace()
        }
        return null
    }

    private fun createVideoEventsTableIfNotExists()
    {
        val createTableQuery =
                "CREATE TABLE IF NOT EXISTS video_events" +
                "(" +
                    "video_event_id INT PRIMARY KEY AUTO_INCREMENT," +
                    "event_time DOUBLE,"                             +
                    "event_type VARCHAR(20),"                        +
                    "video_id VARCHAR(50),"                          +
                    "user_name VARCHAR(50),"                         +
                    "log_file_name VARCHAR(100)"                     +
                ")"
        val statement = this.connection!!.createStatement()
        statement.execute(createTableQuery)
    }

    fun insertVideoEvent(videoEvent: VideoEvent, logFileName: String)
    {
        val insertVideoEventQuery =
                "INSERT INTO video_events (event_time, event_type, video_id, user_name, log_file_name)" +
                        " VALUES (" +
                            "'${videoEvent.eventTime}', " +
                            "'${videoEvent.eventType}', " +
                            "'${videoEvent.videoId}', "   +
                            "'${videoEvent.userName}', "  +
                            "'$logFileName'"              +
                        ")"
        val statement = this.connection!!.createStatement()
        statement.executeUpdate(insertVideoEventQuery)
    }

    fun getUniqueVideoIds(): MutableList<String>
    {
        val result = mutableListOf<String>()
        val getVideoIdsQuery = "SELECT video_id FROM video_events GROUP BY video_id"
        val statement = this.connection!!.createStatement()
        statement.executeQuery(getVideoIdsQuery)
        val resultSet = statement.resultSet
        while (resultSet.next())
        {
            result.add(resultSet.getString(1))
        }
        return result
    }

    fun getUniqueUserNames(): MutableList<String>
    {
        val result = mutableListOf<String>()
        val getUserNamesQuery = "SELECT user_name FROM video_events GROUP BY user_name"
        val statement = this.connection!!.createStatement()
        statement.executeQuery(getUserNamesQuery)
        val resultSet = statement.resultSet
        while (resultSet.next())
        {
            result.add(resultSet.getString(1))
        }
        return result
    }

    fun getVideoEventByUserNameVideoIdFileName(userName: String, videoId: String, fileName: String):
            MutableList<VideoEvent>
    {
        val result = mutableListOf<VideoEvent>()
        val getVideoEventsQuery = "SELECT event_time, event_type FROM video_events WHERE " +
                "video_events.video_id = '$videoId' AND " +
                "video_events.user_name = '$userName' AND " +
                "video_events.log_file_name = '$fileName'"
        val statement = this.connection!!.createStatement()
        statement.executeQuery(getVideoEventsQuery)
        val resultSet = statement.resultSet
        while(resultSet.next())
        {
            val eventTime = resultSet.getString(1).toDouble()
            val eventType = resultSet.getString(2)
            result.add(VideoEvent(
                    eventTime,
                    eventType,
                    videoId,
                    userName
            ))
        }
        return result
    }
}

//fun main()
//{
//    val repository = VideoEventRepository()
//    val videoEvent = VideoEvent(
//            1.toDouble(),
//            "test",
//            "test",
//            "test"
//    )
//    repository.insertVideoEvent(videoEvent, "test")
//    val uniqueUserNames = repository.getUniqueUserNames()
//    println("Unique user names:")
//    for (userName in uniqueUserNames)
//        println(userName)
//
//    var uniqueVideoIds = repository.getUniqueVideoIds()
//    println("Unique video ids")
//    for (videoId in uniqueVideoIds)
//        println(videoId)
//
//    var videoEvent = repository.getVideoEventByUserNameVideoIdFileName(
//            "a",
//            "a",
//            "a"
//    )[0]
//    println("Must be all \"a\"s event")
//    println(videoEvent.eventTime.toString() + " " + videoEvent.eventType)
//}