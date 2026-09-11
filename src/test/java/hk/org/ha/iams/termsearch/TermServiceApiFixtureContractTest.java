package hk.org.ha.iams.termsearch;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Layer 1 enabled now: fixture JSON must bind (Jackson) and stay HTTP 200.
 * Does not assert business returnCode — that is Happy/Edge after SQL/validation.
 */
@SpringBootTest
@AutoConfigureMockMvc
class TermServiceApiFixtureContractTest {

    @Autowired
    private MockMvc mockMvc;

    @ParameterizedTest(name = "{0}")
    @CsvSource({
            "searchTermCode/S1-icpc2-begin-k8, /iams/api/termsearch/searchTermCode",
            "searchTermCode/S4-health-ca-lung, /iams/api/termsearch/searchTermCode",
            "getTermDesc/D1-icd9px-99.60, /iams/api/termsearch/getTermDesc",
            "getTermDesc/D3-health-three-codes, /iams/api/termsearch/getTermDesc"
    })
    @DisplayName("fixture request deserializes and is HTTP 200 (stub returnCode 7 is OK)")
    void fixtureDeserializesAsHttp200(String fixture, String url) throws Exception {
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TermSearchFixtures.request(fixture)))
                .andExpect(status().isOk());
    }
}
