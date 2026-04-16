package com.promptlabs.autenticacion_seguridad.service;

import com.promptlabs.autenticacion_seguridad.security.SecurityCredential;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.*;
import java.util.*;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final PrivateKey privateKey;
    private final PublicKey publicKey;

    @Value("${jwt.expiration.access-token:900000}") // 15 minutos por defecto en milisegundos.
    private Long jwtExpiration;

    /**
     * Constructor: Genera el par de llaves RSA al arrancar el microservicio.
     */
    public JwtService() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            this.privateKey = keyPair.getPrivate();
            this.publicKey = keyPair.getPublic();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error fatal: No se pudieron inicializar las llaves RSA", e); // TODO: Crear excepción personalizada.
        }
    }

    /**
     * PERMITE QUE OTROS SERVICIOS PUEDAN OBTENER LA LLAVE PÚBLICA
     */
    public String getPublicKeyAsBase64() {
        return Base64.getEncoder().encodeToString(this.publicKey.getEncoded());
    }

    /**
     * GENERA TOKEN FIRMADO CON LA LLAVE PRIVADA
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> extraClaims = new HashMap<>();

        if (userDetails instanceof SecurityCredential credential) {
            extraClaims.put("userId", credential.getId().toString());

            List<String> roles = credential.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();
            extraClaims.put("roles", roles);
        }

        return Jwts.builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(this.privateKey, Jwts.SIG.RS256)
                .compact();
    }

    /**
     * EXTRAER EL USUARIO DEL TOKEN
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * VALIDAR TOKEN
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    // --- MÉTODOS INTERNOS ---

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(this.publicKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

}
