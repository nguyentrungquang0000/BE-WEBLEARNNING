package com.example.WebLearn.service.impl;

import com.example.WebLearn.entity.Student;
import com.example.WebLearn.entity.Teacher;
import com.example.WebLearn.model.request.RegisterRequest;
import com.example.WebLearn.model.response.LoginResponse;
import com.example.WebLearn.model.response.Response;
import com.example.WebLearn.repository.StudentRepository;
import com.example.WebLearn.repository.TeacherRepository;
import com.example.WebLearn.service.UserService;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    private static final long VALID_DURATION = 3600; // 1 giờ (đơn vị: giây)
    @Value("${jwt.signerKey}")
    private String signerKey;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ModelMapper modelMapper;


    @Override
    public ResponseEntity<Response<Object>> login(String email, String password, String role) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        Optional<?> userOptional = Optional.empty();

        // Kiểm tra user theo role
        if (role.equals("ADMIN")) {
            userOptional = teacherRepository.findByEmail(email);
        } else if (role.equals("USER")) {
            userOptional = studentRepository.findByEmail(email);
        }

        // Kiểm tra user sau khi xử lý
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(404).body(new Response<>(404, "Email không tồn tại!", null));
        }

        // Xác thực mật khẩu
        if (role.equals("ADMIN")) {
            Teacher teacher = (Teacher) userOptional.get(); // Ép kiểu sang Teacher
            boolean authenticated = passwordEncoder.matches(password, teacher.getPassword());
            if (!authenticated) {
                return ResponseEntity.status(401).body(new Response<>(401, "Mật khẩu không đúng!", null));
            }
            String token = generateToken(teacher, role); // Sử dụng teacher trong generateToken
            return ResponseEntity.ok(new Response<>(200, "Xác thực thành công!", new LoginResponse(role, teacher.getId(), token)));
        } else if (role.equals("USER")) {
            Student student = (Student) userOptional.get(); // Ép kiểu sang Student
            boolean authenticated = passwordEncoder.matches(password, student.getPassword());
            if (!authenticated) {
                return ResponseEntity.status(401).body(new Response<>(401, "Mật khẩu không đúng!", null));
            }
            String token = generateToken(student, role); // Sử dụng student trong generateToken
            return ResponseEntity.ok(new Response<>(200, "Xác thực thành công!", new LoginResponse(role, student.getId(), token)));
        }

        return ResponseEntity.status(400).body(new Response<>(400, "Role không hợp lệ", null));
    }

    @Override
    public ResponseEntity<Response<Object>> register(RegisterRequest registerRequest) {
        if (registerRequest.getRole().equals("ADMIN")) {
            Teacher teacher = (Teacher) modelMapper.map(registerRequest, Teacher.class);
            teacher.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
            Optional<Teacher> existingTeacher = teacherRepository.findByEmail(teacher.getEmail());
            if (existingTeacher.isPresent()) {
                return ResponseEntity.status(409).body(new Response<>(409, "Email đã tồn tại!", null));
            }
            teacherRepository.save(teacher);
        } else if (registerRequest.getRole().equals("USER")) {
            Student student = modelMapper.map(registerRequest, Student.class);
            student.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
            Optional<Student> existingStudent = studentRepository.findByEmail(student.getEmail());
            if (existingStudent.isPresent()) {
                return ResponseEntity.status(409).body(new Response<>(409, "Email đã tồn tại!", null));
            }
            studentRepository.save(student);
        }
        return ResponseEntity.ok(new Response<>(200, "Đăng kí thành công", null));
    }

    private String generateToken(Object user, String role) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user instanceof Teacher ? ((Teacher) user).getEmail() : ((Student) user).getEmail())
                .issuer("ss.com")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(VALID_DURATION, ChronoUnit.SECONDS).toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", role)
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(signerKey.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException("Không thể tạo token", e);
        }
    }

//    public JWTClaimsSet verifyToken(String token) {
//        try {
//            SignedJWT signedJWT = SignedJWT.parse(token);
//            signedJWT.verify(new MACVerifier(signerKey.getBytes()));
//            return signedJWT.getJWTClaimsSet();
//        } catch (Exception e) {
//            throw new RuntimeException("Token invalid!");
//        }
//    }
}
