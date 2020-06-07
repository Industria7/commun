import io.ktor.application.*
import io.ktor.freemarker.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.get
import io.ktor.sessions.sessions
import io.ktor.sessions.set
import model.*
import org.joda.time.DateTime
import java.lang.NumberFormatException
import kotlin.random.Random

fun Route.calc() {
    get<Calc> {
        val user = call.sessions.get<UserSession>()?.let { dao.user(it.Login) }
        if(user == null) {
            call.redirect(Login())
        } else {
            val sett = dao.getSettings(user.id)

            val tarif = dao.getTarif(sett!!.tarif)
            call.respond(FreeMarkerContent("calc.ftl", mapOf("user" to user, "setting" to sett, "tarif" to tarif, "oldPay" to dao.getOldInfo(sett.old_pay_Date) ) ) )
        }
    }

    post<Calc>{
        val user = call.sessions.get<UserSession>()?.let { dao.user(it.Login) }
        if(user == null) {
            call.redirect(Login())
        } else {
        val calc: Parameters = call.receiveParameters()
        println(calc)

        val error = Calc()

            val g = calc["gas"] ?: return@post call.redirect(error.copy(error = "Введиде показания Газа"))
            val w = calc["water"] ?: return@post call.redirect(error.copy(error = "Введиде показания Воды"))
            val e = calc["elec"] ?: return@post call.redirect(error.copy(error = "Введиде показания Электричества"))
            val t = calc["id"] ?: return@post call.redirect(error.copy(error = "Выберите Тариф"))
        try {
            val gs = g.toInt()
            val wt = w.toInt()
            val el = e.toInt()
            val tr = t.toInt()

            val inf = Info(gs ,wt ,el, tr)
            val tar = dao.getTarif(tr)

            if (tar != null) {
                val data = calculate(tar, inf)
                call.respond(FreeMarkerContent("calc.ftl", mapOf("user" to user, "data" to data ) ) )
            }

        } catch (e: NumberFormatException){
            call.redirect(error.copy(error = "Неправильный формат числа"))
        }
        }
    }
}