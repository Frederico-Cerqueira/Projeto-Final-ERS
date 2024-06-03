package ers.app.repoTest

import ers.app.delete
import ers.app.insert
import ers.app.jdbiSetup
import ers.app.repo.dtos.UserDto
import ers.app.repo.transaction.JdbiTransactionManager
import kotlin.test.*

class UsersDataTests {

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
    fun createUserTest() {
        manager.run {
            val user =
                it.usersData.createUser("Mafalda", "mafalda@gmail.com", 1234, "8e0d8035-972b-482f-8a72-239e4b7142c9")
            val dto = UserDto(3, "Mafalda", "mafalda@gmail.com", "1234", "8e0d8035-972b-482f-8a72-239e4b7142c9")
            assertEquals(dto, user)
        }
    }

    @Test
    fun `get user by Id`() {
        val hashPass = "123456".hashCode().toString()
        manager.run {
            val user = it.usersData.getUserByID(1)
            val dto = UserDto(1, "Maria", "maria@gmail.com", hashPass, "1")
            assertEquals(dto, user)
        }
    }

    @Test
    fun `get user by ID with invalid id`() {
        manager.run {
            val user = it.usersData.getUserByID(10)
            assertNull(user)
        }
    }

    @Test
    fun `get user by token`() {
        val hashPass = "123456".hashCode().toString()
        val token = "1"
        manager.run {
            val user = it.usersData.getUserByToken(token)
            val dto = UserDto(1, "Maria", "maria@gmail.com", hashPass, "1")
            assertEquals(dto, user)
        }
    }

    @Test
    fun `get user by token with invalid token`() {
        manager.run {
            val user = it.usersData.getUserByToken("10")
            assertNull(user)
        }
    }

    @Test
    fun `login user`() {
        val hasPass = "123456".hashCode().toString()
        manager.run {
            val user = it.usersData.loginUser("maria@gmail.com", "123456")
            val dto = UserDto(1, "Maria", "maria@gmail.com", hasPass, "1")
            assertEquals(dto, user)
        }
    }

    @Test
    fun `login user with invalid email`() {
        val pass = (123456).toString()
        manager.run {
            val user = it.usersData.loginUser("a@gmaul.com", pass)
            assertNull(user)
        }
    }

    @Test
    fun `login user with invalid password`() {
        manager.run {
            val user = it.usersData.loginUser("maria@gmail.com", "abc")
            assertNull(user)
        }
    }

    @Test
    fun `get user by email`() {
        val hashPass = "123456".hashCode().toString()
        manager.run {
            val user = it.usersData.getUserByEmail("maria@gmail.com")
            val dto = UserDto(1, "Maria", "maria@gmail.com", hashPass, "1")
            assertEquals(dto, user)
        }
    }

    @Test
    fun `get user by email with invalid email`() {
        manager.run {
            val user = it.usersData.getUserByEmail("a@gmail.com")
            assertNull(user)
        }
    }


}