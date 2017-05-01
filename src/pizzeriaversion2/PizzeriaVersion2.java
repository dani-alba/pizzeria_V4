/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pizzeriaversion2;

import Modelo.Precios;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Optional;
import java.util.stream.Stream;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Daniel
 */
public class PizzeriaVersion2 extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Optional<ButtonType> resultado;
        ButtonType marcado;
        FXMLDocumentController datosLogin;
        File archivo = null;
        Precios precio;
        Parent root;
        Alert alerta, error;
        GridPane contenidoExpansible;
        TabPane tabPane;
        Tab tabFormato,tabPrecios;
        TextArea textoFormato,textoPrecios;
        Scene scene;
        FXMLLoader loader;
        FileChooser selector;

        loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("FXMLDocument.fxml"));
        root = loader.load();
        datosLogin = loader.getController();
        scene = new Scene(root);
        stage.setTitle("DPIZZA");
        stage.initStyle(StageStyle.DECORATED);
        stage.getIcons().add(new Image("file:images/icono_pizza.png"));
        stage.resizableProperty().setValue(Boolean.FALSE);
        stage.setScene(scene);
        alerta = new Alert(AlertType.CONFIRMATION);
        alerta.setTitle("Cargar Precios");
        alerta.setHeaderText("¿Desea cargar los precios?");
        alerta.setContentText("Si no selecciona ningun archivo se cargarán unos precios por defecto.");
        textoPrecios = new TextArea(leerArchivosIniciales("Precios"));
        textoFormato = new TextArea(leerArchivosIniciales("Formato"));
        textoPrecios.setEditable(false);
        textoPrecios.setWrapText(true);
        textoPrecios.setMaxWidth(Double.MAX_VALUE);
        textoFormato.setMaxWidth(Double.MAX_VALUE);
        textoPrecios.setMaxHeight(Double.MAX_VALUE);
        textoFormato.setMaxHeight(Double.MAX_VALUE);
        textoFormato.setEditable(false);
        textoFormato.setWrapText(true);
        tabPane = new TabPane();
        tabPrecios = new Tab();
        tabFormato = new Tab();
        tabPrecios.setClosable(false);
        tabFormato.setClosable(false);
        tabPrecios.setText("Precios Defecto");
        tabFormato.setText("Formato Archivo Carga");
        tabPrecios.setContent(textoPrecios);
        tabFormato.setContent(textoFormato);
        tabPane.getTabs().add(tabPrecios);
        tabPane.getTabs().add(tabFormato);
        GridPane.setVgrow(tabPane, Priority.ALWAYS);
        GridPane.setHgrow(tabPane, Priority.ALWAYS);
        contenidoExpansible = new GridPane();
        contenidoExpansible.setMaxWidth(Double.MAX_VALUE);
        contenidoExpansible.add(tabPane, 0, 1);
        contenidoExpansible.getStylesheets().add(getClass().getResource("estilosPizzeria.css").toExternalForm());
        alerta.getDialogPane().setExpandableContent(contenidoExpansible);
        darleEstiloAlPanel(alerta);
        resultado = alerta.showAndWait();
        marcado = resultado.get();

        if (marcado == ButtonType.OK) {
            selector = new FileChooser();
            selector.setTitle("Selector de archivos");
            archivo = selector.showOpenDialog(new Stage());

            if (archivo == null) {
                error = new Alert(AlertType.ERROR);
                error.setTitle("Error de Carga");
                error.setHeaderText("No ha seleccionado ningún archivo");
                error.setContentText("A continuación se cargaran unos precios por defecto.");
                darleEstiloAlPanel(error);
                error.showAndWait();
            }
        }
        precio = new Precios(archivo);
        datosLogin.setPrecio(precio);
        datosLogin.cargarDatos();
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    private void darleEstiloAlPanel(Alert panel) {
        DialogPane dialogPane;
        Stage alertaStage;

        dialogPane = panel.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("estilosPizzeria.css").toExternalForm());
        dialogPane.getStyleClass().add("dialog-pane");
        alertaStage = (Stage) panel.getDialogPane().getScene().getWindow();
        alertaStage.getIcons().add(new Image("file:images/icono_pizza.png"));
    }

    private String leerArchivosIniciales(String tipo) {
        String resultado = "";
        Path archivo = null;

        if (tipo.equalsIgnoreCase("Precios")) {
            archivo = Paths.get("ArchivosVarios/Archivo_que_muestra_los_precios_por_defecto.txt");
        } else if (tipo.equalsIgnoreCase("Formato")) {
            archivo = Paths.get("ArchivosVarios/Formato_archivo_precios.txt");
        }

        try (Stream<String> datos = Files.lines(archivo, StandardCharsets.ISO_8859_1)) {
            Iterator<String> it = datos.iterator();
            while (it.hasNext()) {
                resultado = resultado + it.next() + "\n";
            }
        } catch (Exception e) {
            /*Ninguno de los StandartCharsets me permite visualizar el simbolo del euro*/
        }
        return resultado;
    }
}
