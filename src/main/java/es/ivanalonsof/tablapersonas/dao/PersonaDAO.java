package es.ivanalonsof.tablapersonas.dao;

import es.ivanalonsof.tablapersonas.modelo.Persona;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;

import es.ivanalonsof.tablapersonas.config.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Gestiona en memoria las personas y sus identificadores.
 * Permite añadir, borrar y restaurar personas.
 */
public class PersonaDAO {

    private final ObservableList<Persona> personas =
            FXCollections.observableArrayList();

    private final ObservableList<Persona> personasBorradas =
            FXCollections.observableArrayList();

    /**
     * Devuelve la lista observable de personas activas.
     *
     * @return lista de personas que se muestran en la tabla
     */
    public ObservableList<Persona> obtenerPersonas() {

        personas.clear();

        String sql = "SELECT id, nombre, apellido, fecha_nacimiento " +
                "FROM personas " +
                "ORDER BY id";

        try (
                Connection conexion = ConexionBD.conectar();
                PreparedStatement sentencia = conexion.prepareStatement(sql);
                ResultSet resultado = sentencia.executeQuery()
        ) {

            while (resultado.next()) {

                Persona persona = new Persona(
                        resultado.getInt("id"),
                        resultado.getString("nombre"),
                        resultado.getString("apellido"),
                        resultado.getDate("fecha_nacimiento").toLocalDate()
                );

                personas.add(persona);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return personas;
    }

    /**
     * Crea y añade una persona con un identificador automático.
     *
     * @param nombre nombre de la persona
     * @param apellido apellido de la persona
     * @param fechaNacimiento fecha de nacimiento de la persona
     */
    public void anadirPersona(String nombre, String apellido,
                              LocalDate fechaNacimiento) {

        String sql = "INSERT INTO personas " +
                "(nombre, apellido, fecha_nacimiento) " +
                "VALUES (?, ?, ?)";

        try (
                Connection conexion = ConexionBD.conectar();
                PreparedStatement sentencia = conexion.prepareStatement(sql)
        ) {

            sentencia.setString(1, nombre);
            sentencia.setString(2, apellido);
            sentencia.setDate(3, java.sql.Date.valueOf(fechaNacimiento));

            sentencia.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        obtenerPersonas();
    }

    /**
     * Elimina las personas seleccionadas de la lista activa
     * y las guarda para poder restaurarlas posteriormente.
     *
     * @param seleccionadas personas que se quieren borrar
     */
    public void borrarPersonas(Collection<Persona> seleccionadas) {

        String sql = "DELETE FROM personas WHERE id = ?";

        try (
                Connection conexion = ConexionBD.conectar();
                PreparedStatement sentencia = conexion.prepareStatement(sql)
        ) {

            for (Persona persona : new ArrayList<>(seleccionadas)) {

                sentencia.setInt(1, persona.getId());

                int filasAfectadas = sentencia.executeUpdate();

                if (filasAfectadas > 0) {
                    personas.remove(persona);
                    personasBorradas.add(persona);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Comprueba si existen personas borradas para restaurar.
     *
     * @return true si hay personas borradas; false en caso contrario
     */
    public boolean hayPersonasBorradas() {
        return !personasBorradas.isEmpty();
    }

    /**
     * Recupera todas las personas borradas conservando sus datos
     * e identificadores. Vacía la lista de personas borradas
     * y ordena la lista activa por identificador ascendente.
     */
    public void restaurarPersonas() {

        String sql = "INSERT INTO personas " +
                "(id, nombre, apellido, fecha_nacimiento) " +
                "VALUES (?, ?, ?, ?)";

        try (
                Connection conexion = ConexionBD.conectar();
                PreparedStatement sentencia = conexion.prepareStatement(sql)
        ) {

            for (Persona persona : new ArrayList<>(personasBorradas)) {

                sentencia.setInt(1, persona.getId());
                sentencia.setString(2, persona.getNombre());
                sentencia.setString(3, persona.getApellido());
                sentencia.setDate(
                        4,
                        java.sql.Date.valueOf(persona.getFechaNacimiento())
                );

                sentencia.executeUpdate();
            }

            personasBorradas.clear();

        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        obtenerPersonas();
    }
}
