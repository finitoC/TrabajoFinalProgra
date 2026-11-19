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
            System.out.print("Ingrese una opcion: ");

            opcion = sc.nextInt();

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


    }
    private static void menuSocios(Scanner sc) {
        int opcion;

        do {
            System.out.println("\n===== GESTION DE SOCIOS =====");
            System.out.println("1. Registrar socio");
            System.out.println("2. Buscar socio");
            System.out.println("3. Listar socios");
            System.out.println("0. Volver");
            System.out.print("Ingrese una opcion: ");

            opcion = sc.nextInt();

            switch (opcion) {
                case 1:
                    System.out.println("Registrar socio");
                    break;

                case 2:
                    System.out.println("Buscar socio");
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
}