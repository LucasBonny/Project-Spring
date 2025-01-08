# Spring Boot - Projeto Spring Boot

## Introdução

Nesse primeiro projeto spring iremos aprender a utilizar o Spring Boot para fazer o desenvolvimento de uma aplicação Spring com as seguintes tecnologias:

- Spring Boot
- Spring Data JPA
- Spring Web
- H2
- PostgreSQL

O conteúdo deste projeto foi retirado do curso `Java COMPLETO Programação Orientada a Objetos + Projetos`
 do _Nélio Alves_ na [Udemy](https://www.udemy.com/course/java-curso-completo/).
 
A escrita desse documento foi feita por [Lucas Bonny](https://github.com/lucasbonny) para futuras consultas, e esse projeto está de acordo com o material disponibilizado pelo professor.

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

Para a implementação testar a entidade iremos criar um pacote chamado `resource`, e dentro dele iremos criar uma classe chamada `UserResource` e com isso podemos começar a fazer a implementação do recurso web.

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

### Instalando o banco de dados H2

O banco de dados H2 será utilizado para fazer testes na aplicação Spring com salvamento em memória para facilitar o desenvolvimento.

#### Dependências que serão utilizadas
```xml
<!-- Ferramenta de mapeamento ORM -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>

<!-- Driver do banco de dados H2 -->
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>runtime</scope>
</dependency>
```

### Configurando um perfil de testes no projeto

Agora iremos ao diretório `src/main/resource/application.properties` e iremos adicionar a seguinte configuração:

```properties
# Definindo o perfil de desenvolvimento
spring.profiles.active=test 
spring.jpa.open-in-view=true
```
Iremos criar o arquivo `application-test.properties` na mesma pasta e iremos adicionar a seguinte configuração:

```properties
# DATASOURCE
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.username=sa
spring.datasource.password=
# H2 CLIENT
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
# JPA, SQL
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.defer-datasource-initialization=true
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

```
### Mapeamento da entidade para o banco de dados

Agora iremos mapear a entidade User para instruir o JPA de como ele vai converter os objetos para o modelo relacional, e como fazemos isso? Iremos entrar na entidade User e iremos adicionar as seguintes anotações:

```java
@Entity 
@Table(name = "tb_user") // NOME DA TABELA
public class User implements Serializable {
    @Id // PRIMARY KEY
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String password;
    // ...
}
```
Precisamos alterar o nome da tabela pois o nome `User` é um termo reservado do banco de dados, então iremos alterar para `tb_user`.

Agora iremos verificar se o banco de dados foi criado com sucesso, para isso iremos acessar o link `http://localhost:8080/h2-console` e entrar com o nome de `sa` e a senha vazia.

Ao entrar iremos ver a seguinte tela:

![result](assets/image.png)

Implementação do banco de dados H2 foi concluida com sucesso!

## Implementação do UserRepository

Iremos criar um pacote chamado `repositories` e dentro dele iremos criar uma interface chamada `UserRepository` e com isso podemos começar a fazer a implementação.

```java
//JpaRepository<Tipo da entidade, Tipo do ID>
public interface UserRepository extends JpaRepository<User, Long> {

}
```

> [!TIP]
> Na classe `UserRepository` se torna opcional o uso da anotação `@Repository` pois ele estende a interface `JpaRepository` que já está registrado como um componente do Spring.

## Implementação da classe de configuração de testes

Iremos criar um pacote chamado `config` e dentro dele iremos criar uma classe chamada `TestConfig` e com isso podemos começar a semear o banco de dados.

> [!IMPORTANT]
> Para semear o banco de dados iremos utilizar o `CommandLineRunner` que irá executar o código ao iniciar o projeto.

```java
@Configuration
@Profile("test")
public class TestConfig implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {

        User u1 = new User(null, "Maria Brown", "maria@gmail.com", "988888888", "123456");
        User u2 = new User(null, "Alex Green", "alex@gmail.com", "977777777", "123456");

        userRepository.saveAll(Arrays.asList(u1, u2));
    }
}
```
### Resultado:

![result](assets/image-1.png)


## Implementação do UserService e delegação de responsabilidades

Iremos seguir o fluxo de implementação do `UserService` conforme o diagrama abaixo:

![result](assets/image-2.png)

Devemos separar as responsabilidades para facilitar a manutenção do projeto. A classe `UserResource` irá receber as requisções e enviar as respostas para o `UserService`, já o `UserService` é responsável pela regra de negócio, e o `UserRepository` irá fazer o acesso ao banco de dados.

Agora vamos criar um pacote chamado `services` e dentro dele iremos criar uma classe chamada `UserService` e com isso podemos comenzar a fazer a implementação.

```java
@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    public List<User> findAll() {
        return repository.findAll();
    }

    public User findById(Long id) {
        Optional<User> obj = repository.findById(id);
        return obj.get();
    }
}
```

Agora iremos atualizar a classe `UserResource` para que ele possa retornar os dados do banco de dados para a rota `/users`.

```java
@RestController
@RequestMapping(value = "/users")
public class UserResource {

    @Autowired
    private UserService service;

    @GetMapping
    public ResponseEntity<List<User>> findAll() {
        List<User> list = service.findAll();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<User> findById(@PathVariable Long id) {
        User obj = service.findById(id);
        return ResponseEntity.ok().body(obj);
    }
}
```
### Resultado:

![result](assets/image-3.png)

> [!IMPORTANT]
> Para que a injeção de dependência funcione devemos registrar a classe `UserService` como um componente do Spring, utilizando a anotação `@Service` ou `@Component`.
> já na classe `UserRepository` se torna opcional pois ele estende a interface `JpaRepository` que já está registrado como um componente do Spring.

## Classe Order

![result](assets/image-4.png)

A classe `Order` está associada a classe `User` onde 1 `User` pode ter várias `Order` e 1 `Order` pode ter 1 `User`, então iremos criar uma classe chamada `Order` e fazer a associação.

### Ordem de implementação


- Entity
- "To many" association, lazy loading(Carregamento "Preguiçoso"), JsonIgnore
- Repository
- Seed
- Service
- Resource

> [!TIP]
> As coleções é criado somente o Getter sem o Setter.

```java
@Entity
@Table(name = "tb_order")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date moment;
    @ManyToOne
    @JoinColumn(name = "client_id")
    private User client;
    public Order() {
    }
    public Order(Long id, Date moment, User client) {
        this.id = id;
        this.moment = moment;
        this.client = client;
    }
    // getters and setters
}
```

> [!TIP] 
> As coleções deverão ser instanciadas com `new ArrayList<>()` para seu uso.

Utilizando a anotação `@ManyToOne`, indicamos que o atributo `client` é uma chave estrangeira da tabela `tb_order`, referenciando a tabela `tb_client`.

```java
//Arquivo: User.java
@OneToMany(mappedBy = "client")
private List<Order> orders = new ArrayList<>();
```

> [!IMPORTANT]
> Ao fazer a associação com o `@ManyToOne`, o banco de dados irá criar uma nova coluna na tabela `tb_order` para armazenar o ID do `User` que está sendo referenciado, mas se caso eu queira saber os pedidos de um determinado `User` precisaremos mapear na classe `User` a referência ao `List<Order> orders` usando o MappedBy informando o nome do atributo `client`.

#### Seed

Após ter criado o domínio de `Order` iremos criar o seed para popular o banco de dados com os dados de exemplo.

```java
 // injeção de dependência
@Autowired
private OrderRepository orderRepository;

// Dados de exemplo
Order o1 = new Order(null, Instant.parse("2019-06-20T19:53:07Z"), u1);
Order o2 = new Order(null, Instant.parse("2019-07-21T03:42:10Z"), u2);
Order o3 = new Order(null, Instant.parse("2019-07-22T15:21:22Z"), u1); 

orderRepository.saveAll(Arrays.asList(o1, o2, o3));
```

Agora podemos fazer uma requisição GET para a rota `/orders` e ver os dados de exemplo.

![result](assets/image-5.png)

Como podemos ver, os dados de order foram criados com sucesso, mas ao fazer a requisição GET para a rota `/users` ou `/orders` iremos ver os dados em loop. Isso acontece pois a associação foi feita solicitando os dados em ambos os lados. E para resolver isso iremos utilizar o `@JsonIgnore` para evitar a serialização dos dados em um dos lados.

```java
//Arquivo: User.java
@JsonIgnore
@OneToMany(mappedBy = "client")
private List<Order> orders = new ArrayList<>();
```
![result](assets/image-6.png)

Agora podemos ver que os dados veio corretamente.

O lado "para muitos" (`To Many`) não buscará os dados por padrão devido ao carregamento preguiçoso (lazy loading). O processo de serialização, realizado pelo Jackson, não irá automaticamente acionar o banco de dados para buscar esses dados adicionais caso a sessão já tenha sido fechada. Para permitir que esses dados sejam buscados durante a serialização, podemos utilizar a configuração abaixo:

```properties
spring.jpa.open-in-view=true
```
Se essa configuração for desabilitada, a sessão do banco de dados será encerrada antes da serialização, e acessar os dados adicionais resultará em uma exceção.

Para garantir que o meu Instant seja serializado com o formato ISO 8601, iremos utilizar a seguinte configuração:

```java
@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "GMT")
private Instant moment;
```

## Implementação do OrderStatus

O OrderStatus é uma Enum, então iremos criar um pacote chamado `entities/enums` e dentro dele iremos criar uma classe chamada `OrderStatus` e com isso podemos iniciar a implementação.

Primeiramente é importante dizer que somente informando os dados da Enum irá dar certo, mas para evitar que os valores futuramente sejam alterados iremos utilizar o construtor privado `private OrderStatus(int code)` e o getter `public int getCode()` para pegar o valor. O atributo `code` é o valor que irá ser utilizado para identificar o status do pedido.

> [!IMPORTANT]
> Uma Enum por sempre ter valores estáticos poderão ser acessados apenas chamando pela classe sem precisar instantiá-la. 

```java
public enum OrderStatus {
    WAITING_PAYMENT(1),
    PAID(2),
    SHIPPED(3),
    DELIVERED(4),
    CANCELED(5);
    private int code;
    private OrderStatus(int code) {
        this.code = code;
    }
    public int getCode() {
        return code;
    }
    public static OrderStatus valueOf(int code) {
        for(OrderStatus o : OrderStatus.values()) {
            if(o.getCode() == code) return o;
        }
        throw new IllegalArgumentException("Invalid OrderStatus code!");
    }
}
```

> [!TIP]
> O `valueOf` serve para converter o valor informado para o Enum.

Para lidar com o OrderStatus iremos criar um atributo chamado `orderStatus` do tipo Integer na classe `Order` e criaremos um mapeamento para converter para o exterior o atributo `orderStatus` como o Enum `OrderStatus`.

```java
private Integer orderStatus;

public Order(Long id, Date moment, OrderStatus orderStatus, User client) {
    this.id = id;
    this.moment = moment;
    this.orderStatus = orderStatus.getCode();
    this.client = client;
}

public OrderStatus getOrderStatus() {
    return OrderStatus.valueOf(orderStatus);
}
public void setOrderStatus(OrderStatus orderStatus) {
    this.orderStatus = orderStatus.getCode();
}
```

Dessa forma para o mundo externo iremos trabalhar com o Enum `OrderStatus`, e para a classe `Order` iremos trabalhar com o Integer.

## Implementação do Product

### HashSet

A implementação da entidade Product foi feita com o mesmo padrão de implementação do domínio.

O atributo categories adicionado para a classe Product ele irá representar um coleção, garantindo que "Um produto não possa ter mais de uma ocorrência de uma mesma categoria".

```java
    Set<Category> categories = new HashSet<>();
    //A consulta equals está configurada para comparar somente o ID
    categories.add(new Category(1L, "Electronics"));
    categories.add(new Category(3L, "Computers"));
    categories.add(new Category(1L, "Electronics"));

    // como o set não aceita repetição, ele irá ignorar a segunda ocorrencia de "Electronics"
    // e com isso ele irá retornar apenas 1 ocorrência do mesmo.
    System.out.println(categories.size()); // 2
```

> [!IMPORTANT]
> A consulta do HashSet utiliza o equals para verificar se o elemento já existe na coleção, se o equals retornar true ele irá ignorar a ocorrência.

#### Anotação @Transient

A anotação `@Transient` serve para indicar ao JPA que o atributo `categories` não deverá ser serializado, ou seja, ele irá ser ignorado na hora de fazer a serialização.

```java
@Transient
private Set<Category> categories = new HashSet<>();
```

## Associação de muitos para muitos

### Lado Product

1. Escolher uma classe: `Category` ou `Product` para ter a associação de muitos para muitos.
2. Dentro da classe vai ter uma coleção `Set` para armazenar as associações.
3. Criar o mapeamento para a associação.

```java
//Como o Product irá ter muitas categorias, e o Category irá ter muitos produtos, iremos utilizar o relacionamento muitos para muitos.
@ManyToMany
//No JoimColum o name é o nome que irá ser utilizado na tabela de relacionamento que irá receber as duas chaves estrangeiras.
@JoinTable(name = "tb_product_category", joinColumns = @JoinColumn(name = "product_id"), inverseJoinColumns = @JoinColumn(name = "category_id"))
private Set<Category> categories = new HashSet<>();
```

- O conceito sobre o joinColumns é o mesmo que o @ManyToOne
- Na classe de Order, o pedido tem um cliente, então iremos utilizar o `@ManyToOne` para indicar que o atributo `client` é uma chave estrangeira da tabela `tb_order`, referenciando a tabela `tb_client`.

### Lado Category

Agora na classe reversa iremos mapear a associação entre as duas tabelas.

```java
@ManyToMany(mappedBy = "categories")
private Set<Product> products = new HashSet<>();
```

## Associação de um para muitos com dados extras


![result](assets/image-7.png)

A relação entre o Order e o Product temos uma associação com OrderItem. Então iremos começar pelo OrderItemPK.

### OrderItemPK

1. Crie o pacote em `entities/pk`
2. Crie a classe `OrderItemPK` que irá receber as classes `Product` e `Order` que comporão o OrderItem.

> [!TIP]
> A classe `OrderItemPK` não precisa de construtores.

```java
@Embeddable
public class OrderItemPK implements Serializable {
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    //Getters e Setters

    //HashCode e Equals
}
```

> [!IMPORTANT]
> O HashCode e Equals devem ser implementados comparando os atributos `order` e `product`.

### OrderItem

1. Crie a classe `OrderItem` no pacote em `entities`.
2. A classe `OrderItem` irá receber a classe `OrderItemPK` com a anotação `@EmbeddedId`.
3. Construa a classe `OrderItem` com os atributos `quantity` e `price`.
4. Criar o mapeamento para a classe.

```java
@Entity
@Table(name = "tb_order_item")
public class OrderItem implements Serializable {
    @EmbeddedId
    private OrderItemPK id = new OrderItemPK();
    private Integer quantity;
    private Double price;
    public OrderItem() {
    }
    public OrderItem(Order order, Product product, Integer quantity, Double price) {
        id.setOrder(order);
        id.setProduct(product);
        this.quantity = quantity;    
        this.price = price;
    }

    public void setOrder(Order order){
        id.setOrder(order);
    }
    public Order getOrder(){
        return id.getOrder();
    }
    public void setProduct(Product product){
        id.setProduct(product);
    }
    public Product getProduct(){
        return id.getProduct();
    }

    //HashCode e Equals
}
```
Para implementações externas iremos passar os dados do Order e do Product para o OrderItem e na classe `OrderItemPK` iremos receber esses dados como parâmetro do construtor. 

> [!IMPORTANT]
> O objeto `OrderItemPK` terá que ser inicializado no atributo para evitar que receba um `NullPointerException`.

## Mapeamento na Classe Order

Primeiro iremos criar uma coleção para armazenar os dados do OrderItem, e pelo fato de ser um `@EmbeddedId`, o OrderItemPK que terá os dados do Order e Product, logo teremos que criar o mapeamento para a associação de outra forma.

```java
//Como Order está dentro da classe OrderItemPK, iremos colocar o mapeamento para dentro da classe OrderItemPK
@OneToMany(mappedBy = "id.order")
private Set<OrderItem> items = new HashSet<>();
```

Com isso, já podemos semear o banco de dados com os dados de exemplo.

```java
OrderItem oi1 = new OrderItem(o1, p1, 2, p1.getPrice());
OrderItem oi2 = new OrderItem(o1, p3, 1, p3.getPrice());
OrderItem oi3 = new OrderItem(o2, p3, 2, p3.getPrice());
OrderItem oi4 = new OrderItem(o3, p5, 2, p5.getPrice());

orderItemRepository.saveAll(Arrays.asList(oi1, oi2, oi3, oi4));
```

E com isso teremos os dados de exemplo na tabela `tb_order_item`.

![result](assets/image-8.png)

E o OrderItem irá receber os dados de Product e Order mas iremos utilizar o `@JsonIgnore` na classe `OrderItem` para evitar que retorne os valores já presente na Order o deixando em loop.

> [!TIP]
> Quando o jackson serializa uma classe ele busca todos os getters e organiza eles em um JSON.

```java
@JsonIgnore
public Order getOrder() {
    return id.getOrder();
}
```

Dessa forma ao fazer a requisição GET para a rota `/orders/2` iremos ver os dados de exemplo.

![result](assets/image-9.png)

## Associação entre Product e OrderItem

A princípio iremos criar a associação entre Product e OrderItem, e para isso iremos utilizar o `@OneToMany` mas não iremos serializar os dados de OrderItem, pois não será necessário. Então iremos apenas declarar o getter para retornar os dados de OrderItem caso precise utilizar em algum outro momento.

```java
@OneToMany(mappedBy = "id.product")
Set<OrderItem> orders = new HashSet<>();
//...
@JsonIgnore
public Set<OrderItem> getOrders() {
    Set<OrderItem> set = new HashSet<>();
    for(OrderItem o : orders) {
        set.add(o);
    }
    return set;
}
```
Podemos não ter utilizado em primeiro momento mas se for necessário iremos utilizar chamando o getter `getOrders()` e ele irá percorrer o `Set` e irá retornar os Order relacionados ao `Product` atual.

## Associação um para um

![result](assets/image-10.png)

O pagamento está associado ao pedido, então podemos ver que um pedido pode ter 0 ou 1 pagamento, logo conseguimos indentificar quem é a classe dependente.

```java
// Lado Order
@OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
private Payment payment;
```

E o pagamento tem apenas 1 pedido.

```java
// Lado Payment
@ManyToOne
@MapsId
private Order order;
```

Nessa associação estamos mapeando as duas entidades para que ambas tenham o mesmo ID, e com a relação de 1 para 1 devemos colocar o cascade para que o pagamento seja deletado junto com o pedido.

### Semeando no banco

```java
    Order o1 = new Order(null, Instant.parse("2019-06-20T19:53:07Z"), OrderStatus.PAID, u1);

    Payment pay1 = new Payment(null, Instant.parse("2019-06-20T21:53:07Z"), o1);
    o1.setPayment(pay1);

    orderRepository.save(o1);
```

#### Resultado no banco de dados

![result](assets/image-11.png)

## Inserção de User no banco de dados

Primeiro iremos criar no `UserService` um método para inserir um `User` no banco de dados e retorne o `User` criado.

```java
public User insert(User obj) {
    //chamando o save para salvar o objeto no banco de dados
    return repository.save(obj);
}
```

Agora vamos no `UserResource` configurar o endpoint para inserção de `User`.

```java
@PostMapping
public ResponseEntity<User> insert(@RequestBody User obj) {
    obj = service.insert(obj);
    URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
    .path("/{id}").buildAndExpand(obj.getId()).toUri();
    return ResponseEntity.created(uri).body(obj);
}
```
### Explicação da URI
1. **ServletUriComponentsBuilder.fromCurrentRequest()**

    - Obtém a URI base da requisição atual (por exemplo, `http://example.com/resource`).
2. **.path("/{id}")**

    - Acrescenta um segmento de caminho ao final da URI base. O `{id}` aqui é um placeholder que será substituído por um valor real.
3. **.buildAndExpand(obj.getId())**

    - Substitui o placeholder `{id}` pelo valor retornado por `obj.getId()`. Por exemplo, se `obj.getId()` retorna `123`, o `{id}` será substituído por `123`.
4. **.toUri()**

    - Converte a URI final para um objeto do tipo `URI`.

### Retorno da requisição ResponseEntity

1. **O código de status HTTP 201 (Created):**
Indica que um novo recurso foi criado com sucesso.

2. **O cabeçalho `Location`:**
Contém a URI do novo recurso criado, permitindo ao cliente saber onde ele pode acessar esse recurso.

3. **O corpo da resposta:**
Inclui o próprio objeto criado, geralmente no formato JSON (ou outro formato configurado pelo Spring para serialização).

### Explicação das anotações
- `@PostMapping` - Serve para inserir dados no banco de dados.
- `@RequestBody`- Serve para receber os dados do corpo da requisição no formato JSON e desserializa para o objeto `User`.

Nesse método iremos pegar o `User` que vem no corpo da requisição e irá passar como parâmetro do método `insert` que irá salvar o `User` no banco de dados, e depois irá retornar a URI da requisição com o `id` do `User` que acabou de ser salvo no banco.

Como eu quero que os dados inseridos no corpo da requisição vá para o banco de dados, iremos utilizar o `@RequestBody` para desserializar os dados do JSON para o objeto `User`.

Estou recebendo no `obj` o resultado do `service.insert(obj)` que salvou o `User` no banco de dados.

E por fim iremos utilizar o `ServletUriComponentsBuilder` para retornar a URI da requisição com o `id` do `User` que acabou de ser salvo no banco.

### Status das requisições

- 200 OK - Usado para sucesso na requisição
- 201 Created - Usado para criações de recursos
- 204 No Content - Usado para exclusões de recursos

### Resultado da requisição

![result](assets/image-12.png)

## Deleção de User no banco de dados

Para deletar um `User` no banco de dados iremos criar um endpoint para deletar um `User` pelo `{id}`.
```java
@DeleteMapping(value = "/{id}")
public ResponseEntity<Void> delete(@PathVariable Long id) {
    service.delete(id);
    return ResponseEntity.noContent().build();
}
```

Esse endpoint chama o `service.delete(id)` para deletar o `User` no banco de dados.

```java
public void delete(Long id) {
    repository.deleteById(id);
}
```
Apenas com isso iremos deletar o `User` no banco de dados.

![result](assets/image-13.png)

## Atualização de User no banco de dados

Para atualizar um `User` no banco de dados iremos criar um endpoint para atualizar um `User` pelo `{id}` usando o `@PutMapping`.
```java
@PutMapping(value = "/{id}")
public ResponseEntity<User> update(@PathVariable Long id, @RequestBody User obj) {
    obj = service.update(id, obj);
    return ResponseEntity.ok().body(obj);
}
```

Esse endpoint chama o `service.update(id, obj)` para atualizar o `User` no banco de dados.

```java
public User update(Long id, User obj) {
    User entity = repository.getReferenceById(id);
    updateData(entity, obj);
    return repository.save(entity);
}
private void updateData(User entity, User obj) {
    entity.setName(obj.getName());
    entity.setEmail(obj.getEmail());
    entity.setPhone(obj.getPhone());
}
```

### Resultado da requisição

Requisição **GET** para `/users/1`
```json
{
    "id": 1,
    "name": "Maria Brown",
    "email": "maria@gmail.com",
    "phone": "988888888",
    "password": "123456"
}
```

Requisição **PUT** para `/users/1` com o retorno da requisição.

![result](assets/image-14.png)