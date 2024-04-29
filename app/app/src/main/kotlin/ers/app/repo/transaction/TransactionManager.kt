package ers.app.repo.transaction

import org.springframework.stereotype.Component


/**
 * TransactionManager interface that provides access to transactions.
 */
@Component
interface TransactionManager {
    fun <R> run(block: (Transaction) -> R): R
}