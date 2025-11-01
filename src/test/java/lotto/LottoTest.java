package lotto;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

class LottoTest {
    final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    final PrintStream standardOut = System.out;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @AfterEach
    void tearDown() {
        System.setOut(standardOut);
    }


    @Test
    void 로또_번호의_개수가_6개가_넘어가면_예외가_발생한다() {
        assertThatThrownBy(() -> new Lotto(List.of(1, 2, 3, 4, 5, 6, 7)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("로또 번호에 중복된 숫자가 있으면 예외가 발생한다.")
    @Test
    void 로또_번호에_중복된_숫자가_있으면_예외가_발생한다() {
        assertThatThrownBy(() -> new Lotto(List.of(1, 2, 3, 4, 5, 5)))
                .isInstanceOf(IllegalArgumentException.class);
    }


    @Test
    void 로또가_생성될때_로또_번호를_출력한다() {
        String correctOutput = "[1, 2, 3, 4, 5, 6]";

        new Lotto(List.of(1, 2, 3, 4, 5, 6)).getLottoNumbers();

        assertEquals(correctOutput, outputStreamCaptor.toString().trim());
    }


    // 번호 하나하나 검증하는 함수는 private로 작성되어 검증 함수를 실제로 사용하는 confirmWinningNumbers로 테스트한다.
    @Test
    void 로또_번호_검증() {
        Lotto lotto = new Lotto(List.of(1, 2, 3, 10, 11, 7));
        List<Integer> winningNumbers = List.of(1, 2, 3, 4, 5, 6);
        int bonusNumber = 7;

        List<LottoNumberMatchType> matchResult = lotto.confirmWinningNumbers(winningNumbers, bonusNumber);
        List<LottoNumberMatchType> correctMatchResult = List.of(LottoNumberMatchType.CORRECT,
                LottoNumberMatchType.CORRECT,
                LottoNumberMatchType.CORRECT,
                LottoNumberMatchType.INCORRECT,
                LottoNumberMatchType.INCORRECT,
                LottoNumberMatchType.BONUS_NUMBER_CORRECT);

        assertEquals(correctMatchResult, matchResult);
    }

    // TODO: 추가 기능 구현에 따른 테스트 코드 작성
}
