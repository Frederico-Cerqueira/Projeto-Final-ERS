package ers.app.http.pipeline

import ers.app.domainEntities.AuthenticatedUser
import ers.app.utils.errors.Errors
import ers.app.utils.errors.Unauthorized
import jakarta.servlet.http.HttpServletRequest
import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

@Component
class ArgumentResolver : HandlerMethodArgumentResolver {

    override fun supportsParameter(parameter: MethodParameter): Boolean = parameter.parameterType == AuthenticatedUser::class.java


    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): Any? {
        val request = webRequest.getNativeRequest(HttpServletRequest::class.java)
            ?: return Errors.response(Unauthorized)
        return getUserFrom(request)
            ?: return Errors.response(Unauthorized)
    }

    companion object {
        private const val KEY = "AuthenticatedUserArgumentResolver"

        fun addUserTo(user: AuthenticatedUser, request: HttpServletRequest) {
            request.setAttribute(KEY, user)
        }

        fun getUserFrom(request: HttpServletRequest): AuthenticatedUser? {
            return request.getAttribute(KEY).let {
                it as? AuthenticatedUser
            }
        }
    }
}