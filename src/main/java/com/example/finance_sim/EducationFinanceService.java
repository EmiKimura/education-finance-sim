package com.example.finance_sim;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class EducationFinanceService {

    private static final Map<String, Double> COURSE_MAP = new HashMap<>() {{
        put("national", 2500000.0);
        put("private_arts", 4000000.0);
        put("private_science", 5500000.0);
        put("private_medical", 20000000.0);
    }};

    public Map<String, Object> calculateAll(String course, double currentSavings, int yearsUntilCollege, double annualRate) {
        double targetAmount = COURSE_MAP.getOrDefault(course, 4000000.0);
        
        BigDecimal savingBank = calculateMonthly(targetAmount, currentSavings, yearsUntilCollege, 0.01);
        BigDecimal savingNisa = calculateMonthly(targetAmount, currentSavings, yearsUntilCollege, annualRate);
        BigDecimal benefit = savingBank.subtract(savingNisa);

        Map<String, Object> results = new HashMap<>();
        results.put("savingBank", savingBank);
        results.put("savingNisa", savingNisa);
        results.put("benefit", benefit);
        return results;
    }

    private BigDecimal calculateMonthly(double targetAmount, double currentSavings, int yearsUntilCollege, double rate) {
        double shortFall = targetAmount - currentSavings;
        if (shortFall <= 0) return BigDecimal.ZERO;

        int months = yearsUntilCollege * 12;
        if (months <= 0) return BigDecimal.valueOf(shortFall);

        double monthlyRate = rate / 100 / 12;
        double monthlySaving = shortFall * (monthlyRate / (Math.pow(1 + monthlyRate, months) - 1));
        return BigDecimal.valueOf(monthlySaving).setScale(0, RoundingMode.HALF_UP);
    }

    public Map<String, String> getCourseOptions() {
        Map<String, String> options = new HashMap<>();
        options.put("national", "国立大学（約250万円）");
        options.put("private_arts", "私立文系（約400万円）");
        options.put("private_science", "私立理系（約550万円）");
        options.put("private_medical", "私立医歯薬（約2000万円）");
        return options;
    }
}