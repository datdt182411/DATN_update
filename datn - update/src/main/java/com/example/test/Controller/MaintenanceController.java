package com.example.test.Controller;

import com.example.test.Converter.ConvertDate;
import com.example.test.Converter.ConvertObject;
import com.example.test.Entity.Maintenance;
import com.example.test.Entity.Product;
import com.example.test.Exception.ProductNotFoundException;
import com.example.test.Repository.MaintenanceRepository;
import com.example.test.Repository.ProductRepository;
import com.example.test.Service.MaintenanceService;
import com.example.test.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class MaintenanceController {
    @Autowired
    ConvertObject convert;

    @Autowired
    ConvertDate convertDate;

    private final MaintenanceRepository maintenanceRepository;
    private final ProductRepository productRepository;

    private final MaintenanceService maintenanceService;

    private final ProductService productService;

    public MaintenanceController(MaintenanceRepository maintenanceRepository,
                                 ProductRepository productRepository, MaintenanceService maintenanceService, ProductService productService) {
        this.maintenanceRepository = maintenanceRepository;
        this.productRepository = productRepository;
        this.maintenanceService = maintenanceService;
        this.productService = productService;
    }


    @GetMapping("/Maintenance")
    public String maintenance(Model model) {

        List<Object> list = maintenanceRepository.findMaintenace();
        List<Maintenance> maintenanceList = new ArrayList<>();
        Integer index=0;
        for (Object o : list) {
            index+=1;
            Maintenance maintenance= new Maintenance();
            String[] data = convert.convert(o);
            int order_id = Integer.valueOf(data[0]);
            int product_id = Integer.valueOf(data[1]);
            String productName = (data[2]);
            String startDate = convertDate.convertDate(data[3]);
            String endDate = convertDate.convertDate(data[4]);
            String customer=(data[5]);
//
          maintenance= new Maintenance(index,order_id,startDate,endDate,3,"null",productName,customer,productRepository.getOne(product_id));
          maintenanceList.add(maintenance);
        }
        model.addAttribute("MaintenanceList", maintenanceList);
        return "Maintenance/View";
    }

    @GetMapping("/ItemMaintenanceCreate")
    public String maintenanceCre(Model model) {
        model.addAttribute("maintenance", new Maintenance());
        return "Maintenance/Create";
    }

    @PostMapping("/ItemMaintenanceCreate")
    public String maintenanceCreate(Model model, @ModelAttribute Maintenance maintenance,
                                    @RequestParam Integer productId, RedirectAttributes redirectAttributes) {
        Product product = new Product();
        Optional<Product> optionalProduct = Optional.of(productRepository.findById(productId).orElse(product));
        System.out.println(product);
        System.out.println(optionalProduct);
        try {
            if (optionalProduct.equals(product)) {
                redirectAttributes.addFlashAttribute("fail", "Nhập sai mã sản phẩm!");
                return "redirect:/ItemMaintenanceCreate";
            } else {
//                maintenance.setProduct(productRepository.getOne(productId));
                maintenance.setProduct(productService.getProduct(productId));
//                maintenanceRepository.save(maintenance);
                maintenanceService.saveMaintenance(maintenance);
                redirectAttributes.addFlashAttribute("success", "Thông tin bảo hành đã được thêm thành công!");
                return "redirect:/Maintenance";

            }
        } catch (Exception | ProductNotFoundException ex) {
            redirectAttributes.addFlashAttribute("fail", "Nhập sai mã sản phẩm, vui lòng nhập lại!");
            return "redirect:/ItemMaintenanceCreate";
        }

    }
}
