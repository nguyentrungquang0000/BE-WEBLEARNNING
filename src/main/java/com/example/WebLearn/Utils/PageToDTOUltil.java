package com.example.WebLearn.Utils;

import org.springframework.data.domain.Page;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PageToDTOUltil {
    public static Map<String, Object> pageToDTO(Page<?> page, List<?> objectDTOs){
        // Tạo response với các thông tin phân trang
        Map<String, Object> response = new HashMap<>();
        response.put("content", objectDTOs);
        response.put("pageable", page.getPageable());
        response.put("totalPages", page.getTotalPages());
        response.put("totalElements", page.getTotalElements());
        response.put("currentPage", page.getNumber());
        response.put("size", page.getSize());
        return response;
    }
}
