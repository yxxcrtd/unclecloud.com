package com.unclecloud.controller;

import com.unclecloud.domain.Product;
import com.unclecloud.service.ProductService;
import com.unclecloud.util.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

import static com.unclecloud.util.JsonResult.jsonResultSuccess;
import static org.springframework.data.domain.Sort.Direction.DESC;

/**
 * Product Controller
 */
@Slf4j
@RestController
@RequestMapping("product")
public class ProductController {

    @Autowired
    private ProductService productService;

    /**
     * List
     */
    @GetMapping("")
    ModelAndView list() {
        ModelAndView mav = new ModelAndView();
        Sort sort = Sort.by(new Sort.Order(DESC, "createTime"));
        List<Product> list = productService.findAll(sort);
        mav.addObject("list", list);
        mav.addObject("count", list.size());
        mav.addObject("active", "product");
        mav.setViewName("product/ProductList");
        return mav;
    }

    /**
     * Edit
     */
    @GetMapping("edit/{id}")
    ModelAndView edit(@PathVariable(value = "id") Long id) {
        ModelAndView mav = new ModelAndView();
        Product product;
        if (0 == id) {
            product = new Product();
            product.setId(id);
        } else {
            product = productService.findById(id);
        }
        mav.addObject("obj", product);
        mav.setViewName("product/ProductEdit");
        return mav;
    }

    /**
     * Save
     */
    @PostMapping("save")
    JsonResult save(@ModelAttribute("product") @Valid Product product) {
        return jsonResultSuccess("保存成功123456！", productService.save(product));
    }

}
