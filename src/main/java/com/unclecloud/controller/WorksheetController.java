package com.unclecloud.controller;

import com.unclecloud.domain.Worksheet;
import com.unclecloud.service.WorksheetService;
import com.unclecloud.util.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static com.unclecloud.util.Constants.PAGE_SIZE;
import static com.unclecloud.util.FileUtil.uploadFile;
import static com.unclecloud.util.JsonResult.jsonResultSuccess;
import static org.springframework.data.domain.Sort.Direction.ASC;

/**
 * Worksheet Controller
 */
@Slf4j
@RestController
@RequestMapping("manage/worksheet")
public class WorksheetController {

    @Autowired
    private WorksheetService worksheetService;

    protected @Value("${spring.http.multipart.location}") String UPLOAD_PATH;

    /**
     * List
     */
    @GetMapping("")
    ModelAndView list(HttpServletRequest request,
                      @RequestParam(value = "p", defaultValue = "1") int p,
                      @RequestParam(value = "k", defaultValue = "", required = false) String k) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("k", k);
        Sort sort = Sort.by(new Sort.Order(ASC, "status"));
        Pageable pageable = PageRequest.of(p - 1, PAGE_SIZE, sort);

        Worksheet worksheet = new Worksheet();
        worksheet.setTitle(k);
        ExampleMatcher matcher = ExampleMatcher.matching().withMatcher("title", ExampleMatcher.GenericPropertyMatchers.exact());
        Example<Worksheet> example = Example.of(worksheet, matcher);
        Page<Worksheet> pages = worksheetService.findAll(example, pageable);

        mav.addObject("list", pages);
        mav.addObject("count", pages.getTotalPages());
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
            worksheet = worksheetService.findById(id); // todo
        }
        mav.addObject("obj", worksheet);
        mav.setViewName("worksheet/WorksheetEdit");
        return mav;
    }

    /**
     * Save
     */
    @PostMapping("save")
    JsonResult save(HttpServletRequest request, @ModelAttribute("worksheet") @Valid Worksheet worksheet, BindingResult result,
                    @RequestParam(value = "file", required = false) MultipartFile file) throws Exception {


        worksheet.setUserId(1); // todo
        worksheet.setStatus(0);

        String fileName;
        if (null != file && !file.isEmpty()) {
            fileName = uploadFile(file, UPLOAD_PATH + "worksheet/");
            log.info("上传的工单附件原始附件名称：{}，上传后的文件名：{}", file.getOriginalFilename(), fileName);
            if (!"".equals(fileName) && null != fileName) {
                worksheet.setFile(fileName);
            }
        }
        return jsonResultSuccess("保存成功！", worksheetService.save(worksheet));
    }

}
