package nextstep.subway.line.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;

import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_생성_요청;
import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_조회_요청;
import static nextstep.subway.station.StationAcceptanceTest.*;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DisplayName("지하철 구간 인수 테스트")
public class LineSectionAcceptanceTest extends AcceptanceTest {
    private LineResponse 신분당선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 판교역;
    private StationResponse 정자역;
    private StationResponse 광교역;

    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = 지하철역_생성_요청("강남역").as(StationResponse.class);
        양재역 = 지하철역_생성_요청("양재역").as(StationResponse.class);
        판교역 = 지하철역_생성_요청("판교역").as(StationResponse.class);
        정자역 = 지하철역_생성_요청("정자역").as(StationResponse.class);
        광교역 = 지하철역_생성_요청("광교역").as(StationResponse.class);

        LineRequest lineRequest = new LineRequest("신분당선", "bg-red-600", 0, 양재역.getId(), 정자역.getId(), 10);
        신분당선 = 지하철_노선_생성_요청(lineRequest).as(LineResponse.class);
    }

    @DisplayName("지하철 구간 정상 기능")
    @Test
    void normalScenario() {
        지하철_구간_등록됨(지하철_구간_등록_요청(신분당선, 정자역, 광교역, 3));
        지하철_구간_등록됨(지하철_구간_등록_요청(신분당선, 양재역, 판교역, 3));
        지하철_구간_등록됨(지하철_구간_등록_요청(신분당선, 강남역, 양재역, 3));

        지하철_노선에_지하철역_정렬됨(지하철_노선_조회_요청(신분당선), Arrays.asList(강남역, 양재역, 판교역, 정자역, 광교역));

        지하철_노선에_지하철역_제외됨(지하철_노선에_지하철역_제외_요청(신분당선, 강남역));
        지하철_노선에_지하철역_제외됨(지하철_노선에_지하철역_제외_요청(신분당선, 판교역));

        지하철_노선에_지하철역_정렬됨(지하철_노선_조회_요청(신분당선), Arrays.asList(양재역, 정자역, 광교역));
    }

    @DisplayName("지하철 구간 예외 발생")
    @Test
    void exceptionScenario() {
        // 노선에 이미 등록되어 있는 역을 등록 요청
        지하철_노선에_지하철역_등록_실패됨(지하철_구간_등록_요청(신분당선, 양재역, 정자역, 3));

        // 노선과 연결되지 않는 역을 등록 요청
        지하철_노선에_지하철역_등록_실패됨(지하철_구간_등록_요청(신분당선, 강남역, 판교역, 3));

        지하철_노선에_지하철역_정렬됨(지하철_노선_조회_요청(신분당선), Arrays.asList(양재역, 정자역));

        // 등록된 지하철역이 두개일 때 지하철 구간 삭제 요청
        지하철_노선에_지하철역_제외_실패됨(지하철_노선에_지하철역_제외_요청(신분당선, 정자역));

        지하철_노선에_지하철역_정렬됨(지하철_노선_조회_요청(신분당선), Arrays.asList(양재역, 정자역));
    }

    public static ExtractableResponse<Response> 지하철_구간_등록_요청(LineResponse line, StationResponse upStation, StationResponse downStation, int distance) {
        SectionRequest request = new SectionRequest(upStation.getId(), downStation.getId(), distance);
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/lines/{lineId}/sections", line.getId())
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선에_지하철역_제외_요청(LineResponse line, StationResponse station) {
        return RestAssured
                .given().log().all()
                .when().delete("/lines/{lineId}/sections?stationId={stationId}", line.getId(), station.getId())
                .then().log().all()
                .extract();
    }

    public static void 지하철_구간_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 지하철_구간_등록됨(LineResponse line, StationResponse upStation, StationResponse downStation, int distance) {
        ExtractableResponse<Response> response = 지하철_구간_등록_요청(line, upStation, downStation, distance);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 지하철_노선에_지하철역_등록_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static void 지하철_노선에_지하철역_정렬됨(ExtractableResponse<Response> response, List<StationResponse> excepted) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getList("stations.name"))
                .isEqualTo(getStationNames(excepted));
    }

    public static void 지하철_노선에_지하철역_제외됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 지하철_노선에_지하철역_제외_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
