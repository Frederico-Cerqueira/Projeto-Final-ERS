package ers.app.repo.mappers

import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
import ers.app.repo.dtos.TimeDto
import java.sql.ResultSet

class TimeDtoMapper: RowMapper<TimeDto> {
    override fun map(rs: ResultSet, ctx: StatementContext?): TimeDto {
        return TimeDto(
            rs.getInt("id"),
            rs.getInt("tasId"),
            rs.getString("weekDay"),
            rs.getString("start_time"),
            rs.getString("end_time"),
        )
    }
}