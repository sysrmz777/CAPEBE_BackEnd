package CAPEBE.grupo10.repository;

import CAPEBE.grupo10.model.Aeronave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AeronaveRepository extends JpaRepository<Aeronave, Long> {

    Optional<Aeronave> findByMatriculaIgnoreCase(String matricula);
}
