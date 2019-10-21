/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.wedextim.spring.boot.test.jwt;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.UUID;

public class JwtTestSupport {

    private static PrivateKey privateKey;

    public static String createToken(UUID userId, String... roles) throws Exception {
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
            .subject(userId.toString())
            .claim("cognito:groups", roles)
//            .claim("scope", "aws.cognito.signin.user.admin")
            .build();

        SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.RS256), claimsSet);
        if (null == privateKey) {
            try (InputStream is = JwtTestSupport.class.getResourceAsStream("/private.key")) {
                byte[] b = new byte[is.available()];
                is.read(b);
                privateKey = KeyFactory.getInstance("RSA")
                    .generatePrivate(new PKCS8EncodedKeySpec(Base64.getDecoder().decode(b)));
            }
        }
        JWSSigner signer = new RSASSASigner(privateKey);
        signedJWT.sign(signer);
        return signedJWT.serialize();
    }

    public static String bearerToken(final UUID userId, String... roles) throws Exception {
        return "Bearer " + JwtTestSupport.createToken(userId, roles);
    }

    private static void generateKeys() throws Exception {
        KeyPairGenerator gen = KeyPairGenerator.getInstance("RSA");
        gen.initialize(2048, new SecureRandom());
        KeyPair keyPair = gen.generateKeyPair();
        RSAKey jwk = new RSAKey.Builder((RSAPublicKey) keyPair.getPublic()).build();
        Files.write(Paths.get("src/main/resources/jwks.json"), ("{\"keys\": [" + jwk.toJSONString() + "]}").getBytes());
        Files.write(Paths.get("src/main/resources/private.key"),
            Base64.getEncoder().encode(new PKCS8EncodedKeySpec(keyPair.getPrivate().getEncoded()).getEncoded()));
    }

    public static void main(String[] args) throws Exception {
        JwtTestSupport.generateKeys();
    }
}
