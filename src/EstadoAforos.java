package src;
public class EstadoAforos {

    private boolean aforoEsquiCompleto;
    private boolean aforoSenderismoCompleto;
    private boolean aforoCulturalCompleto;
    private boolean aforoPiscinaCompleto;

    public EstadoAforos(boolean aforoEsquiCompleto, boolean aforoSenderismoCompleto, boolean aforoCulturalCompleto, boolean aforoPiscinaCompleto) {

        this.aforoEsquiCompleto = aforoEsquiCompleto;
        this.aforoSenderismoCompleto = aforoSenderismoCompleto;
        this.aforoCulturalCompleto = aforoCulturalCompleto;
        this.aforoPiscinaCompleto = aforoPiscinaCompleto;
    }

    public boolean isAforoEsquiCompleto() { 
        return aforoEsquiCompleto; 
    }

    public boolean isAforoSenderismoCompleto() { 
        return aforoSenderismoCompleto; 
    }

    public boolean isAforoCulturalCompleto() { 
        return aforoCulturalCompleto; 
    }

    public boolean isAforoPiscinaCompleto() { 
        return aforoPiscinaCompleto; 
    }
    
}
