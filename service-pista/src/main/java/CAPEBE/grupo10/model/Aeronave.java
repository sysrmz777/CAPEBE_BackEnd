package CAPEBE.grupo10.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "aeronaves")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Aeronave {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAeronave;

    private String matricula;               // Ej: CC-AAA
    private String modelo;                  // Ej: Cessna 172
    private Integer plazasTotales;          // 4 o 6 plazas
    private Integer asientosDisponibles;    // 3 o 5 pasajeros (descontando piloto)
    private String baseAerea;               // Tobalaba / Talcahuano
    private Double horometroActual;         // 4 dígitos + 1 decimal (ej: 1245.2)
    private Double horometroMaxMantenimiento; // Límite legal DGAC de mantenimiento
    private Boolean enMantenimiento;        // true = FUERA DE VUELO (bloqueo legal DGAC)
}
