package ers.app.repo.transaction

import ers.app.repo.data.areaData.AreaData
import ers.app.repo.data.robotData.RobotData
import ers.app.repo.data.taskData.TaskData
import ers.app.repo.data.timeData.TimeData
import ers.app.repo.data.usersData.UsersData
import org.jdbi.v3.core.Handle

class JdbiTransaction(
        private val handle:Handle
) : Transaction {
    override val areaData = AreaData(handle)
    override val robotData = RobotData(handle)
    override val taskData = TaskData(handle)
    override val timeData = TimeData(handle)
    override val usersData = UsersData(handle)

    override fun rollback() {
        handle.rollback()
    }

}
