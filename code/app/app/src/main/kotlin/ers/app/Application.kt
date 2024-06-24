
package ers.app

import ers.app.http.pipeline.ArgumentResolver
import ers.app.http.pipeline.AuthenticationInterceptor
import ers.app.repo.mappers.*
import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.Jdbi
import org.postgresql.ds.PGSimpleDataSource
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

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

	@Configuration
	class PipelineConfigurer(
		val authenticationInterceptor: AuthenticationInterceptor,
		val argumentResolver: ArgumentResolver
	) : WebMvcConfigurer {

		override fun addInterceptors(registry: InterceptorRegistry) {
			registry.addInterceptor(authenticationInterceptor)
		}

		override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
			resolvers.add(argumentResolver)
		}
	}

}

fun main(args: Array<String>) {
	runApplication<Application>(*args)
}
