package com.kgisl.hiber.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.kgisl.hiber.pojo.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, String> {
//    Page<Employee> findByDepartmentId(String departmentId,Pageable pageable);
//    Page<Employee> findByDepartmentIdAndIdContainingOrNameContainingOrEmailContaining(String departmentId, String id,String name, 
//            String email, Pageable pageable);
}    
