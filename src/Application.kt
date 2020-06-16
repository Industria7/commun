import com.mchange.v2.c3p0.*
import freemarker.cache.*
import io.ktor.application.*
import io.ktor.features.*
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
import org.h2.*
import org.jetbrains.exposed.sql.*
import org.joda.time.DateTime
import java.io.*
import java.net.*
import java.util.concurrent.*
import javax.crypto.*
import javax.crypto.spec.*

/*
 * Classes used for the locations feature to build urls and register routes.
 */

@Location("/")
class Index()

@Location("/login")
data class Login(val login: String = "", val error: String = "")

@Location("/register")
data class Register(val login: String = "", val name: String = "", val error: String = "")

@Location("/user")
class UserPage()

@Location("/delete")
class Delete(val id: Int)

@Location("/add")
class Add()

@Location("/calc")
data class Calc(val error: String = "")

@Location("/edit")
class Edit(val id: Int)

@Location("/grafics")
class Graf()

@Location("/invoice")
class Invoice()

@Location("/logout")
class Logout()

data class UserSession(val login: String)

val hashKey = hex("6819b57a326945c1968f45236589")
val dir = File("info")
//val dir = File("build/db/")

//val pool = ComboPooledDataSource().apply {
//    driverClass = Driver::class.java.name
//    jdbcUrl = "jdbc:h2:file:${dir.canonicalFile.absolutePath}"
//    user = ""
//    password = ""
//}

/**
 * HMac SHA1 key spec for the password hashing.
 */
val hmacKey = SecretKeySpec(hashKey, "HmacSHA1")

/**
 * Constructs a facade with the database, connected to the DataSource configured earlier with the [dir]
 * for storing the database.
 */
val ConnectionString = "jdbc:h2:file:${dir.canonicalFile.absolutePath};DB_CLOSE_DELAY=-1"
val dao: DAOFacade = DAOFacadeDatabase(Database.connect(ConnectionString, driver = "org.h2.Driver"))

fun Application.main() {
    // First we initialize the database.
    dao.init()
    // And we subscribe to the stop event of the application, so we can also close the [ComboPooledDataSource] [pool].
//    environment.monitor.subscribe(ApplicationStopped) { pool.close() }
    // Now we call to a main with the dependencies as arguments.
    // Separating this function with its dependencies allows us to provide several modules with
    // the same code and different datasources living in the same application,
    // and to provide mocked instances for doing integration tests.
    mainWithDependencies(dao)

//    println("${dao.getSettings(1)}")
//    dao.run()
//    dao.showAll()
}

/**
 * This function is called from the entry point and tests to configure an application
 * using the specified [dao] [DAOFacade].
 */
fun Application.mainWithDependencies(dao: DAOFacade) {
    // This adds automatically Date and Server headers to each response, and would allow you to configure
    // additional headers served to each response.
    install(DefaultHeaders)
    // This uses use the logger to log every call (request/response)
    install(CallLogging)
    // Automatic '304 Not Modified' Responses
    install(ConditionalHeaders)
    // Supports for Range, Accept-Range and Content-Range headers
    install(PartialContent)
    // Allows to use classes annotated with @Location to represent URLs.
    // They are typed, can be constructed to generate URLs, and can be used to register routes.
    install(Locations)
    // Adds support to generate templated responses using FreeMarker.
    // We configure it specifying the path inside the resources to use to get the template files.
    // You can use <!-- @ftlvariable --> to annotate types inside the templates
    // in a way that works with IntelliJ IDEA Ultimate.
    // You can check the `resources/templates/*.ftl` files for reference.
    install(FreeMarker) {
        templateLoader = ClassTemplateLoader(this::class.java.classLoader, "templates")
    }

    install(Sessions) {
        cookie<UserSession>("SESSION") {
            transform(SessionTransportTransformerMessageAuthentication(hashKey))
        }
    }

    // Provides a hash function to be used when registering the resources.
    val hashFunction = { s: String -> hash(s) }

    // Register all the routes available to the application.
    // They are split in several methods and files, so it can scale for larger
    // applications keeping a reasonable amount of lines per file.
    routing {
        styles()
        index()
        add()

        delete()

        login(dao, hashFunction)
        register(dao, hashFunction)

        userPage(dao)

        calc()
        edit()
        graf()
        invoice()
    }
}

/**
 * Method that hashes a [password] by using the globally defined secret key [hmacKey].
 */
fun hash(password: String): String {
    val hmac = Mac.getInstance("HmacSHA1")
    hmac.init(hmacKey)
    return hex(hmac.doFinal(password.toByteArray(Charsets.UTF_8)))
}

/**
 * Allows to respond with a absolute redirect from a typed [location] instance of a class annotated
 * with [Location] using the Locations feature.
 */
suspend fun ApplicationCall.redirect(location: Any) {
    val host = request.host()
    val portSpec = request.port().let { if (it == 80) "" else ":$it" }
    val address = host + portSpec

    respondRedirect("http://$address${application.locations.href(location)}")
}

/**
 * Generates a security code using a [hashFunction], a [date], a [user] and an implicit [HttpHeaders.Referrer]
 * to generate tokens to prevent CSRF attacks.
 */
fun ApplicationCall.securityCode(date: Long, user: User, hashFunction: (String) -> String) =
        hashFunction("$date:${user.login}:${request.host()}:${refererHost()}")

/**
 * Verifies that a code generated from [securityCode] is valid for a [date] and a [user] and an implicit [HttpHeaders.Referrer].
 * It should match the generated [securityCode] and also not be older than two hours.
 * Used to prevent CSRF attacks.
 */
fun ApplicationCall.verifyCode(date: Long, user: User, code: String, hashFunction: (String) -> String) =
        securityCode(date, user, hashFunction) == code
                && (System.currentTimeMillis() - date).let { it > 0 && it < TimeUnit.MILLISECONDS.convert(2, TimeUnit.HOURS) }

/**
 * Obtains the [refererHost] from the [HttpHeaders.Referrer] header, to check it to prevent CSRF attacks
 * from other domains.
 */
fun ApplicationCall.refererHost() = request.header(HttpHeaders.Referrer)?.let { URI.create(it).host }

/**
 * Pattern to validate an `userId`
 */
private val userIdPattern = "[a-zA-Z0-9_\\.]+".toRegex()

/**
 * Validates that an [userId] (that is also the user name) is a valid identifier.
 * Here we could add additional checks like the length of the user.
 * Or other things like a bad word filter.
 */
internal fun userNameValid(login: String) = login.matches(userIdPattern)
