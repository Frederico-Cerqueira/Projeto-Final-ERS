package ers.app.servicesTest

import ers.app.delete
import ers.app.utils.errors.Failure
import ers.app.utils.errors.Success
import ers.app.insert
import ers.app.jdbiSetup
import ers.app.repo.dtos.RobotDto
import ers.app.repo.transaction.JdbiTransactionManager
import ers.app.service.RobotService
import ers.app.utils.errors.*
import kotlin.test.*

class RobotServicesTests {

    private val jdbi = jdbiSetup()
    private val manager = JdbiTransactionManager(jdbi)
    private val robotServices = RobotService(manager)

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
        val result = robotServices.createRobot("Robot4", "metal")
        if (result is Success) {
            assert(result.value.id == 4)
            assert(result.value.name == "Robot4")
            assert(result.value.status == "available")
            assert(result.value.characteristics == "metal")
        }
    }

    @Test
    fun `create robot test with invalid input`() {
        val result = robotServices.createRobot("", "")
        if (result is Failure) {
            assertEquals(InvalidInput, result.value)
        }
    }

    @Test
    fun `get robot by ID`(){
        val result = robotServices.getRobotByID(1)
        if (result is Success) {
            assertEquals(1, result.value.id)
            assertEquals("Robot1", result.value.name)
            assertEquals("available", result.value.status)
            assertEquals("plastic", result.value.characteristics)
        }
    }

    @Test
    fun `get robot by ID with invalid ID`(){
        val result = robotServices.getRobotByID(300)
        if (result is Failure) {
            assertEquals(RobotNotFound, result.value)
        }
    }

    @Test
    fun `update robot status`(){
        val result = robotServices.updateRobotStatus(1, "busy")
        if (result is Success) {
            assertEquals(1, result.value.id)
            assertEquals("Robot1", result.value.name)
            assertEquals("busy", result.value.status)
            assertEquals("plastic", result.value.characteristics)
        }
    }

    @Test
    fun `update robot status with invalid status`(){
        val result = robotServices.updateRobotStatus(1, "invalid")
        if (result is Failure) {
            assertEquals(InvalidInput, result.value)
        }
    }

    @Test
    fun `update robot status with invalid ID`(){
        val result = robotServices.updateRobotStatus(300, "busy")
        if (result is Failure) {
            assertEquals(RobotNotFound, result.value)
        }
    }

    @Test
    fun `delete robot`(){
        val result = robotServices.deleteRobot(1)
        if (result is Success) {
            assertEquals(1, result.value.id)
        }
    }

    @Test
    fun `delete robot with invalid ID`(){
        val result = robotServices.deleteRobot(300)
        if (result is Failure) {
            assertEquals(RobotNotFound, result.value)
        }
    }

    @Test
    fun `get robot by user ID`(){
        val result = robotServices.getRobotByUserID(0, 10, 1)
        if (result is Success) {
            val dto = RobotDto(1, "Robot1", "available", "plastic")
            val dto2 = RobotDto(2, "Robot2", "available", "glass")
            assertEquals(listOf(dto, dto2), result.value.robots)
        }
    }

    @Test
    fun `get robot by user ID with invalid ID`(){
        val result = robotServices.getRobotByUserID(0, 10, 300)
        if (result is Failure) {
            assertEquals(UserNotFound, result.value)
        }
    }

    @Test
    fun `get robot by user ID with limit equals 1`(){
        val result = robotServices.getRobotByUserID(0, 1, 1)
        val dto = RobotDto(1, "Robot1", "available", "plastic")
        if (result is Success) {
            assertEquals(listOf(dto), result.value.robots)
        }
    }



}