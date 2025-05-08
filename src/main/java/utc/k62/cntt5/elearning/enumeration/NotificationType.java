package utc.k62.cntt5.elearning.enumeration;

public enum NotificationType {
    ADDITIONAL_STUDENT_NOTIFICATION("Thông báo lớp học");

    private final String value;

    NotificationType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
