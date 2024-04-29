package ers.app.repoTest

import ers.app.delete
import ers.app.insert
import ers.app.jdbiSetup
import ers.app.repo.dtos.RobotDto
import ers.app.repo.transaction.JdbiTransactionManager
import kotlin.test.*

class RobotDataTests {

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
    fun `create robot test`() {
        manager.run {
            val robot = it.robotData.createRobot("Robot4", "metal")
            val dto = RobotDto(4, "Robot4", "available", "metal")
            assertEquals(dto, robot)
        }
    }

    @Test
    fun `get robot by id`() {
        manager.run {
            val robot = it.robotData.getRobotByID(1)
            val dto = RobotDto(1, "Robot1", "available", "plastic")
            assertEquals(dto, robot)
        }
    }

    @Test
    fun `get robot by id with invalid id`() {
        manager.run {
            val robot = it.robotData.getRobotByID(10)
            assertNull(robot)
        }
    }

    @Test
    fun `update robot status`() {
        manager.run {
            val robot = it.robotData.updateRobotStatus(1, "unavailable")
            val dto = RobotDto(1, "Robot1", "unavailable", "plastic")
            assertEquals(dto, robot)
        }
    }

    @Test
    fun `delete robot`() {
        manager.run {
            it.robotData.deleteRobot(1)
            val robot = it.robotData.getRobotByID(1)
            assertNull(robot)
        }
    }

    @Test
    fun `get robot by user id`() {
        manager.run {
            val robots = it.robotData.getRobotByUserID(0, 10, 1)
            val dto1 = RobotDto(1, "Robot1", "available", "plastic")
            val dto2 = RobotDto(2, "Robot2", "available", "glass")
            assertEquals(listOf(dto1, dto2), robots)
        }
    }

    @Test
    fun `get robot by user id with limit equals 1`() {
        manager.run {
            val robots = it.robotData.getRobotByUserID(0, 1, 1)
            val dto1 = RobotDto(1, "Robot1", "available", "plastic")
            assertEquals(listOf(dto1), robots)
        }
    }

}