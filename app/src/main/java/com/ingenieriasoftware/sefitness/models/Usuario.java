package com.ingenieriasoftware.sefitness.models;

import java.util.HashMap;
import java.util.Map;

public class Usuario {
    private String uid;
    private String name;
    private String role;
    private String genre;
    private String email;
    private String birthDate;
    private String rutinaAsignada;
    private String dietaAsignada;
    private String ailments;
    private String status;
    private boolean solicitudBaja;
    private String objetivos;

    public Usuario(String name, String role, String genre, String email, String birthDate, String ailments, String objetivos){
        this.name = name;
        this.role = role;
        this.genre = genre;
        this.email = email;
        this.birthDate = birthDate;
        this.rutinaAsignada = "";
        this.dietaAsignada = "";
        this.status = "Pendiente";
        this.ailments = ailments;
        this.objetivos = objetivos;
        this.solicitudBaja = false;
    }

    public String getObjetivos() {
        return objetivos;
    }

    public void setObjetivos(String objetivos) {
        this.objetivos = objetivos;
    }

    public String getAilments() {
        return ailments;
    }

    public void setAilments(String ailments) {
        this.ailments = ailments;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean getSolicitudBaja() {
        return solicitudBaja;
    }

    public void setSolicitudBaja(boolean solicitudBaja) {
        this.solicitudBaja = solicitudBaja;
    }

    public String isRutinaAsignada() {
        return rutinaAsignada;
    }

    public void setRutinaAsignada(String rutinaAsignada) {
        this.rutinaAsignada = rutinaAsignada;
    }

    public String isDietaAsignada() {
        return dietaAsignada;
    }

    public void setDietaAsignada(String dietaAsignada) {
        this.dietaAsignada = dietaAsignada;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public Map<String, Object> getFirebaseObject(){
        Map<String, Object> firebaseObject = new HashMap<>();
        firebaseObject.put("name", this.name);
        firebaseObject.put("genre", this.genre);
        firebaseObject.put("role", this.role);
        firebaseObject.put("email", this.email);
        firebaseObject.put("uid", this.uid);
        firebaseObject.put("birthDate", this.birthDate);
        firebaseObject.put("rutinaAsignada", this.rutinaAsignada);
        firebaseObject.put("dietaAsignada", this.dietaAsignada);
        firebaseObject.put("ailments", this.ailments);
        firebaseObject.put("objetivos", this.objetivos);
        firebaseObject.put("status", this.status);
        firebaseObject.put("solicitudBaja", this.solicitudBaja);
        return firebaseObject;
    }

}
