package com.anurag.cs.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anurag.cs.dto.LoginDTO;
import com.anurag.cs.dto.RegisterDTO;
import com.anurag.cs.entity.Student;
import com.anurag.cs.service.StudentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/students") // localhost:8080/students
@CrossOrigin(origins = "https://sms-ab-backend.onrender.com")
public class StudentController {

      @Autowired
      private StudentService service;

      @GetMapping
      public List<Student> getAllStudents() {
            return service.getAllStudents();
      }

      @PostMapping("/login")
      public ResponseEntity<?> login(@RequestBody LoginDTO dto) {
            return service.login(dto);
      }

      @PostMapping("/register")
      public Student createStudent(@Valid @RequestBody RegisterDTO dto) {
            return service.createStudent(dto);
      }

      // localhost:8080/students/2
      @GetMapping("/{id}")
      public Student getStudentById(@PathVariable Long id) {
            return service.getStudentById(id);
      }

      @PreAuthorize("hasRole('ADMIN')")
      @PutMapping("/{id}")
      public Student updateStudent(@PathVariable Long id, @RequestBody Student student) {
            return service.updateStudent(id, student);
      }

      @PreAuthorize("hasRole('ADMIN')")
      @DeleteMapping("/{id}")
      public String deleteStudent(@PathVariable Long id) {
            return service.deleteStudent(id);
      }
}