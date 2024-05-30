package com.hampus.authenticate_and_authorize.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
public class MyControllerTest
{
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testHomePageAsAnyone()throws Exception{
        mockMvc.perform(get("/home")).andExpect(status().isOk());
    }

    @Test
    public void testUserPageAsAnyone() throws Exception{
        mockMvc.perform(get("/user/home")).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "test", password = "lmao", roles = "USER")
    public void testUserPageAsUser() throws Exception
    {
        mockMvc.perform(get("/user/home")).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "test", password = "lmao", roles = "USER")
    public void testAdminPageAsUser() throws Exception{
        mockMvc.perform(get("/admin/home")).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", password = "pass", roles = {"ADMIN", "USER"})
    public void testAdminPageAsAdmin() throws Exception {
        mockMvc.perform(get("/admin/home")).andExpect(status().isOk());
    }

    @Test
    public void testRegisterUser() throws Exception {
        mockMvc.perform(get("/register")
                .param("username", "dada")
                .param("password", "dinmamma")
                .param("email", "league@of.legends"))
                .andExpect(status().isOk())
                .andExpect(view().name("Register"));
    }
}
