package io.spring.application.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;
/**
 * @Author：zhangchengxi
 * @Date：2018/9/6 19:48
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentData {
    private String id;
    private String body;
    @JsonIgnore
    private String articleId;
    private DateTime createdAt;
    private DateTime updatedAt;
    @JsonProperty("author")
    private ProfileData author;
}
