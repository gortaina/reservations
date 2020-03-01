package com.joelmelo.landon.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.joelmelo.landon.business.domain.RoomReservation;
import com.joelmelo.landon.business.service.ReservationService;

@RestController
@RequestMapping(value="/api")
public class ReservationServiceController {
	
	@Autowired
	private ReservationService reservationService;
	
	@RequestMapping(method = RequestMethod.GET, value="/reservations/{date}")
	public List<RoomReservation> getAllReservationsForDate(@PathVariable(value="date")String dateString){
		return this.reservationService.getRoomReservationsForDate(dateString);
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/allreservations")
	public List<RoomReservation> getAllRoomReservation(){
		return this.reservationService.getAllRoomReservations();
	}

}
