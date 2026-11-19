
import java.time.LocalDate;
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
                    System.out.println("Membresias");
                    break;
                case 4:
                    System.out.println("Pagos");
                    break;
                case 5:
                    System.out.println("Asistencias");
                    break;
                case 6:
                    System.out.println("Clases grupales");
                    break;
                case 7:
                    System.out.println("Boxeo");
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
                    System.out.println("Registrar membresia");
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


}
