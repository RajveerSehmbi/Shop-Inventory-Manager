//package com.invManagement.Backend_REST_API.controller;
//
//import com.invManagement.Backend_REST_API.model.Employee.Employee;
//import com.invManagement.Backend_REST_API.model.Employee.EmployeeRepository;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//import java.util.Optional;
//
//@RestController
//@RequestMapping("/employee")
//public class EmployeeController {
//
//    private final EmployeeRepository repository;
//
//    public EmployeeController(EmployeeRepository repository) {
//        this.repository = repository;
//    }
//
//    //get all
//    @GetMapping
//    public ResponseEntity<?> getAllEmployees() {
//        List<Employee> employees = repository.findAll();
//        return  ResponseEntity.ok(employees);
//    }
//
//    //get an individual
//    @GetMapping("/{id}")
//    public ResponseEntity<?> getEmployee(@PathVariable String id) {
//        Optional<Employee> employee = repository.findById(id);
//        if (product == null) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found.");
//        }
//        return ResponseEntity.ok(product);
//    }
//}
