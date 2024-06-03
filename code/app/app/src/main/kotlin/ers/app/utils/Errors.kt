package ers.app.utils

/**
 * Represents an error.
 */
sealed class Errors {
    data object UserAlreadyExists : Errors()
    data object InternalServerError : Errors()
    data object InvalidInput : Errors()
    data object InputTooLong : Errors()
    data object InvalidEmail : Errors()
    data object UserNotFound : Errors()
    data object TaskNotFound : Errors()
    data object RobotNotFound : Errors()
    data object AreaNotFound : Errors()
    data object TimeNotFound : Errors()
}