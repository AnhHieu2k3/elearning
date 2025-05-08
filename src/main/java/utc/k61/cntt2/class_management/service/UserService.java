package utc.k61.cntt2.class_management.service;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import utc.k61.cntt2.class_management.domain.ClassRegistration;
import utc.k61.cntt2.class_management.domain.User;
import utc.k61.cntt2.class_management.dto.*;
import utc.k61.cntt2.class_management.dto.security.SignUpRequest;
import utc.k61.cntt2.class_management.enumeration.Role;
import utc.k61.cntt2.class_management.exception.BadRequestException;
import utc.k61.cntt2.class_management.exception.BusinessException;
import utc.k61.cntt2.class_management.repository.ClassRegistrationRepository;
import utc.k61.cntt2.class_management.repository.UserRepository;
import utc.k61.cntt2.class_management.security.SecurityUtils;
import utc.k61.cntt2.class_management.service.email.EmailService;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

@Service
@Log4j2
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final EmailService emailService;
    private final ClassRegistrationRepository classRegistrationRepository;

    @Autowired
    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       EmailService emailService,
                       ClassRegistrationRepository classRegistrationRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.classRegistrationRepository = classRegistrationRepository;
    }

    public void createNewUser(SignUpRequest signUpRequest) {
        if (userRepository.existsByUserNumber(signUpRequest.getUserNumber())) {
            String errorMessage = String.format("Username %s already in use", signUpRequest.getUserNumber());
            throw new BadRequestException(errorMessage);
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            String errorMessage = String.format("Email %s already in use", signUpRequest.getEmail());
            throw new BadRequestException(errorMessage);
        }

        User user = generateUser(signUpRequest);

        sendEmailVerification(user);
        user = userRepository.save(user);
    }

    private User generateUser(SignUpRequest signUpRequest) {
        User user = new User();
        user.setUserNumber(RandomStringUtils.randomNumeric(10));
        user.setEmail(signUpRequest.getEmail());
        user.setUsername(signUpRequest.getUserNumber());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setFirstName(signUpRequest.getFirstName());
        user.setLastName(signUpRequest.getLastName());
        user.setSurname(signUpRequest.getSurname());
        user.setDob(signUpRequest.getDob());

        user.setRole(Role.STUDENT.getValue());

        user.setActive(false);
        user.setNumberActiveAttempt(0);
        String activeCode = RandomStringUtils.randomAlphanumeric(6);
        user.setActiveCode(activeCode);
        return user;
    }

    private void sendEmailVerification(User user) {
        EmailDetail emailDetail = new EmailDetail();
        if (StringUtils.isBlank(user.getEmail())) {
            throw new BusinessException("Can not send email verification for email empty!");
        }
        emailDetail.setSubject("Classroom verification");
        emailDetail.setMsgBody("Here is your active code: " + user.getActiveCode());
        emailDetail.setRecipient(user.getEmail());

        log.info("Sending email verification email for email {}", user.getEmail());
        emailService.sendSimpleEmail(emailDetail);
    }

    public void createDefaultStudentAccount(ClassRegistration student) {
        if (StringUtils.isNotBlank(student.getEmail())) {
            String randomPassword = RandomStringUtils.randomAlphanumeric(6);
            SignUpRequest signUpRequest = new SignUpRequest();
            signUpRequest.setUserNumber(student.getEmail());
            signUpRequest.setEmail(student.getEmail());
            signUpRequest.setPassword(randomPassword);
            signUpRequest.setFirstName(student.getFirstName());
            signUpRequest.setLastName(student.getLastName());
            signUpRequest.setSurname(student.getSurname());
            signUpRequest.setDob(student.getDob());

            User user = generateUser(signUpRequest);
            user.setActive(true);
            sendEmailVerification(user, randomPassword);
            user = userRepository.save(user);

            student.setStudent(user);
        }
    }

    private void sendEmailVerification(User user, String randomPassword) {
        EmailDetail emailDetail = new EmailDetail();
        if (StringUtils.isBlank(user.getEmail())) {
            throw new BusinessException("Cannot send email verification for empty email!");
        }
        emailDetail.setSubject("Xác thực tài khoản lớp học");
        emailDetail.setMsgBody("Đây là mật khẩu của bạn: " + randomPassword);
        emailDetail.setRecipient(user.getEmail());

        log.info("Sending email verification to email {}", user.getEmail());
        emailService.sendSimpleEmail(emailDetail);
    }

    public User getCurrentUserLogin() {
        String currentLogin = SecurityUtils.getCurrentUserLogin()
                .orElseThrow(() -> new BusinessException("Not found current user login"));

        return userRepository.findByUsername(currentLogin)
                .orElseThrow(() -> new BusinessException("Not found user with login " + currentLogin));
    }

    public UserDetailDto getUserInfo() {
        User user = getCurrentUserLogin();
        return new UserDetailDto(user);
    }

    public UserDetailDto updateUserInfo(UserDetailDto userDetailDto) {
        User user = getCurrentUserLogin();
        if (StringUtils.isNoneBlank(userDetailDto.getFirstName())) {
            user.setFirstName(userDetailDto.getFirstName());
        }
        if (StringUtils.isNoneBlank(userDetailDto.getSurname())) {
            user.setSurname(userDetailDto.getSurname());
        }
        if (StringUtils.isNoneBlank(userDetailDto.getLastName())) {
            user.setLastName(userDetailDto.getLastName());
        }
        if (userDetailDto.getDob() != null) {
            user.setDob(userDetailDto.getDob());
        }
        if (StringUtils.isNoneBlank(userDetailDto.getPhone())) {
            user.setPhone(userDetailDto.getPhone());
            List<ClassRegistration> classRegistrations = classRegistrationRepository.findAllByEmail(user.getEmail());
            for (ClassRegistration classRegistration : classRegistrations) {
                classRegistration.setPhone(userDetailDto.getPhone());
            }
            classRegistrationRepository.saveAll(classRegistrations);
        }
        userRepository.save(user);

        log.info("Updated info for user with login {}", user.getUsername());
        return new UserDetailDto(user);
    }

    public ApiResponse activeAccount(ActiveAccountRequest request) {
        User user = userRepository.findFirstByEmail(request.getEmail())
                .orElseThrow(() -> new BusinessException("Not found user for email: " + request.getEmail()));

        boolean result = verifyEmail(request.getCode(), user);

        if (result) {
            user.setActive(true);
            user.setActiveCode(null);
        }
        userRepository.save(user);

        return result ? new ApiResponse(true, "Success") : new ApiResponse(false, "Failed");
    }

    private boolean verifyEmail(String code, User user) {
        Instant startOfToday = LocalDate.now().atStartOfDay(ZoneOffset.UTC).toInstant();
        if (user.getNumberActiveAttempt() >= 3) {
            if (user.getLastActiveAttempt().isAfter(startOfToday)) {
                throw new BusinessException("Only 3 times to verify email a day, please try in next day!");
            } else {
                user.setNumberActiveAttempt(0);
            }
        }
        boolean result = StringUtils.equalsIgnoreCase(code, user.getActiveCode());
        user.setNumberActiveAttempt(user.getNumberActiveAttempt() + 1);
        user.setLastActiveAttempt(Instant.now());

        return result;
    }

    public ApiResponse sendMailForgotPassword(String email) {
        User user = userRepository.findFirstByEmail(email)
                .orElseThrow(() -> new BusinessException("Not found user with email: " + email));

        String activeCode = RandomStringUtils.randomAlphanumeric(6);
        user.setActiveCode(activeCode);
        userRepository.save(user);

        EmailDetail emailDetail = new EmailDetail();
        emailDetail.setSubject("Classroom verification");
        emailDetail.setMsgBody("Here is your code to reset your password: " + user.getActiveCode());
        emailDetail.setRecipient(email);

        log.info("Sending email forgot password for email {}", user.getEmail());
        emailService.sendSimpleEmail(emailDetail);

        return new ApiResponse(true, "Sent email forgot password!");
    }

    public ApiResponse resetPassword(ResetPasswordRequest request) {
        User user = userRepository.findFirstByEmail(request.getEmail())
                .orElseThrow(() -> new BusinessException("Not found user with email: " + request.getEmail()));
        boolean result = verifyEmail(request.getCode(), user);
        if (result) {
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        }
        userRepository.save(user);
        return result ? new ApiResponse(true, "Success") : new ApiResponse(false, "Failed");
    }

    public ApiResponse resetPassword(String newPass) {
        User user = getCurrentUserLogin();
        user.setPassword(passwordEncoder.encode(newPass));
        userRepository.save(user);
        return new ApiResponse(true, "Success");
    }

    public List<User> findAllByEmailIn(List<String> emails) {
        return userRepository.findAllByEmailIn(emails);
    }
}
