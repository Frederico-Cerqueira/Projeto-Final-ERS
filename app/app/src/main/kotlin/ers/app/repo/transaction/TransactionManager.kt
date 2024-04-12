package ers.app.repo.transaction

import org.springframework.stereotype.Component

@Component
interface TransactionManager {
    fun <R> run(block: (Transaction) -> R): R
}