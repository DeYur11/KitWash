package KitWash.KitWashBot.handlers;

public class AddForm {
    private String name;
    private String surname;
    private Long id;

    //геттери класу AddForm
    public String getName() {
        return name;
    }
    public String getSurname() {
        return surname;
    }
    public Long getId() {
        return id;
    }

    //сеттери класу AddForm
    public void setName(String name) {
        this.name = name;
    }
    public void setSurname(String surname) {
        this.surname = surname;
    }
    public void setId(Long id) {
        this.id = id;
    }
}
