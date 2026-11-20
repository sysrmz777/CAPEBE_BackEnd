package com.capebe.vuelos.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.capebe.vuelos.entities.Vuelo;
import com.capebe.vuelos.repository.VueloRepository;

@Service
public class VueloServiceImpl implements VueloService {

    private final VueloRepository vueloRepo;
    private final RestTemplate restTemplate;

    @Value("${services.sync.enabled:true}")
    private boolean syncEnabled;

    @Value("${services.pista.url:http://localhost:8083}")
    private String pistaServiceUrl;

    @Value("${services.qr.url:http://localhost:8084}")
    private String qrServiceUrl;

    public VueloServiceImpl(VueloRepository vueloRepository) {
        this.vueloRepo = vueloRepository;
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(3000);
        factory.setReadTimeout(4000);
        this.restTemplate = new RestTemplate(factory);
    }

    @Override
    public List<Vuelo> obtenerTodos() {
        return vueloRepo.findAll();
    }

    @Override
    public Optional<Vuelo> obtenerPorId(Long id) {
        return vueloRepo.findById(id);
    }

    @Override
    public List<Vuelo> obtenerPorFecha(String fecha) {
        return vueloRepo.findByFecha(fecha);
    }

    @Override
    public Optional<Vuelo> obtenerPorCodigo(String codigoVerificacion) {
        return vueloRepo.findByCodigoVerificacionIgnoreCase(codigoVerificacion);
    }

    @Override
    public Vuelo crear(Vuelo vuelo) {
        int plazas = (vuelo.getPlazasAeronave() != null && vuelo.getPlazasAeronave() == 6) ? 6 : 4;
        vuelo.setPlazasAeronave(plazas);
        int maxPasajeros = plazas - 1; // Descontando al piloto al mando (3 o 5 asientos disponibles)

        int cantPasajeros = (vuelo.getCantidadPasajeros() != null) ? vuelo.getCantidadPasajeros() : 1;
        if (cantPasajeros < 1 || cantPasajeros > maxPasajeros) {
            throw new IllegalArgumentException(
                    "Capacidad excedida: un avión de " + plazas + " plazas permite máximo "
                            + maxPasajeros + " pasajeros (descontando piloto)."
            );
        }
        vuelo.setCantidadPasajeros(cantPasajeros);

        if (vuelo.getEsSocio() == null) {
            vuelo.setEsSocio(true);
        }

        if (vuelo.getCodigoVerificacion() == null || vuelo.getCodigoVerificacion().isBlank()) {
            String prefijo = (vuelo.getBaseAerea() != null
                    && vuelo.getBaseAerea().toUpperCase().contains("TALCAHUANO")) ? "SCIE" : "SCTB";
            String sufijo = UUID.randomUUID().toString().substring(0, 4).toUpperCase();
            vuelo.setCodigoVerificacion("CAPEBE-" + prefijo + "-" + sufijo);
        }

        if (vuelo.getPilotoAlMando() == null || vuelo.getPilotoAlMando().isBlank()) {
            vuelo.setPilotoAlMando("Cap. Rodrigo Fuentes");
        }

        if (vuelo.getDestino() == null || vuelo.getDestino().isBlank()) {
            vuelo.setDestino(
                    vuelo.getBaseAerea() != null && vuelo.getBaseAerea().toUpperCase().contains("TALCAHUANO")
                            ? "Circuito Bahía Concepción (SCIE)"
                            : "Circuito Precordillera Santiago (SCTB)"
            );
        }

        Vuelo guardado = vueloRepo.save(vuelo);

        // Sincronización automática con service-qr (:8084) y service-pista (:8083)
        if (syncEnabled) {
            sincronizarConQrYPista(guardado);
        }

        return guardado;
    }

    private void sincronizarConQrYPista(Vuelo v) {
        String fecha = (v.getFecha() != null && !v.getFecha().isBlank()) ? v.getFecha() : "2026-11-21";
        String horario = (v.getBloqueHorario() != null && !v.getBloqueHorario().isBlank()) ? v.getBloqueHorario() : "10:00 - 11:00";
        String base = (v.getBaseAerea() != null && !v.getBaseAerea().isBlank()) ? v.getBaseAerea() : "Tobalaba";
        String aeronave = (v.getAeronave() != null && !v.getAeronave().isBlank()) ? v.getAeronave() : "CC-AAA Cessna 172";
        String pasajero = (v.getPasajero() != null && !v.getPasajero().isBlank()) ? v.getPasajero() : "Socio CAPEBE";

        // 1. Registrar en service-qr (:8084)
        try {
            Map<String, Object> qrBody = new HashMap<>();
            qrBody.put("codigoQr", v.getCodigoVerificacion());
            qrBody.put("socio", pasajero);
            qrBody.put("rut", v.getRut() != null ? v.getRut() : "");
            qrBody.put("oficinaBanco", v.getOficinaBanco() != null ? v.getOficinaBanco() : "");
            qrBody.put("esSocio", v.getEsSocio() != null ? v.getEsSocio() : true);
            qrBody.put("telefonoMovil", v.getTelefonoMovil() != null ? v.getTelefonoMovil() : "");
            qrBody.put("cantidadPasajeros", v.getCantidadPasajeros() != null ? v.getCantidadPasajeros() : 1);
            qrBody.put("plazasAeronave", v.getPlazasAeronave() != null ? v.getPlazasAeronave() : 4);
            qrBody.put("fecha", fecha);
            qrBody.put("horario", horario);
            qrBody.put("base", base);
            qrBody.put("aeronave", aeronave);
            qrBody.put("destino", v.getDestino());
            qrBody.put("pilotoAlMando", v.getPilotoAlMando());

            restTemplate.postForObject(qrServiceUrl + "/api/v1/qr/generar", qrBody, Object.class);
        } catch (Exception ignored) {
        }

        // 2. Registrar en service-pista (:8083)
        try {
            Map<String, Object> pistaBody = new HashMap<>();
            pistaBody.put("fecha", fecha);
            pistaBody.put("horario", horario);
            pistaBody.put("base", base);
            pistaBody.put("destino", v.getDestino());
            pistaBody.put("aeronave", aeronave);
            pistaBody.put("pilotoAlMando", v.getPilotoAlMando());
            pistaBody.put("pasajero", pasajero);
            pistaBody.put("cantidadPasajeros", v.getCantidadPasajeros() != null ? v.getCantidadPasajeros() : 1);
            pistaBody.put("codigoQr", v.getCodigoVerificacion());
            pistaBody.put("estadoPista", "PENDIENTE EN LOSA");

            restTemplate.postForObject(pistaServiceUrl + "/api/v1/pista", pistaBody, Object.class);
        } catch (Exception ignored) {
        }
    }
}
