/*
 * Copyright (c) 2020 The Web eID Project
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.webeid.security.nonce;

import javax.cache.Cache;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Base64;

final class NonceGeneratorImpl implements NonceGenerator {

    private final Cache<String, LocalDateTime> cache;
    private final SecureRandom secureRandom;
    private final Duration ttl;

    NonceGeneratorImpl(Cache<String, LocalDateTime> cache, SecureRandom secureRandom, Duration ttl) {
        this.cache = cache;
        this.secureRandom = secureRandom;
        this.ttl = ttl;
    }

    @Override
    public String generateAndStoreNonce() {
        final byte[] nonceBytes = new byte[NONCE_LENGTH];
        secureRandom.nextBytes(nonceBytes);
        final LocalDateTime expirationTime = LocalDateTime.now().plus(ttl);
        final String base64StringNonce = Base64.getEncoder().encodeToString(nonceBytes);
        cache.put(base64StringNonce, expirationTime);
        return base64StringNonce;
    }
}
