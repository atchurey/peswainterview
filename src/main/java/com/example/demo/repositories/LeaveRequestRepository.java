package com.example.demo.repositories;

import com.example.demo.entities.LeaveRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeaveRequestRepository extends MongoRepository<LeaveRequest, String> {
}