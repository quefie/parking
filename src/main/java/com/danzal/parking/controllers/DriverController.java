package com.danzal.parking.controllers;

import com.danzal.parking.domain.Currency;
import com.danzal.parking.models.DriverDTO;
import com.danzal.parking.services.DriverService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(DriverController.BASE_URL)
public class DriverController {

    public static final String BASE_URL = "/driver";

    private final DriverService driverService;

    public DriverController(DriverService driverService) {
        this.driverService = driverService;
    }

    @PostMapping("/start")
    @ResponseStatus(HttpStatus.CREATED)
    public DriverDTO startParingkMeter(@RequestBody DriverDTO driverDTO){
        return  driverService.startParkingMeter(driverDTO);

    }


    @GetMapping({"/{id}/check"})
    @ResponseStatus(HttpStatus.OK)
    public boolean checkTicketValid(@PathVariable Long id){
       return driverService.checkTicketValid(id);
    }


    @PutMapping({"/{id}/stop"})
    @ResponseStatus(HttpStatus.OK)
    public DriverDTO stopParkingMeter(@PathVariable Long id){
        return  driverService.stopParkingMeter(id);
    }

    @GetMapping({"/{id}/cost"})
    @ResponseStatus(HttpStatus.OK)
    public float checkAmoutToPay(@PathVariable Long id){
        return driverService.checkAmountToPay(id);
    }

    @GetMapping("/{id}/currency")
    @ResponseStatus(HttpStatus.OK)
    public Currency getDriverCurrency(@PathVariable Long id){
        return driverService.checkCurrency(id);
    }

    @GetMapping("/{id}/show")
    @ResponseStatus(HttpStatus.OK)
    public DriverDTO getDriverInfo(@PathVariable Long id){
        return driverService.findDriverById(id);
    }

}
