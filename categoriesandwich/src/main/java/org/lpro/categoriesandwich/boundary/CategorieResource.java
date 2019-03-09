package org.lpro.categoriesandwich.boundary;

import java.util.List;

import org.lpro.categoriesandwich.entity.Categorie;
import org.springframework.data.repository.CrudRepository;

public interface CategorieResource extends CrudRepository<Categorie, String> {

    List<Categorie> findAll();

}