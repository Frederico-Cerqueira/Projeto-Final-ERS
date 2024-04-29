package ers.app.service

import ers.app.domainEntities.*
import ers.app.domainEntities.outputModels.UserOutputModel
import ers.app.repo.transaction.TransactionManager
import ers.app.utils.Error
import org.springframework.stereotype.Component
import java.util.*

@Component
class UserService(private val transactionManager: TransactionManager) {

    fun createUser(name: String, email: String, password: String): UserResult {
        if (name.length > 50 || email.length > 50 || password.length > 50)
            return failure(Error.InputTooLong)
        if (name.isEmpty() || email.isEmpty() || password.isEmpty())
            return failure(Error.InvalidInput)
        val regex = Regex("[a-zA-Z0-9.\\-_]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}")
        if (!regex.matches(email))
            return failure(Error.InvalidEmail)
        return transactionManager.run {
            try {
                if (it.usersData.getUserByEmail(email) != null)
                    failure(Error.UserAlreadyExists)
                val hashPassword = password.hashCode()
                val token = UUID.randomUUID().toString()
                val user = it.usersData.createUser(name, email, hashPassword, token)
                success(UserOutputModel(user.id, user.name, user.email, token))
            } catch (e: Exception) {
                failure(Error.InternalServerError)
            }
        }
    }


    fun getUserByID(id: Int): UserResult =
        transactionManager.run {
            try {
                val user = it.usersData.getUserByID(id)
                if (user == null)
                    failure(Error.UserNotFound)
                else
                    success(UserOutputModel(user.id, user.name, user.email, user.token))
            } catch (e: Exception) {
                failure(Error.InternalServerError)
            }
        }

    fun getUserByToken(token: String): UserResult =
        transactionManager.run {
            try {
                val user = it.usersData.getUserByToken(token)
                if (user == null)
                    failure(Error.UserNotFound)
                else
                    success(UserOutputModel(user.id, user.name, user.email, user.token))
            } catch (e: Exception) {
                failure(Error.InternalServerError)
            }
        }

    fun loginUser(email: String, password: String): UserResult {
        if (email.length > 50 || password.length > 50 || email.isEmpty() || password.isEmpty())
            return failure(Error.InputTooLong)
        return transactionManager.run {
            try {
                val user = it.usersData.loginUser(email, password)
                if (user == null)
                    failure(Error.UserNotFound)
                else
                    success(UserOutputModel(user.id, user.name, user.email, user.token))
            } catch (e: Exception) {
                failure(Error.InternalServerError)
            }
        }
    }
}