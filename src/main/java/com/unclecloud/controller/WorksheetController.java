package com.unclecloud.controller;

import com.unclecloud.domain.Worksheet;
import com.unclecloud.service.WorksheetService;
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
 * Worksheet Controller
 */
@Slf4j
@RestController
@RequestMapping("worksheet")
public class WorksheetController {

    @Autowired
    private WorksheetService worksheetService;

    /**
     * List
     */
    @GetMapping("")
    ModelAndView list() {
        ModelAndView mav = new ModelAndView();
        Sort sort = Sort.by(new Sort.Order(DESC, "createTime"));
        List<Worksheet> list = worksheetService.findAll(sort);
        mav.addObject("list", list);
        mav.addObject("count", list.size());
        mav.addObject("active", "worksheet");
        mav.setViewName("worksheet/WorksheetList");
        return mav;
    }

    /**
     * Edit
     */
    @GetMapping("edit/{id}")
    ModelAndView edit(@PathVariable(value = "id") Long id) {
        ModelAndView mav = new ModelAndView();
        Worksheet worksheet;
        if (0 == id) {
            worksheet = new Worksheet();
            worksheet.setId(id);
        } else {
            worksheet = worksheetService.findById(id);
        }
        mav.addObject("obj", worksheet);
        mav.setViewName("worksheet/WorksheetEdit");
        return mav;
    }

    /**
     * Save
     */
    @PostMapping("save")
    JsonResult save(@ModelAttribute("worksheet") @Valid Worksheet worksheet) {
        return jsonResultSuccess("保存成功！", worksheetService.save(worksheet));
    }

}
