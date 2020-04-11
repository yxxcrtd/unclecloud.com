package com.unclecloud.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * Code Generate Controller
 */
@Slf4j
@RestController
@RequestMapping("code")
public class CodeController {

    /**
     * List
     */
    @GetMapping("{tableName}")
    ModelAndView generate(@PathVariable String tableName) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("user/UserList");
        return mav;
    }

}
