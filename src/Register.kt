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
import model.Setting
import org.joda.time.DateTime
import java.time.LocalDate.now

fun Route.register(dao: DAOFacade, hashFunction: (String) -> String) {
    post<Register> {
        val user = call.sessions.get<UserSession>()?.let { dao.user(it.login) }

        if (user != null) return@post call.redirect(UserPage())

        val registration = call.receive<Parameters>()
        val login = registration["login"] ?: return@post call.redirect(it)
        val pass = registration["password"] ?: return@post call.redirect(it)
        val name = registration["name"] ?: return@post call.redirect(it)

        val error = Register(login, name)
        var userId = -1

        when {
            pass.length < 6 -> call.redirect(error.copy(error = "Пароль должен быть больше 6 символов"))
            login.length < 4 -> call.redirect(error.copy(error = "Логин должен быть больше 4 символов"))
            !userNameValid(login) -> call.redirect(error.copy(error = "Логин должен состоять из цифр, букв, точек или подчеркиваний"))
            dao.user(login) != null -> call.redirect(error.copy(error = "Пользователь с таким логином уже зарегистрирован"))
            else -> {
                val hash = hashFunction(pass)
                val newUser = User(DateTime.now(), name, login, hash, -1)

                try {
                    userId = dao.newUser(newUser)
                } catch (e: Throwable) {
                    when {
                        dao.user(login) != null -> call.redirect(error.copy(error = "Пользователь с таким логином уже зарегистрирован"))
                        else -> {
                            application.log.error("Ошибка регистрации", e)
                            call.redirect(error.copy(error = "Ошибка регистрации"))
                        }
                    }
                }

                val tarifId = dao.newTarif(userId, Tarif(DateTime.parse("2020-02-01"),2.61,32.162,0.8018,1.0904,2.6808,150,800))
                dao.setSettings(userId, Setting(tarifId))

                call.sessions.set(UserSession(newUser.login))
//                call.redirect(UserPage(newUser.login))
                call.redirect(UserPage())
            }
        }
    }

    get<Register> {
        val user = call.sessions.get<UserSession>()?.let { dao.user(it.login) }
        if (user != null) {
//            call.redirect(UserPage(user.login))
            call.redirect(UserPage())
        } else {
            call.respond(FreeMarkerContent("register.ftl", mapOf("pageUser" to User(DateTime.now(), it.name, it.login, "", -1), "error" to it.error), ""))
        }
    }
}
