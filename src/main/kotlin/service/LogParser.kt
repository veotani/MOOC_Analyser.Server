package services

import com.fasterxml.jackson.databind.JsonNode
import java.io.FileReader
import java.io.BufferedReader
import com.fasterxml.jackson.databind.ObjectMapper


data class VideoEvent(
		val eventTime: Double,
		val eventType: String,
		val videoId: String,
		val userName: String
)


class LogParser(logFileName: String) {
	private val mapper = ObjectMapper()
	private val reader = BufferedReader (FileReader(logFileName))
	private val videoEventTypesSet = setOf("play_video", "seek_video", "pause_video", "stop_video")

	fun extractVideoEventsFromLogs(): MutableList<VideoEvent>
	{
		val videoEvents = mutableListOf<VideoEvent>()
		var line = reader.readLine()
		while (line != null)
		{
			val log = logToJSON(line)
			line = reader.readLine()
			if (!isVideoLog(log))
				continue

			val videoEvent = getVideoEventDataOfLog(log)
			videoEvents.add(videoEvent)
		}
		return videoEvents
	}

	private fun logToJSON(log: String): JsonNode
	{
		return mapper.readTree(log)
	}

	private fun isSeekVideoLog(log: JsonNode): Boolean
	{
		return log["event_type"].asText() == "seek_video"
	}

	private fun isVideoLog(log: JsonNode): Boolean
	{
		if (!log.has("event_type"))
			return false

		val eventType = log["event_type"].asText()
		if (!videoEventTypesSet.contains(eventType))
			return false

		return true
	}

	private fun getVideoEventDataOfLog(log: JsonNode): VideoEvent
	{
		// Fields for video event object
		val videoEventType: String
		val videoId: String
		val userName: String = log["username"].asText()
		val videoEventTime: Double

		val eventType = log["event_type"].asText()
		videoEventType = when(eventType) {
			"play_video" -> "play"
			else         -> "pause"
		}

		val eventTree = mapper.readTree(log["event"].asText())
		videoId = eventTree["id"].asText()

		videoEventTime = if (eventType == "seek_video")
			eventTree["old_time"].asDouble()
		else
			eventTree["currentTime"].asDouble()

		return VideoEvent(videoEventTime, videoEventType, videoId, userName)
	}
}
