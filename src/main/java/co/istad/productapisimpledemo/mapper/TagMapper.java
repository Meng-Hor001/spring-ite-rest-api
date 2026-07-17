package co.istad.productapisimpledemo.mapper;
import co.istad.productapisimpledemo.dto.TagRequest;
import co.istad.productapisimpledemo.dto.TagResponse;
import co.istad.productapisimpledemo.entity.Tag;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TagMapper {


    TagResponse toResponse(Tag tag);
    Tag toEntity(TagRequest tagRequest);
}
