package com.universidad.seguridad.repository;

import com.universidad.seguridad.model.Tarea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TareaRepository extends JpaRepository<Tarea, Long> {

    List<Tarea> findByPropietarioId(Long usuarioId);

    // SEGURO: parámetro nombrado. JPA lo envía como PreparedStatement,
    // el valor nunca se interpreta como JPQL/SQL.
    @Query("SELECT t FROM Tarea t WHERE LOWER(t.titulo) LIKE LOWER(CONCAT('%', :texto, '%'))")
    List<Tarea> buscarPorTitulo(@Param("texto") String texto);

    // INSEGURO: NUNCA HACER ESTO
    // @Query(value = "SELECT * FROM tareas WHERE titulo LIKE '%" +
    //     texto + "%'", nativeQuery = true)
    // Si texto = "' OR '1'='1", la consulta devuelve TODAS las tareas
    // sin filtrar, ignorando por completo el criterio de búsqueda.
}