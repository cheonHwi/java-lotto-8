package lotto;

import camp.nextstep.edu.missionutils.test.NsTest;
import org.junit.jupiter.api.Test;

import static camp.nextstep.edu.missionutils.test.Assertions.assertSimpleTest;
import static org.assertj.core.api.Assertions.assertThat;

public class InputServiceTest extends NsTest {
    private final InputService inputService = new InputService();

    @Test
    void 입력된_숫자가_정상적으로_파싱됨() {
        assertSimpleTest(() -> {
            run("1000");
            int result = inputService.inputNumber();
            assertThat(result).isEqualTo(1000);
        });
    }

    @Test
    void 잘못된_입력이_입력되면_정상적인_입력이_올때까지_에러_출력() {
        assertSimpleTest(() -> {
            run("a", "1000a", "2000");
            int result = inputService.inputNumber();
            assertThat(output()).isEqualTo("[ERROR]" + System.lineSeparator() + "[ERROR]");
            assertThat(result).isEqualTo(2000);
        });
    }

    @Override
    protected void runMain() {
        // Service Layer만 테스트하기에 메인이 존재하지 않음.
    }
}
