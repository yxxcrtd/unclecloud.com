package com.unclecloud.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@RestController
@RequestMapping("manage")
public class ManageController {

    /**
     * Index
     *
     * @return
     */
    @GetMapping("")
    ModelAndView manage() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("manage/Manage");
        return mav;
    }

}
