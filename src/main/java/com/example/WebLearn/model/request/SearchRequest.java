package com.example.WebLearn.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchRequest {
    public String keyword="";
    @Builder.Default
    public int page = 0;
    @Builder.Default
    public int limit = 10;
    @Builder.Default
    public String direction = "des";
    @Builder.Default
    public String sortBy = "id";
}
