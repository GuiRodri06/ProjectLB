package pt.ismt.clinicaVitae.model.enums;

public enum CategoriaEnum {
    ROTINA("Rotina"),
    CHECK_UP("Check-up"),
    URGENCIA("Urgência"),
    RETORNO("Retorno"),
    ESPECIALIDADE("Consulta de Especialidade"),
    EXAME("Realização de Exames"),
    TELECONSULTA("Teleconsulta");

    private final String descricao;

    CategoriaEnum(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}