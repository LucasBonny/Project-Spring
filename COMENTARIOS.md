# Exceções em Java

Este documento descreve algumas das exceções mais comuns em Java e como elas ocorrem.

---

## **`IllegalArgumentException`**

Essa exceção ocorre quando um método ou construtor recebe um argumento inválido.

### Exemplo

```java
public class User {
    private String name;

    public User(String name) {
        if (name == null || name.isBlank()) { // Validação do argumento
            throw new IllegalArgumentException("Name cannot be null or blank");
        }
        this.name = name;
    }
}

// Chamando o construtor com argumento inválido
User user = new User(null); // Lançará IllegalArgumentException
```

---

## **`IndexOutOfBoundsException`**

Essa exceção é lançada ao tentar acessar um índice fora dos limites válidos em arrays, listas ou outras coleções indexadas.

### Exemplo com Array

```java
int[] array = {1, 2, 3};
int index = 3; // Índice inválido, já que os índices válidos são 0, 1 e 2
System.out.println(array[index]); 
// Lançará: ArrayIndexOutOfBoundsException
```

### Exemplo com Lista

```java
import java.util.ArrayList;

ArrayList<Integer> list = new ArrayList<>();
list.add(1);
list.add(2);
list.add(3);

int index = 3; // Índice inválido
System.out.println(list.get(index)); 
// Lançará: IndexOutOfBoundsException
```

---

## **`NullPointerException`**

Essa exceção ocorre quando tentamos acessar um método, atributo ou realizar uma operação em uma variável que está nula (ou seja, não aponta para nenhum objeto válido).

### Exemplo

```java
String name = null;
System.out.println(name.length()); // Lançará: NullPointerException
```

### Prevenção com Injeção de Dependência

A injeção de dependência ajuda a evitar `NullPointerException`, garantindo que os objetos necessários sejam fornecidos pelo sistema de controle de dependências (como Spring).

```java
public class UserService {
    private final UserRepository userRepository;

    // Injeção de dependência via construtor
    public UserService(UserRepository userRepository) {
        if (userRepository == null) {
            throw new IllegalArgumentException("UserRepository cannot be null");
        }
        this.userRepository = userRepository;
    }
}

// Garantindo que o UserRepository seja fornecido corretamente pelo framework de DI (ex.: Spring)
```

---

## Resumo

- **`IllegalArgumentException`**: Ocorre quando um método ou construtor recebe um argumento inválido.
- **`IndexOutOfBoundsException`**: Ocorre ao tentar acessar um índice fora dos limites válidos em listas, arrays ou outras coleções indexadas.
- **`NullPointerException`**: Ocorre quando tentamos acessar métodos, atributos ou realizar operações em um objeto que está nulo.

---

# Estudar depois

- Expressões Lambda
- Mockito
