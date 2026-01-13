package com.viettel.vss.enums;

public enum DeviceStatus {
    PROCESSING("processing"),// đang xử lý
    REJECTED("reject"),// từ chối
    CANCELED("cancel"),// đã hủy
    WAIT_APPROVE("wait_approve"),//Chờ phê duyệt
    APPROVED("approve"),//đã phê duệt
    DISBURSED("disbursed");// đã giải ngân

    private final String status;

    DeviceStatus(String action) {
        this.status = action;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return status;
    }

    // Optional: convert String -> enum
    public static DeviceStatus fromString(String action) {
        for (DeviceStatus a : DeviceStatus.values()) {
            if (a.status.equalsIgnoreCase(action)) {
                return a;
            }
        }
        throw new IllegalArgumentException("Unknown action: " + action);
    }
}
