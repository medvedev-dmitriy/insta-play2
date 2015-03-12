package controllers;

import org.jinstagram.Instagram;
import org.jinstagram.auth.InstagramAuthService;
import org.jinstagram.auth.model.Token;
import org.jinstagram.auth.model.Verifier;
import org.jinstagram.auth.oauth.InstagramService;
import play.mvc.Result;

import static play.mvc.Results.ok;

/**
 * Created by Dimin on 11.03.2015.
 */
public class Authorization {
    private static final Token EMPTY_TOKEN = null;

    public static Result test(String code) {
        InstagramService service = new InstagramAuthService()
                .apiKey("4b3bf947e41d4bb493635920e5b34a1e")
                .apiSecret("17325634d77c4f9ba380c9c26ce07dc3")
                .callback("http://localhost:8080/handleInstagramToken/")
                .build();
        String authorizationUrl = service.getAuthorizationUrl(EMPTY_TOKEN);
        Verifier verifier = new Verifier(code);
        Token accessToken = service.getAccessToken(EMPTY_TOKEN, verifier);
        Instagram instagram = new Instagram(accessToken.getToken(), "17325634d77c4f9ba380c9c26ce07dc3", "127.0.0.1");
        return ok(views.html.home.render("Authorization is successful"));
    }
}
