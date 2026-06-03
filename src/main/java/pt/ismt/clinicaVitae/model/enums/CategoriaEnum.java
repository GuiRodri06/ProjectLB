package pt.ismt.clinicaVitae.model.enums;

public enum CategoriaEnum {
    ROTINA("Rotina"),
    CHECK_UP("Check-up"),
    URGENCIA("Urgência"),
    RETORNO("Retorno");

    private final String descricao;

    CategoriaEnum(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}