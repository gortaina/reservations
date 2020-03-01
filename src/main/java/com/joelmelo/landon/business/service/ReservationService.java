package com.joelmelo.landon.business.service;

import com.joelmelo.landon.business.domain.RoomReservation;
import com.joelmelo.landon.data.entity.Guest;
import com.joelmelo.landon.data.entity.Reservation;
import com.joelmelo.landon.data.entity.Room;
import com.joelmelo.landon.data.repository.GuestRepository;
import com.joelmelo.landon.data.repository.ReservationRepository;
import com.joelmelo.landon.data.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ReservationService {
    private RoomRepository roomRepository;
    private GuestRepository guestRepository;
    private ReservationRepository reservationRepository;

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    public ReservationService(RoomRepository roomRepository, GuestRepository guestRepository, ReservationRepository reservationRepository) {
        this.roomRepository = roomRepository;
        this.guestRepository = guestRepository;
        this.reservationRepository = reservationRepository;
    }

	/*
	 * Traz a lista de reservas por data, as reservas tem dados do hóspede
	 */
    public List<RoomReservation> getRoomReservationsForDate(String dateString){
        Date date = this.createDateFromDateString(dateString);
        
        Iterable<Room> rooms = this.roomRepository.findAll();
        
        Map<Long, RoomReservation> roomReservationMap = new HashMap<>();
        
        rooms.forEach(room->{
            RoomReservation roomReservation = new RoomReservation();
            roomReservation.setRoomId(room.getId());
            roomReservation.setRoomName(room.getName());
            roomReservation.setRoomNumber(room.getNumber());
            roomReservationMap.put(room.getId(), roomReservation);
        });
        
		//Reservation é uma entidade do banco no repositório, logo precisa da data no formato sql
		//retorna as reservas pela data
		Iterable<Reservation> reservations = this.reservationRepository.findByDate(new java.sql.Date(date.getTime()));
		
		//vincula as reservas dos hóspedes em determinada data ao quarto
        
        if(null!=reservations){
            reservations.forEach(reservation -> {
            	

				//Olhe a versão do Springboot se for a 1.4.3 o guestRepository não terá o método findById
				//O Optimal é uma wrapper class para facilitar a checagem de null em objetos
				//Traz o possível hóspede daquela reserva
				Optional<Guest> guestResponse = this.guestRepository.findById(reservation.getGuestId());            	
            	
				if(guestResponse.isPresent()) {
					
					Guest guest = guestResponse.get();
					
					//linha que vincula as reservas dos hóspedes em determinada data ao quarto
					RoomReservation roomReservation = roomReservationMap.get(reservation.getId());
					
					roomReservation.setDate(date);
					roomReservation.setFirstName(guest.getFirstName());
					roomReservation.setLastName(guest.getLastName());
					roomReservation.setGuestId(guest.getId());
				}
			});
        }
        List<RoomReservation> roomReservations = new ArrayList<>();
        for(Long roomId:roomReservationMap.keySet()){
            roomReservations.add(roomReservationMap.get(roomId));
        }
        return roomReservations;
    }

    
	/*
	 * Traz todos os quartos, reservados ou não
	 */
    public List<RoomReservation> getAllRoomReservations(){
      
        Iterable<Room> rooms = this.roomRepository.findAll();
        
        Map<Long, RoomReservation> roomReservationMap = new HashMap<>();
        
        rooms.forEach(room->{
            RoomReservation roomReservation = new RoomReservation();
            roomReservation.setRoomId(room.getId());
            roomReservation.setRoomName(room.getName());
            roomReservation.setRoomNumber(room.getNumber());
            roomReservationMap.put(room.getId(), roomReservation);
        });
        
		//Reservation é uma entidade do banco no repositório, 
		//retorna todos os quartos, tendo reserva ou não
		Iterable<Reservation> reservations = this.reservationRepository.findAll();
		
		//vincula as reservas dos hóspedes em determinada data ao quarto
        
        if(null!=reservations){
            reservations.forEach(reservation -> {
            	

				//Olhe a versão do Springboot se for a 1.4.3 o guestRepository não terá o método findById
				//O Optimal é uma wrapper class para facilitar a checagem de null em objetos
				//Traz o possível hóspede daquela reserva
				Optional<Guest> guestResponse = this.guestRepository.findById(reservation.getGuestId());            	
            	
				if(guestResponse.isPresent()) {
					
					Guest guest = guestResponse.get();
					
					//linha que vincula as reservas dos hóspedes em determinada data ao quarto
					RoomReservation roomReservation = roomReservationMap.get(reservation.getId());
					
					roomReservation.setDate(reservation.getDate());
					roomReservation.setFirstName(guest.getFirstName());
					roomReservation.setLastName(guest.getLastName());
					roomReservation.setGuestId(guest.getId());
				}
			});
        }
        List<RoomReservation> roomReservations = new ArrayList<>();
        for(Long roomId:roomReservationMap.keySet()){
            roomReservations.add(roomReservationMap.get(roomId));
        }
        return roomReservations;
    }
    
    
    private Date createDateFromDateString(String dateString){
        Date date = null;
        if(null!=dateString) {
            try {
                date = DATE_FORMAT.parse(dateString);
            }catch(ParseException pe){
                date = new Date();
            }
        }else{
            date = new Date();
        }
        return date;
    }
}
