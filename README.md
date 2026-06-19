# 🏠 SpotGuard Marketplace

> Plataforma de marketplace para locação de espaços ociosos e info-commerce.

[![Java](https://img.shields.io/badge/Java-21-orange)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot%203.4.13-brightgreen)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL%2015%2B-blue)](https://www.postgresql.org/)
[![PostGIS](https://img.shields.io/badge/PostGIS-3.3-blue)](https://postgis.net/)
[![Redis](https://img.shields.io/badge/Redis%207.2-red)](https://redis.io/)
[![RabbitMQ](https://img.shields.io/badge/RabbitMQ%203.13-orange)](https://www.rabbitmq.com/)
[![License](https://img.shields.io/badge/License-MIT-yellow)](LICENSE)

---

## 📋 Índice

- [Visão Geral](#-visão-geral)
- [Arquitetura](#-arquitetura)
- [Stack Tecnológica](#-stack-tecnológica)
- [Pré-requisitos](#-pré-requisitos)
- [Instalação](#-instalação)
- [Configuração](#-configuração)
- [Executando](#-executando)
- [API Endpoints](#-api-endpoints)
- [Estrutura do Projeto](#-estrutura-do-projeto)
- [Roadmap](#-roadmap)
- [Contribuição](#-contribuição)
- [Licença](#-licença)

---

## 🔭 Visão Geral

O **SpotGuard Marketplace** é uma plataforma de marketplace com duas verticais de negócio:

1. **Marketplace de Espaços** — Permite que usuários aluguem garagens, depósitos, quartos e escritórios ociosos. Inclui busca geográfica via PostGIS, motor de reservas com prevenção de overbooking e sistema de escrow para pagamentos.

2. **Info-Commerce** — Monetização de comunidades via infoprodutos digitais com integração a grupos VIP (Telegram/WhatsApp).

### Diferenciais Técnicos

- **Busca espacial** com PostGIS (raio de proximidade em tempo real)
- **Prevenção de overbooking** com validação de sobreposição de datas
- **Idempotência de pagamentos** via Redis + PostgreSQL
- **Autenticação JWT** com RBAC (Role-Based Access Control)
- **Documentação OpenAPI** (Swagger UI) automática
- **Mensageria assíncrona** com RabbitMQ

---

## 🏗️ Arquitetura

```
┌─────────────────────────────────────────────────────────────┐
│                      Frontend (React)                       │
└──────────────────────────┬──────────────────────────────────┘
                           │ HTTPS / REST API
┌──────────────────────────▼──────────────────────────────────┐
│                   Spring Boot 3.4 (Java 21)                 │
│  ┌─────────────┐  ┌──────────────┐  ┌───────────────────┐  │
│  │   Config    │  │     Core     │  │     Modules       │  │
│  │  Security   │  │   Entities   │  │  space/           │  │
│  │  JWT Filter │  │   Enums      │  │  infoproduct/     │  │
│  │  OpenAPI    │  │   Exceptions │  │  payment/         │  │
│  └─────────────┘  └──────────────┘  └───────────────────┘  │
└──────┬──────────────┬───────────────┬───────────────────────┘
       │              │               │
┌──────▼──────┐ ┌─────▼──────┐ ┌─────▼──────┐
│ PostgreSQL  │ │   Redis    │ │  RabbitMQ  │
│   + PostGIS │ │   Cache    │ │  Messaging │
└─────────────┘ └────────────┘ └────────────┘
```

### Padrão de Módulos

O projeto segue uma arquitetura modular onde cada vertical de negócio é um módulo independente:

```
modules/
├── space/          # Marketplace de espaços (User, Space, Booking)
├── infoproduct/    # Info-commerce (Infoproduct)
└── payment/        # Pagamentos (Payment)
```

Cada módulo contém sua própria estrutura:
```
module/
├── controller/     # Endpoints REST
├── service/        # Lógica de negócio
├── repository/     # Acesso a dados (Spring Data JPA)
├── dto/            # Data Transfer Objects
└── entity/         # Entidades JPA
```

---

## 🛠️ Stack Tecnológica

| Camada | Tecnologia | Propósito |
|---|---|---|
| **Backend** | Spring Boot 3.4 + Java 21 | API REST com Virtual Threads (Loom) |
| **Banco de Dados** | PostgreSQL 15 + PostGIS 3.3 | Dados relacionais + busca espacial |
| **Cache** | Redis 7.2 | Sessões, idempotência, rate limiting |
| **Mensageria** | RabbitMQ 3.13 | Processamento assíncrono de webhooks |
| **Segurança** | Spring Security + JWT | Autenticação stateless com RBAC |
| **Documentação** | SpringDoc OpenAPI | Swagger UI automático |
| **ORM** | Hibernate 6 + Spatial | Mapeamento objeto-relacional |
| **Build** | Maven 3.9 | Gerenciamento de dependências |
| **Infra** | Docker Compose | Ambiente de desenvolvimento |

---

## 📦 Pré-requisitos

- [Java 21+](https://openjdk.org/projects/jdk/21/) (JDK)
- [Maven 3.9+](https://maven.apache.org/)
- [Docker](https://www.docker.com/) + [Docker Compose](https://docs.docker.com/compose/)

Verifique as versões:
```bash
java -version
mvn -version
docker --version
docker compose version
```

---

## 🚀 Instalação

### 1. Clone o repositório

```bash
git clone https://github.com/ernstneto/spotguard_marketplace.git
cd spotguard_marketplace
```

### 2. Configure as variáveis de ambiente

```bash
cp .env.example .env
```

Edite o `.env` com seus valores:
```env
POSTGRES_USER=admin
POSTGRES_PASSWORD=your_secure_password
POSTGRES_DB=spotguard_marketplace
REDIS_PASSWORD=your_redis_password
RABBITMQ_USER=admin
RABBITMQ_PASSWORD=your_rabbitmq_password
JWT_SECRET_KEY=your-256-bit-secret-key-here
```

### 3. Suba a infraestrutura (PostgreSQL + Redis + RabbitMQ)

```bash
docker compose up -d
```

Verifique se todos os serviços estão rodando:
```bash
docker compose ps
```

### 4. Compile o projeto

```bash
mvn clean compile
```

### 5. Execute a aplicação

```bash
mvn spring-boot:run
```

A aplicação estará disponível em `http://localhost:9090`

---

## ⚙️ Configuração

### Perfis de Ambiente

| Perfil | Arquivo | Uso |
|---|---|---|
| `dev` | `application.yml` | Desenvolvimento local (padrão) |
| `test` | `application-test.yml` | Testes com TestContainers |

Para ativar um perfil:
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### Portas dos Serviços

| Serviço | Porta | Descrição |
|---|---|---|
| API Backend | `9090` | Aplicação Spring Boot |
| PostgreSQL | `5454` | Banco de dados (PostGIS) |
| Redis | `6379` | Cache |
| RabbitMQ AMQP | `5672` | Mensageria |
| RabbitMQ Management | `15672` | UI de administração |
| Swagger UI | `9090/swagger-ui.html` | Documentação da API |

---

## 📡 API Endpoints

### Autenticação

| Método | Endpoint | Auth | Descrição |
|---|---|---|---|
| `POST` | `/api/v1/auth/register` | Público | Registro de usuário |
| `POST` | `/api/v1/auth/login` | Público | Login (retorna JWT) |

### Espaços

| Método | Endpoint | Auth | Descrição |
|---|---|---|---|
| `GET` | `/api/v1/spaces` | Público | Lista disponíveis |
| `GET` | `/api/v1/spaces/{id}` | Público | Detalhe do espaço |
| `GET` | `/api/v1/spaces/nearby` | Público | Busca por proximidade |
| `POST` | `/api/v1/spaces` | OWNER/ADMIN | Cria espaço |
| `PUT` | `/api/v1/spaces/{id}` | OWNER/ADMIN | Atualiza espaço |
| `DELETE` | `/api/v1/spaces/{id}` | OWNER/ADMIN | Desativa espaço |

### Reservas

| Método | Endpoint | Auth | Descrição |
|---|---|---|---|
| `GET` | `/api/v1/bookings/my` | TENANT/OWNER/ADMIN | Minhas reservas |
| `GET` | `/api/v1/bookings/{id}` | TENANT/OWNER/ADMIN | Detalhe da reserva |
| `POST` | `/api/v1/bookings` | TENANT/ADMIN | Cria reserva |
| `PATCH` | `/api/v1/bookings/{id}/cancel` | TENANT/OWNER/ADMIN | Cancela reserva |

### Infoprodutos

| Método | Endpoint | Auth | Descrição |
|---|---|---|---|
| `GET` | `/api/v1/infoproducts` | Público | Lista publicados |
| `GET` | `/api/v1/infoproducts/my` | CREATOR/ADMIN | Meus infoprodutos |
| `GET` | `/api/v1/infoproducts/{id}` | Público | Detalhe |
| `POST` | `/api/v1/infoproducts` | CREATOR/ADMIN | Cria |
| `PUT` | `/api/v1/infoproducts/{id}` | CREATOR/ADMIN | Atualiza |
| `DELETE` | `/api/v1/infoproducts/{id}` | CREATOR/ADMIN | Arquiva |

### Exemplo de Uso

```bash
# Registrar usuário
curl -X POST http://localhost:9090/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{"name":"João","email":"joao@email.com","password":"123456","role":"OWNER"}'

# Login
curl -X POST http://localhost:9090/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"joao@email.com","password":"123456"}'

# Criar espaço (usar token do login)
curl -X POST http://localhost:9090/api/v1/spaces \
  -H "Authorization: Bearer <TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{"title":"Garagem Centro","pricePerDay":50.00,"address":"Rua A, 123","latitude":-23.55,"longitude":-46.63,"type":"GARAGE"}'

# Buscar espaços próximos
curl "http://localhost:9090/api/v1/spaces/nearby?latitude=-23.55&longitude=-46.63&radiusInMeters=5000"
```

---

## 📁 Estrutura do Projeto

```
spotguard-marketplace/
├── src/
│   ├── main/
│   │   ├── java/com/spotguard/marketplace/
│   │   │   ├── SpotguardMarketplaceApplication.java
│   │   │   ├── config/                    # Security, JWT, OpenAPI
│   │   │   ├── core/
│   │   │   │   ├── entity/                # BaseEntity
│   │   │   │   ├── enums/                 # UserRole, SpaceType, etc.
│   │   │   │   └── exception/             # GlobalExceptionHandler
│   │   │   └── modules/
│   │   │       ├── space/                 # User, Space, Booking
│   │   │       ├── infoproduct/           # Infoproduct
│   │   │       └── payment/               # Payment
│   │   └── resources/
│   │       ├── application.yml            # Config principal
│   │       ├── application-test.yml       # Config de testes
│   │       └── db/migration/              # Flyway migrations
│   └── test/                              # Testes (TODO)
├── pom.xml                                # Dependências Maven
├── docker-compose.yml                     # Infraestrutura
├── .env.example                           # Template de variáveis
├── .gitignore                             # Arquivos ignorados
└── README.md                              # Este arquivo
```

---

## 🗺️ Roadmap

### Fase 1 — Fundação ✅
- [x] Configuração do projeto (Spring Boot, Docker Compose)
- [x] Autenticação JWT com RBAC
- [x] CRUD de espaços com PostGIS
- [x] CRUD de infoprodutos
- [x] Motor de reservas com prevenção de overbooking
- [x] Documentação OpenAPI (Swagger)

### Fase 2 — Pagamentos (Em breve)
- [ ] Integração com gateway de pagamento (Stripe/Asaas)
- [ ] Idempotência de pagamentos via Redis
- [ ] Sistema de escrow
- [ ] Webhooks de confirmação

### Fase 3 — Automação (Em breve)
- [ ] Integração com Telegram/WhatsApp
- [ ] Links de convite com expiração
- [ ] Cron jobs de monitoramento de assinaturas

### Fase 4 — Segurança e Produção (Em breve)
- [ ] Rate limiting com Bucket4j
- [ ] Auditoria de ações sensíveis
- [ ] Testes de integração com TestContainers
- [ ] CI/CD com GitHub Actions
- [ ] Deploy em produção

---

## 🤝 Contribuição

1. Faça um fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/nova-feature`)
3. Commit suas mudanças (`git commit -m 'Adiciona nova feature'`)
4. Push para a branch (`git push origin feature/nova-feature`)
5. Abra um Pull Request

---

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

---

## 👤 Autor

**Ernst Neto**
- GitHub: [@ernstneto](https://github.com/ernstneto)
