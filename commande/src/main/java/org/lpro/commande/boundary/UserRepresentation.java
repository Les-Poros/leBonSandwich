package org.lpro.commande.boundary;

import java.util.*;

import org.lpro.commande.entity.Item;
import org.lpro.commande.entity.Commande;
import org.lpro.commande.exception.BadRequest;
import org.lpro.commande.exception.NotFound;
import org.lpro.commande.exception.MethodNotAllowed;
import org.springframework.http.HttpStatus;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import org.springframework.web.bind.annotation.*;

//Annotation pour controller rest
@RestController
@RequestMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
@ExposesResourceFor(Item.class)
public class UserRepresentation {

    private final UserResource ur;

    public UserRepresentation( UserResource ur) {
        this.ur = ur;
    }

    @GetMapping(value = "/{itemId}/auth", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getItemAvecId(@PathVariable("itemId") String id) {
        return new ResponseEntity<>(ir.findById(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getItem() {
        return new ResponseEntity<>(ir.findAll(), HttpStatus.OK);
    }
}