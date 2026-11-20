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
@Table(name = "pase_qr")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaseQr {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idQr;

    private String codigoQr;

    // Datos obligatorios de identificación del invitado/socio (Socio Formador CAPEBE)
    @NotBlank(message = "El nombre completo del socio/invitado es obligatorio")
    private String socio;

    private String rut;

    private String oficinaBanco;

    private Boolean esSocio;

    private String telefonoMovil;

    private Integer cantidadPasajeros;

    private Integer plazasAeronave;

    // Planificación del vuelo (bloque 1h, vuelo máx 20 min)
    @NotBlank(message = "La fecha es obligatoria")
    private String fecha;

    @NotBlank(message = "El horario es obligatorio")
    private String horario;

    @NotBlank(message = "La base es obligatoria")
    private String base;

    @NotBlank(message = "La aeronave es obligatoria")
    private String aeronave;

    private String destino;

    private String pilotoAlMando;

    // Control en pista y horómetro (4 dígitos + 1 decimal)
    private String encargadoPista;

    private String observaciones;

    private Boolean pistaDespejada;

    private Boolean combustibleOk;

    private Boolean lucesOk;

    private Double horometroInicial;

    private Double horometroFinal;

    private String estadoQr;
}
