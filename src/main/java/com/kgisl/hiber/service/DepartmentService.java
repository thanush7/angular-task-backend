package com.kgisl.hiber.service;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kgisl.hiber.pojo.Department;
import com.kgisl.hiber.pojo.Employee;
import com.kgisl.hiber.repository.DepartmentRepository;
import com.kgisl.hiber.repository.EmployeeRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;
    
    @PersistenceContext
    private EntityManager entityManager;
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private EmployeeRepository employeeRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    public void saveDepartmentsFromJson(MultipartFile file) throws IOException {
        List<Department> departments = objectMapper.readValue(file.getInputStream(), new TypeReference<>() {});
        departmentRepository.saveAll(departments);
    }
    
    
    @Transactional
    public void saveDepartmentWithEmployees(Department department) {
//        for (Employee employee : department.getEmployees()) {
//            employee.setDepartment(department);
//        }
        // Save department and employees in a single transaction
        departmentRepository.save(department);
    }

    public Employee getEmployee(String id) {
        return employeeRepository.findById(id).orElse(null);
    }

    //public Page<Employee> getEmployeesByDepartmentId(String departmentId, Pageable pageable, String searchTerm) {
//    	Department department=departmentRepository.findById(departmentId).orElseThrow();
//    	List<Employee> employees=department.getEmployees();
//    	if(searchTerm!=null && !searchTerm.isEmpty()) {
//    		employees=employees.stream()
//    				.filter(employee->employee.getId().contains(searchTerm)||
//    						          employee.getName().contains(searchTerm)||
//    						          employee.getEmail().contains(searchTerm))
//    				.collect(Collectors.toList());
//    	}
//    	int pageSize=pageable.getPageSize();
//    	int currentPage=pageable.getPageNumber();
//    	int startItem=currentPage*pageSize;
//    	List<Employee> paginatedEmployees;
//    	
//    	if(employees.size()<startItem) {
//    		paginatedEmployees=List.of();
//    	}
//    	else {
//    		int toIndex=Math.min(startItem+pageSize, employees.size());
//    		paginatedEmployees=employees.subList(startItem,toIndex);
//    	}
//    	return new PageImpl<>(paginatedEmployees,PageRequest.of(currentPage, pageSize),employees.size());
//    	String sql = "SELECT * FROM employee WHERE departmentid = ?";
//    	String countSql = "SELECT COUNT(*) FROM employee WHERE departmentid = ?";

    	// Create a list to hold the parameters for the SQL query
//    	List<Object> params = new ArrayList<>();
//    	params.add(departmentId); // Add departmentId as the first parameter

    	// Add search term conditions if a search term is provided
//    	if (searchTerm != null && !searchTerm.isEmpty()) {
//    	    sql += " AND (id LIKE ? OR name LIKE ? OR email LIKE ?)";
//    	    countSql += " AND (id LIKE ? OR name LIKE ? OR email LIKE ?)";
//    	    String wildcardSearchTerm = "%" + searchTerm + "%"; // Adding wildcards for LIKE
//    	    params.add(wildcardSearchTerm);
//    	    params.add(wildcardSearchTerm);
//    	    params.add(wildcardSearchTerm);
//    	}

    	// Calculate the pagination parameters
//    	int pageSize = pageable.getPageSize();
//    	int currentPage = pageable.getPageNumber();
//    	int startItem = currentPage * pageSize;

    	// Retrieve the total number of employees matching the criteria
    	//Integer totalEmployees = jdbcTemplate.queryForObject(countSql, Integer.class, params.toArray());

    	// Retrieve the paginated employees
//    	List<Employee> paginatedEmployees = jdbcTemplate.query(
//    	        sql,
//    	        params.toArray(),
//    	        new EmployeeRowMapper(),
//    	        new RowMapperResultSetExtractor<>(new EmployeeRowMapper(), startItem, pageSize)
//    	);

    	// Return the paginated result
    	//return new PageImpl<>(paginatedEmployees, PageRequest.of(currentPage, pageSize), totalEmployees);

    	 
    //}
    
    public Page<Employee> getEmployeesByDepartmentId(String departmentId, Pageable pageable, String searchTerm) {
        String sql = "SELECT * FROM employee WHERE departmentid = ?";
        String countSql = "SELECT COUNT(*) FROM employee WHERE departmentid = ?";

        // Create a list to hold the parameters for the SQL query
        List<Object> params = new ArrayList<>();
        params.add(departmentId); // Add departmentId as the first parameter

        // Add search term conditions if a search term is provided
        if (searchTerm != null && !searchTerm.isEmpty()) {
            sql += " AND (id LIKE ? OR name LIKE ? OR email LIKE ?)";
            countSql += " AND (id LIKE ? OR name LIKE ? OR email LIKE ?)";
            String wildcardSearchTerm = "%" + searchTerm + "%"; // Adding wildcards for LIKE
            params.add(wildcardSearchTerm);
            params.add(wildcardSearchTerm);
            params.add(wildcardSearchTerm);
        }

        // Calculate the pagination parameters
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;

        // Retrieve the total number of employees matching the criteria
        Integer totalEmployees = jdbcTemplate.queryForObject(countSql, Integer.class, params.toArray());

        // Retrieve the paginated employees
        @SuppressWarnings("deprecation")
		List<Employee> paginatedEmployees = jdbcTemplate.query(
                sql,
                params.toArray(),
                new EmployeeRowMapper()
        );

        // Handle pagination logic
        if (paginatedEmployees.size() < startItem) {
            paginatedEmployees = List.of(); // No results for this page
        } else {
            int toIndex = Math.min(startItem + pageSize, paginatedEmployees.size());
            paginatedEmployees = paginatedEmployees.subList(startItem, toIndex);
        }

        // Return the paginated result
        return new PageImpl<>(paginatedEmployees, PageRequest.of(currentPage, pageSize), totalEmployees);
    }

    private static class EmployeeRowMapper implements RowMapper<Employee> {
        @Override
        public Employee mapRow(ResultSet rs, int rowNum) throws SQLException {
            Employee employee = new Employee();
            employee.setId(rs.getString("id"));
            employee.setName(rs.getString("name"));
            employee.setEmail(rs.getString("email"));
            employee.setGender(rs.getString("gender"));
            employee.setCity(rs.getString("city"));
            // Set other fields as needed
            return employee;
        }
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
//        if (searchTerm == null || searchTerm.isEmpty()) {
//            return departmentRepository.findAll(pageable);
//        }
        
        // Modify this method to perform a case-insensitive search by name
//        return departmentRepository.findByNameContainingIgnoreCase(searchTerm, pageable);
    	CriteriaBuilder cb=entityManager.getCriteriaBuilder();
    	CriteriaQuery<Department> cq=cb.createQuery(Department.class);
    	Root<Department> departmentRoot=cq.from(Department.class);
    	if(searchTerm==null||searchTerm.isEmpty()) {
    		cq.select(departmentRoot);
    	}
    	else {
    		Predicate predicate=cb.like(cb.lower(departmentRoot.get("name")), "%"+searchTerm.toLowerCase());
    		cq.where(predicate);
    	}
    	
    	List<Department> resultList=entityManager.createQuery(cq)
    			.setFirstResult((int)pageable.getOffset())
    			.setMaxResults(pageable.getPageSize())
    			.getResultList();
    	CriteriaQuery<Long> countQuery=cb.createQuery(Long.class);
    	countQuery.select(cb.count(countQuery.from(Department.class)));
    	Long total=entityManager.createQuery(countQuery).getSingleResult();
    	return new PageImpl<>(resultList,pageable,total);
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
