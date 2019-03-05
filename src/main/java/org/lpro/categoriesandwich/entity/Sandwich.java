package org.lpro.leBonSandwich.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
public class Sandwich {

    @Id
    private String id;
    private String nom;
    private String description;
    private String pain;
    private Integer prix;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "categorie_id", nullable = false)
    @JsonIgnore
    private Categorie categorie;

    Sandwich() {
        // necessaire pour JPA !
    }

    public String getPain() {
        return this.pain;
    }

    public void setPain(String pain) {
        this.pain = pain;
    }

    public Integer getPrix() {
        return this.prix;
    }

    public void setPrix(Integer prix) {
        this.prix = prix;
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

    public Categorie getCategorie() {
        return this.categorie;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}