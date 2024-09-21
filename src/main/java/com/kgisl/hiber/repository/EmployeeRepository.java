package com.kgisl.hiber.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kgisl.hiber.pojo.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findByDepartmentId(Long departmentId);
}    
