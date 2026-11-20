CREATE TABLE IF NOT EXISTS pase_qr (
    id_qr BIGSERIAL PRIMARY KEY,
    codigo_qr VARCHAR(80) NOT NULL,
    socio VARCHAR(100) NOT NULL,
    fecha VARCHAR(80) NOT NULL,
    horario VARCHAR(80) NOT NULL,
    base VARCHAR(100) NOT NULL,
    aeronave VARCHAR(100) NOT NULL,
    encargado_pista VARCHAR(100),
    observaciones VARCHAR(300),
    pista_despejada BOOLEAN DEFAULT TRUE,
    combustible_ok BOOLEAN DEFAULT TRUE,
    luces_ok BOOLEAN DEFAULT TRUE,
    estado_qr VARCHAR(80)
);

INSERT INTO pase_qr (codigo_qr, socio, fecha, horario, base, aeronave, encargado_pista, observaciones, pista_despejada, combustible_ok, luces_ok, estado_qr)
VALUES
('CAPEBE-SCTB-1001', 'Socio CAPEBE', 'Sábado 24/10/2026', '10:00 - 11:00', 'Tobalaba', 'CC-AAA Cessna 172', 'Encargado Pista', 'Pista 19 despejada y lista', TRUE, TRUE, TRUE, 'QR EMITIDO - PENDIENTE EN PISTA'),
('CAPEBE-SCTB-1002', 'Socio CAPEBE', 'Sábado 24/10/2026', '11:30 - 12:30', 'Tobalaba', 'CC-BBB Piper PA-28', 'Encargado Pista', 'Esperando ingreso a losa', TRUE, TRUE, TRUE, 'QR EMITIDO - PENDIENTE EN PISTA'),
('CAPEBE-SCIE-1003', 'Socio CAPEBE', 'Domingo 25/10/2026', '09:00 - 10:00', 'Talcahuano', 'CC-CCC Cessna 150', 'Encargado Pista', 'Esperando ingreso a losa', TRUE, TRUE, TRUE, 'QR EMITIDO - PENDIENTE EN PISTA'),
('CAPEBE-SCTB-1004', 'Socio CAPEBE', 'Sábado 31/10/2026', '15:00 - 16:00', 'Tobalaba', 'CC-AAA Cessna 172', 'Encargado Pista', 'Esperando verificación de combustible 100LL', TRUE, TRUE, TRUE, 'QR EMITIDO - PENDIENTE EN PISTA'),
('CAPEBE-SCIE-1005', 'Socio CAPEBE', 'Sábado 07/11/2026', '10:30 - 11:30', 'Talcahuano', 'CC-BBB Piper PA-28', 'Encargado Pista', 'Esperando inspección de balizas', TRUE, TRUE, TRUE, 'QR EMITIDO - PENDIENTE EN PISTA');
