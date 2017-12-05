package com.example.neror.trabalho_4.Model;

import java.util.Date;
import java.util.UUID;

/**
 * Created by neror on 23/11/2017.
 */

public class Infracao {

    private UUID Id;
    private String Descricao;
    private Double IMEI;
    private Double Latitude;
    private Double Longitude;
    private Date DataInfracao;

    public UUID getId() {
        return Id;
    }

    public void setId(UUID id) {
        Id = id;
    }

    public String getDescricao() {
        return Descricao;
    }

    public void setDescricao(String descricao) {
        Descricao = descricao;
    }

    public Double getIMEI() {
        return IMEI;
    }

    public void setIMEI(Double IMEI) {
        this.IMEI = IMEI;
    }

    public Double getLatitude() {
        return Latitude;
    }

    public void setLatitude(Double latitude) {
        Latitude = latitude;
    }

    public Double getLongitude() {
        return Longitude;
    }

    public void setLongitude(Double longitude) {
        Longitude = longitude;
    }

    public Date getDataInfracao() {
        return DataInfracao;
    }

    public void setDataInfracao(Date dataInfracao) {
        DataInfracao = dataInfracao;
    }
}
