# API REST para Gerenciamento de Clientes - Desafio Técnico NeoApp

## 📖 Visão Geral

Esta é uma API RESTful completa desenvolvida em Java com Spring Boot como solução para o desafio técnico da NeoApp. A aplicação permite o gerenciamento de clientes pessoa física, implementando funcionalidades de CRUD, busca dinâmica, autenticação via JWT e um sistema de permissões baseado em papéis (Admin e Cliente).

O projeto foi construído seguindo as melhores práticas de mercado, com uma arquitetura limpa, código testável e foco na segurança, escalabilidade e portabilidade através do Docker.

---

## ✨ Funcionalidades Principais

* **Gerenciamento Completo de Clientes (CRUD):** Endpoints para criar, ler, atualizar e deletar clientes.
* **Segurança Robusta com JWT:** Autenticação baseada em token e autorização granular por papéis.
* **Permissões Baseadas em Papéis (Roles):**
    * **Admin:** Acesso total à API.
    * **Cliente:** Acesso restrito para gerenciar apenas seus próprios dados.
* **Busca Dinâmica e Paginada:** Endpoint de busca que permite a filtragem por múltiplos atributos.
* **Documentação Interativa com Swagger/OpenAPI:** Interface para visualizar e testar todos os endpoints.
* **Aplicação 100% Containerizada com Docker:** A API e o banco de dados PostgreSQL são gerenciados via Docker Compose, garantindo um ambiente consistente e fácil de executar.
* **Cobertura de Testes:** Testes unitários (Mockito) e de integração (MockMvc) para garantir a qualidade do código.

---

## 🛠️ Tecnologias Utilizadas

* **Linguagem:** Java 17
* **Framework:** Spring Boot 3
* **Persistência:** Spring Data JPA / Hibernate
* **Banco de Dados:** PostgreSQL
* **Containerização:** Docker / Docker Compose
* **Segurança:** Spring Security (autenticação com JWT)
* **Validação:** Jakarta Bean Validation
* **Documentação:** Springdoc OpenAPI (Swagger UI)
* **Build Tool:** Maven
* **Testes:** JUnit 5, Mockito
* **Ferramenta de Testes de API:** Postman
* **Utilitários:** Lombok

---

## 🚀 Como Executar o Projeto Localmente

### Pré-requisitos
Graças ao Docker, a configuração do ambiente é mínima. Você só precisa ter:
* [Docker Desktop](https://www.docker.com/products/docker-desktop/)

**Não é necessário ter Java ou Maven instalados na sua máquina!** O `Dockerfile` multi-stage cuida de todo o processo de build da aplicação em um ambiente isolado.

### Passo a Passo

**1. Clone o Repositório:**
```bash
git clone https://github.com/erfonspanos/neoapp-desafio-tecnico.git
cd neoapp-desafio-tecnico
```

**2. Execute a Aplicação com Docker Compose:**
Certifique-se de que o Docker Desktop esteja em execução. No terminal, na raiz do projeto, execute o comando:
```bash
docker-compose up --build
```
* **O que este comando faz?**
    * **`--build`**: Constrói a imagem Docker da aplicação Spring Boot a partir do `Dockerfile`.
    * **`up`**: Inicia todos os serviços definidos no `docker-compose.yml` (a API e o banco de dados) e os conecta em uma mesma rede.

Aguarde a finalização do build e a inicialização dos containers. Você verá os logs de ambos os serviços no seu terminal. Quando o log do Spring Boot indicar que a aplicação foi iniciada, a API estará pronta para uso.

A aplicação estará disponível em `http://localhost:8080`.

**Para rodar em segundo plano (detached mode), use:**
```bash
docker-compose up --build -d
```

**Para parar todos os containers:**
```bash
docker-compose down
```

---

##  API Endpoints

A documentação interativa completa está disponível via Swagger UI após iniciar a aplicação:
* **[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)**

Um usuário **Administrador** padrão é criado automaticamente (`DataSeeder`) para facilitar os testes:
* **Email:** `admin@neoapp.com`
* **Senha:** `admin123`

### Autenticação de Usuário Admin (`/auth`)

#### `POST /auth/login`
* **Descrição:** Autentica um usuário (Admin ou Cliente) e retorna um token JWT.
* **Permissão:** Público.
* **Request Body:**
    ```json
    {
        "email": "admin@neoapp.com",
        "senha": "admin123"
    }
    ```
* **Success Response (200 OK):**
    ```json
    {
        "token": "eyJhbGciOiJIUzI1NiJ9..."
    }
    ```

### Gerenciamento de Usuários (`/usuarios`)

#### `POST /usuarios/admin`
* **Descrição:** Cria uma nova conta de usuário com permissão de Administrador.
* **Permissão:** `ADMIN`.
* **Request Body:**
    ```json
    {
        "email": "novo.admin@neoapp.com",
        "senha": "novaSenhaAdmin456"
    }
    ```
* **Success Response (201 Created):** Resposta vazia.

### Gerenciamento de Clientes (`/clientes`)

#### `POST /clientes`
* **Descrição:** Cria um novo perfil de cliente. **Nota:** Esta ação não cria a conta de usuário associada.
* **Permissão:** `ADMIN`.
* **Request Body:** `ClienteRequestDTO` (contendo nome, cpf, email, etc.).
   ```json
    {
      "nome": "Carla Vieira",
      "cpf": "12312312311",
      "email": "carla.vieira@email.com",
      "telefone": "85912345678",
      "dataNascimento": "1995-10-08",
      "endereco": { "cep": "60123456", "logradouro": "Avenida Beira Mar", "numero": "3000", "bairro": "Meireles", "cidade": "Fortaleza", "estado": "CE" }
   }
    ```
* **Success Response (201 Created):** Retorna os dados do cliente recém-criado.

#### `GET /clientes`
* **Descrição:** Lista todos os clientes de forma paginada.
* **Permissão:** `ADMIN`.
* **Query Params:** `page`, `size`, `sort` (ex: `?page=0&size=10&sort=nome,asc`).
* **Success Response (200 OK):** Retorna um objeto `Page` com a lista de clientes.

#### `GET /clientes/buscar` ou `/clientes/buscar?cidade=... | /buscar?nome=...`
* **Descrição:** Realiza uma busca dinâmica e paginada por múltiplos atributos.
* **Permissão:** `ADMIN`.
* **Query Params:** `nome`, `cpf`, `email`, `cidade`, etc.
* **Success Response (200 OK):** Retorna um objeto `Page` com os clientes que correspondem aos filtros.

#### `GET /clientes/{id}`
* **Descrição:** Busca um cliente específico pelo seu ID.
* **Permissão:** `ADMIN` ou `CLIENTE` (apenas se o ID for o seu próprio).
* **Success Response (200 OK):** Retorna os dados do cliente encontrado.

#### `PUT /clientes/{id}`
* **Descrição:** Atualiza os dados de um cliente específico.
* **Permissão:** `ADMIN` ou `CLIENTE` (apenas se o ID for o seu próprio).
* **Request Body:** `ClienteRequestDTO`.
* **Success Response (200 OK):** Retorna os dados atualizados do cliente.

#### `DELETE /clientes/{id}`
* **Descrição:** Exclui um cliente e o usuário associado a ele.
* **Permissão:** `ADMIN` ou `CLIENTE` (apenas se o ID for o seu próprio).
* **Success Response (204 No Content):** Resposta vazia.

### Autenticação de Usuários Cliente (`/auth`)

#### FAZER ESSE PASSO SOMENTE APOS O POST DO CLIENTE!
#### `POST /auth/register`
* **Descrição:** Permite que um cliente já cadastrado (por um admin) crie sua conta de usuário, definindo uma senha. (Senha é criada na hora do registro do usuário cliente, pelo próprio cliente, email é o que o admin cadastrou do cliente)
* **Permissão:** Público.
* **Request Body:**
    ```json
    {
        "email": "carla.vieira@email.com",
        "senha": "cliente123"
    }
    ```
* **Success Response (201 Created):** Retorna os dados do perfil do cliente recém-registrado.

#### `POST /auth/login`
* **Descrição:** Permite que um usuário ja cadastrado como cliente faça a autenticação e receba seu token (necessario passar como Bearer token no Postman para teste)
* **Permissão:** Público.
* **Request Body:**
    ```json
    {
        "email": "carla.vieira@email.com",
        "senha": "cliente123"
    }
    ```
* **Success Response (200 OK):**
    ```json
    {
        "token": "eyJhbGciOiJIUzI1NiJ9..."
    }
    ```

---

## 🧠 Processo Criativo e Decisões de Arquitetura

Eu iniciei o projeto criando toda a estrutura principal e obrigatória para o desafio, por ser a maior prioridade. Após desenvolver o CRUD principal da aplicação (que por sinal não levou mais de 2 dias), decidi focar em implementar algo a mais, o sistema de usuários com roles, onde a logica mudou um pouco, mas sempre me mantive focado em preservar a regra de negócios central: o ADMIN gerencia os dados dos CLIENTES, e um CLIENTE só pode gerenciar a si mesmo.

Apareceu um bug durante o processo de o usuario cliente apagar sua propria conta, mas após ver o erro, com uma analise minunciosa no console, no código e no banco de dados, consegui descobrir o que estava causando o erro. Era a conexão direta que as duas classes (usuário e cliente) tinham entre si, a foreign key de `id_cliente`, que ao ser apagada estava proibindo que o usuario fosse apagado. Entao ajustei a logica para que isso fosse ajeitado.

No fim, foi um ótimo lembrete de que, com um pouco de paciência, tudo dá certo 😅

Este projeto foi desenvolvido com foco em criar uma base de código limpa, segura e escalável, seguindo os princípios do SOLID e as melhores práticas do ecossistema Spring.

* **Arquitetura em Camadas:** A aplicação foi dividida nas camadas clássicas (Controller, Service, Repository) para garantir uma clara separação de responsabilidades. A camada de Controller lida apenas com o tráfego HTTP, a de Service contém toda a lógica de negócio, e a de Repository abstrai o acesso aos dados.

* **Padrão DTO (Data Transfer Object):** Foi adotado o uso de DTOs (`Request` e `Response`) para desacoplar o contrato da API do modelo de persistência. Isso oferece segurança (não expondo dados internos), flexibilidade (formatando a resposta, como no cálculo da idade) e um contrato de API estável.

* **Separação de `Usuario` e `Cliente`:** A decisão de ter duas entidades separadas foi crucial. `Cliente` representa os dados cadastrais (o "o quê"), enquanto `Usuario` representa a conta de acesso (o "quem"). Isso permite um modelo muito mais flexível, onde um `Usuario` (como o Admin) pode existir sem um perfil de `Cliente`, alinhando-se com a regra de negócio "nem todo usuário é um cliente".

* **Busca Dinâmica com Specifications:** Em vez de poluir o repositório com múltiplos métodos `findBy...`, foi utilizado o padrão Specification do JPA. Isso centraliza a lógica de filtragem em uma única classe (`ClienteSpecification`) e permite a construção de consultas complexas e dinâmicas de forma limpa e reutilizável.

* **Segurança em Profundidade:** A segurança foi implementada em duas camadas:
    1.  **`SecurityConfig`:** Define regras globais baseadas em URL e verbo HTTP (ex: apenas Admins podem fazer `POST` em `/clientes`).
    2.  **`@PreAuthorize`:** Adiciona uma verificação fina a nível de método para regras complexas (ex: um cliente só pode deletar seu próprio perfil).

* **Tratamento de Exceções Centralizado:** Um `@ControllerAdvice` (`ResourceExceptionHandler`) foi implementado para capturar exceções lançadas pela aplicação e traduzi-las em respostas de erro HTTP padronizadas e amigáveis, melhorando a experiência do consumidor da API.

---

## 🔮 Próximos Passos e Melhorias Futuras

* **Cache:** Implementar uma camada de cache (ex: com Redis) para otimizar consultas frequentes e melhorar a performance.
* **Testes de Integração Contínua (CI/CD):** Configurar um pipeline de CI/CD (ex: com GitHub Actions) para automatizar a execução dos testes e o processo de deploy.
