import java.time.LocalDate;

public class Membresia {

    private int id;
    private final String nombre;
    private final Actividad actividad;
    private final int duracionDias;
    private final double precio;
    private static int numerosID = 1;


    public Membresia(String nombre, Actividad actividad, int duracionDias, double precio){
        this.id = numerosID++;
        this.nombre = Validador.texto(nombre, "Nombre del plan", 3, 40);
        this.actividad = actividad;
        this.duracionDias = Validador.entero(duracionDias, "Duración (días)", 1, 730);
        this.precio = Validador.decimal(precio, "Precio", 1, 10_000_000);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        if (id <= 0) throw new DatosInvalidosException("El ID del plan debe ser positivo.");
        this.id = id;
    }
    public String getNombre() {
        return nombre;
    }

    public Actividad getActividad() {
        return actividad;
    }

    public int getDuracionDias() {
        return duracionDias;
    }

    public double getPrecio() {
        return precio;
    }

    public LocalDate calcularVencimiento(LocalDate desde) {
        if (desde == null) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser nula.");
        }
        return desde.plusDays(duracionDias);  ///plusdays le suma a la fecha la cantidad de dias que aparezca en las ()
    }

}

