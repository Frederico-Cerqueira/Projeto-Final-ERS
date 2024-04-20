package ers.app.domainEntities

sealed class Either<out L, out R> {
    data class Left<out L>(val value: L) : Either<L, Nothing>()
    data class Right<out R>(val value: R) : Either<Nothing, R>()
}

fun <R> success(value: R) = Either.Right(value)
fun <L> failure(error: L) = Either.Left(error)