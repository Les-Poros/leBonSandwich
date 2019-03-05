package org.lpro.leBonSandwich.boundary;

import org.lpro.leBonSandwich.entity.Item;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ItemResource extends CrudRepository<Item, String> {
    List<Item> findAll(Pageable pegeable);
}