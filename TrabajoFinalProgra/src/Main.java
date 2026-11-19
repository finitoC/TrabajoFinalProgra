
import java.time.LocalDate;
import java.util.ArrayList;
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
                    System.out.println("Gestion de entrenadores");
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
}
