package CAPEBE.grupo10.service;

import CAPEBE.grupo10.model.Aeronave;
import CAPEBE.grupo10.model.OperacionPista;
import CAPEBE.grupo10.model.ReporteDiarioDto;
import CAPEBE.grupo10.repository.AeronaveRepository;
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
public class OperacionPistaServiceTest {

    @Autowired
    private OperacionPistaService service;

    @Autowired
    private AeronaveRepository aeronaveRepository;

    @Test
    public void testGuardarOperacionConDefaults() {
        OperacionPista op = new OperacionPista();
        op.setFecha("2026-11-21");
        op.setHorario("10:00 - 11:00");
        op.setBase("Tobalaba");
        op.setAeronave("CC-AAA Cessna 172");

        OperacionPista res = service.guardar(op);

        assertNotNull(res.getIdOperacion());
        assertEquals("CC-AAA", res.getMatricula());
        assertEquals("PENDIENTE EN LOSA", res.getEstadoPista());
        assertNotNull(res.getCodigoQr());
        assertTrue(res.getCodigoQr().startsWith("CAPEBE-SCTB-"));
    }

    @Test
    public void testObtenerTodasYFlotaAeronaves() {
        List<OperacionPista> operaciones = service.obtenerTodas();
        List<Aeronave> flota = service.obtenerAeronaves();

        assertFalse(operaciones.isEmpty());
        assertTrue(flota.size() >= 4);
    }

    @Test
    public void testRegistrarVueloYHorometroValidoActualizaAeronave() {
        OperacionPista creada = service.guardar(new OperacionPista(
                null, "2026-11-21", "10:00 - 11:00", "10:15", "Tobalaba",
                "Circuito Precordillera Santiago (SCTB)", "CC-AAA Cessna 172", "CC-AAA",
                "Cap. Rodrigo Fuentes", "Álvaro Ramírez", 3, "CAPEBE-SCTB-TEST1",
                1245.2, null, null, "PENDIENTE EN LOSA"
        ));

        OperacionPista datos = new OperacionPista();
        datos.setHorometroInicial(1245.2);
        datos.setHorometroFinal(1245.5);
        datos.setPilotoAlMando("Cap. Rodrigo Fuentes");

        OperacionPista res = service.registrarVueloYHorometro(creada.getIdOperacion(), datos);
        Aeronave avionActualizado = aeronaveRepository.findByMatriculaIgnoreCase("CC-AAA").orElseThrow();

        assertEquals("HABILITADO EN PISTA", res.getEstadoPista());
        assertEquals(1245.5, res.getHorometroFinal());
        assertEquals(18, res.getDuracionMinutos());
        assertEquals(1245.5, avionActualizado.getHorometroActual());
        assertEquals(Boolean.FALSE, avionActualizado.getEnMantenimiento());
    }

    @Test
    public void testBloqueaAutomaticamenteAeronaveAlAlcanzarLimiteMantenimientoDgac() {
        Aeronave avion = aeronaveRepository.findByMatriculaIgnoreCase("CC-BBB").orElseThrow();
        avion.setHorometroActual(2199.7);
        avion.setHorometroMaxMantenimiento(2200.0);
        avion.setEnMantenimiento(false);
        aeronaveRepository.save(avion);

        OperacionPista creada = service.guardar(new OperacionPista(
                null, "2026-11-21", "11:00 - 12:00", "11:10", "Tobalaba",
                "Circuito SCTB", "CC-BBB Piper PA-28", "CC-BBB",
                "Cap. Marcela Paredes", "Camila Morales", 2, "CAPEBE-SCTB-TEST2",
                2199.7, null, null, "PENDIENTE EN LOSA"
        ));

        OperacionPista datos = new OperacionPista();
        datos.setHorometroInicial(2199.7);
        datos.setHorometroFinal(2200.0);

        service.registrarVueloYHorometro(creada.getIdOperacion(), datos);

        Aeronave despues = aeronaveRepository.findByMatriculaIgnoreCase("CC-BBB").orElseThrow();
        assertEquals(Boolean.TRUE, despues.getEnMantenimiento());
    }

    @Test
    public void testRechazaAeronaveEnMantenimiento() {
        OperacionPista creada = service.guardar(new OperacionPista(
                null, "2026-11-21", "12:00 - 13:00", "12:15", "Tobalaba",
                "Circuito SCTB", "CC-EEE Piper PA-32 Saratoga", "CC-EEE",
                "Cap. Rodrigo Fuentes", "Socio Test", 5, "CAPEBE-SCTB-TEST3",
                4100.0, null, null, "PENDIENTE EN LOSA"
        ));

        OperacionPista datos = new OperacionPista();
        datos.setHorometroInicial(4100.0);
        datos.setHorometroFinal(4100.3);

        assertThrows(IllegalStateException.class,
                () -> service.registrarVueloYHorometro(creada.getIdOperacion(), datos));
    }

    @Test
    public void testRechazaHorometroDecreciente() {
        OperacionPista creada = service.guardar(new OperacionPista(
                null, "2026-11-21", "15:00 - 16:00", "15:10", "Tobalaba",
                "Circuito SCTB", "CC-AAA Cessna 172", "CC-AAA",
                "Cap. Rodrigo Fuentes", "Socio Test", 2, "CAPEBE-SCTB-TEST4",
                1245.5, null, null, "PENDIENTE EN LOSA"
        ));

        OperacionPista datos = new OperacionPista();
        datos.setHorometroInicial(1245.5);
        datos.setHorometroFinal(1245.2);

        assertThrows(IllegalStateException.class,
                () -> service.registrarVueloYHorometro(creada.getIdOperacion(), datos));
    }

    @Test
    public void testRechazaVueloMayorA20Minutos() {
        OperacionPista creada = service.guardar(new OperacionPista(
                null, "2026-11-21", "16:00 - 17:00", "16:10", "Tobalaba",
                "Circuito SCTB", "CC-AAA Cessna 172", "CC-AAA",
                "Cap. Rodrigo Fuentes", "Socio Test", 2, "CAPEBE-SCTB-TEST5",
                1245.2, null, null, "PENDIENTE EN LOSA"
        ));

        OperacionPista datos = new OperacionPista();
        datos.setHorometroInicial(1245.2);
        datos.setHorometroFinal(1246.0);

        assertThrows(IllegalStateException.class,
                () -> service.registrarVueloYHorometro(creada.getIdOperacion(), datos));
    }

    @Test
    public void testGenerarReporteDiarioDgacYMantenimiento() {
        OperacionPista creada = service.guardar(new OperacionPista(
                null, "2026-12-08", "10:00 - 11:00", "10:15", "Tobalaba",
                "Circuito Precordillera", "CC-AAA Cessna 172", "CC-AAA",
                "Cap. Rodrigo Fuentes", "Álvaro Ramírez", 3, "CAPEBE-SCTB-REP1",
                1245.2, null, null, "PENDIENTE EN LOSA"
        ));

        OperacionPista datos = new OperacionPista();
        datos.setHorometroInicial(1245.2);
        datos.setHorometroFinal(1245.5);
        datos.setPilotoAlMando("Cap. Rodrigo Fuentes");
        service.registrarVueloYHorometro(creada.getIdOperacion(), datos);

        ReporteDiarioDto reporte = service.generarReporteDiario("2026-12-08");

        assertNotNull(reporte);
        assertEquals(1, reporte.getTotalVuelosRealizados());
        assertEquals(0.3, reporte.getTotalHorasVoladas());
        assertEquals(2, reporte.getDestinatarios().size());
        assertFalse(reporte.getEstadoFlotaMantenimiento().isEmpty());
    }
}
