package lotto;

import camp.nextstep.edu.missionutils.Console;

public class InputService {
    public int inputNumber() {
        int number = 0;
        try {
            String numberInput = Console.readLine();
            try {
                number = Integer.parseInt(numberInput);
            }
            catch (NumberFormatException e) {
                throw new IllegalArgumentException("input must be an integer");
            }
        }
        catch (IllegalArgumentException e) {
            System.out.println("[ERROR]");
            inputNumber();
        }

        return number;
    }
}
