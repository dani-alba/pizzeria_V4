package Modelo;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DialogPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class Precios {

    private Map<String, Double> listaPreciosIngredientes = new HashMap<>(),
            listaPreciosPizzas = new HashMap<>(),
            listaPreciosMasas = new HashMap<>(),
            listaPreciosTamaños = new HashMap<>();

    /*Creo esta lista para hacer la comprobación de que están mínimo todos los ingredientes
    de los botones creados*/
    private Map<String, Double> listaPreciosIngredientesSeguridad = new HashMap<>(),
            listaPreciosPizzasSeguridad = new HashMap<>(),
            listaPreciosMasasSeguridad = new HashMap<>(),
            listaPreciosTamañosSeguridad = new HashMap<>();

    private File archivo;

    private boolean ArchivoDePrecios;

    public Precios(File archivo) {
        /*Aquí analizamos que exista un archivo de donde cargar los precios, y que
        de ser así como mínimo contenga los precios de los ingdientes para 
        los cuales hemos diseñado la app. En el caso de no cumplir esta condicion
        carga los precios por defecto (Para ello nos ayudamos del metodo
        comprobarPrecios. Para que el archivo de cargar precios sea correcto
        tenemos que analizar si empieza on carta de precios, para así distingirlo
        de cualquier otro archivo y no salte una exception*/

        boolean cargaErronea = false;

        this.archivo = archivo;
        if (this.archivo == null) {
            this.cargarPreciosDefecto();
            cargaErronea = true;
        } else {
            this.cargarPrecios();
            if (this.ArchivoDePrecios == true) { // es un archivo con el formato correcto
                this.cargarPreciosDefecto();
                if (comprobarPrecios() == false) { // comprueba que minimo esten todos los elementos de la app
                    this.archivo = null;
                    this.cargarPreciosDefecto();
                    cargaErronea = true;
                }

            } else {
                this.archivo = null;
                this.cargarPreciosDefecto();
                cargaErronea = true;
            }
        }

        mensajeCorrecto(cargaErronea);

    }

    private void cargarPrecios() {
        ArchivoDePrecios = true;
        boolean primeraLinea = true;
        try (Stream<String> datos = Files.lines(this.archivo.toPath(), StandardCharsets.ISO_8859_1)) {
            Iterator<String> it = datos.iterator();
            int entrarLista = 0;
            double precio;
            while (it.hasNext() && ArchivoDePrecios == true) {
                String linea = it.next();

                if (primeraLinea == true) {
                    if (linea.equalsIgnoreCase("CARTAPRECIOS")) {
                        primeraLinea = false;
                    } else {
                        ArchivoDePrecios = false;
                        Alert alerta = new Alert(AlertType.ERROR);
                        alerta.setTitle("ERROR CARGA");
                        alerta.setHeaderText("No has seleccionado una archivo con precios");
                        alerta.setContentText("A continuación se cargarán los precios por defecto.");
                        darleEstiloAlPanel(alerta);
                        alerta.showAndWait();
                    }
                }
                if (!linea.equalsIgnoreCase("CARTAPRECIOS")) {
                    if (ArchivoDePrecios == true) {
                        if (!linea.equals("//")) {
                            if (linea.equalsIgnoreCase("TIPOMASA")) {
                                entrarLista = 1;
                            } else if (linea.equalsIgnoreCase("TIPOPIZZA")) {
                                entrarLista = 2;
                            } else if (linea.equalsIgnoreCase("INGREDIENTES")) {
                                entrarLista = 3;
                            } else if (linea.equalsIgnoreCase("TAMAÑO")) {
                                entrarLista = 4;
                            } else {
                                String palabra[] = linea.split(":");
                                String ingredientes = palabra[0];
                                precio = Double.parseDouble(palabra[1]);
                                switch (entrarLista) {
                                    case 1:
                                        listaPreciosMasas.put(ingredientes, precio);
                                        break;
                                    case 2:
                                        listaPreciosPizzas.put(ingredientes, precio);
                                        break;
                                    case 3:
                                        listaPreciosIngredientes.put(ingredientes, precio);
                                        break;
                                    case 4:
                                        listaPreciosTamaños.put(ingredientes, precio);
                                        break;
                                }

                            }
                        }
                    }
                }
            }
        } catch (IOException ex) {

        }

    }

    private boolean comprobarPrecios() {
        List<String> datosQueFaltan = new ArrayList();
        Double precio;
        boolean preciosCorrectos = true;

        // COMPROBACIÓN DE INGREDIENTES
        for (String vIngrediente : this.listaPreciosIngredientesSeguridad.keySet()) {
            precio = this.listaPreciosIngredientesSeguridad.get(vIngrediente);

            if (!this.listaPreciosIngredientes.containsKey(vIngrediente)) {
                datosQueFaltan.add(vIngrediente);
            }
        }
        // COMPROBACIÓN DE MASAS
        for (String vIngrediente : this.listaPreciosMasasSeguridad.keySet()) {
            precio = this.listaPreciosMasasSeguridad.get(vIngrediente);

            if (!this.listaPreciosMasas.containsKey(vIngrediente)) {
                datosQueFaltan.add(vIngrediente);
            }
        }

        // COMPROBACIÓN DE PIZZAS
        for (String vIngrediente : this.listaPreciosPizzasSeguridad.keySet()) {
            precio = this.listaPreciosPizzasSeguridad.get(vIngrediente);

            if (!this.listaPreciosPizzas.containsKey(vIngrediente)) {
                datosQueFaltan.add(vIngrediente);
            }
        }

        // COMPROBACIÓN DE TAMAÑOS
        for (String vIngrediente : this.listaPreciosTamañosSeguridad.keySet()) {
            precio = this.listaPreciosTamañosSeguridad.get(vIngrediente);

            if (!this.listaPreciosTamaños.containsKey(vIngrediente)) {
                datosQueFaltan.add(vIngrediente);
            }
        }

        if (!datosQueFaltan.isEmpty()) {
            preciosCorrectos = false;
            Alert alerta = new Alert(AlertType.ERROR);
            alerta.setTitle("ERROR DE CARGA");
            alerta.setHeaderText("Faltan elementos por cargar");
            alerta.setContentText(datosQueFaltan.toString() + "\nCargaremos la lista de precios"
                    + " por defecto");
            darleEstiloAlPanel(alerta);
            alerta.showAndWait();

        }

        return preciosCorrectos;

    }

    private void cargarPreciosDefecto() {

        if (this.archivo == null) {
            listaPreciosIngredientes.put("Jamon", 0.50);
            listaPreciosIngredientes.put("Queso", 0.75);
            listaPreciosIngredientes.put("Tomate", 1.50);
            listaPreciosIngredientes.put("Cebolla", 2.50);
            listaPreciosIngredientes.put("Olivas", 1.00);

            listaPreciosPizzas.put("Ninguna", 0.0);
            listaPreciosPizzas.put("Basica", 3.00);
            listaPreciosPizzas.put("4 Quesos", 5.00);
            listaPreciosPizzas.put("Barbacoa", 7.00);
            listaPreciosPizzas.put("Mexicana", 8.50);

            listaPreciosMasas.put("Normal", 9.00);
            listaPreciosMasas.put("Integral", 9.50);

            listaPreciosTamaños.put("Pequeña", 1.0);
            listaPreciosTamaños.put("Mediana", 1.15);
            listaPreciosTamaños.put("Familiar", 1.30);

        } else {
            listaPreciosIngredientesSeguridad.put("Jamon", 0.50);
            listaPreciosIngredientesSeguridad.put("Queso", 0.75);
            listaPreciosIngredientesSeguridad.put("Tomate", 1.50);
            listaPreciosIngredientesSeguridad.put("Cebolla", 2.50);
            listaPreciosIngredientesSeguridad.put("Olivas", 1.00);

            listaPreciosPizzasSeguridad.put("Ninguna", 0.0);
            listaPreciosPizzasSeguridad.put("Basica", 3.00);
            listaPreciosPizzasSeguridad.put("4 Quesos", 5.00);
            listaPreciosPizzasSeguridad.put("Barbacoa", 7.00);
            listaPreciosPizzasSeguridad.put("Mexicana", 8.50);

            listaPreciosMasasSeguridad.put("Normal", 9.00);
            listaPreciosMasasSeguridad.put("Integral", 9.50);

            listaPreciosTamañosSeguridad.put("Pequeña", 1.0);
            listaPreciosTamañosSeguridad.put("Mediana", 1.15);
            listaPreciosTamañosSeguridad.put("Familiar", 1.30);
        }

    }

    private void darleEstiloAlPanel(Alert panel) {

        try {
            DialogPane dialogPane = panel.getDialogPane();
            dialogPane.getStylesheets().add(getClass().getResource("estilos.css").toExternalForm());
            dialogPane.getStyleClass().add("dialog-pane");
            Stage alertaStage = (Stage) panel.getDialogPane().getScene().getWindow();
            alertaStage.getIcons().add(new Image("file:images/icono_pizza.png"));
        } catch (Exception e) {
            /*Tras bastantes horas intentando sacar la ruta del css estilosPizzeria,
            he decidido aun a sabiendas de que no sería la mejor solución, colocar
            un css dentro de modelo solo para el alert dialog. Esto no lo dejaría 
            así en un proyecto real, pero como la entrega es el miercoles que viene
            y desgraciadamente no le puedo dedicar más tiempo, lo dejo así para 
            que al menos a la hora de correr la aplicación se vea la parte visual 
            que respecta a esta modificación.*/
        }
    }

    private void mensajeCorrecto(boolean condicion) {
        
        if (condicion == false) {
            Alert alerta = new Alert(AlertType.INFORMATION);
            alerta.setTitle("CARGA DE PRECIOS");
            alerta.setHeaderText("Carga correcta");
            alerta.setContentText(null);
            Image correcto = new Image("file:images/preciosCargados.png", 130, 120, false, false);
            alerta.setGraphic(new ImageView(correcto));
            darleEstiloAlPanel(alerta);
            alerta.showAndWait();
        }
    }

    // * * * * * * * * GET AND SET * * * * * * * * * * 
    public Map<String, Double> getListaPreciosIngredientes() {
        return listaPreciosIngredientes;
    }

    public Map<String, Double> getListaPreciosPizzas() {
        return listaPreciosPizzas;
    }

    public Map<String, Double> getListaPreciosMasas() {
        return listaPreciosMasas;
    }

    public Map<String, Double> getListaPreciosTamaños() {
        return listaPreciosTamaños;
    }

    public Double precioMasa(String masa) {
        return listaPreciosMasas.get(masa);
    }

    public Double precioTipo(String tipo) {
        return listaPreciosPizzas.get(tipo);
    }

    public Double precioIngre(List listIngre) {
        Double precio = 0d;

        for (Object ingre : listIngre) {
            precio = precio + listaPreciosIngredientes.get(ingre);
        }

        return precio;
    }

    public Double precioDeUnIngrediente(String ingre) {
        return listaPreciosIngredientes.get(ingre);
    }

    public Double precioTamaño(String tamaño) {
        return listaPreciosTamaños.get(tamaño);
    }
}
