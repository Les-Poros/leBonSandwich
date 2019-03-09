package org.lpro.commande.boundary;

import io.jsonwebtoken.*;
import org.lpro.commande.entity.*;
import org.lpro.commande.exception.BadRequest;
import org.lpro.commande.exception.NotFound;
import org.lpro.commande.exception.MethodNotAllowed;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.Link;
import org.lpro.commande.entity.Item;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

//Annotation pour controller rest
@RestController
@RequestMapping(value = "/commandes", produces = MediaType.APPLICATION_JSON_VALUE)
@ExposesResourceFor(Commande.class)

public class CommandeRepresentation {
    
    private final ItemResource ir;
    private final CommandeResource cr;

    public CommandeRepresentation(ItemResource ir,CommandeResource cr) {
        this.cr = cr;
        this.ir = ir;
    }

    @GetMapping
    public ResponseEntity<?> getAllCommandes(
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {
        return new ResponseEntity<>(commande2Ressource(cr.findAll(PageRequest.of(page, size))), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> postCommande(@RequestBody Commande commande) {
        String commandId=UUID.randomUUID().toString();
        commande.setId(commandId);
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        commande.setCreated_at(dateFormat.format(date));
        commande.setUpdated_at(dateFormat.format(date));
        String token =Jwts.builder().setSubject(commande.getId()).signWith(SignatureAlgorithm.HS256,"secret").compact();
        commande.setToken(token);
        commande.getItems().forEach(item -> {
            RestTemplate template = new RestTemplate();
            Sandwich sandwich = template.getForObject("http://192.168.99.100:8083"+item.getUri(), Sandwich.class);
            item.setTarif(sandwich.getPrix());
            item.setLibelle(sandwich.getNom());
            item.setId(UUID.randomUUID().toString());
            ir.save(item);
        });
        cr.save(commande);
        commande.getItems().forEach(item -> {
            item.setCommande(commande);
            ir.save(item);
        });
        Commande saved = cr.save(commande);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setLocation(linkTo(CommandeRepresentation.class).slash(saved.getId()).toUri());
        return new ResponseEntity<>(saved, responseHeaders, HttpStatus.CREATED);
    }

    @GetMapping(value = "/{commandeId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getCommandeAvecId(@PathVariable("commandeId") String id,
    @RequestParam(value = "token", required = false, defaultValue = "") String token,
    @RequestHeader(value="x-lbs-token"  ,required = false, defaultValue = "") String tokenHeader) {
        Optional<Commande> commande=cr.findById(id);
            if(commande.get().getToken().equals(token) || commande.get().getToken().equals(tokenHeader))
             return new ResponseEntity<>(commande2Ressource(commande), HttpStatus.OK);
            else return new ResponseEntity<>("{\"erreur\":\"Token invalide\"}", HttpStatus.FORBIDDEN);
    }

    @PutMapping(value = "/{commandeId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> putCommande(@PathVariable("commandeId") String id, @RequestBody Commande commandeUpdated)
            throws NotFound {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return cr.findById(id).map(commande -> {
            commande.setNom(commandeUpdated.getNom());
            commande.setLivraison(commandeUpdated.getLivraison());
            commande.setMail(commandeUpdated.getMail());
            commande.setStatus(commandeUpdated.getStatus());
            commande.setUpdated_at(dateFormat.format(date));
            cr.save(commande);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }).orElseThrow(() -> new NotFound("Commande inexistante !"));
    }

    @DeleteMapping(value = "/{commandeId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteCommande(@PathVariable("commandeId") String id) throws NotFound {
        return cr.findById(id).map(commande -> {
            cr.delete(commande);
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


    @GetMapping(value = "/{commandeId}/items")
    public ResponseEntity<?> getAllitemsByCommandeId(@PathVariable("commandeId") String id) throws NotFound {
        if (!cr.existsById(id)) {
            throw new NotFound("Commande inexistante !");
        }
        return new ResponseEntity<>(ir.findByCommandeId(id), HttpStatus.OK);
    }

    @PostMapping(value = "/{commandeId}/items")
    public ResponseEntity<?> postitem(@PathVariable("commandeId") String id, @RequestBody Item item)
            throws NotFound {
        return cr.findById(id).map(commande -> {
            item.setId(UUID.randomUUID().toString());
            item.setCommande(commande);
            Item saved=ir.save(item);
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.setLocation(linkTo(ItemRepresentation.class).slash(saved.getId()).toUri());
            return new ResponseEntity<>(saved, responseHeaders, HttpStatus.CREATED);
        }).orElseThrow(() -> new NotFound("Commande inexistante !"));
    }

    @PutMapping(value = "/{commandeId}/items/{itemId}")
    public ResponseEntity<?> putitem(@PathVariable("commandeId") String idCommande,
            @PathVariable("itemId") String idItem, @RequestBody Item itemUpdated) throws NotFound {
        if (!cr.existsById(idCommande)) {
            throw new NotFound("Commande inexistante !");
        }
        return ir.findById(idItem).map(item -> {
            item.setQuantite(itemUpdated.getQuantite());
            item.setUri(itemUpdated.getUri());
            item.setLibelle(itemUpdated.getLibelle());
            item.setTarif(itemUpdated.getTarif());
            ir.save(item);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }).orElseThrow(() -> new NotFound("item inexistant !"));
    }

    @DeleteMapping(value = "/{commandeId}/items/{itemId}")
    public ResponseEntity<?> deleteitem(@PathVariable("commandeId") String idCommande,
            @PathVariable("itemId") String idItem, @RequestBody Item itemUpdated) throws NotFound {
        if (!cr.existsById(idCommande)) {
            throw new NotFound("Commande inexistante !");
        }
        return ir.findById(idItem).map(item -> {
            ir.delete(item);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }).orElseThrow(() -> new NotFound("item inexistant !"));
    }


}