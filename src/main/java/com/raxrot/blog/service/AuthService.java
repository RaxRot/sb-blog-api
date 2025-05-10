package com.raxrot.blog.service;

import com.raxrot.blog.dto.LoginDTO;
import com.raxrot.blog.dto.RegisterDTO;

public interface AuthService {
    String login(LoginDTO loginDTO);
    String register(RegisterDTO registerDTO);
}
