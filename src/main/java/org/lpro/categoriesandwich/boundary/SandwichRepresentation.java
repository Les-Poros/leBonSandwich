package org.lpro.leBonSandwich.boundary;

import java.util.*;

import org.lpro.leBonSandwich.entity.Sandwich;
import org.lpro.leBonSandwich.entity.Categorie;
import org.lpro.leBonSandwich.exception.BadRequest;
import org.lpro.leBonSandwich.exception.NotFound;
import org.lpro.leBonSandwich.exception.MethodNotAllowed;
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
@RequestMapping(value = "/sandwichs", produces = MediaType.APPLICATION_JSON_VALUE)
@ExposesResourceFor(Categorie.class)
public class SandwichRepresentation {

    private final SandwichResource sr;
    private final CategorieResource cr;

    // Injection de dépendances
    public SandwichRepresentation(SandwichResource sr, CategorieResource cr) {
        this.sr = sr;
        this.cr = cr;
    }

    @GetMapping
    public ResponseEntity<?> getAllSandwichs(
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit,
            @RequestParam(value = "pain", required = false) String pain,
            @RequestParam(value = "prix", required = false) Integer prix) {
        if (prix != null && pain != null) {
            return new ResponseEntity<>(sandwich2Resource(sr.findByPrixAndPain(prix, pain)), HttpStatus.OK);
        } else if (prix != null) {
            return new ResponseEntity<>(sandwich2Resource(sr.findByPrix(prix)), HttpStatus.OK);
        } else if (pain != null) {
            return new ResponseEntity<>(sandwich2Resource(sr.findByPain(pain)), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(sandwich2Resource(sr.findAll(PageRequest.of(page, limit))), HttpStatus.OK);
        }
    }

    @GetMapping(value = "/categories/{categorieId}")
    public ResponseEntity<?> getAllSandwichsByCategorieId(@PathVariable("categorieId") String id) throws NotFound {
        if (!cr.existsById(id)) {
            throw new NotFound("Catégorie inexistante !");
        }
      
        return new ResponseEntity<>(sandwich2Resource(sr.findByCategorieId(id)), HttpStatus.OK);
    }

    @GetMapping(value = "/{sandwichId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getCategorieAvecId(@PathVariable("sandwichId") String id) throws NotFound {
        return Optional.ofNullable(sr.findById(id)).filter(Optional::isPresent)
                .map(sandwich -> new ResponseEntity<>(sandwichToResource(sandwich.get(),false), HttpStatus.OK))
                .orElseThrow(() -> new NotFound("Catégorie inexistante !"));
    }

    @PostMapping(value = "/categories/{categorieId}")
    public ResponseEntity<?> postSandwich(@PathVariable("categorieId") String id, @RequestBody Sandwich sandwich)
            throws NotFound {
        return cr.findById(id).map(categorie -> {
            sandwich.setId(UUID.randomUUID().toString());
            sandwich.setCategorie(categorie);
            sr.save(sandwich);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }).orElseThrow(() -> new NotFound("Catégorie inexistante !"));
    }

    @PutMapping(value = "/{sandwichId}")
    public ResponseEntity<?> putSandwich(@PathVariable("categorieId") String idCategorie,
            @PathVariable("sandwichId") String idSandwich, @RequestBody Sandwich sandwichUpdated) throws NotFound {
        if (!cr.existsById(idCategorie)) {
            throw new NotFound("Catégorie inexistante !");
        }
        return sr.findById(idSandwich).map(sandwich -> {
            sandwich.setNom(sandwichUpdated.getNom());
            sandwich.setDescription(sandwichUpdated.getDescription());
            sandwich.setPain(sandwichUpdated.getPain());
            sandwich.setPrix(sandwichUpdated.getPrix());
            sr.save(sandwich);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }).orElseThrow(() -> new NotFound("Sandwich inexistant !"));
    }

    @DeleteMapping(value = "/{sandwichId}")
    public ResponseEntity<?> deleteSandwich(@PathVariable("categorieId") String idCategorie,
            @PathVariable("sandwichId") String idSandwich) throws NotFound {
        if (!cr.existsById(idCategorie)) {
            throw new NotFound("Catégorie inexistante !");
        }
        return sr.findById(idSandwich).map(sandwich -> {
            sr.delete(sandwich);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }).orElseThrow(() -> new NotFound("Sandwich inexistant !"));
    }

    @PostMapping
    public ResponseEntity<?> postMethode405Sand() throws MethodNotAllowed {
        throw new MethodNotAllowed("La méthode HTTP PUT n'est pas prévue pour cette route !");
    }

    @PutMapping
    public ResponseEntity<?> putMethode405Sand() throws MethodNotAllowed {
        throw new MethodNotAllowed("La méthode HTTP PUT n'est pas prévue pour cette route !");
    }

    @DeleteMapping
    public ResponseEntity<?> deleteMethode405Sand() throws MethodNotAllowed {
        throw new MethodNotAllowed("La méthode HTTP DELETE n'est pas prévue pour cette route !");
    }

    @PutMapping(value = "/{categorieId}/sandwichs", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> putMethode405WithCategSand() throws MethodNotAllowed {
        throw new MethodNotAllowed("La méthode HTTP PUT n'est pas prévue pour cette route !");
    }

    @DeleteMapping(value = "/{categorieId}/sandwichs", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteMethode405WithCategSand() throws MethodNotAllowed {
        throw new MethodNotAllowed("La méthode HTTP DELETE n'est pas prévue pour cette route !");
    }

    @GetMapping(value = "/{categorieId}/sandwichs/{sandwichId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getMethode405WithCategAndSand() throws MethodNotAllowed {
        throw new MethodNotAllowed("La méthode HTTP PUT n'est pas prévue pour cette route !");
    }

    @PostMapping(value = "/{categorieId}/sandwichs/{sandwichId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> postMethode405WithCategAndSand() throws MethodNotAllowed {
        throw new MethodNotAllowed("La méthode HTTP DELETE n'est pas prévue pour cette route !");
    }

    private Resources<Resource<Sandwich>> sandwich2Resource(Iterable<Sandwich> sandwiches) {
        Link selfLink = linkTo(SandwichRepresentation.class).withSelfRel();
        List<Resource<Sandwich>> sandwichResources = new ArrayList();
        sandwiches.forEach(sandwich -> sandwichResources.add(sandwichToResource(sandwich, false)));
        return new Resources<>(sandwichResources, selfLink);
    }

    private Resource<Sandwich> sandwichToResource(Sandwich sandwich, Boolean collection) {
        Link selfLink = linkTo(SandwichRepresentation.class).slash(sandwich.getId()).withSelfRel();
        Link CategLink = linkTo(CategorieRepresentation.class).slash(sandwich.getCategorie().getId()).withRel("categorie");
        if (collection) {
            Link collectionLink = linkTo(SandwichRepresentation.class).withRel("collection");
            return new Resource<>(sandwich, selfLink, CategLink,collectionLink);
        } else {
            return new Resource<>(sandwich, CategLink,selfLink);
        }
    }

}