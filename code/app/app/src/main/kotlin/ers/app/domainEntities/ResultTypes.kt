package ers.app.domainEntities

import ers.app.domainEntities.outputModels.*
import ers.app.utils.Errors

typealias UserResult = Either<Errors, UserOutputModel>

typealias TaskResult = Either<Errors, TaskOutputModel>
typealias TasksResult = Either<Errors, TasksOutputModel>
typealias TaskIDResult = Either<Errors, TaskIDOutputModel>

typealias RobotResult = Either<Errors, RobotOutputModel>
typealias RobotsResult = Either<Errors, RobotsOutputModel>
typealias RobotIDResult = Either<Errors, RobotIDOutputModel>

typealias AreaResult = Either<Errors, AreaOutputModel>
typealias AreasResult = Either<Errors, AreasOutputModel>
typealias AreaIDResult = Either<Errors, AreaIDOutputModel>

typealias TimeResult = Either<Errors, TimeOutputModel>
typealias TimesResult = Either<Errors, TimesOutputModel>
typealias TimeIDResult = Either<Errors, TimeIDOutputModel>
