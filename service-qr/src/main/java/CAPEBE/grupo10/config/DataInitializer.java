package CAPEBE.grupo10.config;

import CAPEBE.grupo10.model.PaseQr;
import CAPEBE.grupo10.repository.PaseQrRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initPasesQr(PaseQrRepository repository) {
        return args -> {
            if (repository.count() == 0) {
                repository.saveAll(List.of(
                        new PaseQr(null, "CAPEBE-SCTB-1001", "Álvaro Ramírez Soto", "18.452.310-K", "Casa Matriz Alameda", true, "+56987654321", 3, 4, "2026-11-21", "10:00 - 11:00", "Tobalaba", "CC-AAA Cessna 172", "Circuito Precordillera Santiago (SCTB)", "Cap. Rodrigo Fuentes", "Encargado Pista", "Pendiente verificación en losa", true, true, true, 1245.2, null, "QR EMITIDO - PENDIENTE EN PISTA"),
                        new PaseQr(null, "CAPEBE-SCTB-1002", "Camila Morales Vega", "17.920.445-3", "Sucursal Providencia", true, "+56976543210", 2, 4, "2026-11-21", "11:00 - 12:00", "Tobalaba", "CC-BBB Piper PA-28", "Circuito Cordillera Oriente (SCTB)", "Cap. Marcela Paredes", "Encargado Pista", "Esperando ingreso a losa", true, true, true, 2198.4, null, "QR EMITIDO - PENDIENTE EN PISTA"),
                        new PaseQr(null, "CAPEBE-SCIE-1003", "Cristián Herrera Díaz", "16.512.889-7", "Sucursal Concepción Centro", false, "+56965432109", 4, 6, "2026-11-22", "09:00 - 10:00", "Talcahuano", "CC-DDD Cessna 206", "Circuito Bahía Concepción (SCIE)", "Cap. Javier Muñoz", "Encargado Pista", "Invitado acompañado por socio titular", true, true, true, 3410.0, null, "QR EMITIDO - PENDIENTE EN PISTA"),
                        new PaseQr(null, "CAPEBE-SCTB-1004", "Francisca Rojas Silva", "19.210.774-1", "Sucursal Las Condes", true, "+56954321098", 5, 6, "2026-11-28", "15:00 - 16:00", "Tobalaba", "CC-DDD Cessna 206", "Circuito Precordillera Santiago (SCTB)", "Cap. Rodrigo Fuentes", "Encargado Pista", "Esperando verificación de combustible 100LL", true, true, true, 3410.3, null, "QR EMITIDO - PENDIENTE EN PISTA"),
                        new PaseQr(null, "CAPEBE-SCIE-1005", "Matías Contreras Pino", "15.803.112-4", "Sucursal Talcahuano", true, "+56943210987", 3, 4, "2026-11-29", "10:00 - 11:00", "Talcahuano", "CC-AAA Cessna 172", "Circuito Península Tumbes (SCIE)", "Cap. Javier Muñoz", "Encargado Pista", "Esperando inspección de balizas", true, true, true, 1245.5, null, "QR EMITIDO - PENDIENTE EN PISTA")
                ));
            }
        };
    }
}
