package org.lpro.categoriesandwich.boundary;

import java.util.*;
import org.lpro.categoriesandwich.entity.Categorie;
import org.lpro.categoriesandwich.exception.NotFound;
import org.lpro.categoriesandwich.exception.MethodNotAllowed;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

//Annotation pour controller rest
@RestController
@RequestMapping(value = "/categories", produces = MediaType.APPLICATION_JSON_VALUE)
@ExposesResourceFor(Categorie.class)
public class CategorieRepresentation {

    private final CategorieResource cr;

    public CategorieRepresentation(CategorieResource cr) {
        this.cr = cr;
    }

    @GetMapping
    public ResponseEntity<?> getAllCategories() {
        return new ResponseEntity<>(categorie2Resource(cr.findAll()), HttpStatus.OK);
    }

    @GetMapping(value = "/{categorieId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getCategorieAvecId(@PathVariable("categorieId") String id) throws NotFound {
        return Optional.ofNullable(cr.findById(id)).filter(Optional::isPresent)
                .map(categorie -> new ResponseEntity<>(categorieToResource(categorie.get(), false), HttpStatus.OK))
                .orElseThrow(() -> new NotFound("Catégorie inexistante !"));
    }

    @PostMapping
    public ResponseEntity<?> postCategorie(@RequestBody Categorie categorie) {
        categorie.setId(UUID.randomUUID().toString());
        Categorie saved = cr.save(categorie);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders
                .setLocation(linkTo(CategorieRepresentation.class).slash("categories").slash(saved.getId()).toUri());
        return new ResponseEntity<>(saved, responseHeaders, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{categorieId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> putCategorie(@PathVariable("categorieId") String id,
            @RequestBody Categorie categorieUpdated) throws NotFound {
        return cr.findById(id).map(categorie -> {
            categorie.setDescription(categorieUpdated.getDescription());
            categorie.setNom(categorieUpdated.getNom());
            cr.save(categorie);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }).orElseThrow(() -> new NotFound("Catégorie inexistante !"));
    }

    @DeleteMapping(value = "/{categorieId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteCategorie(@PathVariable("categorieId") String id) throws NotFound {
        return cr.findById(id).map(categorie -> {
            cr.delete(categorie);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }).orElseThrow(() -> new NotFound("Catégorie inexistante !"));
    }

    @PostMapping(value = "/{categorieId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> postMethode405Cat() throws MethodNotAllowed {
        throw new MethodNotAllowed("La méthode HTTP POST n'est pas prévue pour cette route !");
    }

    @PutMapping
    public ResponseEntity<?> putMethode405Cat() throws MethodNotAllowed {
        throw new MethodNotAllowed("La méthode HTTP PUT n'est pas prévue pour cette route !");
    }

    @DeleteMapping
    public ResponseEntity<?> deleteMethode405Cat() throws MethodNotAllowed {
        throw new MethodNotAllowed("La méthode HTTP DELETE n'est pas prévue pour cette route !");
    }

    private Resources<Resource<Categorie>> categorie2Resource(Iterable<Categorie> categories) {
        Link selfLink = linkTo(CategorieRepresentation.class).withSelfRel();
        List<Resource<Categorie>> categorieResources = new ArrayList();
        categories.forEach(categorie -> categorieResources.add(categorieToResource(categorie, false)));
        return new Resources<>(categorieResources, selfLink);
    }

    private Resource<Categorie> categorieToResource(Categorie categorie, Boolean collection) {
        Link selfLink = linkTo(CategorieRepresentation.class).slash(categorie.getId()).withSelfRel();
        Link sandwichLink = linkTo(SandwichRepresentation.class).slash(linkTo(CategorieRepresentation.class))
                .slash(categorie.getId()).withRel("sandwichs");
        if (collection) {
            Link collectionLink = linkTo(CategorieRepresentation.class).withRel("collection");
            return new Resource<>(categorie, sandwichLink, selfLink, collectionLink);
        } else {
            return new Resource<>(categorie, sandwichLink, selfLink);
        }
    }

}