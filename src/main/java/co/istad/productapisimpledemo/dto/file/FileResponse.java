package co.istad.productapisimpledemo.dto.file;


import lombok.Builder;

@Builder
public record FileResponse(
        String name,
        String caption,
        String extension,
        Long size,
        // localhost:8080/files/filename.png ...
        String url
) {
}
