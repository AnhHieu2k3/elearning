package utc.k61.cntt2.class_management.enumeration;

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
