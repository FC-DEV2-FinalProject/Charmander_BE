package org.cm.test.config;

import org.cm.jwt.JwtConfig;
import org.cm.jwt.JwtService;
import org.cm.web.resolver.AuthUserResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

@Import({
    AuthUserResolver.class,
    JwtConfig.class,
    JwtService.class,
})
public abstract class BaseWebMvcUnitTest {
    @Autowired
    protected MockMvc mvc;
}
