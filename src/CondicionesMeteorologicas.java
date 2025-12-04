package src;
public class CondicionesMeteorologicas {
    private double temperatura;
    private int humedadRelativa; // 0-100
    private boolean hayPrecipitaciones; // true si llueve o nieva
    private boolean esNieve; // true si la precipitación es nieve
    private boolean estaNublado;

    public CondicionesMeteorologicas(double temperatura, int humedadRelativa, boolean hayPrecipitaciones, boolean esNieve, boolean estaNublado) {
        if (humedadRelativa < 0 || humedadRelativa > 100) {
            throw new IllegalArgumentException("La humedad debe estar entre 0 y 100");
        }
        this.temperatura = temperatura;
        this.humedadRelativa = humedadRelativa;
        this.hayPrecipitaciones = hayPrecipitaciones;
        this.esNieve = esNieve;
        this.estaNublado = estaNublado;
    }

    public double getTemperatura() { return temperatura; }
    public int getHumedadRelativa() { return humedadRelativa; }
    public boolean isHayPrecipitaciones() { return hayPrecipitaciones; }
    public boolean isEsNieve() { return esNieve; }
    public boolean isEstaNublado() { return estaNublado; }
}
