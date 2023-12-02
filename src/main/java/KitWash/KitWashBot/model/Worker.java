package KitWash.KitWashBot.model;

import java.util.ArrayList;
import java.util.HashMap;

public class Worker {
    String name;
    String surname;
    Long telegramId;
    ArrayList<Service> completedServices;

    public Long getTelegramId() {
        return telegramId;
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

    public void setTelegramId(Long telegramId) {
        this.telegramId = telegramId;
    }

    public ArrayList<Service> getCompletedServices() {
        return completedServices;
    }

    public void setCompletedServices(ArrayList<Service> completedServices) {
        this.completedServices = completedServices;
    }

    public Worker(String name, String surname, Long telegramId) {
        this.name = name;
        this.surname = surname;
        this.telegramId = telegramId;
        completedServices = new ArrayList<>();
    }
    public void addService(Service serviceToAdd) throws Exception{
        for(var i: completedServices){
            if(serviceToAdd.isInDateRange(i)){
                throw new Exception("Cannot add service");
            }
        }
        completedServices.add(serviceToAdd);
    }
    public void removeService(Service service){
        completedServices.remove(service);
    }

    @Override
    public String toString() {
        return "Worker{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", telegramId=" + telegramId;
    }
}
