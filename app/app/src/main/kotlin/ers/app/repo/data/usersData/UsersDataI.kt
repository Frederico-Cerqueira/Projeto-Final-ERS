package ers.app.repo.data.usersData

import ers.app.repo.dtos.UserDto

interface UsersDataI {
    fun createUser(name: String, email: String, password: String) : UserDto?
    fun getUser()
    fun getUserById()
    fun getUserByToken()
    fun loginUser()
}