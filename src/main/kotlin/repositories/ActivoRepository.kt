package org.example.repositories

import java.sql.DriverManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.example.models.Activo
import java.sql.ResultSet
import java.sql.SQLException

class ActivoRepository {
    private val jdbcUrl = "jdbc:sqlserver://localhost:1433;databaseName=FarmaciaActivos;user=sa;password=@Dmin123;encrypt=true;trustServerCertificate=true"

    suspend fun buscarPorActivoFijo(activoFijo: String): Activo? = withContext(Dispatchers.IO) {
        DriverManager.getConnection(jdbcUrl).use { conn ->
            val sql = """
                SELECT [Activo fijo], [Nombre Activo], [NombreCC], [Fecha de Alta]
                FROM Activos
                WHERE [Activo fijo] = ?
            """
            conn.prepareStatement(sql).use { stmt ->
                stmt.setString(1, activoFijo)
                val rs = stmt.executeQuery()
                if (rs.next()) {
                    Activo(
                        rs.getString("Activo fijo"),
                        rs.getString("Nombre Activo"),
                        rs.getString("NombreCC"),
                        rs.getString("Fecha de Alta")
                    )
                } else null
            }
        }
    }
    suspend fun listarActivosFarmacia(nombreFarmacia: String): List<Activo> = withContext(Dispatchers.IO){
        DriverManager.getConnection(jdbcUrl).use { conn ->
            val sql = """
                SELECT [Activo fijo], [Nombre Activo], [NombreCC], NULLIF([Fecha de Alta], '') AS [Fecha de Alta]
                FROM Activos
                WHERE [NombreCC] LIKE ?
            """
            conn.prepareStatement(sql).use { stmt ->
                stmt.setString(1, "%$nombreFarmacia%")
                val rs = stmt.executeQuery()
                val activos = mutableListOf<Activo>()
                while (rs.next()) {
                    activos.add(
                        Activo(
                            rs.getString("Activo fijo"),
                            rs.getString("Nombre Activo"),
                            rs.getString("NombreCC"),
                            rs.getStringOrNull("Fecha de Alta")
                        )
                    )
                }
                activos
            }
        }
    }
    // funcion para validar null en campo de la consulta a la bd
    fun ResultSet.getStringOrNull(column: String): String? {
        return try {
            this.getString(column).takeIf { !this.wasNull() }
        } catch (e: SQLException) {
            null
        }
    }
    // LISTAR TODOS LOS 20 PRIMEROS
    /*suspend fun listarTodos(): List<Activo> = withContext(Dispatchers.IO) {
        DriverManager.getConnection(jdbcUrl).use { conn ->
            val sql = "SELECT TOP(20) [Activo fijo], [Nombre Activo], [NombreCC], [Fecha de Alta] FROM Activos"
            conn.prepareStatement(sql).use { stmt ->
                val rs = stmt.executeQuery()
                val activos = mutableListOf<Activo>()
                while (rs.next()) {
                    activos.add(
                        Activo(
                            rs.getString("Activo fijo"),
                            rs.getString("Nombre Activo"),
                            rs.getString("NombreCC"),
                            rs.getString("Fecha de Alta")
                        )
                    )
                }
                activos
            }
        }
    }*/
}
