package ers.app.service

import ers.app.domainEntities.*
import ers.app.repo.dtos.TaskDto
import ers.app.repo.transaction.TransactionManager
import ers.app.utils.Error
import org.springframework.stereotype.Component
import java.util.*

data class UserOutputModel(val id: Int, val name: String, val email: String, val token: String)


@Component
class UsersService(private val transactionManager: TransactionManager) {

    fun createUser(name: String, email: String, password: String): UserResult =
        transactionManager.run {
            try {
                if (it.usersData.getUserByEmail)
                    failure(Error.UserAlreadyExists)
                else {
                    val hashPass = password.hashCode()
                    val token = UUID.randomUUID().toString()
                    val user = it.usersData.createUser(name, email, hashPass, token)
                    success(UserOutputModel(user.id, user.name, user.email, token))
                }
            } catch (e: Exception) {
                failure(Error.InternalServerError)
            }
        }


    fun getUserByID(id: Int): UserResult =
        transactionManager.run {
            try {
                val user = it.usersData.getUserById(id)
                if (user == null) {
                    failure(Error.UserNotFound)
                } else {
                    success(UserOutputModel(user.id, user.name, user.email, user.token))
                }
            } catch (e: Exception) {
                failure(Error.InternalServerError)
            }
        }

    fun getUserByToken(token: String): UserResult =
        transactionManager.run {
            try {
                val user = it.usersData.getUserByToken(token)
                if (user == null) {
                    failure(Error.UserNotFound)
                } else {
                    success(UserOutputModel(user.id, user.name, user.email, user.token))
                }
            } catch (e: Exception) {
                failure(Error.InternalServerError)
            }
        }

    fun loginUser(email: String, password: String): UserResult =
        transactionManager.run {
            try {
                val user = it.usersData.loginUser(email, password)
                if (user == null) {
                    failure(Error.UserNotFound)
                } else {
                    success(UserOutputModel(user.id, user.name, user.email, user.token))
                }
            } catch (e: Exception) {
                failure(Error.InternalServerError)
            }
        }




}