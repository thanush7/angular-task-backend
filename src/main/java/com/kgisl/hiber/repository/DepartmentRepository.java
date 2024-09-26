package com.kgisl.hiber.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kgisl.hiber.pojo.Department;

public interface DepartmentRepository extends JpaRepository<Department, String> {
}
