package ers.app.utils

sealed class Error{
    object UserAlreadyExists : Error()
    object InternalServerError : Error()
    object UserNotFound : Error()
    object TaskNotFound : Error()

    object RobotNotFound : Error()
    object AreaNotFound : Error()
}