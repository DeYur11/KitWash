package KitWash.KitWashBot.domain;

import KitWash.KitWashBot.model.Worker;

public class BotUser {
    Position position;
    AddPosition addPosition;
    WorkPosition workPosition;

    Worker worker;
    private Long id;


    public BotUser(Long id){
        this.id = id;
    }
    public BotUser(Long id, Position position){
        this.id = id;
        this.position = position;
    }
    public BotUser(){}
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

    public WorkPosition getWorkPosition() {
        return workPosition;
    }

    public void setWorkPosition(WorkPosition workPosition) {
        this.workPosition = workPosition;
    }
}
