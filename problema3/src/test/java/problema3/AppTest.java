package problema3;



import static org.junit.Assert.*;

import org.junit.Test;

import java.util.List;



public class AppTest {



    private RecomendadorActividades sistema = new RecomendadorActividades();



    // --- Helper para crear aforos vacíos (todo libre) ---

    private EstadoAforos aforosLibres() {

        return new EstadoAforos(false, false, false, false);

    }

    

    // --- Helper para Salud Perfecta ---

    private EstadoSalud saludOK() {

        return new EstadoSalud(true, false);

    }



    // 1. Test Salud Incorrecta (Debe bloquear todo)

    @Test

    public void testSaludIncorrecta() {

        // Facultades mal, síntomas bien

        EstadoSalud enfermo = new EstadoSalud(false, false);

        CondicionesMeteorologicas climaPerfecto = new CondicionesMeteorologicas(20, 50, false, false, false);

        

        List<String> resultado = sistema.recomendar(enfermo, climaPerfecto, aforosLibres());

        

        assertEquals(1, resultado.size());

        assertTrue(resultado.get(0).contains("No se puede realizar ninguna actividad"));

    }



    // 2. Test Quedarse en Casa (Frío extremo + Lluvia)

    @Test

    public void testQuedarseEnCasa() {

        // Temp -5, Hum 10, Precipitación (Lluvia o Nieve)

        CondicionesMeteorologicas climaInvernal = new CondicionesMeteorologicas(-5, 10, true, true, true);

        

        List<String> resultado = sistema.recomendar(saludOK(), climaInvernal, aforosLibres());

        

        assertTrue(resultado.contains("Quedarse en casa"));

    }



    // 3. Test Esquí (Frío extremo, seco, sin precipitación)

    @Test

    public void testEsqui() {

        // Temp -5, Hum 10, No Precipitación

        CondicionesMeteorologicas climaEsqui = new CondicionesMeteorologicas(-5, 10, false, false, false);

        

        List<String> resultado = sistema.recomendar(saludOK(), climaEsqui, aforosLibres());

        

        assertTrue(resultado.contains("Esquí"));

    }

    

    // 4. Test Esquí (Aforo Completo -> No debe recomendar)

    @Test

    public void testEsquiAforoLleno() {

        CondicionesMeteorologicas climaEsqui = new CondicionesMeteorologicas(-5, 10, false, false, false);

        EstadoAforos aforosOcupados = new EstadoAforos(true, false, false, false); // Esqui ocupado

        

        List<String> resultado = sistema.recomendar(saludOK(), climaEsqui, aforosOcupados);

        

        assertFalse(resultado.contains("Esquí"));

    }



    // 5. Test Senderismo (0-15 grados, sin lluvia)

    @Test

    public void testSenderismo() {

        // Temp 10, No llueve

        CondicionesMeteorologicas climaOtonio = new CondicionesMeteorologicas(10, 50, false, false, false);

        

        List<String> resultado = sistema.recomendar(saludOK(), climaOtonio, aforosLibres());

        

        assertTrue(resultado.contains("Senderismo"));

        assertTrue(resultado.contains("Escalada"));

    }



    // 6. Test Primavera (15-25 grados, ideal)

    @Test

    public void testPrimavera() {

        // Temp 20, Hum 50, Sol

        CondicionesMeteorologicas climaPrimavera = new CondicionesMeteorologicas(20, 50, false, false, false);

        

        List<String> resultado = sistema.recomendar(saludOK(), climaPrimavera, aforosLibres());

        

        assertTrue(resultado.contains("Actividades del catálogo de primavera, verano u otoño"));

    }



    // 7. Test Solapamiento: Culturales y Playa (32 grados)

    // Este caso es interesante porque 32ºC cumple la regla de Culturales (25-35) Y la de Playa (>30).

    @Test

    public void testSolapamientoCalor() {

        // Temp 32, No llueve

        CondicionesMeteorologicas climaCalor = new CondicionesMeteorologicas(32, 40, false, false, false);

        

        List<String> resultado = sistema.recomendar(saludOK(), climaCalor, aforosLibres());

        

        // Debe sugerir TODO lo posible

        assertTrue("Debe sugerir culturales", resultado.contains("Actividades culturales"));

        assertTrue("Debe sugerir playa", resultado.contains("Playa"));

        assertTrue("Debe sugerir piscina", resultado.contains("Piscina"));

    }

    

    // 8. Test Lluvia (Bloquea casi todo excepto casa, o nada si no hace frío extremo)

    @Test

    public void testLluviaGeneral() {

        // Temp 20, Lluvia

        CondicionesMeteorologicas climaLluvioso = new CondicionesMeteorologicas(20, 80, true, false, true);

        

        List<String> resultado = sistema.recomendar(saludOK(), climaLluvioso, aforosLibres());

        

        // No debe recomendar senderismo, ni primavera, ni playa...

        // Dependiendo de la implementación, podría devolver lista vacía o mensaje por defecto.

        assertFalse(resultado.contains("Senderismo"));

        assertFalse(resultado.contains("Playa"));

        

        // Verificamos si devuelve el mensaje por defecto (opcional según implementación)

        if (resultado.size() == 1) {

             assertTrue(resultado.get(0).contains("No hay recomendaciones"));

        }

    }

}