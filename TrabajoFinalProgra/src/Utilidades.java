

import java.time.DayOfWeek;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

///Métodos genéricos reutilizables
public final class Utilidades {

    private Utilidades() { }

    public static <T extends ClaseDescribible> void imprimirLista(
            String titulo, ArrayList<T> elementos) {

        System.out.println("\n--- " + titulo
                + " (" + elementos.size() + ") ---");

        if (elementos.isEmpty()) {
            System.out.println("  (sin resultados)");
            return;
        }

        for (T e : elementos) {
            System.out.println("  " + e.resumen());
        }
    }

    /** Traduce un día de la semana a español. */
    public static String diaEnEspanol(DayOfWeek d) {
        return switch (d) {
            case MONDAY -> "Lunes";
            case TUESDAY -> "Martes";
            case WEDNESDAY -> "Miércoles";
            case THURSDAY -> "Jueves";
            case FRIDAY -> "Viernes";
            case SATURDAY -> "Sábado";
            case SUNDAY -> "Domingo";
        };
    }
}
