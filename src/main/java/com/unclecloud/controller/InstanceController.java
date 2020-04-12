package com.unclecloud.controller;

import com.unclecloud.domain.Instance;
import com.unclecloud.service.InstanceService;
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
 * Instance Controller
 */
@Slf4j
@RestController
@RequestMapping("instance")
public class InstanceController {

    @Autowired
    private InstanceService instanceService;

    /**
     * List
     */
    @GetMapping("")
    ModelAndView list() {
        ModelAndView mav = new ModelAndView();
        Sort sort = Sort.by(new Sort.Order(DESC, "createTime"));
        List<Instance> list = instanceService.findAll(sort);
        mav.addObject("list", list);
        mav.addObject("count", list.size());
        mav.addObject("active", "instance");
        mav.setViewName("instance/Instance");
        return mav;
    }

    /**
     * Edit
     */
    @GetMapping("edit/{id}")
    ModelAndView edit(@PathVariable(value = "id") String id) {
        ModelAndView mav = new ModelAndView();
        Instance instance;
        if ("0".equals(id)) {
            instance = new Instance();
            instance.setId(id);
        } else {
            instance = instanceService.findById(id);
        }
        mav.addObject("obj", instance);
        mav.setViewName("instance/InstanceEdit");
        return mav;
    }

    /**
     * Save
     */
    @PostMapping("save")
    JsonResult save(@ModelAttribute("instance") @Valid Instance instance) {
        return jsonResultSuccess("保存成功！", instanceService.save(instance));
    }

}
