package com.kgisl.hiber.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kgisl.hiber.pojo.Department;
import com.kgisl.hiber.pojo.Employee;
import com.kgisl.hiber.repository.DepartmentRepository;
import com.kgisl.hiber.repository.EmployeeRepository;

import jakarta.transaction.Transactional;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Transactional
    public ResponseEntity<Map<String, String>> createDepartmentWithEmployees(Department department) {
        for (Employee employee : department.getEmployees()) {
            employee.setDepartment(department);
        }

        departmentRepository.save(department);
        return ResponseEntity.ok().body(Collections.singletonMap("message", "Department and employees created successfully"));
    }

    public Employee getEmployee(String id) {
        return employeeRepository.findById(id).orElse(null);
    }

    public List<Employee> getEmployeesByDepartmentId(String departmentId) {
        return employeeRepository.findByDepartmentId(departmentId);
    }

    @SuppressWarnings("rawtypes")
    public ResponseEntity deleteEmployee(String id) {
        employeeRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    public List<Department> getDepartments() {
        return departmentRepository.findAll();
    }

    public Employee updateEmployee(String id, Employee emp) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new RuntimeException("Employee not found"));
        employee.setName(emp.getName());
        employee.setEmail(emp.getEmail());
        employee.setMobile(emp.getMobile());
        employee.setGender(emp.getGender());
        employee.setCity(emp.getCity());
        if (emp.getDepartment() != null && emp.getDepartment().getId() != null) {
            Department department = departmentRepository.findById(emp.getDepartment().getId())
                    .orElseThrow(() -> new RuntimeException("Department not found"));
            employee.setDepartment(department);
        }
        return employeeRepository.save(employee);
    }
    

    public Department updateDepartment(String id, Department dep) {
        Department department = departmentRepository.findById(id).orElseThrow();
        department.setName(dep.getName());
        return departmentRepository.save(department);
    }

    public Employee createByDepartmentId(String id, Employee emp) {
        Department department=departmentRepository.findById(id).orElseThrow();
        emp.setDepartment(department);
        return employeeRepository.save(emp);
    }

    public ResponseEntity<Department> createDepartment(Department department){
      Department dept = departmentRepository.save(department);
        return new ResponseEntity<>(dept,HttpStatus.CREATED);
    }

    @SuppressWarnings("rawtypes")
    public ResponseEntity deleteDepartment(String id) {
        departmentRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    public List<Department> getAllDepartmentsWithEmployees() {
        return departmentRepository.findAll(); 
    }

    public Optional<Department> getDepartmentById(String id){
        return departmentRepository.findById(id);
    }
    public Department updateDepartmentAndEmployee(String id, Department departmentDetails) {
        Optional<Department> departmentOptional = departmentRepository.findById(id);
        if (departmentOptional.isPresent()) {
            Department existingDepartment = departmentOptional.get();
            existingDepartment.setName(departmentDetails.getName());
            existingDepartment.setEmployees(departmentDetails.getEmployees());
            return departmentRepository.save(existingDepartment);
        } else {
            throw new RuntimeException("Department not found with id " + id);
        }
    }
    public String getDepartmentName(String id){
        Optional<Department> department=departmentRepository.findById(id);
        return department.get().getName();
        
    }
}
