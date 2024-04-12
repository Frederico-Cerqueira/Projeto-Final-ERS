package ers.app.repo.data.taskData

import org.jdbi.v3.core.Handle

class TaskData (private val handle: Handle) : TaskDataI {
    override fun createTask() {
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun getTask() {
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun getTaskById() {
        throw UnsupportedOperationException("Not yet implemented")
    }
}