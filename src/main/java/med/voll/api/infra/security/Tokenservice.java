package med.voll.api.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import med.voll.api.domain.usuario.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class Tokenservice {

    @Value("${api.security.token.secret}")
    private String secret;

    private static final String ISSUER = "API Voll.med";
    public String gerarToken(Usuario usuario){
        try {
            var algorithm = Algorithm.HMAC256(this.secret);
            return JWT.create()
                    .withIssuer(ISSUER)
                    .withSubject(usuario.getLogin())
                    .withExpiresAt(dataExpiracao())
                    .sign(algorithm);
        } catch (JWTCreationException exc){
            throw new RuntimeException("Erro ao gerar token jwt", exc);
        }
    }

    public String getSubject(String tokenJWT){
        try {
            var algorithm = Algorithm.HMAC256(this.secret);
            return JWT.require(algorithm)
                    .withIssuer(ISSUER)
                    .build().verify(tokenJWT)
                    .getSubject();
        } catch (JWTVerificationException exception){
            throw new RuntimeException("Token JWT inválido ou expirado!");
        }
    }

    private Instant dataExpiracao() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
