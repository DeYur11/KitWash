package KitWash.KitWashBot.model;

import org.springframework.stereotype.Component;

import java.util.Vector;
@Component
public class Database {
    private final Vector<Service> totalServices = new Vector<>();
    private final Vector<Worker> workers = new Vector<>();

    //геттери класу Database
    public Vector<Service> getTotalServices() {
        return totalServices;
    }
    public Vector<Worker> getWorkers() {
        return workers;
    }


    //додавання робітника до бази даних
    public void addWorker(Worker worker) throws Exception{
        if(workers.contains(worker)){
            throw new Exception();
        }
        workers.add(worker);
    }

    //додавання послуги до бази даних
    public void addService(Service service){
        for(var i: workers){
            if(service.workers.contains(i)){
                try {
                    i.addCompletedService(service);
                }catch (Exception e){
                    //TODO
                    // Тут потрібно зробити якусь перевірку на видалення послуги з працівників, яким вже послуга додалася.
                    // Думаю через ArrayList зробити.
                    System.out.println("Error adding service: Start: " + service.startDate.getHour() + " " +
                            service.startDate.getMinute() + " End: " +
                            service.endDate.getHour() + " " + service.endDate.getMinute());

                    return;
                }

            }
        }
        totalServices.add(service);
    }
}
