package org.lpro.commande.entity;

import javax.persistence.*;

@Entity
public class Sandwich {

    @Id
    private String id;
    private String nom;
    private String description;
    private String pain;
    private Integer prix;

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

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}