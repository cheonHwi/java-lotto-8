package lotto;


import camp.nextstep.edu.missionutils.Randoms;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class Application {
    public static void main(String[] args) {
        InputService inputService = new InputService();

        System.out.println("구입금액을 입력해 주세요.");
        int money = inputService.inputNumber();

        if(money % 1000 != 0) {
            throw new IllegalArgumentException("로또 구입 금액은 1,000으로 나누어 떨어져야 합니다.");
        }

        int lottoNumbers = money / 1000;
        List<Lotto> lottos = new ArrayList<>();

        System.out.println(lottoNumbers + "개를 구매했습니다.");
        IntStream.range(0, lottoNumbers)
                .forEach(index -> {
                    Lotto lotto = new Lotto(
                            Randoms.pickUniqueNumbersInRange(1, 45, 6)
                    );
                    lotto.getLottoNumbers();
                    lottos.add(lotto);
                });
    }
}
