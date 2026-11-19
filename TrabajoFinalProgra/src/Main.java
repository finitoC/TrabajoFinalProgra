
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("\n===== FIGHTFIT =====");
            System.out.println("1. Gestion de socios");
            System.out.println("2. Gestion de entrenadores");
            System.out.println("3. Membresias");
            System.out.println("4. Pagos");
            System.out.println("5. Asistencias");
            System.out.println("6. Clases grupales");
            System.out.println("7. Boxeo");
            System.out.println("0. Salir");

            opcion = leerEntero(sc, "Ingrese una opcion: ");

            switch (opcion) {
                case 1:
                    menuSocios(sc);
                    break;
                case 2:
                    menuEntrenadores(sc);
                    break;
                case 3:
                    menuMembresias(sc);
                    break;
                case 4:
                    menuPagos(sc);
                    break;
                case 5:
                    menuAsistencias(sc);
                    break;
                case 6:
                    menuClases(sc);
                    break;
                case 7:
                    menuBoxeo(sc);
                    break;
                case 0:
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Opcion incorrecta");
            }

        } while (opcion != 0);

        sc.close();
    }

    private static void menuSocios(Scanner sc) {
        int opcion;

        do {
            System.out.println("\n===== GESTION DE SOCIOS =====");
            System.out.println("1. Registrar socio");
            System.out.println("2. Buscar socio por DNI");
            System.out.println("3. Listar socios");
            System.out.println("0. Volver");

            opcion = leerEntero(sc, "Ingrese una opcion: ");

            switch (opcion) {
                case 1:
                    try {
                        String nombre = leerTexto(sc, "Nombre: ");
                        String apellido = leerTexto(sc, "Apellido: ");
                        String dni = leerTexto(sc, "DNI: ");
                        int edad = leerEntero(sc, "Edad: ");
                        String telefono = leerTexto(sc, "Telefono: ");
                        String email = leerTexto(sc, "Email: ");

                        Socio socio = new Socio(0, nombre, apellido, dni,
                                edad, telefono, email, LocalDate.now());

                        Socio guardado = SistemBDD.guardarSocio(socio);

                        System.out.println("Socio registrado correctamente!");
                        System.out.println(guardado.resumen());

                    } catch (DatosInvalidosException | GimnasioException e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case 2:
                    try {
                        String dni = leerTexto(sc, "Ingrese el DNI del socio: ");

                        Socio socio = SistemBDD.buscarSocioPorDni(dni);

                        System.out.println(socio.resumen());

                    } catch (DatosInvalidosException | GimnasioException e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case 3:
                    try {
                        ArrayList<Socio> socios = SistemBDD.listarSocios();

                        if (socios.isEmpty()) {
                            System.out.println("No hay socios registrados");
                        } else {
                            for (Socio socio : socios) {
                                System.out.println(socio.resumen());
                            }
                        }

                    } catch (GimnasioException e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case 0:
                    System.out.println("Volviendo al menu principal...");
                    break;

                default:
                    System.out.println("Opcion incorrecta");
            }

        } while (opcion != 0);
    }

    // VALIDACION DE NUMEROS ENTEROS
    private static int leerEntero(Scanner sc, String mensaje) {
        System.out.print(mensaje);

        while (!sc.hasNextInt()) {
            System.out.println("Error: debe ingresar un numero entero.");
            sc.nextLine();
            System.out.print(mensaje);
        }

        int numero = sc.nextInt();
        sc.nextLine();

        return numero;
    }

    // VALIDACION DE TEXTOS VACIOS
    private static String leerTexto(Scanner sc, String mensaje) {
        String texto;

        do {
            System.out.print(mensaje);
            texto = sc.nextLine().trim();

            if (texto.isEmpty()) {
                System.out.println("Error: el campo no puede estar vacio.");
            }

        } while (texto.isEmpty());

        return texto;
    }

    private static void menuEntrenadores(Scanner sc) {
        int opcion;

        do {
            System.out.println("\n===== GESTION DE ENTRENADORES =====");
            System.out.println("1. Registrar entrenador");
            System.out.println("2. Buscar entrenador por DNI");
            System.out.println("3. Listar entrenadores");
            System.out.println("0. Volver");

            opcion = leerEntero(sc, "Ingrese una opcion: ");

            switch (opcion) {

                case 1:
                    try {
                        String nombre = leerTexto(sc, "Nombre: ");
                        String apellido = leerTexto(sc, "Apellido: ");
                        String dni = leerTexto(sc, "DNI: ");
                        int edad = leerEntero(sc, "Edad: ");
                        String telefono = leerTexto(sc, "Telefono: ");
                        String email = leerTexto(sc, "Email: ");
                        int experiencia = leerEntero(sc, "Años de experiencia: ");

                        HashSet<Actividad> actividades = new HashSet<>();

                        int opcionActividad;

                        do {
                            System.out.println("Seleccione las actividades:");
                            System.out.println("1. Gimnasio");
                            System.out.println("2. Boxeo");
                            System.out.println("3. Ambas");

                            opcionActividad = leerEntero(sc, "Opcion: ");

                            if (opcionActividad < 1 || opcionActividad > 3) {
                                System.out.println("Opcion incorrecta");
                            }

                        } while (opcionActividad < 1 || opcionActividad > 3);

                        if (opcionActividad == 1 || opcionActividad == 3) {
                            actividades.add(Actividad.GIMNASIO);
                        }

                        if (opcionActividad == 2 || opcionActividad == 3) {
                            actividades.add(Actividad.BOXEO);
                        }

                        Entrenador entrenador = new Entrenador(
                                0, nombre, apellido, dni, edad,
                                telefono, email, experiencia, actividades
                        );

                        Entrenador guardado = SistemBDD.guardarEntrenador(entrenador);

                        System.out.println("Entrenador registrado correctamente!");
                        System.out.println(guardado.resumen());

                    } catch (DatosInvalidosException | GimnasioException e) {
                        System.out.println(e.getMessage());
                    }
                    break;


                case 2:
                    try {
                        String dni = leerTexto(sc, "Ingrese el DNI del entrenador: ");

                        Entrenador entrenador = SistemBDD.buscarEntrenadorPorDni(dni);

                        System.out.println(entrenador.resumen());
                        System.out.println("Experiencia: " +
                                entrenador.getAniosExperiencia() + " años");
                        System.out.println("Actividades: " +
                                entrenador.getActividades());

                    } catch (DatosInvalidosException | GimnasioException e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case 3:
                    try {
                        ArrayList<Entrenador> entrenadores =
                                SistemBDD.listarEntrenadores();

                        if (entrenadores.isEmpty()) {
                            System.out.println("No hay entrenadores registrados");
                        } else {
                            for (Entrenador entrenador : entrenadores) {
                                System.out.println(entrenador.resumen());
                                System.out.println("Experiencia: "
                                        + entrenador.getAniosExperiencia() + " años");
                                System.out.println("Actividades: "
                                        + entrenador.getActividades());
                            }
                        }

                    } catch (GimnasioException e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case 0:
                    System.out.println("Volviendo al menu principal...");
                    break;

                default:
                    System.out.println("Opcion incorrecta");
            }

        } while (opcion != 0);
    }
    private static double leerDecimal(Scanner sc, String mensaje) {
        System.out.print(mensaje);

        while (!sc.hasNextDouble()) {
            System.out.println("Error: debe ingresar un numero.");
            sc.nextLine();
            System.out.print(mensaje);
        }

        double numero = sc.nextDouble();
        sc.nextLine();

        return numero;
    }
    private static void menuMembresias(Scanner sc) {
        int opcion;

        do {
            System.out.println("\n===== GESTION DE MEMBRESIAS =====");
            System.out.println("1. Registrar membresia");
            System.out.println("2. Listar membresias");
            System.out.println("0. Volver");

            opcion = leerEntero(sc, "Ingrese una opcion: ");

            switch (opcion) {

                case 1:
                    try {
                        String nombre = leerTexto(sc, "Nombre del plan: ");

                        int opcionActividad;

                        do {
                            System.out.println("Seleccione la actividad:");
                            System.out.println("1. Gimnasio");
                            System.out.println("2. Boxeo");

                            opcionActividad = leerEntero(sc, "Opcion: ");

                            if (opcionActividad < 1 || opcionActividad > 2) {
                                System.out.println("Opcion incorrecta");
                            }

                        } while (opcionActividad < 1 || opcionActividad > 2);

                        Actividad actividad;

                        if (opcionActividad == 1) {
                            actividad = Actividad.GIMNASIO;
                        } else {
                            actividad = Actividad.BOXEO;
                        }

                        int duracion = leerEntero(sc, "Duracion en dias: ");
                        double precio = leerDecimal(sc, "Precio: ");

                        Membresia membresia = new Membresia(
                                nombre, actividad, duracion, precio
                        );

                        SistemBDD.guardarMembresia(membresia);

                        System.out.println("Membresia registrada correctamente!");
                        System.out.println("Nombre: " + membresia.getNombre());
                        System.out.println("Actividad: " + membresia.getActividad());
                        System.out.println("Duracion: " + membresia.getDuracionDias() + " dias");
                        System.out.println("Precio: $" + membresia.getPrecio());

                    } catch (DatosInvalidosException | GimnasioException e) {
                        System.out.println(e.getMessage());
                    }
                    break;


                case 2:
                    try {
                        ArrayList<Membresia> membresias =
                                SistemBDD.listarMembresias();

                        if (membresias.isEmpty()) {
                            System.out.println("No hay membresias registradas");
                        } else {
                            for (Membresia membresia : membresias) {
                                System.out.println("Nombre: " + membresia.getNombre());
                                System.out.println("Actividad: " + membresia.getActividad());
                                System.out.println("Duracion: " + membresia.getDuracionDias() + " dias");
                                System.out.println("Precio: $" + membresia.getPrecio());
                                System.out.println("--------------------------");
                            }
                        }

                    } catch (GimnasioException e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case 0:
                    System.out.println("Volviendo al menu principal...");
                    break;

                default:
                    System.out.println("Opcion incorrecta");
            }

        } while (opcion != 0);
    }
    private static void menuPagos(Scanner sc) {
        int opcion;

        do {
            System.out.println("\n===== GESTION DE PAGOS =====");
            System.out.println("1. Registrar pago");
            System.out.println("2. Listar pagos de un socio");
            System.out.println("3. Consultar estado de membresia");
            System.out.println("0. Volver");

            opcion = leerEntero(sc, "Ingrese una opcion: ");

            switch (opcion) {

                case 1:
                    try {
                        String dni = leerTexto(sc, "DNI del socio: ");

                        Socio socio = SistemBDD.buscarSocioPorDni(dni);

                        System.out.println("Socio encontrado: "
                                + socio.getNombre() + " " + socio.getApellido());

                        ArrayList<Membresia> membresias = SistemBDD.listarMembresias();

                        if (membresias.isEmpty()) {
                            System.out.println("No hay membresias disponibles");
                        } else {

                            System.out.println("\n===== MEMBRESIAS DISPONIBLES =====");

                            for (int i = 0; i < membresias.size(); i++) {
                                Membresia m = membresias.get(i);

                                System.out.println((i + 1) + ". " + m.getNombre()
                                        + " | " + m.getActividad()
                                        + " | $" + m.getPrecio());
                            }

                            int opcionPlan;

                            do {
                                opcionPlan = leerEntero(sc, "Seleccione una membresia: ");

                                if (opcionPlan < 1 || opcionPlan > membresias.size()) {
                                    System.out.println("Opcion incorrecta");
                                }

                            } while (opcionPlan < 1 || opcionPlan > membresias.size());

                            Membresia plan = membresias.get(opcionPlan - 1);

                            LocalDate fechaPago = LocalDate.now();

                            LocalDate inicio = fechaPago;
                            LocalDate ultimoVencimiento = SistemBDD.ultimoVencimiento(
                                    socio.getId(), plan.getActividad());

                            if (ultimoVencimiento != null &&
                                    ultimoVencimiento.isAfter(inicio)) {
                                inicio = ultimoVencimiento;
                            }

                            LocalDate vencimiento = plan.calcularVencimiento(inicio);

                            Pago pago = new Pago(
                                    0,
                                    socio.getId(),
                                    plan.getId(),
                                    plan.getActividad(),
                                    plan.getPrecio(),
                                    fechaPago,
                                    vencimiento
                            );

                            SistemBDD.guardarPago(pago);

                            System.out.println("Pago registrado correctamente!");
                            System.out.println("Socio: " + socio.getNombre());
                            System.out.println("Plan: " + plan.getNombre());
                            System.out.println("Monto: $" + pago.getMonto());
                            System.out.println("Vencimiento: " + pago.getFechaVencimiento());
                        }

                    } catch (DatosInvalidosException | GimnasioException e) {
                        System.out.println(e.getMessage());
                    }
                    break;


                case 2:
                    try {
                        String dni = leerTexto(sc, "DNI del socio: ");

                        Socio socio = SistemBDD.buscarSocioPorDni(dni);

                        ArrayList<Pago> pagos =
                                SistemBDD.listarPagosDeSocio(socio.getId());

                        if (pagos.isEmpty()) {
                            System.out.println("El socio no tiene pagos registrados");
                        } else {
                            System.out.println("Pagos de " + socio.getNombre()
                                    + " " + socio.getApellido());

                            for (Pago pago : pagos) {
                                System.out.println("Actividad: " + pago.getActividad());
                                System.out.println("Monto: $" + pago.getMonto());
                                System.out.println("Fecha de pago: " + pago.getFechaPago());
                                System.out.println("Vencimiento: " + pago.getFechaVencimiento());
                                System.out.println("----------------------");
                            }
                        }

                    } catch (DatosInvalidosException | GimnasioException e) {
                        System.out.println(e.getMessage());
                    }
                    break;


                case 3:
                    try {
                        String dni = leerTexto(sc, "DNI del socio: ");

                        Socio socio = SistemBDD.buscarSocioPorDni(dni);

                        System.out.println("\nEstado de membresias de "
                                + socio.getNombre() + " " + socio.getApellido());

                        for (Actividad actividad : Actividad.values()) {

                            LocalDate vencimiento = SistemBDD.ultimoVencimiento(
                                    socio.getId(), actividad);

                            EstadoMembresia estado =
                                    EstadoMembresia.calcular(vencimiento);

                            System.out.println("\nActividad: " + actividad);
                            System.out.println("Estado: " + estado);

                            if (vencimiento != null) {
                                System.out.println("Vencimiento: " + vencimiento);
                            }
                        }

                    } catch (DatosInvalidosException | GimnasioException e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case 0:
                    System.out.println("Volviendo al menu principal...");
                    break;

                default:
                    System.out.println("Opcion incorrecta");
            }

        } while (opcion != 0);
    }

    private static void menuAsistencias(Scanner sc) {
        int opcion;

        do {
            System.out.println("\n===== GESTION DE ASISTENCIAS =====");
            System.out.println("1. Registrar asistencia");
            System.out.println("2. Listar asistencias de un socio");
            System.out.println("0. Volver");

            opcion = leerEntero(sc, "Ingrese una opcion: ");

            switch (opcion) {
                case 1:
                    try {
                        String dni = leerTexto(sc, "DNI del socio: ");
                        Socio socio = SistemBDD.buscarSocioPorDni(dni);

                        System.out.println("Socio: " + socio.getNombre()
                                + " " + socio.getApellido());

                        int opcionActividad;

                        do {
                            System.out.println("1. Gimnasio");
                            System.out.println("2. Boxeo");

                            opcionActividad = leerEntero(sc, "Actividad: ");

                            if (opcionActividad < 1 || opcionActividad > 2) {
                                System.out.println("Opcion incorrecta");
                            }

                        } while (opcionActividad < 1 || opcionActividad > 2);

                        Actividad actividad;

                        if (opcionActividad == 1) {
                            actividad = Actividad.GIMNASIO;
                        } else {
                            actividad = Actividad.BOXEO;
                        }

                        Asistencia asistencia = new Asistencia(
                                0,
                                socio.getId(),
                                actividad,
                                LocalDateTime.now()
                        );

                        SistemBDD.guardarAsistencia(asistencia);

                        System.out.println("Asistencia registrada correctamente!");
                        System.out.println("Actividad: " + actividad);
                        System.out.println("Fecha y hora: " + asistencia.getFechaHora());

                    } catch (DatosInvalidosException | GimnasioException e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case 2:
                    try {
                        String dni = leerTexto(sc, "DNI del socio: ");
                        Socio socio = SistemBDD.buscarSocioPorDni(dni);

                        ArrayList<Asistencia> asistencias =
                                SistemBDD.listarAsistenciasDeSocio(socio.getId());

                        if (asistencias.isEmpty()) {
                            System.out.println("El socio no tiene asistencias registradas");
                        } else {
                            for (Asistencia asistencia : asistencias) {
                                System.out.println("Actividad: " + asistencia.getActividad());
                                System.out.println("Fecha y hora: " + asistencia.getFechaHora());
                                System.out.println("----------------------");
                            }
                        }

                    } catch (DatosInvalidosException | GimnasioException e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case 0:
                    System.out.println("Volviendo al menu principal...");
                    break;

                default:
                    System.out.println("Opcion incorrecta");
            }

        } while (opcion != 0);
    }

    private static void menuClases(Scanner sc) {
        int opcion;

        do {
            System.out.println("\n===== CLASES GRUPALES =====");
            System.out.println("1. Registrar clase");
            System.out.println("2. Listar clases");
            System.out.println("3. Inscribir socio en una clase");
            System.out.println("4. Listar socios inscriptos");
            System.out.println("0. Volver");

            opcion = leerEntero(sc, "Ingrese una opcion: ");

            switch (opcion) {

                case 1:
                    try {
                        String nombre = leerTexto(sc, "Nombre de la clase: ");

                        System.out.println("1. Gimnasio");
                        System.out.println("2. Boxeo");

                        int opcionActividad;
                        do {
                            opcionActividad = leerEntero(sc, "Actividad: ");
                        } while (opcionActividad < 1 || opcionActividad > 2);

                        Actividad actividad;

                        if (opcionActividad == 1) {
                            actividad = Actividad.GIMNASIO;
                        } else {
                            actividad = Actividad.BOXEO;
                        }

                        ArrayList<Entrenador> disponibles = new ArrayList<>();

                        for (Entrenador entrenador : SistemBDD.listarEntrenadores()) {
                            if (entrenador.entrena(actividad)) {
                                disponibles.add(entrenador);
                            }
                        }

                        if (disponibles.isEmpty()) {
                            System.out.println("No hay entrenadores disponibles para esta actividad");
                        } else {
                            System.out.println("\n===== ENTRENADORES =====");

                            for (int i = 0; i < disponibles.size(); i++) {
                                Entrenador e = disponibles.get(i);

                                System.out.println((i + 1) + ". "
                                        + e.getNombre() + " " + e.getApellido());
                            }

                            int opcionEntrenador;
                            do {
                                opcionEntrenador = leerEntero(sc, "Seleccione entrenador: ");
                            } while (opcionEntrenador < 1
                                    || opcionEntrenador > disponibles.size());

                            Entrenador entrenador = disponibles.get(opcionEntrenador - 1);

                            System.out.println("\n===== DIAS DISPONIBLES =====");

                            DiaSemana[] dias = DiaSemana.values();

                            for (int i = 0; i < dias.length; i++) {
                                System.out.println((i + 1) + ". " + dias[i].getNombre());
                            }

                            int opcionDia;
                            do {
                                opcionDia = leerEntero(sc, "Seleccione dia: ");
                            } while (opcionDia < 1 || opcionDia > dias.length);

                            DiaSemana dia = dias[opcionDia - 1];

                            LocalTime hora = null;

                            while (hora == null) {
                                try {
                                    String textoHora = leerTexto(sc, "Hora (HH:mm): ");
                                    hora = LocalTime.parse(textoHora);
                                } catch (DateTimeParseException e) {
                                    System.out.println("Formato incorrecto. Ejemplo: 18:30");
                                }
                            }

                            int cupo = leerEntero(sc, "Cupo maximo: ");

                            ClaseGrupal clase = new ClaseGrupal(
                                    0,
                                    nombre,
                                    actividad,
                                    dia,
                                    hora,
                                    cupo,
                                    entrenador.getId()
                            );

                            SistemBDD.guardarClase(clase);

                            System.out.println("Clase registrada correctamente!");
                            System.out.println("Nombre: " + clase.getNombre());
                            System.out.println("Actividad: " + clase.getActividad());
                            System.out.println("Dia: " + clase.getDia().getNombre());
                            System.out.println("Hora: " + clase.getHora());
                        }

                    } catch (DatosInvalidosException | GimnasioException e) {
                        System.out.println(e.getMessage());
                    }
                    break;


                case 2:
                    try {
                        ArrayList<ClaseGrupal> clases =
                                SistemBDD.listarClases();

                        if (clases.isEmpty()) {
                            System.out.println("No hay clases registradas");
                        } else {
                            for (ClaseGrupal clase : clases) {
                                System.out.println("Nombre: " + clase.getNombre());
                                System.out.println("Actividad: " + clase.getActividad());
                                System.out.println("Dia: " + clase.getDia().getNombre());
                                System.out.println("Hora: " + clase.getHora());
                                System.out.println("Cupos disponibles: "
                                        + clase.cuposLibres()
                                        + "/" + clase.getCupo());
                                System.out.println("-------------------------");
                            }
                        }

                    } catch (GimnasioException e) {
                        System.out.println(e.getMessage());
                    }
                    break;


                case 3:
                    try {
                        ArrayList<ClaseGrupal> clases = SistemBDD.listarClases();

                        if (clases.isEmpty()) {
                            System.out.println("No hay clases disponibles");
                        } else {
                            System.out.println("\n===== CLASES DISPONIBLES =====");

                            for (int i = 0; i < clases.size(); i++) {
                                ClaseGrupal clase = clases.get(i);

                                System.out.println((i + 1) + ". " + clase.getNombre());
                                System.out.println("Actividad: " + clase.getActividad());
                                System.out.println("Dia: " + clase.getDia().getNombre());
                                System.out.println("Hora: " + clase.getHora());
                                System.out.println("Cupos libres: " + clase.cuposLibres());
                                System.out.println("----------------------");
                            }

                            int opcionClase;

                            do {
                                opcionClase = leerEntero(sc, "Seleccione una clase: ");

                                if (opcionClase < 1 || opcionClase > clases.size()) {
                                    System.out.println("Opcion incorrecta");
                                }

                            } while (opcionClase < 1 || opcionClase > clases.size());

                            ClaseGrupal clase = clases.get(opcionClase - 1);

                            String dni = leerTexto(sc, "DNI del socio: ");
                            Socio socio = SistemBDD.buscarSocioPorDni(dni);

                            SistemBDD.inscribirSocioEnClase(
                                    clase.getId(),
                                    socio.getId()
                            );

                            System.out.println("Socio inscripto correctamente!");
                            System.out.println("Socio: " + socio.getNombre()
                                    + " " + socio.getApellido());
                            System.out.println("Clase: " + clase.getNombre());
                        }

                    } catch (DatosInvalidosException | GimnasioException e) {
                        System.out.println(e.getMessage());
                    }
                    break;



                case 4:
                    try {
                        ArrayList<ClaseGrupal> clases = SistemBDD.listarClases();

                        if (clases.isEmpty()) {
                            System.out.println("No hay clases registradas");
                        } else {
                            for (int i = 0; i < clases.size(); i++) {
                                System.out.println((i + 1) + ". "
                                        + clases.get(i).getNombre());
                            }

                            int opcionClase;

                            do {
                                opcionClase = leerEntero(sc, "Seleccione una clase: ");
                            } while (opcionClase < 1 || opcionClase > clases.size());

                            ClaseGrupal clase = clases.get(opcionClase - 1);

                            ArrayList<Socio> socios =
                                    SistemBDD.listarInscriptosDeClase(clase.getId());

                            if (socios.isEmpty()) {
                                System.out.println("No hay socios inscriptos");
                            } else {
                                System.out.println("Inscriptos en " + clase.getNombre());

                                for (Socio socio : socios) {
                                    System.out.println(socio.getNombre() + " "
                                            + socio.getApellido()
                                            + " | DNI: " + socio.getDni());
                                }
                            }
                        }

                    } catch (DatosInvalidosException | GimnasioException e) {
                        System.out.println(e.getMessage());
                    }
                    break;


                case 0:
                    System.out.println("Volviendo al menu principal...");
                    break;

                default:
                    System.out.println("Opcion incorrecta");
            }

        } while (opcion != 0);
    }

    private static void menuBoxeo(Scanner sc) {
        int opcion;

        do {
            System.out.println("\n===== GESTION DE BOXEO =====");
            System.out.println("1. Registrar competidor");
            System.out.println("2. Listar competidores");
            System.out.println("3. Actualizar peso");
            System.out.println("4. Programar pelea");
            System.out.println("5. Listar peleas");
            System.out.println("6. Consultar peleas de un competidor");
            System.out.println("7. Registrar resultado de pelea");
            System.out.println("0. Volver");

            opcion = leerEntero(sc, "Ingrese una opcion: ");

            switch (opcion) {
                case 1:
                    try {
                        String dni = leerTexto(sc, "DNI del socio: ");
                        Socio socio = SistemBDD.buscarSocioPorDni(dni);

                        double peso = leerDecimal(sc, "Peso en kg: ");

                        Competidor competidor = new Competidor(
                                socio, peso, 0, 0, 0, true
                        );

                        SistemBDD.guardarCompetidor(competidor);

                        System.out.println("Competidor registrado correctamente!");
                        System.out.println("Nombre: " + competidor.getNombreCompleto());
                        System.out.println("Peso: " + competidor.getPesoKg());
                        System.out.println("Categoria: " + competidor.getCategoria());

                    } catch (DatosInvalidosException | GimnasioException e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case 2:
                    try {
                        ArrayList<Competidor> competidores =
                                SistemBDD.listarCompetidores();

                        if (competidores.isEmpty()) {
                            System.out.println("No hay competidores registrados");
                        } else {
                            for (Competidor c : competidores) {
                                System.out.println("Nombre: " + c.getNombreCompleto());
                                System.out.println("DNI: " + c.getDni());
                                System.out.println("Peso: " + c.getPesoKg() + " kg");
                                System.out.println("Categoria: " + c.getCategoria());
                                System.out.println("Record: " + c.getVictorias()
                                        + "-" + c.getDerrotas()
                                        + "-" + c.getEmpates());
                                System.out.println("----------------------");
                            }
                        }

                    } catch (GimnasioException e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case 3:
                    System.out.println("Actualizar peso");
                    break;

                case 4:
                    System.out.println("Programar pelea");
                    break;

                case 5:
                    System.out.println("Listar peleas");
                    break;

                case 6:
                    System.out.println("Consultar peleas");
                    break;

                case 7:
                    System.out.println("Registrar resultado");
                    break;

                case 0:
                    System.out.println("Volviendo al menu principal...");
                    break;

                default:
                    System.out.println("Opcion incorrecta");
            }

        } while (opcion != 0);
    }

}
