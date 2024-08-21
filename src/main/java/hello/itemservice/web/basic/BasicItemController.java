package hello.itemservice.web.basic;


import hello.itemservice.domain.Item;
import hello.itemservice.domain.ItemRepository;
import jakarta.annotation.PostConstruct;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor
public class BasicItemController {

  private final ItemRepository itemRepository;

  @GetMapping
  public String items(Model model) {
    List<Item> items = itemRepository.findAll();
    model.addAttribute("items", items);
    return "basic/items";
  }

  @GetMapping("/{itemId}")
  public String item(@PathVariable long itemId, Model model) {
    Item item = itemRepository.findById(itemId);
    model.addAttribute("item", item);
    return "basic/item";
  }

  @GetMapping("/add")
  public String addForm() {
    return "basic/addForm";
  }

  //  @PostMapping("/add")
  public String addItemV1(
      @RequestParam String itemName,
      @RequestParam int price,
      @RequestParam Integer quantity,
      Model model) {

    Item item = new Item();
    item.setItemName(itemName);
    item.setPrice(price);
    item.setQuantity(quantity);

    itemRepository.save(item);

    model.addAttribute("item", item);

    return "basic/item";
  }

  /**
   * @ModelAttribute의 name 속성 : model에 item이름으로 자동으로 addAttribute
   */
//  @PostMapping("/add")
  public String addItemV2(@ModelAttribute("item") Item item, Model model) {

    itemRepository.save(item);
//    model.addAttribute("item", item);

    return "basic/item";
  }

  /**
   * name 속성을 생략할 경우 default는 클래스명
   * Item -> item을 이름으로 저장.
   */
//  @PostMapping("/add")
  public String addItemV3(@ModelAttribute Item item) {
    itemRepository.save(item);
    return "basic/item";
  }

  /**
   * 객체 타입은 @ModelAttriBute 생략 가능.
   */
//  @PostMapping("/add")
  public String addItemV4(Item item) {
    itemRepository.save(item);
    return "basic/item";
  }

  /**
   * PRG 패턴 : Post - Redirect - Get
   * 새로고침에 의한 중복 상품 등록을 막는 패턴으로, Get으로 리다이렉트 한다.
   * 그러나 URL을 +로 합치는 것은 URL 인코딩이 안되기 때문에 위험하다.
   * 그리고 저장이 잘되었습니다. 라는 메세지 요구사항이 왔다면?
   */

//  @PostMapping("/add")
  public String addItemV5(Item item) {
    itemRepository.save(item);
    return "redirect:/basic/items/" + item.getId();
  }

  /**
   * RedirectAttributes 를 쓰면 URL에 특정 파라미터를 넣을 수 있다.
   * 이때 자동으로 URL 인코딩도 해준다.
   * 넣지 못하는 부분은 쿼리 파라미터로 붙여서 들어간다.
   * ?status=true
   */
  @PostMapping("/add")
  public String addItemV6(Item item, RedirectAttributes redirectAttributes) {
    Item saveItem = itemRepository.save(item);

    redirectAttributes.addAttribute("itemId", saveItem.getId());
    redirectAttributes.addAttribute("status", true);

    return "redirect:/basic/items/{itemId}";
  }

  @GetMapping("/{itemId}/edit")
  public String editForm(@PathVariable Long itemId, Model model) {
    Item item = itemRepository.findById(itemId);
    model.addAttribute("item", item);
    return "basic/editForm";
  }

  @PostMapping("/{itemId}/edit")
  public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
    itemRepository.update(itemId, item);
    return "redirect:/basic/items/{itemId}"; // redirect가 되면서 상품상세 호출
  }

  @PostConstruct
  public void init() {
    itemRepository.save(new Item("itemA", 10000, 10));
    itemRepository.save(new Item("itemB", 20000, 20));
  }

}
