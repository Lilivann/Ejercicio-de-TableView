package es.ivanalonsof.tablapersonas.dao;

import es.ivanalonsof.tablapersonas.modelo.Persona;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;

/**
 * Gestiona en memoria las personas y sus identificadores.
 * Permite añadir, borrar y restaurar personas.
 */
public class PersonaDAO {

    private final ObservableList<Persona> personas =
            FXCollections.observableArrayList();

    private final ObservableList<Persona> personasBorradas =
            FXCollections.observableArrayList();

    private int siguienteId = 1;

    /**
     * Devuelve la lista observable de personas activas.
     *
     * @return lista de personas que se muestran en la tabla
     */
    public ObservableList<Persona> obtenerPersonas() {
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
        Persona persona = new Persona(
                siguienteId,
                nombre,
                apellido,
                fechaNacimiento
        );

        personas.add(persona);
        siguienteId++;
    }

    /**
     * Elimina las personas seleccionadas de la lista activa
     * y las guarda para poder restaurarlas posteriormente.
     *
     * @param seleccionadas personas que se quieren borrar
     */
    public void borrarPersonas(Collection<Persona> seleccionadas) {
        // Copiar para que los cambios en la tabla no alteren la selección.
        for (Persona persona : new ArrayList<>(seleccionadas)) {
            if (personas.remove(persona)) {
                personasBorradas.add(persona);
            }
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
        personas.addAll(personasBorradas);
        personasBorradas.clear();

        personas.sort(Comparator.comparingInt(Persona::getId));
    }
}
