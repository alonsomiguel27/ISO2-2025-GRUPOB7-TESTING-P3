package src;
public class EstadoSalud {

    private boolean facultadesFisicasPlenas;
    private boolean sintomasInfecciososUltimas2Semanas;

    public EstadoSalud(boolean facultadesFisicasPlenas, boolean sintomasInfecciososUltimas2Semanas) {
        
        this.facultadesFisicasPlenas = facultadesFisicasPlenas;
        this.sintomasInfecciososUltimas2Semanas = sintomasInfecciososUltimas2Semanas;
    }

    public boolean isFacultadesFisicasPlenas() {
        return facultadesFisicasPlenas;
    }

    public boolean hasSintomasInfecciososUltimas2Semanas() {
        return sintomasInfecciososUltimas2Semanas;
    }
}
