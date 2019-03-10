package org.lpro.commande.entity;

import javax.persistence.*;

@Entity
public class Utilisateur {

    @Id
    private String id;
    private String nom;
    private String mdp;

    Utilisateur() {
        // necessaire pour JPA !
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

    public String getMdp() {
        return this.mdp;
    }

    public void setMdp(String mdp) {
        this.mdp = mdp;
    }

}