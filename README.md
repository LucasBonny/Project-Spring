# Spring Boot - Projeto Spring Boot

## Introdução

Nesse projeto iremos criar um projeto Spring Boot com as seguintes stacks:

- Spring Boot
- Spring Data JPA
- Spring Web
- H2
- PostgreSQL

## Criação do projeto com Spring Boot

Para a criação do projeto iremos utilizar o [Spring Initializr](https://start.spring.io/) que irá nos ajudar a criar o projeto de forma simples.

### Projeto

- Maven
- Java 17

### Dependências Iniciais

- Spring Web

Vamos seguir um modelo de domínio, onde iremos criar um recurso web para acessar os domínios e um banco de dados para manter a persistência dos dados.

## Criação de um modelo de domínio

Iremos criar a entidade para respeitar o fluxo do modelo na arquitetura do projeto.

- Entidade do domínio
- Repository
- Service
- Resource


### Implementação da entidade

Para a implementação da entidade iremos criar um pacote chamado `entity`, e dentro dele iremos criar uma classe chamada `User` e com isso podemos começar a fazer a implementação do domínio.

#### Modelo seguido na criação da entidade:

- Atributos
- Associações
- Construtores
- Getters and Setters
- Equals and HashCode
- Serializable

```java
public class User implements Serializable {

    private Long id;
    private String name;
    private String email;
    private String password;
    private String phone;

    public User() {
    }

    public User(Long id, String name, String email, String phone, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
    }
    // getters and setters

    // equals and hashCode
}
```

### Implementação do Resource

Para a implementação testar a entidade iremos criar um pacote chamado `resource`, e dentro dele iremos criar uma classe chamada `UserResource` e com isso podemos comecar a fazer a implementação do recurso web.

#### Modelo seguido na criação do recurso web:

- Anotação RestController
- Anotação RequestMapping
- Classe Resource

```java
@RestController
@RequestMapping(value = "/users")
public class UserResource {
    //criação de recuro web para acessar os usuários
    @GetMapping
    public ResponseEntity<User> findAll() {
        User user = new User(1L, "Lucas", "lucas@lucasbonny", "61966997755", "123456");
        return ResponseEntity.ok().body(user);
    }

}
```
#### Resposta da requisição GET
```json
{
  "id": 1,
  "name": "Lucas",
  "email": "lucas@lucasbonny",
  "phone": "61966997755",
  "password": "123456"
}
```

## Implementação do banco de dados H2

## Implementação do banco de dados PostgresSQL
