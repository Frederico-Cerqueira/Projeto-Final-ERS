package ers.app.http

import ers.app.domainEntities.Either
import ers.app.domainEntities.inputModels.LoginInputModel
import ers.app.domainEntities.inputModels.TokenInputModel
import ers.app.domainEntities.inputModels.UserInputModel
import ers.app.service.UserService
import ers.app.utils.Error
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
                Error.InvalidInput -> ResponseEntity.badRequest().body(res.value)
                Error.InputTooLong -> ResponseEntity.status(413).body(res.value)
                Error.UserAlreadyExists -> ResponseEntity.status(409).body(res.value)
                Error.InvalidEmail -> ResponseEntity.badRequest().body(res.value)
                else -> ResponseEntity.internalServerError().body(res.value)
            }
        }
    }

    @GetMapping(PathTemplate.ID)
    fun getUserByID(@PathVariable id: Int): ResponseEntity<*> {
        return when (val res = userService.getUserByID(id)) {
            is Either.Right -> ResponseEntity.ok(res.value)
            is Either.Left -> when (res.value) {
                Error.UserNotFound -> ResponseEntity.badRequest().body(res.value)
                else -> ResponseEntity.internalServerError().body(res.value)
            }
        }
    }

    @GetMapping(PathTemplate.USER_BY_TOKEN)
    fun getUserByToken(@RequestBody token: TokenInputModel): ResponseEntity<*> {
        return when (val res = userService.getUserByToken(token.token)) {
            is Either.Right -> ResponseEntity.ok(res.value)
            is Either.Left -> when (res.value) {
                Error.UserNotFound -> ResponseEntity.badRequest().body(res.value)
                else -> ResponseEntity.internalServerError().body(res.value)
            }
        }
    }

    @GetMapping(PathTemplate.LOGIN)
    fun loginUser(@RequestBody login: LoginInputModel): ResponseEntity<*> {
        return when (val res = userService.loginUser(login.email, login.password)) {
            is Either.Right -> ResponseEntity.ok(res.value)
            is Either.Left -> when (res.value) {
                Error.UserNotFound -> ResponseEntity.badRequest().body(res.value)
                Error.InputTooLong -> ResponseEntity.status(413).body(res.value)
                else -> ResponseEntity.internalServerError().body(res.value)
            }
        }
    }
}