package CAPEBE.grupo10.repository;

import CAPEBE.grupo10.model.OperacionPista;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OperacionPistaRepository extends JpaRepository<OperacionPista, Long> {

    List<OperacionPista> findByBaseIgnoreCase(String base);

    List<OperacionPista> findByEstadoPistaIgnoreCase(String estadoPista);

    Optional<OperacionPista> findByCodigoQrIgnoreCase(String codigoQr);
}
