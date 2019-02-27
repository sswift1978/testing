package steve.framework.extensions;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import steve.framework.annotations.UserName;
import steve.framework.utils.constants.TestFrameworkConstants;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SystemPropertyExtension.class)
class SystemPropertyExtensionTest {

    @UserName
    String userName;

    final static String EXPECTED_USERNAME = "steve";

    @BeforeAll
    static void init() {
        System.setProperty(TestFrameworkConstants.USER_NAME, EXPECTED_USERNAME);
    }

    @Test
    void userNameFieldShouldNotBeBlank() {
        assertThat(userName).isNotBlank();
    }

    @Test
    void userNameFieldShouldBeSet() {
        assertThat(userName).isEqualTo(EXPECTED_USERNAME);
    }

    @Test
    void userNameParameterShouldBeSet(@UserName String userName) {
        assertThat(userName).isEqualTo(EXPECTED_USERNAME);
    }

}