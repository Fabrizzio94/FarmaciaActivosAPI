package org.example.routes

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.example.repositories.ActivoRepository

fun Route.activoRoutes() {
    val repo = ActivoRepository()
    route("/activos") {
        get("/{activoFijo}") {
            val activoFijo = call.parameters["activoFijo"]
                ?: return@get call.respondText("Falta 'activoFijo'", status = HttpStatusCode.BadRequest)

            val activo = repo.buscarPorActivoFijo(activoFijo)
            activo?.let { call.respond(it) }
                ?: call.respondText("No encontrado", status = HttpStatusCode.NotFound)
        }
        // Ejemplo de otro endpoint: GET /activos/listar?farmacia={nombre}
        // GET /activos/listar?farmacia={nombre}
        get("/listar") {
            val farmacia = call.request.queryParameters["farmacia"]
            // Aquí iría la lógica para filtrar por farmacia (implementa en Repository)
            call.respondText("Listado de activos para $farmacia")
//            val activos = if (farmacia != null) {
//                repo.listarPorFarmacia(farmacia)
//            } else {
//                repo.listarTodos()
//            }
//            call.respond(activos)
        }
    }
}