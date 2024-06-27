package ers.app.repo.data.usersData

import org.jdbi.v3.core.Handle
import ers.app.repo.mappers.UserDtoMapper
import ers.app.repo.dtos.UserDto


/**
 * Class that implements the UsersDataI interface and contains the methods to interact with the database for the users
 * @property handle Handle to connect to the database
 */
class UsersData(private val handle: Handle) : UsersDataI {

    /**
     * Method to create a new user in the database
     * @param name Name of the user
     * @param email Email of the user
     * @param hashPass Password of the user
     * @return UserDto? Returns the user created
     */
    override fun createUser(name: String, email: String, hashPass: Int, token: String): UserDto {
        return handle.createUpdate("INSERT INTO users (name, email, hashPass, token) VALUES (:name, :email, :hashPass, :token)")
            .bind("name", name)
            .bind("email", email)
            .bind("hashPass", hashPass)
            .bind("token", token)
            .executeAndReturnGeneratedKeys()
            .map(UserDtoMapper())
            .first()
    }

    /**
     * Method to get a user by its id
     * @param id ID of the user
     * @return UserDto? Returns the user found
     */
    override fun getUserByID(id: Int): UserDto? {
        return handle.createQuery("SELECT * FROM users WHERE id = :id")
            .bind("id", id)
            .map(UserDtoMapper())
            .singleOrNull()
    }

    /**
     * Method to get a user by its token
     * @param token Token of the user
     * @return UserDto? Returns the user found
     */
    override fun getUserByToken(token: String): UserDto? {
        return handle.createQuery("SELECT * FROM users WHERE token = :token")
            .bind("token", token)
            .map(UserDtoMapper())
            .singleOrNull()
    }

    /**
     * Method to log in a user
     * @param email Email of the user
     * @param password Password of the user
     * @param token New token of the user
     * @return UserDto? Returns the user found
     */
    override fun loginUser(email: String, password: String,token: String): UserDto? {
        val hashPass = password.hashCode()
        return handle.createUpdate("UPDATE users SET token = :token WHERE email = :email AND hashPass = :hashPass")
            .bind("token", token)
            .bind("email", email)
            .bind("hashPass", hashPass)
            .executeAndReturnGeneratedKeys()
            .map(UserDtoMapper())
            .first()
    }

    /**
     * Method to log out a user
     * @param id ID of the user
     * @return UserDto? Returns the user found
     */
    override fun logoutUser(id: Int): UserDto? {
        return handle.createUpdate("UPDATE users SET token = null WHERE id = :id")
            .bind("id", id)
            .executeAndReturnGeneratedKeys()
            .map(UserDtoMapper())
            .first()
    }

    /**
     * Method to get a user by its email
     * @param email Email of the user
     * @return UserDto? Returns the user found
     */
    override fun getUserByEmail(email: String): UserDto? {
        return handle.createQuery("SELECT * FROM users WHERE email = :email")
            .bind("email", email)
            .map(UserDtoMapper())
            .singleOrNull()
    }
}