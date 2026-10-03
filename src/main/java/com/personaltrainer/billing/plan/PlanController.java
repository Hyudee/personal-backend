package com.personaltrainer.billing.plan;

import com.personaltrainer.billing.plan.dto.PlanResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping ("/api/plans")
@RequiredArgsConstructor

public class PlanController {

    private final PlanRepository planRepository;

    @GetMapping
    public List <PlanResponse> listarAtivos(){
        return planRepository.findByActiveTrueOrderByPriceAsc().stream()
                .map(PlanResponse::from)
                .toList();
    }
}
