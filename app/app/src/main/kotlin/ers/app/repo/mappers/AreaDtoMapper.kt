package ers.app.repo.mappers

import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
import ers.app.repo.dtos.AreaDto
import java.sql.ResultSet

class AreaDtoMapper : RowMapper<AreaDto> {
    override fun map(rs: ResultSet, ctx: StatementContext?): AreaDto {
        return AreaDto(
            id = rs.getInt("id"),
            taskId = rs.getInt("taskId"),
            height = rs.getInt("height"),
            width = rs.getInt("width"),
        )
    }
}