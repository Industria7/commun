import io.ktor.application.*
import io.ktor.http.content.*
import io.ktor.locations.*
import io.ktor.response.*
import io.ktor.routing.*

@Location("/styles/main.css")
class MainCss()

//@Location("/favicon.ico")
//class MainIcon

/**
 * Register the styles, [MainCss] route (/styles/main.css)
 */
fun Route.styles() {
    /**
     * On a GET request to the [MainCss] route, it returns the `blog.css` file from the resources.
     *
     * Here we could preprocess or join several CSS/SASS/LESS.
     */
    get<MainCss> {
        call.respond(call.resolveResource("styles.css")!!)
    }

    static("/img") {
        resources("img")
    }
}

//fun Route.favicon(){
//    get<MainIcon>{
//        call.respond(call.resolveResource("favicon.ico")!!)
//    }
////    route("/favicon.ico").handler {
////        it.response().putHeader("Content-Type", "image/x-icon").sendFile("webroot/favicon.ico")
////    }
//}
