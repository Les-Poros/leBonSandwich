package org.lpro.leBonSandwich.boundary;

import org.lpro.leBonSandwich.entity.Commande;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CommandeResource extends CrudRepository<Commande, String> {
    List<Commande> findAll(Pageable pegeable);
}