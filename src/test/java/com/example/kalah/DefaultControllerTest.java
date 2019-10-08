package com.example.kalah;

import com.example.kalah.controller.DefaultController;
import com.example.kalah.entity.GameStatus;
import com.example.kalah.entity.User;
import com.example.kalah.entity.UserStep;
import com.example.kalah.service.GameService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(DefaultController.class)
public class DefaultControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameService gameService;

    private User user1;
    private UserStep userStep;
    @Before
    public void setUp() {
        user1 = new User("user1");
        userStep = new UserStep(user1, "1");
    }

    @Test
    public void connectionTest() throws Exception {
        when(gameService.getGameStatus()).thenReturn(GameStatus.STARTED);
        String inputJson = mapToJson(user1);

        mockMvc.perform(post("/react/connect")
                .contentType(MediaType.APPLICATION_JSON)
                .content(inputJson))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("Game has been started..")));
    }

    @Test
    public void moveTest() throws Exception {
        String inputJson = mapToJson(userStep);

        mockMvc.perform(post("/react/move")
                .contentType(MediaType.APPLICATION_JSON)
                .content(inputJson))
                .andDo(print()).andExpect(status().isOk());
    }

    @Test
    public void gameStatusTest() throws Exception {
        mockMvc.perform(get("/react/getGameStatus"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void disconnectionTest() throws Exception {
        when(gameService.getGameStatus()).thenReturn(GameStatus.DISCONNECTED);
        String inputJson = mapToJson(user1);

        mockMvc.perform(post("/react/disconnect")
                .contentType(MediaType.APPLICATION_JSON)
                .content(inputJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("DISCONNECTED")));
    }

    private String mapToJson(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }
}
