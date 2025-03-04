package class101.foo.io;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostController {
    private final static Integer PAGE_SIZE = 20;

    private final Producer producer;
    private final PostRepository postRepository;
    private final PostCacheService postCacheService;
    private final ObjectMapper objectMapper;

    // 1. 글을 작성한다.
    @PostMapping("/posts")
    public Post createPost(@RequestBody Post post) throws JsonProcessingException {
        String jsonPost = objectMapper.writeValueAsString(post);
        producer.sendTo(jsonPost);

        return post;
    }

    // 2. 글 목록을 페이징하여 반환
    @GetMapping("/posts")
    public Page<Post> getPostList(@RequestParam(defaultValue = "1") Integer page) {
        if (page.equals(1)) {
            return postCacheService.getFirstPostPage();
        } else {
            return postRepository.findAll(
                    PageRequest.of(page - 1, PAGE_SIZE, Sort.by("id").descending())
            );
        }
    }

    // 3. 글 번호로 조회
    @GetMapping("/posts/{id}")
    public Post getPostById(@PathVariable("id") Long id) {
        return postRepository.findById(id).get();
    }

    // 4. 글 내용으로 검색 -> 해당 내용이 포함된 모든 글
    @GetMapping("/search")
    public List<Post> getPostByContent(@RequestParam String content) {
        return postRepository.findByContentContains(content);
    }

}
