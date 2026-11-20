package com.capebe.vuelos.repository;

import com.capebe.vuelos.entities.Vuelo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VueloRepository extends JpaRepository<Vuelo, Long> {

    List<Vuelo> findByFecha(String fecha);

    Optional<Vuelo> findByCodigoVerificacionIgnoreCase(String codigoVerificacion);
}
