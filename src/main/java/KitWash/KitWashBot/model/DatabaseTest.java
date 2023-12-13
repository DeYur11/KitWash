package KitWash.KitWashBot.model;

import javax.xml.crypto.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class DatabaseTest {
    public static void main(String[] args){
        Database db = new Database();
        Worker worker = new Worker("Yura", "Debelyak", 9999L);

        Service service = new Service();
        Service service2 = new Service();
        Service service3 = new Service();

        /*service.startDate = LocalDateTime.of(2023, 11, 28, 7, 12);
        service.endDate = LocalDateTime.of(2023, 11, 28, 8, 12);

        service2.startDate = LocalDateTime.of(2023, 11, 28, 7, 13);
        service2.endDate = LocalDateTime.of(2023, 11, 28, 8, 13);

        service3.startDate = LocalDateTime.of(2023, 11, 28, 9, 13);
        service3.endDate = LocalDateTime.of(2023, 11, 28, 10, 13);*/

        service.addWorker(worker);
        service2.addWorker(worker);
        service3.addWorker(worker);


        try {
            db.addWorker(worker);
            db.addService(service);
        }catch (Exception e){
            System.out.println("Error");
        }

        try {
            db.addService(service2);
        }catch (Exception e){
            e.printStackTrace();
        }

        try {
            db.addService(service3);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
