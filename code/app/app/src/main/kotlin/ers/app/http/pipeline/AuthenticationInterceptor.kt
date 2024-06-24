package ers.app.http.pipeline

import ers.app.domainEntities.AuthenticatedUser
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor

@Component
class AuthenticationInterceptor(
    private val bearerTokenProcessor: BearerTokenProcessor
) : HandlerInterceptor {

    companion object {
        const val NAME_AUTHORIZATION_HEADER = "Authorization"
        private const val NAME_WWW_AUTHENTICATE_HEADER = "WWW-Authenticate"
    }

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        if((handler is HandlerMethod) && handler.methodParameters.any {
                it.parameterType == AuthenticatedUser::class.java
            }
        ) {
            val cookies = request.cookies?.asList()?.find{ it.name=="token" }
            val user = bearerTokenProcessor.process(request.getHeader(NAME_AUTHORIZATION_HEADER), cookies?.value)
            return if(user == null) {
                response.status = 401
                response.addHeader(NAME_WWW_AUTHENTICATE_HEADER, BearerTokenProcessor.SCHEMA)
                false
            } else {
                ArgumentResolver.addUserTo(user, request)
                true
            }
        }
        return true
    }
}