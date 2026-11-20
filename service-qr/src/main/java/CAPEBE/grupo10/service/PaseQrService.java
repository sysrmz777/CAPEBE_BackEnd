package CAPEBE.grupo10.service;

import CAPEBE.grupo10.model.PaseQr;
import CAPEBE.grupo10.repository.PaseQrRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class PaseQrService {

    private final PaseQrRepository repository;

    public PaseQrService(PaseQrRepository repository) {
        this.repository = repository;
    }

    public List<PaseQr> obtenerTodos() {
        return repository.findAll();
    }

    public PaseQr buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pase QR no encontrado"));
    }

    public PaseQr buscarPorCodigo(String codigoQr) {
        return repository.findByCodigoQrIgnoreCase(codigoQr.trim())
                .orElseThrow(() -> new RuntimeException("Código QR/verificación no registrado en CAPEBE"));
    }

    @Transactional
    public PaseQr generarQrVuelo(PaseQr pase) {
        if (pase.getCodigoQr() != null && !pase.getCodigoQr().isBlank()) {
            var yaExiste = repository.findByCodigoQrIgnoreCase(pase.getCodigoQr().trim());
            if (yaExiste.isPresent()) {
                return yaExiste.get();
            }
        }
        int plazas = (pase.getPlazasAeronave() != null && pase.getPlazasAeronave() == 6) ? 6 : 4;
        pase.setPlazasAeronave(plazas);
        int maxPasajeros = plazas - 1;
        int cant = (pase.getCantidadPasajeros() != null) ? pase.getCantidadPasajeros() : 1;
        if (cant < 1 || cant > maxPasajeros) {
            throw new IllegalStateException(
                    "Capacidad excedida: avión de " + plazas + " plazas permite máximo " + maxPasajeros + " pasajeros."
            );
        }
        pase.setCantidadPasajeros(cant);

        if (pase.getEsSocio() == null) pase.setEsSocio(true);

        if (pase.getCodigoQr() == null || pase.getCodigoQr().isBlank()) {
            String prefijoBase = pase.getBase() != null && pase.getBase().toUpperCase().contains("TALCAHUANO") ? "SCIE" : "SCTB";
            String sufijo = UUID.randomUUID().toString().substring(0, 4).toUpperCase();
            pase.setCodigoQr("CAPEBE-" + prefijoBase + "-" + sufijo);
        }
        pase.setEstadoQr("QR EMITIDO - PENDIENTE EN PISTA");
        if (pase.getPistaDespejada() == null) pase.setPistaDespejada(true);
        if (pase.getCombustibleOk() == null) pase.setCombustibleOk(true);
        if (pase.getLucesOk() == null) pase.setLucesOk(true);
        if (pase.getDestino() == null || pase.getDestino().isBlank()) {
            pase.setDestino(pase.getBase() != null && pase.getBase().toUpperCase().contains("TALCAHUANO")
                    ? "Circuito Bahía Concepción (SCIE)"
                    : "Circuito Precordillera Santiago (SCTB)");
        }
        if (pase.getPilotoAlMando() == null || pase.getPilotoAlMando().isBlank()) {
            pase.setPilotoAlMando("Cap. Rodrigo Fuentes");
        }
        return repository.save(pase);
    }

    @Transactional
    public PaseQr validarQrEnPista(String codigoQr, PaseQr datosValidacion) {
        PaseQr existente = buscarPorCodigo(codigoQr);

        if ("QR VALIDADO - HABILITADO EN PISTA".equalsIgnoreCase(existente.getEstadoQr())) {
            throw new IllegalStateException("Este código QR ya fue utilizado en pista");
        }

        if (datosValidacion.getHorometroInicial() != null && datosValidacion.getHorometroFinal() != null) {
            double ini = datosValidacion.getHorometroInicial();
            double fin = datosValidacion.getHorometroFinal();
            if (fin <= ini) {
                throw new IllegalStateException("El horómetro final debe ser estrictamente mayor al horómetro inicial");
            }
            if ((fin - ini) > 0.41) {
                throw new IllegalStateException("El tiempo de vuelo excede el máximo recreativo de 20 minutos (0.4 hrs horómetro)");
            }
            existente.setHorometroInicial(ini);
            existente.setHorometroFinal(fin);
        }

        existente.setEncargadoPista(datosValidacion.getEncargadoPista());
        existente.setObservaciones(datosValidacion.getObservaciones());
        if (datosValidacion.getPilotoAlMando() != null && !datosValidacion.getPilotoAlMando().isBlank()) {
            existente.setPilotoAlMando(datosValidacion.getPilotoAlMando());
        }
        existente.setPistaDespejada(datosValidacion.getPistaDespejada());
        existente.setCombustibleOk(datosValidacion.getCombustibleOk());
        existente.setLucesOk(datosValidacion.getLucesOk());

        if (Boolean.FALSE.equals(datosValidacion.getPistaDespejada())
                || Boolean.FALSE.equals(datosValidacion.getCombustibleOk())
                || Boolean.FALSE.equals(datosValidacion.getLucesOk())) {
            existente.setEstadoQr("QR RECHAZADO EN LOSA");
        } else {
            existente.setEstadoQr("QR VALIDADO - HABILITADO EN PISTA");
        }

        return repository.save(existente);
    }

    @Transactional
    public void eliminar(Long id) {
        PaseQr existente = buscarPorId(id);
        repository.delete(existente);
    }
}
