package utc.k62.cntt5.elearning.service;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import utc.k62.cntt5.elearning.domain.*;
import utc.k62.cntt5.elearning.dto.ApiResponse;
import utc.k62.cntt5.elearning.dto.StudentDto;
import utc.k62.cntt5.elearning.enumeration.NotificationType;
import utc.k62.cntt5.elearning.enumeration.Role;
import utc.k62.cntt5.elearning.exception.BusinessException;
import utc.k62.cntt5.elearning.exception.ResourceNotFoundException;
import utc.k62.cntt5.elearning.repository.*;

import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
public class StudentService {
    private final ClassRegistrationRepository classRegistrationRepository;
    private final ClassAttendanceRepository classAttendanceRepository;
    private final TutorFeeDetailRepository tutorFeeDetailRepository;
    private final ClassroomRepository classroomRepository;
    private final UserService userService;
    private final ClassroomService classroomService;
    private final String tempFolder;
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    @Autowired
    public StudentService(
            ClassRegistrationRepository classRegistrationRepository,
            ClassAttendanceRepository classAttendanceRepository,
            TutorFeeDetailRepository tutorFeeDetailRepository,
            ClassroomRepository classroomRepository,
            UserService userService,
            ClassroomService classroomService,
            @Value("${app.temp}") String tempFolder, NotificationRepository notificationRepository, UserRepository userRepository) {
        this.classRegistrationRepository = classRegistrationRepository;
        this.classAttendanceRepository = classAttendanceRepository;
        this.tutorFeeDetailRepository = tutorFeeDetailRepository;
        this.classroomRepository = classroomRepository;
        this.userService = userService;
        this.classroomService = classroomService;
        this.tempFolder = tempFolder;
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }

    public List<ClassRegistration> getAllStudentForClass(Long classId) {
        return classRegistrationRepository.findAllByClassroomIdOrderByLastNameAsc(classId);
    }

    public Page<?> search(Map<String, String> params, Pageable pageable) {
        User currentLoginUser = userService.getCurrentUserLogin();
        if (currentLoginUser.getRole() != Role.TEACHER.getValue()) {
            throw new BusinessException("Require Role Teacher!");
        }
        List<Classroom> classrooms = classroomRepository.findAllByTeacherId(currentLoginUser.getId());
        List<Long> classId = classrooms.stream().map(Classroom::getId).collect(Collectors.toList());
        Specification<ClassRegistration> specs = getSpecification(params, classId);
        Page<ClassRegistration> all = classRegistrationRepository.findAll(specs, pageable);
        List<ClassRegistration> students = all.getContent();
        List<StudentDto> studentDtos = new ArrayList<>();
        for (ClassRegistration classRegistration : students) {
            List<TutorFeeDetail> tutorFeeDetails = classRegistration.getTutorFeeDetails();
            Long feeNotSubmitted = 0L;
            for (TutorFeeDetail feeDetail : tutorFeeDetails) {
                TutorFee tutorFee = feeDetail.getTutorFee();
                feeNotSubmitted += (long)tutorFee.getLessonPrice() * feeDetail.getNumberOfAttendedLesson() - feeDetail.getFeeSubmitted();
            }
            Classroom classroom = classRegistration.getClassroom();
            StudentDto studentDto = StudentDto.builder().
                    id(classRegistration.getId())
                    .dob(classRegistration.getDob())
                    .firstName(classRegistration.getFirstName())
                    .surname(classRegistration.getSurname())
                    .lastName(classRegistration.getLastName())
                    .email(classRegistration.getEmail())
                    .phone(classRegistration.getPhone())
                    .note(classRegistration.getNote())
                    .feeNotSubmitted(feeNotSubmitted)
                    .className(classroom.getClassName())
                    .build();
            studentDtos.add(studentDto);
        }
        return new PageImpl<>(studentDtos, pageable, all.getTotalElements());
    }

    private Specification<ClassRegistration> getSpecification(Map<String, String> params, List<Long> classIds) {
        return Specification.where((root, criteriaQuery, criteriaBuilder) -> {
            Predicate predicate = null;
            List<Predicate> predicateList = new ArrayList<>();
            for (Map.Entry<String, String> p : params.entrySet()) {
                String key = p.getKey();
                String value = p.getValue();
                if (!"page".equalsIgnoreCase(key) && !"size".equalsIgnoreCase(key) && !"sort".equalsIgnoreCase(key)) {
                    if (StringUtils.equalsIgnoreCase("startCreatedDate", key)) { //"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
                        predicateList.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createdDate"), LocalDate.parse(value, DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay().toInstant(ZoneOffset.UTC)));
                    } else if (StringUtils.equalsIgnoreCase("endCreatedDate", key)) {
                        predicateList.add(criteriaBuilder.lessThanOrEqualTo(root.get("createdDate"), LocalDate.parse(value, DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay().toInstant(ZoneOffset.UTC)));
                    } else if (StringUtils.equalsIgnoreCase("className", key)) {
                        predicateList.add(criteriaBuilder.like(root.get("classroom").get("className"), "%" + value + "%"));
                    } else {
                        if (value != null && (value.contains("*") || value.contains("%"))) {
                            predicateList.add(criteriaBuilder.like(root.get(key), "%" + value + "%"));
                        } else if (value != null) {
                            predicateList.add(criteriaBuilder.like(root.get(key), value + "%"));
                        }
                    }
                }
            }

            predicateList.add(root.get("classroom").get("id").in(classIds));

            if (!predicateList.isEmpty()) {
                predicate = criteriaBuilder.and(predicateList.toArray(new Predicate[]{}));
            }

            return predicate;
        });
    }

    public StudentDto getStudentDetail(String userNumber){
        User user = userRepository.findByUserNumber(userNumber)
                .orElseThrow(() -> new BusinessException("Not found user for user number " + userNumber));
        return StudentDto.mapToStudentDto(user);
    }

    public Object addStudentForClass(StudentDto studentDto, Long classId) {
        Classroom classroom = classroomService.getById(classId);
        ClassRegistration student = ClassRegistration.newBuilder()
                .firstName(studentDto.getFirstName())
                .surname(studentDto.getSurname())
                .lastName(studentDto.getLastName())
                .email(studentDto.getEmail())
                .phone(studentDto.getPhone())
                .address(studentDto.getAddress()).build();
        student.setClassroom(classroom);
        if (StringUtils.isNotBlank(studentDto.getUserNumber())) {
            Optional<User> existingUser = userRepository.findByUserNumber(studentDto.getUserNumber());
            if (existingUser.isPresent()) {
                student.setStudent(existingUser.get());
            } else {
                try {
                    userService.createDefaultStudentAccount(student);

                    generateNotification(classroom, student);
                } catch (Exception e) {
                    log.error("Failed to create account for email {}", student.getEmail(), e);
                }
            }
        }
        classRegistrationRepository.save(student);

        return new ApiResponse(true, "Success");
    }

    private void generateNotification(Classroom classroom, ClassRegistration student) {
        Notification notification = new Notification();
        notification.setFromUserNumber(classroom.getTeacher().getUserNumber());
        notification.setToUserNumber(student.getStudent().getUserNumber());
        notification.setContent("Bạn đã được thêm vào lớp " + classroom.getClassName());
        notification.setType(NotificationType.ADDITIONAL_STUDENT_NOTIFICATION.getValue());

        log.info("Sent notification to user {}", student.getStudent().getUserNumber());
        notificationRepository.save(notification);
    }

    public String extractListStudent(Long classId) {
        List<ClassRegistration> students = getAllStudentForClass(classId);
        String fileName = tempFolder + "/" + "students_class_" + classId + ".xlsx";

        Workbook workbook = new XSSFWorkbook();
        try {
            Sheet sheet = workbook.createSheet("Students");

            // Create header row
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("ID");
            headerRow.createCell(1).setCellValue("Tên Họ");
            headerRow.createCell(2).setCellValue("Tên Đệm");
            headerRow.createCell(3).setCellValue("Tên");
            headerRow.createCell(4).setCellValue("Email");
            headerRow.createCell(5).setCellValue("Số điện thoại");
            headerRow.createCell(6).setCellValue("Địa chỉ");

            // Create data rows
            int rowIndex = 1;
            for (ClassRegistration student : students) {
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(student.getId());
                row.createCell(1).setCellValue(student.getFirstName());
                row.createCell(2).setCellValue(student.getSurname());
                row.createCell(3).setCellValue(student.getLastName());
                row.createCell(4).setCellValue(student.getEmail());
                row.createCell(5).setCellValue(student.getPhone());
                row.createCell(6).setCellValue(student.getAddress());
            }

            // Write the output to a file
            try (FileOutputStream fileOut = new FileOutputStream(fileName)) {
                workbook.write(fileOut);
            }
        } catch (IOException e) {
            log.error("Error while writing XLSX file", e);
            return null;
        }

        return fileName;
    }

    @Transactional
    public ApiResponse deleteStudent(Long studentId) {
        User user = userService.getCurrentUserLogin();
        if (user.getRole() != Role.TEACHER.getValue() || StringUtils.equalsIgnoreCase(user.getRole(), Role.ADMIN.getValue())) {
            throw new BusinessException("Missing permission");
        }
        List<Classroom> classrooms = user.getClassrooms();
        List<ClassRegistration> classRegistrations = classrooms.stream().flatMap(classroom -> classroom.getClassRegistrations().stream()).collect(Collectors.toList());
        classRegistrations.stream()
                .filter(student -> student.getId().equals(studentId)).findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Not found student"));
        try {
            classAttendanceRepository.deleteAllByClassRegistrationId(studentId);
            // if exist tutorFee -> can not delete
            classRegistrationRepository.deleteById(studentId);
        } catch (Exception e) {
            log.error("Exception during delete operation", e);
            throw new BusinessException("Deletion failed due to an error");
        }

        return new ApiResponse(true, "Success");
    }

    public Object updateStudent(StudentDto studentDto) {
        ClassRegistration existingStudent = classRegistrationRepository.findById(studentDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("Student not found with id: " + studentDto.getDob()));

        // Update the fields
        existingStudent.setFirstName(studentDto.getFirstName());
        existingStudent.setSurname(studentDto.getSurname());
        existingStudent.setLastName(studentDto.getLastName());
        existingStudent.setEmail(studentDto.getEmail());
        existingStudent.setPhone(studentDto.getPhone());
        existingStudent.setAddress(studentDto.getAddress());
        existingStudent.setDob(studentDto.getDob());

        classRegistrationRepository.save(existingStudent);

        return "Success";
    }
}
