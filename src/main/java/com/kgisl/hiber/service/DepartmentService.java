package com.kgisl.hiber.service;

import java.util.List;

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
    public void createDepartmentWithEmployees(Department department) {
        for (Employee employee : department.getEmployees()) {
            employee.setDepartment(department);
        }

        departmentRepository.save(department);
    }

    public Employee getEmployee(Long id) {
        return employeeRepository.findById(id).orElse(null);
    }

    public List<Employee> getEmployeesByDepartmentId(Long departmentId) {
        return employeeRepository.findByDepartmentId(departmentId);
    }

    @SuppressWarnings("rawtypes")
    public ResponseEntity deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    public List<Department> getDepartments() {
        return departmentRepository.findAll();
    }

    public Employee updateEmployee(Long id, Employee emp) {
        Employee employee = employeeRepository.findById(id).orElseThrow();
        employee.setName(emp.getName());
        employee.setEmail(emp.getEmail());
        employee.setMobile(emp.getMobile());
        employee.setDepartment(emp.getDepartment());
        return employeeRepository.save(employee);
    }

    public Department updateDepartment(Long id, Department dep) {
        Department department = departmentRepository.findById(id).orElseThrow();
        department.setName(dep.getName());
        return departmentRepository.save(department);
    }

    public Employee createByDepartmentId(Long id, Employee emp) {
        Department department=departmentRepository.findById(id).orElseThrow();
        emp.setDepartment(department);
        return employeeRepository.save(emp);
    }

    public Department createDepartment(Department department){
        return departmentRepository.save(department);
    }

    public ResponseEntity deleteDepartment(Long id) {
        departmentRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
