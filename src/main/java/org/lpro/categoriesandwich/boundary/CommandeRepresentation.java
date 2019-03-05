package org.lpro.leBonSandwich.boundary;

import org.lpro.leBonSandwich.entity.Commande;
import org.lpro.leBonSandwich.exception.BadRequest;
import org.lpro.leBonSandwich.exception.NotFound;
import org.lpro.leBonSandwich.exception.MethodNotAllowed;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.Link;
import org.lpro.leBonSandwich.entity.Item;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

//Annotation pour controller rest
@RestController
@RequestMapping(value = "/commandes", produces = MediaType.APPLICATION_JSON_VALUE)
@ExposesResourceFor(Commande.class)

public class CommandeRepresentation {

    private final CommandeResource cmdr;

    public CommandeRepresentation(CommandeResource cmdr) {
        this.cmdr = cmdr;
    }

    @GetMapping
    public ResponseEntity<?> getAllCommandes(
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit) {
        return new ResponseEntity<>(commande2Ressource(cmdr.findAll(PageRequest.of(page, limit))), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> postCommande(@RequestBody Commande commande) {
        commande.setId(UUID.randomUUID().toString());
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        commande.setCreated_at(dateFormat.format(date));
        commande.setUpdated_at(dateFormat.format(date));
        Commande saved = cmdr.save(commande);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setLocation(linkTo(CommandeRepresentation.class).slash(saved.getId()).toUri());
        return new ResponseEntity<>(saved, responseHeaders, HttpStatus.CREATED);
    }

    @GetMapping(value = "/{commandeId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getCommandeAvecId(@PathVariable("commandeId") String id) {
        return new ResponseEntity<>(commande2Ressource(cmdr.findById(id)), HttpStatus.OK);
    }

    @PutMapping(value = "/{commandeId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> putCommande(@PathVariable("commandeId") String id, @RequestBody Commande commandeUpdated)
            throws NotFound {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return cmdr.findById(id).map(commande -> {
            commande.setNom(commandeUpdated.getNom());
            commande.setLivraison(commandeUpdated.getLivraison());
            commande.setMail(commandeUpdated.getMail());
            commande.setStatus(commandeUpdated.getStatus());
            commande.setUpdated_at(dateFormat.format(date));
            cmdr.save(commande);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }).orElseThrow(() -> new NotFound("Commande inexistante !"));
    }

    @DeleteMapping(value = "/{commandeId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteCommande(@PathVariable("commandeId") String id) throws NotFound {
        return cmdr.findById(id).map(commande -> {
            cmdr.delete(commande);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }).orElseThrow(() -> new NotFound("Commande inexistante !"));
    }

    @PostMapping(value = "/{commandeId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> postMethode405Cmd(@PathVariable("commandeId") int id) throws MethodNotAllowed {
        throw new MethodNotAllowed("La méthode HTTP POST n'est pas prévue pour cette route !");
    }

    @PutMapping
    public ResponseEntity<?> putMethode405Cmd() throws MethodNotAllowed {
        throw new MethodNotAllowed("La méthode HTTP PUT n'est pas prévue pour cette route !");
    }

    @DeleteMapping
    public ResponseEntity<?> deleteMethode405Cmd() throws MethodNotAllowed {
        throw new MethodNotAllowed("La méthode HTTP DELETE n'est pas prévue pour cette route !");
    }

    private Resources<Resource<Commande>> commande2Ressource(Iterable<Commande> commandes) {
        Link selfLink = linkTo(CommandeRepresentation.class).withSelfRel();
        List<Resource<Commande>> commandeResources = new ArrayList();
        commandes.forEach(commande -> commandeResources.add(commandeToResource(commande, false)));
        return new Resources<>(commandeResources, selfLink);
    }

    private Resource<Commande> commande2Ressource(Optional<Commande> commandes) {
        Link selfLink = linkTo(CommandeRepresentation.class).slash(commandes.get().getId()).withSelfRel();
        Link sandwichLink = linkTo(CommandeRepresentation.class).slash(commandes.get().getId()).slash("items")
                .withRel("items");
        return new Resource<>(commandes.get(), selfLink, sandwichLink);
    }

    private Resource<Commande> commandeToResource(Commande commande, Boolean collection) {
        Link selfLink = linkTo(CommandeRepresentation.class).slash(commande.getId()).withSelfRel();
        Link sandwichLink = linkTo(CommandeRepresentation.class).slash(commande.getId()).slash("items")
                .withRel("items");
        if (collection) {
            Link collectionLink = linkTo(CommandeRepresentation.class).withRel("collection");
            return new Resource<>(commande, selfLink, sandwichLink, collectionLink);
        } else {
            return new Resource<>(commande, selfLink);
        }
    }

}