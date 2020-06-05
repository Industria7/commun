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

fun Route.delete() {
    get<Delete> {
        val id = call.request.queryParameters["id"]
        if(id != null){
//            dao.deleteEmployee(id.toInt())
//            call.respond(FreeMarkerContent("index.ftl", mapOf("employees" to dao.getAllEmployees(), "users" to dao.getAllUsers() )))
            call.respond(FreeMarkerContent("index.ftl",""))
        }
    }
}