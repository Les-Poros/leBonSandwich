package org.lpro.leBonSandwich.boundary;

import org.lpro.leBonSandwich.entity.Commande;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.domain.PageImpl;
import java.util.*;

public interface CommandeResource extends CrudRepository<Commande, String> {
    PageImpl<Commande> findAll(Pageable pegeable);
    List<Commande> findAll();
}