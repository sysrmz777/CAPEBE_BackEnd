package CAPEBE.grupo10.config;

import CAPEBE.grupo10.model.Aeronave;
import CAPEBE.grupo10.model.OperacionPista;
import CAPEBE.grupo10.repository.AeronaveRepository;
import CAPEBE.grupo10.repository.OperacionPistaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initOperacionesPista(
            OperacionPistaRepository repository,
            AeronaveRepository aeronaveRepository
    ) {
        return args -> {
            if (aeronaveRepository.count() == 0) {
                aeronaveRepository.saveAll(List.of(
                        new Aeronave(null, "CC-AAA", "Cessna 172", 4, 3, "Tobalaba", 1245.2, 1250.0, false),
                        new Aeronave(null, "CC-BBB", "Piper PA-28", 4, 3, "Tobalaba", 2198.4, 2200.0, false),
                        new Aeronave(null, "CC-DDD", "Cessna 206", 6, 5, "Talcahuano", 3410.0, 3450.0, false),
                        new Aeronave(null, "CC-EEE", "Piper PA-32 Saratoga", 6, 5, "Tobalaba", 4100.0, 4100.0, true)
                ));
            }

            if (repository.count() == 0) {
                repository.saveAll(List.of(
                        new OperacionPista(null, "2026-11-21", "10:00 - 11:00", "10:15", "Tobalaba", "Circuito Precordillera Santiago (SCTB)", "CC-AAA Cessna 172", "CC-AAA", "Cap. Rodrigo Fuentes", "Álvaro Ramírez Soto", 3, "CAPEBE-SCTB-1001", 1245.2, null, null, "PENDIENTE EN LOSA"),
                        new OperacionPista(null, "2026-11-21", "11:00 - 12:00", "11:10", "Tobalaba", "Circuito Cordillera Oriente (SCTB)", "CC-BBB Piper PA-28", "CC-BBB", "Cap. Marcela Paredes", "Camila Morales Vega", 2, "CAPEBE-SCTB-1002", 2198.4, null, null, "PENDIENTE EN LOSA"),
                        new OperacionPista(null, "2026-11-22", "09:00 - 10:00", "09:20", "Talcahuano", "Circuito Bahía Concepción (SCIE)", "CC-DDD Cessna 206", "CC-DDD", "Cap. Javier Muñoz", "Cristián Herrera Díaz", 4, "CAPEBE-SCIE-1003", 3410.0, null, null, "PENDIENTE EN LOSA"),
                        new OperacionPista(null, "2026-11-28", "15:00 - 16:00", "15:15", "Tobalaba", "Circuito Precordillera Santiago (SCTB)", "CC-DDD Cessna 206", "CC-DDD", "Cap. Rodrigo Fuentes", "Francisca Rojas Silva", 5, "CAPEBE-SCTB-1004", 3410.3, null, null, "PENDIENTE EN LOSA"),
                        new OperacionPista(null, "2026-11-29", "10:00 - 11:00", "10:15", "Talcahuano", "Circuito Península Tumbes (SCIE)", "CC-AAA Cessna 172", "CC-AAA", "Cap. Javier Muñoz", "Matías Contreras Pino", 3, "CAPEBE-SCIE-1005", 1245.5, null, null, "PENDIENTE EN LOSA")
                ));
            }
        };
    }
}
