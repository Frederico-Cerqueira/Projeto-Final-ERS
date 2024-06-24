package ers.app.utils.errors

import org.springframework.http.ResponseEntity

/**
 * Represents an error.
 */
sealed class Errors(val status: Int, val message: String) {

    companion object {
        fun response(error: Errors) = ResponseEntity
            .status(error.status)
            .header("Content-Type", "application/problem+json")
            .body<Any>(error.message)
    }
}

data object UserAlreadyExists : Errors(409, "User already exists")
data object InternalServerError : Errors(500, "Internal server error")
data object InvalidInput : Errors(400, "Invalid input")
data object InputTooLong : Errors(413, "Input too long")
data object InvalidEmail : Errors(400, "Invalid email")
data object InvalidToken : Errors(400, "Invalid token")
data object UserNotFound : Errors(404, "User not found")
data object TaskNotFound : Errors(404, "Task not found")
data object RobotNotFound : Errors(404, "Robot not found")
data object AreaNotFound : Errors(404, "Area not found")
data object TimeNotFound : Errors(404, "Time not found")
data object Unauthorized : Errors(401, "Unauthorized")


typealias Result<T> = Either<Errors, T>