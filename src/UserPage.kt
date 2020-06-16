import io.ktor.application.*
import io.ktor.freemarker.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.response.*
import io.ktor.routing.*
import dao.*
import io.ktor.request.receive
import io.ktor.request.receiveParameters
import io.ktor.sessions.*

/**
 * Register the [UserPage] route '/user/{user}',
 * with the user profile.
 */
fun Route.userPage(dao: DAOFacade) {

    get<UserPage> {
        val user = call.sessions.get<UserSession>()?.let { dao.user(it.login)  }
        if (user == null) return@get call.redirect(Login())

        println("get")
        val setting = dao.getSettings(user.id)
        val tarif = setting?.let { dao.getTarif(setting.tarif) }

        val action = (call.request.queryParameters["action"] ?: "show")
        when(action){
            "show" -> {
                call.respond(FreeMarkerContent("user.ftl", mapOf(
                        "action" to action,
                        "user" to user,
                        "tarif" to tarif,
                        "setting" to setting
                )
                ))
            }
            "edit" -> {
                val id = call.request.queryParameters["id"]
                if(id != null){
                    call.respond(FreeMarkerContent("employee.ftl",
                            mapOf("action" to action)))
                }
            }
            "name" -> {

            }
        }
    }

    post<UserPage> {
        var user = call.sessions.get<UserSession>()?.let { dao.user(it.login)  }
        if (user == null) return@post call.redirect(Login())

        println("post")
        val setting = dao.getSettings(user.id)
        val tarif = setting?.let { dao.getTarif(setting.tarif) }

        val postParameters: Parameters = call.receiveParameters()
        val action = postParameters["action"] ?: "show"
        println("$action")


        when(action){
            "name" -> {
                val nm = postParameters["name"] ?: ""
                if(nm != "" && nm != user.name) {
                    dao.setName(user.id,postParameters["name"] ?: "")
                    user.name = nm
                }
            }
//            "edit" ->{
//                val id = postParameters["id"]
//                if(id != null)
//                    dao.updateEmployee(id.toInt(), postParameters["name"] ?: "", postParameters["email"] ?: "", postParameters["city"] ?: "")
//            }
        }

        call.respond(FreeMarkerContent("user.ftl",
                mapOf("action" to action,
                      "user" to user,
                      "tarif" to tarif,
                      "setting" to setting)
        )
        )
    }
}
