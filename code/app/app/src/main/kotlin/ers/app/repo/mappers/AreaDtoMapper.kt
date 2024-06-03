package ers.app.repo.mappers

import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
import ers.app.repo.dtos.AreaDto
import java.sql.ResultSet

/**
 * Mapper for AreaDto.
 */
class AreaDtoMapper : RowMapper<AreaDto> {

    /**
     * Map a result set to an AreaDto.
     * @param rs Result set to map.
     * @param ctx Statement context.
     * @return AreaDto.
     */
    override fun map(rs: ResultSet, ctx: StatementContext?): AreaDto {
        return AreaDto(
            id = rs.getInt("id"),
            taskId = rs.getInt("taskId"),
            height = rs.getInt("height"),
            width = rs.getInt("width"),
            name = rs.getString("name"),
            description = rs.getString("description")
        )
    }
}