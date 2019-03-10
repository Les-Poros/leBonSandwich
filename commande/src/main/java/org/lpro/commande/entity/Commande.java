package org.lpro.commande.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Commande {

    @Id
    private String id;
    private String nom;
    private String mail;
    private String created_at;
    private String livraison;
    private String updated_at;
    private Integer montant;
    private String status;
    private String remise;
    private String token;
    private String client_id;
    private String ref_paiement;
    private String date_paiement;
    private String mode_paiement;

    @OneToMany(mappedBy = "commande", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Item> items;

    Commande() {
        // necessaire pour JPA !
    }

    public Set<Item> getItems() {
        return this.items;
    }

    public void setItems(Set<Item> items) {
        this.items = items;
    }

    public String getRemise() {
        return this.remise;
    }

    public void setRemise(String remise) {
        this.remise = remise;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getClient_id() {
        return this.client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getRef_paiement() {
        return this.ref_paiement;
    }

    public void setRef_paiement(String ref_paiement) {
        this.ref_paiement = ref_paiement;
    }

    public String getDate_paiement() {
        return this.date_paiement;
    }

    public void setDate_paiement(String date_paiement) {
        this.date_paiement = date_paiement;
    }

    public String getMode_paiement() {
        return this.mode_paiement;
    }

    public void setMode_paiement(String mode_paiement) {
        this.mode_paiement = mode_paiement;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getMail() {
        return this.mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getCreated_at() {
        return this.created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getLivraison() {
        return this.livraison;
    }

    public void setLivraison(String livraison) {
        this.livraison = livraison;
    }

    public String getUpdated_at() {
        return this.updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public Integer getMontant() {
        return this.montant;
    }

    public void setMontant(Integer montant) {
        this.montant = montant;
    }

}