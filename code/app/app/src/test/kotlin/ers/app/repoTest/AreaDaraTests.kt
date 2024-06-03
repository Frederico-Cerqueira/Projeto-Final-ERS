package ers.app.repoTest

import ers.app.delete
import ers.app.insert
import ers.app.jdbiSetup
import ers.app.repo.dtos.AreaDto
import ers.app.repo.transaction.JdbiTransactionManager
import kotlin.test.*

class AreaDaraTests {
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
    fun `create area test`() {
        manager.run {
            val area = it.areaData.createArea(10, 10, 1, "Area5", "description5")
            val dto = AreaDto(5, 1, 10, 10, "Area5", "description5")
            assertEquals(dto, area)
        }
    }

    @Test
fun `get area by id`() {
        manager.run {
            val area = it.areaData.getAreaByID(1)
            val dto = AreaDto(1, 1, 10, 10, "Area1", "description1")
            assertEquals(dto, area)
        }
    }

    @Test
    fun `get area by id with invalid id`() {
        manager.run {
            val area = it.areaData.getAreaByID(10)
            assertNull(area)
        }
    }

    @Test
    fun `update area height and width`() {
        manager.run {
            val area = it.areaData.updateArea(1, 20, 20)
            val dto = AreaDto(1, 1, 20, 20, "Area1", "description1")
            assertEquals(dto, area)
        }
    }

    @Test
    fun `update area description`() {
        manager.run {
            val area = it.areaData.updateAreaDescription(1, "new description")
            val dto = AreaDto(1, 1, 10, 10, "Area1", "new description")
            assertEquals(dto, area)
        }
    }

    @Test
    fun `delete area`() {
        manager.run {
            it.areaData.deleteArea(1)
            val area = it.areaData.getAreaByID(1)
            assertNull(area)
        }
    }

    @Test
    fun `get areas by task id`() {
        manager.run {
            val areas = it.areaData.getAreasByTaskID(0, 10, 3)
            val dto = AreaDto(3, 3, 10, 10, "Area3", "description3")
            val dto2 = AreaDto(4, 3, 10, 10, "Area4", "description4")
            assertEquals(listOf(dto,dto2), areas)
        }
    }

    @Test
    fun `get areas by task id with limit equals 1`() {
        manager.run {
            val areas = it.areaData.getAreasByTaskID(0, 1, 3)
            val dto = AreaDto(3, 3, 10, 10, "Area3", "description3")
            assertEquals(listOf(dto), areas)
        }
    }



}