package kz.sdu.clubs.meetings;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class MeetingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldCreateMeetingSuccessfully() throws Exception {
        mockMvc.perform(post("/api/meetings")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "title": "Test Meeting",
                        "date": "2026-11-27",
                        "time": "07:27:00",
                        "location": "Room 204"
                    }
                    """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Test Meeting"));
    }

    @Test
    void shouldRejectMeetingWithBlankTitle() throws Exception {
        mockMvc.perform(post("/api/meetings")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "title": "",
                        "date": "2026-11-27",
                        "time": "07:27:00",
                        "location": "Room 204"
                    }
                    """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldRejectMeetingInThePast() throws Exception {
        mockMvc.perform(post("/api/meetings")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "title": "Past Meeting",
                        "date": "2020-01-01",
                        "time": "07:27:00",
                        "location": "Room 204"
                    }
                    """))
                .andExpect(status().isBadRequest());
    }
}