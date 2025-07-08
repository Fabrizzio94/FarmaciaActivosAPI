package org.example.repositories

import java.sql.DriverManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.example.models.Activo

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
    suspend fun listarActivosFarmacia(nombreFarmacia: String): Activo? = withContext(Dispatchers.IO){
        DriverManager.getConnection(jdbcUrl).use { conn ->
            val sql = """
                SELECT [Activo fijo], [Nombre Activo], [NombreCC], [Fecha de Alta]
                FROM Activos
                WHERE [NombreCC] LIKE ?
            """
            conn.prepareStatement(sql).use { stmt ->
                stmt.setString(3, nombreFarmacia)
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
}
