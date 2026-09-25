package es.ivanalonsof.tablapersonas.modelo;

import java.time.LocalDate;

public class Persona {

    /**
     * Representa una persona con su identificador, nombre,
     * apellido y fecha de nacimiento.
     */
    private int id;
    private String nombre;
    private String apellido;
    private LocalDate fechaNacimiento;


    /**
     * Crea una persona con los datos indicados.
     *
     * @param id identificador de la persona
     * @param nombre nombre de pila de la persona
     * @param apellido apellido de la persona
     * @param fechaNacimiento fecha de nacimiento de la persona
     */
    public Persona(int id, String nombre, String apellido,
                   LocalDate fechaNacimiento) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.fechaNacimiento = fechaNacimiento;
    }

    /**
     * Devuelve el identificador de la persona.
     *
     * @return identificador de la persona
     */
    public int getId() {
        return id;
    }

    /**
     * Devuelve el nombre de la persona.
     *
     * @return nombre de la persona
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Devuelve el apellido de la persona.
     *
     * @return apellido de la persona
     */
    public String getApellido() {
        return apellido;
    }

    /**
     * Devuelve la fecha de nacimiento de la persona.
     *
     * @return fecha de nacimiento de la persona
     */
    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }
}