package com.multimidia.projeto.trabalho_final.modules.shared;

import lombok.Data;

@Data
public class AuthenticationRequest {
    private String nickname;
    private String password;
}
