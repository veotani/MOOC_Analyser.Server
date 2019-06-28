package web.api.controller

import managers.PlotManager
import model.PlotPointsDTO
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin
class PointsController
{
    private val plotManager = PlotManager()

    @GetMapping("/individualVideoAnalysis")
    fun individualVideoAnalysis(@RequestParam(value = "userName") userName: String,
                                @RequestParam(value = "videoId")  videoId:  String,
                                @RequestParam(value = "fileName") fileName: String)
    : PlotPointsDTO
    {
        this.plotManager.initService(fileName)
        return this.plotManager.getLogFilePlotPointsByUserVideoId(userName, videoId)
    }
}

