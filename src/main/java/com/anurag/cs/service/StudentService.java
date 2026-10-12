package com.anurag.cs.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.anurag.cs.dto.LoginDTO;
import com.anurag.cs.dto.LoginResponseDTO;
import com.anurag.cs.dto.RegisterDTO;
import com.anurag.cs.entity.Student;
import com.anurag.cs.enums.Role;
import com.anurag.cs.repository.StudentRepository;

@Service
public class StudentService {
      @Autowired // dependency injection(field)
      private StudentRepository repo;

      @Autowired
      private JwtService service;

      @Autowired
      private PasswordEncoder passwordEncoder;

      public List<Student> getAllStudents() {
            return repo.findAll();
      }

      public Student createStudent(RegisterDTO dto) {
            Student student = new Student();
            student.setName(dto.getName());
            student.setRole(Role.valueOf(dto.getRole()));
            student.setEmail(dto.getEmail());
            student.setPassword(passwordEncoder.encode(dto.getPassword()));
            return repo.save(student);
      }

      public Student getStudentById(Long id) {
            return repo.findById(id).orElse(null);
      }

      public Student updateStudent(Long id, Student student) {
            Student existingStudent = repo.findById(id).orElse(null);
            if (existingStudent != null) {
                  existingStudent.setName(student.getName());
                  existingStudent.setEmail(student.getEmail());
                  existingStudent.setPassword(passwordEncoder.encode(student.getPassword()));
                  return repo.save(existingStudent);
            }
            return null;
      }

      public String deleteStudent(Long id) {
            if (repo.existsById(id)) {
                  repo.deleteById(id);
                  return "Student deleted successfully";
            }
            return "Student not found";
      }

      public ResponseEntity<?> login(LoginDTO dto) {
            Student existingStu = repo.findByEmail(dto.getEmail());
            if (existingStu == null) {
                  return ResponseEntity
                              .status(HttpStatus.UNAUTHORIZED)
                              .body("Email is Wrong");
            }
            boolean password = passwordEncoder.matches(dto.getPassword(), existingStu.getPassword());
            if (!password) {
                  return ResponseEntity
                              .status(HttpStatus.UNAUTHORIZED)
                              .body("Password is Wrong");
            }
            String token = service.generateToken(dto.getEmail());
            LoginResponseDTO response = new LoginResponseDTO(token, existingStu.getId(), existingStu.getEmail(),
                        existingStu.getRole().name());
            return ResponseEntity.ok(response);
      }
}