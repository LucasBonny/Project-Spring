package br.com.gunthercloud.coursejava.entities;

import br.com.gunthercloud.coursejava.entities.pk.OrderItemPK;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "tb_order_item")
public class OrderItem implements Serializable {

    @EmbeddedId
    private final OrderItemPK id = new OrderItemPK();
    private Integer quantity;
    private Double price;

    public OrderItem () {

    }

    public OrderItem(Order order, Product product, Integer quantity, Double price) {
        id.setOrder(order);
        id.setProduct(product);
        this.quantity = quantity;
        this.price = price;
    }

    public void setOrder(Order order){
        id.setOrder(order);
    }

    @JsonIgnore
    public Order getOrder(){
        return id.getOrder();
    }
    public void setProduct(Product product){
        id.setProduct(product);
    }
    public Product getProduct(){
        return id.getProduct();
    }
    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItem orderItem = (OrderItem) o;
        return Objects.equals(id, orderItem.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
