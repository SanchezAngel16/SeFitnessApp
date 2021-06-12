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

    public boolean isRutinaAsignada() {
        return rutinaAsignada;
    }

    public void setRutinaAsignada(boolean rutinaAsignada) {
        this.rutinaAsignada = rutinaAsignada;
    }

    public boolean isDietaAsignada() {
        return dietaAsignada;
    }

    public void setDietaAsignada(boolean dietaAsignada) {
        this.dietaAsignada = dietaAsignada;
    }

    private boolean rutinaAsignada;
    private boolean dietaAsignada;

    public Usuario(String name, String role, String genre, String email, String birthDate){
        this.name = name;
        this.role = role;
        this.genre = genre;
        this.email = email;
        this.birthDate = birthDate;
        this.rutinaAsignada = false;
        this.dietaAsignada = false;
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
        return firebaseObject;
    }

}
