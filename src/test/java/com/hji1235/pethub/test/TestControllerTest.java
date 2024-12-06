package com.hji1235.pethub.test;

import com.hji1235.pethub.RestDocsTestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(TestController.class)
class TestControllerTest extends RestDocsTestSupport {

    @Test
    public void example() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/example"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

}