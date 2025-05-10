package com.raxrot.blog.service;

import com.raxrot.blog.dto.LoginDTO;

public interface AuthService {
    String login(LoginDTO loginDTO);
}
