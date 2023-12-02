package KitWash.KitWashBot.model;

import java.time.LocalDateTime;
import java.util.*;

public class Service {
    LocalDateTime startDate;
    LocalDateTime endDate;
    final HashSet<Worker> workers = new HashSet<>(); // працівники, що працюють над виконанням послуги
    private ServiceType serviceType;

    //конструктор класу Service
    public ServiceType getServiceType() {
        return serviceType;
    }

    //геттери класу Service
    public LocalDateTime getEndDate() {
        return endDate;
    }
    public LocalDateTime getStartDate() {
        return startDate;
    }
    public int getPrice(){
        return serviceType.price;
    }

    //сеттери класу Service
    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }
    public void setStartDate(LocalDateTime start) {
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
}
