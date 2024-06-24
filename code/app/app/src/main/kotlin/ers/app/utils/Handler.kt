package ers.app.utils

import ers.app.utils.errors.*
import org.springframework.http.ResponseEntity

class Handler {
    inline fun <reified T> responseHandler(res: Result<T>, status: Int): ResponseEntity<*> {
        return when (res) {
            is Success -> ResponseEntity.status(status).body(res.value)
            is Failure -> Errors.response(res.value)
        }
    }

    fun <T> servicesHandler(action: () -> Result<T>): Result<T> {
        return try {
            action()
        }catch (e : Exception){
            failure(InternalServerError)
        }
    }

}
