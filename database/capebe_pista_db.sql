CREATE TABLE IF NOT EXISTS operacion_pista (
    id_operacion BIGSERIAL PRIMARY KEY,
    fecha VARCHAR(80) NOT NULL,
    horario VARCHAR(80) NOT NULL,
    base VARCHAR(100) NOT NULL,
    aeronave VARCHAR(100) NOT NULL,
    codigo_qr VARCHAR(80),
    estado_pista VARCHAR(60)
);

INSERT INTO operacion_pista (fecha, horario, base, aeronave, codigo_qr, estado_pista)
VALUES
('Sábado 24/10/2026', '10:00 - 11:00', 'Tobalaba', 'CC-AAA Cessna 172', 'CAPEBE-SCTB-1001', 'PENDIENTE EN LOSA'),
('Sábado 24/10/2026', '11:30 - 12:30', 'Tobalaba', 'CC-BBB Piper PA-28', 'CAPEBE-SCTB-1002', 'PENDIENTE EN LOSA'),
('Domingo 25/10/2026', '09:00 - 10:00', 'Talcahuano', 'CC-CCC Cessna 150', 'CAPEBE-SCIE-1003', 'PENDIENTE EN LOSA'),
('Sábado 31/10/2026', '15:00 - 16:00', 'Tobalaba', 'CC-AAA Cessna 172', 'CAPEBE-SCTB-1004', 'PENDIENTE EN LOSA'),
('Sábado 07/11/2026', '10:30 - 11:30', 'Talcahuano', 'CC-BBB Piper PA-28', 'CAPEBE-SCIE-1005', 'PENDIENTE EN LOSA');
