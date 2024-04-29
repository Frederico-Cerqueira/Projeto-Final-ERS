package ers.app.servicesTest

import ers.app.delete
import ers.app.insert
import ers.app.jdbiSetup
import ers.app.repo.transaction.JdbiTransactionManager
import ers.app.service.RobotService
import kotlin.test.*

class TaskServicesTests {

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


}