package model

data class Point(val x: Double, var y: Int)

data class PlotPointsDTO(val points: List<Point>)
