package org.cm.test.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.repository.CrudRepository;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
public abstract class BaseServiceUnitTest {
    private static final Logger log = LoggerFactory.getLogger(BaseServiceUnitTest.class);

    @BeforeEach
    void setUp() {
        mockRepositorySaveMethods();
    }

    private void mockRepositorySaveMethods() {
        try {
            var repositories = getTypedDeclaredFields(CrudRepository.class)
                .stream()
                .filter(field -> Mockito.mockingDetails(field).isMock())
                .map(field -> (CrudRepository<?, ?>) field)
                .toList();
            repositories.forEach((repo) -> {
                // use lenient to avoid unnecessary stubbing warnings
                lenient().when(repo.save(Mockito.any()))
                    .thenAnswer((invocation) -> invocation.getArgument(0));
            });
        } catch (Exception e) {
            log.error("Failed to mock repository save methods. :{}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("SameParameterValue")
    private <R> List<R> getTypedDeclaredFields(Class<?> clazz) {
        return Arrays.stream(this.getClass().getDeclaredFields())
            .peek(field -> field.setAccessible(true))
            .filter(field -> isInterfaceAssignableFrom(field.getType(), clazz))
            .map((field) -> {
                try {
                    //noinspection unchecked
                    return (R) field.get(this);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            })
            .toList();
    }

    private boolean isInterfaceAssignableFrom(Class<?> clazz, Class<?> target) {
        return clazz.isAssignableFrom(target) || Arrays.stream(clazz.getInterfaces()).anyMatch(i -> isInterfaceAssignableFrom(i, target));
    }
}
