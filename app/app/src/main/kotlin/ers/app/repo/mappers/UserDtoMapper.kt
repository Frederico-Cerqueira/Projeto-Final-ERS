package ers.app.repo.mappers

import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
import ers.app.repo.dtos.UserDto
import java.sql.ResultSet

class UserDtoMapper : RowMapper<UserDto> {
    override fun map(rs: ResultSet, ctx: StatementContext?): UserDto {
        return UserDto(
            id = rs.getInt("id"),
            name = rs.getString("name"),
            email = rs.getString("email"),
            password = rs.getString("password"),
            token = rs.getString("token")
        )
    }
}