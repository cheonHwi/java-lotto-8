package lotto;

import camp.nextstep.edu.missionutils.Console;

import java.util.List;

public class Application {
    public static void main(String[] args) {
        InputService inputService = new InputService();
        LottoService lottoService = new LottoService();

        int money;
        int bonusNumber;

        System.out.println("구입금액을 입력해 주세요.");
        money = inputService.inputNumber();

        if(money % 1000 != 0) {
            throw new IllegalArgumentException("로또 구입 금액은 1,000으로 나누어 떨어져야 합니다.");
        }

        int lottoNumbers = money / 1000;

        System.out.println(lottoNumbers + "개를 구매했습니다.");
        List<Lotto> lottos = lottoService.getLottos(lottoNumbers);


        System.out.println("당첨 번호를 입력해 주세요.");
        List<Integer> winningNumber = lottoService.getWinningNumbers(Console.readLine());

        System.out.println("보너스 번호를 입력해 주세요.");
        bonusNumber = inputService.inputNumber();
    }
}
