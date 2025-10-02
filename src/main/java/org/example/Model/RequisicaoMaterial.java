package org.example.Model;

import java.util.Date;

public class RequisicaoMaterial {

    private int id;

    private String setor;

    private String status;

    private Date dataSolicitacao;

    public RequisicaoMaterial(int id, String setor, String status, Date dataSolicitacao) {
        this.id = id;
        this.setor = setor;
        this.status = status;
        this.dataSolicitacao = dataSolicitacao;
    }

    public RequisicaoMaterial(String setor, String status, Date dataSolicitacao) {
        this.setor = setor;
        this.status = status;
        this.dataSolicitacao = dataSolicitacao;
    }


    public Date getDataSolicitacao() {
        return dataSolicitacao;
    }

    public void setDataSolicitacao(Date dataSolicitacao) {
        this.dataSolicitacao = dataSolicitacao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSetor() {
        return setor;
    }

    public void setSetor(String setor) {
        this.setor = setor;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
