import io.ktor.application.*
import io.ktor.freemarker.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import dao.*
import model.*
import io.ktor.sessions.*
import io.ktor.util.*
import org.joda.time.DateTime
import kotlin.random.Random

/**
 * Register the index route of the website.
 */
fun Route.index() {
    get<Index> {
//        println("session")
        val user = call.sessions.get<UserSession>()?.let { dao.user(it.Login) }
        if(user == null) {
//            println("Сессии нет")
            call.redirect(Login())
        } else {
            val sett = dao.getSettings(user.id)

            val rand = Random.nextInt(1990,2050)
            dao.setSettings(user.id, Setting(sett!!.tarif, DateTime.parse("$rand-04-01")))

            val tarif = dao.getTarif(sett!!.tarif)
            call.respond(FreeMarkerContent("index.ftl", mapOf("user" to user, "setting" to sett, "tarif" to tarif, "oldPay" to dao.getOldInfo(sett.old_pay_Date) ) ) )
        }
    }
    // Uses the location feature to register a get route for '/'.
//    get<Index> {
//        /*
//        val user = call.sessions.get<UserSession>()?.let { dao.user(it.user_login) }
//        if (user != null) {
//            call.redirect(UserPage(user.userlogin))
//        } else {
//            call.respond(FreeMarkerContent("register.ftl", mapOf("pageUser" to User(null, DateTime.now(), it.name, it.login, ""), "error" to it.error), ""))
//        }
//        * */
//        // Tries to get the user from the session (null if failure)
//        val user = call.sessions.get<UserSession>()?.let { dao.user(it.user_login) }
//
//        val user3 = dao.user("Login")
//        val user2 = mapOf("user" to user3, "name" to "TestName")
//
//        // Generates an ETag unique string for this route that will be used for caching.
//        val etagString = user3?.userlogin
//        val etag = etagString.hashCode()
//
////        mapOf("pageUser" to User(null, DateTime.now(), it.name, it.login, ""), "error" to it.error)
//        // Uses FreeMarker to render the page.
//        call.respond(FreeMarkerContent("index.ftl", user2, etag.toString()))
//    }

//    post<Index> {
//        val registration = call.receive<Parameters>()
//        val login = registration["login"] ?: return@post call.redirect(it)
//        val pass = registration["password"] ?: return@post call.redirect(it)
//        val name = registration["name"] ?: return@post call.redirect(it)
//
//        println("name=$name login=$login pass=$pass")
//
//        // prepare location class for error if any
//        println("Register")
//        val error = Register(login, name)
//
//        println("when")
////        when {
////            pass.length < 6 -> call.redirect(error.copy(error = "Password should be at least 6 characters long"))
////            login.length < 4 -> call.redirect(error.copy(error = "Login should be at least 4 characters long"))
////            !userNameValid(login) -> call.redirect(error.copy(error = "Login should be consists of digits, letters, dots or underscores"))
////            dao.user(login) != null -> call.redirect(error.copy(error = "User with the following login is already registered"))
////            else -> {
////                val hash = hashFunction(pass)
//                println("newUser")
//                val newUser = User(DateTime.now(), name, login, pass)
//
//                try {
//                    println("createUser")
//                    dao.createUser(newUser)
//                } catch (e: Throwable) {
//                    when {
//                        // NOTE: This is security issue that allows to enumerate/verify registered users. Do not do this in real app :)
//                        dao.user(login) != null -> call.redirect(error.copy(error = "User with the following login is already registered"))
//                        else -> {
//                            application.log.error("Failed to register user", e)
//                            call.redirect(error.copy(error = "Failed to register"))
//                        }
//                    }
//                }
//
////                call.sessions.set(UserSession(newUser.userlogin))
//                call.redirect(Index())
////            }
////        }
//    }
}