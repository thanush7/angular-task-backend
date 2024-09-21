package com.kgisl.hiber.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
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
@CrossOrigin(origins="http://localhost:4200")
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
    public Employee createByDepartmentId(@PathVariable Long id,@RequestBody Employee employee){
        return departmentService.createByDepartmentId(id,employee);
    }

    @PostMapping("/create/department")
    public Department createDepartment(@RequestBody Department department){
        return departmentService.createDepartment(department);
    }

    @DeleteMapping("/department/{id}")
    public ResponseEntity deleteDepartment(@PathVariable Long id){
        return departmentService.deleteDepartment(id);
    }
}
