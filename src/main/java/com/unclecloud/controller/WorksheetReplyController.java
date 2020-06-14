package com.unclecloud.controller;

import com.unclecloud.domain.WorksheetReply;
import com.unclecloud.service.WorksheetReplyService;
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
 * WorksheetReply Controller
 */
@Slf4j
@RestController
@RequestMapping("worksheetReply")
public class WorksheetReplyController {

    @Autowired
    private WorksheetReplyService worksheetReplyService;

    /**
     * List
     */
    @GetMapping("")
    ModelAndView list() {
        ModelAndView mav = new ModelAndView();
        Sort sort = Sort.by(new Sort.Order(DESC, "createTime"));
        List<WorksheetReply> list = worksheetReplyService.findAll(sort);
        mav.addObject("list", list);
        mav.addObject("count", list.size());
        mav.addObject("active", "worksheetReply");
        mav.setViewName("worksheetReply/WorksheetReplyList");
        return mav;
    }

    /**
     * Edit
     */
    @GetMapping("edit/{id}")
    ModelAndView edit(@PathVariable(value = "id") Long id) {
        ModelAndView mav = new ModelAndView();
        WorksheetReply worksheetReply;
        if (0 == id) {
            worksheetReply = new WorksheetReply();
            worksheetReply.setId(id);
        } else {
            worksheetReply = worksheetReplyService.findById(id);
        }
        mav.addObject("obj", worksheetReply);
        mav.setViewName("worksheetReply/WorksheetReplyEdit");
        return mav;
    }

    /**
     * Save
     */
    @PostMapping("save")
    JsonResult save(@ModelAttribute("worksheetReply") @Valid WorksheetReply worksheetReply) {
        return jsonResultSuccess("保存成功！", worksheetReplyService.save(worksheetReply));
    }

}
