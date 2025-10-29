package lotto;

import camp.nextstep.edu.missionutils.Console;

public class InputService {
    public int inputNumber() {
        try {
            return Integer.parseInt(Console.readLine());
        } catch (NumberFormatException e) {
            System.out.println("[ERROR]");
            return inputNumber();
        }
    }
}
