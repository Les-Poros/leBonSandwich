package org.lpro.commande.boundary;

import java.util.*;

import org.lpro.commande.entity.*;
import org.lpro.commande.exception.NotFound;
import org.lpro.commande.exception.MethodNotAllowed;
import org.springframework.http.HttpStatus;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import org.springframework.web.bind.annotation.*;

//Annotation pour controller rest
@RestController
@RequestMapping(value = "/utilisateurs", produces = MediaType.APPLICATION_JSON_VALUE)
@ExposesResourceFor(Utilisateur.class)
public class UtilisateurRepresentation {

    private final UtilisateurResource ur;

    public UtilisateurRepresentation(UtilisateurResource ur) {
        this.ur = ur;
    }

    @GetMapping
    public ResponseEntity<?> getAllCommandes(
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {
        return new ResponseEntity<>(ur.findAll(PageRequest.of(page, size)), HttpStatus.OK);
    }

    @GetMapping(value = "/{utilisateurId}/auth", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUtilisateurAvecId(@PathVariable("utilisateurId") String id,
            @RequestHeader(value = "Authorization") String autho) {
        if (!ur.existsById(id)) {
            throw new NotFound("Utilisateur inexistant !");
        }
        return new ResponseEntity<>(ur.findById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> postUtilisateur(@RequestBody Utilisateur Utilisateur) {
        Utilisateur.setId(UUID.randomUUID().toString());
        Utilisateur saved = ur.save(Utilisateur);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setLocation(linkTo(UtilisateurRepresentation.class).slash(saved.getId()).slash("auth").toUri());
        return new ResponseEntity<>(saved, responseHeaders, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<?> putMethode405Uti() throws MethodNotAllowed {
        throw new MethodNotAllowed("La méthode HTTP PUT n'est pas prévue pour cette route !");
    }

    @DeleteMapping
    public ResponseEntity<?> deleteMethode405Uti() throws MethodNotAllowed {
        throw new MethodNotAllowed("La méthode HTTP DELETE n'est pas prévue pour cette route !");
    }

    @PostMapping(value = "/{utilisateurId}/auth", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> postMethode405UtiAuth() throws MethodNotAllowed {
        throw new MethodNotAllowed("La méthode HTTP POST n'est pas prévue pour cette route !");
    }

    @PutMapping(value = "/{utilisateurId}/auth", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> putMethode405UtiAuth() throws MethodNotAllowed {
        throw new MethodNotAllowed("La méthode HTTP PUT n'est pas prévue pour cette route !");
    }

    @DeleteMapping(value = "/{utilisateurId}/auth", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteMethode405UtiAuth() throws MethodNotAllowed {
        throw new MethodNotAllowed("La méthode HTTP DELETE n'est pas prévue pour cette route !");
    }

}