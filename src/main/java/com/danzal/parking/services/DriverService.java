package com.danzal.parking.services;

import com.danzal.parking.domain.Currency;
import com.danzal.parking.models.DriverDTO;

public interface DriverService {

    DriverDTO startParkingMeter(DriverDTO driverDTO);

    DriverDTO stopParkingMeter(Long id);

    boolean checkTicketValid(Long id);

    DriverDTO findDriverById(Long id);

    Currency checkCurrency(Long id);

    float checkAmountToPay(Long id);


}
