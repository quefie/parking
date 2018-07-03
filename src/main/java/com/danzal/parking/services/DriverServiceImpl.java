package com.danzal.parking.services;

import com.danzal.parking.controllers.DriverController;
import com.danzal.parking.domain.Currency;
import com.danzal.parking.domain.Driver;
import com.danzal.parking.domain.DriverType;
import com.danzal.parking.mappers.DriverMapper;
import com.danzal.parking.models.DriverDTO;
import com.danzal.parking.repositories.DriverRepository;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class DriverServiceImpl implements DriverService {


    private final DriverMapper driverMapper;
    private final DayProfitService dayProfitService;
    private final DriverRepository driverRepository;

    public DriverServiceImpl(DriverMapper driverMapper, DayProfitService dayProfitService, DriverRepository driverRepository) {
        this.driverMapper = driverMapper;
        this.dayProfitService = dayProfitService;
        this.driverRepository = driverRepository;
    }

    // ============= START PARKING METHODS ===============
    @Override
    public DriverDTO startParkingMeter(DriverDTO driverDTO) {

        DriverDTO newDriverDTO = driverDTO;
        newDriverDTO.setTicket_active(true);

        Date currDate = new Date();
        String timeDateFormat = "HH:mm:ss";
        DateFormat timeFormat = new SimpleDateFormat(timeDateFormat);
        String formattedTime = timeFormat.format(currDate);
        newDriverDTO.setStartTime(formattedTime);

        String dayDateFormat = "yyyy/MM/dd";
        DateFormat dateFormat = new SimpleDateFormat(dayDateFormat);
        String formattedDate = dateFormat.format(currDate);
        newDriverDTO.setTransactionDay(formattedDate);

        return createNewDriver(newDriverDTO);

    }


    private DriverDTO createNewDriver(DriverDTO driverDTO) {
        return saveAndReturnDTO(driverMapper.driverDTOToDriver(driverDTO));
    }

    private DriverDTO saveAndReturnDTO(Driver driver) {
        Driver savedDriver = driverRepository.save(driver);
        DriverDTO returnedDTO = driverMapper.driverToDriverDTO(savedDriver);
        returnedDTO.setDriverUrl(getDriverUrl(savedDriver.getId()));

        return returnedDTO;
    }

    // ============= CHECK TICKET VALID METHOD ===============
    @Override
    public boolean checkTicketValid(Long id) {

        DriverDTO driverDTO = findDriverById(id);
        if (driverDTO.isTicket_active()) {
            return true;
        }

        return false;
    }

    // ============= STOP PARKING METER METHODS ===============
    @Override
    public DriverDTO stopParkingMeter(Long id) {

        DriverDTO driverDTO = findDriverById(id);

        driverDTO.setTicket_active(false);

        Date currDate = new Date();
        String strDateFormat = "HH:mm:ss";
        DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
        String formattedDate = dateFormat.format(currDate);


        DriverDTO returnedDriverDTO = driverRepository.findById(id).map(driver -> {
            driver.setStopTime(formattedDate);
            driver.setTicket_active(false);
            calculateAmountToPay(id);
            DriverDTO returnDTO = driverMapper.driverToDriverDTO(driverRepository.save(driver));
            return returnDTO;
        }).orElseThrow(ResourceNotFoundException::new);

        returnedDriverDTO.setDriverUrl(getDriverUrl(id));

        dayProfitService.saveOrUpdateDayProfit(driverDTO.getTransactionDay());

        return returnedDriverDTO;
    }

    private void calculateAmountToPay(Long id) {
        DriverDTO driverDTO = findDriverById(id);
        long hours = 0;

        float amountToPay = 0.0f;
        hours = calcHours(id);


        if (driverDTO.getDriverType() == DriverType.VIP) {
            if (hours > 2) {
                amountToPay = (float) (2 + 1.2 * 2 * (hours - 2));

            } else if (hours > 1 && hours < 2) {
                amountToPay = 2.0f;
            }
        } else {
            if (hours > 2) {
                amountToPay = (float) (1 + 2 + 1.5 * 2 * (hours - 2));
            } else if (hours > 1 && hours < 2) {
                amountToPay = 3.0f;
            } else {
                amountToPay = 1.0f;
            }
        }

        float finalAmountToPay = amountToPay;
        driverRepository.findById(id).map(driver -> {
            driver.setAmountToPay(finalAmountToPay);
            DriverDTO returnDTO = driverMapper.driverToDriverDTO(driverRepository.save(driver));
            return driverDTO;
        }).orElseThrow(ResourceNotFoundException::new);

    }

    private long calcHours(Long id) {
        DriverDTO driverDTO = findDriverById(id);


        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");

        try {
            Date date1 = format.parse(driverDTO.getStartTime());
            Date date2 = format.parse(driverDTO.getStopTime());
            long diff = date2.getTime() - date1.getTime();

            long diffTime = diff / (60 * 60 * 1000) % 24;
            return diffTime;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    // ============= CHECK PARKING COST METHOD ===============
    @Override
    public float checkAmountToPay(Long id) {
        DriverDTO driverDTO = findDriverById(id);
        return driverDTO.getAmountToPay();
    }

    // ============= CHECK CURRENCY METHOD ===============
    @Override
    public Currency checkCurrency(Long id) {
        return findDriverById(id).getCurrency();
    }

    // ============= GET DRIVER METHOD ===============
    @Override
    public DriverDTO findDriverById(Long id) {

        return driverRepository.findById(id)
                .map(driverMapper::driverToDriverDTO)
                .map(driverDTOreturn -> {
                    driverDTOreturn.setDriverUrl(getDriverUrl(id));
                    return driverDTOreturn;
                })
                .orElseThrow(ResourceNotFoundException::new);

    }


    private String getDriverUrl(Long id) {
        return DriverController.BASE_URL + "/" + id;
    }
}
