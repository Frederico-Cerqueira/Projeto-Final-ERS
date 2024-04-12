package ers.app.repo.mappers

import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
import ers.app.repo.dtos.RobotDto
import java.sql.ResultSet

class RobotDtoMapper : RowMapper<RobotDto> {
    override fun map(rs: ResultSet, ctx: StatementContext?): RobotDto {
        return RobotDto(
            id = rs.getInt("id"),
            name = rs.getString("name"),
            status = rs.getString("status"),
            characteristics = rs.getString("characteristics")
        )
    }
}