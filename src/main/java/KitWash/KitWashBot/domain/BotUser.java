package KitWash.KitWashBot.domain;

import KitWash.KitWashBot.model.Service;
import KitWash.KitWashBot.model.Worker;
import org.springframework.stereotype.Component;

@Component
public class BotUser {
    GeneralStatus generalStatus;
    InputStatus inputStatus;
    DeleteServiceStatus deleteServiceStatus;
    DeleteWorkerStatus deleteWorkerStatus;
    WorkStatus workStatus;
    WorkerEditStatus workerEditStatus;
    ServiceEditStatus serviceEditStatus;
    ManageStatus manageStatus;
    Worker worker;
    Service service;
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


    public DeleteServiceStatus getDeleteServiceStatus() {
        return deleteServiceStatus;
    }

    public void setDeleteServiceStatus(DeleteServiceStatus deleteServiceStatus) {
        this.deleteServiceStatus = deleteServiceStatus;
    }

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

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public ServiceEditStatus getServiceEditStatus() {
        return serviceEditStatus;
    }

    public void setServiceEditStatus(ServiceEditStatus serviceEditStatus) {
        this.serviceEditStatus = serviceEditStatus;
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
