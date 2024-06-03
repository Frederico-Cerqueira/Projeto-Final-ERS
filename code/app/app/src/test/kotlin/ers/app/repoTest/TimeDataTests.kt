package ers.app.repoTest

import ers.app.delete
import ers.app.insert
import ers.app.jdbiSetup
import ers.app.repo.dtos.TimeDto
import ers.app.repo.transaction.JdbiTransactionManager
import java.sql.Time
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class TimeDataTests {
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
    fun `create time test`() {
        manager.run {
            val time =
                it.timeData.createTime(1, Time.valueOf("10:00:00"), Time.valueOf("12:00:00"), "Monday", "description")
            val dto = TimeDto(
                5,
                1,
                "Monday",
                Time.valueOf("10:00:00").toString(),
                Time.valueOf("12:00:00").toString(),
                "description"
            )
            assertEquals(dto, time)
        }
    }

    @Test
    fun `get time by id`() {
        manager.run {
            val time = it.timeData.getTimeByID(1)
            val dto = TimeDto(
                1,
                1,
                "Friday",
                Time.valueOf("10:00:00").toString(),
                Time.valueOf("12:00:00").toString(),
                "time1"
            )
            assertEquals(dto, time)
        }
    }

    @Test
    fun `get time by id with invalid id`() {
        manager.run {
            val time = it.timeData.getTimeByID(10)
            assertEquals(null, time)
        }
    }

    @Test
    fun `update time`() {
        manager.run {
            val time = it.timeData.updateTime(1, Time.valueOf("11:00:00"), Time.valueOf("13:00:00"), "Tuesday")
            val dto = TimeDto(
                1,
                1,
                "Tuesday",
                Time.valueOf("11:00:00").toString(),
                Time.valueOf("13:00:00").toString(),
                "time1"
            )
            assertEquals(dto, time)
        }
    }

    @Test
    fun `update time description`() {
        manager.run {
            val time = it.timeData.updateTimeDescription(1, "new description")
            val dto = TimeDto(
                1,
                1,
                "Friday",
                Time.valueOf("10:00:00").toString(),
                Time.valueOf("12:00:00").toString(),
                "new description"
            )
            assertEquals(dto, time)
        }
    }

    @Test
    fun `delete time`() {
        manager.run {
            it.timeData.deleteTime(1)
            val time = it.timeData.getTimeByID(1)
            assertEquals(null, time)
        }
    }

    @Test
    fun `get times by task id`(){
        manager.run {
            val times = it.timeData.getTimesByTaskID(0, 10, 3)
            val dto = TimeDto(
                3,
                3,
                "Friday",
                Time.valueOf("10:00:00").toString(),
                Time.valueOf("12:00:00").toString(),
                "time3"
            )
            val dto2 = TimeDto(
                4,
                3,
                "Monday",
                Time.valueOf("10:00:00").toString(),
                Time.valueOf("12:00:00").toString(),
                "time4"
            )
            assertEquals(listOf(dto, dto2), times)
        }
    }

    @Test
    fun `get times by task id with limit equals 1`(){
        manager.run {
            val times = it.timeData.getTimesByTaskID(0, 1, 3)
            val dto = TimeDto(
                3,
                3,
                "Friday",
                Time.valueOf("10:00:00").toString(),
                Time.valueOf("12:00:00").toString(),
                "time3"
            )
            assertEquals(listOf(dto), times)
        }
    }

}