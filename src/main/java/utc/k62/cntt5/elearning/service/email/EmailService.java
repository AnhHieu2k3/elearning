package utc.k62.cntt5.elearning.service.email;

import utc.k62.cntt5.elearning.dto.EmailDetail;

public interface EmailService {
    String sendSimpleEmail(EmailDetail detail);
}
