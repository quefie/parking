package com.danzal.parking.mappers;

import com.danzal.parking.domain.Currency;
import com.danzal.parking.domain.Driver;
import com.danzal.parking.domain.DriverType;
import com.danzal.parking.models.DriverDTO;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DriverMapperTest {

    public static final Currency CURRENCY = Currency.PLN;
    public static final DriverType DRIVER_TYPE = DriverType.VIP;
    DriverMapper driverMapper = DriverMapper.INSTANCE;

    @Test
    public void testDriverToDriverDTO() throws Exception {
        Driver driver = new Driver();
        driver.setCurrency(CURRENCY);
        driver.setDriverType(DRIVER_TYPE);

        DriverDTO driverDTO = driverMapper.driverToDriverDTO(driver);

        assertEquals(CURRENCY, driverDTO.getCurrency());
        assertEquals(DRIVER_TYPE, driverDTO.getDriverType());

    }

    @Test
    public void testDriverDTOToDriver() throws Exception {
        DriverDTO driverDTO = new DriverDTO();
        driverDTO.setCurrency(CURRENCY);
        driverDTO.setDriverType(DRIVER_TYPE);

        Driver driver = driverMapper.driverDTOToDriver(driverDTO);

        assertEquals(CURRENCY, driver.getCurrency());
        assertEquals(DRIVER_TYPE, driver.getDriverType());

    }
}
