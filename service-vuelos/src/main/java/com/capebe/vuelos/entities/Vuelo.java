package com.capebe.vuelos.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "vuelos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vuelo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Datos obligatorios del invitado / socio (Caso CAPEBE)
    private String pasajero;          // Nombres y Apellidos
    private String rut;               // RUT chileno
    private String oficinaBanco;      // Oficina BancoEstado
    private Boolean esSocio;          // Calidad de socio (Sí / No)
    private String telefonoMovil;     // Teléfono móvil principal
    private Integer cantidadPasajeros;// 1..3 (avión 4 plazas) o 1..5 (avión 6 plazas)

    // Planificación operativa (bloques 1 hora, máx 20 min vuelo, fines de semana / festivos)
    private String fecha;             // Formato YYYY-MM-DD
    private String bloqueHorario;     // Ej: "10:00 - 11:00"
    private String aeronave;          // Ej: "CC-AAA Cessna 172"
    private Integer plazasAeronave;   // 4 o 6 plazas (disponibles = plazas - 1 piloto)
    private String baseAerea;         // Tobalaba (SCTB) / Talcahuano (SCIE)
    private String destino;           // Circuito local / destino
    private String pilotoAlMando;     // Piloto asignado
    private String codigoVerificacion;// Código único para QR y embarque en pista

    private Double pesoKg;
    private Double horometro;
}
