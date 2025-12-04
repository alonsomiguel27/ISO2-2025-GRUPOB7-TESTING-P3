package problema3;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;


public class Main {

    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        RecomendadorActividades sistema = new RecomendadorActividades();
        boolean continuar = true;

        while (continuar) {
            try {
                System.out.println("\n--- 1. Datos del Cliente ---");
                boolean facultades = leerBoolean("¿Tiene facultades físicas plenas?");
                boolean sintomas = leerBoolean("¿Ha tenido síntomas infecciosos en las últimas 2 semanas?");
                EstadoSalud salud = new EstadoSalud(facultades, sintomas);

                System.out.println("\n--- 2. Condiciones Meteorológicas ---");
                double temperatura = leerDouble("Temperatura actual (°C): ");
                int humedad = leerEntero("Humedad relativa (0-100): ");
                boolean precipitaciones = leerBoolean("¿Hay precipitaciones?");
                boolean esNieve = false;
                if (precipitaciones) {
                    esNieve = leerBoolean("¿Las precipitaciones son de nieve?");
                }
                boolean nublado = leerBoolean("¿Está nublado?");
                
                CondicionesMeteorologicas clima = new CondicionesMeteorologicas( //valida humedad internamente
                        temperatura, humedad, precipitaciones, esNieve, nublado
                );

                System.out.println("\n--- 3. Estado de Aforos (Instalaciones) ---");
                // Solo preguntamos aforos si el cliente está sano, para agilizar, 
                boolean aforoEsqui = leerBoolean("¿Aforo de esquí completo?");
                boolean aforoSenderismo = leerBoolean("¿Aforo de senderismo completo?");
                boolean aforoCultural = leerBoolean("¿Aforo cultural/gastronómico completo?");
                boolean aforoPiscina = leerBoolean("¿Aforo de piscina completo?");

                EstadoAforos aforos = new EstadoAforos(
                        aforoEsqui, aforoSenderismo, aforoCultural, aforoPiscina
                );

                List<String> recomendaciones = sistema.recomendar(salud, clima, aforos);

                System.out.println("ACTIVIDADES RECOMENDADAS");

                if (recomendaciones.isEmpty()) {
                    System.out.println("- No hay recomendaciones disponibles para estas condiciones.");
                } else {
                    for (String actividad : recomendaciones) {
                        System.out.println("-> " + actividad);
                    }
                }

            } catch (IllegalArgumentException e) {
                System.err.println("\nERROR DE VALIDACIÓN: " + e.getMessage());
            } catch (Exception e) {
                System.err.println("\nERROR INESPERADO: " + e.getMessage());
                scanner.nextLine();
            }

            continuar = leerBoolean("\n¿Desea realizar otra consulta?");
        }

        System.out.println("Gracias por usar el sistema.");
        scanner.close();
    }

    // --- Métodos auxiliares para lectura robusta de datos ---

    private static boolean leerBoolean(String mensaje) {
        while (true) {
            System.out.print(mensaje + " (s/n): ");
            String entrada = scanner.next().trim().toLowerCase();
            if (entrada.equals("s") || entrada.equalsIgnoreCase("Si")) return true;
            if (entrada.equals("n") || entrada.equalsIgnoreCase("No")) return false;
            System.out.println("Por favor, introduzca 's' para sí o 'n' para no.");
        }
    }

    private static double leerDouble(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                return scanner.nextDouble();
            } catch (InputMismatchException e) {
                System.out.println("Error: Introduce un número válido.");
                scanner.next();
            }
        }
    }

    private static int leerEntero(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                return scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Error: Introduce un número entero válido.");
                scanner.next();
            }
        }
    }
}
