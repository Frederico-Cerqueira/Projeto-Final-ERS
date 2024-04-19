package ers.app.http

import ers.app.domainEntities.Either
import ers.app.service.UsersService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

data class UserInputModel(val name:String, val email:String, val password:String)
data class LoginInputModel(val email:String, val password:String)
data class TokenInputModel(val token:String)

@RestController
@RequestMapping("/users")
class UsersController(private val userService: UsersService) {

    @PostMapping
    fun createUser(@RequestBody user : UserInputModel):ResponseEntity<*>{
        return when (val res = userService.createUser(user.name, user.email, user.password)){
            is Either.Right -> ResponseEntity.status(201).body(res.value)
            is Either.Left -> ResponseEntity.status(409).body(res.value)
        }
    }

    @GetMapping(PathTemplate.ID)
    fun getUserByID(@PathVariable id: Int):ResponseEntity<*>{
        return when( val res = userService.getUserByID(id)){
            is Either.Right -> ResponseEntity.ok(res.value)
            is Either.Left -> ResponseEntity.badRequest().body(res.value)
        }
    }

    @GetMapping(PathTemplate.USER_BY_TOKEN)
    fun getUserByToken(@RequestBody token: TokenInputModel):ResponseEntity<*>{
        return when( val res = userService.getUserByToken(token.token)){
            is Either.Right -> ResponseEntity.ok(res.value)
            is Either.Left -> ResponseEntity.badRequest().body(res.value)
        }
    }

    @GetMapping(PathTemplate.LOGIN)
    fun loginUser(@RequestBody login:LoginInputModel):ResponseEntity<*>{
        return when( val res = userService.loginUser(login.email, login.password)){
            is Either.Right -> ResponseEntity.ok(res.value)
            is Either.Left -> ResponseEntity.badRequest().body(res.value)
        }
    }


}