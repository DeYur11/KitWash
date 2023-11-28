package KitWash.KitWashBot.model;



import java.time.LocalDateTime;
import java.util.*;

public class Service {
    LocalDateTime startDate;
    LocalDateTime endDate;
    final HashSet<Worker> workers = new HashSet<>(); // Storing workers that worked on this service

    private final static HashMap<ServiceType, Integer> priceList= new HashMap<ServiceType, Integer>();
    /*
    * Starting initialization of class.
    * Puts different prices to services type.
    * */
    static {

        priceList.put(ServiceType.BODYWASH, 250);
        priceList.put(ServiceType.INTERIORBODYWASH, 350);
        priceList.put(ServiceType.DRYCLEANING, 1800);
    }
    private ServiceType serviceType;
    public void addWorker(Worker toAdd){
        workers.add(toAdd);
    }
    public void deleteWorker(Worker toDelete){
        workers.remove(toDelete);
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime start) {
        this.startDate = start;
    }

    public int getPrice(){
        return Service.priceList.get(this.serviceType);
    }

    public boolean isInDateRange(Service toComp){
        return endDate.isAfter(toComp.startDate) && endDate.isBefore(toComp.endDate) || startDate.isAfter(toComp.startDate) && startDate.isBefore(toComp.endDate);
    }

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
