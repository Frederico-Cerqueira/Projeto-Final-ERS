package ers.app.domainEntities

import ers.app.domainEntities.outputModels.UserModel

class AuthenticatedUser (
    val user: UserModel,
    val token: String
)