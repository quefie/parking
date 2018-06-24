package com.danzal.parking.services;

import com.danzal.parking.controllers.DriverController;
import com.danzal.parking.domain.Currency;
import com.danzal.parking.domain.DayProfit;
import com.danzal.parking.domain.Driver;
import com.danzal.parking.domain.DriverType;
import com.danzal.parking.mappers.DayProfitMapper;
import com.danzal.parking.mappers.DriverMapper;
import com.danzal.parking.models.DriverDTO;
import com.danzal.parking.repositories.DayProfitRepository;
import com.danzal.parking.repositories.DriverRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class DriverServiceTest {

    public static final Long ID = 2L;
    public static final DriverType DRIVER_TYPE = DriverType.VIP;

    DriverService driverService;

    DayProfitService dayProfitService;

    @Mock
    DriverRepository driverRepository;

    @Mock
    DayProfitRepository dayProfitRepository;

    @Before
    public void setUp() throws Exception{
        MockitoAnnotations.initMocks(this);

        dayProfitService = new DayProfitServiceImpl(driverRepository ,dayProfitRepository, DayProfitMapper.INSTANCE);

        driverService = new DriverServiceImpl(DriverMapper.INSTANCE,dayProfitService,driverRepository );
    }

    @Test
    public void testStartParkingMeter() throws Exception{
        DriverDTO driverDTO = new DriverDTO();
        driverDTO.setDriverType(DRIVER_TYPE);

        Driver savedDriver = new Driver();
        savedDriver.setDriverType(driverDTO.getDriverType());
        savedDriver.setCurrency(driverDTO.getCurrency());
        savedDriver.setId(1l);

        when(driverRepository.save(any(Driver.class))).thenReturn(savedDriver);

        DriverDTO savedDTO = driverService.startParkingMeter(driverDTO);

        assertEquals(driverDTO.getDriverType(), savedDTO.getDriverType());
        assertEquals("/driver/1", savedDTO.getDriverUrl());
    }

    @Test
    public void testStopParkingMeter() throws Exception{
        DriverDTO driverDTO = new DriverDTO();
        driverDTO.setDriverType(DRIVER_TYPE);

        Driver savedDriver = new Driver();
        savedDriver.setDriverType(driverDTO.getDriverType());
        savedDriver.setCurrency(driverDTO.getCurrency());
        savedDriver.setId(1l);

        Date currDate = new Date();
        String strDateFormat = "HH:mm:ss";
        DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
        String formattedDate = dateFormat.format(currDate);
        savedDriver.setStartTime(formattedDate);

        DayProfit savedDay = new DayProfit();
        savedDay.setDate("2018/06/23");


        when(driverRepository.findById(anyLong())).thenReturn(Optional.ofNullable(savedDriver));
        when(dayProfitRepository.save(any(DayProfit.class))).thenReturn(savedDay);

        DriverDTO savedDTO = driverService.stopParkingMeter(1l);

        assertEquals(driverDTO.getDriverType(), savedDTO.getDriverType());
        assertEquals("/driver/1", savedDTO.getDriverUrl());
        assertEquals(formattedDate, savedDTO.getStartTime());
    }

    @Test
    public void testCheckTicketValid() throws Exception{
        Driver driver = new Driver();
        driver.setTicket_active(true);
        when(driverRepository.findById(anyLong())).thenReturn(Optional.ofNullable(driver));

        assertEquals(driverService.checkTicketValid(1l) , true);
    }

    @Test
    public void testFindDriverById() throws Exception{

        Driver driver = new Driver();
        driver.setId(1l);
        driver.setCurrency(Currency.PLN);
        driver.setDriverType(DRIVER_TYPE);

        when(driverRepository.findById(anyLong())).thenReturn(Optional.ofNullable(driver));

        DriverDTO driverDTO = driverService.findDriverById(1L);

        assertEquals(DRIVER_TYPE, driverDTO.getDriverType());
        assertEquals(Currency.PLN, driverDTO.getCurrency());
    }

    @Test
    public void testCheckCurrency() throws Exception{

        Driver driver = new Driver();
        driver.setCurrency(Currency.PLN);

        when(driverRepository.findById(anyLong())).thenReturn(Optional.ofNullable(driver));

        Currency currency = driverService.checkCurrency(1L);

        assertEquals(Currency.PLN, currency);

    }

    @Test
    public void testCheckAmountToPay() throws Exception{

        Driver driver = new Driver();
        driver.setAmountToPay(1f);

        when(driverRepository.findById(anyLong())).thenReturn(Optional.ofNullable(driver));

        float amountToPay = driverService.checkAmountToPay(1l);

        assertEquals(1f, amountToPay, 0.0);
    }
}