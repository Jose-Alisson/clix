# 🚀 Visão Geral

Esta é uma CLI (*Command Line Interface*) desenvolvida em Java que permite executar comandos e ações definidas por meio das anotações **@Command** e **@Action**.
O objetivo é oferecer uma interface simples, robusta e extensível para integrar e organizar funcionalidades do sistema a partir de classes anotadas.

---

# ⚠️ Aviso

Este projeto ainda está em desenvolvimento.
Funcionalidades podem mudar, quebrar ou ser removidas sem aviso prévio.

---

# 🛠️ Implementação

## Método `main`

```java
import clix.manager.CommandManager;

public class Main {

    public static void main(String[] args) {
        CommandManager.initialize("<my package>");
        CommandManager.exec(new Parser(args));
    }
}
```

## Exemplo de uso das anotações

```java
@Command(command = "print")
public class Print {

    @Action(
        refactor = {
            @RefactorArgument(params = {"msg", "m"}, refactor = "message")
        },
        arguments = {
            @DefineArgument(name = "message"),
            @DefineArgument(name = "text")
        }
    )
    void voidHelloWorld(List<Argument> arguments) {
        System.out.println(arguments);
    }
}
```

## Execução do comando

```bash
print msg "Hello World"
```

---

# 📌 Anotações Disponíveis

* **@Command**
* **@Action**
* **@EnableHelp**
* **@RefactorArgument**
* **@DefineArgument**

---

# 🧩 @Command

A anotação `@Command` torna a classe visível para o processo de escaneamento do Clix.
A partir dela o framework monta a estrutura de definição do comando.

* O elemento `command` define o nome do comando principal.
* Atualmente, o Clix utiliza apenas o **primeiro** método anotado com `@Action`, ignorando os demais (comportamento sujeito a mudança).

---

# 🧨 @Action

A anotação `@Action` define qual método será executado quando o comando for acionado.

Ela possui dois elementos importantes:

### `refactor`

Uma lista de `@RefactorArgument` que permite mapear nomes alternativos de argumentos.
Exemplo: `"msg"` ou `"m"` → `"message"`.

### `arguments`

Uma lista de `@DefineArgument` contendo os argumentos que o comando aceita — utilizados tanto na execução quanto no sistema de ajuda.

### Tipos aceitos nos parâmetros do método `@Action`

O Clix reconhece automaticamente:

* `List<Argument>`
* `Argument`
* `Argument` referenciado pelo nome definido em `@DefineArgument`
* `List<Flags>` (suporte atual limitado; melhorias planejadas)
* `Flags` (em desenvolvimento)

---

# 📝 @EnableHelp

Habilita o comando nativo `help`, que gera automaticamente uma interface de ajuda com base nas anotações fornecidas pelo desenvolvedor.

