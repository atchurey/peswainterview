package com.example.demo.repositories;

import com.example.demo.entities.Employee;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends MongoRepository<Employee, String> {

    List<Employee> findByFirstName(String firstName);
    List<Employee> findByLastName(String lastName);
}