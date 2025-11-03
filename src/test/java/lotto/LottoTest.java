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


    @DisplayName("로또 번호의 개수가 6개가 넘어가면 예외가 발생한다.")
    @Test
    void 로또_번호의_개수가_6개가_넘어가면_예외가_발생한다() {
        assertThatThrownBy(() -> new Lotto(List.of(1, 2, 3, 4, 5, 6, 7)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("로또 번호의 개수가 6개 미만이면 예외가 발생한다.")
    @Test
    void 로또_번호의_개수가_6개_미만이면_예외가_발생한다() {
        assertThatThrownBy(() -> new Lotto(List.of(1, 2, 3, 4, 5)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("로또 번호가 비어있으면 예외가 발생한다.")
    @Test
    void 로또_번호가_비어있으면_예외가_발생한다() {
        assertThatThrownBy(() -> new Lotto(List.of()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("로또 번호에 중복된 숫자가 있으면 예외가 발생한다.")
    @Test
    void 로또_번호에_중복된_숫자가_있으면_예외가_발생한다() {
        assertThatThrownBy(() -> new Lotto(List.of(1, 2, 3, 4, 5, 5)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("로또가 생성될 때 로또 번호를 올바른 형식으로 출력한다.")
    @Test
    void 로또가_생성될때_로또_번호를_출력한다() {
        String correctOutput = "[1, 2, 3, 4, 5, 6]";

        new Lotto(List.of(1, 2, 3, 4, 5, 6)).getLottoNumbers();

        assertEquals(correctOutput, outputStreamCaptor.toString().trim());
    }

    @DisplayName("로또 번호가 당첨 번호 및 보너스 번호와 정확히 매칭된다.")
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

    @DisplayName("로또 번호가 당첨 번호와 하나도 일치하지 않는 경우를 확인한다.")
    @Test
    void 로또_번호가_당첨번호와_하나도_일치하지_않는_경우() {
        Lotto lotto = new Lotto(List.of(10, 11, 12, 13, 14, 15));
        List<Integer> winningNumbers = List.of(1, 2, 3, 4, 5, 6);
        int bonusNumber = 7;

        List<LottoNumberMatchType> matchResult = lotto.confirmWinningNumbers(winningNumbers, bonusNumber);
        List<LottoNumberMatchType> correctMatchResult = List.of(
                LottoNumberMatchType.INCORRECT,
                LottoNumberMatchType.INCORRECT,
                LottoNumberMatchType.INCORRECT,
                LottoNumberMatchType.INCORRECT,
                LottoNumberMatchType.INCORRECT,
                LottoNumberMatchType.INCORRECT);

        assertEquals(correctMatchResult, matchResult);
    }

    @DisplayName("로또 번호가 당첨 번호와 모두 일치하는 경우를 확인한다.")
    @Test
    void 로또_번호가_당첨번호와_모두_일치하는_경우() {
        Lotto lotto = new Lotto(List.of(1, 2, 3, 4, 5, 6));
        List<Integer> winningNumbers = List.of(1, 2, 3, 4, 5, 6);
        int bonusNumber = 7;

        List<LottoNumberMatchType> matchResult = lotto.confirmWinningNumbers(winningNumbers, bonusNumber);
        List<LottoNumberMatchType> correctMatchResult = List.of(
                LottoNumberMatchType.CORRECT,
                LottoNumberMatchType.CORRECT,
                LottoNumberMatchType.CORRECT,
                LottoNumberMatchType.CORRECT,
                LottoNumberMatchType.CORRECT,
                LottoNumberMatchType.CORRECT);

        assertEquals(correctMatchResult, matchResult);
    }

    @DisplayName("로또 번호가 보너스 번호만 일치하는 경우를 확인한다.")
    @Test
    void 로또_번호가_보너스번호만_일치하는_경우() {
        Lotto lotto = new Lotto(List.of(7, 10, 11, 12, 13, 14));
        List<Integer> winningNumbers = List.of(1, 2, 3, 4, 5, 6);
        int bonusNumber = 7;

        List<LottoNumberMatchType> matchResult = lotto.confirmWinningNumbers(winningNumbers, bonusNumber);
        List<LottoNumberMatchType> correctMatchResult = List.of(
                LottoNumberMatchType.BONUS_NUMBER_CORRECT,
                LottoNumberMatchType.INCORRECT,
                LottoNumberMatchType.INCORRECT,
                LottoNumberMatchType.INCORRECT,
                LottoNumberMatchType.INCORRECT,
                LottoNumberMatchType.INCORRECT);

        assertEquals(correctMatchResult, matchResult);
    }
    // TODO: 추가 기능 구현에 따른 테스트 코드 작성
}
