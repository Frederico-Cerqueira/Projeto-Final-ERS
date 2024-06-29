package ers.app.http

import ers.app.domainEntities.inputModels.LoginInputModel
import ers.app.domainEntities.inputModels.LogoutInputModel
import ers.app.domainEntities.inputModels.TokenInputModel
import ers.app.domainEntities.inputModels.UserInputModel
import ers.app.service.UserService
import ers.app.utils.Handler
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseCookie
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/user")
class UserController(private val userService: UserService) {

    @PostMapping
    fun createUser(@RequestBody user: UserInputModel, response: HttpServletResponse): ResponseEntity<*> {
        val user = userService.createUser(user.name, user.email, user.password)
        val token = user.fold({ " " }, { it.token })
        val cookieToken = ResponseCookie
            .from("token", token)
            .maxAge(3600)
            .path("/")
            .httpOnly(true)
            .secure(false)
            .build()

        response.addHeader(HttpHeaders.SET_COOKIE, cookieToken.toString())
        return Handler().responseHandler(user, 201)
    }

    @GetMapping(PathTemplate.ID)
    fun getUserByID(@PathVariable id: Int): ResponseEntity<*> {
        return Handler().responseHandler(userService.getUserByID(id), 200)
    }

    @GetMapping(PathTemplate.USER_BY_TOKEN)
    fun getUserByToken(@RequestParam token: TokenInputModel): ResponseEntity<*> {
        return Handler().responseHandler(userService.getUserByToken(token.token), 200)
    }

    @PostMapping(PathTemplate.LOGIN)
    fun loginUser(@RequestBody login: LoginInputModel, response: HttpServletResponse): ResponseEntity<*> {
        val user = userService.loginUser(login.email, login.password)
        val token = user.fold({ " " }, { it.token })
        val cookieToken = ResponseCookie
            .from("token", token)
            .maxAge(3600)
            .path("/")
            .httpOnly(true)
            .secure(false)
            .build()

        response.addHeader(HttpHeaders.SET_COOKIE, cookieToken.toString())
        return Handler().responseHandler(user, 200)
    }

    @GetMapping(PathTemplate.SESSION)
    fun checkSession(
        request: HttpServletRequest
    ): Array<out Cookie>? {
        val cookie = request.cookies
        val cookiesToReturn = cookie?.filter { it.value != "" }
        return cookiesToReturn?.toTypedArray()
    }

    @PostMapping(PathTemplate.LOGOUT)
    fun logoutUser(@RequestBody logout: LogoutInputModel, response: HttpServletResponse): ResponseEntity<*> {
        //removes the token of the cookie to logout
        val cookieToken = Cookie("token", null)
        cookieToken.maxAge = 0
        cookieToken.secure = false
        cookieToken.path = "/"
        cookieToken.isHttpOnly = true

        response.addCookie(cookieToken)
        return Handler().responseHandler(userService.logoutUser(logout.id), 200)
    }
}