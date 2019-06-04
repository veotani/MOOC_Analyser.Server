import java.io.IOException
import java.io.FileReader
import java.io.BufferedReader

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.core.type.TypeReference


class LogParser(val logFileName: String) {

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

	fun countVideoLogs() {
		val reader = BufferedReader (FileReader(logFileName))
		var line = reader.readLine()
		val mapper = ObjectMapper()
		val root = mapper.readTree(line)
		val rootFields = root.fields()
		for (field in rootFields) {
			println(field.key)
			// println(field.value)
		}

	}
}

fun main() {
	val parser = LogParser("src/resources/spbu_DEL_OBS_fall_2018")
	parser.countVideoLogs()
}

