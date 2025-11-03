package lotto;

import camp.nextstep.edu.missionutils.Randoms;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LottoService {
    public List<Lotto> getLottos(int lottoCount) {
        List<Lotto> lottos = new ArrayList<>();
        for (int i = 0; i < lottoCount; i++) {
            Lotto lotto = new Lotto(
                    Randoms.pickUniqueNumbersInRange(1, 45, 6)
            );
            lotto.getLottoNumbers();
            lottos.add(lotto);
        }
        
        return lottos;
    }

    public List<Integer> getWinningNumbers(String userInputWinningNumbers) {
        List<Integer> winningNumbers = new ArrayList<>();

        String[] tokenizedWinningNumbers = userInputWinningNumbers.split(",");
        for(String winningNumber: tokenizedWinningNumbers) {
            try {
                winningNumbers.add(Integer.parseInt(winningNumber.trim()));
            }  catch (NumberFormatException e) {
                System.out.println("[ERROR]");
                return getWinningNumbers(userInputWinningNumbers);
            }
        }
        return winningNumbers;
    }

    public Map<LottoNumberMatchType, Long> confirmWinning(Lotto lotto, List<Integer> winningNumbers, int bonusNumber) {
        List<LottoNumberMatchType> numberMatchResult = lotto.confirmWinningNumbers(winningNumbers, bonusNumber);

        return numberMatchResult.stream()
                .collect(Collectors.groupingBy(type -> type, Collectors.counting()));
    }

    public WinningRank calculateWinningRank(Lotto lotto, List<Integer> winningNumbers, int bonusNumber) {
        Map<LottoNumberMatchType, Long> matchResult = confirmWinning(lotto, winningNumbers, bonusNumber);

        int correctCount = matchResult.getOrDefault(LottoNumberMatchType.CORRECT, 0L).intValue();
        boolean hasBonusMatch = matchResult.getOrDefault(LottoNumberMatchType.BONUS_NUMBER_CORRECT, 0L) > 0;

        return WinningRank.getRank(correctCount, hasBonusMatch);
    }

    public Map<WinningRank, Long> calculateStatistics(List<Lotto> lottos, List<Integer> winningNumbers, int bonusNumber) {
        return lottos.stream()
                .map(lotto -> calculateWinningRank(lotto, winningNumbers, bonusNumber))
                .collect(Collectors.groupingBy(rank -> rank, Collectors.counting()));
    }

    public double calculateReturnRate(Map<WinningRank, Long> statistics, int purchaseAmount) {
        long totalPrize = statistics.entrySet().stream()
                .mapToLong(entry -> entry.getKey().getPrize() * entry.getValue())
                .sum();

        return (double) totalPrize / purchaseAmount * 100;
    }

    public void printStatistics(Map<WinningRank, Long> statistics, double returnRate) {
        System.out.println("\n당첨 통계");
        System.out.println("---");

        DecimalFormat prizeFormat = new DecimalFormat("#,###");
        printRankResult(WinningRank.FIFTH, statistics, prizeFormat);
        printRankResult(WinningRank.FOURTH, statistics, prizeFormat);
        printRankResult(WinningRank.THIRD, statistics, prizeFormat);
        printRankResult(WinningRank.SECOND, statistics, prizeFormat);
        printRankResult(WinningRank.FIRST, statistics, prizeFormat);

        System.out.printf("총 수익률은 %.1f%%입니다.%n", returnRate);
    }

    private void printRankResult(WinningRank rank, Map<WinningRank, Long> statistics, DecimalFormat format) {
        long count = statistics.getOrDefault(rank, 0L);
        String prizeFormatted = format.format(rank.getPrize());
        System.out.printf("%s (%s원) - %d개%n", rank.getDescription(), prizeFormatted, count);
    }
}
