package nextstep.subway.auth.acceptance;

import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.member.dto.MemberRequest;

import static nextstep.subway.member.MemberFixture.*;


public class AuthRequestFixture {
    public static final MemberRequest 성인_계정 = new MemberRequest("A@A.com", "qwe123", 30);
    public static final MemberRequest 청소년_계정 = new MemberRequest("B@B.com", "qwe123", 13);
    public static final MemberRequest 어린이_계정 = new MemberRequest("C@C.com", "qwe123", 6);

    public static final TokenRequest 성인_계정_토큰 = new TokenRequest("A@A.com", "qwe123");
    public static final TokenRequest 청소년_계정_토큰 = new TokenRequest("B@B.com", "qwe123");
    public static final TokenRequest 어린이_계정_토큰 = new TokenRequest("C@C.com", "qwe123");

    public static final TokenRequest 등록된_계정_토큰_요청 = new TokenRequest(EMAIL, PASSWORD);
    public static final TokenRequest 등록되지_않은_계정_토큰_요청 = new TokenRequest(NEW_EMAIL, NEW_PASSWORD);
}