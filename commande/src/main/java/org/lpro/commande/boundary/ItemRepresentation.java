package org.lpro.commande.boundary;

import org.lpro.commande.entity.Item;
import org.lpro.commande.exception.MethodNotAllowed;
import org.lpro.commande.exception.NotFound;
import org.springframework.http.HttpStatus;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//Annotation pour controller rest
@RestController
@RequestMapping(value = "/items", produces = MediaType.APPLICATION_JSON_VALUE)
@ExposesResourceFor(Item.class)
public class ItemRepresentation {

    private final ItemResource ir;

    public ItemRepresentation(ItemResource ir) {
        this.ir = ir;
    }

    @GetMapping(value = "/{itemId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getItemAvecId(@PathVariable("itemId") String id) {
        if (!ir.existsById(id)) {
            throw new NotFound("Item inexistant !");
        }
        return new ResponseEntity<>(ir.findById(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getItem() {
        return new ResponseEntity<>(ir.findAll(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> postMethode405Item() throws MethodNotAllowed {
        throw new MethodNotAllowed("La méthode HTTP POST n'est pas prévue pour cette route !");
    }

    @PutMapping
    public ResponseEntity<?> putMethode405Item() throws MethodNotAllowed {
        throw new MethodNotAllowed("La méthode HTTP PUT n'est pas prévue pour cette route !");
    }

    @DeleteMapping
    public ResponseEntity<?> deleteMethode405Item() throws MethodNotAllowed {
        throw new MethodNotAllowed("La méthode HTTP DELETE n'est pas prévue pour cette route !");
    }

    @PostMapping(value = "/{itemId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> postMethode405ItemWithId() throws MethodNotAllowed {
        throw new MethodNotAllowed("La méthode HTTP POST n'est pas prévue pour cette route !");
    }

    @PutMapping(value = "/{itemId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> putMethode405ItemWithId() throws MethodNotAllowed {
        throw new MethodNotAllowed("La méthode HTTP PUT n'est pas prévue pour cette route !");
    }

    @DeleteMapping(value = "/{itemId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteMethode405ItemWithId() throws MethodNotAllowed {
        throw new MethodNotAllowed("La méthode HTTP DELETE n'est pas prévue pour cette route !");
    }

}