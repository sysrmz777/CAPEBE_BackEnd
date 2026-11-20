package CAPEBE.grupo10.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "operacion_pista")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OperacionPista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idOperacion;

    @NotBlank(message = "La fecha es obligatoria")
    private String fecha;               // YYYY-MM-DD (fines de semana / festivos)

    @NotBlank(message = "El horario es obligatorio")
    private String horario;             // Bloque 1 hora (ej: 10:00 - 11:00)

    private String horaLocal;           // Hora local efectiva de vuelo (ej: 10:15)

    @NotBlank(message = "La base es obligatoria")
    private String base;                // Tobalaba / Talcahuano

    private String destino;             // Circuito / destino del vuelo

    @NotBlank(message = "La aeronave es obligatoria")
    private String aeronave;            // Ej: CC-AAA Cessna 172

    private String matricula;           // Ej: CC-AAA

    private String pilotoAlMando;       // Piloto al mando (requerido para reporte DGAC)

    private String pasajero;            // Pasajero/socio principal

    private Integer cantidadPasajeros;  // 1..3 (4 plazas) o 1..5 (6 plazas)

    private String codigoQr;            // Código QR / verificación único

    private Double horometroInicial;    // 4 dígitos + 1 decimal (ej: 1245.2)

    private Double horometroFinal;      // Siempre en aumento (ej: 1245.5)

    private Integer duracionMinutos;    // Tiempo efectivo (<= 20 min)

    private String estadoPista;         // PENDIENTE EN LOSA / HABILITADO EN PISTA / CERRADA
}
