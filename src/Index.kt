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
        val user = call.sessions.get<UserSession>()?.let { dao.user(it.login) }
        if(user == null) {
//            println("Сессии нет")
            call.redirect(Login())
        } else {

            val setting = dao.getSettings(user.id)
            if(setting != null) {
                val rand = Random.nextInt(1990, 2050)
                dao.setSettings(user.id, Setting(setting.tarif, DateTime.parse("$rand-04-01")), setting)

                val tarif = dao.getTarif(setting.tarif)
                call.respond(FreeMarkerContent("index.ftl", mapOf("user" to user, "setting" to setting, "tarif" to tarif, "oldPay" to dao.getOldInfo(setting.old_pay_Date))))
            }else{
                call.respond(FreeMarkerContent("index.ftl", mapOf("user" to user, "setting" to setting)))
            }
        }
    }
}