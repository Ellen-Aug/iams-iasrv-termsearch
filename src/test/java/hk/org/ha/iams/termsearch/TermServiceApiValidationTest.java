package hk.org.ha.iams.termsearch;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Empty {} → 6 (missing compulsory). Malformed JSON → 400.
 */
@SpringBootTest
@AutoConfigureMockMvc
class TermServiceApiValidationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void searchTermCodeReturnsHttp200WithStatusInBody() throws Exception {
        mockMvc.perform(post("/iams/api/termsearch/searchTermCode")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.termStatus.returnCode").value(6));
    }

    @Test
    void getTermDescReturnsHttp200WithStatusInBody() throws Exception {
        mockMvc.perform(post("/iams/api/termsearch/getTermDesc")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status.returnCode").value(6));
    }

    @Test
    void malformedJsonIsHttp400() throws Exception {
        mockMvc.perform(post("/iams/api/termsearch/searchTermCode")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{"))
                .andExpect(status().isBadRequest());
    }
}
