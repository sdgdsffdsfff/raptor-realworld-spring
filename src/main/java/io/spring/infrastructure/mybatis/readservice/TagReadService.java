package io.spring.infrastructure.mybatis.readservice;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
/**
 * @Author：zhangchengxi
 * @Date：2018/9/6 19:48
 */
@Component
@Mapper
public interface TagReadService {
    List<String> all();
}
