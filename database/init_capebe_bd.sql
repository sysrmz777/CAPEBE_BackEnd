-- Script unificado PostgreSQL para base de datos: capebeBD (puerto 5434)
-- Incluye parámetros oficiales del Socio Formador (Caso CAPEBE)

CREATE TABLE IF NOT EXISTS aeronaves (
    id_aeronave BIGSERIAL PRIMARY KEY,
    matricula VARCHAR(20) NOT NULL,
    modelo VARCHAR(80) NOT NULL,
    plazas_totales INTEGER NOT NULL,
    asientos_disponibles INTEGER NOT NULL,
    base_aerea VARCHAR(80) NOT NULL,
    horometro_actual DOUBLE PRECISION NOT NULL,
    horometro_max_mantenimiento DOUBLE PRECISION NOT NULL,
    en_mantenimiento BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS vuelos (
    id BIGSERIAL PRIMARY KEY,
    pasajero VARCHAR(120),
    rut VARCHAR(20),
    oficina_banco VARCHAR(120),
    es_socio BOOLEAN DEFAULT TRUE,
    telefono_movil VARCHAR(25),
    cantidad_pasajeros INTEGER DEFAULT 1,
    fecha VARCHAR(20),
    bloque_horario VARCHAR(40),
    aeronave VARCHAR(120),
    plazas_aeronave INTEGER DEFAULT 4,
    base_aerea VARCHAR(120),
    destino VARCHAR(150),
    piloto_al_mando VARCHAR(120),
    codigo_verificacion VARCHAR(40),
    peso_kg DOUBLE PRECISION,
    horometro DOUBLE PRECISION
);

CREATE TABLE IF NOT EXISTS operacion_pista (
    id_operacion BIGSERIAL PRIMARY KEY,
    fecha VARCHAR(20) NOT NULL,
    horario VARCHAR(40) NOT NULL,
    hora_local VARCHAR(20),
    base VARCHAR(100) NOT NULL,
    destino VARCHAR(150),
    aeronave VARCHAR(100) NOT NULL,
    matricula VARCHAR(20),
    piloto_al_mando VARCHAR(120),
    pasajero VARCHAR(120),
    cantidad_pasajeros INTEGER DEFAULT 1,
    codigo_qr VARCHAR(80),
    horometro_inicial DOUBLE PRECISION,
    horometro_final DOUBLE PRECISION,
    duracion_minutos INTEGER,
    estado_pista VARCHAR(60)
);

CREATE TABLE IF NOT EXISTS pase_qr (
    id_qr BIGSERIAL PRIMARY KEY,
    codigo_qr VARCHAR(80) NOT NULL,
    socio VARCHAR(120) NOT NULL,
    rut VARCHAR(20),
    oficina_banco VARCHAR(120),
    es_socio BOOLEAN DEFAULT TRUE,
    telefono_movil VARCHAR(25),
    cantidad_pasajeros INTEGER DEFAULT 1,
    plazas_aeronave INTEGER DEFAULT 4,
    fecha VARCHAR(20) NOT NULL,
    horario VARCHAR(40) NOT NULL,
    base VARCHAR(100) NOT NULL,
    aeronave VARCHAR(100) NOT NULL,
    destino VARCHAR(150),
    piloto_al_mando VARCHAR(120),
    encargado_pista VARCHAR(100),
    observaciones VARCHAR(300),
    pista_despejada BOOLEAN DEFAULT TRUE,
    combustible_ok BOOLEAN DEFAULT TRUE,
    luces_ok BOOLEAN DEFAULT TRUE,
    horometro_inicial DOUBLE PRECISION,
    horometro_final DOUBLE PRECISION,
    estado_qr VARCHAR(80)
);
