package com.capebe.vuelos.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.capebe.vuelos.entities.Vuelo;
import com.capebe.vuelos.services.VueloService;

@RestController
@RequestMapping("api/vuelos")
@CrossOrigin(origins = "*")
public class VueloController {

    private final VueloService vueloService;

    public VueloController(VueloService vueloService) {
        this.vueloService = vueloService;
    }

    @GetMapping
    public List<Vuelo> listarVuelos() {
        return vueloService.obtenerTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vuelo> obtenerPorId(@PathVariable Long id) {
        return vueloService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/fecha/{fecha}")
    public List<Vuelo> listarPorFecha(@PathVariable String fecha) {
        return vueloService.obtenerPorFecha(fecha);
    }

    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<Vuelo> obtenerPorCodigo(@PathVariable String codigo) {
        return vueloService.obtenerPorCodigo(codigo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Vuelo guardarVuelo(@RequestBody Vuelo vuelo) {
        return vueloService.crear(vuelo);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleValidation(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
    }
}
