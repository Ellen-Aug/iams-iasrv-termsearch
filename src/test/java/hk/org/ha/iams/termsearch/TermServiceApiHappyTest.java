package hk.org.ha.iams.termsearch;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Layer 2 happy paths from old EJB tests. Enable after TermServiceDataAccessPOJO SQL port.
 * Stub today returns returnCode 7 ("SQL port pending") — these would fail, which is the TDD signal.
 */
@Disabled("Enable after DEV Oracle (termsearch.datasource.enabled=true). Local profile has no DataSource so valid searches return 7.")
@SpringBootTest
@AutoConfigureMockMvc
class TermServiceApiHappyTest {

    @Autowired
    private MockMvc mockMvc;

    @ParameterizedTest(name = "{0}")
    @CsvSource({
            "searchTermCode/S1-icpc2-begin-k8, /iams/api/termsearch/searchTermCode",
            "searchTermCode/S2-keyword-hypertension, /iams/api/termsearch/searchTermCode",
            "searchTermCode/S3-term-ids, /iams/api/termsearch/searchTermCode",
            "searchTermCode/S4-health-ca-lung, /iams/api/termsearch/searchTermCode",
            "getTermDesc/D1-icd9px-99.60, /iams/api/termsearch/getTermDesc",
            "getTermDesc/D2-term-ids, /iams/api/termsearch/getTermDesc",
            "getTermDesc/D3-health-three-codes, /iams/api/termsearch/getTermDesc"
    })
    @DisplayName("HTTP 200 + success returnCode + minCount from fixture")
    void happyPathMatchesExpectedStatus(String fixture, String url) throws Exception {
        JsonNode expected = TermSearchFixtures.expected(fixture);
        String statusField = expected.get("statusField").asText();
        int returnCode = expected.get("returnCode").asInt();
        int minCount = expected.get("minCount").asInt();

        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TermSearchFixtures.request(fixture)))
                .andExpect(status().is(expected.get("httpStatus").asInt()))
                .andExpect(jsonPath("$." + statusField + ".returnCode").value(returnCode))
                .andExpect(jsonPath("$." + statusField + ".count").value(greaterThanOrEqualTo(minCount)));
    }
}
