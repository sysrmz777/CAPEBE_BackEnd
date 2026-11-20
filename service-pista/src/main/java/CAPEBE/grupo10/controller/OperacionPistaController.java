package CAPEBE.grupo10.controller;

import CAPEBE.grupo10.model.Aeronave;
import CAPEBE.grupo10.model.OperacionPista;
import CAPEBE.grupo10.model.ReporteDiarioDto;
import CAPEBE.grupo10.service.OperacionPistaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pista")
@CrossOrigin(origins = "*")
public class OperacionPistaController {

    private final OperacionPistaService service;

    public OperacionPistaController(OperacionPistaService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<OperacionPista>> listar() {
        return ResponseEntity.ok(service.obtenerTodas());
    }

    @GetMapping("/aeronaves")
    public ResponseEntity<List<Aeronave>> listarAeronaves() {
        return ResponseEntity.ok(service.obtenerAeronaves());
    }

    @GetMapping("/reporte")
    public ResponseEntity<ReporteDiarioDto> obtenerReporteDiario(
            @RequestParam(required = false) String fecha
    ) {
        return ResponseEntity.ok(service.generarReporteDiario(fecha));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OperacionPista> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping("/base/{base}")
    public ResponseEntity<List<OperacionPista>> buscarPorBase(@PathVariable String base) {
        return ResponseEntity.ok(service.buscarPorBase(base));
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<OperacionPista>> buscarPorEstado(@PathVariable String estado) {
        return ResponseEntity.ok(service.buscarPorEstado(estado));
    }

    @PostMapping
    public ResponseEntity<OperacionPista> guardar(@Valid @RequestBody OperacionPista operacion) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(operacion));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OperacionPista> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody OperacionPista operacion
    ) {
        return ResponseEntity.ok(service.actualizar(id, operacion));
    }

    @PutMapping("/{id}/habilitar")
    public ResponseEntity<OperacionPista> habilitarOperacion(@PathVariable Long id) {
        return ResponseEntity.ok(service.habilitarOperacion(id));
    }

    @PutMapping("/{id}/registrar-vuelo")
    public ResponseEntity<OperacionPista> registrarVueloYHorometro(
            @PathVariable Long id,
            @RequestBody OperacionPista datosVuelo
    ) {
        return ResponseEntity.ok(service.registrarVueloYHorometro(id, datosVuelo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
