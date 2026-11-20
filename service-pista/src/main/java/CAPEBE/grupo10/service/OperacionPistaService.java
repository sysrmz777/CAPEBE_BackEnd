package CAPEBE.grupo10.service;

import CAPEBE.grupo10.model.Aeronave;
import CAPEBE.grupo10.model.OperacionPista;
import CAPEBE.grupo10.model.ReporteDiarioDto;
import CAPEBE.grupo10.repository.AeronaveRepository;
import CAPEBE.grupo10.repository.OperacionPistaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OperacionPistaService {

    private final OperacionPistaRepository repository;
    private final AeronaveRepository aeronaveRepository;

    public OperacionPistaService(
            OperacionPistaRepository repository,
            AeronaveRepository aeronaveRepository
    ) {
        this.repository = repository;
        this.aeronaveRepository = aeronaveRepository;
    }

    public List<OperacionPista> obtenerTodas() {
        return repository.findAll();
    }

    public List<Aeronave> obtenerAeronaves() {
        return aeronaveRepository.findAll();
    }

    public OperacionPista buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vuelo en pista no encontrado"));
    }

    public List<OperacionPista> buscarPorBase(String base) {
        return repository.findByBaseIgnoreCase(base);
    }

    public List<OperacionPista> buscarPorEstado(String estadoPista) {
        return repository.findByEstadoPistaIgnoreCase(estadoPista);
    }

    @Transactional
    public OperacionPista guardar(OperacionPista operacion) {
        if (operacion.getCodigoQr() != null && !operacion.getCodigoQr().isBlank()) {
            Optional<OperacionPista> yaExiste = repository.findByCodigoQrIgnoreCase(operacion.getCodigoQr().trim());
            if (yaExiste.isPresent()) {
                return yaExiste.get();
            }
        }
        if (operacion.getEstadoPista() == null || operacion.getEstadoPista().isBlank()) {
            operacion.setEstadoPista("PENDIENTE EN LOSA");
        }
        if (operacion.getMatricula() == null || operacion.getMatricula().isBlank()) {
            operacion.setMatricula(extraerMatricula(operacion.getAeronave()));
        }
        if (operacion.getCodigoQr() == null || operacion.getCodigoQr().isBlank()) {
            String prefijoBase = operacion.getBase() != null
                    && operacion.getBase().toUpperCase().contains("TALCAHUANO") ? "SCIE" : "SCTB";
            String sufijo = UUID.randomUUID().toString().substring(0, 4).toUpperCase();
            operacion.setCodigoQr("CAPEBE-" + prefijoBase + "-" + sufijo);
        }
        if (operacion.getPilotoAlMando() == null || operacion.getPilotoAlMando().isBlank()) {
            operacion.setPilotoAlMando("Cap. Rodrigo Fuentes");
        }
        if (operacion.getDestino() == null || operacion.getDestino().isBlank()) {
            operacion.setDestino(operacion.getBase() != null && operacion.getBase().toUpperCase().contains("TALCAHUANO")
                    ? "Circuito Bahía Concepción (SCIE)"
                    : "Circuito Precordillera Santiago (SCTB)");
        }
        return repository.save(operacion);
    }

    @Transactional
    public OperacionPista actualizar(Long id, OperacionPista actualizada) {
        OperacionPista existente = buscarPorId(id);
        existente.setFecha(actualizada.getFecha());
        existente.setHorario(actualizada.getHorario());
        existente.setBase(actualizada.getBase());
        existente.setAeronave(actualizada.getAeronave());
        if (actualizada.getMatricula() != null && !actualizada.getMatricula().isBlank()) {
            existente.setMatricula(actualizada.getMatricula());
        }
        if (actualizada.getDestino() != null && !actualizada.getDestino().isBlank()) {
            existente.setDestino(actualizada.getDestino());
        }
        if (actualizada.getPilotoAlMando() != null && !actualizada.getPilotoAlMando().isBlank()) {
            existente.setPilotoAlMando(actualizada.getPilotoAlMando());
        }
        if (actualizada.getCodigoQr() != null && !actualizada.getCodigoQr().isBlank()) {
            existente.setCodigoQr(actualizada.getCodigoQr());
        }
        if (actualizada.getEstadoPista() != null && !actualizada.getEstadoPista().isBlank()) {
            existente.setEstadoPista(actualizada.getEstadoPista());
        }
        return repository.save(existente);
    }

    @Transactional
    public OperacionPista habilitarOperacion(Long id) {
        OperacionPista existente = buscarPorId(id);
        if ("CERRADA".equalsIgnoreCase(existente.getEstadoPista())) {
            throw new IllegalStateException("No se puede habilitar un vuelo con la pista cerrada");
        }
        verificarDisponibilidadAeronave(existente.getMatricula(), existente.getAeronave());
        existente.setEstadoPista("HABILITADO EN PISTA");
        return repository.save(existente);
    }

    @Transactional
    public OperacionPista registrarVueloYHorometro(Long id, OperacionPista datos) {
        OperacionPista existente = buscarPorId(id);
        if ("CERRADA".equalsIgnoreCase(existente.getEstadoPista())) {
            throw new IllegalStateException("No se puede habilitar un vuelo con la pista cerrada");
        }

        String matricula = existente.getMatricula() != null && !existente.getMatricula().isBlank()
                ? existente.getMatricula()
                : extraerMatricula(existente.getAeronave());

        Optional<Aeronave> aeronaveOpt = aeronaveRepository.findByMatriculaIgnoreCase(matricula);
        if (aeronaveOpt.isPresent()) {
            Aeronave avion = aeronaveOpt.get();
            if (Boolean.TRUE.equals(avion.getEnMantenimiento())
                    || avion.getHorometroActual() >= avion.getHorometroMaxMantenimiento()) {
                throw new IllegalStateException(
                        "BLOQUEO LEGAL DGAC: La aeronave " + avion.getMatricula()
                                + " alcanzó su límite de horas (" + avion.getHorometroMaxMantenimiento()
                                + " hrs) y está FUERA DE VUELO en mantenimiento obligatorio."
                );
            }
        }

        Double ini = datos.getHorometroInicial();
        Double fin = datos.getHorometroFinal();

        if (ini == null || fin == null) {
            throw new IllegalStateException("Debe registrar horómetro inicial y final (formato XXXX.X)");
        }
        if (ini < 1000.0 || ini > 9999.9 || fin < 1000.0 || fin > 9999.9) {
            throw new IllegalStateException("El horómetro debe tener 4 dígitos y 1 decimal (1000.0 a 9999.9)");
        }
        if (fin <= ini) {
            throw new IllegalStateException("El horómetro siempre debe ir en aumento (final debe ser mayor que inicial)");
        }

        double deltaHoras = Math.round((fin - ini) * 10.0) / 10.0;
        if (deltaHoras > 0.4) {
            throw new IllegalStateException(
                    "El tiempo de vuelo recreativo no debe exceder 20 minutos (máximo 0.4 hrs de horómetro por bloque)."
            );
        }

        if (aeronaveOpt.isPresent()) {
            Aeronave avion = aeronaveOpt.get();
            if (ini < avion.getHorometroActual()) {
                throw new IllegalStateException(
                        "El horómetro inicial (" + ini + ") no puede ser menor al último registro oficial de la aeronave ("
                                + avion.getHorometroActual() + ")."
                );
            }
            avion.setHorometroActual(fin);
            if (fin >= avion.getHorometroMaxMantenimiento()) {
                avion.setEnMantenimiento(true);
            }
            aeronaveRepository.save(avion);
        }

        existente.setMatricula(matricula);
        existente.setHorometroInicial(ini);
        existente.setHorometroFinal(fin);
        existente.setDuracionMinutos((int) Math.round(deltaHoras * 60.0));
        if (datos.getPilotoAlMando() != null && !datos.getPilotoAlMando().isBlank()) {
            existente.setPilotoAlMando(datos.getPilotoAlMando());
        }
        if (datos.getDestino() != null && !datos.getDestino().isBlank()) {
            existente.setDestino(datos.getDestino());
        }
        if (datos.getHoraLocal() != null && !datos.getHoraLocal().isBlank()) {
            existente.setHoraLocal(datos.getHoraLocal());
        } else if (existente.getHoraLocal() == null || existente.getHoraLocal().isBlank()) {
            existente.setHoraLocal(existente.getHorario().split("-")[0].trim());
        }
        existente.setEstadoPista("HABILITADO EN PISTA");

        return repository.save(existente);
    }

    public ReporteDiarioDto generarReporteDiario(String fecha) {
        List<OperacionPista> vuelos = repository.findAll().stream()
                .filter(op -> "HABILITADO EN PISTA".equalsIgnoreCase(op.getEstadoPista()))
                .filter(op -> fecha == null || fecha.isBlank() || fecha.equalsIgnoreCase(op.getFecha()))
                .sorted(Comparator
                        .comparing((OperacionPista op) -> op.getMatricula() != null ? op.getMatricula() : "")
                        .thenComparing(op -> op.getHoraLocal() != null ? op.getHoraLocal() : op.getHorario()))
                .toList();

        double totalHoras = vuelos.stream()
                .mapToDouble(op -> {
                    if (op.getHorometroInicial() != null && op.getHorometroFinal() != null) {
                        return Math.max(0.0, op.getHorometroFinal() - op.getHorometroInicial());
                    }
                    return 0.0;
                })
                .sum();
        totalHoras = Math.round(totalHoras * 10.0) / 10.0;

        List<Aeronave> flota = aeronaveRepository.findAll();

        return new ReporteDiarioDto(
                (fecha == null || fecha.isBlank()) ? "Resumen General de Operaciones" : fecha,
                List.of(
                        "Director de Operaciones (Bitácora oficial exigible por DGAC)",
                        "Director de Mantenimiento (Control estricto de horas de vuelo)"
                ),
                vuelos.size(),
                totalHoras,
                "Documento operacional CAPEBE: Registro riguroso de horómetro conforme al Código Aeronáutico y normativa DGAC. Toda aeronave que alcance su límite horario queda automáticamente FUERA DE VUELO.",
                vuelos,
                flota
        );
    }

    @Transactional
    public void eliminar(Long id) {
        OperacionPista existente = buscarPorId(id);
        repository.delete(existente);
    }

    private void verificarDisponibilidadAeronave(String matricula, String aeronaveTexto) {
        String mat = (matricula != null && !matricula.isBlank()) ? matricula : extraerMatricula(aeronaveTexto);
        aeronaveRepository.findByMatriculaIgnoreCase(mat).ifPresent(avion -> {
            if (Boolean.TRUE.equals(avion.getEnMantenimiento())
                    || avion.getHorometroActual() >= avion.getHorometroMaxMantenimiento()) {
                throw new IllegalStateException(
                        "BLOQUEO LEGAL DGAC: La aeronave " + avion.getMatricula()
                                + " está FUERA DE VUELO por límite de mantenimiento."
                );
            }
        });
    }

    private String extraerMatricula(String aeronave) {
        if (aeronave == null || aeronave.isBlank()) return "CC-AAA";
        String[] partes = aeronave.trim().split("\\s+");
        return partes[0].toUpperCase();
    }
}
