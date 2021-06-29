package nextstep.subway.wrapped;

import javax.persistence.Embeddable;
import java.util.Objects;

import static java.lang.String.format;

@Embeddable
public class Money implements Comparable<Money> {
    private static final int MINIMUM_MONEY = 0;

    private int money;

    protected Money() {
        this.money = 0;
    }

    public Money(int money) {
        validate(money);

        this.money = money;
    }

    private void validate(int money) {
        if (money < MINIMUM_MONEY) {
            throw new IllegalArgumentException(format("돈은 %d원 이상이여야 합니다.", MINIMUM_MONEY));
        }
    }

    public Money plus(Money money) {
        return new Money(this.money + money.money);
    }

    public Money minus(Money money) {
        return new Money(this.money - money.money);
    }

    public Money multi(Money money) {
        return new Money(this.money * money.money);
    }

    public Money divide(Money money) {
        return new Money(this.money / money.money);
    }

    public int toInt() {
        return money;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money1 = (Money) o;
        return money == money1.money;
    }

    @Override
    public int hashCode() {
        return Objects.hash(money);
    }

    @Override
    public int compareTo(Money o) {
        return Integer.compare(this.money, o.money);
    }
}