package com.promptlabs.backend_for_frontend.service;

import com.promptlabs.backend_for_frontend.dto.SuperRegistroDTO;
import com.promptlabs.backend_for_frontend.dto.UserResponse;
import com.promptlabs.backend_for_frontend.utils.JwtService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.UUID;

@Service
@AllArgsConstructor
public class RegistroService {
    private final AuthService authService;
    private final UserService userService;
    private final JwtService jwtService;

    public Map<String, Object> registrar(SuperRegistroDTO superRequest, String headerDeviceId) {

        Map<String, Object> authRes = authService.registrarUsuario(superRequest, headerDeviceId);

        String token = (String) authRes.get("accessToken");

        String bearerToken = "Bearer " + token;

        Map<String, Object> claims = jwtService.extraerPayloadDelToken(token);
        UUID userId= UUID.fromString((String) claims.get("userId"));
        String rol= jwtService.extraerRol(claims);

        UserResponse userRes= userService.completarPerfilBase(userId,superRequest.personal());
        String profileRes= null;
        if (superRequest.profile()!= null){
            profileRes = userService.completarPerfilEspecifico(bearerToken,userId,superRequest.profile());
        }
        Map<String, Object> resFinal =
                new java.util.HashMap<>(authRes);

        resFinal.put("perfilCompletado", true);
        resFinal.put("rolAsignado", rol);
        resFinal.put("userData", userRes);

        if (profileRes != null) {
            resFinal.put("profileData", profileRes);
        }

        return resFinal;
    }


}