# API REST para Gerenciamento de Clientes - Desafio Técnico NeoApp

## 📖 Visão Geral

Esta é uma API RESTful completa desenvolvida em Java com Spring Boot como solução para o desafio técnico da NeoApp. A aplicação permite o gerenciamento de clientes pessoa física, implementando funcionalidades de CRUD, busca dinâmica, autenticação via JWT e um sistema de permissões baseado em papéis (Admin e Cliente).

O projeto foi construído seguindo as melhores práticas de mercado, com uma arquitetura limpa, código testável e foco na segurança e escalabilidade.

---

## ✨ Funcionalidades Principais

* **Gerenciamento Completo de Clientes (CRUD):** Endpoints para criar, ler, atualizar e deletar clientes.
* **Segurança Robusta com JWT:** Autenticação baseada em token e autorização granular.
* **Permissões Baseadas em Papéis (Roles):**
    * **Admin:** Acesso total à API, incluindo a criação de clientes e listagem de todos os usuários.
    * **Cliente:** Acesso restrito para visualizar, editar e apagar apenas seus próprios dados.
* **Busca Dinâmica e Paginada:** Endpoint de busca poderoso que permite a filtragem por múltiplos atributos cadastrais com resultados paginados.
* **Documentação Interativa com Swagger/OpenAPI:** Interface de usuário para visualizar e testar todos os endpoints da API.
* **Ambiente de Desenvolvimento com Docker:** Banco de dados PostgreSQL gerenciado via Docker Compose para garantir consistência e facilidade na configuração.
* **Cobertura de Testes:** Testes unitários e de integração para garantir a qualidade e o funcionamento correto da aplicação.

---

## 🛠️ Tecnologias Utilizadas

* **Linguagem:** Java 17
* **Framework:** Spring Boot 3
* **Persistência:** Spring Data JPA / Hibernate
* **Banco de Dados:** PostgreSQL (gerenciado com Docker)
* **Segurança:** Spring Security (autenticação com JWT)
* **Validação:** Jakarta Bean Validation
* **Documentação:** Springdoc OpenAPI (Swagger UI)
* **Build Tool:** Maven
* **Testes:** JUnit 5, Mockito, Spring Boot Test
* **Utilitários:** Lombok

---

## 🚀 Como Executar o Projeto Localmente

### Pré-requisitos
Antes de começar, garanta que você tenha as seguintes ferramentas instaladas:
* [JDK 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) ou superior
* [Docker Desktop](https://www.docker.com/products/docker-desktop/)

### Passo a Passo

**1. Clone o Repositório:**
```bash
git clone https://github.com/erfonspanos/neoapp-desafio-tecnico.git
cd api-clientes
```

**2. Inicie o Banco de Dados:**
Certifique-se de que o Docker Desktop esteja em execução. No terminal, na raiz do projeto, execute:
```bash
docker-compose up -d
```
Este comando irá iniciar um container PostgreSQL com um banco de dados pré-configurado e um volume para persistir os dados.

**3. Execute a Aplicação Spring Boot:**
No mesmo terminal, execute o wrapper do Maven para iniciar a aplicação:
```bash
./mvnw spring-boot:run
```
A aplicação iniciará na porta `8080`. Ao ser executada com o perfil `dev`, um usuário **Administrador** padrão será criado automaticamente (`DataSeeder`):
* **Email:** `admin@neoapp.com`
* **Senha:** `admin123`

---

##  API Endpoints

A documentação interativa completa está disponível via Swagger UI após iniciar a aplicação:
* **[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)**

### Autenticação (`/auth`)

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

#### `POST /auth/register`
* **Descrição:** Permite que um cliente já cadastrado (por um admin) crie sua conta de usuário, definindo uma senha.
* **Permissão:** Público.
* **Request Body:**
    ```json
    {
        "email": "cliente.cadastrado@email.com",
        "senha": "senhaForte123"
    }
    ```
* **Success Response (201 Created):** Retorna os dados do perfil do cliente recém-registrado.

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
* **Success Response (201 Created):** Retorna os dados do cliente recém-criado.

#### `GET /clientes`
* **Descrição:** Lista todos os clientes de forma paginada.
* **Permissão:** `ADMIN`.
* **Query Params:** `page`, `size`, `sort` (ex: `?page=0&size=10&sort=nome,asc`).
* **Success Response (200 OK):** Retorna um objeto `Page` com a lista de clientes.

#### `GET /clientes/buscar`
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

---

## 🧠 Decisões de Arquitetura (Processo Criativo)

Este projeto foi desenvolvido com foco em criar uma base de código limpa, segura e escalável, seguindo os princípios do SOLID e as melhores práticas do ecossistema Spring.

* **Arquitetura em Camadas:** A aplicação foi dividida nas camadas clássicas (Controller, Service, Repository) para garantir uma clara separação de responsabilidades. A camada de Controller lida apenas com o tráfego HTTP, a de Service contém toda a lógica de negócio, e a de Repository abstrai o acesso aos dados.

* **Padrão DTO (Data Transfer Object):** Foi adotado o uso de DTOs (`Request` e `Response`) para desacoplar o contrato da API do modelo de persistência. Isso oferece segurança (não expondo dados internos), flexibilidade (formatando a resposta, como no cálculo da idade) e um contrato de API estável.

* **Separação de `Usuario` e `Cliente`:** A decisão de ter duas entidades separadas foi crucial. `Cliente` representa os dados cadastrais (o "o quê"), enquanto `Usuario` representa a conta de acesso (o "quem"). Isso permite um modelo muito mais flexível, onde um `Usuario` (como o Admin) pode existir sem um perfil de `Cliente`, alinhando-se com a regra de negócio "nem todo usuário é um cliente".

* **Busca Dinâmica com Specifications:** Em vez de poluir o repositório com múltiplos métodos `findBy...`, foi utilizado o padrão Specification do JPA. Isso centraliza a lógica de filtragem em uma única classe (`ClienteSpecification`) e permite a construção de consultas complexas e dinâmicas de forma limpa e reutilizável.

* **Segurança em Profundidade:** A segurança foi implementada em duas camadas:
    1.  **`SecurityConfig`:** Define regras globais baseadas em URL e verbo HTTP (ex: apenas Admins podem fazer `POST` em `/clientes`).
    2.  **`@PreAuthorize`:** Adiciona uma verificação fina a nível de método para regras complexas (ex: um cliente só pode deletar seu próprio perfil).

* **Tratamento de Exceções Centralizado:** Um `@ControllerAdvice` (`ResourceExceptionHandler`) foi implementado para capturar exceções lançadas pela aplicação e traduzi-las em respostas de erro HTTP padronizadas e amigáveis, melhorando a experiência do consumidor da API.

Meu foco inicial no projeto foi criar toda a estrutura principal e obrigatória para o desafio, por entender que era a maior prioridade. Após desenvolver o CRUD principal da aplicação, o que por sinal não levou mais de 1 dia, decidi focar em implementar algo a mais para realmente elevar a qualidade do projeto: um sistema de Usuarios com Roles (Administrador e Cliente).

Essa decisão mudou um pouco a lógica, mas sempre me mantive focado em preservar a regra de negócios central: o ADMIN gerencia os dados dos CLIENTES, e um CLIENTE só pode gerenciar a si mesmo.

Durante o processo, um dos desafios mais interessantes apareceu: um bug impedia que um usuário do tipo CLIENTE apagasse a sua própria conta, retornando um erro de acesso negado. Para resolvê-lo, com uma análise minuciosa em três frentes – o console da aplicação para entender a exceção, o código para rastrear o fluxo e o banco de dados para observar o comportamento das tabelas – consegui descobrir o que estava causando o erro.

A causa era a conexão direta que as duas classes (Usuario e Cliente) tinham entre si. A foreign key de cliente_id na tabela de usuários impedia que o registro do cliente fosse apagado primeiro. Para resolver, ajustei a lógica na camada de serviço para que, ao solicitar a exclusão, o sistema primeiro remova o Usuario associado e só então delete o Cliente, respeitando a ordem de integridade dos dados.

**No fim, foi um ótimo lembrete de que, com um pouco de paciência, tudo da certo 😅**
---

## 🔮 Próximos Passos e Melhorias Futuras

* **Dockerizar a Aplicação:** Criar um `Dockerfile` para a aplicação Spring Boot para que todo o ambiente (API + Banco) possa ser orquestrado com um único comando `docker-compose up`.
* **Deploy na Nuvem:** Publicar a aplicação em um provedor de nuvem (Heroku, Render, AWS) para torná-la acessível publicamente.
* **Cache:** Implementar uma camada de cache (ex: com Redis) para otimizar consultas frequentes.
