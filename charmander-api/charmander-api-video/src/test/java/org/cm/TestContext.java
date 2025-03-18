package org.cm;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public abstract class TestContext {

    @Autowired
    private DatabaseCleaner cleaner;

    @BeforeEach
    void setup(){
        System.out.println("before test clean up");
        cleaner.execute();
    }

    @AfterEach
    void tearDown(){
        System.out.println("after test clean up");
        cleaner.execute();
    }
}
