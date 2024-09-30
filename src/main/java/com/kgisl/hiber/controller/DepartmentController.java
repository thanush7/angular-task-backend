package com.kgisl.hiber.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kgisl.hiber.pojo.Department;
import com.kgisl.hiber.pojo.Employee;
import com.kgisl.hiber.service.DepartmentService;

@RestController
@RequestMapping("/departments")
@CrossOrigin(origins = "http://localhost:4200")
public class DepartmentController {

//    @Autowired
//    private DepartmentService departmentService;
	
	private final DepartmentService departmentService;
    private final ObjectMapper objectMapper;  // Jackson object mapper for parsing JSON

    public DepartmentController(DepartmentService departmentService, ObjectMapper objectMapper) {
        this.departmentService = departmentService;
        this.objectMapper = objectMapper;
    }
    
    @PostMapping("/upload-json")
    public ResponseEntity<Map<String, String>> uploadJsonFile(@RequestParam("file") MultipartFile file) {
        try {
            departmentService.saveDepartmentsFromJson(file);
            return ResponseEntity.ok(Map.of("message", "Departments uploaded successfully!"));
        } catch (IOException e) {
            return ResponseEntity.status(500).body(Map.of("error", "Error processing file: " + e.getMessage()));
        }
    }

//    @PostMapping
//    public String createDepartmentWithEmployees(@RequestBody Department department) {
//        departmentService.createDepartmentWithEmployees(department);
//        return "Department and employees created successfully";
//    }

    @PostMapping("/employees")
    public Employee getEmployee(@RequestBody String id) {
        return departmentService.getEmployee(id);
    }

//    @GetMapping("/{departmentId}")
//    public Page<Employee> getEmployeesByDepartmentId(@PathVariable String departmentId,
//    		@RequestParam(defaultValue="0")int page,
//    		@RequestParam(defaultValue="4")int size) {
//    	PageRequest pageable=PageRequest.of(page, size);
//        return departmentService.getEmployeesByDepartmentId(departmentId,pageable);
//    }
    
    //change from getmapping to postmapping 
    @PostMapping("/employee-by-department")
    public Page<Employee> getEmployeeByDepartmentId(
        @RequestBody DepartmentEmployeeRequest request,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "4") int size,
        @RequestParam(required = false) String searchTerm) {
        
        PageRequest pageable = PageRequest.of(page, size, Sort.by("id").ascending());

        // Assuming your service handles the search term and returns filtered results
        return departmentService.getEmployeesByDepartmentId(request.getDepartmentId(),pageable, searchTerm);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteEmployee(@PathVariable String id) {
        return departmentService.deleteEmployee(id);
    }
    @PostMapping("/allDepartment")
    public Page<Department> getDepartmentsWithSearch(@RequestBody Map<String, Object> params) {
        int page = (int) params.get("page");
        int size = (int) params.get("size");
        String sortBy = (String) params.get("sortBy");
        String sortDirection = (String) params.get("direction"); // Renamed for clarity
        String searchTerm = (String) params.get("searchTerm"); // Search term

        // Set default direction if not provided or if null
        Sort.Direction direction = (sortDirection == null || sortDirection.isEmpty()) 
                ? Sort.Direction.ASC 
                : Sort.Direction.fromString(sortDirection.toUpperCase()); // Ensure case is handled

        // Create Pageable object
        Pageable pageable = PageRequest.of(page, size, direction, sortBy);

        // Search by the provided term (only by name if email is removed)
        return departmentService.searchDepartmentsByName(searchTerm, pageable);
    }




//    
//    @PostMapping("/allDepartment")
//    public Page<Department> getDeparments(@RequestBody(required = false) Map<String, String> request){
//    	 int page = (request != null && request.containsKey("page")) ? Integer.parseInt(request.get("page")) : 0;
//    	    int size = (request != null && request.containsKey("size")) ? Integer.parseInt(request.get("size")) : 10; // Default size to 10
//
//    	    PageRequest pageable = PageRequest.of(page, size);
//    	return departmentService.getDepartments(pageable);
//    }
    

    @PutMapping("/{id}")
    public Employee updateEmployee(@PathVariable String id, @RequestBody Employee employee) {
        return departmentService.updateEmployee(id, employee);
    }

    @PutMapping("deparmart/{id}")
    public Department updateDepartment(@PathVariable String id, @RequestBody Department department) {
        return departmentService.updateDepartment(id, department);
    }

    @PostMapping("/create/{id}")
    public ResponseEntity<Employee> createByDepartmentId(@PathVariable String id, @RequestBody Employee employee) {
    	try {
    		Employee createdEmployee=departmentService.createByDepartmentId(id, employee);
    		return ResponseEntity.ok(createdEmployee);
    	}
        catch(RuntimeException e) {
        	return ResponseEntity.notFound().build();
        }
    }
//
    @PostMapping("/create/department")
    public ResponseEntity<Department> createDepartment(@RequestBody Department department) {
        return departmentService.createDepartment(department);
    }

    @SuppressWarnings("rawtypes")
    @DeleteMapping("/department/{id}")
    public ResponseEntity deleteDepartment(@PathVariable String id) {
        return departmentService.deleteDepartment(id);
    }
//
//    @GetMapping("/whole")
//    public ResponseEntity<List<Department>> getAllDepartmentsWithEmployees() {
//        List<Department> departments = departmentService.getAllDepartmentsWithEmployees();
//        if (departments != null && !departments.isEmpty()) {
//            return ResponseEntity.ok(departments);
//        } else {
//            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
//        }
//    }
    @PostMapping("/whole")
    public ResponseEntity<List<Department>> getAllDepartmentsWithEmployees(@RequestBody(required = false) Department request) {
        List<Department> departments = departmentService.getAllDepartmentsWithEmployees();
        if (departments != null && !departments.isEmpty()) {
            return ResponseEntity.ok(departments);
        } else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
    }
//
    @GetMapping("/departmentId/{id}")
    public Optional<Department> getAllDeparmentwithEmployeeById(@PathVariable String id) {
        Optional<Department> departments = departmentService.getDepartmentById(id);
        return departments;
    }
//
//    @PutMapping("/departmentId/{id}")
//    public ResponseEntity<Department> updateDepartmentAndEmployee(@PathVariable String id, @RequestBody Department departmentDetails) {
//        Department updatedDepartment = departmentService.updateDepartmentAndEmployee(id, departmentDetails);
//        return ResponseEntity.ok(updatedDepartment);
//    }
//
    @GetMapping("/dep/{id}")
    public ResponseEntity<Map<String, String>> getDepartmentById(@PathVariable String id) {
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
