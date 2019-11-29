package xj.love.hj.demo.spring.jpa.controller;

import com.querydsl.core.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import xj.love.hj.demo.spring.jpa.controller.payload.UserPayload;
import xj.love.hj.demo.spring.jpa.dao.UserRepository;
import xj.love.hj.demo.spring.jpa.domain.User;

/**
 * 用户控制器
 *
 * @author xiaojia
 * @since 1.0
 */
@RestController
@RequestMapping(value = "users", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("{id}")
    public User findUserById(@PathVariable("id") User user) {
        return user;
    }

    @GetMapping
    public HttpEntity<PagedResources<User>> findUsers(Pageable pageable,
            PagedResourcesAssembler assembler) {
        Page<User> users = userRepository.findAll(pageable);
        return new ResponseEntity<>(assembler.toResource(users), HttpStatus.OK);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public String requestPayload(@RequestBody UserPayload userPayload) {
        return String.format("%s.%s", userPayload.getFirstName(), userPayload.getLastName());
    }

    @GetMapping("querydsl")
    public Page<User> findUsersByQuerydsl(@QuerydslPredicate(root = User.class) Predicate predicate,
            Pageable pageable, @RequestParam MultiValueMap<String, String> parameters) {
        return userRepository.findAll(predicate, pageable);
    }
}
