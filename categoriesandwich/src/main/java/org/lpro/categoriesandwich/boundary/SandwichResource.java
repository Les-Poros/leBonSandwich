package org.lpro.categoriesandwich.boundary;

import java.util.List;

import org.lpro.categoriesandwich.entity.Sandwich;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.domain.*;

public interface SandwichResource extends CrudRepository<Sandwich, String> {

    List<Sandwich> findByCategorieId(String categorieId);

    List<Sandwich> findByPain(String pain);

    List<Sandwich> findByPrix(Integer prix);

    List<Sandwich> findByPrixAndPain(Integer prix, String pain);

    Page<Sandwich> findAll(Pageable pegeable);

}