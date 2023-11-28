package KitWash.KitWashBot.model;

import java.util.ArrayList;
import java.util.HashMap;

public class Worker {
    String name;
    String surname;
    int telegramId;
    //ArrayList<Service> completedServices;
    ArrayList<Service> completedServices;

    public Worker(String name, String surname, int telegramId) {
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
