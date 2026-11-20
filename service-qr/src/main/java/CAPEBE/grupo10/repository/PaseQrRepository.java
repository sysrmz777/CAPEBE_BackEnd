package CAPEBE.grupo10.repository;

import CAPEBE.grupo10.model.PaseQr;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaseQrRepository extends JpaRepository<PaseQr, Long> {

    Optional<PaseQr> findByCodigoQrIgnoreCase(String codigoQr);
}
