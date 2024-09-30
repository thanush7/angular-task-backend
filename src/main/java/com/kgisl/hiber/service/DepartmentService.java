package com.kgisl.hiber.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

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

//    @Transactional
//    public ResponseEntity<Map<String, String>> createDepartmentWithEmployees(Department department) {
//        for (Employee employee : department.getEmployees()) {
//            employee.setDepartment(department);
//        }
//
//        departmentRepository.save(department);
//        return ResponseEntity.ok().body(Collections.singletonMap("message", "Department and employees created successfully"));
//    }

    public Employee getEmployee(String id) {
        return employeeRepository.findById(id).orElse(null);
    }

    public Page<Employee> getEmployeesByDepartmentId(String departmentId, Pageable pageable, String searchTerm) {
//        if (searchTerm != null && !searchTerm.isEmpty()) {
//            return employeeRepository.findByDepartmentIdAndIdContainingOrNameContainingOrEmailContaining(
//                    departmentId, searchTerm, searchTerm, searchTerm, pageable
//                );
//        } else {
//            return employeeRepository.findByDepartment_Id(departmentId, pageable);
//        }
    	Department department=departmentRepository.findById(departmentId).orElseThrow();
    	List<Employee> employees=department.getEmployees();
    	if(searchTerm!=null && !searchTerm.isEmpty()) {
    		employees=employees.stream()
    				.filter(employee->employee.getId().contains(searchTerm)||
    						          employee.getName().contains(searchTerm)||
    						          employee.getEmail().contains(searchTerm))
    				.collect(Collectors.toList());
    	}
    	int pageSize=pageable.getPageSize();
    	int currentPage=pageable.getPageNumber();
    	int startItem=currentPage*pageSize;
    	List<Employee> paginatedEmployees;
    	
    	if(employees.size()<startItem) {
    		paginatedEmployees=List.of();
    	}
    	else {
    		int toIndex=Math.min(startItem+pageSize, employees.size());
    		paginatedEmployees=employees.subList(startItem,toIndex);
    	}
    	return new PageImpl<>(paginatedEmployees,PageRequest.of(currentPage, pageSize),employees.size());
    }


    @SuppressWarnings("rawtypes")
    public ResponseEntity deleteEmployee(String id) {
        employeeRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    public Page<Department> getDepartments(Pageable pageble) {
        return departmentRepository.findAll(pageble);
    }
    public Page<Department> searchDepartmentsByName(String searchTerm, Pageable pageable) {
        // If searchTerm is null or empty, return all departments
        if (searchTerm == null || searchTerm.isEmpty()) {
            return departmentRepository.findAll(pageable);
        }
        
        // Modify this method to perform a case-insensitive search by name
        return departmentRepository.findByNameContainingIgnoreCase(searchTerm, pageable);
    }


    public Employee updateEmployee(String id, Employee emp) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new RuntimeException("Employee not found"));
        employee.setName(emp.getName());
        employee.setEmail(emp.getEmail());
        employee.setMobile(emp.getMobile());
        employee.setGender(emp.getGender());
        employee.setCity(emp.getCity());
//        if (emp.getDepartment() != null && emp.getDepartment().getId() != null) {
//            Department department = departmentRepository.findById(emp.getDepartment().getId())
//                    .orElseThrow(() -> new RuntimeException("Department not found"));
//            employee.setDepartment(department);
//        }
        return employeeRepository.save(employee);
    }
    

    public Department updateDepartment(String id, Department dep) {
        Department department = departmentRepository.findById(id).orElseThrow();
        department.setName(dep.getName());
        return departmentRepository.save(department);
    }

    public Employee createByDepartmentId(String id, Employee emp) {
       Optional<Department> deparmentPt=departmentRepository.findById(id);
       if(deparmentPt.isPresent()) {
    	   Department department=deparmentPt.get();
    	   department.getEmployees().add(emp);
    	   departmentRepository.save(department);
    	   return emp;
       }
       return null;
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
