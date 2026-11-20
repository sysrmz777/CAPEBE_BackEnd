package com.capebe.vuelos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.capebe.vuelos.entities.Vuelo;
import com.capebe.vuelos.services.VueloService;

@SpringBootTest
class CapebeBackendApplicationTests {

    @Autowired
    private VueloService vueloService;

    @Test
    void testCrearVueloConDatosCompletosSocioFormador() {
        Vuelo nuevo = new Vuelo(
                null,
                "Álvaro Ramírez",
                "18.452.310-K",
                "Casa Matriz Alameda",
                true,
                "+56987654321",
                3,
                "2026-11-21",
                "10:00 - 11:00",
                "CC-AAA Cessna 172",
                4,
                "Tobalaba",
                "Circuito Precordillera Santiago (SCTB)",
                "Cap. Rodrigo Fuentes",
                null,
                78.0,
                1245.2
        );
        Vuelo guardado = vueloService.crear(nuevo);

        assertNotNull(guardado.getId());
        assertEquals("18.452.310-K", guardado.getRut());
        assertEquals("Casa Matriz Alameda", guardado.getOficinaBanco());
        assertEquals(Boolean.TRUE, guardado.getEsSocio());
        assertEquals(3, guardado.getCantidadPasajeros());
        assertNotNull(guardado.getCodigoVerificacion());
        assertTrue(guardado.getCodigoVerificacion().startsWith("CAPEBE-SCTB-"));
    }

    @Test
    void testAvionSeisPlazasPermiteCincoPasajeros() {
        Vuelo nuevo = new Vuelo(
                null, "Carolina Soto", "16.320.111-2", "Sucursal Concepción",
                false, "+56912345678", 5, "2026-11-22", "11:00 - 12:00",
                "CC-DDD Cessna 206", 6, "Talcahuano", null, null, null, 0.0, 0.0
        );
        Vuelo guardado = vueloService.crear(nuevo);

        assertEquals(6, guardado.getPlazasAeronave());
        assertEquals(5, guardado.getCantidadPasajeros());
        assertTrue(guardado.getCodigoVerificacion().startsWith("CAPEBE-SCIE-"));
    }

    @Test
    void testRechazaExcesoPasajerosEnAvionCuatroPlazas() {
        Vuelo invalido = new Vuelo(
                null, "Juan Pérez", "15.111.222-3", "Sucursal Providencia",
                true, "+56999887766", 4, "2026-11-21", "12:00 - 13:00",
                "CC-AAA Cessna 172", 4, "Tobalaba", null, null, null, 0.0, 0.0
        );

        assertThrows(IllegalArgumentException.class, () -> vueloService.crear(invalido));
    }

    @Test
    void testListarVuelosYBuscarPorFechaYCodigo() {
        Vuelo creado = vueloService.crear(new Vuelo(
                null, "Socio Test", "12.345.678-5", "Sucursal Ñuñoa",
                true, "+56955443322", 2, "2026-11-28", "15:00 - 16:00",
                "CC-BBB Piper PA-28", 4, "Tobalaba", null, null, "CAPEBE-SCTB-TEST", 0.0, 0.0
        ));

        List<Vuelo> porFecha = vueloService.obtenerPorFecha("2026-11-28");
        Optional<Vuelo> porCodigo = vueloService.obtenerPorCodigo("CAPEBE-SCTB-TEST");

        assertTrue(porFecha.size() >= 1);
        assertTrue(porCodigo.isPresent());
        assertEquals(creado.getId(), porCodigo.get().getId());
    }
}
