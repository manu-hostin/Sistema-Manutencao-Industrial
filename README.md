# 💾 Sistema de Manutenção Industrial - Protótipo WEG

## 🔹 Descrição
Este projeto é um protótipo de sistema desenvolvido em **Java** para gerenciamento de almoxarifado industrial. O sistema permite controlar fornecedores, materiais, notas de entrada e requisições de materiais, utilizando **JDBC** para persistência em banco de dados MySQL.  

O protótipo simula cenários corporativos reais, servindo como base para futuros sistemas mais complexos na WEG.

---

## 🔹 Funcionalidades

1. **Cadastrar Fornecedor**
   - Nome e CNPJ obrigatórios
   - Valida CNPJ único
   - Confirmação de sucesso ou erro

2. **Cadastrar Material**
   - Nome, unidade de medida e estoque inicial
   - Validação de duplicidade e estoque ≥ 0
   - Confirmação de sucesso ou erro

3. **Registrar Nota de Entrada**
   - Seleção do fornecedor e data da entrada
   - Associação de materiais e quantidades
   - Atualização automática do estoque
   - Confirmação de sucesso ou erro

4. **Criar Requisição de Material**
   - Cadastro de setor requisitante
   - Seleção de materiais e quantidades
   - Validação de estoque disponível
   - Status inicial: PENDENTE
   - Confirmação de sucesso ou erro

5. **Atender Requisição**
   - Seleção de requisições PENDENTES
   - Atualiza estoque subtraindo materiais atendidos
   - Alteração do status para ATENDIDA
   - Confirmação de sucesso ou erro

6. **Sair**
   - Encerra a aplicação de forma segura

---

## 🔹 Tecnologias Utilizadas
- **Java 17+**
- **JDBC** para conexão com MySQL
- **MySQL** (banco de dados)
- **IDE** recomendada: IntelliJ IDEA / Eclipse

---

## 🔹 Estrutura do Banco de Dados

```sql
-- Fornecedores
CREATE TABLE Fornecedor (
  id INT PRIMARY KEY AUTO_INCREMENT,
  nome VARCHAR(100) NOT NULL,
  cnpj VARCHAR(14) UNIQUE NOT NULL
);

-- Materiais
CREATE TABLE Material (
  id INT PRIMARY KEY AUTO_INCREMENT,
  nome VARCHAR(100) NOT NULL,
  unidade VARCHAR(20) NOT NULL,
  estoque DOUBLE NOT NULL
);

-- Requisições de Material
CREATE TABLE Requisicao (
  id INT PRIMARY KEY AUTO_INCREMENT,
  setor VARCHAR(50) NOT NULL,
  dataSolicitacao DATE NOT NULL,
  status VARCHAR(20) NOT NULL -- PENDENTE / ATENDIDA / CANCELADA
);

-- Itens da Requisição
CREATE TABLE RequisicaoItem (
  idRequisicao INT NOT NULL,
  idMaterial INT NOT NULL,
  quantidade DOUBLE NOT NULL,
  PRIMARY KEY (idRequisicao, idMaterial),
  FOREIGN KEY (idRequisicao) REFERENCES Requisicao(id),
  FOREIGN KEY (idMaterial) REFERENCES Material(id)
);

-- Notas de Entrada
CREATE TABLE NotaEntrada (
  id INT PRIMARY KEY AUTO_INCREMENT,
  idFornecedor INT NOT NULL,
  dataEntrada DATE NOT NULL,
  FOREIGN KEY (idFornecedor) REFERENCES Fornecedor(id)
);

-- Itens da Nota de Entrada
CREATE TABLE NotaEntradaItem (
  idNotaEntrada INT NOT NULL,
  idMaterial INT NOT NULL,
  quantidade DOUBLE NOT NULL,
  PRIMARY KEY (idNotaEntrada, idMaterial),
  FOREIGN KEY (idNotaEntrada) REFERENCES NotaEntrada(id),
  FOREIGN KEY (idMaterial) REFERENCES Material(id)
);
````

## 🔹 Estrutura do Projeto
```
src/  
 ├─ model/         # Classes Modelo (Fornecedor, Material, Requisicao, etc.)  
 ├─ dao/           # Classes DAO para CRUD no banco  
 ├─ util/          # Classes utilitárias (conexão, validações)  
 └─ Main.java      # Menu principal e fluxo do sistema
````
 
## 🔹 Regras de Negócio Importantes
✅ Cadastro de **fornecedor** e **material** não permite duplicidade.  
✅ **Estoque** não pode ser negativo.  
✅ Requisições só podem ser atendidas se houver estoque suficiente.  
✅ Status da requisição: **PENDENTE → ATENDIDA → CANCELADA** (se necessário).  
✅ Validações de entradas **nulas ou inválidas**.  
✅ Sistema funcional **sem `static`**, utilizando instâncias e objetos.  

---

## 🔹 Como Executar o Sistema
1. **Criar o banco de dados MySQL** e importar as tabelas fornecidas.
2. **Configurar a conexão** no arquivo `Conexao.java`:  
   
   ```java
   String url = "jdbc:mysql://localhost:3306/nomedobanco?useSSL=false&serverTimezone=UTC";
   String user = "root";
   String password = "senha";


## 🔹 Observações

- Desenvolvido como protótipo para WEG, simulando cenários corporativos.
- Aplicação contínua até escolha de “Sair” no menu.
- Código estruturado e legível, seguindo boas práticas de Java e POO.
