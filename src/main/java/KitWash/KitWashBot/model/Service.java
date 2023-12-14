package KitWash.KitWashBot.model;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Service {
    LocalTime startDate;
    LocalTime endDate;
    final HashSet<Worker> workers = new HashSet<>(); // працівники, що працюють над виконанням послуги
    private ServiceType serviceType;

    //конструктор класу Service
    public ServiceType getServiceType() {
        return serviceType;
    }

    //геттери класу Service
    public LocalTime getEndDate() {
        return endDate;
    }
    public LocalTime getStartDate() {
        return startDate;
    }

    public HashSet<Worker> getWorkers() {
        return workers;
    }

    public int getPrice(){
        return serviceType.price;
    }

    //сеттери класу Service
    public void setEndDate(LocalTime endDate) {
        this.endDate = endDate;
    }
    public void setStartDate(LocalTime start) {
        this.startDate = start;
    }
    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }

    //функції видалення/додавання працівника до послуги
    public void addWorker(Worker toAdd){
        workers.add(toAdd);
    }
    public void deleteWorker(Worker toDelete){
        workers.remove(toDelete);
    }

    //???
    public boolean isInDateRange(Service toComp){
        return endDate.isAfter(toComp.startDate) && endDate.isBefore(toComp.endDate) || startDate.isAfter(toComp.startDate) && startDate.isBefore(toComp.endDate);
    }

    //перетворення інформації про послугу у String
    @Override
    public String toString() {
        return "Service{" +
                "startDate=" + startDate +
                ", endDate=" + endDate +
                ", workers=" + workers +
                ", serviceType=" + serviceType +
                '}';
    }
    public String outString(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        String sEndTime = endDate.format(formatter);
        String sStartTime = startDate.format(formatter);

        String performers = "Виконавці:"+ '\n';

        for (Worker worker : workers) {
            performers = performers.concat( "\t- " +worker.surname +' '+ worker.name  + '\n');

        }

        String serviceType="";
        switch (this.getServiceType()) {
            case BODYWASH -> serviceType = "Мийка кузова";
            case INTERIORBODYWASH -> serviceType = "Мийка кузова і салону";
            case DRYCLEANING -> serviceType = "Хімчистка";
        }



        return "Послуга: " + serviceType + '\n' +
                "Початок послуги: " + sStartTime +" Кінець послуги: " + sEndTime + '\n'+
                performers;
    }
}
