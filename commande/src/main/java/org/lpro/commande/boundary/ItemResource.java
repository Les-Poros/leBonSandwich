package org.lpro.commande.boundary;

import org.lpro.commande.entity.Item;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface ItemResource extends CrudRepository<Item, String> {

    List<Item> findAll(Pageable pegeable);

    List<Item> findByCommandeId(String id);

}