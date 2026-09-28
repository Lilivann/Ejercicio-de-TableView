package es.ivanalonsof.tablapersonas.config;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    private static final Dotenv dotenv = Dotenv.load();

    private static final String URL =
            "jdbc:mariadb://localhost:3306/" + dotenv.get("DB_NAME");

    private static final String USUARIO = dotenv.get("DB_USER");
    private static final String PASSWORD = dotenv.get("DB_PASSWORD");

    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, PASSWORD);
    }
}
