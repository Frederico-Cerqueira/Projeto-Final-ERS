package ers.app.servicesTest


import ers.app.delete
import ers.app.utils.errors.*

import ers.app.insert
import ers.app.jdbiSetup
import ers.app.repo.transaction.JdbiTransactionManager
import ers.app.service.UserService
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class UserServicesTests {

    private val jdbi = jdbiSetup()
    private val manager = JdbiTransactionManager(jdbi)
    private val userServices = UserService(manager)

    @BeforeTest
    fun init() {
        insert(jdbi)
    }

    @AfterTest
    fun end() {
        delete(jdbi)
    }

    @Test
    fun `create user test`() {
        val result = userServices.createUser("Mafalda", "mafalda@gmail.com", "123456")
        if (result is Success) {
            assertEquals(3, result.value.id)
            assertEquals("Mafalda", result.value.name)
        }
    }

    @Test
    fun `create user test with invalid email`() {
        val result = userServices.createUser("Mafalda", "mafalda", "123456")
        if (result is Failure) {
            assertEquals(InvalidEmail, result.value)
        }
    }

    @Test
    fun `create user test with invalid input`() {
        val result = userServices.createUser("", "", "")
        if (result is Failure) {
            assertEquals(InvalidInput, result.value)
        }
    }

    @Test
    fun `create user test with input too long`() {
        val result = userServices.createUser(
            "Mafaldaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
            "",
            ""
        )
        if (result is Failure) {
            assertEquals(InputTooLong, result.value)
        }
    }

    @Test
    fun `create user test with user already exists`() {
        val result = userServices.createUser("Maria", "maria@gmail.com", "123456")
        if (result is Failure) {
            assertEquals(UserAlreadyExists, result.value)
        }
    }

    @Test
    fun `get user by id test`() {
        val result = userServices.getUserByID(1)
        if (result is Success) {
            assertEquals(1, result.value.id)
            assertEquals("Maria", result.value.name)
        }
    }

    @Test
    fun `get user by id test with invalid id`() {
        val result = userServices.getUserByID(300)
        if (result is Failure) {
            assertEquals(UserNotFound, result.value)
        }
    }

    @Test
    fun `get user by token test`() {
        val result = userServices.getUserByToken("1")
        if (result is Success) {
            assertEquals(1, result.value.id)
            assertEquals("Maria", result.value.name)
        }
    }

    @Test
    fun `get user by token test with invalid token`() {
        val result = userServices.getUserByToken("token300")
        if (result is Failure) {
            assertEquals(UserNotFound, result.value)
        }
    }

    @Test
    fun `login user test`() {
        val result = userServices.loginUser("maria@gmail.com", "123456")
        if (result is Success) {
            assertEquals(1, result.value.id)
            assertEquals("Maria", result.value.name)
        }
    }

    @Test
    fun `login user test with invalid email`() {
        val result = userServices.loginUser("ana@gmail.com", "123456")
        if (result is Failure) {
            assertEquals(UserNotFound, result.value)
        }
    }

}