package com.example.test.Controller;

import com.example.test.Converter.ConvertObject;
import com.example.test.Entity.*;
import com.example.test.Repository.*;
import com.example.test.Service.ShoppingCartService;
import com.example.test.Service.ShoppingCartServiceTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Controller
public class TestController {
    @Autowired
    private ConvertObject convert;
    @Autowired
    ShoppingCartService shoppingCartService;
    private final ProductRepository productRepository;
    private final TypeRepository typeRepository;
    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;
    private final OrderLinerRepository orderLinerRepository;
    private final UserRepository userRepository;

    private final CartRepository cartRepository;

    private final ShoppingCartServiceTest shoppingCartServiceTest;

    public TestController(ProductRepository productRepository,
                          TypeRepository typeRepository,
                          CustomerRepository customerRepository,
                          OrderRepository orderRepository,
                          OrderLinerRepository orderLinerRepository,
                          UserRepository userRepository, CartRepository cartRepository, ShoppingCartServiceTest shoppingCartServiceTest) {
        this.productRepository = productRepository;
        this.typeRepository = typeRepository;
        this.customerRepository = customerRepository;
        this.orderRepository = orderRepository;
        this.orderLinerRepository = orderLinerRepository;
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
        this.shoppingCartServiceTest = shoppingCartServiceTest;
    }

    @GetMapping("/home")
    public String Index(Model model) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetail = (UserDetails) authentication.getPrincipal();
            model.addAttribute("userAuthen", userDetail.getUsername());
        } catch (Exception ex) {
            model.addAttribute("userAuthen", null);
        }

//        model.addAttribute("itemsWithFineList", itemIssuanceService.getItemsWithFine());
        return "home";
    }

    @GetMapping("/intro")
    public String intro(Model model) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetail = (UserDetails) authentication.getPrincipal();
            model.addAttribute("userAuthen", userDetail.getUsername());
        } catch (Exception ex) {
            model.addAttribute("userAuthen", null);
        }


//        model.addAttribute("itemsWithFineList", itemIssuanceService.getItemsWithFine());
        return "IntroPage/intro";
    }

    @GetMapping("/product")
    public String product(Model model, @RequestParam(name = "id") Integer id) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetail = (UserDetails) authentication.getPrincipal();
            model.addAttribute("userAuthen", userDetail.getUsername());
        } catch (Exception ex) {
            model.addAttribute("userAuthen", null);
        }
//        System.out.println(productRepository.findAllByType(typeRepository.getOne(id)).size());
        List<Product> productList= new ArrayList<>();
        for (Product product : productRepository.findAllByType(typeRepository.getOne(id))) {
            if(product.getStatus()==1){
//                System.out.println(product);
                productList.add(product);
            }
        }
        model.addAttribute("ProductList",productList );
        model.addAttribute("TypeList", typeRepository.findAll());
//        model.addAttribute("itemsWithFineList", itemIssuanceService.getItemsWithFine());
        return "IntroPage/product";
    }

    @GetMapping("/contact")
    public String contact(Model model) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetail = (UserDetails) authentication.getPrincipal();
//            System.out.println(userDetail.getAuthorities());
            model.addAttribute("userAuthen", userDetail.getUsername());
        } catch (Exception ex) {
            model.addAttribute("userAuthen", null);
        }

//        model.addAttribute("itemsWithFineList", itemIssuanceService.getItemsWithFine());
        return "IntroPage/contact";
    }


    @GetMapping("/order")
    public String order(Model model) {
        OrderLine orderLine = new OrderLine(1, null, new Order(), new Product());
        model.addAttribute("custommer", new Custommer());
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetail = (UserDetails) authentication.getPrincipal();
//            System.out.println(userDetail.getAuthorities());
            model.addAttribute("userAuthen", userDetail.getUsername());
        } catch (Exception ex) {
            model.addAttribute("userAuthen", null);
        }

        Collection<Cart> cart = shoppingCartServiceTest.getCart();
        System.out.println(cart);
        model.addAttribute("cartItems", cart);
        model.addAttribute("total", shoppingCartService.getAmount());
        double totalPrice = 0;
        for (Cart cartItem : cart) {
//            System.out.println(cartItem);
            double price = cartItem.getQuantity() * cartItem.getProduct().getPrice();
            totalPrice += price;
        }
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("totalCartItems", shoppingCartServiceTest.getCount());

//        model.addAttribute("itemsWithFineList", itemIssuanceService.getItemsWithFine());
        return "IntroPage/order";
    }

    @PostMapping("/order")
    public String addOrder(Model model,
                           @ModelAttribute Custommer custommer,
                           RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetail = (UserDetails) authentication.getPrincipal();
//        System.out.println(userDetail.getAuthorities());
        try {
            model.addAttribute("userAuthen", userDetail.getUsername());
        } catch (Exception ex) {
            model.addAttribute("userAuthen", null);
        }
        try {
            customerRepository.save(custommer);
            Collection<Cart> cart = shoppingCartServiceTest.getCart();
            model.addAttribute("cart", cart);
            model.addAttribute("total", shoppingCartService.getAmount());
            double totalPrice = 0;
            for (Cart carts : cart) {
                System.out.println(carts);
                double price = carts.getQuantity() * carts.getProduct().getPrice();
                totalPrice += price;
            }
            Order order = new Order();
            order.setOrderDate(LocalDate.now());
            order.setAmount(totalPrice);
            order.setCustommer(customerRepository.getOne(custommer.getId()));
            order.setUser(userRepository.getUsersByUsername(userDetail.getUsername()));
//            System.out.println(userRepository.getUsersByUsername(userDetail.getUsername()));
            orderRepository.save(order);
            List<OrderLine> orderLineList = new ArrayList<>();

            for (Cart cartItem : cart) {
                OrderLine orderLine = new OrderLine();
//                System.out.println("oiddddddddđ" + order.getId());
                orderLine.setOrder(orderRepository.getOne(order.getId()));
                orderLine.setProduct(productRepository.getOne(cartItem.getId()));
                orderLine.setQuantity(cartItem.getQuantity());

                System.out.println(orderLine);
                orderLinerRepository.save(orderLine);
                System.out.println(orderLine);
                orderLineList.add(orderLine);
            }
            redirectAttributes.addFlashAttribute("success", "Đơn hàng đã được gửi thành công!");
            shoppingCartService.clear();
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("error", "Thông tin đơn hàng không hợp lệ, vui lòng nhập lại!");
        }

//        model.addAttribute("itemsWithFineList", itemIssuanceService.getItemsWithFine());
        return "redirect:/history";
    }

//    @GetMapping("/addToCart")
//    public String addToCart(Model model, @RequestParam("id")Integer id, @RequestParam("itemQuantity") int quantity, @RequestParam("userid") Integer userid) {
//        Optional<Users> optionalUsers = userRepository.findById(userid);
//        Users users = optionalUsers.get();
//
//        Optional<Product> optionalProduct = productRepository.findById(id);
//        Product product1 = optionalProduct.get();
//        Cart cart = new Cart();
//        cart.setQuantity(quantity);
//        cart.setUsers(users);
//        cart.setProduct(product1);
////        cartRepository.save(cart);
//        cartRepository.save(cart);
//
////        model.addAttribute("itemsWithFineList", itemIssuanceService.getItemsWithFine());
//        System.out.println(shoppingCartService.getCartItems().size());
//        return "redirect:/order";
//    }

    @GetMapping("/addToCart")
    public String addToCart(Model model, @RequestParam Integer id) {
        Product product = productRepository.getOne(id);
        shoppingCartService.add(new Cart(product.getId(), product.getName(), product.getPhoto(), 1, product.getPrice()));
//        model.addAttribute("itemsWithFineList", itemIssuanceService.getItemsWithFine());
        System.out.println(shoppingCartServiceTest.getCart().size());
        System.out.println(shoppingCartServiceTest.getCart());
        return "redirect:/order";
    }

    @GetMapping("/history")
    public String history(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetail = (UserDetails) authentication.getPrincipal();
        System.out.println(userDetail.getAuthorities());
        try {
            model.addAttribute("userAuthen", userDetail.getUsername());
        } catch (Exception ex) {
            model.addAttribute("userAuthen", null);
        }
        Users users=  userRepository.getUsersByUsername( userDetail.getUsername());
        List<Order> orders=users.getOrderList();
        model.addAttribute("orderList",orders);
        return "IntroPage/history";
    }

    @GetMapping("/historyDetail")
    public String orderDetail(Model model, @RequestParam Integer id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetail = (UserDetails) authentication.getPrincipal();
        System.out.println(userDetail.getAuthorities());
        try {
            model.addAttribute("userAuthen", userDetail.getUsername());
        } catch (Exception ex) {
            model.addAttribute("userAuthen", null);
        }

        model.addAttribute("order", orderRepository.getOne(id));
        String total = String.valueOf(orderRepository.getOne(id).getAmount());
        String newTotal= total.substring(0,total.length()-2);
        model.addAttribute("total",newTotal);
        List<Object> list = orderRepository.managerOrder(id);
//        List<HistoryForUser> historyForUsers = new ArrayList<>();
//        HistoryForUser h;
        List<String> customerName = new ArrayList<>();
        List<String> quantities = new ArrayList<>();
        List<String> prices = new ArrayList<>();
        List<String> photos = new ArrayList<>();
        for (Object o : list) {
            String[] data = convert.convert(o);
            String cusName = (data[0]);
            String quantity = (data[2]);
            String price = (data[1]);
            String photo = (data[3].substring(1, data[3].length() - 1));

            customerName.add(cusName);
            quantities.add(quantity);
            prices.add(price);
            photos.add(photo);
        }
        model.addAttribute("customerName", customerName);
        model.addAttribute("quantities", quantities);
        model.addAttribute("prices", prices);
        model.addAttribute("photos", photos);
        return "IntroPage/historyDetail";
    }
    @GetMapping("/removeCart")
    public String removeToCart(Model model, @RequestParam Integer id) {
        shoppingCartService.remove(id);
        return "redirect:/order";
    }

    @GetMapping("/productDetail")
    public String productDetail(Model model, @RequestParam Integer id) {
//        model.addAttribute("user", )
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetail = (UserDetails) authentication.getPrincipal();
        Users users = userRepository.getUsersByUsername(userDetail.getUsername());
        Integer userid = users.getId();
        model.addAttribute("userAuthenId", userid);
        model.addAttribute("product", productRepository.getOne(id));
        return "IntroPage/ProductDetail";
    }

    //Create new Display
//    @GetMapping("/productDetail")
//    public String productDetailDemo(Model model, @RequestParam Integer id) {
//        model.addAttribute("product", productRepository.getOne(id));
//        return "cart/ProductDetailDemo";
//    }

    @GetMapping("/")
    public String homeIntro(Model model) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetail = (UserDetails) authentication.getPrincipal();
            System.out.println(userDetail.getAuthorities());
            model.addAttribute("userAuthen", userDetail.getUsername());
        } catch (Exception ex) {
            model.addAttribute("userAuthen", null);
        }
        List<Product> productList= new ArrayList<>();
        for (Product product : productRepository.findAll()) {
            if(product.getStatus()==1){
                productList.add(product);
            }
        }
        model.addAttribute("ProductList",productList );
        return "IntroPage/home";
    }

}


