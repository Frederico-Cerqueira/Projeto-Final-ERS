package ers.app.repo.mappers

import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
import ers.app.repo.dtos.TaskDto
import java.sql.ResultSet

class TaskDtoMapper : RowMapper<TaskDto> {
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