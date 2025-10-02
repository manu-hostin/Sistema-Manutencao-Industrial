package org.example.Model;

public class RequisicaoItem {

    private int idRequisicao;

    private int idMaterial;

    private Double quantidade;

    public RequisicaoItem(int idRequisicao, int idMaterial, Double quantidade) {
        this.idRequisicao = idRequisicao;
        this.idMaterial = idMaterial;
        this.quantidade = quantidade;
    }

    public RequisicaoItem(int idMaterial, Double quantidade) {
        this.idMaterial = idMaterial;
        this.quantidade = quantidade;
    }

    public int getIdRequisicao() {
        return idRequisicao;
    }

    public void setIdRequisicao(int idRequisicao) {
        this.idRequisicao = idRequisicao;
    }

    public int getIdMaterial() {
        return idMaterial;
    }

    public void setIdMaterial(int idMaterial) {
        this.idMaterial = idMaterial;
    }

    public Double getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Double quantidade) {
        this.quantidade = quantidade;
    }
}
