package io.spring.application;

import io.spring.infrastructure.mybatis.readservice.TagReadService;
import org.springframework.stereotype.Service;

import java.util.List;
/**
 * @Author：zhangchengxi
 * @Date：2018/9/6 19:48
 */
@Service
public class TagsQueryService {
    private TagReadService tagReadService;

    public TagsQueryService(TagReadService tagReadService) {
        this.tagReadService = tagReadService;
    }

    public List<String> allTags() {
        return tagReadService.all();
    }
}
