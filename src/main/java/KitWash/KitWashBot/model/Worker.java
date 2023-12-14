package KitWash.KitWashBot.model;

import java.util.ArrayList;

public class Worker {
    String name;
    String surname;
    Long telegramId;
    ArrayList<Service> completedServices;
    public Worker(String name, String surname, Long telegramId) {
        this.name = name;
        this.surname = surname;
        this.telegramId = telegramId;
        completedServices = new ArrayList<>();
    }
    //геттери класу Worker
    public Long getTelegramId() {
        return telegramId;
    }
    public String getName() {
        return name;
    }
    public String getSurname() {
        return surname;
    }
    public ArrayList<Service> getCompletedServices() {
        return completedServices;
    }

    //сеттери класу Worker
    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setTelegramId(Long telegramId) {
        this.telegramId = telegramId;
    }

    //додавання виконаної послуги до робітника
    public void addCompletedService(Service serviceToAdd) throws Exception{
        for(var i: completedServices){
            if(serviceToAdd.isInDateRange(i)){
                throw new Exception("Cannot add service");
            }
        }
        completedServices.add(serviceToAdd);
    }

    //видалення послуги з робітника
    public void removeService(Service service){
        completedServices.remove(service);
    }

    //перетворення інформації про робітника у String
    @Override
    public String toString() {
        return surname + ' ' + name  + " Телеграм ID: " + telegramId;
    }
    public String outString(){
        double salary = 0.0F;
        for(var i: completedServices){
            salary += (i.getPrice()*0.5)/i.workers.size();
        }
        return surname + ' ' + name + ". Зарплата: " + salary + '\n';
    }
}
