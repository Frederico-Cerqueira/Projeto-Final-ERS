package ers.app.repo.mappers

import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
import ers.app.repo.dtos.TimeDto
import java.sql.ResultSet

/**
 * Mapper for TimeDto.
 */
class TimeDtoMapper: RowMapper<TimeDto> {

    /**
     * Map a result set to a TimeDto.
     * @param rs Result set to map.
     * @param ctx Statement context.
     * @return TimeDto.
     */
    override fun map(rs: ResultSet, ctx: StatementContext?): TimeDto {
        val time = TimeDto(
            rs.getInt("id"),
            rs.getInt("taskId"),
            rs.getString("weekDay"),
            rs.getTime("start_time").toString(),
            rs.getTime("end_time").toString(),
            rs.getString("description"),
        )
        return time
    }
}