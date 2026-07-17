package co.istad.productapisimpledemo.service;

import co.istad.productapisimpledemo.dto.TagRequest;
import co.istad.productapisimpledemo.dto.TagResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TagService {
    // request, response
    TagResponse createTag(TagRequest request);
    Page<TagResponse> getAllTags(Pageable pageable);
}
