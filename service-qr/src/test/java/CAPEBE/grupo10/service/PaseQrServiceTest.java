package CAPEBE.grupo10.service;

import CAPEBE.grupo10.model.PaseQr;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
public class PaseQrServiceTest {

    @Autowired
    private PaseQrService service;

    @Test
    public void testGenerarQrVueloTobalabaConDatosInvitado() {
        PaseQr nuevo = new PaseQr(
                null, null, "Álvaro Ramírez", "18.452.310-K", "Casa Matriz Alameda", true,
                "+56987654321", 3, 4, "2026-11-21", "10:00 - 11:00", "Tobalaba",
                "CC-AAA Cessna 172", null, null, null, null, true, true, true, null, null, null
        );

        PaseQr generado = service.generarQrVuelo(nuevo);

        assertNotNull(generado.getIdQr());
        assertNotNull(generado.getCodigoQr());
        assertTrue(generado.getCodigoQr().startsWith("CAPEBE-SCTB-"));
        assertEquals("QR EMITIDO - PENDIENTE EN PISTA", generado.getEstadoQr());
        assertEquals("18.452.310-K", generado.getRut());
    }

    @Test
    public void testRechazaExcesoPasajerosAlGenerarQr() {
        PaseQr invalido = new PaseQr(
                null, null, "Socio Test", "12.345.678-5", "Sucursal Centro", true,
                "+56912345678", 4, 4, "2026-11-21", "10:00 - 11:00", "Tobalaba",
                "CC-AAA Cessna 172", null, null, null, null, true, true, true, null, null, null
        );
        assertThrows(IllegalStateException.class, () -> service.generarQrVuelo(invalido));
    }

    @Test
    public void testValidarQrEnPistaExitosoConHorometro() {
        PaseQr validacion = new PaseQr();
        validacion.setEncargadoPista("Encargado Pista");
        validacion.setPilotoAlMando("Cap. Rodrigo Fuentes");
        validacion.setObservaciones("Todo en orden en pista");
        validacion.setPistaDespejada(true);
        validacion.setCombustibleOk(true);
        validacion.setLucesOk(true);
        validacion.setHorometroInicial(1245.2);
        validacion.setHorometroFinal(1245.5);

        PaseQr resultado = service.validarQrEnPista("CAPEBE-SCTB-1001", validacion);

        assertEquals("QR VALIDADO - HABILITADO EN PISTA", resultado.getEstadoQr());
        assertEquals(1245.5, resultado.getHorometroFinal());
        assertEquals("Todo en orden en pista", resultado.getObservaciones());
    }

    @Test
    public void testRechazaHorometroDecrecienteAlValidarQr() {
        PaseQr validacion = new PaseQr();
        validacion.setHorometroInicial(1245.5);
        validacion.setHorometroFinal(1245.2);
        validacion.setPistaDespejada(true);
        validacion.setCombustibleOk(true);
        validacion.setLucesOk(true);

        assertThrows(IllegalStateException.class,
                () -> service.validarQrEnPista("CAPEBE-SCTB-1002", validacion));
    }

    @Test
    public void testRechazarQrYaUtilizado() {
        PaseQr validacion = new PaseQr();
        validacion.setEncargadoPista("Encargado Pista");
        validacion.setObservaciones("Primer uso");
        validacion.setPistaDespejada(true);
        validacion.setCombustibleOk(true);
        validacion.setLucesOk(true);
        validacion.setHorometroInicial(3410.0);
        validacion.setHorometroFinal(3410.3);

        service.validarQrEnPista("CAPEBE-SCIE-1003", validacion);

        assertThrows(IllegalStateException.class,
                () -> service.validarQrEnPista("CAPEBE-SCIE-1003", validacion));
    }

    @Test
    public void testObtenerTodos() {
        List<PaseQr> todos = service.obtenerTodos();
        assertFalse(todos.isEmpty());
        assertTrue(todos.size() >= 5);
    }
}
