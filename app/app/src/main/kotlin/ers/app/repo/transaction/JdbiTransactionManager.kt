package ers.app.repo.transaction

import org.jdbi.v3.core.Jdbi
import org.springframework.stereotype.Component

/**
 * Transaction manager that uses Jdbi to manage transactions.
 * @param jdbi Jdbi instance to use for transactions.
 */
@Component
class JdbiTransactionManager(
    private val jdbi: Jdbi
) : TransactionManager {

    /**
     * Run a transaction.
     * @param block Block of code to run in the transaction.
     * @return Result of the block.
     */
    override fun <R> run(block: (Transaction) -> R): R =
        jdbi.inTransaction<R, Exception> { handle ->
            val transaction = JdbiTransaction(handle)
            block(transaction)
        }
}