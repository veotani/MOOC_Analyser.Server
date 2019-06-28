package service

import repositories.VideoEventRepository

class LogInfoService
{
    val repository = VideoEventRepository()
    fun getFileNames(): List<String>
    {
        return repository.getVideoEventFileNames().toList()
    }

    fun getVideoIds(fileName: String): List<String>
    {
        return repository.getVideoIdForFile(fileName)
    }

    fun getUserNames(fileName: String): List<String>
    {
        return repository.getUserNamesForFile(fileName)
    }
}