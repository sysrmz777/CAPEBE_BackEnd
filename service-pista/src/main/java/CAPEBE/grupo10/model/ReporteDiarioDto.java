package CAPEBE.grupo10.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReporteDiarioDto {

    private String fechaReporte;
    private List<String> destinatarios;
    private Integer totalVuelosRealizados;
    private Double totalHorasVoladas;
    private String notaLegalDgac;
    private List<OperacionPista> vuelosRealizados;
    private List<Aeronave> estadoFlotaMantenimiento;
}
