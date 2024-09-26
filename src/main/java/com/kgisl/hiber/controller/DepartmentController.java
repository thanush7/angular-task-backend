package com.kgisl.hiber.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kgisl.hiber.pojo.Department;
import com.kgisl.hiber.pojo.Employee;
import com.kgisl.hiber.service.DepartmentService;

@RestController
@RequestMapping("/departments")
@CrossOrigin(origins = "http://localhost:4200")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @PostMapping
    public String createDepartmentWithEmployees(@RequestBody Department department) {
        departmentService.createDepartmentWithEmployees(department);
        return "Department and employees created successfully";
    }

    @GetMapping("/employees/{id}")
    public Employee getEmployee(@PathVariable Long id) {
        return departmentService.getEmployee(id);
    }

    @GetMapping("/{departmentId}")
    public List<Employee> getEmployeesByDepartmentId(@PathVariable Long departmentId) {
        return departmentService.getEmployeesByDepartmentId(departmentId);
    }

    @SuppressWarnings("rawtypes")
    @DeleteMapping("/{id}")
    public ResponseEntity deleteEmployee(@PathVariable Long id) {
        return departmentService.deleteEmployee(id);
    }

    @GetMapping("/allDepartment")
    public List<Department> getDepartments() {
        return departmentService.getDepartments();
    }

    @PutMapping("/{id}")
    public Employee updateEmployee(@PathVariable Long id, @RequestBody Employee employee) {
        return departmentService.updateEmployee(id, employee);
    }

    @PutMapping("deparmart/{id}")
    public Department updateDepartment(@PathVariable Long id, @RequestBody Department department) {
        return departmentService.updateDepartment(id, department);
    }

    @PostMapping("/create/{id}")
    public Employee createByDepartmentId(@PathVariable Long id, @RequestBody Employee employee) {
        return departmentService.createByDepartmentId(id, employee);
    }

    @PostMapping("/create/department")
    public ResponseEntity<Department> createDepartment(@RequestBody Department department) {
        return departmentService.createDepartment(department);
    }

    @SuppressWarnings("rawtypes")
    @DeleteMapping("/department/{id}")
    public ResponseEntity deleteDepartment(@PathVariable Long id) {
        return departmentService.deleteDepartment(id);
    }

    @GetMapping("/whole")
    public ResponseEntity<List<Department>> getAllDepartmentsWithEmployees() {
        List<Department> departments = departmentService.getAllDepartmentsWithEmployees();
        if (departments != null && !departments.isEmpty()) {
            return ResponseEntity.ok(departments);
        } else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
    }

    @GetMapping("/departmentId/{id}")
    public Optional<Department> getAllDeparmentwithEmployeeById(@PathVariable Long id) {
        Optional<Department> departments = departmentService.getDepartmentById(id);
        return departments;
    }

    @PutMapping("/departmentId/{id}")
    public ResponseEntity<Department> updateDepartmentAndEmployee(@PathVariable Long id, @RequestBody Department departmentDetails) {
        Department updatedDepartment = departmentService.updateDepartmentAndEmployee(id, departmentDetails);
        return ResponseEntity.ok(updatedDepartment);
    }

    @GetMapping("/dep/{id}")
    public ResponseEntity<Map<String, String>> getDepartmentById(@PathVariable Long id) {
        Optional<Department> department = departmentService.getDepartmentById(id);
        if (department.isPresent()) {
            Map<String, String> response = new HashMap<>();
            response.put("name", department.get().getName());
            return ResponseEntity.ok(response);  // Returning JSON object { "name": "Department Name" }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();  // Return 404 if department not found
        }
    }
}
