package ers.app.http.pipeline

import ers.app.domainEntities.AuthenticatedUser
import ers.app.domainEntities.outputModels.UserModel
import ers.app.service.UserService
import ers.app.utils.errors.Success
import org.springframework.stereotype.Component

@Component
class BearerTokenProcessor(
    private val services: UserService,
) {
    fun process(authorizationHeaderValue: String?, cookie: String?): AuthenticatedUser? {
        if(cookie != null) {
            val userOutput = services.getUserByToken(cookie)

            var authUser: AuthenticatedUser? = null

            if(userOutput is Success) {


                val user = UserModel(userOutput.value.id, userOutput.value.name, userOutput.value.email)
                authUser = AuthenticatedUser(user, cookie)
            }
            return authUser
        }

        if(authorizationHeaderValue == null ) {
            return null
        }
        val header = authorizationHeaderValue.trim().split(" ")

        if(header.size != 2 || header[0].lowercase() != SCHEMA) {
            return null
        }

        if(header[0].lowercase() != SCHEMA) {
            return null
        }

        val userOutput = services.getUserByToken(header[1])

        var authUser: AuthenticatedUser? = null

        if(userOutput is Success) {

            val user = UserModel(userOutput.value.id, userOutput.value.name,userOutput.value.email)
            authUser = AuthenticatedUser(user, header[1])
        }
        return authUser
    }
    companion object{
        const val SCHEMA = "bearer"
    }
}