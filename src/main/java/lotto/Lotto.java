package lotto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class Lotto {
    private final List<Integer> numbers;

    public Lotto(List<Integer> numbers) {
        validate(numbers);
        this.numbers = numbers;
    }

    private void validate(List<Integer> numbers) {
        if (numbers.size() != 6) {
            throw new IllegalArgumentException("[ERROR] 로또 번호는 6개여야 합니다.");
        }
        // 중복된 숫자 여부 확인
        if(numbers.stream().distinct().toList().size() != 6) {
            throw new IllegalArgumentException("[ERROR] 로또 번호는 중복될 수 없습니다.");
        };
    }

    public void getLottoNumbers() {
        String winnerString = numbers.stream()
                .map(integer -> Integer.toString(integer))
                .collect(Collectors.joining(", "));

        System.out.println("[" + winnerString + "]");
    }

    public List<LottoNumberMatchType> confirmWinningNumbers(List<Integer> winningNumbers, int bonusNumber) {
        List<LottoNumberMatchType> numberMatchResults = new ArrayList<>();

        numbers.forEach(number -> {
            numberMatchResults.add(
                    checkNumber(number, winningNumbers, bonusNumber)
            );
        });
        return numberMatchResults;
    }

    private LottoNumberMatchType checkNumber(int myNumber, List<Integer> winningNumbers, int bonusNumber) {
        if(myNumber == bonusNumber) {
            return LottoNumberMatchType.BONUS_NUMBER_CORRECT;
        }
        else if(winningNumbers.contains(myNumber)) {
            return LottoNumberMatchType.CORRECT;
        }
        return LottoNumberMatchType.INCORRECT;
    }
}
