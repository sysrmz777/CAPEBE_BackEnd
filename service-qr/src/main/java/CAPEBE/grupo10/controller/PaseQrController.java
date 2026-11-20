package CAPEBE.grupo10.controller;

import CAPEBE.grupo10.model.PaseQr;
import CAPEBE.grupo10.service.PaseQrService;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/qr")
@CrossOrigin(origins = "*")
public class PaseQrController {

    private final PaseQrService service;

    public PaseQrController(PaseQrService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<PaseQr>> listar() {
        return ResponseEntity.ok(service.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaseQr> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping("/codigo/{codigoQr}")
    public ResponseEntity<PaseQr> leerPorCodigo(@PathVariable String codigoQr) {
        return ResponseEntity.ok(service.buscarPorCodigo(codigoQr));
    }

    @PostMapping("/generar")
    public ResponseEntity<PaseQr> generarQr(@Valid @RequestBody PaseQr pase) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.generarQrVuelo(pase));
    }

    @PutMapping("/validar/{codigoQr}")
    public ResponseEntity<PaseQr> validarQrEnPista(
            @PathVariable String codigoQr,
            @RequestBody PaseQr datosValidacion
    ) {
        return ResponseEntity.ok(service.validarQrEnPista(codigoQr, datosValidacion));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
