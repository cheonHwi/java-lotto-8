package lotto;

import camp.nextstep.edu.missionutils.test.NsTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static camp.nextstep.edu.missionutils.test.Assertions.assertRandomUniqueNumbersInRangeTest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class LottoServiceTest extends NsTest {
    private final LottoService lottoService = new LottoService();

    @DisplayName("로또를 1개 생성하고 출력한다.")
    @Test
    void 로또_생성_검증() {
        assertRandomUniqueNumbersInRangeTest(
                () -> {
                    lottoService.getLottos(1);
                    assertThat(output()).contains(
                            "[1, 2, 3, 4, 5, 6]");
                },
                List.of(1, 2, 3, 4, 5, 6)
        );
    }

    @Test
    void 로또_1등_당첨_확인_검증() {
        Lotto lotto = new Lotto(List.of(1, 2, 3, 4, 5, 6));
        List<Integer> winningNumbers = List.of(1, 2, 3, 4, 5, 6);
        int bonusNumber = 7;

        assertEquals(
                lottoService.confirmWinning(lotto, winningNumbers, bonusNumber),
                Map.of(
                        LottoNumberMatchType.CORRECT, 6L
                )
        );
    }

    @DisplayName("6개 번호가 모두 일치하면 1등으로 판정된다.")
    @Test
    void 당첨_등수_판정_1등() {
        Lotto lotto = new Lotto(List.of(1, 2, 3, 4, 5, 6));
        List<Integer> winningNumbers = List.of(1, 2, 3, 4, 5, 6);
        int bonusNumber = 7;

        WinningRank rank = lottoService.calculateWinningRank(lotto, winningNumbers, bonusNumber);

        assertEquals(WinningRank.FIRST, rank);
    }

    @DisplayName("5개 번호와 보너스 번호가 일치하면 2등으로 판정된다.")
    @Test
    void 당첨_등수_판정_2등() {
        Lotto lotto = new Lotto(List.of(1, 2, 3, 4, 5, 7));
        List<Integer> winningNumbers = List.of(1, 2, 3, 4, 5, 6);
        int bonusNumber = 7;

        WinningRank rank = lottoService.calculateWinningRank(lotto, winningNumbers, bonusNumber);

        assertEquals(WinningRank.SECOND, rank);
    }

    @DisplayName("5개 번호가 일치하면 3등으로 판정된다.")
    @Test
    void 당첨_등수_판정_3등() {
        Lotto lotto = new Lotto(List.of(1, 2, 3, 4, 5, 10));
        List<Integer> winningNumbers = List.of(1, 2, 3, 4, 5, 6);
        int bonusNumber = 7;

        WinningRank rank = lottoService.calculateWinningRank(lotto, winningNumbers, bonusNumber);

        assertEquals(WinningRank.THIRD, rank);
    }

    @DisplayName("4개 번호가 일치하면 4등으로 판정된다.")
    @Test
    void 당첨_등수_판정_4등() {
        Lotto lotto = new Lotto(List.of(1, 2, 3, 4, 10, 11));
        List<Integer> winningNumbers = List.of(1, 2, 3, 4, 5, 6);
        int bonusNumber = 7;

        WinningRank rank = lottoService.calculateWinningRank(lotto, winningNumbers, bonusNumber);

        assertEquals(WinningRank.FOURTH, rank);
    }

    @DisplayName("3개 번호가 일치하면 5등으로 판정된다.")
    @Test
    void 당첨_등수_판정_5등() {
        Lotto lotto = new Lotto(List.of(1, 2, 3, 10, 11, 12));
        List<Integer> winningNumbers = List.of(1, 2, 3, 4, 5, 6);
        int bonusNumber = 7;

        WinningRank rank = lottoService.calculateWinningRank(lotto, winningNumbers, bonusNumber);

        assertEquals(WinningRank.FIFTH, rank);
    }

    @DisplayName("2개 이하로 일치하면 낙첨으로 판정된다.")
    @Test
    void 당첨_등수_판정_낙첨() {
        Lotto lotto = new Lotto(List.of(1, 2, 10, 11, 12, 13));
        List<Integer> winningNumbers = List.of(1, 2, 3, 4, 5, 6);
        int bonusNumber = 7;

        WinningRank rank = lottoService.calculateWinningRank(lotto, winningNumbers, bonusNumber);

        assertEquals(WinningRank.NONE, rank);
    }

    @DisplayName("여러 로또의 당첨 통계를 정확히 집계한다.")
    @Test
    void 당첨_통계_집계() {
        List<Lotto> lottos = List.of(
                new Lotto(List.of(1, 2, 3, 4, 5, 6)),    // 1등
                new Lotto(List.of(1, 2, 3, 4, 5, 7)),    // 2등
                new Lotto(List.of(1, 2, 3, 4, 5, 10)),   // 3등
                new Lotto(List.of(1, 2, 3, 4, 10, 11)),  // 4등
                new Lotto(List.of(1, 2, 3, 10, 11, 12))  // 5등
        );
        List<Integer> winningNumbers = List.of(1, 2, 3, 4, 5, 6);
        int bonusNumber = 7;

        Map<WinningRank, Long> statistics = lottoService.calculateStatistics(lottos, winningNumbers, bonusNumber);

        assertEquals(1L, statistics.get(WinningRank.FIRST));
        assertEquals(1L, statistics.get(WinningRank.SECOND));
        assertEquals(1L, statistics.get(WinningRank.THIRD));
        assertEquals(1L, statistics.get(WinningRank.FOURTH));
        assertEquals(1L, statistics.get(WinningRank.FIFTH));
    }

    @DisplayName("수익률을 정확히 계산한다.")
    @Test
    void 수익률_계산() {
        Map<WinningRank, Long> statistics = Map.of(
                WinningRank.FIFTH, 1L  // 5,000원
        );
        int purchaseAmount = 8000;

        double returnRate = lottoService.calculateReturnRate(statistics, purchaseAmount);

        assertEquals(62.5, returnRate, 0.01);
    }

    @DisplayName("로또를 여러 개 생성하면 개수만큼 반환된다.")
    @Test
    void 로또_여러개_생성() {
        assertRandomUniqueNumbersInRangeTest(
                () -> {
                    List<Lotto> lottos = lottoService.getLottos(3);
                    assertEquals(3, lottos.size());
                },
                List.of(1, 2, 3, 4, 5, 6),
                List.of(7, 8, 9, 10, 11, 12),
                List.of(13, 14, 15, 16, 17, 18)
        );
    }

    @DisplayName("당첨 번호 문자열을 정상적으로 파싱한다.")
    @Test
    void 당첨번호_파싱_정상() {
        String input = "1,2,3,4,5,6";

        List<Integer> winningNumbers = lottoService.getWinningNumbers(input);

        assertEquals(List.of(1, 2, 3, 4, 5, 6), winningNumbers);
    }

    @DisplayName("당첨 번호 파싱 시 공백이 포함되어도 정상 처리된다.")
    @Test
    void 당첨번호_파싱_공백포함() {
        String input = "1, 2, 3, 4, 5, 6";

        List<Integer> winningNumbers = lottoService.getWinningNumbers(input);

        assertEquals(List.of(1, 2, 3, 4, 5, 6), winningNumbers);
    }

    @DisplayName("로또 번호가 보너스 번호와 일치하는 경우를 확인한다.")
    @Test
    void 보너스_번호_매칭_확인() {
        Lotto lotto = new Lotto(List.of(1, 2, 3, 4, 5, 7));
        List<Integer> winningNumbers = List.of(10, 11, 12, 13, 14, 15);
        int bonusNumber = 7;

        Map<LottoNumberMatchType, Long> matchResult = lottoService.confirmWinning(lotto, winningNumbers, bonusNumber);

        assertEquals(1L, matchResult.get(LottoNumberMatchType.BONUS_NUMBER_CORRECT));
        assertEquals(5L, matchResult.get(LottoNumberMatchType.INCORRECT));
    }

    @DisplayName("당첨 번호와 일부만 일치하는 경우를 확인한다.")
    @Test
    void 일부_일치_확인() {
        Lotto lotto = new Lotto(List.of(1, 2, 3, 10, 11, 12));
        List<Integer> winningNumbers = List.of(1, 2, 3, 4, 5, 6);
        int bonusNumber = 7;

        Map<LottoNumberMatchType, Long> matchResult = lottoService.confirmWinning(lotto, winningNumbers, bonusNumber);

        assertEquals(3L, matchResult.get(LottoNumberMatchType.CORRECT));
        assertEquals(3L, matchResult.get(LottoNumberMatchType.INCORRECT));
    }

    @DisplayName("낙첨만 있는 경우 통계를 정상적으로 집계한다.")
    @Test
    void 당첨_통계_낙첨만() {
        List<Lotto> lottos = List.of(
                new Lotto(List.of(10, 11, 12, 13, 14, 15)),
                new Lotto(List.of(20, 21, 22, 23, 24, 25))
        );
        List<Integer> winningNumbers = List.of(1, 2, 3, 4, 5, 6);
        int bonusNumber = 7;

        Map<WinningRank, Long> statistics = lottoService.calculateStatistics(lottos, winningNumbers, bonusNumber);

        assertEquals(2L, statistics.getOrDefault(WinningRank.NONE, 0L));
    }

    @DisplayName("같은 등수의 당첨이 여러 개인 경우 정확히 집계한다.")
    @Test
    void 당첨_통계_같은등수_여러개() {
        List<Lotto> lottos = List.of(
                new Lotto(List.of(1, 2, 3, 10, 11, 12)),  // 5등
                new Lotto(List.of(1, 2, 3, 13, 14, 15)),  // 5등
                new Lotto(List.of(1, 2, 3, 16, 17, 18))   // 5등
        );
        List<Integer> winningNumbers = List.of(1, 2, 3, 4, 5, 6);
        int bonusNumber = 7;

        Map<WinningRank, Long> statistics = lottoService.calculateStatistics(lottos, winningNumbers, bonusNumber);

        assertEquals(3L, statistics.get(WinningRank.FIFTH));
    }

    @DisplayName("수익률이 0%인 경우를 정확히 계산한다.")
    @Test
    void 수익률_0퍼센트() {
        Map<WinningRank, Long> statistics = Map.of(
                WinningRank.NONE, 10L
        );
        int purchaseAmount = 10000;

        double returnRate = lottoService.calculateReturnRate(statistics, purchaseAmount);

        assertEquals(0.0, returnRate, 0.01);
    }

    @DisplayName("수익률이 100%를 초과하는 경우를 정확히 계산한다.")
    @Test
    void 수익률_100퍼센트_초과() {
        Map<WinningRank, Long> statistics = Map.of(
                WinningRank.FIRST, 1L  // 2,000,000,000원
        );
        int purchaseAmount = 1000;

        double returnRate = lottoService.calculateReturnRate(statistics, purchaseAmount);

        assertEquals(200_000_000.0, returnRate, 0.01);
    }

    @DisplayName("여러 등수가 섞인 경우 수익률을 정확히 계산한다.")
    @Test
    void 수익률_복합계산() {
        Map<WinningRank, Long> statistics = Map.of(
                WinningRank.FIFTH, 2L,   // 5,000 * 2 = 10,000원
                WinningRank.FOURTH, 1L   // 50,000원
        );
        int purchaseAmount = 10000;  // 10장 구매

        double returnRate = lottoService.calculateReturnRate(statistics, purchaseAmount);

        assertEquals(600.0, returnRate, 0.01);  // (10,000 + 50,000) / 10,000 * 100 = 600%
    }


    @Override
    protected void runMain() {}
}
