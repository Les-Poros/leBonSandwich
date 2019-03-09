package org.lpro.commande.boundary;

import org.lpro.commande.entity.Utilisateur;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UtilisateurResource extends CrudRepository<Utilisateur, String> {
    List<Utilisateur> findAll(Pageable pegeable);
}