package managers

import model.PlotPointsDTO
import services.VideoEventsAnalysisService
import java.lang.Exception

class PlotManager
{
    private var videoAnalysisService: VideoEventsAnalysisService? = null

    fun getLogFilePlotPointsByUserVideoId(userName: String, videoId: String): PlotPointsDTO
    {
        if (this.videoAnalysisService == null)
        {
            throw Exception("Video analysis service is not initialized")
        }
        return this.videoAnalysisService!!.getGlobalPointsForUserVideo(userName, videoId)
    }

    fun getLogFilePlotPointsByVideoId(videoId: String): PlotPointsDTO
    {
        TODO()
    }

    fun initService(fileName: String)
    {
        this.videoAnalysisService = VideoEventsAnalysisService(fileName)
    }
}