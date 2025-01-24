package com.invManagement.Backend_REST_API.models.webtoken;

import com.invManagement.Backend_REST_API.models.MyUser.MyUser;
import com.invManagement.Backend_REST_API.models.MyUser.MyUserRepository;
import com.invManagement.Backend_REST_API.models.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class JwtService {

    final
    MyUserRepository userRepository;

    @Value("${JWT_SECRET}")
    public static String SECRET;

    private static final Long VALIDITY = TimeUnit.MINUTES.toMillis(60);

    public JwtService(MyUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plusMillis(VALIDITY)))
                .signWith(generateKey())
                .compact();

    }

//    private String findUserRole(String username) {
//        Optional<MyUser> userObj = userRepository.findByUsername(username);
//        if (userObj.isPresent()){
//            MyUser user = userObj.get();
//            if (user.getRole() == Role.EMPLOYEE) {
//                return "EMPLOYEE";
//            }
//            else if (user.getRole() == Role.MANAGER) {
//                return "MANAGER";
//            }
//            return "NOROLE";
//        }
//        return "NO_USER";
//    }

    private SecretKey generateKey() {
        byte[] decodedKey = Base64.getDecoder().decode(SECRET);
        return Keys.hmacShaKeyFor(decodedKey);
    }

    public String extractUsername(String jwt) {
        Claims claims = getClaims(jwt);
        return claims.getSubject();
    }

    private Claims getClaims(String jwt) {
        return Jwts.parser()
                .verifyWith(generateKey())
                .build()
                .parseSignedClaims(jwt)
                .getPayload();
    }

    public boolean isTokenValid(String jwt) {
        Claims claims = getClaims(jwt);
        return claims.getExpiration().after(Date.from(Instant.now()));
    }
}
