# Payment Service

O **Payment Service** é um microserviço responsável por orquestrar o fluxo de pagamentos entre usuários no ecossistema da plataforma **AmigoPay**. Ele recebe solicitações de pagamento, valida regras de negócio (como limite diário e saldo disponível), emite eventos para os demais serviços e gerencia o histórico de transações realizadas.

---

## Sumário

- [Arquitetura](#arquitetura)
- [Tecnologias Utilizadas](#tecnologias-utilizadas)
- [Endpoints](#endpoints)
- [Eventos Kafka](#eventos-kafka)
- [Regras de Negócio](#regras-de-negócio)
- [Estrutura do Projeto](#estrutura-do-projeto)
- [Como Executar](#como-executar)
- [Testes](#testes)
- [Padrões e Boas Práticas](#padrões-e-boas-práticas)

---

## Arquitetura

- **Estilo:** Microservices
- **Protocolo:** REST + Kafka Event Streaming
- **Banco de Dados:** PostgreSQL
- **Mensageria:** Apache Kafka (produção e consumo de eventos)
- **Segurança:** Pode ser integrada com JWT e API Gateway (futuro)

---

## Tecnologias Utilizadas

- Java 17
- Spring Boot 3.x
- Spring Web, Spring Data JPA
- Apache Kafka (spring-kafka)
- PostgreSQL
- Docker + Docker Compose
- JUnit 5 + Mockito (testes unitários)
- MapStruct (para mapeamento entre DTOs e entidades)

---

## Endpoints

| Método | URI                | Descrição                        |
|--------|--------------------|----------------------------------|
| POST   | `/api/v1/payments` | Cria uma nova solicitação de pagamento |

Payload de requisição (exemplo):

```json
{
  "payerId": "uuid-do-pagador",
  "payeeId": "uuid-do-recebedor",
  "amount": 150.00,
  "description": "Almoço com amigos"
}
```

---

## Eventos Kafka

### Eventos Consumidos

| Tópico             | Origem           | Ação                                                        |
| ------------------ | ---------------- | ----------------------------------------------------------- |
| `payment-done`     | `wallet-service` | Atualiza o status do pagamento para "Concluído"             |
| `payment-rejected` | `wallet-service` | Atualiza o status para "Rejeitado" (ex: saldo insuficiente) |

### Eventos Produzidos

| Tópico              | Destino(s)       | Conteúdo                                      |
| ------------------- | ---------------- | --------------------------------------------- |
| `payment.initiated` | `wallet-service` | Informações completas do pagamento solicitado |

---

## Regras de Negócio

- Pagador e recebedor não podem ser nulos
- Pagador e recebedor devem ser diferentes
- Valor deve ser positivo e maior que zero
- Pagador e recebedor devem existir no sistema
- Limite diário de pagamentos: R$ 20.000,00
- Após validação, o evento `payment.initiated` é publicado no Kafka

--- 

## Estrutura do Projeto

```markdown
payment-service/
├── common/                        # Utilitários e componentes genéricos reutilizáveis por todo o projeto (ex: mensagens, helpers, constantes globais)
├── config/                        # Configurações gerais da aplicação (ex: beans, CORS, Swagger, segurança, etc.)
├── exception/                     # Hierarquia de exceções customizadas (ex: BusinessException), e handlers globais para retorno consistente de erros

├── messaging/                     # Infraestrutura de mensageria (Kafka), separando claramente os papéis e responsabilidades
│   ├── config/                    # Configurações dos tópicos Kafka, serializadores e listeners
│   ├── event/                     # Representações dos eventos transmitidos e consumidos via Kafka, incluindo fábricas responsáveis por construir os eventos a partir das entidades de domínio (ex: PaymentInitiatedEventFactory → Payment → PaymentInitiatedEvent)
│   ├── producer/                  # Interfaces responsáveis por enviar eventos Kafka
│   │   ├── impl/                  # Implementações que utilizam KafkaTemplate para publicar eventos
│   ├── publisher/                 # Orquestradores de publicação de eventos (fazem validação, construção e chamada da producer)
│   ├── consumer/                  # Consumidores Kafka (listeners dos tópicos que escutam eventos)
│   │   ├── handle/                # Classes responsáveis por tratar a lógica de negócio de cada evento consumido

├── payment/                       # Camada de domínio do microserviço de pagamento
│   ├── controller/                # Exposição de endpoints REST para clientes externos (ex: `/api/v1/payments`)
│   ├── dto/                       # Objetos de entrada e saída da API (ex: PaymentRequest, PaymentResponse)
│   ├── entity/                    # Entidades JPA que representam tabelas do banco de dados (ex: Payment)
│   ├── enums/                     # Enumerações de status, tipos de pagamento, mensagens de validação, etc.
│   ├── factory/                   # Criação de objetos complexos e imutáveis (ex: eventos ou entidades a partir de contexto)
│   ├── mapper/                    # Conversores entre DTOs, entidades e eventos (geralmente usando MapStruct)
│   ├── repository/                # Interfaces JPA para persistência de dados (ex: PaymentRepository)
│   ├── service/                   # Interfaces de regras de negócio principais
│   │   ├── impl/                  # Implementações das regras de negócio (ex: PaymentServiceImpl)
│   ├── validation/                # Validadores de negócio, incluindo composição de validações com Strategy Pattern

└── test/                          # Testes unitários e de integração, organizados para validar funcionalidades críticas
                  
```

---

## Padrões e Boas Práticas

**Design Patterns Aplicados:**
- Strategy Pattern para validações de pagamento
- Factory Pattern para construção de eventos
- Separation of Concerns entre produção de eventos (producer) e orquestração (publisher)

**Princípios SOLID**
- Cada classe com responsabilidade única
- Interfaces segregadas e componentes injetáveis para facilitar testes

**Testabilidade**
- Serviços desacoplados
- Testes com mocks para Kafka, repositórios e validações

---

## Contato
Este microserviço faz parte da plataforma **AmigoPay**, projeto mantido por **Eduardo Sartori**.