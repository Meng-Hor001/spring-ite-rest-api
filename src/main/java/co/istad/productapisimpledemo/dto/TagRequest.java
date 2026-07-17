package co.istad.productapisimpledemo.dto;

import lombok.Builder;

@Builder
public record TagRequest(
        String name
) {
}
