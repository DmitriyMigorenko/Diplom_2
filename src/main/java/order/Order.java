package order;

import lombok.Data;

@Data
public class Order {
    private String[] ingredients;

    public Order(String[] ingredients) {
        this.ingredients = ingredients;
    }

    public Order() {
    }

}