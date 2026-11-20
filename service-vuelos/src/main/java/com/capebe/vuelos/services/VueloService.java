package com.capebe.vuelos.services;

import com.capebe.vuelos.entities.Vuelo;
import java.util.List;
import java.util.Optional;

public interface VueloService {
    List<Vuelo> obtenerTodos();
    Optional<Vuelo> obtenerPorId(Long id);
    List<Vuelo> obtenerPorFecha(String fecha);
    Optional<Vuelo> obtenerPorCodigo(String codigoVerificacion);
    Vuelo crear(Vuelo vuelo);
}
