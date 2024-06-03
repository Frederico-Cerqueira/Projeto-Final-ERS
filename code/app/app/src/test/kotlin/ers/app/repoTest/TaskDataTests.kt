package ers.app.repoTest

import ers.app.delete
import ers.app.insert
import ers.app.jdbiSetup
import ers.app.repo.dtos.TaskDto
import ers.app.repo.transaction.JdbiTransactionManager
import kotlin.test.*

class TaskDataTests {
    private val jdbi = jdbiSetup()
    private val manager = JdbiTransactionManager(jdbi)

    @BeforeTest
    fun init() {
        insert(jdbi)
    }

    @AfterTest
    fun end() {
        delete(jdbi)
    }

    @Test
    fun `create task test`() {
        manager.run {
            val task = it.taskData.createTask("Task5", 1, 1)
            val dto = TaskDto(5, "Task5", 1, 1, "pending")
            assertEquals(dto, task)
        }
    }

    @Test
    fun `get task by id`() {
        manager.run {
            val task = it.taskData.getTaskByID(1)
            val dto = TaskDto(1, "Task1", 1, 1, "pending")
            assertEquals(dto, task)
        }
    }

    @Test
    fun `get task by id with invalid id`() {
        manager.run {
            val task = it.taskData.getTaskByID(10)
            assertNull(task)
        }
    }

    @Test
    fun `update task status`() {
        manager.run {
            val task = it.taskData.updateTask(1, "completed")
            val dto = TaskDto(1, "Task1", 1, 1, "completed")
            assertEquals(dto, task)
        }
    }

    @Test
    fun `delete task`() {
        manager.run {
            it.taskData.deleteTask(1)
            val task = it.taskData.getTaskByID(1)
            assertNull(task)
        }
    }

    @Test
    fun `get tasks by user id`() {
        manager.run {
            val tasks = it.taskData.getTasksByUserID(0, 10, 1)
            val dto = TaskDto(1, "Task1", 1, 1, "pending")
            val dto2 = TaskDto(2, "Task2", 1, 2, "pending")
            val dto3 = TaskDto(4, "Task4", 1, 1, "pending")
            assertEquals(listOf(dto,dto2,dto3), tasks)
        }
    }

    @Test
    fun `get tasks by user id with limit equals 2`() {
        manager.run {
            val tasks = it.taskData.getTasksByUserID(0, 2, 1)
            val dto = TaskDto(1, "Task1", 1, 1, "pending")
            val dto2 = TaskDto(2, "Task2", 1, 2, "pending")
            assertEquals(listOf(dto,dto2), tasks)
        }
    }

    @Test
    fun `get tasks by robot id`() {
        manager.run {
            val tasks = it.taskData.getTasksByRobotID(0, 10, 1)
            val dto = TaskDto(1, "Task1", 1, 1, "pending")
            val dto2 = TaskDto(4, "Task4", 1, 1, "pending")
            assertEquals(listOf(dto, dto2), tasks)
        }
    }

    @Test
    fun `get tasks by robot id with limits equals 1`() {
        manager.run {
            val tasks = it.taskData.getTasksByRobotID(0, 1, 1)
            val dto = TaskDto(1, "Task1", 1, 1, "pending")
            assertEquals(listOf(dto), tasks)
        }
    }

}