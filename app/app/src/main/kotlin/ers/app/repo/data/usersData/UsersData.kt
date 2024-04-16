package ers.app.repo.data.usersData

import org.jdbi.v3.core.Handle
import ers.app.repo.mappers.UserDtoMapper
import ers.app.repo.dtos.UserDto
import java.util.*

/**
 * Class that implements the UsersDataI interface and contains the methods to interact with the database for the users
 * @property handle Handle to connect to the database
 */
class UsersData(private val handle: Handle) : UsersDataI {

    //PROVAVELMENTE NÃO IRÁ RETORNAR UM DTO MAS UM USER NO FUTURO

    /**
     * Method to create a new user in the database
     * @param name Name of the user
     * @param email Email of the user
     * @param password Password of the user
     * @return UserDto? Returns the user created
     */
    override fun createUser(name: String, email: String, password: String): UserDto? {
        val hashPass = password.hashCode()
        val token = UUID.randomUUID().toString()
        val newUser =
            handle.createUpdate("INSERT INTO users (name, email, password, token) VALUES (:name, :email, :password, :token)")
                .bind("name", name)
                .bind("email", email)
                .bind("password", hashPass)
                .bind("token", token)
                .executeAndReturnGeneratedKeys()
                .map(UserDtoMapper())
                .singleOrNull()
        return newUser
    }

    /**
     * Method to get a user by its id
     * @param id Id of the user
     * @return UserDto? Returns the user found
     */
    override fun getUserById(id: Int): UserDto? {
        val user = handle.createQuery("SELECT * FROM users WHERE id = :id")
            .bind("id", id)
            .map(UserDtoMapper())
            .singleOrNull()
        return user
    }

    /**
     * Method to get a user by its token
     * @param token Token of the user
     * @return UserDto? Returns the user found
     */
    override fun getUserByToken(token: String): UserDto? {
        val user = handle.createQuery("SELECT * FROM users WHERE token = :token")
            .bind("token", token)
            .map(UserDtoMapper())
            .singleOrNull()
        return user
    }

    /**
     * Method to login a user
     * @param email Email of the user
     * @param password Password of the user
     * @return UserDto? Returns the user found
     */
    override fun loginUser(email: String, password: String): UserDto? {
        val hashPass = password.hashCode()
        val user = handle.createQuery("SELECT * FROM users WHERE email = :email AND password = :password")
            .bind("email", email)
            .bind("password", hashPass)
            .map(UserDtoMapper())
            .singleOrNull()
        return user
    }
}