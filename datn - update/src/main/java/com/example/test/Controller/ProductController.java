package com.example.test.Controller;

import com.example.test.Entity.Product;
import com.example.test.Exception.ProductNotFoundException;
import com.example.test.Repository.ProductRepository;
import com.example.test.Repository.TypeRepository;
import com.example.test.Repository.VendorRepository;
import com.example.test.Service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ProductController {
    private final String UPLOAD_DIR = "src/main/resources/static/img/";
    private final ProductRepository productRepository;
    private final ProductService productService;
    private final TypeRepository typeRepository;
    private final VendorRepository vendorRepository;


    public ProductController(ProductRepository productRepository,
                             ProductService productService, TypeRepository typeRepository,
                             VendorRepository vendorRepository) {
        this.productRepository = productRepository;
        this.productService = productService;
        this.typeRepository = typeRepository;
        this.vendorRepository = vendorRepository;
    }

    //    @GetMapping("/parameters/countries/page/{pageNumber}")
//    public String getOnePage(Model model, @PathVariable("pageNumber") int currentPage){
//        Page<Country> page = countryService.findPage(currentPage);
//        int totalPages = page.getTotalPages();
//        long totalItems = page.getTotalElements();
//        List<Country> countries = page.getContent();
//
//        model.addAttribute("currentPage", currentPage);
//        model.addAttribute("totalPages", totalPages);
//        model.addAttribute("totalItems", totalItems);
//        model.addAttribute("countries", countries);
//
//        return "/parameters/countries";
//    }
    @GetMapping("/ItemView")
    public String itemviewDefault(Model model) {
        Pageable pageable = PageRequest.of(0, 5);
        Page<Product> productPage = productRepository.findAll(pageable);
        int totalPages = productPage.getTotalPages();
        long totalItems = productPage.getTotalElements();
//    List<Product> productList= new ArrayList<>();
//    System.out.println(productPage);
//    for (Product product : productPage) {
//        if(product.getStatus()==1){
//            productList.add(product);
//        }
//    }
        model.addAttribute("productList", productPage);
        model.addAttribute("currentPage", 0);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalItems", totalItems);
        return "Item/View";
    }

    @GetMapping("/ItemView/{pageNumber}")
    public String itemview(Model model, @PathVariable(value = "pageNumber") int currentPage) {
        Pageable pageable = PageRequest.of(currentPage - 1, 5);
        Page<Product> productPage = productRepository.findAll(pageable);
        int totalPages = productPage.getTotalPages();
        long totalItems = productPage.getTotalElements();
//        List<Product> productList= new ArrayList<>();
//        System.out.println(productPage);
//        for (Product product : productPage) {
//            if(product.getStatus()==1){
//                productList.add(product);
//            }
//        }
        model.addAttribute("productList", productPage);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalItems", totalItems);
        return "Item/View";
    }

    @GetMapping("/ItemCreate")
    public String viewItemCreate(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("typeList", typeRepository.findAll());
        model.addAttribute("vendorList", vendorRepository.findAll());
        return "Item/Create";
    }

    @GetMapping("/ItemEdit")
    public String viewItemCreate(Model model, @RequestParam(name = "id") Integer id) throws ProductNotFoundException {
//        model.addAttribute("product", productRepository.getOne(id));
        model.addAttribute("product", productService.getProduct(id));
        model.addAttribute("typeList", typeRepository.findAll());
        model.addAttribute("vendorList", vendorRepository.findAll());
        return "Item/Edit";
    }

    @GetMapping("/searchProduct")
    public String searchProduct(Model model, @RequestParam(name = "keyword") String keyword) {
        System.out.println(keyword);
//        List<Product> products = productRepository.findAllByNameContaining(keyword);
        List<Product> products = productService.findAllByProductName(keyword);
        List<Product> productList = new ArrayList<>();
        for (Product product : products) {
            if (product.getStatus() == 1) {
                productList.add(product);
            }
        }
        model.addAttribute("productList", productList);
        return "Item/View";
    }

    @PostMapping("/ItemEdit")
    public String ItemEdit(Model model, @ModelAttribute Product product, RedirectAttributes redirectAttributes) {
        try {
            productService.saveProduct(product);
//            productRepository.save(product);
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("mess", "Sửa không thành công!");
            return "redirect:/ItemEdit";
        }
        return "redirect:/ItemView";
    }

    @PostMapping("/ItemCreate")
    public String itemCreate(Model model, @ModelAttribute Product product,
                             @RequestParam("type") String TypeId,
                             @RequestParam("vendor") String VendorId,
                             @RequestParam("image") MultipartFile file) {
        Pageable pageable = PageRequest.of(0, 5);
        Page<Product> productPage = productRepository.findAll(pageable);
        int totalPages = productPage.getTotalPages();

        // normalize the file path
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        // save the file on the local file system
        try {
            Path path = Paths.get(UPLOAD_DIR + fileName);
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            product.setPhoto(fileName);
            product.setType(typeRepository.getOne(Integer.parseInt(TypeId)));
            product.setVendor(vendorRepository.getOne(Integer.parseInt(VendorId)));
            product.setStatus(1);
//            if (productRepository.findAllByName(product.getName()) != null) {
            if (productService.checkProduct(product.getName()) == true) {
                model.addAttribute("mess", "Tên sản phẩm đã tồn tại! vui lòng nhập thông tin khác !");
                return "Item/Create";
            }
            productService.saveProduct(product);
        } catch (IOException e) {
            e.printStackTrace();
            return "Item/Create";
        }
        return "redirect:/ItemView/" + totalPages;
    }

    @GetMapping("/search")
    public String searchItem(Model model, @RequestParam(name = "keyword") String keyword) {
//        List<Product> productList = productRepository.findAllByNameContaining(keyword);
        List<Product> productList = productService.findAllByProductName(keyword);
        System.out.println(productList);
        return "Item/View";
    }

    @GetMapping("/ItemDelete")
    public String delete(Model model, @RequestParam Integer id) throws ProductNotFoundException {
//        Product product = productRepository.getOne(id);
        Product product = productService.getProduct(id);
        model.addAttribute("product", product);
        return "Item/Delete";
    }

    @PostMapping("/ItemDelete")
    public String toDelete(Model model, @RequestParam Integer id) throws ProductNotFoundException {
//        Product product = productRepository.getOne(id);
        Product product = productService.getProduct(id);
        product.setStatus(0);
        productService.saveProduct(product);
        return "redirect:/ItemView";
    }


    @GetMapping("/ItemEnable")
    public String enable(Model model, @RequestParam Integer id) throws ProductNotFoundException {
//        Product product = productRepository.getOne(id);
        Product product = productService.getProduct(id);
        product.setStatus(1);
        productService.saveProduct(product);
        return "redirect:/ItemView";
    }

}
