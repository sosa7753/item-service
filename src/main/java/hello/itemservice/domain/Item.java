package hello.itemservice.domain;

import lombok.Data;

@Data
public class Item {

  private Long id;
  private String itemName;
  private Integer price; // 없을 때 가정.
  private int quantity; // 수량

  public Item() {

  }

  public Item(String itemName, Integer price, int quantity) {
    this.itemName = itemName;
    this.price = price;
    this.quantity = quantity;
  }
}
