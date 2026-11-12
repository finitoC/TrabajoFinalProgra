import java.time.LocalDate;

public enum EstadoMembresia {

    ACTIVO,
    VENCIDO,
    SIN_PLAN;

        /** Calcula el estado a partir del último vencimiento (null si nunca pagó). */
        public static EstadoMembresia calcular(LocalDate ultimoVencimiento) {

            LocalDate hoy  = LocalDate.now();

            if (ultimoVencimiento == null) return SIN_PLAN;
            if (!ultimoVencimiento.isBefore(hoy)) return ACTIVO;

            return VENCIDO;
        }
    }


