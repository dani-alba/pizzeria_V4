package pizzeriaversion2;

import Modelo.Pizza;
import Modelo.Precios;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

/*DANIEL ALBA DIAZ*/

public class FXMLDocumentController implements Initializable {

    private Image imgBasica, img4quesos, imgBarbacoa, imgMexicana, imgHorno,
            imgPizzaTamaño, imgEmpresa, imgToldo, imgCocinero, imgInterrogante,
            imgPizza1, imgPizza2, imgPizza3, imgPizza4, imgPizza5, imgPizza6,
            imgPizza7, imgPizza8, imgPizza9;

    private Pizza pizza;

    private Precios precio; // Cargo el precio para el tooltip

    private DecimalFormat formateador = new DecimalFormat("#.##");

    private Object obejtoViejo = null;

    private File directorio;

    private int contCargaPizza = 0;

    @FXML
    private TitledPane acrd_masa;
    @FXML
    private RadioButton cb_integral;
    @FXML
    private TitledPane acrd_tipoPizza;
    @FXML
    private ComboBox<String> cb_tipoPizza;
    @FXML
    private TitledPane acrd_ingreExtra;
    @FXML
    private ImageView iv_imagenPizza;
    @FXML
    private Label lb_masaTitulo;
    @FXML
    private Label lb_tipoPizzaTitulo;
    @FXML
    private Label lb_ingredientesTitulo;
    @FXML
    private Label lb_masa;
    @FXML
    private Label lb_tipoPizza;
    @FXML
    private Label lb_ingredientes;
    @FXML
    private RadioButton cb_normal;
    @FXML
    private TextArea ta_ingredientes;
    @FXML
    private TitledPane acrd_tamaño;
    @FXML
    private Slider sb_tamaño;
    @FXML
    private ImageView iv_tamañoPizza;
    @FXML
    private Label lb_tamañoTitulo;
    @FXML
    private Label lb_tamaño;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private CheckBox cb_jamon;
    @FXML
    private CheckBox cb_queso;
    @FXML
    private CheckBox cb_tomate;
    @FXML
    private CheckBox cb_cebolla;
    @FXML
    private CheckBox cb_olivas;
    @FXML
    private Label lb_tmFamTitulo;
    @FXML
    private Label lb_tmMedTitulo;
    @FXML
    private Label lb_tmPeqTitulo;
    @FXML
    private Pane pn_datosFactura;
    @FXML
    private Label lb_totalPagar;
    @FXML
    private TextField tf_totalPagar;
    @FXML
    private ToggleGroup masa;
    @FXML
    private Pane pn_titulo;
    @FXML
    private Pane pn_fondoMenuSeleccion;
    @FXML
    private ImageView iv_ImagenEmpresa;
    @FXML
    private ImageView iv_toldo;
    @FXML
    private Pane pn_pizarra;
    @FXML
    private ImageView iv_cocinero;
    @FXML
    private Button bt_mostrarDetalle;
    @FXML
    private AnchorPane ap_ingredientesExtra;
    @FXML
    private ImageView iv_interrogante;
    @FXML
    private ImageView iv_cargaPizza;
    private WebView wv_web;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cb_tipoPizza.getItems().addAll("Ninguna", "Basica", "4 Quesos",
                "Barbacoa", "Mexicana");
        cb_tipoPizza.setValue("Ninguna");
        lb_tmMedTitulo.setStyle("-fx-font-weight:bold");
        tf_totalPagar.setText("0 €");

    }

    public void cargarDatos() {
        pizza = new Pizza(this.precio); // porque ya estamos creando una nueva pizza
        cargarImagenes();
        cargarHovers();
        iv_interrogante.setImage(imgInterrogante);
    }

    @FXML
    private void elegirMasaAction(ActionEvent event) {
        String tipoMasa;
        cb_normal.setUserData("Normal");
        cb_integral.setUserData("Integral");
        tipoMasa = masa.getSelectedToggle().getUserData().toString();
        lb_masa.setText(tipoMasa);
        pizza.setMasa(tipoMasa);
        tf_totalPagar.setText(formateador.format(pizza.calcularPrecio()) + " €");
    }

    @FXML
    private void elegirIngredienteAction(ActionEvent event) {
        subEleccionIngrediente(); // Para que no haya conflicto con lo del ctrl
    }

    private void subEleccionIngrediente() {
        List<String> listaIngredientes = new ArrayList<>();

        cb_jamon.setUserData("Jamon");
        cb_queso.setUserData("Queso");
        cb_tomate.setUserData("Tomate");
        cb_cebolla.setUserData("Cebolla");
        cb_olivas.setUserData("Olivas");

        if (cb_jamon.isSelected()) {
            listaIngredientes.add(cb_jamon.getUserData().toString());
        } else {
            listaIngredientes.remove(cb_jamon.getUserData().toString());
        }

        if (cb_queso.isSelected()) {
            listaIngredientes.add(cb_queso.getUserData().toString());
        } else {
            listaIngredientes.remove(cb_queso.getUserData().toString());
        }

        if (cb_tomate.isSelected()) {
            listaIngredientes.add(cb_tomate.getUserData().toString());
        } else {
            listaIngredientes.remove(cb_tomate.getUserData().toString());
        }

        if (cb_cebolla.isSelected()) {
            listaIngredientes.add(cb_cebolla.getUserData().toString());
        } else {
            listaIngredientes.remove(cb_cebolla.getUserData().toString());
        }

        if (cb_olivas.isSelected()) {
            listaIngredientes.add(cb_olivas.getUserData().toString());
        } else {
            listaIngredientes.remove(cb_olivas.getUserData().toString());
        }

        mostarIngredientes(listaIngredientes);
        pizza.setListaIngre(listaIngredientes);
        tf_totalPagar.setText(formateador.format(pizza.calcularPrecio()) + " €");

    }

    @FXML
    private void elegirTipoPizzaAction(ActionEvent event) {
        String tipoPizza = cb_tipoPizza.getValue();

        if (!tipoPizza.equalsIgnoreCase("Ninguna")) {
            lb_tipoPizza.setText(tipoPizza);

            switch (tipoPizza) {
                case "Basica":
                    iv_imagenPizza.setImage(imgBasica);
                    break;
                case "4 Quesos":
                    iv_imagenPizza.setImage(img4quesos);
                    break;
                case "Barbacoa":
                    iv_imagenPizza.setImage(imgBarbacoa);
                    break;
                case "Mexicana":
                    iv_imagenPizza.setImage(imgMexicana);
                    break;
                default:
                    throw new AssertionError();
            }

        } else {
            lb_tipoPizza.setText("");
            iv_imagenPizza.setImage(imgHorno);
        }

        centrarImagen(iv_imagenPizza);
        pizza.setTipo(tipoPizza);
        tf_totalPagar.setText(formateador.format(pizza.calcularPrecio()) + " €");

    }

    @FXML
    private void limpiarRegistrosAction(ActionEvent event) {

        lb_masa.setText("");
        lb_ingredientes.setText("");
        lb_tipoPizza.setText("");
        ta_ingredientes.clear();
        sb_tamaño.setValue(3);
        lb_tmFamTitulo.setStyle("-fx-font-weight:none");
        lb_tmMedTitulo.setStyle("-fx-font-weight:bold");
        lb_tmPeqTitulo.setStyle("-fx-font-weight:none");
        iv_tamañoPizza.setFitHeight(100);
        iv_tamañoPizza.setFitWidth(100);
        iv_tamañoPizza.setX(25);
        iv_tamañoPizza.setY(25);
        lb_tamaño.setText("");
        iv_imagenPizza.setImage(imgHorno);
        tf_totalPagar.setText("0 €");
        pizza.limpiarDatos();
        cb_jamon.setSelected(false);
        cb_queso.setSelected(false);
        cb_tomate.setSelected(false);
        cb_cebolla.setSelected(false);
        cb_olivas.setSelected(false);

        if (!cb_tipoPizza.getValue().equalsIgnoreCase("Ninguna")) {
            lb_tipoPizza.setText("");
            cb_tipoPizza.setValue("Ninguna");
        }

        if (cb_normal.isSelected() || cb_integral.isSelected()) {
            masa.getSelectedToggle().setSelected(false);
        }

    }

    @FXML
    private void mostarDatosSeleccionAction(MouseEvent event) {

        if (acrd_masa.isHover()) {
            acrd_masa.setExpanded(true);
            acrd_tipoPizza.setExpanded(false);
            acrd_ingreExtra.setExpanded(false);
            acrd_tamaño.setExpanded(false);
        }

        if (acrd_tipoPizza.isHover()) {
            acrd_tipoPizza.setExpanded(true);
            acrd_masa.setExpanded(false);
            acrd_ingreExtra.setExpanded(false);
            acrd_tamaño.setExpanded(false);
        }

        if (acrd_ingreExtra.isHover()) {
            acrd_ingreExtra.setExpanded(true);
            acrd_masa.setExpanded(false);
            acrd_tipoPizza.setExpanded(false);
            acrd_tamaño.setExpanded(false);
        }

        if (acrd_tamaño.isHover()) {
            acrd_tamaño.setExpanded(true);
            acrd_ingreExtra.setExpanded(false);
            acrd_masa.setExpanded(false);
            acrd_tipoPizza.setExpanded(false);
        }

    }

    @FXML
    private void tamañoPizzaAction(MouseEvent event) {
        String tipoTamaño;
        Double tamañoPizza;
        tipoTamaño = "Mediana";
        tamañoPizza = sb_tamaño.getValue();

        lb_tmPeqTitulo.setStyle("-fx-font-weight:none");
        lb_tmMedTitulo.setStyle("-fx-font-weight:none");
        lb_tmFamTitulo.setStyle("-fx-font-weight:none");

        if (tamañoPizza == 0) {
            tipoTamaño = "Pequeña";
            iv_tamañoPizza.setFitHeight(50);
            iv_tamañoPizza.setFitWidth(50);
            iv_tamañoPizza.setX(50);
            iv_tamañoPizza.setY(50);
            lb_tmPeqTitulo.setStyle("-fx-font-weight:bold");

        }

        if (tamañoPizza == 3) {
            tipoTamaño = "Mediana";
            iv_tamañoPizza.setFitHeight(100);
            iv_tamañoPizza.setFitWidth(100);
            iv_tamañoPizza.setX(25);
            iv_tamañoPizza.setY(25);
            lb_tmMedTitulo.setStyle("-fx-font-weight:bold");

        }

        if (tamañoPizza == 6) {
            tipoTamaño = "Familiar";
            iv_tamañoPizza.setFitHeight(150);
            iv_tamañoPizza.setFitWidth(150);
            iv_tamañoPizza.setX(0);
            iv_tamañoPizza.setY(0);
            lb_tmFamTitulo.setStyle("-fx-font-weight:bold");

        }

        lb_tamaño.setText(tipoTamaño);
        pizza.setTamaño(tipoTamaño);
        tf_totalPagar.setText(formateador.format(pizza.calcularPrecio()) + " €");

    }

    @FXML
    private void cerrarDatosSeleccionAction(MouseEvent event) {
        Object evento = event.getSource();
        if (evento == anchorPane || evento == pn_titulo || evento == pn_fondoMenuSeleccion) {
            acrd_tamaño.setExpanded(false);
            acrd_ingreExtra.setExpanded(false);
            acrd_masa.setExpanded(false);
            acrd_tipoPizza.setExpanded(false);
        }
    }

    private void cargarHovers() {

        Tooltip tt_normal, tt_integral, tt_jamon, tt_queso, tt_tomate, tt_cebolla,
                tt_olivas, tt_verDetalle, tt_panelIngredientes, tt_cargarPizza
                , tt_web;

        tt_normal = new Tooltip(String.valueOf(precio.precioMasa("Normal")) + " €");
        instalarHover(cb_normal, tt_normal);
        tt_integral = new Tooltip(String.valueOf(precio.precioMasa("Integral")) + " €");
        instalarHover(cb_integral, tt_integral);
        tt_jamon = new Tooltip(String.valueOf(precio.precioDeUnIngrediente("Jamon")) + " €");
        instalarHover(cb_jamon, tt_jamon);
        tt_queso = new Tooltip(String.valueOf(precio.precioDeUnIngrediente("Queso")) + " €");
        instalarHover(cb_queso, tt_queso);
        tt_tomate = new Tooltip(String.valueOf(precio.precioDeUnIngrediente("Tomate")) + " €");
        instalarHover(cb_tomate, tt_tomate);
        tt_cebolla = new Tooltip(String.valueOf(precio.precioDeUnIngrediente("Cebolla")) + " €");
        instalarHover(cb_cebolla, tt_cebolla);
        tt_olivas = new Tooltip(String.valueOf(precio.precioDeUnIngrediente("Olivas")) + " €");
        instalarHover(cb_olivas, tt_olivas);
        tt_verDetalle = new Tooltip("Ver Detalle");
        instalarHover(bt_mostrarDetalle, tt_verDetalle);
        tt_panelIngredientes = new Tooltip("ctrl mientras pasas por \n"
                + "encima de los ingredientes");
        instalarHover(iv_interrogante, tt_panelIngredientes);
        tt_cargarPizza = new Tooltip("Click me");
        instalarHover(iv_cargaPizza, tt_cargarPizza);
        tt_web = new Tooltip("Web");
        instalarHover(iv_ImagenEmpresa, tt_web);

    }

    public void instalarHover(Node nodo, Tooltip etiqueta) {
        /*RAQUEL, tengo la siguiente duda: Como para todos los tooltip quiero el
        mismo estilo, ¿Es mejor crear este metodo o escribir en el de arriba lo mismo
        repetidas veces?.
         */
        etiqueta.setStyle("-fx-background-color: lightgreen;-fx-text-fill: black;");
        Tooltip.install(nodo, etiqueta);
    }

    private void mostarIngredientes(List listaIngre) {

        ta_ingredientes.clear();
        ta_ingredientes.setText("\n");

        for (Object ingrediente : listaIngre) {
            ta_ingredientes.appendText(ingrediente.toString() + "\n");
        }
    }

    private void cargarImagenes() {

        img4quesos = new Image("file:images/pizza_cuatro_quesos.jpg", 940, 550, false, false);
        imgBasica = new Image("file:images/Pizza_basica.jpg", 940, 550, false, false);
        imgBarbacoa = new Image("file:images/pizza_barbacoa.jpg", 940, 550, false, false);
        imgMexicana = new Image("file:images/Pizza_Mexicana.jpg", 940, 550, false, false);
        imgHorno = new Image("file:images/horno.png", 940, 550, false, false);
        imgPizzaTamaño = new Image("file:images/tamaño.png");
        imgEmpresa = new Image("file:images/Captura2.png");
        imgToldo = new Image("file:images/toldo_completo.png");
        imgCocinero = new Image("file:images/cocinero.png", 1000, 1000, false, false);
        imgInterrogante = new Image("file:images/orange-help-256.png", 60, 60, false, false);
        imgPizza1 = new Image("file:images/1.png", 250, 250, false, false);
        imgPizza2 = new Image("file:images/2.png", 250, 250, false, false);
        imgPizza3 = new Image("file:images/3.png", 250, 250, false, false);
        imgPizza4 = new Image("file:images/4.png", 250, 250, false, false);
        imgPizza5 = new Image("file:images/5.png", 250, 250, false, false);
        imgPizza6 = new Image("file:images/6.png", 250, 250, false, false);
        imgPizza7 = new Image("file:images/7.png", 250, 250, false, false);
        imgPizza8 = new Image("file:images/8.png", 250, 250, false, false);
        imgPizza9 = new Image("file:images/9.png", 250, 250, false, false);
        iv_cargaPizza.setImage(imgPizza9);
        iv_ImagenEmpresa.setImage(imgEmpresa);
        iv_imagenPizza.setImage(imgHorno);
        centrarImagen(iv_imagenPizza);
        iv_toldo.setImage(imgToldo);
        iv_cocinero.setImage(imgCocinero);
        iv_tamañoPizza.setImage(imgPizzaTamaño);
        iv_tamañoPizza.setFitHeight(100);
        iv_tamañoPizza.setFitWidth(100);
        iv_tamañoPizza.setX(25);
        iv_tamañoPizza.setY(25);
    }

    private void centrarImagen(ImageView img) {

        Image imagen = img.getImage();

        double ancho = 0, alto = 0, radioX, radioY, reducirCoeficiente = 0;

        if (img != null) {

            radioX = img.getFitWidth() / imagen.getWidth();
            radioY = img.getFitHeight() / imagen.getHeight();

            if (radioX >= radioY) {
                reducirCoeficiente = radioY;
            } else {
                reducirCoeficiente = radioX;
            }

            ancho = imagen.getWidth() * reducirCoeficiente;
            alto = imagen.getHeight() * reducirCoeficiente;

            img.setX((img.getFitWidth() - ancho) / 2);
            img.setY((img.getFitHeight() - alto) / 2);

        }
    }

    @FXML
    private void verDetallePedidoAction(ActionEvent event) {
        /* Aquí tengo el problema que el otro día comentamos en clase.
        Cuando le he añadido botones¿Cómo hago que al clicar la X salga 
        del programa?*/
        ButtonType marcado, imprimir, salir;
        Alert alerta;
        Optional<ButtonType> resultado;

        alerta = new Alert(AlertType.INFORMATION);
        alerta.setTitle("TICKET");
        alerta.setHeaderText("Datos del pedido");
        alerta.setContentText(pizza.detallePedido());
        darleEstiloAlPanel(alerta);
        imprimir = new ButtonType("Imprimir");
        salir = new ButtonType("Salir");
        alerta.getButtonTypes().setAll(imprimir, salir);
        resultado = alerta.showAndWait();
        marcado = resultado.get();

        if (marcado == imprimir) {
            if (imprimirPedido() == true) {
            }
        }
    }

    public boolean imprimirPedido() {
        boolean creado = false;
        List<String> faltaSeleccion = new ArrayList<>();

        if (pizza.getMasa() == null) {
            faltaSeleccion.add("Masa");
        }
        if (pizza.getTipo() == null || pizza.getTipo().equalsIgnoreCase("Ninguna")) {
            faltaSeleccion.add("Tipo de pizza");
        }
        if (pizza.getTamaño() == null) {
            faltaSeleccion.add("Tamaño de la pizza");
        }
        if (faltaSeleccion.isEmpty()) {
            crearTicket();
            creado = true;
        } else {
            Alert subAlerta = new Alert(AlertType.INFORMATION);
            subAlerta.setTitle("TICKET");
            subAlerta.setHeaderText("Falta por seleccionar");
            subAlerta.setContentText(pintarDatosQueFaltan(faltaSeleccion));
            darleEstiloAlPanel(subAlerta);
            subAlerta.showAndWait();
        }

        return creado;
    }

    public String pintarDatosQueFaltan(List datosFalta) {
        String respuesta = "";

        switch (datosFalta.size()) {
            case 1:
                respuesta = datosFalta.get(0).toString();

                break;
            case 2:
                respuesta = datosFalta.get(0).toString() + " y " + datosFalta.get(1).toString();

                break;
            case 3:
                respuesta = datosFalta.get(0).toString() + " , " + datosFalta.get(1).toString() + " y " + datosFalta.get(2).toString();
                ;

                break;
            default:
                break;
        }

        return respuesta;
    }

    public void crearTicket() {

        DirectoryChooser selectorDirectorio = new DirectoryChooser();
        selectorDirectorio.setTitle("Selector de directorio");
        directorio = selectorDirectorio.showDialog(new Stage());

        if (directorio != null) {
            crearArchivoTicket();
        } else {
            Alert error = new Alert(AlertType.ERROR);
            error.setTitle("Error de Impresión");
            error.setHeaderText("Directorio no seleccionado");
            error.setContentText("No ha seleccionado ningún directorio donde"
                    + " imprimir el ticket");
            darleEstiloAlPanel(error);
            error.showAndWait();
        }
    }

    private void crearArchivoTicket() {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-d-MM (h-m-s a)");
        String nombreTicket = "ticket " + LocalDateTime.now().format(formato) + ".txt";
        Path archivo = Paths.get(this.directorio.toString(), "\\", nombreTicket);

        try (BufferedWriter out = Files.newBufferedWriter(archivo, StandardOpenOption.CREATE_NEW)) {
            StringTokenizer st = new StringTokenizer(pizza.detallePedido(), "\n");
            while (st.hasMoreTokens()) {
                out.write(st.nextToken());
                out.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error al abrir el directorio");
        }
    }

    public void darleEstiloAlPanel(Alert panel) {

        DialogPane dialogPane = panel.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("estilosPizzeria.css").toExternalForm());
        dialogPane.getStyleClass().add("dialog-pane");
        Stage alertaStage = (Stage) panel.getDialogPane().getScene().getWindow();
        alertaStage.getIcons().add(new Image("file:images/icono_pizza.png"));
    }

    @FXML
    private void seleccionMultipleAction(MouseEvent event) {

        /*        RECORDATORIO DEL PROBLEMA Y LA SOLUCIÓN HALLADA
        En este metodo el problema que tenia era que, una vez seleccionados los
        ingredientes que quería sin soltar el cntrl puediese deseleccionar los
        que quisiese, así que se me ha ocurrido hacerlo de la siguiente manera:
        
        En este metodo cada vez que entramos nos guardamos a donde apunta el 
        evento(raton), entonces si es la primera vez que entra en un ingrediente lo 
        cambia a donde apunta y me lo selecciona. Como mientras nos movemos por
        el ingrediente no queremos que nos analice mas y nos lo vaya alternando 
        entre deseleccionado y seleccionado para acabar en un estado aleatorio
        y no poder cambiarlo a nuestro gusto, utilizamos la ultima posicion de donde
        apuntava el evento, y si es igual al ultimo que cambiamos no lo tocamos,
        asi, cada vez que cambiemos de ingrediente sabrá que si volvemos a entrar
        en uno es para deseleccionarlo o volverlo a seleccionar.        
         */
        if (obejtoViejo == null) {
            obejtoViejo = event.getSource();
        }

        if (event.isControlDown()) {

            if (obejtoViejo != event.getSource() && event.getSource() == cb_cebolla) {
                if (cb_cebolla.isHover() && !cb_cebolla.isSelected()) {
                    cb_cebolla.setSelected(true);
                } else {
                    cb_cebolla.setSelected(false);
                }
            }

            if (obejtoViejo != event.getSource() && event.getSource() == cb_jamon) {
                if (cb_jamon.isHover() && !cb_jamon.isSelected()) {
                    cb_jamon.setSelected(true);
                } else {
                    cb_jamon.setSelected(false);
                }
            }

            if (obejtoViejo != event.getSource() && event.getSource() == cb_olivas) {
                if (cb_olivas.isHover() && !cb_olivas.isSelected()) {
                    cb_olivas.setSelected(true);
                } else {
                    cb_olivas.setSelected(false);
                }
            }

            if (obejtoViejo != event.getSource() && event.getSource() == cb_queso) {
                if (cb_queso.isHover() && !cb_queso.isSelected()) {
                    cb_queso.setSelected(true);
                } else {
                    cb_queso.setSelected(false);
                }
            }

            if (obejtoViejo != event.getSource() && event.getSource() == cb_tomate) {
                if (cb_tomate.isHover() && !cb_tomate.isSelected()) {
                    cb_tomate.setSelected(true);
                } else {
                    cb_tomate.setSelected(false);
                }
            }
            subEleccionIngrediente();
        }

        if (obejtoViejo != event.getSource()) {
            obejtoViejo = event.getSource();
        }

    }

    public void setPrecio(Precios precio) {
        this.precio = precio;
    }

    @FXML
    private void pizzaCargaAction() {
        /*La idea de este metodo aún esta por terminar. Mientras tanto lo dejo
        interactivo con los clics*/
        Image pizza = null;
        contCargaPizza++;

        if (contCargaPizza == 0) {
            pizza = null;
        }
        if (contCargaPizza == 1) {
            pizza = imgPizza1;
        }
        if (contCargaPizza == 2) {
            pizza = imgPizza2;
        }
        if (contCargaPizza == 3) {
            pizza = imgPizza3;
        }
        if (contCargaPizza == 4) {
            pizza = imgPizza4;
        }
        if (contCargaPizza == 5) {
            pizza = imgPizza5;
        }
        if (contCargaPizza == 6) {
            pizza = imgPizza6;
        }
        if (contCargaPizza == 7) {
            pizza = imgPizza7;
        }
        if (contCargaPizza == 8) {
            pizza = imgPizza8;
        }
        if (contCargaPizza == 9) {
            pizza = imgPizza9;
            contCargaPizza = 0;
        }
        iv_cargaPizza.setImage(pizza);

    }

    @FXML
    private void verWebAction(MouseEvent event) {
        wv_web = new WebView();
        WebEngine webEngine = wv_web.getEngine();
        webEngine.load("https://dpizzablog.wordpress.com/");
        Dialog web = new Dialog();
        web.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        web.getDialogPane().setContent(wv_web);
        web.show();
    }

}
