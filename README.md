# API Academia - Checkpoint 1

Esta é uma API desenvolvida em Spring Boot para o gerenciamento de treinos. O sistema permite o cadastro de exercícios (Cardio e Força), criação de fichas de treino, vinculação de exercícios às fichas e registro de histórico de treinos concluídos.

## Persistência de Dados
O projeto utiliza persistência em arquivos de texto (.txt). Os dados salvos são carregados automaticamente assim que a aplicação é iniciada, e as novas operações de criação, alteração ou exclusão são gravadas no disco em tempo real.

## Teste da API (Swagger UI)
A API conta com a documentação do Swagger UI para facilitar a execução dos testes dos endpoints.

Com a aplicação rodando localmente, acesse o link abaixo no seu navegador:
http://localhost:8080/swagger-ui/index.html

### Fluxo de Teste Recomendado:
1. Cadastrar Exercícios: Utilize o endpoint POST /exercicios para criar um exercício de Cardio (tipo 1) e um de Força (tipo 2).
2. Criar uma Ficha: Utilize o endpoint POST /fichas para criar uma ficha (ex: "Treino A").
3. Consultar os IDs: Utilize os endpoints GET /exercicios e GET /fichas para visualizar as listas e copiar os IDs gerados pelo sistema.
4. Vincular Exercício: Utilize o endpoint POST /fichas/adicionar-exercicio informando o ID da ficha e o ID do exercício para fazer a união.
5. Concluir o Treino: Utilize o endpoint POST /concluir-treino informando o ID da ficha para simular a execução e calcular o gasto calórico total.
6. Consultar Histórico: Acesse o endpoint GET /historico para visualizar o registro do treino executado.

## Tecnologias
- Java
- Spring Boot Web
- Springdoc OpenAPI (Swagger UI)