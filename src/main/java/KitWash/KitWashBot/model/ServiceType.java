package KitWash.KitWashBot.model;
import java.util.HashMap;

//клас-перелічення типів послуг
public enum ServiceType {
    BODYWASH(250),
    INTERIORBODYWASH(350),
    DRYCLEANING(1800);
    public final int price;
    ServiceType(int price) {
        this.price = price;
    }
}
