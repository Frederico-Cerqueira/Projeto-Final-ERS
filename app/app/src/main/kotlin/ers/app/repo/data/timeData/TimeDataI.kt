package ers.app.repo.data.timeData

import ers.app.repo.dtos.TimeDto
import java.sql.Time

interface TimeDataI {
    fun createTime (taskId : Int, startTime : Time, endTime : Time, weekDay : String) : TimeDto?
    fun updateTime (taskId : Int, startTime : Time, endTime : Time, weekDay : String) : TimeDto?
    fun deleteTime (id : Int)
    fun getTimeById (id : Int) : TimeDto?
    fun getTimeByTaskId (taskId : Int) : TimeDto?
}