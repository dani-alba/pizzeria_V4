package Modelo;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Pizza {

    private String masa, tipo, tamaño;
    private List<String> listaIngre;
    private Double precioMasa = 0d, precioTipo = 0d, totalIngre = 0d,
            increTamaño = 1d;
    private Precios precios; // recoger el precio con el que trabaja
    private DecimalFormat formateador = new DecimalFormat("#.##");

    public Pizza(Precios precio) {
        this.precios = precio;
        listaIngre = new ArrayList<>();
    }

    public Double calcularPrecio() {

        return (precioMasa + precioTipo + totalIngre) * increTamaño;
    }

    public void limpiarDatos() {
        masa = null;
        precioMasa = 0d;
        tipo = "Ninguna";
        precioTipo = 0d;
        totalIngre = 0d;
        tamaño = null;
        increTamaño = 1d;
        listaIngre.clear();
    }

    private String detalleAumentoTamaño() {
        String mensaje;        
        /* solucionar porque cuando es 1.50, solo sale en el ticket 5% induciendo a equivocación*/
        if (this.increTamaño == 1.0) {
            mensaje = "Sin incremento";
        } else{
            mensaje = "Incremento del " + this.increTamaño.toString().substring(2) + "%";
        }

        return mensaje;
    }

    private String pintarListaIngre() {
        /*Esta en private porque este no quiero que se pueda usar salvo para 
        completar la lista del pedido */

        String mostrar = "";
        int cont = 1;

        for (Object ingrediente : listaIngre) {
            mostrar = mostrar + String.format("%7s %-3s %-8s %-44s %-5s", cont, ".- ",
                    ingrediente.toString(), ".......................................",
                    precios.precioDeUnIngrediente(ingrediente.toString())) + " €\n";
            cont++;
        }

        return "INGREDIENTES:   " + "\n" + mostrar
                + String.format("%13s %-44s %-5s", "      Subtotal :",
                        "...........................................    ", formateador.format(totalIngre) )
                + "  € \n";
    }

    public String detallePedido() {
        String masaFormateada = "", tipoFormateado = "", listaFormateada = "",
                tamañoFormateado = "", precioTotalFormateado = "";
        int cont = 0;

        if (this.masa != null) {
            masaFormateada = String.format("%-9s %-10s %-30s %-5s %-5s", "MASA:   ", masa, "........................................  ", precioMasa, "€ \n");
        } else {
            cont++;
        }

        if (this.tipo != null) {
            if (!this.tipo.equalsIgnoreCase("Ninguna")) {
                tipoFormateado = String.format("%-9s %-10s %-30s %-5s %-5s", "TIPO:   ", tipo, " ........................................ ", precioTipo, "€ \n");
            } else {
                cont++;
            }
        } else {
            cont++;
        }

        if (!this.listaIngre.isEmpty()) {
            listaFormateada = pintarListaIngre();
        } else {
            cont++;
        }

        if (this.tamaño != null) {
            tamañoFormateado = String.format("%9s %-10s %-30s %-5s %-5s", "TAMAÑO:   ", tamaño, "...........................  ", detalleAumentoTamaño(), " \n", "\n");
        } else {
            cont++;
        }

        if (cont != 4) {
            precioTotalFormateado = "------------------------------------------------------\n \n"
                    + "TOTAL:   .....................................................   "
                    + formateador.format(calcularPrecio()) + " €";
        } else {
            precioTotalFormateado = "Haga algún pedido";
        }

        return masaFormateada + tipoFormateado + listaFormateada + tamañoFormateado + precioTotalFormateado;

    }

    // * * * * * * * * GET AND SET * * * * * * * * * * 
    public String getMasa() {
        return masa;
    }

    public void setMasa(String masa) {
        this.masa = masa;
        this.precioMasa = precios.precioMasa(masa);
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
        this.precioTipo = precios.precioTipo(tipo);
    }

    public String getTamaño() {
        return tamaño;
    }

    public void setTamaño(String tamaño) {
        this.tamaño = tamaño;
        this.increTamaño = precios.precioTamaño(tamaño);
    }

    public List<String> getListaIngre() {
        return listaIngre;
    }

    public void setListaIngre(List<String> ingre) {
        this.listaIngre = ingre;
        this.totalIngre = precios.precioIngre(ingre);
    }

    public Double getPrecioMasa() {
        return precioMasa;
    }

    public Double getPrecioTipo() {
        return precioTipo;
    }

    public Double getTotalIngre() {
        return totalIngre;
    }

    public Double getIncreTamaño() {
        return increTamaño;
    }

}
