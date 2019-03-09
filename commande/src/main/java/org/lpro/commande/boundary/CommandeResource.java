package org.lpro.commande.boundary;

import org.lpro.commande.entity.Commande;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.domain.*;
import java.util.*;

public interface CommandeResource extends CrudRepository<Commande, String> {
    Page<Commande> findAll(Pageable pegeable);
    List<Commande> findAll();
    Optional<Commande> findById(String id);
}