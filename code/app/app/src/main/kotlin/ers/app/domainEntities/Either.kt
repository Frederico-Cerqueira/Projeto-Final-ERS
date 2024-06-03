package ers.app.domainEntities

sealed class Either<out L, out R> {
    data class Left<out L>(val value: L) : Either<L, Nothing>()
    data class Right<out R>(val value: R) : Either<Nothing, R>()

    fun <T> fold(onLeft: (L) -> T, onRight: (R) -> T): T {
        return when (this) {
            is Left -> onLeft(value)
            is Right -> onRight(value)
        }
    }
}

fun <L> failure(error: L) = Either.Left(error)
fun <R> success(value: R) = Either.Right(value)

typealias Success<S> = Either.Right<S>
typealias Failure<F> = Either.Left<F>