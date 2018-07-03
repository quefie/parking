package com.danzal.parking.mappers;

import com.danzal.parking.domain.Currency;
import com.danzal.parking.domain.DayProfit;
import com.danzal.parking.models.DayProfitDTO;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DayProfitMapperTest {


    public static final Currency CURRENCY = Currency.PLN;
    public static final String DATE = "2018/06/24";
    DayProfitMapper dayProfitMapper = DayProfitMapper.INSTANCE;

    @Test
    public void testDayProfitToDayProfitDTO() throws Exception {

        DayProfit dayProfit = new DayProfit();
        dayProfit.setCurrency(CURRENCY);
        dayProfit.setDate(DATE);

        DayProfitDTO dayProfitDTO = dayProfitMapper.dayProfitToDayProfitDTO(dayProfit);

        assertEquals(CURRENCY, dayProfitDTO.getCurrency());
        assertEquals(DATE, dayProfitDTO.getDate());

    }

    @Test
    public void testDriverDTOToDriver() throws Exception {

        DayProfitDTO dayProfitDTO = new DayProfitDTO();
        dayProfitDTO.setCurrency(CURRENCY);
        dayProfitDTO.setDate(DATE);

        DayProfit dayProfit = dayProfitMapper.dayProfitDTOToDayProfit(dayProfitDTO);

        assertEquals(CURRENCY, dayProfit.getCurrency());
        assertEquals(DATE, dayProfit.getDate());

    }
}
