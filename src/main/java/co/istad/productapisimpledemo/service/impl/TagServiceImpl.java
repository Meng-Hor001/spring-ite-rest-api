package co.istad.productapisimpledemo.service.impl;

import co.istad.productapisimpledemo.dto.TagRequest;
import co.istad.productapisimpledemo.dto.TagResponse;
import co.istad.productapisimpledemo.mapper.TagMapper;
import co.istad.productapisimpledemo.repository.TagRepository;
import co.istad.productapisimpledemo.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;
    private final TagMapper tagMapper;

    @Override
    public TagResponse createTag(TagRequest request) {
       var tag =  tagMapper.toEntity(request);
       return tagMapper.toResponse(tagRepository.save(tag));
    }

    @Override
    public Page<TagResponse> getAllTags(Pageable pageable) {
       return  tagRepository
               .findAll(pageable)
               .map(tagMapper::toResponse);
    }
}
