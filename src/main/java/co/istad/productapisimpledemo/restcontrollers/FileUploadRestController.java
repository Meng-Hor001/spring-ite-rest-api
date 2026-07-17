package co.istad.productapisimpledemo.restcontrollers;

import co.istad.productapisimpledemo.dto.file.FileResponse;
import co.istad.productapisimpledemo.mapper.FileUploadMapper;
import co.istad.productapisimpledemo.service.FileUploadService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
public class FileUploadRestController {
    private final FileUploadService fileUploadService;


    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public FileResponse uploadFile(@RequestPart MultipartFile file) {
        return fileUploadService.upload(file);
    }

    @PostMapping("/multiple")
    public List<FileResponse> uploadMultipleFiles(@RequestPart List<MultipartFile> files) {
        return fileUploadService.uploadMultipleFiles(files);
    }

    @GetMapping
    public Page<FileResponse> getAllFiles(@RequestParam int pageNumber,@RequestParam int pageSize) {
        return fileUploadService.findAll(pageNumber, pageSize);
    }


    @DeleteMapping("/{fileName}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFile(@PathVariable String fileName ){
        fileUploadService.deleteByName(fileName);
    }
}
