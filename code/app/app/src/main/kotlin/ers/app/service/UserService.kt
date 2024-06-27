package ers.app.service

import ers.app.domainEntities.outputModels.LogoutOutputModel
import ers.app.domainEntities.outputModels.UserOutputModel
import ers.app.repo.transaction.TransactionManager
import ers.app.utils.*
import ers.app.utils.errors.failure
import ers.app.utils.errors.success
import org.springframework.stereotype.Component
import java.util.*
import ers.app.utils.errors.*

@Component
class UserService(private val transactionManager: TransactionManager) {

    fun createUser(name: String, email: String, password: String): Result<UserOutputModel> {
        if (name.length > 50 || email.length > 50 || password.length > 50)
            return failure(InputTooLong)
        if (name.isEmpty() || email.isEmpty() || password.isEmpty())
            return failure(InvalidInput)
        val regex = Regex("[a-zA-Z0-9.\\-_]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}")
        if (!regex.matches(email))
            return failure(InvalidEmail)
        val hashPassword = password.hashCode()
        val token = UUID.randomUUID().toString()
        return Handler().servicesHandler {
            transactionManager.run {
                if (it.usersData.getUserByEmail(email) != null) return@run failure(UserAlreadyExists)
                val user = it.usersData.createUser(name, email, hashPassword, token)
                return@run success(UserOutputModel(user.id, user.name, user.email, token))
            }
        }
    }


    fun getUserByID(id: Int): Result<UserOutputModel> =
        Handler().servicesHandler {
            transactionManager.run {
                val user = it.usersData.getUserByID(id) ?: return@run failure(UserNotFound)
                return@run success(UserOutputModel(user.id, user.name, user.email, user.token))
            }
        }

    fun getUserByToken(token: String): Result<UserOutputModel> =
        Handler().servicesHandler {
            transactionManager.run {
                val user = it.usersData.getUserByToken(token) ?: return@run failure(UserNotFound)
                return@run success(UserOutputModel(user.id, user.name, user.email, user.token))
            }
        }

    fun loginUser(email: String, password: String): Result<UserOutputModel> {
        if (email.length > 50 || password.length > 50 || email.isEmpty() || password.isEmpty())
            return failure(InputTooLong)
        val token = UUID.randomUUID().toString()
        return Handler().servicesHandler {
            transactionManager.run {
                val user = it.usersData.loginUser(email, password, token) ?: return@run failure(UserNotFound)
                return@run success(UserOutputModel(user.id, user.name, user.email, user.token))
            }
        }
    }

    fun logoutUser(id: Int): Result<LogoutOutputModel> {
        return Handler().servicesHandler {
            transactionManager.run {
                val user = it.usersData.getUserByID(id) ?: return@run failure(UserNotFound)
                it.usersData.logoutUser(id)
                return@run success(LogoutOutputModel(user.id, user.email))
            }
        }
    }
}