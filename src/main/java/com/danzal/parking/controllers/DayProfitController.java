package com.danzal.parking.controllers;


import com.danzal.parking.domain.Currency;
import com.danzal.parking.models.DayProfitDTO;
import com.danzal.parking.services.DayProfitService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(DayProfitController.BASE_URL)
public class DayProfitController {

    public static final String BASE_URL = "/day";

    private final DayProfitService dayProfitService;

    public DayProfitController(DayProfitService dayProfitService) {
        this.dayProfitService = dayProfitService;
    }

    @GetMapping("/{year}/{month}/{day}/profit")
    @ResponseStatus(HttpStatus.OK)
    public float checkDayProfit(@PathVariable String year, @PathVariable String month, @PathVariable String day){
        String date = year + "/" + month + "/" + day;
        return dayProfitService.checkDayProfit(date);
    }

    @GetMapping("/{year}/{month}/{day}/show")
    @ResponseStatus(HttpStatus.OK)
    public DayProfitDTO getDayProfitInfo(@PathVariable String year, @PathVariable String month, @PathVariable String day) {
        String date = year + "/" + month + "/" + day;


        return dayProfitService.getProfitDayInfo(date);
    }

    @GetMapping("/{year}/{month}/{day}/currency")
    @ResponseStatus(HttpStatus.OK)
    public Currency getDayProfitCurrency(@PathVariable String year, @PathVariable String month, @PathVariable String day) {
        String date = year + "/" + month + "/" + day;
        return dayProfitService.getDayProfitCurrency(date);
    }

}
