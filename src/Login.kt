import io.ktor.application.*
import io.ktor.freemarker.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import dao.*
import io.ktor.sessions.*
import io.ktor.util.*

/**
 * Registers the [Login] and [Logout] routes '/login' and '/logout'.
 */
fun Route.login(dao: DAOFacade, hash: (String) -> String) {
    /**
     * A GET request to the [Login], would respond with the login page
     * (unless the user is already logged in, in which case it would redirect to the user's page)
     */
    get<Login> {
        val user = call.sessions.get<UserSession>()?.let { dao.user(it.Login) }

        if (user != null) {
//            call.redirect(UserPage(user.login))
            call.redirect(UserPage())
        } else {
            call.respond(FreeMarkerContent("login.ftl", mapOf("login" to it.login, "error" to it.error), ""))
        }
    }

    post<Login> {
        val post = call.receive<Parameters>()
        val userLogin = post["login"] ?: return@post call.redirect(it)
        val password = post["password"] ?: return@post call.redirect(it)

        val error = Login(userLogin)

        val login = when {
            userLogin.length < 4 -> null
            password.length < 6 -> null
            !userNameValid(userLogin) -> null
            else -> dao.user(userLogin, hash(password))
        }

        if (login == null) {
            call.redirect(error.copy(error = "Неправильные имя пользователя или пароль"))
        } else {
            call.sessions.set(UserSession(login.login, login.id))
//            call.redirect(UserPage(login.login))
            call.redirect(UserPage())
        }
    }

    get<Logout> {
        call.sessions.clear<UserSession>()
        call.redirect(Login())
    }
}
