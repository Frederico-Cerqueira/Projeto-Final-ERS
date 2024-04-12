package ers.app

import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.Jdbi
import org.postgresql.ds.PGSimpleDataSource
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class Application {
	@Bean
	fun jdbi(): Jdbi {
		val jdbcDatabaseUrl =
			System.getenv("ERS_DATABASE")
		val dataSource = PGSimpleDataSource()
		dataSource.setURL(jdbcDatabaseUrl)
		return Jdbi.create(dataSource)
	}

	@Bean
	fun jdbiHandle(jdbi: Jdbi): Handle {
		return jdbi.open()  // Open a new Handle
	}
}

fun main(args: Array<String>) {
	runApplication<Application>(*args)
}
