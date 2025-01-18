package ar.edu.utn.frc.tup.lc.iv.controllers;

import ar.edu.utn.frc.tup.lc.iv.dtos.common.user.UserDto;
import ar.edu.utn.frc.tup.lc.iv.services.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    void getUsers() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setEmail("fede@fede.com");

        Mockito.when(userService.allUsers()).thenReturn(List.of(userDto));

        mockMvc.perform(get("/user/getUsers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("fede@fede.com"));
    }
}
