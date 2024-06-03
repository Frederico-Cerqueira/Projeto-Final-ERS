package ers.app.repo.mappers

import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
import ers.app.repo.dtos.RobotDto
import java.sql.ResultSet

/**
 * Mapper for RobotDto.
 */
class RobotDtoMapper : RowMapper<RobotDto> {

    /**
     * Map a result set to a RobotDto.
     * @param rs Result set to map.
     * @param ctx Statement context.
     * @return RobotDto.
     */
    override fun map(rs: ResultSet, ctx: StatementContext?): RobotDto {
        return RobotDto(
            id = rs.getInt("id"),
            name = rs.getString("name"),
            status = rs.getString("status"),
            characteristics = rs.getString("characteristics")
        )
    }
}