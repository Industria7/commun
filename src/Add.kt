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
fun Route.add() {
    get<Add> {
        val user = call.sessions.get<UserSession>()?.let { dao.user(it.login)  }
        if (user == null) return@get call.redirect(Login())

        val setting = dao.getSettings(user.id)

        val tarif = setting?.let { dao.getTarif(setting.tarif) }

//        call.respond(FreeMarkerContent("month.ftl", mapOf("user" to user, "setting" to setting, "tarif" to tarif)))
println("get")
        call.respond(FreeMarkerContent("month.ftl", mapOf(
                "user" to user,
                "tarif" to tarif,
                "setting" to setting
        )
        ))
    }

    post<Add> {
        var user = call.sessions.get<UserSession>()?.let { dao.user(it.login)  }
        if (user == null) return@post call.redirect(Login())

        val setting = dao.getSettings(user.id)
        val tarif = setting?.let { dao.getTarif(setting.tarif) }

        println("post")
        val postParameters: Parameters = call.receiveParameters()
        println("$postParameters")

        val action = postParameters["action"] ?: "show"

        when(action){
            "show" -> call.respond(FreeMarkerContent("month.ftl", mapOf(
                    "user" to user,
                    "tarif" to tarif,
                    "setting" to setting
            )))

            "month" -> call.respond(FreeMarkerContent("add.ftl", mapOf(
                    "month" to postParameters["month"],
                    "user" to user,
                    "tarif" to tarif,
                    "setting" to setting
            )))

            "add" -> {

                println("Добавляем $postParameters")
                call.respond(FreeMarkerContent("month.ftl", mapOf(
                        "inf" to "Успешно добавлено"
                )))
            }

            else -> call.respond(FreeMarkerContent("month.ftl", mapOf(
                    "inf" to "Что-то не так"
            )))

        }
    }
}