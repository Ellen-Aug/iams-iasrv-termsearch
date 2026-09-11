package hk.org.ha.iams.termsearch;

import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Layer 2 edge paths. S5/D5 validation and S6/D4 no-record work without Oracle
 * (local profile has no DataSource → empty DAO results).
 */
@SpringBootTest
@AutoConfigureMockMvc
class TermServiceApiEdgeTest {

    @Autowired
    private MockMvc mockMvc;

    @ParameterizedTest(name = "{0}")
    @CsvSource({
            "searchTermCode/S5-empty, /iams/api/termsearch/searchTermCode",
            "searchTermCode/S6-zero-hits, /iams/api/termsearch/searchTermCode",
            "getTermDesc/D4-unknown-code, /iams/api/termsearch/getTermDesc",
            "getTermDesc/D5-empty, /iams/api/termsearch/getTermDesc"
    })
    @DisplayName("HTTP 200 + business returnCode in body (never HTTP 404)")
    void edgePathMatchesExpectedStatus(String fixture, String url) throws Exception {
        JsonNode expected = TermSearchFixtures.expected(fixture);
        String statusField = expected.get("statusField").asText();
        int returnCode = expected.get("returnCode").asInt();

        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TermSearchFixtures.request(fixture)))
                .andExpect(status().is(expected.get("httpStatus").asInt()))
                .andExpect(jsonPath("$." + statusField + ".returnCode").value(returnCode));
    }

    @Test
    @DisplayName("stub message must not be treated as a real no-record once SQL is live")
    void zeroHitsIsNotTheSqlPendingStub() throws Exception {
        mockMvc.perform(post("/iams/api/termsearch/searchTermCode")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TermSearchFixtures.request("searchTermCode/S6-zero-hits")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.termStatus.returnCode").value(7))
                .andExpect(jsonPath("$.termStatus.returnMessage", not("Not implemented: search SQL port pending")));
    }
}
