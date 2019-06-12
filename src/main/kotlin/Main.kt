import com.fasterxml.jackson.databind.JsonNode
import java.io.IOException
import java.io.FileReader
import java.io.BufferedReader
import com.fasterxml.jackson.databind.ObjectMapper
import java.io.File
import java.lang.Exception


data class VideoEvent(
		val eventTime: Double,
		val eventType: String,
		val videoId: String,
		val userName: String
)

class NotAVideoEventLog(message: String): Exception(message)
class InvalidEventTypeField(message: String): Exception(message)
class EventTypeFieldNotFound(message: String): Exception(message)
class InvalidEventField(message: String): Exception(message)
class InvalidUsernameField(message: String): Exception(message)
class MissingLogData(message: String): Exception(message)
class MissingVideoId(message: String): Exception(message)

class LogParser(private val logFileName: String) {

	private val mapper = ObjectMapper()

	fun getVideoEventTime(log: Iterator<MutableMap.MutableEntry<String, JsonNode>>): Double
	{
		var event: JsonNode? = null
		for (field in log)
		{
			if (field.key != "event") continue
			event = mapper.readTree(field.value.asText())
		}

		for (field in event!!.fields())
		{
			if (field.key != "currentTime" && field.key != "old_time") continue
			if (!field.value.isDouble) throw Exception("Found event time, but it's not double: ${field.value.asText()}")
			println(field.value.asText())
			return field.value.asDouble()
		}
		throw Exception("Found nothing")
	}

	fun getVideoEventUserName(log: Iterator<MutableMap.MutableEntry<String, JsonNode>>): String
	{
		TODO()
	}

	fun getVideoEventVideoId(log: Iterator<MutableMap.MutableEntry<String, JsonNode>>): String
	{
		TODO()
	}

	fun getVideoEventType(log: Iterator<MutableMap.MutableEntry<String, JsonNode>>): String
	{
		TODO()
	}

	fun isVideoEvent(log: Iterator<MutableMap.MutableEntry<String, JsonNode>>): Boolean
	{
		for (field in log)
		{
			if (field.key != "event_type") continue
			if (!field.value.isTextual) throw Exception("Found event_type, but it's not string")
			val eventType = field.value.asText()
			if (
					eventType == "play_video" ||
					eventType == "seek_video" ||
					eventType == "pause_video" ||
					eventType == "stop_video"
			)
				return true

		}
		return false
	}

	fun extractVideoEvents(logString: String)
	{
		val parser = LogParser("F")
	}
}

fun main() {
	val videoEventTypesSet = setOf("play_video", "seek_video", "pause_video", "stop_video")

	// Create parser
	val parser = LogParser("src/resources/spbu_DEL_OBS_fall_2018")

	// Open file
	val logFileName = "src/resources/spbu_BIOINF_spring_2018-TL"
	val mapper = ObjectMapper()
	val reader = BufferedReader (FileReader(logFileName))

	var line = reader.readLine()
	while (line != null)
	{
		var tree = mapper.readTree(line)
		line = reader.readLine()
		if (!tree.has("event_type"))
			continue
		var eventType = tree["event_type"].asText()
		if (!videoEventTypesSet.contains(eventType))
			continue
		var eventTree = mapper.readTree(tree["event"].asText())
		println(eventTree["id"])
		if (eventType == "seek_video")
			println(eventTree["old_time"].asDouble())
		else
			println(eventTree["currentTime"].asDouble())
		when(eventType)
		{
			"play_video" -> println("play")
			else -> println("pause")
		}
		println(tree["username"])
	}
}

