package ers.app.repo.mappers

import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
import ers.app.repo.dtos.TaskDto
import java.sql.ResultSet

/**
 * Mapper for TaskDto.
 */
class TaskDtoMapper : RowMapper<TaskDto> {

    /**
     * Map a result set to a TaskDto.
     * @param rs Result set to map.
     * @param ctx Statement context.
     * @return TaskDto.
     */
    override fun map(rs: ResultSet, ctx: StatementContext?): TaskDto {
        return TaskDto(
            id = rs.getInt("id"),
            userId = rs.getInt("userId"),
            robotId = rs.getInt("robotId"),
            name = rs.getString("name"),
            status = rs.getString("status"),
        )
    }
}