# Desafio: DSMovie RestAssured

## Descrição

Este projeto consiste na implementação de **testes de API** para o sistema **DSMovie**, uma aplicação de filmes e avaliações de filmes. Os testes são escritos com o **RestAssured**, validando as respostas dos endpoints, incluindo status HTTP, dados retornados e regras de acesso.

O sistema possui as seguintes características:

- **Consulta de filmes:** a visualização dos dados dos filmes é pública e não exige login.
- **Gerenciamento de filmes:** inserir, atualizar e deletar filmes são operações permitidas apenas para usuários **ADMIN**.
- **Avaliação de filmes:** qualquer usuário logado (**CLIENT** ou **ADMIN**) pode registrar uma nota para um filme.

<img width="889" height="267" alt="image" src="https://github.com/user-attachments/assets/5860601d-03af-42c4-b4b9-d89ccb1a5fa3" />

## Especificações

### Modelo de domínio

O modelo é composto pelas seguintes entidades:

- **Movie:** id, título, nota média (`score`), contagem de votos (`count`) e imagem;
- **User:** id e email;
- **Score:** armazena a nota (`value`), de 0 a 5, que cada usuário deu a cada filme. Associa um usuário a um filme.

#### Regras

- Sempre que um usuário registra uma nota, o sistema deverá calcular a **média das notas de todos os usuários** para aquele filme.
- A nota média deverá ser armazenada no atributo `score` da entidade `Movie`, juntamente com a **contagem de votos** (`count`).

---

### Testes de API

Devem ser implementados os testes de API listados abaixo, organizados por classe de teste.

#### MovieControllerRA

- `findAllShouldReturnOkWhenMovieNoArgumentsGiven`
- `findAllShouldReturnPagedMoviesWhenMovieTitleParamIsNotEmpty`
- `findByIdShouldReturnMovieWhenIdExists`
- `findByIdShouldReturnNotFoundWhenIdDoesNotExist`
- `insertShouldReturnUnprocessableEntityWhenAdminLoggedAndBlankTitle`
- `insertShouldReturnForbiddenWhenClientLogged`
- `insertShouldReturnUnauthorizedWhenInvalidToken`

#### ScoreControllerRA

- `saveScoreShouldReturnNotFoundWhenMovieIdDoesNotExist`
- `saveScoreShouldReturnUnprocessableEntityWhenMissingMovieId`
- `saveScoreShouldReturnUnprocessableEntityWhenScoreIsLessThanZero`

## Critério de aprovação

O mínimo para aprovação no desafio é de **8 dos 10 testes** implementados.

## Tecnologias

Este projeto utiliza tecnologias do ecossistema Java e Spring, com testes de API implementados com o RestAssured e autenticação baseada em token.

## Referência

Desafio proposto pela [DevSuperior](https://devsuperior.com.br/).

---

**Todos os direitos reservados à DevSuperior.**
