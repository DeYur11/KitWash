package KitWash.KitWashBot.domain;

import KitWash.KitWashBot.model.Worker;

public class BotUser {
    GeneralStatus generalStatus;
    InputStatus inputStatus;
    WorkStatus workStatus;
    Worker worker;
    private Long id;

    //конструктори класу BotUser
    public BotUser(Long id){
        this.id = id;
    }
    public BotUser(Long id, GeneralStatus generalStatus){
        this.id = id;
        this.generalStatus = generalStatus;
    }
    public BotUser(){}

    //геттери класу BotUser
    public InputStatus getInputStatus() {
        return inputStatus;
    }
    public GeneralStatus getGeneralStatus() {
        return generalStatus;
    }
    public WorkStatus getWorkStatus() {return workStatus;}
    public Worker getWorker() {
        return worker;
    }
    public Long getId() {
        return id;
    }

    //сеттери класу BotUser
    public void setInputStatus(InputStatus inputStatus) {
        this.inputStatus = inputStatus;
    }
    public void setGeneralStatus(GeneralStatus generalStatus){
        this.generalStatus = generalStatus;
    }
    public void setWorkStatus(WorkStatus workStatus) {
        this.workStatus = workStatus;
    }
    public void setWorker(Worker worker) {
        this.worker = worker;
    }
}
