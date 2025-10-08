package org.example;

import org.example.Model.Fornecedor;
import org.example.Model.Material;
import org.example.Model.RequisicaoItem;
import org.example.Model.RequisicaoMaterial;
import org.example.dao.*;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class Main {
    static Scanner SC = new Scanner(System.in);

    public static void main(String[] args) {

        inicio();

    }

    public static void inicio() {
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
                
                0. Sair.""");
        System.out.print("Digite a opção que deseja: ");
        int opcao = SC.nextInt();
        SC.nextLine();

        switch (opcao) {
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
                requisicaoMaterial();
                break;
            }
            case 5 -> {
                atenderRequisicao();
                break;
            }
            case 0 -> {
                sair = true;
                System.out.println("Saindo... Até breve.");
                break;
            }

        }
        if (!sair) {
            inicio();
        }
    }

    public static void cadastrarFornecedor() {
        System.out.println("\n=== CADASTRAR FORNECEDOR ===");
        System.out.print("\nDigite o nome do fornecedor: ");
        String nome = SC.nextLine();

        System.out.print("Digite o CNPJ do fornecedor: ");
        String cnpj = SC.nextLine();

        if (!nome.isEmpty() && !cnpj.isEmpty()) {

            var fornecedor = new Fornecedor(nome, cnpj);
            var dao = new FornecedorDAO();

            try {
                if (!dao.validarDuplicacao(fornecedor)) {

                    dao.cadastrarFornecedor(fornecedor);

                } else {
                    System.out.println("\nCNPJ já cadastrado!");
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("\nPor favor, digite um nome e um cnpj válido.");
        }
    }

    public static void cadastrarMaterial() {
        System.out.println("\n=== CADASTRAR MATERIAL ===");
        System.out.print("Digite o nome do material: ");
        String nome = SC.nextLine();

        System.out.print("Digite a unidade de medida (kg, m², m³, peça...): ");
        String medida = SC.nextLine();

        System.out.print("Digite a quantidade inicial em estoque (apenas números): ");
        double quantidade = SC.nextDouble();
        SC.nextLine();

        if (!nome.isEmpty() && !medida.isEmpty() && quantidade >= 0) {

            var material = new Material(nome, medida, quantidade);
            var dao = new MaterialDAO();

            try {
                if (!dao.verificarDuplicacao(material)) {

                    dao.cadastrarMaterial(material);
                } else {
                    System.out.println("\nMaterial já cadastrado!");
                }
            } catch (SQLException e) {
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
        int opcao;
        int idMaterial;
        Double quantidade;
        int fornecedor = 0;
        int idNota = 0;
        Double novaQuantidade;

        System.out.println("\n=== REGISTRAR NOTA DE ENTRADA ===");

        System.out.print("Digite o CNPJ do fornecedor: ");
        String cnpj = SC.nextLine();

        try {
            fornecedor = fornecedorDao.buscarPorCnpj(cnpj);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (fornecedor == -1) {
            System.out.println("Fornecedor não encontrado!");
            return;
        }

        System.out.print("Digite a data de entrada (AAAA-MM-DD): ");
        String dataStr = SC.nextLine();
        LocalDate dataLocal = LocalDate.parse(dataStr); // Converte para LocalDate
        java.sql.Date dataEntrada = java.sql.Date.valueOf(dataLocal); // Converte para java.sql.Date


        try { // Cria a lista de materiais
            materiais = materialDao.listarMateriais();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        do {
            try {
                materiais = materialDao.listarMateriais();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            System.out.println("\nMateriais disponíveis:");
            materiais.forEach(material -> { // Lista os materiais
                System.out.println("ID: " + material.getId());
                System.out.println("Material " + material.getNome());
                System.out.println("Unidade: " + material.getUnidade());
                System.out.println("Estoque: " + material.getEstoque());
                System.out.println("----------------------------------------");
            });

            System.out.print("\nDigite o ID do material que deseja associar: "); // Pede o id de qual material
            idMaterial = SC.nextInt();

            var material = new Material(idMaterial);

            try {
                if (!materialDao.verificarDuplicacaoID(material)) { // Verifica se o material existe
                    System.out.println("\nNão há nenhum material cadastrado com esse ID. Operação cancelada.");
                    return;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            do {
                System.out.println("Digite a quantidade: ");
                quantidade = SC.nextDouble();
                SC.nextLine();

                if (quantidade <= 0) {
                    System.out.println("Quantidade não pode ser nula ou negativa.");
                }
            } while (quantidade <= 0);

            materiais.remove(material);
            // Arrumar a questão do pegar estoque e somar com o antigo.
            Double antigoEstoque = material.getEstoque();
            novaQuantidade = (antigoEstoque + quantidade);

            try {
                materialDao.atualizarMaterial(novaQuantidade, idMaterial);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            System.out.print("Deseja adicionar mais algum material?\n1 - Sim; \n2 - Não: ");
            opcao = SC.nextInt();

        } while (opcao == 1);

        try {
            daoNota.registrarNotaEntrada(fornecedor, dataEntrada);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        try {
            idNota = daoNota.pegarIDNota(fornecedor, dataEntrada);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            daoItem.registrarNotaEntradaItem(idNota, idMaterial, quantidade);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public static void requisicaoMaterial() {
        var materialDao = new MaterialDAO();
        var daoReq = new RequisicaoMaterialDAO();
        var daoReqItem = new RequisicaoItemDAO();

        System.out.println("\n=== REQUISIÇÃO DE MATERIAL ===");
        System.out.print("Digite o nome do setor requisitante: ");
        String setor = SC.nextLine();


        System.out.print("\nDigite o nome do material que deseja requisitar: ");
        String material = SC.nextLine();

        try { // Confirma se material existe
            if (!materialDao.validarMaterial(material)) {
                System.out.println("\nMaterial não existe! Operação cancelada.");
                return;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        System.out.print("\nDigite a quantidade do material: ");
        Double quantidade = SC.nextDouble();
        SC.nextLine();

        try { // Confirma quantidade digitada
            if (quantidade > materialDao.buscarQuantidade(material) || quantidade <= 0) {
                System.out.println("\nQuantidade indisponível. Operação cancelada.");
                return;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        System.out.println("\nDigite a data de solicitação: ");
        String dataStr = SC.nextLine();
        LocalDate dataSolicitacao = LocalDate.parse(dataStr);
        java.sql.Date sqlDate = java.sql.Date.valueOf(dataSolicitacao);

        try {
            var requisicao = new RequisicaoMaterial(setor, "PENDENTE", sqlDate); // Cria objeto requisicao

            if (!setor.isEmpty() && !material.isEmpty()) {
                daoReq.RegistrarRequisicao(requisicao); // Realizou a requisição do material
            } else {
                System.out.println("Por favor, digite todos os campos.");
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            int idReq = daoReq.pegarIDRequisicao(setor, Date.valueOf(dataSolicitacao)); // Pega ID requisição
            int idMaterial = materialDao.pegarIDMaterial(material); // Pega ID material

            var requisicaoItem = new RequisicaoItem(idReq, idMaterial, quantidade); // Cria o objeto reqItem
            daoReqItem.registrarRequisicaoItem(idReq, idMaterial, quantidade);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void atenderRequisicao() {
        var daoRqMaterial = new RequisicaoMaterialDAO();
        var daoMaterial = new MaterialDAO();
        var daoReqItem = new RequisicaoItemDAO();
        var material = new Material();
        List<RequisicaoMaterial> listaPendentes = new ArrayList<>();
        List<RequisicaoItem> itens = new ArrayList<>();

        try {
            listaPendentes = daoRqMaterial.listarRequisicoesPendentes();
            itens = daoReqItem.listarItens();

            if (listaPendentes.isEmpty()) {
                System.out.println("Não há requisições pendentes!");
                return;
            } // Não há requisições

            System.out.println("Requisições PENDENTES disponíveis:");
            for (RequisicaoMaterial rq : listaPendentes) {
                System.out.println("ID: " + rq.getId() + " | Setor: " + rq.getSetor() +
                        " | Data: " + rq.getDataSolicitacao());
            } // Lista as requisições pendentes

            System.out.print("Digite o ID da requisição que deseja atender: ");
            int idRequisicao = SC.nextInt();
            SC.nextLine();

            itens.forEach(itensDaReq -> { // Lista o item da requisição escolhida
                if (itensDaReq.getIdRequisicao() == idRequisicao) {
                    System.out.println("----------------------");
                    System.out.println("ID da Requisição: " + itensDaReq.getIdRequisicao());
                    System.out.println("ID do Material: " + itensDaReq.getIdMaterial());
                    System.out.println("Quantidade: " + itensDaReq.getQuantidade());
                }
            });

            if (itens.isEmpty()) {
                System.out.println("Nenhum item encontrado para essa requisição!");
                return;
            } // Não há Requisições Itens


            for (RequisicaoItem item : itens) {
                if (item.getIdRequisicao() == idRequisicao) {

                    int idMaterialDoItem = item.getIdMaterial();
                    Double quantidadeRequisitada = item.getQuantidade();

                    daoRqMaterial.atualizarStatus(idRequisicao); // Atualiza o status da requisição
                    daoMaterial.subtrairEstoque(idMaterialDoItem, quantidadeRequisitada);

                    System.out.println("\nRequisição feita e estoque e status atualizados!");
                }
            } // Atualiza o estoque do material

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}