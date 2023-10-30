package it.almaviva.impleme.prenotauffici.util;

import com.auth0.jwk.*;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.interfaces.RSAPublicKey;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
public class JWTUtil {

    public static Claim getClaim(String jwt, String claim){
        DecodedJWT decodedJWT =  JWT.decode(jwt);
        return decodedJWT.getClaim(claim);
    }

    public static List<String>  getClaimAsList(String jwt, String claim){
        DecodedJWT decodedJWT =  JWT.decode(jwt);
        final Claim cClaim = decodedJWT.getClaim(claim);

        List<String> list =null;
        if(cClaim.asString()!= null){
            list = Arrays.asList(cClaim.asString().split(","));
        }
        if(cClaim.asArray(String.class) != null){
            list = Arrays.asList(cClaim.asArray(String.class));
        }
        return list;
    }

    private static Jwk getRSAKeyProvider(String kid, String urlJwkProvider) throws MalformedURLException, JwkException {
        JwkProvider jwkProvider = new JwkProviderBuilder(new URL(urlJwkProvider))
                .cached(10,24, TimeUnit.HOURS)
                .build();

        return jwkProvider.get(kid);
    }

    public static void verify(String jwt, String urlJwkProvider){
        DecodedJWT decodedJWT =  JWT.decode(jwt);
        String kid = decodedJWT.getKeyId();
        Jwk jwk;
        try {
            jwk = getRSAKeyProvider(kid, urlJwkProvider);
        } catch (MalformedURLException e) {
            log.error("MalformedURLException", e);
            throw new JWTException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (JwkException e) {
            log.error("JwkException", e);
            throw new JWTException(HttpStatus.UNAUTHORIZED, e.getMessage(), e);
        }

        if(jwk == null){
            throw new JWTException(HttpStatus.INTERNAL_SERVER_ERROR,"JWK is null");
        }

        Algorithm algorithm;
        try {
            algorithm = Algorithm.RSA256((RSAPublicKey) jwk.getPublicKey(), null);

        } catch (InvalidPublicKeyException e) {
            log.error("InvalidPublicKeyException", e);
            throw new JWTException(HttpStatus.SERVICE_UNAVAILABLE, e.getMessage(), e);
        }
        JWTVerifier verifier = JWT.require(algorithm).build();
        verifier.verify(jwt);
    }
}
