package KitWash.KitWashBot.domain;

import KitWash.KitWashBot.handlers.ManageWorkerHandler;
import KitWash.KitWashBot.model.Worker;

public class BotUser {
    GeneralStatus generalStatus;
    InputStatus inputStatus;

    DeleteWorkerStatus deleteWorkerStatus;
    WorkStatus workStatus;
    WorkerEditStatus workerEditStatus;
    ManageStatus manageStatus;
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

    public DeleteWorkerStatus getDeleteWorkerStatus() {
        return deleteWorkerStatus;
    }

    public void setDeleteWorkerStatus(DeleteWorkerStatus deleteWorkerStatus) {
        this.deleteWorkerStatus = deleteWorkerStatus;
    }

    public ManageStatus getManageStatus() {
        return manageStatus;
    }

    public void setManageStatus(ManageStatus manageStatus) {
        this.manageStatus = manageStatus;
    }

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
