
/**
 * CLASE GENÉRICA: agrupa dos valores de tipos distintos.
 * Se usa, por ejemplo, para devolver "socio + fecha de vencimiento" en los reportes.
 *
 * @param <A> tipo del primer valor
 * @param <B> tipo del segundo valor
 */
public class Par<A, B> {
    private final A primero;
    private final B segundo;

    public Par(A primero, B segundo) {
        this.primero = primero;
        this.segundo = segundo;
    }

    public A getPrimero() { return primero; }
    public B getSegundo() { return segundo; }
}
