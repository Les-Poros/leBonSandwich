package org.lpro.leBonSandwich.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Categorie {

    @Id
    private String id;
    private String nom;
    private String description;

    @OneToMany(mappedBy = "categorie", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Sandwich> sandwichs;

    Categorie() {
        // necessaire pour JPA !
    }

    public Categorie(String nom) {
        this.nom = nom;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNom() {
        return this.nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Sandwich> getSandwichs() {
        return this.sandwichs;
    }

    public void setSandwichs(Set<Sandwich> sandwichs) {
        this.sandwichs = sandwichs;
    }
}