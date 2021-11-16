import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.associate
import com.github.ajalt.clikt.parameters.options.help
import com.github.ajalt.clikt.parameters.options.option
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.html.*
import io.ktor.http.ContentType.Application.Json
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.html.a
import kotlinx.html.body
import kotlinx.html.h1
import kotlinx.html.p
import java.io.File

fun main(args: Array<String>) = Server().main(args)

class Server : CliktCommand() {

    private val routes: Map<String, String> by option("--route")
        .help(
            "A route is a mapping between a REST path and the file from which to serve the contents " +
                    "(e.g. --route /api/something=/data/big_file.json)"
        )
        .associate()

    override fun run() {
        val port = System.getenv("PORT")?.toInt() ?: 9999
        val host = System.getenv("HOST") ?: "0.0.0.0"

        embeddedServer(Netty, port = port, host = host) {

            install(CallLogging)

            log.info("Routes input: $routes")
            require(routes.isNotEmpty()) { "No routes provided! Please provide at least one `--route xxx=yyy` or use `--help` for more information" }

            val routesMappedToFiles = mapToFiles()

            routing {
                routesMappedToFiles.forEach { (restPath, file) ->
                    log.info("Creating route '$restPath' to serve contents from $file")
                    createRoute(restPath, file)
                }
                indexPage(routesMappedToFiles)
            }
        }.start(wait = true)
    }

    // TODO: use template (https://ktor.io/docs/html-dsl.html#templates)
    private fun Routing.indexPage(routesMappedToFiles: Map<String, File>) {
        get("/") {
            call.respondHtml {
                body {
                    h1 {
                        +"Serving:"
                    }
                    routesMappedToFiles.forEach { (restPath, file) ->
                        p {
                            a {
                                href = restPath
                                +restPath
                            }
                            +" (${file.absolutePath})"
                        }
                    }
                }
            }
        }
    }

    private fun Routing.createRoute(restPath: String, file: File) {
        get(restPath) {
            call.respondBytes(contentType = Json, status = OK) {
                // TODO: Stream data(?)
                val bytes = file.readBytes()
                println("Serving ${call.request} with ${bytes.size} bytes from $file")
                bytes
            }
        }
    }

    private fun mapToFiles() = routes.mapValues { (_, filePath) -> File(filePath) }
        .mapValues { (_, file) ->
            require(file.isFile && file.canRead()) { "Not a file or cannot read: $file" }
            file
        }
}
