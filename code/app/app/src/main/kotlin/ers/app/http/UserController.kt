package ers.app.http

import ers.app.domainEntities.Either
import ers.app.domainEntities.inputModels.LoginInputModel
import ers.app.domainEntities.inputModels.LogoutInputModel
import ers.app.domainEntities.inputModels.TokenInputModel
import ers.app.domainEntities.inputModels.UserInputModel
import ers.app.service.UserService
import ers.app.utils.Errors
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/user")
class UserController(private val userService: UserService) {

    @PostMapping
    fun createUser(@RequestBody user: UserInputModel): ResponseEntity<*> {
        return when (val res = userService.createUser(user.name, user.email, user.password)) {
            is Either.Right -> ResponseEntity.status(201).body(res.value)
            is Either.Left -> when (res.value) {
                Errors.InvalidInput -> ResponseEntity.badRequest().body(res.value)
                Errors.InputTooLong -> ResponseEntity.status(413).body(res.value)
                Errors.UserAlreadyExists -> ResponseEntity.status(409).body(res.value)
                Errors.InvalidEmail -> ResponseEntity.badRequest().body(res.value)
                else -> ResponseEntity.internalServerError().body(res.value)
            }
        }
    }



    @GetMapping(PathTemplate.ID)
    fun getUserByID(@PathVariable id: Int): ResponseEntity<*> {
        return when (val res = userService.getUserByID(id)) {
            is Either.Right -> ResponseEntity.ok(res.value)
            is Either.Left -> when (res.value) {
                Errors.UserNotFound -> ResponseEntity.badRequest().body(res.value)
                else -> ResponseEntity.internalServerError().body(res.value)
            }
        }
    }

    @GetMapping(PathTemplate.USER_BY_TOKEN)
    fun getUserByToken(@RequestParam token: TokenInputModel): ResponseEntity<*> {
        return when (val res = userService.getUserByToken(token.token)) {
            is Either.Right -> ResponseEntity.ok(res.value)
            is Either.Left -> when (res.value) {
                Errors.UserNotFound -> ResponseEntity.badRequest().body(res.value)
                else -> ResponseEntity.internalServerError().body(res.value)
            }
        }
    }

    @PostMapping(PathTemplate.LOGIN)
    fun loginUser(@RequestBody login: LoginInputModel): ResponseEntity<*> {
        return when (val res = userService.loginUser(login.email, login.password)) {
            is Either.Right -> ResponseEntity.ok(res.value)
            is Either.Left -> when (res.value) {
                Errors.UserNotFound -> ResponseEntity.badRequest().body(res.value)
                Errors.InputTooLong -> ResponseEntity.status(413).body(res.value)
                else -> ResponseEntity.internalServerError().body(res.value)
            }
        }
    }

    @PostMapping(PathTemplate.LOGOUT)
    fun logoutUser(@RequestBody logout: LogoutInputModel): ResponseEntity<*> {
        return when (val res = userService.logoutUser(logout.id,logout.token)) {
            is Either.Right -> ResponseEntity.ok(res.value)
            is Either.Left -> when (res.value) {
                Errors.UserNotFound -> ResponseEntity.badRequest().body(res.value)
                else -> ResponseEntity.internalServerError().body(res.value)
            }
        }
    }
}