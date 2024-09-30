package com.kgisl.hiber.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.kgisl.hiber.pojo.Department;

public interface DepartmentRepository extends JpaRepository<Department, String> {
	Page<Department> findByNameContainingIgnoreCase(String name, Pageable pageable);

}
