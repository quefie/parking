package com.danzal.parking.services;


import com.danzal.parking.controllers.DayProfitController;
import com.danzal.parking.domain.Currency;
import com.danzal.parking.domain.DayProfit;
import com.danzal.parking.domain.Driver;
import com.danzal.parking.mappers.DayProfitMapper;
import com.danzal.parking.models.DayProfitDTO;
import com.danzal.parking.repositories.DayProfitRepository;
import com.danzal.parking.repositories.DriverRepository;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DayProfitServiceImpl implements DayProfitService {


    private final DriverRepository driverRepository;
    private final DayProfitRepository dayProfitRepository;
    private final DayProfitMapper dayProfitMapper;

    public DayProfitServiceImpl(DriverRepository driverRepository, DayProfitRepository dayProfitRepository, DayProfitMapper dayProfitMapper) {
        this.driverRepository = driverRepository;
        this.dayProfitRepository = dayProfitRepository;
        this.dayProfitMapper = dayProfitMapper;
    }


    // ============= GET DAY PROFIT INFO METHOD ===============
    @Override
    public DayProfitDTO getProfitDayInfo(String date) {

        return dayProfitRepository.findByDate(date)
                .map(dayProfitMapper::dayProfitToDayProfitDTO)
                .map(dayProfitDTOreturned -> {
                    dayProfitDTOreturned.setDayUrl(getDayProfitUrl(date));
                    return dayProfitDTOreturned;
                })
                .orElseThrow(ResourceNotFoundException::new);


    }

    // ============= CHECK DAY PROFIT METHOD ===============
    @Override
    public float checkDayProfit(String date){
        return getProfitDayInfo(date).getProfit();
    }

    // ============= GET DAY PROFIT CURRENCY METHOD ===============
    @Override
    public Currency getDayProfitCurrency(String date) {
        return getProfitDayInfo(date).getCurrency();
    }

    // ============= CALCULATE DAY PROFIT METHODS ===============
    @Override
    public void saveOrUpdateDayProfit(String date) {

        Optional<DayProfit> dayProfit = dayProfitRepository.findByDate(date);

        if(dayProfit.isPresent()) {
               updateDayProfit(date);
           } else {
               createDay(date);
           }

    }

    private void updateDayProfit(String date) {

        DayProfit dayProfit = dayProfitRepository.findByDate(date).get();
        dayProfit.setProfit(calcDayProfit(date));
        saveAndReturnDTO(dayProfit);
    }

    private DayProfitDTO createDay(String date) {

        DayProfitDTO dayProfitDTO = new DayProfitDTO();
        dayProfitDTO.setDate(date);
        dayProfitDTO.setProfit(calcDayProfit(date));

        return saveAndReturnDTO(dayProfitMapper.dayProfitDTOToDayProfit(dayProfitDTO));
    }

    private DayProfitDTO saveAndReturnDTO(DayProfit dayProfit) {

        dayProfit.setDate(dayProfit.getDate());
        DayProfit savedDay = dayProfitRepository.save(dayProfit);
        DayProfitDTO returnedDayDTO = dayProfitMapper.dayProfitToDayProfitDTO(savedDay);
        returnedDayDTO.setDayUrl(getDayProfitUrl(savedDay.getDate()));

        return returnedDayDTO;
    }

    private float calcDayProfit(String date) {

        List<Driver> drivers = driverRepository.findAll();

        float profit = 0.0f;

        for (Driver driver : drivers
                ) {

            if (Objects.equals(driver.getTransactionDay(), date)) {
                profit = profit + driver.getAmountToPay();
            }
                    }

        return profit;
    }



    private String getDayProfitUrl(String date) {

        return DayProfitController.BASE_URL + date;
    }

}
