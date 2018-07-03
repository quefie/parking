package com.danzal.parking.controllers;

import com.danzal.parking.domain.Currency;
import com.danzal.parking.models.DayProfitDTO;
import com.danzal.parking.services.DayProfitService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class DayProfitControllerTest {

    @Mock
    DayProfitService dayProfitService;

    @InjectMocks
    DayProfitController dayProfitController;

    MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(dayProfitController)
                .setControllerAdvice(new RestResponseEntityExceptionHandler())
                .build();
    }

    @Test
    public void testCheckDayProfit() throws Exception {

        when(dayProfitService.checkDayProfit(anyString())).thenReturn(1.0f);

        mockMvc.perform(get(DayProfitController.BASE_URL + "/2018/06/22/profit"))
                .andExpect(status().isOk())
                .andExpect(content().string("1.0"));

    }

    @Test
    public void testGetDayProfitInfo() throws Exception {

        DayProfitDTO dayProfitDTO = new DayProfitDTO();
        dayProfitDTO.setDate("2018/06/24");
        dayProfitDTO.setDayUrl(DayProfitController.BASE_URL + "/2018/06/24");

        DayProfitDTO returnDayProfitDTO = new DayProfitDTO();
        returnDayProfitDTO.setDate(dayProfitDTO.getDate());
        returnDayProfitDTO.setDayUrl(dayProfitDTO.getDayUrl());

        when(dayProfitService.getProfitDayInfo(anyString())).thenReturn(returnDayProfitDTO);

        mockMvc.perform(get(DayProfitController.BASE_URL + "/2018/06/24/show"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.date", equalTo("2018/06/24")))
                .andExpect(jsonPath("$.profit", equalTo(0.0)))
                .andExpect(jsonPath("$.day_url", equalTo("/day/2018/06/24")));
    }

    @Test
    public void testGetDayProfitCurrency() throws Exception {

        when(dayProfitService.getDayProfitCurrency(anyString())).thenReturn(Currency.PLN);

        mockMvc.perform(get(DayProfitController.BASE_URL + "/2018/06/24/currency"))
                .andExpect(status().isOk())
                .andExpect(content().string("\"PLN\""));
    }
}
