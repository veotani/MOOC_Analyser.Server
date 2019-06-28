package services

import model.PlotPointsDTO
import model.Point
import repositories.VideoEventRepository

class VideoEventsAnalysisService(private val fileName: String)
{
    private val repository = VideoEventRepository()

    fun getGlobalPointsForUserVideo(userName: String, videoId: String): PlotPointsDTO
    {
        val intervals = getUserVideoWatchIntervals(userName, videoId)
        return getPlotPointsByIntervals(intervals)
    }

    private fun getPlotPointsByIntervals(intervals: List<Pair<Double, Double>>): PlotPointsDTO
    {
        val START_LABEL = "start"
        val STOP_LABEL  = "stop"

        var labeledPoints = mutableListOf<Pair<Double, String>>()

        for (interval in intervals)
        {
            labeledPoints.add(interval.first to START_LABEL)
            labeledPoints.add(interval.second to STOP_LABEL)
        }

        var currentPlayCount = 0

        val plotPoints = mutableListOf<Point>()

        labeledPoints.sortBy { point -> point.first }
        for (point in labeledPoints)
        {
            when (point.second)
            {
                START_LABEL -> currentPlayCount++
                STOP_LABEL -> if (currentPlayCount > 0) currentPlayCount--
            }
            plotPoints.add(Point(point.first,currentPlayCount))
        }

        // plotPoints.sortBy { point -> point.x }
        return PlotPointsDTO(plotPoints.toList())
    }

    private fun getUserVideoWatchIntervals(userName: String, videoId: String): List<Pair<Double, Double>>
    {
        val videoEvents = repository.getVideoEventByUserNameVideoIdFileName(
                userName,
                videoId,
                fileName
        )
        var intervals = mutableListOf<Pair<Double, Double>>()

        var isPrevPlay = true           // Last video event was pause event
                                        // It's necessary to check because there can't be two pause or play events
                                        // going one after another (it's impossible to pause video that's already
                                        // paused and impossible to play video that's already playing)

        var prevPlayTime = .0           // Previous play video event time. Pause video event time is not necessary
                                        // to store because it's added into map immediately after being spotted.

        for (event in videoEvents)
        {
            when (isPrevPlay)
            {
                true ->
                {
                    if (event.eventType == "pause")
                    {
                        intervals.add(prevPlayTime to event.eventTime)
                        isPrevPlay = false
                    }
                }
                false ->
                {
                    if (event.eventType == "play")
                    {
                        prevPlayTime = event.eventTime
                        isPrevPlay = true
                    }
                }
            }
        }

        return intervals
    }
}
