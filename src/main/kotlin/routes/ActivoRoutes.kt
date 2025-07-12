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
        get("/listar") {
            try {
                val filtro = call.request.queryParameters["nombre"]
                    ?: return@get call.respond(
                        HttpStatusCode.BadRequest,
                        "Parámetro 'filtro' requerido (ej: /listar?filtro=NombreFarmacia)"
                    )

                val activos = repo.listarActivosFarmacia(filtro)

                if (activos.isEmpty()) {
                    call.respond(
                        HttpStatusCode.NotFound,
                        "No se encontraron activos para '$filtro'"
                    )
                } else {
                    call.respond(activos)
                }
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    "Error al buscar activos: ${e.message}"
                )
            }
        }
    }
}