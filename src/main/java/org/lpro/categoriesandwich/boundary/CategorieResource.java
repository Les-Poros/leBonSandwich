package org.lpro.leBonSandwich.boundary;

import java.util.List;

import org.lpro.leBonSandwich.entity.Categorie;
import org.springframework.data.repository.CrudRepository;

public interface CategorieResource extends CrudRepository<Categorie, String> {

    List<Categorie> findAll();

}