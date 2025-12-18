package problema3;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class AppTest {

    private RecomendadorActividades sut;

    @Before
    public void setUp() {
        sut = new RecomendadorActividades();
    }

    // Helpers para crear mocks rápido
    private EstadoSalud salud(boolean plenas, boolean sintomas) {
        EstadoSalud s = mock(EstadoSalud.class);
        when(s.isFacultadesFisicasPlenas()).thenReturn(plenas);
        when(s.hasSintomasInfecciososUltimas2Semanas()).thenReturn(sintomas);
        return s;
    }

    private CondicionesMeteorologicas clima(double temp, int hum, boolean precip, boolean nieve, boolean nublado) {
        CondicionesMeteorologicas c = mock(CondicionesMeteorologicas.class);
        when(c.getTemperatura()).thenReturn(temp);
        when(c.getHumedadRelativa()).thenReturn(hum);
        when(c.isHayPrecipitaciones()).thenReturn(precip);
        when(c.isEsNieve()).thenReturn(nieve);
        when(c.isEstaNublado()).thenReturn(nublado);
        return c;
    }

    private EstadoAforos aforos(boolean esquiCompleto, boolean senderismoCompleto, boolean culturalCompleto, boolean piscinaCompleta) {
        EstadoAforos a = mock(EstadoAforos.class);
        when(a.isAforoEsquiCompleto()).thenReturn(esquiCompleto);
        when(a.isAforoSenderismoCompleto()).thenReturn(senderismoCompleto);
        when(a.isAforoCulturalCompleto()).thenReturn(culturalCompleto);
        when(a.isAforoPiscinaCompleto()).thenReturn(piscinaCompleta);
        return a;
    }

    // --- Pruebas de Caja Negra (reglas) ---

    // 1) Salud mala -> retorno temprano
    @Test
    public void testSaludSinFacultades_NoPuedeHacerNada() {
        EstadoSalud s = salud(false, false);

        // clima y aforos no deberían usarse si retorna temprano, pero igual los pasamos
        CondicionesMeteorologicas c = mock(CondicionesMeteorologicas.class);
        EstadoAforos a = mock(EstadoAforos.class);

        List<String> out = sut.recomendar(s, c, a);

        assertEquals(1, out.size());
        assertEquals("No se puede realizar ninguna actividad debido al estado de salud.", out.get(0));
        verifyNoInteractions(c);
    }

    // 2) Casa (temp<0, hum<15, precip=true) -> retorno temprano exclusivo
    @Test
    public void testQuedarseEnCasa_PrioridadAlta() {
        EstadoSalud s = salud(true, false);
        CondicionesMeteorologicas c = clima(-1.0, 10, true, false, false);
        EstadoAforos a = aforos(false, false, false, false);

        List<String> out = sut.recomendar(s, c, a);

        assertEquals(1, out.size());
        assertEquals("Quedarse en casa", out.get(0));
    }

    // 3) Esquí (temp<0, hum<15, NO precip) y aforo NO completo
    @Test
    public void testEsqui_ConAforoDisponible() {
        EstadoSalud s = salud(true, false);
        CondicionesMeteorologicas c = clima(-5.0, 10, false, false, false);
        EstadoAforos a = aforos(false, false, false, false);

        List<String> out = sut.recomendar(s, c, a);

        assertTrue(out.contains("Esquí"));
    }

    // 4) Senderismo/Escalada (0..15, NO llueve) y aforo senderismo NO completo
    @Test
    public void testSenderismoYEscalada() {
        EstadoSalud s = salud(true, false);
        CondicionesMeteorologicas c = clima(10.0, 50, false, false, false);
        EstadoAforos a = aforos(false, false, false, false);

        List<String> out = sut.recomendar(s, c, a);

        assertTrue(out.contains("Senderismo"));
        assertTrue(out.contains("Escalada"));
    }

    // 5) Primavera/Verano/Otoño (15..25, no llueve, no nublado, hum<=60)
    @Test
    public void testCatalogoPrimaveraVeranoOtono() {
        EstadoSalud s = salud(true, false);
        CondicionesMeteorologicas c = clima(20.0, 60, false, false, false);
        EstadoAforos a = aforos(false, false, false, false);

        List<String> out = sut.recomendar(s, c, a);

        assertTrue(out.contains("Actividades del catálogo de primavera, verano u otoño"));
    }

    // 6) Culturales/Gastronómicas (25..35, NO llueve) y aforo cultural NO completo
    @Test
    public void testCulturalesYGastronomicas() {
        EstadoSalud s = salud(true, false);
        CondicionesMeteorologicas c = clima(25.0, 80, false, false, false);
        EstadoAforos a = aforos(false, false, false, false);

        List<String> out = sut.recomendar(s, c, a);

        assertTrue(out.contains("Actividades culturales"));
        assertTrue(out.contains("Actividades gastronómicas"));
    }

    // 7) Playa/Piscina (temp>30, NO llueve). Piscina depende de aforo
    @Test
    public void testPlayaYPiscina_AforoPiscinaDisponible() {
        EstadoSalud s = salud(true, false);
        CondicionesMeteorologicas c = clima(31.0, 40, false, false, false);
        EstadoAforos a = aforos(false, false, false, false);

        List<String> out = sut.recomendar(s, c, a);

        assertTrue(out.contains("Playa"));
        assertTrue(out.contains("Piscina"));
    }

    // 8) Caso donde ninguna regla aplica => mensaje por defecto
    @Test
    public void testSinRecomendaciones_DevuelveMensajeGenerico() {
        EstadoSalud s = salud(true, false);

        // Lluvia: precip=true y nieve=false => llueve=true
        // Con llueve=true NO se activan Senderismo/Escalada, ni PVO, ni Cultural/Gastro, ni Playa/Piscina.
        // Además temp no es <0, así que no entra en Casa ni Esquí.
        CondicionesMeteorologicas c = clima(20.0, 50, true, false, false);

        EstadoAforos a = aforos(false, false, false, false);

        List<String> out = sut.recomendar(s, c, a);

        assertEquals(1, out.size());
        assertEquals("No hay recomendaciones específicas para las condiciones actuales.", out.get(0));
    }

}
