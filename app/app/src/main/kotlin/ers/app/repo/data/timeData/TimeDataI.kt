package ers.app.repo.data.timeData

import ers.app.repo.dtos.TimeDto
import java.sql.Time

interface TimeDataI {
    fun createTime (taskID: Int, startTime: Time, endTime: Time, weekDay: String, description: String) : TimeDto
    fun getTimeByID(id: Int) : TimeDto?
    fun updateTime (id : Int, startTime : Time, endTime : Time, weekDay : String) : TimeDto
    fun updateTimeDescription (id : Int, description : String) : TimeDto
    fun deleteTime (id : Int)
    fun getTimesByTaskID (offset: Int, limit: Int, taskID: Int): List<TimeDto>
}