package xj.love.hj.demo.spring.jpa.controller;

import org.junit.Test;
import org.springframework.http.MediaType;
import xj.love.hj.demo.spring.jpa.WebBaseTests;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * 用户控制器测试
 *
 * @author xiaojia
 * @since 1.0
 */
public class UserControllerTests extends WebBaseTests {

    /**
     * 3.8.2. Web支持
     */
    @Test
    public void findUserById() throws Exception {
        mockMvc.perform(
                get("/users/{id}", 1)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.firstName").value("bob"))
                .andExpect(jsonPath("$.lastName").value("cui"))
                .andExpect(jsonPath("$.address.city").value("上海"));
    }

    @Test
    public void findUsers() throws Exception {
        mockMvc.perform(
                get("/users")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(jsonPath("$..userList.length()").value(2))
                .andExpect(jsonPath("$..userList[0].firstName").value("bob"))
                .andExpect(jsonPath("$..self.href").exists())
                .andExpect(jsonPath("$.page.number").value(0))
                .andExpect(jsonPath("$.page.size").value(10));
    }

    /**
     * Web数据绑定支持
     */
    @Test
    public void requestPayload() throws Exception {
        String requestBody = "{\n"
                + "    \"firstName\": \"david\",\n"
                + "    \"lastName\": \"zhang\"\n"
                + "}";
        mockMvc.perform(
                post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(content().string("david.zhang"));

        requestBody = "{\n"
                + "    \"firstName\": \"david\",\n"
                + "    \"user\": { \"lastName\": \"zhang\" }\n"
                + "}";
        mockMvc.perform(
                post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(content().string("david.zhang"));
    }

    /**
     * Querydsl Web支持
     */
    @Test
    public void findUsersByQuerydsl() throws Exception {
        mockMvc.perform(
                get("/users/querydsl")
                        .param("firstName", "bob")
                        .param("firstName", "sunny"))
                .andExpect(jsonPath("$..content.length()").value(2))
                .andExpect(jsonPath("$..content[0].firstName").value("bob"))
                .andExpect(jsonPath("$..content[1].firstName").value("sunny"));
    }
}
