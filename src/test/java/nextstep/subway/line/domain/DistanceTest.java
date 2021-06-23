package nextstep.subway.line.domain;

import static nextstep.subway.line.domain.Distance.INVALID_DISTANCE;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DistanceTest {
    private Distance distance;

    @BeforeEach
    void setup() {
        distance = new Distance(5);
    }

    @DisplayName("값 확인")
    @Test
    void create() {
        //given
        //when
        //then
        assertThat(distance.equals(new Distance(5))).isTrue();
    }

    @DisplayName("거리 차이 구하기")
    @Test
    void diff() {
        //given
        //when
        Distance request = new Distance(4);
        Distance result = distance.diff(request);
        //then
        assertThat(result.equals(new Distance(1))).isTrue();
    }

    @DisplayName("거리 합 구하기")
    @Test
    void add() {
        //given
        //when
        Distance request = new Distance(4);
        Distance result = distance.add(request);
        //then
        assertThat(result.equals(new Distance(9))).isTrue();
    }

    @DisplayName("거리가 0이하로 생성")
    @Test
    void createFailed() {
        //given
        //when
        //then
        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> new Distance(-10)).withMessage(INVALID_DISTANCE);
    }
}