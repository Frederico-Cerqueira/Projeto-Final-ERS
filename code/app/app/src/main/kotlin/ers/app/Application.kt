package ers.app

import ers.app.repo.mappers.*
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
		val jdbcDatabaseURL =
			System.getenv("JDBC_DATABASE_URL")
				?: "jdbc:postgresql://localhost/ProjetoFinal?user=postgres&password=Asus"
		val dataSource = PGSimpleDataSource()
		dataSource.setURL(jdbcDatabaseURL)
		return Jdbi.create(dataSource)
			.registerRowMapper(RobotDtoMapper())
			.registerRowMapper(TaskDtoMapper())
			.registerRowMapper(AreaDtoMapper())
			.registerRowMapper(TimeDtoMapper())
			.registerRowMapper(UserDtoMapper())
	}

	@Bean
	fun jdbiHandle(jdbi: Jdbi): Handle {
		return jdbi.open()  // Open a new Handle
	}
}

fun main(args: Array<String>) {
	runApplication<Application>(*args)
}
