package com.example.anto.chronosmart;

public class UserInformation  {

    private String name;
    private String surname;
    private String email;
    private String age;
    private String privilegi;

    public UserInformation(){

    }

    public UserInformation(String name,String surname, String email, String age, String privilegi) {
        this.name = name;
        this.surname= surname;
        this.email = email;
        this.age = age;
        this.privilegi= privilegi;

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getPrivilegi() {
        return privilegi;
    }

    public void setPrivilegi(String privilegi) {
        this.privilegi = privilegi;
    }



}
