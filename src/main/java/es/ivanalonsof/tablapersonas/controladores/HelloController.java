package es.ivanalonsof.tablapersonas.controladores;

import es.ivanalonsof.tablapersonas.dao.PersonaDAO;
import es.ivanalonsof.tablapersonas.modelo.Persona;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.util.ResourceBundle;

/**
 * Controla la interfaz de la tabla de personas.
 * Recoge los datos del formulario y utiliza PersonaDAO
 * para añadir, borrar y restaurar personas.
 */
public class HelloController {

    @FXML
    private TableColumn<Persona, String> colApellido;

    @FXML
    private TableColumn<Persona, LocalDate> colFechaNac;

    @FXML
    private TableColumn<Persona, Integer> colId;

    @FXML
    private TableColumn<Persona, String> colNombre;

    @FXML
    private DatePicker dpFechaNacimiento;

    @FXML
    private TableView<Persona> tablaPersonas;

    @FXML
    private TextField txtApellido;

    @FXML
    private TextField txtNombre;

    private final PersonaDAO personaDAO = new PersonaDAO();

    private final ResourceBundle textos =
            ResourceBundle.getBundle(
                    "es.ivanalonsof.tablapersonas.textos"
            );

    /**
     * Inicializa la interfaz al cargar el archivo FXML.
     * Conecta las columnas con los datos de Persona,
     * asigna la lista del DAO a la tabla y permite
     * seleccionar varias filas.
     */
    @FXML
    public void initialize() {
        colId.setCellValueFactory(
                new PropertyValueFactory<>("id")
        );
        colNombre.setCellValueFactory(
                new PropertyValueFactory<>("nombre")
        );
        colApellido.setCellValueFactory(
                new PropertyValueFactory<>("apellido")
        );
        colFechaNac.setCellValueFactory(
                new PropertyValueFactory<>("fechaNacimiento")
        );

        // Mostrar la lista que gestiona el DAO.
        tablaPersonas.setItems(personaDAO.obtenerPersonas());

        tablaPersonas.getSelectionModel()
                .setSelectionMode(SelectionMode.MULTIPLE);
    }

    /**
     * Recoge y comprueba los datos del formulario.
     * Si están completos y la fecha se puede interpretar,
     * añade la persona mediante el DAO y limpia los campos.
     * En caso contrario, muestra un aviso.
     */
    @FXML
    private void anadirPersona() {
        String nombre = txtNombre.getText().trim();
        String apellido = txtApellido.getText().trim();

        try {
            dpFechaNacimiento.commitValue();
        } catch (RuntimeException e) {
            mostrarAviso("Introduce una fecha válida.");
            return;
        }

        LocalDate fechaNacimiento = dpFechaNacimiento.getValue();

        if (nombre.isEmpty() || apellido.isEmpty()
                || fechaNacimiento == null) {
            mostrarAviso("Completa el nombre, el apellido y la fecha.");
            return;
        }

        // El DAO crea y guarda la persona.
        personaDAO.anadirPersona(nombre, apellido, fechaNacimiento);

        txtNombre.clear();
        txtApellido.clear();
        dpFechaNacimiento.setValue(null);
        dpFechaNacimiento.getEditor().clear();
        txtNombre.requestFocus();
    }

    /**
     * Solicita al DAO que borre las personas seleccionadas
     * y limpia la selección de la tabla.
     * Muestra un aviso si no hay ninguna fila seleccionada.
     */
    @FXML
    private void borrarPersonas() {
        if (tablaPersonas.getSelectionModel()
                .getSelectedItems().isEmpty()) {
            mostrarAviso("Selecciona al menos una fila para borrar.");
            return;
        }

        // El DAO copia la selección y elimina las personas.
        personaDAO.borrarPersonas(
                tablaPersonas.getSelectionModel().getSelectedItems()
        );

        tablaPersonas.getSelectionModel().clearSelection();
    }

    /**
     * Solicita al DAO que restaure las personas borradas.
     * Muestra un aviso si no hay personas para recuperar.
     */
    @FXML
    private void restaurarPersonas() {
        if (!personaDAO.hayPersonasBorradas()) {
            mostrarAviso("No hay personas borradas para restaurar.");
            return;
        }

        personaDAO.restaurarPersonas();
    }

    /**
     * Muestra una ventana de advertencia con el mensaje indicado
     * y el título obtenido del archivo de traducciones.
     * Espera a que se cierre la ventana para continuar.
     *
     * @param mensaje texto que se muestra en la advertencia
     */
    private void mostrarAviso(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.WARNING);
        alerta.setTitle(textos.getString("aviso.titulo"));
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}