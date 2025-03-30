package vn.hieu4tuoi.common;

import java.util.List;

public enum FoodStatus {
    AVAILABLE,
    UNAVAILABLE,
    DELETED;

    // Trả về danh sách trạng thái hợp lệ để hiển thị
    public static List<FoodStatus> getValidStatuses() {
        return List.of(AVAILABLE, UNAVAILABLE);
    }
}
