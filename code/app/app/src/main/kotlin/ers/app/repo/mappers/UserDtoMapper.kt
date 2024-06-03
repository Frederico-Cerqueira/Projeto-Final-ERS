package ers.app.repo.mappers

import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
import ers.app.repo.dtos.UserDto
import java.sql.ResultSet

/**
 * Mapper for UserDto.
 */
class UserDtoMapper : RowMapper<UserDto> {

    /**
     * Map a result set to a UserDto.
     * @param rs Result set to map.
     * @param ctx Statement context.
     * @return UserDto.
     */
    override fun map(rs: ResultSet, ctx: StatementContext?): UserDto {
        return UserDto(
            id = rs.getInt("id"),
            name = rs.getString("name"),
            email = rs.getString("email"),
            password = rs.getInt("hashPass").toString(),
            token = rs.getString("token")
        )
    }
}