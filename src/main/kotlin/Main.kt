import com.fasterxml.jackson.databind.JsonNode
import java.io.IOException
import java.io.FileReader
import java.io.BufferedReader

import com.fasterxml.jackson.databind.ObjectMapper
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

	private val VIDEO_EVENT_TYPES = listOf("play_video", "stop_video", "pause_video", "seek_video")
	private val mapper = ObjectMapper()

	fun readAndPrintLines() {
		try {
			val reader = BufferedReader (FileReader(logFileName))
			var line = reader.readLine()
			while (line != null) {
				println(line)
				line = reader.readLine()
			}
			reader.close()
		} catch (e: IOException) {
			e.printStackTrace()
		}
	}


	fun extractVideoEvent(logFieldsTree: Iterator<MutableMap.MutableEntry<String, JsonNode>>): VideoEvent
	{
		if (!isVideoLog(logFieldsTree))
			throw NotAVideoEventLog(logFieldsTree.toString())
		return when (getEventTypeFromLog(logFieldsTree))
		{
			"play_video" -> extractPlayVideoEvent(logFieldsTree)
			"seek_video" -> extractSeekVideoEvent(logFieldsTree)
			"pause_video" -> extractPauseVideoEvent(logFieldsTree)
			"stop_video" -> extractStopVideoEvent(logFieldsTree)
			else -> throw NotAVideoEventLog(logFieldsTree.toString())
		}
	}

	private fun extractEventTimePauseStopPlayVideoLog(
			logEventFieldTree: JsonNode): Double
	{
		for (field in logEventFieldTree!!.fields())
		{
			if (field.key == "currentTime")
			{
				if (!field.value.isDouble)
					throw InvalidEventField(logEventFieldTree.fields().toString())
				return field.value.asDouble()
			}
			else
				continue
		}
		throw MissingLogData(logEventFieldTree.toString())
	}

	private fun extractEventTimeSeekVideoLog(logEventFieldTree: JsonNode): Double
	{
		for (field in logEventFieldTree!!.fields())
		{
			if (field.key == "old_time")
			{
				if (!field.value.isDouble)
					throw InvalidEventField(logEventFieldTree.fields().toString())
				return field.value.asDouble()
			}
			else
				continue
		}
		throw MissingLogData(logEventFieldTree.toString())
	}

	private fun extractUsernameVideoLog(logEventFileTree: Iterator<MutableMap.MutableEntry<String, JsonNode>>): String
	{
		for (field in logEventFileTree)
		{
			if (field.key != "username")
				continue
			if (!field.value.isTextual)
				throw InvalidUsernameField(logEventFileTree.toString())
			return field.value.asText()
		}
		throw InvalidUsernameField(logEventFileTree.toString())
	}

	private fun extractVideoIdVideoLog(logEventFieldTree: JsonNode): String
	{
		for (field in logEventFieldTree.fields())
		{
			if (field.key != "id")
				continue
			if (!field.value.isTextual)
				throw MissingVideoId(logEventFieldTree.toString())
			return field.value.asText()
		}
		throw MissingVideoId(logEventFieldTree.toString())
	}

	private fun extractPauseVideoEvent(logFieldsTree: Iterator<MutableMap.MutableEntry<String, JsonNode>>): VideoEvent
	{
		var logEventFieldTree: JsonNode? = null
		var videoId: String? = null
		var videoTime: Double? = null
		val eventType = "pause"
		var userName: String? = null


		for (field in logFieldsTree)
		{
			if (field.key != "event")
				continue
			if (!field.value.isTextual)
				throw InvalidEventField(logFieldsTree.toString())
			logEventFieldTree = field.value
		}

		for (field in logEventFieldTree!!.fields())
		{
			if (field.key == "id")
			{
				if (!field.value.isTextual)
					throw InvalidEventField(logEventFieldTree.fields().toString())
				videoId = field.value.asText()
			}
			else if (field.key == "currentTime")
			{
				if (!field.value.isDouble)
					throw InvalidEventField(logEventFieldTree.fields().toString())
				videoTime = field.value.asDouble()
			}
			else
				continue
		}

		for (field in logFieldsTree)
		{
			if (field.key != "username")
				continue
			if (field.value.isTextual)
				throw InvalidUsernameField(logFieldsTree.toString())
			userName = field.value.asText()
		}

		if (userName.isNullOrEmpty() || videoTime == null || videoId.isNullOrEmpty())
		{
			throw MissingLogData(logEventFieldTree.toString())
		}

		return VideoEvent(
				eventTime = videoTime,
				eventType = eventType,
				videoId = videoId,
				userName = userName
		)
	}

	private fun getEventTypeFromLog(logFieldsTree: Iterator<MutableMap.MutableEntry<String, JsonNode>>): String {
		if (!isVideoLog(logFieldsTree))
			throw NotAVideoEventLog(logFieldsTree.toString())
		for (field in logFieldsTree)
		{
			if (field.key !in this.VIDEO_EVENT_TYPES)
				continue
			if (!field.value.isTextual)
				throw InvalidEventTypeField(logFieldsTree.toString())
			return field.value.asText()
		}
		throw EventTypeFieldNotFound(logFieldsTree.toString())
	}

	private fun isVideoLog(logFieldsTree: Iterator<MutableMap.MutableEntry<String, JsonNode>>): Boolean {
		for (field in logFieldsTree)
		{
			if (field.key != "video_event")
				continue
			if (!field.value.isTextual)
				continue
			return field.value.asText() in this.VIDEO_EVENT_TYPES
		}
		return false
	}

	fun extractVideoEvents() {
		val reader = BufferedReader (FileReader(logFileName))
		var logLine = reader.readLine()

		val logTreeRoot = this.mapper.readTree(logLine)
		val logFieldsTree = logTreeRoot.fields()
	}
}

fun main() {
	val parser = LogParser("src/resources/spbu_DEL_OBS_fall_2018")
	parser.countVideoLogs()
}

