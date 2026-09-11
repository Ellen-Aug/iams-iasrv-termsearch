package hk.org.ha.iams.termsearch;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Happy paths against DEV Oracle. Skipped unless ORACLE_PASSWORD is set so
 * {@code ./mvnw -B verify} stays green without a database.
 */
@EnabledIfEnvironmentVariable(named = "ORACLE_PASSWORD", matches = ".+")
@ActiveProfiles("dev")
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
