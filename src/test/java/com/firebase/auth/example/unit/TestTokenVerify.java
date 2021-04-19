package com.firebase.auth.example.unit;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.firebase.auth.example.core.jwt.JwtTokenVerify;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestTokenVerify {

	public TestTokenVerify() {
	}

	@Test
	public void testVerifyToken() throws Exception {
		String jwtToken = "eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiIyNmFkNDEzZC04OTViLTRhMzUtYWI3OC05OWU0MGM3MWU2MDIiLCJpYXQiOjE2MTg1NjUzMTQsImV4cCI6MTY1MDEwMTMxNH0.MxyoqTN2Vjc488LJi4s5VRx9yMDwTNf98OhjOhMewAcCNHV9Omm3AF1ylSJClSBPBFjnZ_Ov2i0aeB5NiEkNAktgSs1_fE7o-nwXC3LxzRXXSaXcNHJFp-bVJLXOIF9M3_j9zqplBIeEkIoxi8GFLZruAuhY_1VlcMkE5vOfxEgTs1bfDWSrzdLwdsn4BlND4UwZfKYFkscUKBWQybHqk0TWxL0ODiXtel2vL3hJ6954vmUdJhuS8BAOBpXxBPoEbort9vxict0DfPRpe5OXjxryDRgs2V_oLFkUsRIhuw9l84Yue1TQyrZ2R7Pxi0scPAX5Igc8hvY5bNDeB6IrLQ";
		JwtTokenVerify verifyTokenInstance = JwtTokenVerify.getInstance();
		String base64PublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAn50c8PSdxKYqv4jW5V9wjdDMZr5xZhVDlFNrwVVb170ZWLqKUvado9bnJFad+i1Bsvyhsqln8vHCCAxGPDSrbw8AffrES8JBw1xb6p2QyBtr+GqS7k4S8EMgOY1Cw0RmBLUxyfTd0Rwg3oeOvSGJRcYdcQEy114eIL4gfYV3Hbd9TvArtB0cynkPTR6Q8KzEbWR8PJ7rwhKZyxDbpUTVjKw6mcbKrSMcRD5lvgsZVZk1uweDi30j7haD/IjoCgv9UfcYETRI2QSwW5g472Pd7tR4cmEqWnJC99NleNzdQK5iG4rL9+06PePgqJq05+I5Ed+XlPfzLK8ukE1I8rv0kwIDAQAB";
		byte[] rawPublicKey = Base64.getDecoder().decode(base64PublicKey);
		X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(rawPublicKey);
		PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(publicKeySpec);
		Map<String, Object> claims = verifyTokenInstance.verifyToken(publicKey, jwtToken);
		claims.forEach((key, value) -> {
			log.info("{} : {}", key, value);
		});
	}

	@Test
	public void testVerifyTokenInvalidPublicKey() throws Exception {
		String jwtToken = "eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiIxNTQxZGNmMC02NTg3LTRiNjMtOWMzMy1kZDhlMDMyNzEyMWMiLCJpYXQiOjE2MTg1NjYzNTMsImV4cCI6MTYxODU2NjY1M30.MmX-h0NQHUw0BlmZogP1n0Sin1u4Fl1RrqRtLl5rqoTrAslhOT0uUbL_CgQ0uwG7kkpRuhF45E8oozFyZS7HRiQ8ZA2FRv6rS4IHa4Q4UV3ZhoQJXOt_ngc0qOKr9Zhl9v8ksn8FvF74jWLT_LuyVl7H03XrPzcxP46VrnPecdLjjgPCCeML6KgtKashjQCUl4Pji9qYEsXVuBwz9g44lQ307fy219BmfRtBU2_f-YuW6F4aO7iL_OlKByULUyjbl9iLAhGduRGU-Yaf_hXBmlWH_bDO2rkQPPo8D2HSVGpZccaeAD6W6RwRbEN7oTM3UM8d544R1aSDUz1LP6Nk7FqmdNTpLZvlRwBdg0CHWujcR55LjweFnfVPdLV8exloeQZU1cdNzUUZENfiQri5Qf4SNMcoZD-jcMLTDJXerAD-yyv2UDBIrGcvE67xxQO2E7x1KDl6C2USDaIDPv_KoaqxeSjjwVjzMWpcRJpgzDEtFzTlv4iKLL_aRyUYm8BKzT-Wcqa2bMxZrbkXeGvPhrlkvRMX2mzEzl-MY-KK4D8VXJtFC5p_9EPyKWLpYvRX8N-PYdKZ51knrl1LW4D8F9M-ZPfx3cfre6YTIvIgTeh5yp6iwc8Bt0C-yOUVh-gNbnZbHYNMF2uSzQ-VeeH8KxLaD9NTKu-dE4rjN8ot8lI";
		String base64PublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAk+7GkoLdoz01ks7GLJ0FZag10f4CvDacygYBDD8F+LATK5p4kx0hpyr0zroGXXYCCHt9il+iiEYTJB8K0OTLcSLyx4wD11juCzjE0XaXZuFvG1NpMJn8NWFKYGw1ueoxPWg+mKqREBUevuYVoWXp9idlDQzH8mmYt1H24IaYAGfm7LTScaERAyEqQBlLX8/mepMVA9tQKReAqUT5xzAs/8huZp63AV/XLSTBFkJ0hsRqD73X35p3bjS9optOUwhirtc1dVVWkGRjT45FBHIJDjtu0c8c84TgKLHjheuqRlDChGFj77DRfr8A2GEhK5bSyYueVHHlTKAFe6gVjpI5AwIDAQAB";
		byte[] rawPublicKey = Base64.getDecoder().decode(base64PublicKey);
		X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(rawPublicKey);
		PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(publicKeySpec);
		Throwable error = assertThrows(RuntimeException.class, () -> {
			JwtTokenVerify.getInstance().verifyToken(publicKey, jwtToken);
		}, "Must throw exeption because invalid public key.");
		log.debug(error.getMessage(), error);
		log.info("Error message: {}", error.getMessage());
	}

	@Test
	public void testVerifyTokenExpiry() throws Exception {
		String jwtToken = "eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiIzM2ZiZjY3ZS04ZmYwLTRmOTctODY5Zi0xNjJkYWU5NTBmY2QiLCJpYXQiOjE2MTg1NjU4NjUsImV4cCI6MTYxODU2NjE2NX0.fA3fFatbjJzeGtmqCiaqEz39PK3y3RKc1OuyaVN8hJfKcKbUMm-dMbrtmYILpF-wjnnO_eC3D6qZznjvF1lTWtu6qpLa_JRX6GjdyO5a8_GgMH5AKU-2gWj0DivryCStUxILIGlRlPE2zBHu_EU9TcOI0GBHC7yiIjsYzfoGEi3phVXtHLkdgXUVWLwtzBlrm6t7emxPG3ktnVmWJEEN0aAVWzyq9jhs6dleLmgQxYds8rbxYsE0eAjcUsXfg-ZGXrXtYsgBis-Jv9K53J8ZR2uk44wa8LwGP4nhnRfm595UTUke6hXGTqhKFPRj-sZrzcs0FqyDJgBBIcV8oaDSsv0QrF7TVIpz0Jr9KfTy5ejei1pLWg10zyKs7RJrHHvCtFfowJ9VR2RxB1UX7LeL5uEvn6eQzzAOhgvOMq2eeBSgaUmSu1PhjcYpMEWUlufJMnx6MezOd2HTV1j8BjTLlnYswfDPfBP7ayRoNXmwCdQTwpQb2ShT6lEkBiFgMt_zp2cvXomEDEt9fjS1txfhPpgXdr9rB7_jO6uTL4ept0gdb6cM6Rfp0lXaUp_Ewcb79aPtJQxrH8wQZmFyAa_KL92mDfbMhS0nkMtGxM2GlPp_Y97rzytb-d9gIdBkyVBcVtD2YiDQ_NFUgyXOepr8IHmwMQYx_DmEiCzAoBCQ0bg";
		String base64PublicKey = "MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEAkj/kyxeosaUKjydslyvozhnLw9MayMhzWo2AN2/IjI3uOie0GwggEUq58OKtCge0QitILRvMMCqHBsFC7FY3CMGBOO1/PcdGV/+HqG0NyhlGDijfI1OG69royENpek2oBf8o8w8sED36acH1jxaa0kpJOUlyrLxAa7wrc6i5sfJMdq6VYxNkfN89FJPWkLny3+xwFsWSlGcIzIJGkmYJb10LQzmPxF4YGeLGs7Xm6vjpwzCxhh6f6Ll252A8mv94sHE4y6IfscBpq59aleEHHOvhXRUddOGS+dVfrUe4Fn/ov+5G8Ujcx4DNMeXKMGaPLQm+lV4Ds+IEvSwna0OWrzSAG3qBB0dF8erqw7hk4/PFkaT9SC794lFvV0aAZUCHIGXBBCfOEHvpIdRl1TxbLI+zmOlX3OIi/q3CruTSheYMiOoDVO0PpK9szcgUa/0GYTYky5n4ruhTq3jmaPZkjNQznI/UTAChz/18UvBkOaXklA73JA3W3+QKfeKka3PqSqYOgB57YV/m0nXgMfGGgkTgdOZlWFp2PY4FqwpkXUJtw+4UPvY2OCu1pILahW1DUY25EVSrZ73DvYqyhefd2CGSzVFMZ+/yL6qqyOYhYcGS5GlyhUZWM2IlbTcfTDpxzIwh7P+iDDQg9YMWAEK3dSEDuwe3gQHow9aiy53bdiMCAwEAAQ==";
		byte[] rawPublicKey = Base64.getDecoder().decode(base64PublicKey);
		X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(rawPublicKey);
		PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(publicKeySpec);
		Throwable error = assertThrows(RuntimeException.class, () -> {
			JwtTokenVerify.getInstance().verifyToken(publicKey, jwtToken);
		}, "Must throw exeption because token had expiried.");
		log.debug(error.getMessage(), error);
		log.info("Error message: {}", error.getMessage());
	}

}
