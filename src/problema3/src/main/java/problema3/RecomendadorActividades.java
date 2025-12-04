package problema3;
import java.util.ArrayList;
import java.util.List;



public class RecomendadorActividades {

    /**
     * Genera una lista de recomendaciones basada en las condiciones dadas.
     * @param salud Estado de salud del cliente
     * @param clima Condiciones del tiempo
     * @param aforos Estado de los aforos
     * @return Lista de cadenas con las actividades sugeridas
     */
    public List<String> recomendar(EstadoSalud salud, CondicionesMeteorologicas clima, EstadoAforos aforos) {
        List<String> recomendaciones = new ArrayList<>();

        // Regla 1: Salud
        // Si no tiene facultades plenas o ha tenido síntomas, no puede hacer nada.
        if (!salud.isFacultadesFisicasPlenas() || salud.hasSintomasInfecciososUltimas2Semanas()) {
            recomendaciones.add("No se puede realizar ninguna actividad debido al estado de salud.");
            return recomendaciones; // Retorno temprano
        }

        double temp = clima.getTemperatura();
        int hum = clima.getHumedadRelativa();
        boolean precip = clima.isHayPrecipitaciones();
        boolean nieve = clima.isEsNieve();
        boolean nublado = clima.isEstaNublado();

        // Regla 2: Casa
        // Temp < 0, Hum < 15%, Precipitación (Nieve o Agua)
        if (temp < 0 && hum < 15 && precip) {
            recomendaciones.add("Quedarse en casa");
            return recomendaciones; // Prioridad alta, sugerencia exclusiva
        }

        // Regla 3: Esquí 
        // Temp < 0, Hum < 15%, NO precipitación
        if (temp < 0 && hum < 15 && !precip) {
            if (!aforos.isAforoEsquiCompleto()) {
                recomendaciones.add("Esquí");
            }
        }

        // Regla 4: Senderismo / Escalada 
        // Temp 0-15, NO precipitación de agua (asumimos precip=true y nieve=false es lluvia)
        boolean llueve = precip && !nieve;
        if (temp >= 0 && temp <= 15 && !llueve) {
            if (!aforos.isAforoSenderismoCompleto()) {
                recomendaciones.add("Senderismo");
                recomendaciones.add("Escalada");
            }
        }

        // Regla 5: Primavera/Verano/Otoño 
        // Temp 15-25, NO llueve, NO nublado, Hum <= 60%
        if (temp >= 15 && temp <= 25 && !llueve && !nublado && hum <= 60) {
            recomendaciones.add("Actividades del catálogo de primavera, verano u otoño");
        }

        // Regla 6: Culturales / Gastronómicas 
        // Temp 25-35, NO llueve
        if (temp >= 25 && temp <= 35 && !llueve) {
            if (!aforos.isAforoCulturalCompleto()) {
                recomendaciones.add("Actividades culturales");
                recomendaciones.add("Actividades gastronómicas");
            }
        }

        // Regla 7: Playa / Piscina [cite: 225-226]
        // Temp > 30, NO llueve
        if (temp > 30 && !llueve) {
            recomendaciones.add("Playa");
            if (!aforos.isAforoPiscinaCompleto()) {
                recomendaciones.add("Piscina");
            }
        }
        
        // Manejo de caso sin recomendaciones explícitas en el texto
        if (recomendaciones.isEmpty()) {
            recomendaciones.add("No hay recomendaciones específicas para las condiciones actuales.");
        }

        return recomendaciones;
    }
}
