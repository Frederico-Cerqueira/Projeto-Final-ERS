package ers.app.domainEntities

import ers.app.service.*
import ers.app.utils.Error


typealias UserResult = Either<Error, UserOutputModel>

typealias TaskResult = Either<Error, TaskOutputModel>
typealias TasksResult = Either<Error, TasksOutputModel>
typealias TaskIDResult = Either<Error, TaskIDOutputModel>

typealias RobotResult = Either<Error, RobotOutputModel>
typealias RobotsResult = Either<Error, RobotsOutputModel>
typealias RobotIDResult = Either<Error, RobotIDOutputModel>

typealias AreaResult = Either<Error, AreaOutputModel>
typealias AreasResult = Either<Error, AreasOutputModel>
typealias AreaIDResult = Either<Error, AreaIDOutputModel>