package com.example.finance_sim;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class FinanceController {

    @Autowired
    private EducationFinanceService financeService;

    @GetMapping("/")
    public String index(Model model) {
        // 初期表示の設定
        model.addAttribute("courses", financeService.getCourseOptions());
        model.addAttribute("currentSavings", 0);
        model.addAttribute("yearsUntilCollege", 10);
        model.addAttribute("annualRate", 3.0);
        return "index";
    }

    @PostMapping("/calculate")
    public String calculate(
            @RequestParam String course,
            @RequestParam double currentSavings,
            @RequestParam int yearsUntilCollege,
            @RequestParam double annualRate,
            Model model) {

        // 1. 計算実行
        Map<String, Object> results = financeService.calculateAll(course, currentSavings, yearsUntilCollege, annualRate);
        
        // 2. 計算結果をModelに入れる
        model.addAllAttributes(results);
        
        // 3. 入力された値をそのままModelに戻す（これでリセットを防ぐ）
        model.addAttribute("selectedCourse", course);
        model.addAttribute("currentSavings", currentSavings);
        model.addAttribute("yearsUntilCollege", yearsUntilCollege);
        model.addAttribute("annualRate", annualRate);

        // 4. 選択肢を再送
        model.addAttribute("courses", financeService.getCourseOptions());
        
        return "index";
    }
}