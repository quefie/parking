package com.danzal.parking.services;

import com.danzal.parking.domain.Currency;
import com.danzal.parking.domain.DayProfit;
import com.danzal.parking.mappers.DayProfitMapper;
import com.danzal.parking.models.DayProfitDTO;
import com.danzal.parking.repositories.DayProfitRepository;
import com.danzal.parking.repositories.DriverRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class DayProfitServiceTest {

    public static final String DATE = "2018/06/24";

    DayProfitService dayProfitService;

    @Mock
    DriverRepository driverRepository;
    @Mock
    DayProfitRepository dayProfitRepository;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        dayProfitService = new DayProfitServiceImpl(driverRepository, dayProfitRepository, DayProfitMapper.INSTANCE);

    }

    @Test
    public void testGetProfitDayInfo() throws Exception {
        DayProfit dayProfit = new DayProfit();
        dayProfit.setId(1L);
        dayProfit.setDate(DATE);
        dayProfit.setProfit(3L);

        when(dayProfitRepository.findByDate(anyString())).thenReturn(Optional.ofNullable(dayProfit));

        DayProfitDTO dayProfitDTO = dayProfitService.getProfitDayInfo(DATE);

        assertEquals(DATE, dayProfitDTO.getDate());
        assertEquals(3L, dayProfitDTO.getProfit(), 0.0);

    }

    @Test
    public void testCheckDayProfit() throws Exception {
        DayProfit dayProfit = new DayProfit();
        dayProfit.setDate(DATE);
        dayProfit.setProfit(3L);

        when(dayProfitRepository.findByDate(anyString())).thenReturn(Optional.ofNullable(dayProfit));

        float profit = dayProfitService.checkDayProfit(DATE);

        assertEquals(dayProfit.getProfit(), profit, 0.0);

    }

    @Test
    public void testGetDayProfitCurrency() throws Exception {

        DayProfit dayProfit = new DayProfit();
        dayProfit.setCurrency(Currency.PLN);

        when(dayProfitRepository.findByDate(anyString())).thenReturn(Optional.ofNullable(dayProfit));

        Currency currency = dayProfitService.getDayProfitCurrency(DATE);

        assertEquals(dayProfit.getCurrency(), currency);
    }


}
