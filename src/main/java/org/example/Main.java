package org.example;

import org.example.Model.Fornecedor;
import org.example.Model.Material;
import org.example.dao.FornecedorDAO;
import org.example.dao.MaterialDAO;
import org.example.dao.NotaEntradaDAO;
import org.example.dao.NotaEntradaItemDAO;

import javax.net.ssl.SSLContext;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Main {
    static Scanner SC = new Scanner(System.in);
    public static void main(String[] args) {

        inicio();

    }

    public static void inicio () {
        boolean sair = false;
        System.out.println("------------------------------------------------------");
        System.out.println("    Sistema de Gestão de Almoxarifado Industrial");
        System.out.println("------------------------------------------------------");
        System.out.println("""
               1. Cadastrar fornecedor;
               2. Cadastral Material;
               3. Registrar Nota de Entrada;
               4. Criar Requisição de Material;
               5. Atender Requisição;
               6. Cancelar Requisição;
               
               0. Sair.""");
        System.out.print("Digite a opção que deseja: ");
        int opcao = SC.nextInt();
        SC.nextLine();

        switch (opcao){
            case 1 -> {
                cadastrarFornecedor();
                break;
            }
            case 2 -> {
                cadastrarMaterial();
                break;
            }
            case 3 -> {
                registrarNota();
                break;
            }
            case 4 -> {

            }
            case 5 -> {

            }
            case 6 -> {

            }
            case 0 -> {

            }

        }
        if(!sair){
            inicio();
        }
    }
    public static void cadastrarFornecedor() {
        System.out.println("\n=== CADASTRAR FORNECEDOR ===");
        System.out.print("\nDigite o nome do fornecedor: ");
        String nome = SC.nextLine();

        System.out.print("Digite o CNPJ do fornecedor: ");
        String cnpj = SC.nextLine();

            if(!nome.isEmpty() && !cnpj.isEmpty()){

                var fornecedor = new Fornecedor(nome, cnpj);
                var dao = new FornecedorDAO();

                try{
                    if (!dao.validarDuplicacao(fornecedor)) {

                        dao.cadastrarFornecedor(fornecedor);

                    } else {
                        System.out.println("\nCNPJ já cadastrado!");
                    }

                } catch (SQLException e){
                    e.printStackTrace();
                }
            } else {
                System.out.println("\nPor favor, digite um nome e um cnpj válido.");
            }
    }
    public static void cadastrarMaterial(){
        System.out.println("\n=== CADASTRAR MATERIAL ===");
        System.out.print("Digite o nome do material: ");
        String nome = SC.nextLine();

        System.out.print("Digite a unidade de medida (kg, m², m³, peça...): ");
        String medida = SC.nextLine();

        System.out.print("Digite a quantidade inicial em estoque (apenas números): ");
        double quantidade = SC.nextDouble();
        SC.nextLine();

        if(!nome.isEmpty() && !medida.isEmpty() && quantidade >= 0){

            var material = new Material(nome, medida, quantidade);
            var dao = new MaterialDAO();

            try{
                if(!dao.verificarDuplicacao(material)){

                    dao.cadastrarMaterial(material);
                } else {
                    System.out.println("\nMaterial já cadastrado!");
                }
            } catch (SQLException e){
                e.printStackTrace();
            }
        } else {
            System.out.println("\nPor favor, digite todos os campos referentes.");
        }
    }

    public static void registrarNota() {

        var fornecedorDao = new FornecedorDAO();
        var materialDao = new MaterialDAO();
        var daoNota = new NotaEntradaDAO();
        var daoItem = new NotaEntradaItemDAO();
        List<Material> materiais = new ArrayList<>();
        List<Fornecedor> fornecedores = new ArrayList<>();

        System.out.println("\n=== REGISTRAR NOTA DE ENTRADA ===");

        System.out.println("Fornecedores disponíveis: ");
        for (Fornecedor f : fornecedores) {
            System.out.println(f.getId() + " - " + f.getNome());
            System.out.println("CNPJ: "+f.getCnpj());
        }
        System.out.print("Digite o id do fornecedor que deseja adicionar: "); // Pede o CNPJ
        int id = SC.nextInt();
        SC.nextLine();

        var fornecedor = new Fornecedor(id); // Cria o objeto
        try {
            if (!fornecedorDao.validarDuplicacao(fornecedor)) { // Verifica se fornecedor existe
                System.out.println("Fornecedor não existe.");
                return;
            }
        } catch (SQLException e){
            e.printStackTrace();
        }

        System.out.print("Digite a data de entrada (AAAA-MM-DD): "); // Pede a data
        Date dataEntrada = java.sql.Date.valueOf(SC.nextLine());

        try{ // Cria a lista de materiais
            materiais = materialDao.listarMateriais();
        } catch (SQLException e){
            e.printStackTrace();
        }

        System.out.println("\nMateriais disponíveis:");
        materiais.forEach(material -> { // Lista os materiais
            System.out.println("Material "+material.getNome());
            System.out.println("Unidade: "+material.getUnidade());
            System.out.println("Estoque: "+material.getEstoque());
        });

        int opcao;
        do {
            System.out.print("\nDigite o nome do material que deseja associar: "); // Pede o nome de qual material
            String materialNome = SC.nextLine();
            var material = new Material(materialNome);

            try{
                if(!materialDao.verificarDuplicacao(material)){ // Verifica se o material existe
                    System.out.println("\nNão há nenhum material cadastrado com esse nome.");
                    return;
                }

            } catch (SQLException e){
                e.printStackTrace();
            }

            System.out.print("Deseja adicionar mais algum material?\n1 - Sim; \n2 - Não: ");
            opcao = SC.nextInt();

        } while (opcao == 1);

        daoNota.registrarNotaEntrada(id, dataEntrada);

    }


}