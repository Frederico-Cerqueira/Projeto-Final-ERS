package ers.app.repo.transaction

import ers.app.repo.data.areaData.AreaDataI
import ers.app.repo.data.robotData.RobotDataI
import ers.app.repo.data.taskData.TaskDataI
import ers.app.repo.data.timeData.TimeDataI
import ers.app.repo.data.usersData.UsersDataI
import org.springframework.stereotype.Component

@Component
interface Transaction {
    val robotData: RobotDataI
    val areaData: AreaDataI
    val taskData: TaskDataI
    val usersData: UsersDataI
    val timeData: TimeDataI

    // other repository types
    fun rollback()
}