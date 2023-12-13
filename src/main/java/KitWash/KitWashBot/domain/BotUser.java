package KitWash.KitWashBot.domain;

import KitWash.KitWashBot.model.Worker;

public class BotUser {
    GeneralStatus generalStatus;
    InputStatus inputStatus;
    WorkStatus workStatus;
    WorkerEditStatus workerEditStatus;
    Worker worker;
    private Long telegramID;

    //конструктори класу BotUser
    public BotUser(Long telegramID){
        this.telegramID = telegramID;
    }
    public BotUser(Long telegramID, GeneralStatus generalStatus){
        this.telegramID = telegramID;
        this.generalStatus = generalStatus;
    }
    public BotUser(){}


    public WorkerEditStatus getWorkerEditStatus() {
        return workerEditStatus;
    }

    public void setWorkerEditStatus(WorkerEditStatus workerEditStatus) {
        this.workerEditStatus = workerEditStatus;
    }

    public void setTelegramID(Long telegramID) {
        this.telegramID = telegramID;
    }

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
    public Long getTelegramID() {
        return telegramID;
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
