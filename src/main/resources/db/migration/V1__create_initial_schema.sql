-- =============================================
-- SpotGuard Marketplace - Schema Inicial
-- =============================================

-- Habilitar extensão PostGIS
CREATE EXTENSION IF NOT EXISTS postgis;

-- Tabela de Usuários
CREATE TABLE users (
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    email       VARCHAR(150) NOT NULL UNIQUE,
    password    VARCHAR(255) NOT NULL,
    role        VARCHAR(20) NOT NULL CHECK (role IN ('TENANT', 'OWNER', 'CREATOR', 'ADMIN')),
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabela de Espaços (Marketplace)
CREATE TABLE spaces (
    id              BIGSERIAL PRIMARY KEY,
    title           VARCHAR(200) NOT NULL,
    description     TEXT,
    price_per_day   DECIMAL(10, 2) NOT NULL,
    address         VARCHAR(300) NOT NULL,
    location        geometry(Point, 4326),
    type            VARCHAR(20) NOT NULL CHECK (type IN ('GARAGE', 'WAREHOUSE', 'ROOM', 'OFFICE')),
    status          VARCHAR(20) NOT NULL DEFAULT 'AVAILABLE' CHECK (status IN ('AVAILABLE', 'RENTED', 'INACTIVE')),
    owner_id        BIGINT NOT NULL REFERENCES users(id),
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Índice espacial para buscas geográficas
CREATE INDEX idx_spaces_location ON spaces USING GIST (location);
CREATE INDEX idx_spaces_status ON spaces(status);
CREATE INDEX idx_spaces_owner ON spaces(owner_id);

-- Tabela de Infoprodutos
CREATE TABLE infoproducts (
    id          BIGSERIAL PRIMARY KEY,
    title       VARCHAR(200) NOT NULL,
    description TEXT,
    price       DECIMAL(10, 2) NOT NULL,
    file_url    VARCHAR(500),
    status      VARCHAR(20) NOT NULL DEFAULT 'DRAFT' CHECK (status IN ('DRAFT', 'PUBLISHED', 'ARCHIVED')),
    creator_id  BIGINT NOT NULL REFERENCES users(id),
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_infoproducts_creator ON infoproducts(creator_id);
CREATE INDEX idx_infoproducts_status ON infoproducts(status);

-- Tabela de Pagamentos (com idempotência)
CREATE TABLE payments (
    id                BIGSERIAL PRIMARY KEY,
    idempotency_key   VARCHAR(100) NOT NULL UNIQUE,
    amount            DECIMAL(10, 2) NOT NULL CHECK (amount >= 0),
    status            VARCHAR(20) NOT NULL DEFAULT 'PENDING' CHECK (status IN ('PENDING', 'COMPLETED', 'FAILED', 'REFUNDED', 'IN_ESCROW')),
    gateway           VARCHAR(20) NOT NULL CHECK (gateway IN ('STRIPE', 'ASAAS', 'PAGSEGURO')),
    external_id       VARCHAR(200),
    space_id          BIGINT NOT NULL,
    tenant_id         BIGINT NOT NULL,
    start_date        DATE NOT NULL,
    end_date          DATE NOT NULL,
    created_at        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at        TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_payments_idempotency ON payments(idempotency_key);
CREATE INDEX idx_payments_status ON payments(status);
CREATE INDEX idx_payments_space ON payments(space_id);
