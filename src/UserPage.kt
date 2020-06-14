import io.ktor.application.*
import io.ktor.freemarker.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.response.*
import io.ktor.routing.*
import dao.*
import io.ktor.sessions.*

/**
 * Register the [UserPage] route '/user/{user}',
 * with the user profile.
 */
fun Route.userPage(dao: DAOFacade) {

    get<UserPage> {
        val user = call.sessions.get<UserSession>()?.let { dao.user(it.login)  }
        if (user != null) {

            call.respond(FreeMarkerContent("user.ftl", mapOf(
                    "user" to user,
                    "tarifs" to dao.getAllTarifs(user.id),
                    "setting" to dao.getSettings(user.id)
            )
            ))
        }else{
            call.redirect(Login())
        }
//        if (it.user == null) {
//            call.respond(HttpStatusCode.NotFound.description("Пользователь ${it.user} не существует"))
//        } else {
//            call.respond(FreeMarkerContent("user.ftl", mapOf("user" to user)))
//        }
    }
}
