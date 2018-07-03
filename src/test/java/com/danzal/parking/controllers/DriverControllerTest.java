package com.danzal.parking.controllers;

import com.danzal.parking.domain.Currency;
import com.danzal.parking.domain.DriverType;
import com.danzal.parking.models.DriverDTO;
import com.danzal.parking.services.DriverService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class DriverControllerTest extends AbstractRestControllerTest {

    @Mock
    DriverService driverService;

    @InjectMocks
    DriverController driverController;

    MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(driverController)
                .setControllerAdvice(new RestResponseEntityExceptionHandler())
                .build();
    }

    @Test
    public void testStartParkingMeter() throws Exception {
        DriverDTO driverDTO = new DriverDTO();
        driverDTO.setCurrency(Currency.PLN);
        driverDTO.setDriverType(DriverType.VIP);

        Date currDate = new Date();
        String timeDateFormat = "HH:mm:ss";
        DateFormat timeFormat = new SimpleDateFormat(timeDateFormat);
        String formattedTime = timeFormat.format(currDate);
        driverDTO.setStartTime(formattedTime);

        DriverDTO returnDTO = new DriverDTO();
        returnDTO.setDriverType(driverDTO.getDriverType());
        returnDTO.setCurrency(driverDTO.getCurrency());
        returnDTO.setStartTime(driverDTO.getStartTime());
        returnDTO.setDriverUrl(DriverController.BASE_URL + "/1");

        when(driverService.startParkingMeter(driverDTO)).thenReturn(returnDTO);

        mockMvc.perform(post(DriverController.BASE_URL + "/start")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(driverDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.startTime", equalTo(formattedTime)))
                .andExpect(jsonPath("$.currency", equalTo("PLN")))
                .andExpect(jsonPath("$.driverType", equalTo("VIP")))
                .andExpect(jsonPath("$.driver_url", equalTo(DriverController.BASE_URL + "/1")));

    }

    @Test
    public void testCheckTicketValid() throws Exception {

        when(driverService.checkTicketValid(anyLong())).thenReturn(true);

        mockMvc.perform(get(DriverController.BASE_URL + "/1/check"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

    }

    @Test
    public void testStopParkingMeter() throws Exception {

        DriverDTO driverDTO = new DriverDTO();
        driverDTO.setCurrency(Currency.PLN);
        driverDTO.setDriverType(DriverType.VIP);

        Date currDate = new Date();
        String timeDateFormat = "HH:mm:ss";
        DateFormat timeFormat = new SimpleDateFormat(timeDateFormat);
        String formattedTime = timeFormat.format(currDate);
        driverDTO.setStopTime(formattedTime);

        DriverDTO returnDTO = new DriverDTO();
        returnDTO.setDriverType(driverDTO.getDriverType());
        returnDTO.setCurrency(driverDTO.getCurrency());
        returnDTO.setStopTime(driverDTO.getStopTime());
        returnDTO.setDriverUrl(DriverController.BASE_URL + "/1/stop");

        when(driverService.stopParkingMeter(anyLong())).thenReturn(returnDTO);

        mockMvc.perform(put(DriverController.BASE_URL + "/1/stop")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(driverDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stopTime", equalTo(formattedTime)))
                .andExpect(jsonPath("$.currency", equalTo("PLN")))
                .andExpect(jsonPath("$.driverType", equalTo("VIP")))
                .andExpect(jsonPath("$.driver_url", equalTo(DriverController.BASE_URL + "/1/stop")));


    }

    @Test
    public void testCheckAmoutToPay() throws Exception {

        when(driverService.checkAmountToPay(anyLong())).thenReturn(1.0f);

        mockMvc.perform(get(DriverController.BASE_URL + "/1/cost"))
                .andExpect(status().isOk())
                .andExpect(content().string("1.0"));
    }

    @Test
    public void testGetDriverCurrency() throws Exception {

        when(driverService.checkCurrency(anyLong())).thenReturn(Currency.PLN);

        mockMvc.perform(get(DriverController.BASE_URL + "/1/currency"))
                .andExpect(status().isOk())
                .andExpect(content().string("\"PLN\""));

    }

    @Test
    public void testGetDriverInfo() throws Exception {

        DriverDTO driverDTO = new DriverDTO();
        driverDTO.setCurrency(Currency.PLN);
        driverDTO.setDriverType(DriverType.VIP);

        DriverDTO returnDTO = new DriverDTO();
        returnDTO.setDriverType(driverDTO.getDriverType());
        returnDTO.setCurrency(driverDTO.getCurrency());
        returnDTO.setDriverUrl(DriverController.BASE_URL + "/1/show");

        when(driverService.findDriverById(anyLong())).thenReturn(returnDTO);

        mockMvc.perform(get(DriverController.BASE_URL + "/1/show")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(driverDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currency", equalTo("PLN")))
                .andExpect(jsonPath("$.driverType", equalTo("VIP")))
                .andExpect(jsonPath("$.driver_url", equalTo(DriverController.BASE_URL + "/1/show")));

    }
}
