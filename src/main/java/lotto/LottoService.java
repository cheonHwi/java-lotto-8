package lotto;

import camp.nextstep.edu.missionutils.Randoms;

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
                winningNumbers.add(Integer.parseInt(winningNumber));
            }  catch (NumberFormatException e) {
                System.out.println("[ERROR]");
                getWinningNumbers(userInputWinningNumbers);
            }
        }
        return winningNumbers;
    }

    public Map<LottoNumberMatchType, Long> confirmWinning(Lotto lotto, List<Integer> winningNumbers, int bonusNumber) {
        List<LottoNumberMatchType> numberMatchResult = lotto.confirmWinningNumbers(winningNumbers, bonusNumber);

        return numberMatchResult.stream()
                .collect(Collectors.groupingBy(type -> type, Collectors.counting()));
    }
}
