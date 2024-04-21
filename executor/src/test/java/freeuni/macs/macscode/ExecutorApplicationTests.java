package freeuni.macs.macscode;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class ExecutorApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldPass_WhenGivenCorrectSolution_ToNumberAdditionProblemTest() throws Exception {
        this.mockMvc.perform(post("/submission")
                .contentType(MediaType.APPLICATION_JSON)
                .content(fromFile("twoNumberAdditionCorrectSolutionRequest.json")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(3)))
                .andExpect(jsonPath("$[0].result", is("PASS")))
                .andExpect(jsonPath("$[1].result", is("PASS")))
                .andExpect(jsonPath("$[2].result", is("PASS")));
    }

    @Test
    void shouldFail_WhenGivenWrongSolution_ToNumberAdditionProblemTest() throws Exception {
        this.mockMvc.perform(post("/submission")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(fromFile("twoNumberAdditionWrongSolutionRequest.json")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(1)))
                .andExpect(jsonPath("$[0].result", is("FAIL")));
    }

    private byte[] fromFile(String fileName) throws IOException {
        String path = "src/test/java/freeuni/macs/macscode/" + fileName;
        File file = new File(path);
        return Files.readAllBytes(Path.of(file.getAbsolutePath()));
    }

}
