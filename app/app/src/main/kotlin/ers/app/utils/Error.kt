package ers.app.utils

sealed class Error {
    data object UserAlreadyExists : Error()
    data object InternalServerError : Error()
    data object InvalidInput : Error()
    data object InputTooLong : Error()
    data object InvalidEmail : Error()
    data object UserNotFound : Error()
    data object TaskNotFound : Error()
    data object RobotNotFound : Error()
    data object AreaNotFound : Error()
    data object TimeNotFound : Error()
}