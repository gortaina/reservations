package com.joelmelo.landon.data.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.joelmelo.landon.data.entity.Guest;
import org.springframework.stereotype.Repository;

@Repository
public interface GuestRepository extends PagingAndSortingRepository<Guest, Long> {

}