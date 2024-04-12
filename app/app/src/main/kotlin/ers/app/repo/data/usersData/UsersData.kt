package ers.app.repo.data.usersData

import org.jdbi.v3.core.Handle
import ers.app.repo.mappers.UserDtoMapper
import ers.app.repo.dtos.UserDto

class UsersData (private val handle: Handle) : UsersDataI {

    override fun createUser(name : String, email : String, password : String) : UserDto? =
        handle.createUpdate("INSERT INTO users (name, email, password) VALUES (:name, :email, :password)")
            .bind("name", name)
            .bind("email", email)
            .bind("password", password)
            .executeAndReturnGeneratedKeys()
            .map(UserDtoMapper())
            .singleOrNull()


    override fun getUser(){
        TODO()
    }

    override fun getUserById(){
        TODO()
    }

    override fun getUserByToken(){
        TODO()
    }

    override fun loginUser(){
        TODO()
    }
}