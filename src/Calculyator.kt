import io.ktor.application.*
import io.ktor.freemarker.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.get
import io.ktor.sessions.sessions
import model.Setting
import org.joda.time.DateTime
import kotlin.random.Random

fun Route.calc() {
    get<Calc> {
//        val action = (call.request.queryParameters["action"] ?: "new")
//        when(action){
//            "new" -> call.respond(FreeMarkerContent("employee.ftl",
//                    mapOf("action" to action)))
//            "edit" -> {
//                val id = call.request.queryParameters["id"]
//                if(id != null){
//                    call.respond(FreeMarkerContent("employee.ftl",
//                            mapOf("employee" to dao.getEmployee(id.toInt()),
//                                    "action" to action)))
//                }
//            }
//        }

        val user = call.sessions.get<UserSession>()?.let { dao.user(it.Login) }
        if(user == null) {
//            println("Сессии нет")
            call.redirect(Login())
        } else {
            val sett = dao.getSettings(user.id)

            val tarif = dao.getTarif(sett!!.tarif)
            call.respond(FreeMarkerContent("calc.ftl", mapOf("user" to user, "setting" to sett, "tarif" to tarif, "oldPay" to dao.getOldInfo(sett.old_pay_Date) ) ) )
        }
    }

    post<Calc>{
        val postParameters: Parameters = call.receiveParameters()
        println(postParameters)
        val action = postParameters["action"] ?: "new"
//        when(action){
//            "new" -> dao.createEmployee(postParameters["name"] ?: "", postParameters["email"] ?: "", postParameters["city"] ?: "")
//            "edit" ->{
//                val id = postParameters["id"]
//                if(id != null)
//                    dao.updateEmployee(id.toInt(), postParameters["name"] ?: "", postParameters["email"] ?: "", postParameters["city"] ?: "")
//            }
//        }
        val text = ""
        call.respond(FreeMarkerContent("calc.ftl", mapOf("text" to text ) ) )
    }
}