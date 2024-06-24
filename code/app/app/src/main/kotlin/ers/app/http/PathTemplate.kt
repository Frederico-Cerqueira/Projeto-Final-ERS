package ers.app.http

object PathTemplate {
    const val ID = "/{id}"
    const val USER_ID = "/user/{id}"
    const val ROBOT_TASKS = "/robot/{id}"
    const val USER_BY_TOKEN = "/token"
    const val LOGIN = "/login"
    const val LOGOUT = "/logout"
    const val UPDATE = "/update/{id}"
    const val CREATE_WITH_TASK = "/{taskID}"
    const val UPDATE_DESCRIPTION = "/update/description/{id}"
    const val TASK_ID = "/task/{id}"
    const val SESSION = "/session"
}