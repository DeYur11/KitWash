package KitWash.KitWashBot.domain;

import KitWash.KitWashBot.model.Worker;

public class User {
    Position position;
    AddPosition addPosition;
    Worker worker;
    private Long id;


    public User(Long id){
        this.id = id;
    }
    public User(Long id, Position position){
        this.id = id;
        this.position = position;
    }
    public User(){}
    public AddPosition getAddPosition() {
        return addPosition;
    }

    public void setAddPosition(AddPosition addPosition) {
        this.addPosition = addPosition;
    }

    public Position getPosition() {
        return position;
    }

    public Long getId() {
        return id;
    }
    public void setPosition(Position position){
        this.position = position;
    }

    public Worker getWorker() {
        return worker;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }
}
