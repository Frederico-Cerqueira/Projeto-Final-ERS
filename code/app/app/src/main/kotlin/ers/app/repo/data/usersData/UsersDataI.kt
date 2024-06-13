package ers.app.repo.data.usersData

import ers.app.repo.dtos.UserDto

/**
 * Interface that defines the operations related to the user table in the database.
 */
interface UsersDataI {
    fun createUser(name: String, email: String, hashPass: Int,token: String) : UserDto
    fun getUserByID(id: Int) : UserDto?
    fun getUserByToken(token : String) : UserDto?
    fun loginUser(email: String, password: String,token:String) : UserDto?
    fun logoutUser(id: Int, token: String) : UserDto?
    fun getUserByEmail(email: String) : UserDto?
}